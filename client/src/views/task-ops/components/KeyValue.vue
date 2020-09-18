<template>
  <div class="key-value-component">
    <div class="item">
      <div class="checkbox item-content"></div>
      <div class="key item-content desc">KEY</div>
      <div class="value item-content desc">VALUE</div>
      <div class="description item-content desc">DESCRIPTION</div>
    </div>
    <div class="item" v-for="(item, index) in options" :key="index">
      <div class="checkbox item-content">
        <el-checkbox v-model="item.checked"></el-checkbox>
      </div>
      <div class="key item-content">
        <input class="input" placeholder="Key" v-model="item.key" />
      </div>
      <div class="value item-content">
        <input class="input" placeholder="Value" v-model="item.value" />
      </div>
      <div class="description item-content">
        <input class="input" placeholder="Description" v-model="item.description" />
      </div>
    </div>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'

import { IHttpHeader } from '@/interfaces/http-interface'

@Component
export default class KeyValue extends Vue {
  @Prop(Array)
  options!: IHttpHeader[]
}
</script>

<style lang='stylus' scoped>
.key-value-component
  position relative
  font-size 14px
  .item
    position relative
    display flex
    align-items center
    padding 0px 5px
    .desc
      color $text-color3
    .item-content
      position relative
      display flex
      align-items center
      height 22px
      padding 2px
      border 1px solid $border-color1
      .input
        width 100%
        height 100%
        margin 1px
        border none
        outline none
        background-color $bg-color3
        color $text-color2
    .checkbox
      position relative
      width 30px
      justify-content center
    .key
      position relative
      flex 1
    .value
      position relative
      flex 1
    .description
      position relative
      flex 1
</style>
