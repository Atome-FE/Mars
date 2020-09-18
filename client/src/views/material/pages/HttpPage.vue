<template>
  <div class="material-page">
    <div class="material-content">
      <HttpOps ref="httpOpsEl" :selectedHttpMaterial="selectedMaterial" />
    </div>
  </div>
</template>

<script lang="tsx">
import { Vue, Component } from 'vue-property-decorator'
import HttpOps from '@/views/task-ops/components/HttpOps.vue'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'

@Component({
  components: {
    HttpOps,
  },
})
export default class HttpPage extends Vue {
  get selectedMaterial() {
    return NestedMaterialModule.selectedNestedMaterial?.material || {}
  }

  get materialId() {
    return NestedMaterialModule.materialId
  }
}
</script>
