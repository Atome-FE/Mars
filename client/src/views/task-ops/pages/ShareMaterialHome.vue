<template>
  <div class="material-main material-main-flex-1">
    <AButtonBar v-model="searchText"/>
    <el-radio-group class="a-radio-group" v-model="materialType" size="mini">
      <el-radio-button v-for="item in materialTypes" :key="item" :label="item">{{ item }}</el-radio-button>
    </el-radio-group>
    <div class="material-main-content">
      <el-table size="mini" :data="filterShareMaterialBySearchText">
        <el-table-column label="id">
          <template slot-scope="scope">
            <span class="text-span">{{ scope.row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="名称">
          <template slot-scope="scope">
            <span class="text-span">{{ scope.row.material.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分享者">
          <template slot-scope="scope">
            <span class="text-span">{{ scope.row.userEmail }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分享时间">
          <template slot-scope="scope">
            <span class="text-span">{{ scope.row.createTimestamp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleMaterialClick(scope.row)">运行</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      :title="getModalTitle"
      :visible.sync="modalVisible"
      width="50%"
      center
      :show-close="false"
      :close-on-click-modal="canCloseModal"
    >
      <div class="share-modal-container">
        <div class="share-modal-main">
          <el-progress
            size="small"
            :text-inside="true"
            :stroke-width="24"
            :percentage="percentage"
            status="success"
            class="share-material-run-progress"
          ></el-progress>
          <MonacoEditor
            editorId="shareMaterialDataId"
            :height="200"
            v-model="dataMaterialData"
            :language="editorLanguage"
          />
          <TaskResult :options="taskResult" />
        </div>
        <div class="share-modal-record">
          <ExecView :closable="false" />
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button :disabled="!canCloseModal" size="mini" @click="modalVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="confirm" :loading="!canCloseModal">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="tsx">
import { Component, Vue } from 'vue-property-decorator'
import { ShareMaterialModule } from '@/store/modules/share-material-module'
import { IShareMaterial, IShareMaterialParams } from '@/interfaces/share-interface'
import { IDataMaterial } from '@/interfaces/data-material-interface'
import { saveDataMaterial, updateDataMaterial, getDataMaterial } from '@/apis/data-material-api'
import { saveShareMaterialParams } from '@/apis/share-api'
import { DataMaterialModule, getDataMaterialById } from '@/store/modules/data-material-module'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'
import { ITaskResult } from '@/interfaces/task-interface'
import TaskList from '@/components/task-list/TaskList.vue'
import { ShareMaterialSchedule } from '../../../lib/utils/share-material-schedule'
import TaskResult from '../components/TaskResult.vue'
import { testCaseSubject } from '../../../subject/test-case-subject'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { IMaterial } from '../../../interfaces/common-interface'
import ExecView from '../components/ExecView.vue'
import { TaskRecordModule } from '../../../store/modules/task-record-module'
import { EditorLanguageType } from '../../../enums/editor'
import AButtonBar from '@/components/AButtonBar.vue'

@Component({
  components: { MonacoEditor, TaskResult, ExecView, TaskList, AButtonBar },
})
export default class ShareMaterialHome extends Vue {
  private currentShareMaterial: IShareMaterial | null = null

  private dataMaterialData = ''

  materialType: TaskTypeEnum = TaskTypeEnum.HTTP

  materialTypes = [TaskTypeEnum.HTTP, TaskTypeEnum.SQL, TaskTypeEnum.REDIS, TaskTypeEnum.MQ, TaskTypeEnum.MONGO, TaskTypeEnum.CASE]

  modalVisible = false

  canCloseModal = true

  percentage = 0

  editorLanguage = EditorLanguageType.JSON

  searchText = ''

  taskResult: ITaskResult = {
    pretty: '',
    type: '',
    origin: '',
    clipText: '',
  }

  get getModalTitle() {
    return this.currentShareMaterial?.material?.name || '任务模块'
  }

  async submit() {
    if (!this.currentShareMaterial) {
      return
    }
    const { material, shareMaterialParams, id } = this.currentShareMaterial
    if (!material) {
      return
    }
    const dataMaterialParams: IDataMaterial = {
      id: '',
      data: this.dataMaterialData,
      describe: '',
      groupName: '分享数据源',
      secondGroupName: '',
      name: `${material.name} 数据源`,
    }

    if (shareMaterialParams) {
      dataMaterialParams.id = shareMaterialParams.dataMaterialId
      await updateDataMaterial(dataMaterialParams)
      DataMaterialModule.updateDataMaterial(dataMaterialParams)
    } else {
      const { data } = await saveDataMaterial(dataMaterialParams)
      DataMaterialModule.addDataMaterial(data)

      const shareParams: IShareMaterialParams = {
        shareMaterialId: id as string,
        dataMaterialId: data.id,
      }
      const { data: res } = await saveShareMaterialParams(shareParams)
      this.currentShareMaterial.shareMaterialParams = res
    }
  }

  async runShareMaterial(): Promise<ITaskResult[]> {
    const schedule = new ShareMaterialSchedule(this.currentShareMaterial as IShareMaterial)
    schedule.paramReplace = this.dataMaterialData
    return (await schedule.runShareMaterial()) as ITaskResult[]
  }

  async confirm() {
    this.canCloseModal = false
    try {
      if (this.currentShareMaterial!.type === TaskTypeEnum.CASE) {
        await this.submit()
      }
      TaskRecordModule.removeAllRecords()
      const arr = await this.runShareMaterial()
      this.taskResult = arr[arr.length - 1]
      this.$notify({
        title: '成功',
        message: '运行任务成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      this.canCloseModal = true
    }
  }

  reset() {
    TaskRecordModule.removeAllRecords()
    this.percentage = 0
    this.taskResult = {
      pretty: '',
      type: '',
      origin: '',
      clipText: '',
    }
  }

  async getMaterialFirstDataSource(material: IMaterial) {
    const tasks = JSON.parse(material.material)
    const task = tasks[0]
    const { data } = await getDataMaterial(task.id)
    return data.data
  }

  async handleMaterialClick(shareMaterial: IShareMaterial) {
    this.reset()
    this.currentShareMaterial = shareMaterial
    switch (shareMaterial.type) {
      case TaskTypeEnum.CASE:
        if (shareMaterial.shareMaterialParams) {
          const dataMaterial: IDataMaterial = getDataMaterialById(
            shareMaterial.shareMaterialParams.dataMaterialId,
          ) as IDataMaterial
          this.dataMaterialData = dataMaterial.data
        } else {
          this.dataMaterialData = await this.getMaterialFirstDataSource(shareMaterial.material!)
        }
        this.editorLanguage = EditorLanguageType.JSON
        break
      case TaskTypeEnum.HTTP:
      case TaskTypeEnum.SQL:
      case TaskTypeEnum.REDIS:
      case TaskTypeEnum.MQ:
      case TaskTypeEnum.MONGO:
        this.dataMaterialData = shareMaterial.material!.paramReplace || ''
        this.editorLanguage = EditorLanguageType.YAML
        break
      default:
        console.log(0)
    }
    this.modalVisible = true
  }

  get filterShareMaterialBySearchText() {
    if (this.searchText) {
      return this.filterShareMaterialByMaterialType.filter((v: any) => {
        return v.material.name.includes(this.searchText)
      })
    } else {
      return this.filterShareMaterialByMaterialType
    }
  }

  get filterShareMaterialByMaterialType() {
    return this.getShareMaterials[this.materialType]
  }

  get getShareMaterials() {
    const shareMaterials: any = ShareMaterialModule.shareMaterials
    const result = Object.keys(shareMaterials)
      .filter(key => key !== TaskTypeEnum.ENV)
      .reduce((obj: any, key: string) => {
        obj[key] = shareMaterials[key]
        return obj
      }, {})
    return result
  }

  wrapMaterialList(list: IShareMaterial[]) {
    return list.map(v => ({ ...v, name: v.material!.name }))
  }

  listenTaskSchedule() {
    testCaseSubject.subscribe({
      next: data => {
        this.percentage = Math.floor(((data.currentTaskIndex + 1) / data.totalTaskCount) * 100)
      },
    })
  }

  mounted() {
    this.listenTaskSchedule()
  }
}
</script>

<style lang="stylus" scoped>
.share-modal-container
  position relative
  display flex
  align-items center
  justify-content space-between
  width 100%
  .share-modal-main
    width 70%
  .share-modal-record
    width 30%
.share-material-run-progress
  margin-bottom 20px
>>>.el-table
  background-color $bg-color2 !important
  height 100%
  overflow scroll
>>>.el-table__row
  background-color $bg-color2 !important
>>>.el-form-item__error
  position relative !important
.material-main-content
  flex 1
  overflow hidden
.a-radio-group
  margin-bottom 10px
</style>
