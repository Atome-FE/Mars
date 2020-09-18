<template>
  <div class="material-main material-main-flex-1">
    <div class="group-main">
      <div class="a-test-group-container">
        <div class="content">
          <TestGroupList :testGroupsProp="getTestGroups" />
        </div>
        <div class="ops">
          <el-button type="primary" @click="testGroupDialogVisible = true" size="mini">添加测试组</el-button>
        </div>
      </div>
      <transition name="width-40">
        <div class="exec-main" v-if="getRecords.length">
          <ExecView />
        </div>
      </transition>
    </div>
    <el-dialog title="添加测试组" :visible.sync="testGroupDialogVisible" width="30%" @open="dialogOpen" center>
      <div>
        <el-input
          v-model="testGroup.name"
          size="mini"
          ref="testGroupNameInput"
          placeholder="请输入名称"
          @keyup.enter.native="addTestGroup"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="testGroupDialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="addTestGroup">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="tsx">
import { Component, Vue } from 'vue-property-decorator'

import { saveTestGroup } from '@/apis/test-api'

import { FieldModule } from '@/store/modules/field-module'
import { TaskRecordModule } from '@/store/modules/task-record-module'

import { ITestGroup } from '@/interfaces/test-interface'

import TestGroupList from '../components/TestGroupList.vue'
import ExecView from '../components/ExecView.vue'

@Component({
  components: {
    TestGroupList,
    ExecView,
  },
})
export default class TestGroupHome extends Vue {
  testGroup: ITestGroup = {
    id: '',
    name: '',
    material: {
      runStatus: true,
    },
  }

  get getRecords() {
    return TaskRecordModule.taskRecords
  }

  dialogOpen() {
    this.$nextTick(() => {
      const el = this.$refs.testGroupNameInput as any
      el.focus()
    })
  }

  testGroupDialogVisible: boolean = false

  get getTestGroups() {
    return FieldModule.testGroups.map(v => v)
  }

  async addTestGroup() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const obj: ITestGroup = {
        id: '',
        name: this.testGroup.name,
        material: JSON.stringify(this.testGroup.material),
        concurrency: false,
      }
      const { data } = await saveTestGroup(obj)
      obj.id = data.id
      FieldModule.addTestGroup(obj)
      this.testGroupDialogVisible = false
      this.testGroup.name = ''
      this.$notify.success({
        title: '成功',
        message: '创建测试组成功!',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }
}
</script>

<style lang="stylus" scoped>
.group-main
  position relative
  display flex
  width 100%
  height 100%
  margin-bottom 20px
  margin-top 20px
  overflow hidden
  .exec-main
    position relative
    width 40%
    background-color $bg-color3
    margin 20px 0 20px 20px
    border-radius 5px
.a-test-group-container
  position relative
  display flex
  flex-direction column
  flex 1
  height 100%
  width 100%
  margin-bottom 20px
  .title
    position relative
    margin-bottom 20px
  .content
    position relative
    height 100%
    margin-bottom 20px
    overflow hidden
  .ops
    position relative
</style>
