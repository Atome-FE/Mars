<template>
  <div class="group-container">
    <div class="title-ops">
      <div class="ops">
        <el-button size="mini" @click="runAllTestGroup" type="primary">运行所有测试组</el-button>
        <el-button size="mini" @click="clearAllTestGroupStatus" type="info">清除状态</el-button>
      </div>
    </div>
    <transition name="height">
      <ExecuteResult :record="record" :executeTime="executeTime" />
    </transition>
    <div class="test-group">
      <div class="content scroll-v">
        <el-collapse class="collapse-content" @change="collapseChange" accordion>
          <el-collapse-item v-for="item in testGroups" :key="item.id" :title="item.name" :name="item.id">
            <template slot="title">
              <div class="collapse-item">
                <div class="tip" v-if="item.ui.status === 'LOADING'">
                  <i class="el-icon-loading"></i>
                </div>
                <div class="tip" v-if="item.ui.status === 'SUCCESS'">
                  <i class="el-icon-check"></i>
                </div>
                <div class="text">
                  <span>{{ item.name }}</span>
                </div>
              </div>
            </template>
            <div class="group-ops">
              <el-button type="primary" @click="runOneTestGroup(item.id)" size="mini" icon="el-icon-video-play">运行</el-button>
              <el-button type="primary" @click="openAddDialog(item.id)" size="mini" icon="el-icon-document">添加测试用例</el-button>
              <el-button type="primary" @click="rename(item)" size="mini" icon="el-icon-edit">重命名</el-button>
              <el-button type="info" @click="deleteTestGroup(item.id)" size="mini" icon="el-icon-delete">删除测试组</el-button>
              <el-switch
                size="mini"
                class="item"
                @change="updateTestGroupRunStatus(item)"
                v-model="item.material.runStatus"
              ></el-switch>
              <el-switch size="mini" class="item" @change="updateTestGroupConcurrency(item)" v-model="item.concurrency"></el-switch>
            </div>
            <TestGroupCaseList
              class="group-case-list"
              :materialListVisible="materialListVisible"
              @dragAddCase="dragAddCase"
              :options="testGroupCaseMap[item.id]"
            />
          </el-collapse-item>
        </el-collapse>
      </div>
      <transition name="width">
        <MaterialAddList
          v-if="materialListVisible"
          @closeMaterialList="materialListVisible = false"
          group="case"
          class="add-content"
        />
      </transition>
    </div>
    <el-dialog title="修改测试组名称" @open="renameDialogOpen" :visible.sync="renameDialogVisible" width="30%" center>
      <div>
        <el-input
          v-model="testGroup.name"
          ref="testGroupRenameInput"
          size="mini"
          class="mobile-input"
          @keyup.enter.native="updateTestGroup"
          placeholder="请输入测试组名称"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="renameDialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="updateTestGroup">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="tsx">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'

import { saveTestGroupCase, updateTestGroup, deleteTestGroup } from '@/apis/test-api'

// import { TaskSchedule } from '@/lib/utils/task-schedule'
import { TaskSchedule } from '@/service/task-schedule'
import { TNotification } from '@/lib/utils/notification'

import { FieldModule } from '@/store/modules/field-module'
import { TaskRecordModule } from '@/store/modules/task-record-module'
import { TestCaseMaterialModule } from '@/store/modules/test-case-material-module'

import { ITestCase } from '@/interfaces/test-case-interface'
import { ITestGroupCase, ITestGroup } from '@/interfaces/test-interface'
import { ITaskRecord, ITaskResult } from '@/interfaces/task-interface'
import { IRecord } from '@/interfaces/record-interface'

import { TestCaseRunStatusEnum } from '@/enums/test-case-enum'
import { RecordStatusEnum } from '@/enums/record-enum'
import { TaskTypeEnum } from '@/enums/task-enum'

import { RouteConstants } from '@/constants/route-constants'

import ExecuteResult from './ExecuteResult.vue'
import TestGroupCaseList from './TestGroupCaseList.vue'
import MaterialAddList from './MaterialAddList.vue'

