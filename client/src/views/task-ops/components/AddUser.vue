<script lang="tsx">
import { Component as VueComponent, component } from 'vue-tsx-support'
import { Vue, Component, Prop, Emit } from 'vue-property-decorator'
import { IUser, IRole } from '@/interfaces/user-role'
import { AuthoritiesModule } from '@/store/modules/authorities'

@Component
export default class AddUser extends VueComponent<{}> {
  @Prop()
  visible!: false

  @Prop({
    type: Object,
    default: {
      name: '',
    },
  })
  form!: IUser

  selectedRoles: IRole[] = []

  rules = {
    username: [
      { required: true, message: '请输入username', trigger: 'blur' },
      { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'change' },
    ],
    email: [
      { required: true, message: '请输入email', trigger: 'change' },
      { pattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/, message: '请输入正确格式的email', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入password', trigger: 'blur' },
      { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'change' },
    ],
    roles: [
      { required: true, message: '请选择角色', trigger: 'change' },
    ],
  }

  @Emit('cancel')
  cancel() {}

  confirm() {
    (this.$refs.form as Vue & { validate: (vaild: any) => boolean }).validate((vaild: any) => {
      if (vaild) {
        this.$emit('confirm', this.form)
        return true
      }
      return false
    })
  }

  render() {
    return (
      this.visible && <el-dialog title="Add User" visible={true} onClose={() => this.cancel()} width="30%">
        <el-form props={{ model: this.form }} ref="form" rules={this.rules} label-width="90px">
          <el-form-item label="Username" prop="username">
            <el-input size="mini" v-model={this.form.username} ></el-input>
          </el-form-item>
          <el-form-item label="Email" prop="email">
            <el-input size="mini" v-model={this.form.email} ></el-input>
          </el-form-item>
          <el-form-item label="Password" prop="password">
            <el-input size="mini" v-model={this.form.password} ></el-input>
          </el-form-item>
          <el-form-item label="Role" prop="roles">
            <el-select size="mini" v-model={this.form.roles} value-key="name" multiple placeholder="请选择">
              {
                AuthoritiesModule.roles.map((item: IRole) => (
                  <el-option
                    key={item.name}
                    label={item.name}
                    v-model={item}>
                  </el-option>
                ))
              }
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button size="mini" onClick={() => this.cancel()}>取 消</el-button>
          <el-button size="mini" type="primary" onClick={() => this.confirm()}>确 定</el-button>
        </div>
      </el-dialog>
    )
  }
}
</script>

<style lang="stylus" scoped>
</style>
