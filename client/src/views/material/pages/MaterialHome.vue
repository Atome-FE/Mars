<template>
  <div class="material-container">
    <div class="left-area">
      <TreeContainer />
    </div>
    <div class="right-area">
      <router-view></router-view>
    </div>
  </div>
</template>

<script lang="tsx">
import { Vue, Component, Watch } from 'vue-property-decorator'
import { fetchAllNestedMaterials } from '@/apis/nested-material'
import { NestedMaterialModule } from '@/store/modules/nested-material-module'
import { Route } from 'vue-router'
import TreeContainer from '../components/TreeContainer.vue'
import { RouteConstants } from '../../../constants/route-constants'

@Component({
  components: {
    TreeContainer,
  },
})
export default class MaterialHome extends Vue {
  beforeRouteEnter(to: Route, from: Route, next: (vm: any) => void) {
    const { materialType, materialId } = to.params
    next((vm: any) => {
      vm.initNestedMaterialModule(materialType, materialId)
    })
  }

  initNestedMaterialModule(materialType: string, materialId: string) {
    NestedMaterialModule.updateMaterialType(materialType)
    if (materialId) {
      NestedMaterialModule.updateMaterialId(materialId)
    }
  }

  @Watch('$route')
  watchRoute(val: Route) {
    if (val.name === RouteConstants.MATERIAL) {
      const { materialType, materialId } = val.params
      this.initNestedMaterialModule(materialType, materialId)
    }
  }

  mounted() {
    NestedMaterialModule.loadAllNestedMaterials()
  }
}
</script>

<style lang="stylus" scoped>
.material-container
  position relative
  display flex
  flex 1
  overflow hidden
  .left-area
    position relative
    width 300px
    height 100%
    overflow hidden
  .right-area
    position relative
    flex 1
    height 100%
    overflow hidden
    // background-color gray
</style>
