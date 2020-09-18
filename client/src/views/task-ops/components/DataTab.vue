<template>
  <div class="tab-container">
    <div
      :class="['tab', tab.active ? 'tab-bottom' : '']"
      v-for="tab in tabs"
      @click="$emit('clickTab', tab)"
      :key="tab.name"
    >
      <span class="name">{{ tab.name }}</span>
    </div>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'

import { IHttpTab } from '@/interfaces/http-interface'

@Component
export default class DataTab extends Vue {
  @Prop(Array)
  tabs!: IHttpTab[]
}
</script>

<style lang='stylus' scoped>
.tab-container
  position relative
  display flex
  align-items center
  font-size 12px
  width 100%
  padding 0 5px
  border-radius 5px
  box-sizing border-box
  color #808080
  background-color $bg-color2
  .tab-bottom
    position relative
    border-bottom 1px solid $text-color3
    padding 5px 0
    color $text-color1
  .tab
    margin-right 10px
    .name
      position relative
</style>
