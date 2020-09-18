<template>
  <div class="material-main a-record-container">
    <div class="left scroll-v">
      <div v-for="time in recordTimes" @click="clickTime(time)" class="item" :key="time">{{ formatTime(time) }}</div>
    </div>
    <div class="right">
      <div class="top">
        <el-radio v-model="resultType" label="TOTAL" class="radio">全部</el-radio>
        <el-radio v-model="resultType" label="SUCCESS" class="radio">成功</el-radio>
        <el-radio v-model="resultType" label="FAILED" class="radio">失败</el-radio>
      </div>
      <div class="right-main scroll-v">
        <el-collapse accordion>
          <el-collapse-item v-for="item in getCurrentTaskRecords" :key="item.id" :name="item.id">
            <template slot="title">
              <div class="content">
                <div class="text">
                  <span class="tip">任务名称:</span>
                  <span>{{ item.taskName }}</span>
                </div>
                <div class="text">
                  <span class="tip">测试用例名称:</span>
                  <span>{{ item.testCaseName }}</span>
                </div>
                <div class="text">
                  <span class="tip">测试组名称:</span>
                  <span>{{ item.testGroupName }}</span>
                </div>
              </div>
            </template>
            <div>
              <TaskResult
                v-if="item.executeTime"
                title="任务开始执行时间"
                :options="getMessage(formatTime(item.executeTime))"
              />
              <TaskResult v-if="item.taskDelay" title="任务延迟执行时间" :options="getMessage(item.taskDelay)" />
              <TaskResult v-if="item.taskParam" title="任务参数" :options="getMessage(item.taskParam)" />
              <TaskResult v-if="item.taskAssert" title="任务断言" :options="getMessage(item.taskAssert)" />
              <TaskResult v-if="item.taskResult" title="任务结果" :options="getMessage(item.taskResult)" />
              <TaskResult title="任务是否成功" :options="getMessage(!!Number(item.assertResult))" />
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </div>
</template>

<script lang="tsx">
import { Component, Vue } from 'vue-property-decorator'
import { Route } from 'vue-router'
import dayjs from 'dayjs'

import { fetchTaskRecord } from '@/apis/task-record-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'

import { ITaskRecord } from '@/interfaces/task-interface'

import { TaskResultTypeEnum } from '@/enums/task-enum'

import TaskResult from '../components/TaskResult.vue'

@Component({
  components: {
    TaskResult,
  },
})
export default class TaskRecordHome extends Vue {
  taskRecords: ITaskRecord[] = []

  resultType = TaskResultTypeEnum.SUCCESS

  recordTimes: number[] = []

  currentTiem: number = 0

  get getCurrentTaskRecords() {
    const arr = this.taskRecords.filter((v: ITaskRecord) => {
      switch (this.resultType) {
        case TaskResultTypeEnum.SUCCESS:
          return v.executeTime === this.currentTiem && !!Number(v.assertResult)
        case TaskResultTypeEnum.FAILED:
          return v.executeTime === this.currentTiem && !Number(v.assertResult)
        default:
          return v.executeTime === this.currentTiem
      }
    })
    return arr
  }

  clickTime(time: number) {
    this.currentTiem = time
  }

  formatTime(time: string) {
    return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
  }

  getMessage(data: any) {
    const json = DataTransformUtils.jsonify(data)
    const pretty = DataTransformUtils.toPrettyData(json)
    return pretty
  }

  initData(data: ITaskRecord[]) {
    this.recordTimes.splice(0)
    data.forEach((v: ITaskRecord) => {
      if (!this.recordTimes.includes(v.executeTime)) {
        this.recordTimes.push(v.executeTime)
      }
    })
  }

  async loadData() {
    const { executeTime } = this.$route.query
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      this.taskRecords.splice(0)
      const { data } = await fetchTaskRecord()
      this.initData(data)
      this.taskRecords.push(...data)
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  beforeRouteEnter(to: Route, from: Route, next: any) {
    next((vm: TaskRecordHome) => {
      vm.loadData()
    })
  }
}
</script>

<style lang="stylus" scoped>
.a-record-container
  position relative
  display flex
  flex-direction row!important
  flex 1 !important
.left
  position relative
  flex 1
  height 100%
  margin-right 20px
  .item
    position relative
    font-size 20px
    border-bottom 1px dashed $border-color1
    cursor pointer
    padding-bottom 20px
    margin-bottom 5px
    color $text-color2
  .item:active
    background-color gray
.right
  position relative
  display flex
  flex-direction column
  width 50%
  height 100%
  .top
    position relative
    display flex
    align-items center
    margin-bottom 20px
    .radio
      margin-right 10px
  .right-main
    position relative
    height 100%
    .content
      position relative
      display flex
      width 100%
      padding 0 20px
      .text
        display flex
        align-items center
        margin-right 30px
        max-width 140px
        overflow hidden
        white-space nowrap
        text-overflow ellipsis
        .tip
          color gray
          margin-right 5px
</style>
