import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { ISqlMaterial } from '@/interfaces/sql-interface'

@Module({ dynamic: true, store, name: 'sqlMaterial' })
class SqlMaterial extends VuexModule {
  sqlMaterials: ISqlMaterial[] = []

  @Mutation
  addSqlMaterial(payload: ISqlMaterial) {
    this.sqlMaterials.push(payload)
  }

  @Mutation
  addAllSqlMaterials(payload: ISqlMaterial[]) {
    this.sqlMaterials.splice(0)
    this.sqlMaterials.push(...payload)
  }

  @Mutation
  deleteSqlMaterial(id: string) {
    const index = this.sqlMaterials.findIndex((item: ISqlMaterial) => {
      return item.id === id
    })
    this.sqlMaterials.splice(index, 1)
  }

  @Mutation
  updateSqlMaterial(payload: ISqlMaterial) {
    const index = this.sqlMaterials.findIndex((item: ISqlMaterial) => {
      return item.id === payload.id
    })
    this.sqlMaterials.splice(index, 1, payload)
  }
}
export const SqlMaterialModule = getModule(SqlMaterial)

export const getSqlMaterialById = (id: string) => {
  const result = SqlMaterialModule.sqlMaterials.find((v: ISqlMaterial) => {
    return v.id === id
  })
  return { ...result }
}
