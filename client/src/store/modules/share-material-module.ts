import { VuexModule, Module, Mutation, getModule, Action } from 'vuex-module-decorators'

import store from '@/store'

import { fetchShareMaterials, saveShareMaterial, updateShareMaterial, deleteShareMaterial } from '@/apis/share-api'

import { IShareMaterial, IShareMaterialDTO } from '@/interfaces/share-interface'
import { IEnv } from '@/interfaces/env-interface'
import { UserModule } from './user-module'
import { EnvModule } from '@/store/modules/env-module'

@Module({ dynamic: true, store, name: 'shareMaterial' })
class shareMaterial extends VuexModule {
  shareMaterials: IShareMaterialDTO = {
    CASE: [],
    ENV: [],
  }

  get listShareMaterial(): IShareMaterial[] {
    return Object.values(this.shareMaterials).flat()
  }

  get allShareMaterialIds(): Array<string> {
    return Object.values(this.shareMaterials)
      .flat()
      .map((x: any) => x.materialId)
  }

  get listEnvConfig() {
    const ENV = this.shareMaterials.ENV
    if (!ENV) {
      return []
    }
    const list = ENV.map(env => env.material).filter(env => EnvModule.envs.every(v => v.id !== env?.id)) as IEnv[]
    return list
  }

  @Mutation
  updateModule(data: IShareMaterialDTO) {
    this.shareMaterials = data
  }

  @Action
  async add(payload: IShareMaterial) {
    await saveShareMaterial(payload)
    this.initData()
  }

  @Action
  async del(payload: IShareMaterial) {
    await deleteShareMaterial(payload)
    this.initData()
  }

  @Action
  async initData() {
    const { data } = await fetchShareMaterials()
    this.updateModule(data)
  }
}
export const ShareMaterialModule = getModule(shareMaterial)

export const getShareEnvConfigById = (id: string) => {
  console.log(UserModule.userId, 'userId')
  return ShareMaterialModule.listEnvConfig.find(v => v.id === id && UserModule.userId !== v.userId)
}

export const getShareMaterialByMaterialId = (materialId: string) => {
  const obj = Object.values(ShareMaterialModule.shareMaterials)
    .flat()
    .find(material => material.materialId === materialId)
  return obj
}
