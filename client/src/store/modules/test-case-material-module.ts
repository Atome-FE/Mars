import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { ITestCase } from '@/interfaces/test-case-interface'

@Module({ dynamic: true, store, name: 'testCaseMaterial' })
class TestCaseMaterial extends VuexModule {
  testCaseMaterials: ITestCase[] = []

  @Mutation
  addTestCase(payload: ITestCase) {
    this.testCaseMaterials.push(payload)
  }

  @Mutation
  addAllTestCaseMaterials(payload: ITestCase[]) {
    this.testCaseMaterials.splice(0)
    this.testCaseMaterials.push(...payload)
  }

  @Mutation
  deleteTestCaseMaterial(id: string) {
    const index = this.testCaseMaterials.findIndex((item: ITestCase) => {
      return item.id === id
    })
    this.testCaseMaterials.splice(index, 1)
  }

  @Mutation
  updateTestCase(payload: ITestCase) {
    const index = this.testCaseMaterials.findIndex((item: ITestCase) => {
      return item.id === payload.id
    })
    this.testCaseMaterials.splice(index, 1, payload)
  }
}
export const TestCaseMaterialModule = getModule(TestCaseMaterial)

export const getTestCaseMaterialById = (id: string) => {
  const result = TestCaseMaterialModule.testCaseMaterials.find((v: ITestCase) => {
    return v.id === id
  })
  return { ...result } as ITestCase
}
