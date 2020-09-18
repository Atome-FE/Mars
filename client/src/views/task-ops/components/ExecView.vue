<template>
  <div class="exec-wrap">
    <el-button v-if="closable" class="exec-footer" size="mini" @click="close" type="danger" icon="el-icon-close">关闭</el-button>

    <div class="exec-container">
      <div class="item" v-for="(item, index) in getRecords" :key="item.taskId + index">
        <div class="task-time item-main">
          <span class="time">{{ formatTime(item.executeTaskTime) }} --</span>
        </div>
        <div class="task-name item-main">
          <span class="name">任务名称: {{ item.taskName }}</span>
        </div>
        <div class="task-result item-main">
          <span class="result">运行结果: {{ item.taskResult }}</span>
        </div>
        <div class="task-assert-result item-main">
          <span class="result">断言结果: {{ !!Number(item.assertResult) }}</span>
        </div>
      </div>
      <div id="execFooterEl"></div>
    </div>
  </div>
</template>

<script lang="tsx">
import { Component, Vue, Watch, Prop } from 'vue-property-decorator'
import dayjs from 'dayjs'

import { TaskRecordModule } from '@/store/modules/task-record-module'

import { ITaskRecord } from '@/interfaces/task-interface'

@Component
export default class ExecView extends Vue {
  @Prop({ type: Boolean, default: true })
  closable!: boolean

  get getRecords() {
    return TaskRecordModule.taskRecords
  }

  formatTime(time: string) {
    return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
  }

  @Watch('getRecords')
  getRecordsChange(records: ITaskRecord[]) {
    this.$nextTick(() => {
      const el: any = document.querySelector('#execFooterEl')
      el?.scrollIntoView(false)
    })
  }

  close() {
    TaskRecordModule.removeAllRecords()
  }
}
</script>

<style lang="stylus" scoped>
.exec-wrap
  height 100%
  overflow hidden
  >>> .exec-footer
    position absolute
    top 10px
    right 10px
    z-index 2
.exec-container
  position absolute
  top 0
  height 100%
  padding 12px
  box-sizing border-box
  font-size 16px
  overflow scroll
  scroll-behavior smooth
  .item
    position relative
    display flex
    flex-direction column
    border-bottom 1px dashed gray
    margin-bottom 20px
    .item-main
      position relative
      padding 5px
      display flex
      align-items center
      justify-content flex-start
      word-break break-all
    .task-name
      .name
        position relative
        color #409EFF
    .task-time
      position relative
      .time
        position relative
        color #E6A23C
    .task-param
      position relative
      .param
        position relative
        color #FFF
    .task-result
      position relative
      .result
        position relative
        color green
    .task-assert-result
      position relative
      .result
        position relative
        color #F56C6C
</style>
