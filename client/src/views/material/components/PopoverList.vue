<template>
  <div class="popover-list-container">
    <div class="action-item" v-for="item in list" :key="item.title" @click="item.action(item.label)">
      <span>{{ item.title }}</span>
      <i v-if="item.label === 'DELETE'" class="el-icon-delete node-action-icon"></i>
      <i v-if="item.label === 'SHARE'" class="el-icon-star-off node-action-icon"></i>
      <i v-if="item.label === 'ADD_MATERIAL'" class="el-icon-document node-action-icon"></i>
      <i v-if="item.label === 'ADD_FOLDER'" class="el-icon-folder node-action-icon"></i>
      <i v-if="item.label === 'RENAME'" class="el-icon-edit node-action-icon"></i>
      <i v-if="item.label === 'RUN'" class="el-icon-video-play node-action-icon"></i>
      <i v-if="item.label === 'COPY'" class="el-icon-document-copy node-action-icon"></i>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator'
import { PopoverListItem } from '../../../interfaces/nested-material-interface'

@Component
export default class PopoverList extends Vue {
  @Prop({ type: Array, default: () => [] })
  list!: PopoverListItem[]
}
</script>

<style lang="stylus" scoped>
.popover-list-container
  position relative
  display flex
  flex-direction column
  width 100%
  cursor pointer
  .action-item
    display flex
    align-items center
    justify-content space-between
    margin 5px 0
    .node-action-icon
      color $text-color3
</style>
