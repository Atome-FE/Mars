import { VuexModule, Module, Mutation, getModule } from 'vuex-module-decorators'

import { setItem, getItem } from '@/lib/utils/storage-handler'

import store from '@/store'

import { RoleTypeEnum } from '@/enums/user-enum'

@Module({ dynamic: true, store, name: 'user' })
class User extends VuexModule {
  userId: string = (() => {
    return getItem('user-id') || ''
  })()

  role: RoleTypeEnum | '' = ''

  @Mutation
  setUserId(payload: string) {
    this.userId = payload
    setItem('user-id', payload)
  }

  @Mutation
  setRole(payload: RoleTypeEnum) {
    this.role = payload
  }
}

export const UserModule = getModule(User)
