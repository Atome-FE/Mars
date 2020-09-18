<template>
  <div id="app">
    <div class="navigator">
      <NewNavigator :active="'10'" />
    </div>
    <keep-alive>
      <router-view v-if="!$route.meta.reload"></router-view>
    </keep-alive>
    <router-view v-if="$route.meta.reload"></router-view>
  </div>
</template>
<script lang="tsx">
import { Component, Vue } from 'vue-property-decorator'
import NewNavigator from '@/components/NewNavigator.vue'

import { TNotification } from '@/lib/utils/notification'
import { Route } from 'vue-router'
import { NestedMaterialModule } from './store/modules/nested-material-module'

@Component({
  components: {
    NewNavigator,
  },
})
export default class App extends Vue {
  async created() {
    TNotification.requestPermission()
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    loading.close()
    NestedMaterialModule.loadAllNestedMaterials()
  }
}
</script>

<style lang="stylus">
#app
  position relative
  display flex
  height 100%
  color #2c3e50
  background-color $bg-color2
#nav
  padding 30px
  a
    font-weight bold
    color #2c3e50
    &.router-link-exact-active
      color #42b983
</style>