@Component({
  components: {
    TestGroupCaseList,
    ExecuteResult,
    MaterialAddList,
  },
})
export default class TestGroupList extends Vue {
  testGroupCaseDialogVisible: boolean = false

  materialListVisible: boolean = false

  renameDialogVisible: boolean = false

  testGroupCase: ITestGroupCase = {
    testGroupId: '',
    testCaseId: '',
    id: '',
    priority: 0,
  }

  executeTime: number = Date.now()

  record: IRecord = {
    status: RecordStatusEnum.NORMAL,
    result: true,
    total: 0,
    errorTotal: 0,
    sqlErrorTotal: 0,
    sqlTotal: 0,
    httpErrorTotal: 0,
    httpTotal: 0,
    redisErrorTotal: 0,
    redisTotal: 0,
    mqTotal: 0,
    mqErrorTotal: 0,
    mongoTotal: 0,
    mongoErrorTotal: 0,
  }

  testGroup: ITestGroup = {
    id: '',
    name: '',
    material: {},
    ui: {
      status: TestCaseRunStatusEnum.NORMAL,
    },
  }

  testGroupCaseMap: { [id: string]: ITestGroupCase[] } = {}

  testGroups: ITestGroup[] = []

  @Prop(Array)
  testGroupsProp!: ITestGroup[]

  renameTestGroup?: ITestCase

  rename(testGroup: ITestCase) {
    this.testGroup.id = testGroup.id
    this.renameTestGroup = testGroup
    this.renameDialogVisible = true
  }

