import { VuexModule, Module, Mutation, getModule } from 'vuex-module-decorators'

import store from '@/store'

import { setItem, getItem } from '@/lib/utils/storage-handler'

import { IEnv } from '@/interfaces/env-interface'
import { ShareMaterialModule } from './share-material-module'

@Module({ dynamic: true, store, name: 'env' })
class Env extends VuexModule {
  envs: IEnv[] = []

  selectedEnv: IEnv = (() => {
    const env = getItem('env-config')
    if (!env) {
      return {
        id: '',
        name: '',
        http: '',
        sql: '',
        redis: '',
        mq: '',
        mongo: '',
        describe: '',
      }
    }
    return JSON.parse(env)
  })()

  @Mutation
  setSelectedEnv(id: string) {
    let env = this.envs.find((v: IEnv) => {
      return v.id === id
    })
    if (!env) {
      env = ShareMaterialModule.listEnvConfig.find((v: IEnv) => {
        return v.id === id
      })
    }
    if (!env) return
    this.selectedEnv = env
    setItem('env-config', JSON.stringify(env))
  }

  @Mutation
  addEnv(payload: IEnv) {
    this.envs.push(payload)
  }

  @Mutation
  deleteEnv(id: string) {
    const index = this.envs.findIndex((item: IEnv) => {
      return item.id === id
    })
    this.envs.splice(index, 1)
  }

  @Mutation
  addAllEnvs(payload: IEnv[]) {
    this.envs.splice(0)
    this.envs.push(...payload)
  }

  @Mutation
  updateEnv(payload: IEnv) {
    const index = this.envs.findIndex((item: IEnv) => {
      return item.id === payload.id
    })
    this.envs.splice(index, 1, payload)
  }
}
export const EnvModule = getModule(Env)

export const getEnvById = (id: string) => {
  const result = EnvModule.envs.find((v: IEnv) => {
    return v.id === id
  })
  return { ...result }
}
