<template>
  <div class="task-list-container">
    <div class="header">
      <span class="title">{{ title }}</span>
    </div>
    <div class="content">
      <el-card
        size="small"
        shadow="hover"
        @click.native="handleClick(material)"
        class="share-item"
        v-for="material in list"
        :key="material.id"
      >
        <span class="share-item-name">{{ material.name }}</span>
      </el-card>
    </div>
  </div>
</template>

<script lang="tsx">
import { Prop, Component, Vue } from 'vue-property-decorator'
import { IMaterial } from '../../interfaces/common-interface'

@Component
export default class TaskList extends Vue {
  @Prop({ type: String, default: 'title' })
  title!: string

  @Prop({ type: Array, default: () => [] })
  list!: any[]

  handleClick(material: IMaterial) {
    this.$emit('handleClick', material)
  }
}
</script>
<style lang="stylus" scoped>
.task-list-container
  position relative
  display flex
  flex-direction column
  height 100%
  width 300px
  padding 5px
  margin 0 5px
  // box-shadow 5px 5px 5px #ddd
  box-shadow 5px 5px 5px $bg-color1
  .header
    position relative
    margin-bottom 10px
    .title
      color $text-color3
      font-size 20px
      font-weight bold
  .content
    position relative
    flex 1
    overflow scroll
    .share-item
      position relative
      display flex
      align-items center
      justify-content space-between
      font-size 16px
      // border-bottom 1px solid #eee
      border-bottom 1px solid $bg-color1
      cursor pointer
      margin-bottom 5px
      .share-item-name
        position relative
        word-break break-all
      .share-item-tag
        position relative
        color red
</style>