  async deleteTestGroup(id: string) {
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
      const obj = {
        id: id,
        name: '',
        material: '',
      }
      await deleteTestGroup(obj)
      FieldModule.deleteTestGroup(id)
      this.$notify.success({
        title: '成功',
        message: '删除成功!',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  collapseChange(id: string) {
    if (!id) return
    this.testGroupCase.testGroupId = id
  }

  renameDialogOpen() {
    this.$nextTick(() => {
      const el = this.$refs.testGroupRenameInput as any
      el.focus()
    })
  }

  async handleUpdateTestGroup(item: ITestGroup) {
    try {
      const obj: ITestGroup = {
        id: item.id,
        name: item.name,
        material: JSON.stringify(item.material),
        concurrency: item.concurrency,
      }
      await updateTestGroup(obj)
      FieldModule.updateTestGroup({ ...obj })
    } catch (e) {
      this.$handleError(e)
    }
  }

  async updateTestGroupConcurrency(item: ITestCase) {
    await this.handleUpdateTestGroup(item)
  }

  async updateTestGroupRunStatus(item: ITestGroup) {
    await this.handleUpdateTestGroup(item)
  }

  async updateTestGroup() {
    if (!this.testGroup.id || !this.testGroup.name) {
      this.$notify.warning({
        title: '警告',
        message: '更新失败',
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
      const params = {
        id: this.renameTestGroup!.id,
        material: JSON.stringify(this.renameTestGroup!.material),
        name: this.testGroup.name,
        concurrency: this.renameTestGroup!.concurrency,
      }
      await updateTestGroup(params)
      FieldModule.updateTestGroup(params)
      this.renameDialogVisible = false
      this.$notify({
        title: '成功',
        message: '重命名成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
      this.testGroup.name = ''
    }
  }

  getTestGroupCases(testGroupId: string) {
    return FieldModule.testGroupCases
      .filter((item: ITestGroupCase) => item.testGroupId === testGroupId)
      .map((item: ITestGroupCase) => {
        const testCase = TestCaseMaterialModule.testCaseMaterials.find((v: ITestCase) => {
          return v.id === item.testCaseId
        })
        if (!testCase) {
          return {
            id: '',
            testCaseId: '',
            testGroupId: '',
            priority: 0,
            ui: {
              status: TestCaseRunStatusEnum.NORMAL,
            },
            name: '',
          }
        }
        const obj = {
          id: item.id,
          testCaseId: item.testCaseId,
          testGroupId: item.testGroupId,
          priority: item.priority,
          ui: {
            status: TestCaseRunStatusEnum.NORMAL,
          },
          name: testCase.name,
        }
        return obj
      })
      .filter(item => item.id)
      .sort((a: ITestGroupCase, b: ITestGroupCase) => {
        return a.priority - b.priority
      })
  }

  async initTestGroupCases() {
    try {
      this.testGroups.forEach((item: ITestGroup) => {
        this.$set(this.testGroupCaseMap, item.id, this.getTestGroupCases(item.id))
      })
    } catch (e) {
      console.log(e)
    }
  }

  gotoRecord() {
    this.$router.push({
      name: RouteConstants.TASKRECORDHOME,
      query: {
        executeTime: this.executeTime.toString(),
      },
    })
  }

  created() {
    this.initTestGroupCases()
  }

  resetRecord() {
    this.record = {
      status: RecordStatusEnum.NORMAL,
      result: true,
      total: 0,
      errorTotal: 0,
      sqlErrorTotal: 0,
      sqlTotal: 0,
      httpErrorTotal: 0,
      httpTotal: 0,
      redisErrorTotal: 0,
      redisTotal: 0,
      mqTotal: 0,
      mqErrorTotal: 0,
      mongoTotal: 0,
      mongoErrorTotal: 0,
    }
  }

  clearAllTestGroupStatus() {
    this.testGroups.forEach((v: ITestGroup) => {
      v.ui.status = TestCaseRunStatusEnum.NORMAL
    })
    this.resetRecord()
  }

  displayRecord() {
    TaskRecordModule.taskRecords.forEach((v: ITaskRecord) => {
      if (v.executeTime === this.executeTime) {
        this.record.total++
        switch (v.taskType) {
          case TaskTypeEnum.SQL:
            this.record.sqlTotal++
            if (!v.assertResult) {
              this.record.errorTotal++
              this.record.sqlErrorTotal++
            }
            break
          case TaskTypeEnum.HTTP:
            this.record.httpTotal++
            if (!v.assertResult) {
              this.record.errorTotal++
              this.record.httpErrorTotal++
            }
            break
          case TaskTypeEnum.REDIS:
            this.record.redisTotal++
            if (!v.assertResult) {
              this.record.errorTotal++
              this.record.redisErrorTotal++
            }
            break
          case TaskTypeEnum.MQ:
            this.record.mqTotal++
            if (!v.assertResult) {
              this.record.errorTotal++
              this.record.mqErrorTotal++
            }
            break
          case TaskTypeEnum.MONGO:
            this.record.mongoTotal++
            if (!v.assertResult) {
              this.record.errorTotal++
              this.record.mongoErrorTotal++
            }
            break
          default:
        }
      }
    })
    this.record.status = RecordStatusEnum.COMPLETED
    this.record.result = !this.record.errorTotal
  }

  async runAllTestGroup() {
    this.resetRecord()
    TaskRecordModule.removeAllRecords()
    this.executeTime = Date.now()
    this.testGroups.forEach((v: ITestGroup) => {
      v.ui.status = TestCaseRunStatusEnum.LOADING
    })
    for (let i = 0; i < this.testGroups.length; i++) {
      const testGroup = this.testGroups[i]
      if (testGroup.material.runStatus) {
        this.runTestGroup(testGroup.id, this.executeTime)
      }
    }
    this.displayRecord()
    TNotification.send('已运行完所有的测试用例', {
      body: '运行成功',
    })
  }

  setTestGroupStatus(testGroupId: string, status: TestCaseRunStatusEnum) {
    this.testGroups.forEach((v: ITestGroup) => {
      if (v.id === testGroupId) {
        v.ui.status = status
      }
    })
  }

  async runOneTestGroup(testGroupId: string, executeTime: number = Date.now()) {
    this.resetRecord()
    TaskRecordModule.removeAllRecords()
    this.executeTime = Date.now()
    this.setTestGroupStatus(testGroupId, TestCaseRunStatusEnum.LOADING)
    await this.runTestGroup(testGroupId)
    this.displayRecord()
  }

  async runTestGroup(testGroupId: string, executeTime: number = Date.now()) {
    const testGroup = this.testGroups.find(t => t.id === testGroupId) as ITestGroup
    const { concurrency } = testGroup
    const testGroupCases = this.testGroupCaseMap[testGroupId]
    if (!testGroupCases) return
    testGroupCases.forEach((item: any) => {
      item.ui.status = TestCaseRunStatusEnum.LOADING
    })
    const n = testGroupCases.length
    for (let i = 0; i < testGroupCases.length; i++) {
      const testGroupCase: any = testGroupCases[i]
      try {
        // const res = await TaskSchedule.runTestGroupCase(testGroupCase.testCaseId, executeTime, testGroupId)
        const res = await TaskSchedule.runTestGroupCase(testGroupCase.testCaseId, executeTime, testGroupId)
        testGroupCase.ui.status = TestCaseRunStatusEnum.SUCCESS
        // TODO 后期增强结果的分析
        // testGroupCase.response = res
      } catch (e) {
        this.$notify.warning({
          title: '警告',
          message: e.message,
        })
      }
    }
    this.setTestGroupStatus(testGroupId, TestCaseRunStatusEnum.SUCCESS)
  }

  get getTestCases() {
    return TestCaseMaterialModule.testCaseMaterials.map((item: ITestCase) => {
      return {
        value: item.id,
        label: item.name,
      }
    })
  }

  @Watch('testGroupsProp', {
    immediate: true,
    deep: true,
  })
  testGroupsPropChange(val: ITestGroup[]) {
    this.testGroups.splice(0)
    val.forEach((v: ITestGroup) => {
      const obj = {
        id: v.id,
        name: v.name,
        concurrency: !!v.concurrency,
        material: JSON.parse(v.material),
        ui: {
          status: TestCaseRunStatusEnum.NORMAL,
        },
      }
      this.testGroups.push(obj)
    })
    this.initTestGroupCases()
  }

  openAddDialog(id: string) {
    this.materialListVisible = !this.materialListVisible
    this.testGroupCase.testGroupId = id
  }

  async dragAddCase(data: any) {
    const { id } = data.element
    this.testGroupCase.testCaseId = id
    await this.addTestGroupCase()
  }

  async addTestGroupCase() {
    try {
      const testGroupCase = this.testGroupCaseMap[this.testGroupCase.testGroupId]
      const priority = testGroupCase ? testGroupCase.length : 0
      const obj: ITestGroupCase = {
        id: '',
        testGroupId: this.testGroupCase.testGroupId,
        testCaseId: this.testGroupCase.testCaseId,
        priority,
      }
      const { data } = await saveTestGroupCase(obj)
      obj.id = data.id
      FieldModule.addTestGroupCase(obj)
      this.initTestGroupCases()
      this.$notify.success({
        title: '成功',
        message: '添加用例成功!',
      })
    } catch (e) {
      this.$notify.warning({
        title: '警告',
        message: e.message,
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.group-container
  position relative
  display flex
  flex-direction column
  height 100%
  overflow hidden
  .test-group
    position relative
    display flex
    align-items flex-start
    margin-top 50px
    margin-bottom 10px
    height 100%
    width 100%
    overflow hidden
    .content
      position relative
      flex 1
      width 100%
      height 100%
      overflow hidden
      .collapse-content
        position relative
        height 100%
        width 100%
        overflow scroll
    .add-content
      position relative
      width 200px
      margin-left 10px
  .group-case-list
    padding 0 20px
  .group-ops
    position relative
    display flex
    align-items center
    padding 0 20px
    .item
      margin-left 20px
  .title-ops
    position relative
    display flex
    align-items center
    justify-content flex-end
    margin-bottom 10px
    .name
      position relative
      display flex
      align-items center
    .ops
      position relative
      display flex
      align-items center
  .content
    position relative
    display flex
    align-items center
    .collapse-item
      position relative
      display flex
      align-items center
      width 100%
      padding 5px 20px
      .tip
        margin 0 10px
</style>
