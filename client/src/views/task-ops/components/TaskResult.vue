<template>
  <div class="task-result-container" v-if="options.type">
    <div class="title">
      <span class="name">{{ title ? title : '执行结果' }}</span>
      <button class="btn clip-all-btn" ref="copyAllBtn" @click="clip" :data-clipboard-text="options.clipText">copy all</button>
    </div>
    <div class="result">
      <vue-json-pretty v-if="options.type === 'PRETTY'" @click="jsonClick" :data="options.pretty"></vue-json-pretty>
      <span class="json" v-if="options.type === 'ORIGIN'">{{ options.origin }}</span>
    </div>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'
import Clipboard from 'clipboard'

import { ITaskResult } from '@/interfaces/task-interface'

const VueJsonPretty = require('vue-json-pretty').default

@Component({
  components: {
    VueJsonPretty,
  },
})
export default class TaskResult extends Vue {
  selectedNode: any = ''

  @Prop(Object)
  options!: ITaskResult

  @Prop(String)
  title!: string

  clip() {
    this.$notify.success({
      title: '成功',
      message: '复制成功!',
    })
  }

  jsonClick(path: any, data: any) {}

  initClipboard() {
    const clipAll = new Clipboard('.clip-all-btn')
  }

  mounted() {
    this.initClipboard()
  }
}
</script>

<style lang='stylus' scoped>
*
  user-select text !important
.task-result-container
  position relative
  display flex
  flex-direction column
  margin 10px 0
  padding 0 10px
  color $text-color2
  .title
    position relative
    display flex
    align-items center
    font-size 16px
    justify-content space-between
    .name
      font-weight bold
    .hide-btn
      display none
    .btn
      position relative
      outline none
      border none
      font-size 16px
      font-weight bold
      transition color 0.5s
      background-color transparent
    .btn:hover
      color #409EFF
  .result
    position relative
    margin-top 10px
    font-size 18px
    padding 5px
    border-radius 5px
    overflow scroll
</style>
