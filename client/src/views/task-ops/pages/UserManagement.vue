<script lang="tsx">
import { Component as VueComponent, component } from 'vue-tsx-support'
import { Vue, Component } from 'vue-property-decorator'
import { VueConstructor } from 'vue'
import AddUser from '../components/AddUser.vue'
import { fetchUsers, addUser } from '@/apis/setting-api'
import { AuthoritiesModule } from '@/store/modules/authorities'
import { IUser, IRole } from '@/interfaces/user-role'

const formData: IUser = {
  id: '',
  username: '',
  email: '',
  status: 'ENABLED',
  password: '',
  createTimestamp: 0,
  updateTimestamp: 0,
  roles: [],
}
@Component({
  components: {
    AddUser,
  },
})
export default class User extends VueComponent<any> {
  userList: IUser[] = []

  currentForm: IUser = { ...formData }

  addUserVisible = false

  async mounted() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const { data } = await fetchUsers()
      this.userList = data
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
    AuthoritiesModule.getRoles()
  }

  addUser(falg: boolean) {
    this.addUserVisible = true
  }

  cancelAddUser() {
    this.addUserVisible = false
  }

  async confirmForm(form: any) {
    this.addUserVisible = false
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const { data } = await addUser(form)
      this.$notify.success({
        message: '用户添加成功',
        title: '成功',
      })
      this.currentForm = { ...formData }
      this.userList.push(Object.assign(form, { id: data }))
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  render() {
    return (
      <div class="material-main material-main-flex-1">
        <el-button class="margin-btm20" size="mini" type="primary" onClick={() => this.addUser(true)}>
          Add User
        </el-button>
        <add-user
          form={this.currentForm}
          visible={this.addUserVisible}
          onCancel={this.cancelAddUser}
          onConfirm={this.confirmForm}
        />
        <el-table
          data={this.userList}
          border
          style="width: 100%"
          max-height="680">
          <el-table-column
            fixed
            prop="id"
            label="ID">
          </el-table-column>
          <el-table-column
            fixed
            prop="username"
            label="Name"
            width="120">
          </el-table-column>
          <el-table-column
            fixed
            prop="email"
            label="email"
            width="180">
          </el-table-column>
          <el-table-column
            fixed
            prop="status"
            label="Status"
            width="100">
          </el-table-column>
          <el-table-column
            fixed
            label="Role List"
            width="180" scopedSlots={
              {
                default: (scope: any) => {
                  return (
                    scope.row.roles.map((role: IRole) => {
                      return <el-tag size="small">
                        {role.name}
                      </el-tag>
                    })
                  )
                },
              }
            }>
          </el-table-column>
        </el-table>
      </div>
    )
  }
}
</script>

<style lang="stylus" scoped>
>>> .el-table__fixed
  border-top: 1px solid #EBEEF5;
  border-left: 1px solid #EBEEF5;
  border-right: 1px solid #EBEEF5;
  box-sizing border-box
>>> .el-tag
  padding-left 10px
  padding-right 10px
  margin-right 10px
.margin-btm20
  margin-bottom 20px
  width 100px
</style>
