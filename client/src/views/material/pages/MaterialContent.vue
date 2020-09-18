<template>
  <div class="material-page">
    <div class="material-content">
      <HttpOps v-if="materialType === 'HTTP'" ref="httpOpsEl" :selectedMaterial="selectedMaterial" />
      <SqlOps v-if="materialType === 'SQL'" ref="sqlOpsEl" :selectedMaterial="selectedMaterial" />
      <EnvOps v-if="materialType === 'ENV'" ref="EnvOpsEl" :selectedMaterial="selectedMaterial" />
      <MongoOps v-if="materialType === 'MONGO'" ref="mongoOpsEl" :selectedMaterial="selectedMaterial"></MongoOps>
      <MqOps v-if="materialType === 'MQ'" ref="mqOpsEl" :selectedMaterial="selectedMaterial" />
      <DataOps v-if="materialType === 'DATA'" ref="dataOpsEl" :selectedMaterial="selectedMaterial" />
      <RedisOps v-if="materialType === 'REDIS'" ref="redisOpsEl" :selectedMaterial="selectedMaterial" />
      <TestCaseOps v-if="materialType === 'CASE'" ref="testCaseOpsEl" :selectedMaterial="selectedMaterial" />
      <MockOps v-if="materialType === 'MOCK'" ref="mockOpsEl" :selectedMaterial="selectedMaterial" />
    </div>
  </div>
</template>

<script lang="tsx">
import { Vue, Component } from 'vue-property-decorator'
import HttpOps from '@/views/task-ops/components/HttpOps.vue'
import SqlOps from '@/views/task-ops/components/SqlOps.vue'
import RedisOps from '@/views/task-ops/components/RedisOps.vue'
import MongoOps from '@/views/task-ops/components/MongoOps.vue'
import MqOps from '@/views/task-ops/components/MqOps.vue'
import DataOps from '@/views/task-ops/components/DataOps.vue'
import EnvOps from '@/views/task-ops/components/EnvOps.vue'
import TestCaseOps from '@/views/task-ops/components/TestCaseOps.vue'
import MockOps from '@/views/task-ops/components/MockOps.vue'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'

@Component({
  components: {
    HttpOps,
    SqlOps,
    RedisOps,
    MongoOps,
    MqOps,
    DataOps,
    EnvOps,
    TestCaseOps,
    MockOps,
  },
})
export default class HttpPage extends Vue {
  materialType = 'HTTP'

  get selectedMaterial() {
    return NestedMaterialModule.selectedNestedMaterial?.material || {}
  }

  get materialId() {
    return NestedMaterialModule.materialId
  }

  created() {
    const { materialType } = this.$route.params
    this.materialType = materialType.toUpperCase()
  }
}
</script>
