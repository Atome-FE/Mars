import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { IRedisMaterial } from '@/interfaces/redis-interface'

@Module({ dynamic: true, store, name: 'redisMaterial' })
class RedisMaterial extends VuexModule {
  redisMaterials: IRedisMaterial[] = []

  @Mutation
  addRedisMaterial(payload: IRedisMaterial) {
    this.redisMaterials.push(payload)
  }

  @Mutation
  deleteRedisMaterial(id: string) {
    const index = this.redisMaterials.findIndex((item: IRedisMaterial) => {
      return item.id === id
    })
    this.redisMaterials.splice(index, 1)
  }

  @Mutation
  addAllRedisMaterials(payload: IRedisMaterial[]) {
    this.redisMaterials.splice(0)
    this.redisMaterials.push(...payload)
  }

  @Mutation
  updateRedisMaterial(payload: IRedisMaterial) {
    const index = this.redisMaterials.findIndex((item: IRedisMaterial) => {
      return item.id === payload.id
    })
    this.redisMaterials.splice(index, 1, payload)
  }
}
export const RedisMaterialModule = getModule(RedisMaterial)

export const getRedisMaterialById = (id: string) => {
  const result = RedisMaterialModule.redisMaterials.find((v: IRedisMaterial) => {
    return v.id === id
  })
  return { ...result }
}
