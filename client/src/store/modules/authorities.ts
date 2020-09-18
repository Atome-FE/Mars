import {
  VuexModule,
  Module,
  Mutation,
  Action,
  getModule,
} from 'vuex-module-decorators'
  
import store from '@/store'
import { IRole } from '@/interfaces/user-role'
import { fetchRoles } from '@/apis/setting-api'

@Module({ dynamic: true, store, name: 'authorities' })
class Authorities extends VuexModule {
  roles: IRole[] = []

  @Mutation
  updateRoles(payLoad: IRole[]) {
    this.roles = payLoad
  }

  @Action
  async getRoles() {
    try {
      const { data } = await fetchRoles()
      this.updateRoles(data)
    } catch (e) {
      console.log(e)
    }
  }
}

export const AuthoritiesModule = getModule(Authorities)
