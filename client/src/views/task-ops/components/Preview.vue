<template>
  <el-tooltip placement="right-start"
    :open-delay="openDelay">
    <div class="preview-container"
      slot="content">
      <vue-json-pretty
        @click="jsonClick"
        selectableType="multiple"
        :data="data">
      </vue-json-pretty>
    </div>
    <div class="preview-content">
      <el-button
        type="text">
        {{ title }}
      </el-button>
    </div>
  </el-tooltip>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'
import Clipboard from 'clipboard'

import { IMaterial } from '@/interfaces/common-interface'

const VueJsonPretty = require('vue-json-pretty').default

@Component({
  components: {
    VueJsonPretty,
  },
})
export default class Preview extends Vue {
  selectedNode: string = ''

  @Prop([Object, Array])
  data!: any

  @Prop(Number)
  openDelay!: number

  @Prop(String)
  title!: string

  clip() {
    this.$notify.success({
        title: '成功',
        message: '复制成功!'
      })
  }

  jsonClick(path: any, data: any) {
  }

  initClipboard() {
  }

  mounted() {
    this.initClipboard()
  }
}
</script>

<style lang="stylus" scoped>
.preview-content
  position relative
  display flex
  align-items center
.preview-btn
  position absolute
.preview-container
  position relative
  max-width 600px
  max-height 600px
  overflow scroll
</style>
