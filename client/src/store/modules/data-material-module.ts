import { VuexModule, Module, Mutation, getModule } from 'vuex-module-decorators'

import store from '@/store'

import { IDataMaterial } from '@/interfaces/data-material-interface'

@Module({ dynamic: true, store, name: 'dataMaterial' })
class DataMaterial extends VuexModule {
  dataMaterials: IDataMaterial[] = []

  @Mutation
  getDataMaterialById(id: string) {
    return this.dataMaterials.find((v: IDataMaterial) => {
      return v.id === id
    })
  }

  @Mutation
  addDataMaterial(payload: IDataMaterial) {
    this.dataMaterials.push(payload)
  }

  @Mutation
  deleteDataMaterial(id: string) {
    const index = this.dataMaterials.findIndex((item: IDataMaterial) => {
      return item.id === id
    })
    this.dataMaterials.splice(index, 1)
  }

  @Mutation
  addAllDataMaterials(payload: IDataMaterial[]) {
    this.dataMaterials.splice(0)
    this.dataMaterials.push(...payload)
  }

  @Mutation
  updateDataMaterial(payload: IDataMaterial) {
    const index = this.dataMaterials.findIndex((item: IDataMaterial) => {
      return item.id === payload.id
    })
    this.dataMaterials.splice(index, 1, payload)
  }
}

export const DataMaterialModule = getModule(DataMaterial)

export const getDataMaterialById = (id: string) => {
  const result = DataMaterialModule.dataMaterials.find((v: IDataMaterial) => {
    return v.id === id
  })
  return { ...result }
}
