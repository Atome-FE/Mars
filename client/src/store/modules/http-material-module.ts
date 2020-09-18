import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { IHttpMaterial } from '@/interfaces/http-interface'

@Module({ dynamic: true, store, name: 'httpMaterial' })
class HttpMaterial extends VuexModule {
  httpMaterials: IHttpMaterial[] = []

  @Mutation
  getHttpMaterialById(id: string) {
    return this.httpMaterials.find((v: IHttpMaterial) => {
      return v.id === id
    })
  }

  @Mutation
  addHttpMaterial(payload: IHttpMaterial) {
    this.httpMaterials.push(payload)
  }

  @Mutation
  deleteHttpMaterial(id: string) {
    const index = this.httpMaterials.findIndex((item: IHttpMaterial) => {
      return item.id === id
    })
    this.httpMaterials.splice(index, 1)
  }

  @Mutation
  addAllHttpMaterials(payload: IHttpMaterial[]) {
    this.httpMaterials.splice(0)
    this.httpMaterials.push(...payload)
  }

  @Mutation
  updateHttpMaterial(payload: IHttpMaterial) {
    const index = this.httpMaterials.findIndex((item: IHttpMaterial) => {
      return item.id === payload.id
    })
    this.httpMaterials.splice(index, 1, payload)
  }
}

export const HttpMaterialModule = getModule(HttpMaterial)

export const getHttpMaterialById = (id: string) => {
  const result = HttpMaterialModule.httpMaterials.find((v: IHttpMaterial) => {
    return v.id === id
  })
  return { ...result }
}
