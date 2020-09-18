<template>
  <div class="params-container">
    <KeyValue
      :options="options"/>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'

import KeyValue from './KeyValue.vue'

@Component({
  components: {
    KeyValue,
  },
})
export default class Headers extends Vue {
  @Prop(Array)
  options!: any[]
}
</script>

<style lang='stylus' scoped>
.params-container
  position relative
</style>
