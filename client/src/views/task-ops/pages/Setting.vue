<script lang="tsx">
import { Component as VueComponent } from 'vue-tsx-support'
import { Vue, Component } from 'vue-property-decorator'
import { RouteConstants } from '@/constants/route-constants'

@Component
export default class Setting extends VueComponent<{}> {
  activeIndex = 1

  pageMap: any = {
    '1': RouteConstants.AUTHUSER,
    '2': RouteConstants.AUTHROLE,
  }

  pageName: any = {
    AuthUser: 1,
    AuthRole: 2,
  }

  created() {}

  mounted() {
    this.activeIndex = this.pageName[this.$route.name || '']
  }

  handleSelect(key: any, keyPath: any) {
    const pageName = this.pageMap[key]
    if (!pageName) return
    this.$router.push({
      name: pageName,
    })
  }

  render() {
    return (
      <router-view />
    )
  }
}
</script>

<style lang="stylus" scoped>
>>>.el-table__fixed
  border none !important
</style>
