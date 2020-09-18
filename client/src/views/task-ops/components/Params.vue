<template>
  <div class="params-container">
    <KeyValue
      :options="options"/>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'

import { IHttpHeader } from '@/interfaces/http-interface'

import KeyValue from './KeyValue.vue'

@Component({
  components: {
    KeyValue,
  },
})
export default class Params extends Vue {
  @Prop(Array)
  options!: IHttpHeader[]
}
</script>

<style lang='stylus' scoped>
.params-container
  position relative
</style>
