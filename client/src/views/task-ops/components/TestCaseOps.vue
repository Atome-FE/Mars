<template>
  <div class="material-main test-case-container">
    <AButtonBar v-model="material.name" :runMaterial="runMaterial" :updateMaterial="updateMaterial"/>
    <AFormBar>
      <template slot="left">
        <el-button size="mini" @click="openAddTasksDialog" plain type="primary">添加用例任务组</el-button>
      </template>
      <template slot="right">
        <el-button
          size="mini"
          @click="materialListVisible = !materialListVisible"
          type="primary"
          icon="el-icon-document"
        >添加任务</el-button>
      </template>
    </AFormBar>
    <!--
    <div class="input-bar-container">
      <div class="a-select">
        <el-button size="mini" @click="openAddTasksDialog" plain type="primary">添加用例任务组</el-button>
      </div>
      <div class="a-input">
        <el-button
          size="mini"
          @click="materialListVisible = !materialListVisible"
          type="primary"
          icon="el-icon-document"
        >添加任务</el-button>
      </div>
    </div>
    -->
    <div class="test-case-content">
      <div class="collapse scroll-v" accordion>
        <draggable
          style="min-height: 600px;"
          :list="tasks"
          group="task"
          :disabled="!dragEnabled"
          @change="dragListChange"
          ghost-class="ghost"
        >
          <div
            @click="handleTaskClick(item)"
            v-for="(item, index) in tasks"
            :key="item.id + index"
            :title="item.name"
            :name="item.name + index"
          >
            <div class="title">
              <div class="left">
                <span class="index-item">{{ index }}</span>
                <span class="task-name">{{ item.name }}</span>
                <span class="task-type-text">{{ item.type }}</span>
              </div>
              <div class="right">
                <Preview
                  class="preview"
                  :openDelay="500"
                  title="执行参数"
                  v-if="item.result && item.result.length"
                  :data="getTaskExecParams(item)"
                />
                <Preview class="preview" :openDelay="500" title="操作" :data="item" />
                <Preview class="preview" :openDelay="500" title="内容" :data="getData(item)" />
              </div>
            </div>
          </div>
        </draggable>
      </div>
      <transition name="width">
        <MaterialAddList
          group="task"
          v-if="materialListVisible"
          @closeMaterialList="materialListVisible = false"
          @typeSelected="typeSelected"
          class="add-content"
        />
      </transition>
      <div class="case-exec-view" v-if="getRecords.length">
        <ExecView />
      </div>
    </div>
    <el-dialog
      :title="currentSelectedTask? currentSelectedTask.name: '编辑任务'"
      :visible.sync="editTaskDialogVisible"
      width="50%"
      center
    >
      <CaseTaskCondition
        @seeTask="seeTask(currentSelectedTask)"
        @deleteTask="deleteTask(currentSelectedTask)"
        @updateTestCaseMaterial="updateMaterial"
        :modalVisible="editTaskDialogVisible"
        :option="currentSelectedTask"
      />
    </el-dialog>
    <el-dialog title="添加测试用例任务组" :visible.sync="testCaseTasksDialogVisible" width="30%" center>
      <div>
        <el-form ref="testGroupForm" size="mini" :model="otherTestCase" label-width="80px">
          <el-form-item label="选择用例">
            <el-select v-model="otherTestCase.testCaseId" size="mini" placeholder="请选择用例">
              <el-option v-for="item in getTestCases" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="testCaseTasksDialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="addTestCaseTasks">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop } from 'vue-property-decorator'

import { saveTestCaseMaterial, updateTestCaseMaterial, deleteTestCaseMaterial } from '@/apis/test-case-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { TaskSchedule } from '@/service/task-schedule'

import { TestCaseMaterialModule, getTestCaseMaterialById } from '@/store/modules/test-case-material-module'
import { HttpMaterialModule, getHttpMaterialById } from '@/store/modules/http-material-module'
import { SqlMaterialModule, getSqlMaterialById } from '@/store/modules/sql-material-module'
import { RedisMaterialModule, getRedisMaterialById } from '@/store/modules/redis-material-module'
import { DataMaterialModule, getDataMaterialById } from '@/store/modules/data-material-module'
import { MqMaterialModule, getMqMaterialById } from '@/store/modules/mq-material-module'
import { MongoMaterialModule, getMongoMaterialById } from '@/store/modules/mongo-material-module'
import { TaskRecordModule } from '@/store/modules/task-record-module'

import { ITestCase } from '@/interfaces/test-case-interface'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { ITaskParams, ITaskResult } from '@/interfaces/task-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IDataMaterial } from '@/interfaces/data-material-interface'
import { ISelectData, IMaterial } from '@/interfaces/common-interface'
import { IMongoMaterial } from '@/interfaces/mongo-interface'

import { TaskTypeEnum } from '@/enums/task-enum'

import { RouteConstants } from '@/constants/route-constants'

