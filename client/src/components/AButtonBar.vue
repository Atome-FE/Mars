<template>
  <div class="button-bar-container">
    <div class="name">
      <el-input :value="value" @input="onInputChange" size="mini" class="a-input-width" placeholder="请输入名称"></el-input>
    </div>
    <div class="buttons">
      <el-button v-if="runSqlCopyMaterial" size="mini" @click="runSqlCopyMaterial" type="primary">运行复制结果</el-button>
      <el-button v-if="runMaterial" size="mini" @click="runMaterial" type="primary">运行</el-button>
      <el-button v-if="updateMaterial" size="mini" @click="updateMaterial" type="info">更新</el-button>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Model, Component, Prop } from 'vue-property-decorator'
@Component
export default class AButtonBar extends Vue {
  @Prop({type: Function})
  runMaterial!: Function

  @Prop({type: Function})
  updateMaterial!: Function

  @Prop({type: Function})
  runSqlCopyMaterial!: Function

  @Model('change', { type: String }) 
  readonly value!: string

  onInputChange(value: any) {
    this.$emit('change', value)
  }
}
</script>

<style lang="stylus" scoped>
.button-bar-container
  position relative
  display flex
  align-items center
  justify-content space-between
  margin-bottom 10px
  .name
    position relative
    display flex
    align-items center
    .name-input
      margin-right 20px
  .buttons
    position relative
    display flex
    align-items center
</style>