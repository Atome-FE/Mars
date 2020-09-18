<template>
  <div class="input-bar-container">
    <div class="a-select">
      <slot name="left"></slot>
    </div>
    <div class="a-input">
      <slot name="right"></slot>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Component } from 'vue-property-decorator'
@Component
export default class AFormBar extends Vue {

} 
</script>
<style lang="stylus" scoped>
.input-bar-container
  position relative
  display flex
  align-items center
  margin-bottom 10px
  .a-select, .a-input
    position relative
    display flex
    align-items center
  .a-select
    margin-right 10px
  .a-input
    flex 1
    .a-input-item
      width 100%
</style>