import { updateRelativeOnAdded, updateRelativeOnMoved } from '@/service/case-sort-service'
import Preview from './Preview.vue'
import CaseTaskCondition from './CaseTaskCondition.vue'
import TaskResult from './TaskResult.vue'
import MaterialAddList from './MaterialAddList.vue'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
import ExecView from './ExecView.vue'
import AEditor from '@/components/AEditor.vue'
import AButtonBar from '@/components/AButtonBar.vue'
import AFormBar from '@/components/AFormBar.vue'

const draggable = require('vuedraggable')

@Component({
  components: {
    CaseTaskCondition,
    TaskResult,
    draggable,
    Preview,
    MaterialAddList,
    ExecView,
    AEditor,
    AButtonBar,
    AFormBar,
  },
})
export default class TestCaseOps extends Vue {
  materialListVisible: boolean = false

  dragEnabled = true

  otherTestCase = {
    testCaseId: '',
  }

  testCaseTasksDialogVisible: boolean = false

  material: ITestCase = {
    id: '',
    name: '',
    material: '',
    groupName: '',
    secondGroupName: '',
    materialType: TaskTypeEnum.CASE,
  }

  tasks: ITaskParams[] = []

  currentSelectedTask: ITaskParams | null = null

  editTaskDialogVisible = false

  taskDialogVisible: boolean = false

  types: ISelectData[] = []

  updateTestCaseTimer: number = 0

  task: ITaskParams = {
    type: TaskTypeEnum.HTTP,
    id: '',
    name: '',
    delay: '',
    assert: '',
    paramReplaceRule: '',
    paramIndex: '',
    assertJump: '',
    assertTrueJump: '',
    loop: '',
    extendIndexs: '',
  }

  taskResults: ITaskResult[] = []

  get getRecords() {
    return TaskRecordModule.taskRecords
  }

  dragListChange(data: any) {
    if (data.added) {
      const { name, id } = data.added.element
      this.task.name = name
      this.task.id = id
      this.tasks.splice(data.added.newIndex, 1, { ...this.task })
      this.saveOrUpdateTestCase()
      updateRelativeOnAdded(this.tasks, data.added.newIndex)
    } else if (data.moved) {
      updateRelativeOnMoved(this.tasks, data.moved.newIndex, data.moved.oldIndex)
    }
  }

  getTaskExecParams(item: ITaskParams) {
    return item.result.map((v: ITaskResult) => {
      const obj = {
        执行参数: v.execParams,
      }
      return obj
    })
  }

  getTaskResult(item: ITaskParams) {
    return item.result.map((v: ITaskResult) => {
      const obj = {
        执行结果: v.pretty ? v.pretty : v.origin,
      }
      return obj
    })
  }

  getData(item: ITaskParams) {
    let task: IMaterial
    const { id, type } = item
    switch (type) {
      case TaskTypeEnum.DATA:
        task = getDataMaterialById(id) as IDataMaterial
        break
      case TaskTypeEnum.SQL:
        task = getSqlMaterialById(id) as ISqlMaterial
        break
      case TaskTypeEnum.HTTP:
        task = getHttpMaterialById(id) as IHttpMaterial
        break
      case TaskTypeEnum.REDIS:
        task = getRedisMaterialById(id) as IRedisMaterial
        break
      case TaskTypeEnum.MQ:
        task = getMqMaterialById(id) as IMqMaterial
        break
      case TaskTypeEnum.MONGO:
        task = getMongoMaterialById(id) as IMqMaterial
        break
      default:
        task = {
          id: '0',
          name: '0',
          groupName: '',
          secondGroupName: '',
        }
    }
    return DataTransformUtils.objPropertyToJson(task)
  }

  seeTask(item: ITaskParams) {
    let routeName = RouteConstants.HTTPMATERIALHOME
    switch (item.type) {
      case TaskTypeEnum.HTTP:
        routeName = RouteConstants.HTTPMATERIALHOME
        break
      case TaskTypeEnum.SQL:
        routeName = RouteConstants.SQLMATERIALHOME
        break
      case TaskTypeEnum.MQ:
        routeName = RouteConstants.MQMATERIALHOME
        break
      case TaskTypeEnum.REDIS:
        routeName = RouteConstants.REDISMATERIALHOME
        break
      case TaskTypeEnum.DATA:
        routeName = RouteConstants.DATAMATERIALHOME
        break
      case TaskTypeEnum.MONGO:
        routeName = RouteConstants.MONGOHOME
        break
      default:
        console.log(0)
    }
    this.editTaskDialogVisible = false
    window.location.href = `/material/${item.type}/${item.id}`
  }

  get getTestCases() {
    return TestCaseMaterialModule.testCaseMaterials.map((item: ITestCase) => {
      return {
        value: item.id,
        label: item.name,
      }
    })
  }

  openAddTasksDialog(id: string) {
    this.testCaseTasksDialogVisible = true
  }

