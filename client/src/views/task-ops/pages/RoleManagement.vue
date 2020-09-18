<script lang="tsx">
import { Component as VueComponent, component } from 'vue-tsx-support'
import { Vue, Component } from 'vue-property-decorator'
import { fetchRoles } from '@/apis/setting-api'
import { IRole } from '@/interfaces/user-role'

@Component
export default class Role extends VueComponent<{}> {
  roleList: IRole[] = []

  async mounted() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const { data } = await fetchRoles()
      this.roleList = data
    } catch (error) {
      console.log(error)
    } finally {
      loading.close()
    }
  }

  handleClick(type: 'view' | 'edit', row: any) {
    console.log(row)
  }

  render() {
    return (
      <div>
        <el-table
          data={this.roleList}
          border
          style="width: 100%">
          <el-table-column
            prop="name"
            label="Name"
            width="80">
          </el-table-column>
          <el-table-column
            prop="status"
            label="status">
          </el-table-column>
        </el-table>
      </div>
    )
  }
}
</script>

<style lang="stylus" scoped>
</style>
