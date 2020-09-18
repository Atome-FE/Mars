import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { IMongoMaterial } from '@/interfaces/mongo-interface'

@Module({ dynamic: true, store, name: 'mongoMaterial' })
class MongoMaterial extends VuexModule {
  mongoMaterials: IMongoMaterial[] = []

  @Mutation
  addMongoMaterial(payload: IMongoMaterial) {
    this.mongoMaterials.push(payload)
  }

  @Mutation
  addAllMongoMaterials(payload: IMongoMaterial[]) {
    this.mongoMaterials.splice(0)
    this.mongoMaterials.push(...payload)
  }

  @Mutation
  deleteMongoMaterial(id: string) {
    const index = this.mongoMaterials.findIndex((item: IMongoMaterial) => {
      return item.id === id
    })
    this.mongoMaterials.splice(index, 1)
  }

  @Mutation
  updateMongoMaterial(payload: IMongoMaterial) {
    const index = this.mongoMaterials.findIndex((item: IMongoMaterial) => {
      return item.id === payload.id
    })
    this.mongoMaterials.splice(index, 1, payload)
  }
}
export const MongoMaterialModule = getModule(MongoMaterial)

export const getMongoMaterialById = (id: string) => {
  const result = MongoMaterialModule.mongoMaterials.find((v: IMongoMaterial) => {
    return v.id === id
  })
  return { ...result }
}
