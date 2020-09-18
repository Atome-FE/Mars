<template>
  <div class="a-editor">
    <span class="name">{{ title }}</span>
    <MonacoEditor :editorId="editorId" :height="height" :value="value" @change="onChange" :language="language" />
  </div>
</template>
<script lang="ts">
import { Component, Vue, Prop, Model } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

@Component({
  components: {
    MonacoEditor
  }
})
export default class AEditor extends Vue {
  @Model('change', { type: String }) 
  readonly value!: string

  @Prop({type: String, default: ''})
  title!: string

  @Prop({type: String, default: 'yaml'})
  language!: string

  @Prop({type: String, default: 'xxx'})
  editorId!: string

  @Prop({type: Number, default: 200})
  height!: number

  onChange(value: any) {
    this.$emit('change', value)
  }
}
</script>
<style lang="stylus" scoped>
.a-editor
  position relative
  display flex
  align-items flex-start
  flex-direction column
  width 100%
  margin-top 10px
  margin-bottom 20px
  box-sizing border-box
  .name
    position relative
    font-size 16px
    font-weight bold
    margin-bottom 10px
    color $text-color3
</style>