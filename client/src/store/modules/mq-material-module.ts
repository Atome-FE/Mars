import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { IMqMaterial } from '@/interfaces/mq-interface'

@Module({ dynamic: true, store, name: 'mqMaterial' })
class MqMaterial extends VuexModule {
  mqMaterials: IMqMaterial[] = []

  @Mutation
  addMqMaterial(payload: IMqMaterial) {
    this.mqMaterials.push(payload)
  }

  @Mutation
  addAllMqMaterials(payload: IMqMaterial[]) {
    this.mqMaterials.splice(0)
    this.mqMaterials.push(...payload)
  }

  @Mutation
  deleteMqMaterial(id: string) {
    const index = this.mqMaterials.findIndex((item: IMqMaterial) => {
      return item.id === id
    })
    this.mqMaterials.splice(index, 1)
  }

  @Mutation
  updateMqMaterial(payload: IMqMaterial) {
    const index = this.mqMaterials.findIndex((item: IMqMaterial) => {
      return item.id === payload.id
    })
    this.mqMaterials.splice(index, 1, payload)
  }
}
export const MqMaterialModule = getModule(MqMaterial)

export const getMqMaterialById = (id: string) => {
  const result = MqMaterialModule.mqMaterials.find((v: IMqMaterial) => {
    return v.id === id
  })
  return { ...result }
}