  addTestCaseTasks() {
    if (!this.otherTestCase.testCaseId) {
      this.$notify.warning({
        title: '警告',
        message: '请选择用例',
      })
      return
    }
    const testCaseMetarial = getTestCaseMaterialById(this.otherTestCase.testCaseId)
    this.tasks.push(...JSON.parse(testCaseMetarial.material))
    this.otherTestCase.testCaseId = ''
    this.testCaseTasksDialogVisible = false
  }

  async runMaterial() {
    if (!this.material.id) {
      this.$notify.warning({
        title: '警告',
        message: '创建之后才能运行',
      })
      return
    }
    TaskRecordModule.removeAllRecords()
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    const res: ITaskResult[] = await TaskSchedule.runTestCase({ ...this.material })
    this.taskResults = [...res]
    loading.close()
  }

  async deleteTask(task: ITaskParams) {
    try {
      const index = this.tasks.findIndex(v => v === task)
      this.tasks.splice(index, 1)
      this.editTaskDialogVisible = false
    } catch (e) {
      this.$handleError(e)
    }
  }

  typeSelected(item: string) {
    this.task.type = item
    this.task.id = ''
  }

  get getOptions() {
    switch (this.task.type) {
      case TaskTypeEnum.HTTP:
        return HttpMaterialModule.httpMaterials.map((item: IHttpMaterial) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
      case TaskTypeEnum.SQL:
        return SqlMaterialModule.sqlMaterials.map((item: ISqlMaterial) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
      case TaskTypeEnum.REDIS:
        return RedisMaterialModule.redisMaterials.map((item: IRedisMaterial) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
      case TaskTypeEnum.MQ:
        return MqMaterialModule.mqMaterials.map((item: IMqMaterial) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
      case TaskTypeEnum.DATA:
        return DataMaterialModule.dataMaterials.map((item: IDataMaterial) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
      case TaskTypeEnum.MONGO:
        return MongoMaterialModule.mongoMaterials.map((item: IMongoMaterial) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
      default:
        return []
    }
  }

  initTypes() {
    this.types = [
      {
        value: 'HTTP',
        label: 'http',
      },
      {
        value: 'SQL',
        label: 'sql',
      },
      {
        value: 'REDIS',
        label: 'redis',
      },
      {
        value: 'MQ',
        label: 'mq',
      },
      {
        value: 'DATA',
        label: 'data',
      },
    ]
  }

  openTaskDialog() {
    this.taskDialogVisible = true
  }

  addTask() {
    this.getOptions.forEach((item: any) => {
      if (item.value === this.task.id) {
        this.task.name = item.label
      }
    })
    this.tasks.push({ ...this.task })
    this.taskDialogVisible = false
  }

  @Prop(Object)
  selectedMaterial!: ITestCase

  @Watch('materialListVisible')
  watchMaterialListVisible(val: boolean) {
    if (val) {
      this.task.type = TaskTypeEnum.HTTP
    }
  }

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: ITestCase) {
    if (!val.id) return
    DataTransformUtils.copyProperties(this.material, val)
    this.tasks = JSON.parse(val.material)
    this.taskResults.splice(0)
  }

  @Watch('taskResults')
  taskResultsChange(val: ITaskResult[]) {
    this.tasks.forEach((task: ITaskParams, index: number) => {
      task.result = val.filter((item: ITaskResult) => {
        return item.taskIndex === index
      })
    })
  }

  handleTaskClick(task: ITaskParams) {
    this.currentSelectedTask = task
    this.editTaskDialogVisible = true
  }

  saveOrUpdateTestCase() {
    this.updateMaterial()
  }

  async updateMaterial() {
    if (!this.material.id) {
      this.$notify.warning({
        title: '警告',
        message: '创建之后才能更新',
      })
      return
    }
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      this.tasks.forEach(task => {
        if (task.result) {
          task.result.splice(0)
        }
      })
      this.material.material = JSON.stringify(this.tasks)
      await updateTestCaseMaterial(this.material)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新用例成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
      this.editTaskDialogVisible = false
    }
  }

  created() {
    this.initTypes()
  }
}
</script>

<style lang="stylus" scoped>
.test-case-container
  overflow hidden !important
.test-case-content
  position relative
  display flex
  height 90%
  margin-bottom 20px
  .add-content
    position relative
    width 200px
    height 90%
    margin-left 10px
    margin-bottom 100px
  .collapse
    position relative
    flex 1
    font-size 13px
    line-height 48px
    width 100%
    cursor pointer
    color $text-color2
    .collapse-item
      position relative
      .result
        position relative
    .title
      position relative
      display flex
      align-items center
      justify-content space-between
      width 100%
      .right, .left
        position relative
        display flex
        align-items center
      .left
        .index-item
          color #bbb
          margin-right 20px
        .task-name
          color $text-color1
      .right
        margin-right 20px
    .preview
      margin-left 20px
    .ghost
      opacity 0.5
      background #c8ebfb
    .task-type-text
      margin-left 20px
      color $text-color5
</style>
