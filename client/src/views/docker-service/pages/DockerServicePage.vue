<template>
  <div class="material-main material-main-flex-1">
    <AButtonBar v-model="searchText"/>
    <el-table size="mini" :data="filterDockerMappings" :default-sort="{prop: 'heathly', order: 'ascending'}">
      <el-table-column label="docker名称">
        <template slot-scope="scope">
          <span class="text-span" style="margin-left: 10px">{{ scope.row.dockerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="环境名称" sortable prop="environmentName">
        <template slot-scope="scope">
          <span class="text-span" style="margin-left: 10px">{{ scope.row.environmentName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检查命令">
        <template slot-scope="scope">
          <span class="text-span" style="margin-left: 10px">{{ scope.row.cmd }}</span>
        </template>
      </el-table-column>
      <el-table-column label="重启命令">
        <template slot-scope="scope">
          <span class="text-span" style="margin-left: 10px">{{ scope.row.restartCmd }}</span>
        </template>
      </el-table-column>
      <el-table-column label="停止命令">
        <template slot-scope="scope">
          <span class="text-span" style="margin-left: 10px">{{ scope.row.stopCmd }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="heathly">
        <template slot-scope="scope">
          <span
            :style="{marginLeft: '10px', color: scope.row.heathly ? 'green' : 'red'}"
          >{{ scope.row.heathly ? "RUNNING" : "DEAD" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280">
        <template slot="header">
          <el-button size="small" @click="openDialog">添加服务检查配置项</el-button>
        </template>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleRestart(scope.$index, scope.row)">重启</el-button>
          <el-button size="mini" type="primary" @click="handleStop(scope.$index, scope.row)">停止</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="info" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="添加服务检查配置" :visible="modalVisible" @close="cancelDialog" width="40%">
      <el-form :model="form" ref="form" :rules="rules" label-width="140px">
        <el-form-item label="docker名称" prop="dockerName">
          <el-input size="mini" v-model="form.dockerName"></el-input>
        </el-form-item>
        <el-form-item label="环境名称" prop="environmentName">
          <el-input size="mini" v-model="form.environmentName"></el-input>
        </el-form-item>
        <el-form-item label="检查CMD" prop="cmd">
          <el-input size="mini" v-model="form.cmd"></el-input>
        </el-form-item>
        <el-form-item label="重启CMD" prop="restartCmd">
          <el-input size="mini" v-model="form.restartCmd"></el-input>
        </el-form-item>
        <el-form-item label="停止CMD" prop="stopCmd">
          <el-input size="mini" v-model="form.stopCmd"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="cancelDialog">取 消</el-button>
        <el-button size="mini" type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="tsx">
import { Component, Vue, Watch } from 'vue-property-decorator'
import {
  fetchDockerMapping,
  saveDockerMapping,
  deleteDockerMapping,
  updateDockerMapping,
  restartDockerService,
  stopDockerService,
  healthCheckDockerService,
} from '@/apis/docker-service-api'
import { IDockerServiceMapping } from '@/interfaces/docker-service-interface'
import { Route } from 'vue-router'
import { DataTransformUtils } from '../../../lib/utils/data-transform-utils'
import AButtonBar from '@/components/AButtonBar.vue'

@Component({
  components: {AButtonBar}
})
export default class DockerServicePage extends Vue {
  dockerMappings: IDockerServiceMapping[] = []

  searchText = ''

  form: IDockerServiceMapping = {
    id: '',
    userId: '',
    cmd: '',
    restartCmd: '',
    stopCmd: '',
    dockerName: '',
    environmentName: '',
  }

  checkTimer: any = null

  isEdit = false

  selectedIndex = -1

  get filterDockerMappings() {
    if (!this.searchText) {
      return this.dockerMappings
    }
    return this.dockerMappings.filter(v => v.dockerName.includes(this.searchText))
  }

  rules = {
    cmd: [
      { required: true, message: '请输入健康检查命令', trigger: 'blur' },
      { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'change' },
    ],
    restartCmd: [
      { required: true, message: '请输入重启命令', trigger: 'blur' },
      { min: 2, max: 150, message: '长度在 2 到 150 个字符', trigger: 'change' },
    ],
    stopCmd: [
      { required: true, message: '请输入停止服务命令', trigger: 'blur' },
      { min: 2, max: 150, message: '长度在 2 到 150 个字符', trigger: 'change' },
    ],
    dockerName: [
      { required: true, message: '请输入docker名称', trigger: 'blur' },
      { min: 2, max: 150, message: '长度在 2 到 150 个字符', trigger: 'change' },
    ],
    environmentName: [
      { required: true, message: '请输入docker服务环境名称', trigger: 'blur' },
      { min: 2, max: 150, message: '长度在 2 到 150 个字符', trigger: 'change' },
    ],
  }

  modalVisible = false

  @Watch('modalVisible')
  watchModalVisible(val: boolean) {
    if (val) {
      clearInterval(this.checkTimer)
    } else {
      this.startCheck()
    }
  }

  async handleEdit(index: number, row: IDockerServiceMapping) {
    this.selectedIndex = index
    this.isEdit = true
    this.modalVisible = true
    DataTransformUtils.copyProperties(this.form, row)
  }

  async handleDelete(index: number, row: IDockerServiceMapping) {
    try {
      await this.$confirm('确定删除吗?')
    } catch {
      return
    }
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      await deleteDockerMapping({ ...row })
      this.dockerMappings.splice(index, 1)
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  async handleRestart(index: number, row: IDockerServiceMapping) {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      await restartDockerService({ ...row })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  async handleStop(index: number, row: IDockerServiceMapping) {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      await stopDockerService({ ...row })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  cancelDialog() {
    this.modalVisible = false
  }

  openDialog() {
    this.isEdit = false
    this.modalVisible = true
    const form: any = this.form
    Object.keys(form).forEach(key => {
      form[key] = ''
    })
  }

  async submit() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      if (this.isEdit) {
        const params = { ...this.form }
        await updateDockerMapping(params)
        this.dockerMappings.splice(this.selectedIndex, 1, params)
      } else {
        const { data } = await saveDockerMapping({ ...this.form })
        this.dockerMappings.push(data)
      }
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
      this.modalVisible = false
    }
  }

  confirm() {
    const ref = this.$refs.form as Vue & { validate: (vaild: any) => boolean }
    ref.validate((vaild: any) => {
      if (vaild) {
        this.submit()
        return true
      }
      return false
    })
  }

  async startCheck() {
    this.checkTimer = setInterval(async () => {
      if (!this.dockerMappings.length) return
      try {
        const { data } = await healthCheckDockerService([...this.dockerMappings])
        this.dockerMappings = data
      } catch (e) {
        this.$handleError(e)
      }
    }, 10000)
  }

  async loadData() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const { data } = await fetchDockerMapping()
      this.dockerMappings = data
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
      // this.startCheck()
    }
  }

  beforeRouteEnter(to: Route, from: Route, next: any) {
    next((vm: DockerServicePage) => {
      vm.startCheck()
    })
  }

  beforeRouteLeave(to: Route, from: Route, next: any) {
    clearInterval(this.checkTimer)
    next()
  }

  mounted() {
    this.loadData()
  }
}
</script>

<style lang="stylus" scoped>
>>>.el-table
  background-color $bg-color2 !important
  overflow scroll
>>>.el-table__row
  background-color $bg-color2 !important
>>>.el-form-item__error
  position relative !important
.text-span
  max-width 100px
  overflow hidden
  text-overflow ellipsis
  white-space nowrap
</style>
