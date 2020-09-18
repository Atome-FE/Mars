import { VuexModule, Module, Mutation, getModule } from 'vuex-module-decorators'

import store from '@/store'

import { ITestCase, ITestGroup, ITestGroupCase, IFieldCategory, IFormField } from '@/interfaces/test-interface'

@Module({ dynamic: true, store, name: 'fieldModule' })
class Field extends VuexModule {
  fieldCategoies: IFieldCategory[] = []

  formFields: IFormField[] = []

  testCases: ITestCase[] = []

  testGroups: ITestGroup[] = []

  testGroupCases: ITestGroupCase[] = []

  @Mutation
  addAllTestGroup(payload: ITestGroup[]) {
    this.testGroups.splice(0)
    this.testGroups.push(...payload)
  }

  @Mutation
  addAllFieldCategory(payload: IFieldCategory[]) {
    this.fieldCategoies.splice(0)
    this.fieldCategoies.push(...payload)
  }

  @Mutation
  addAllFormField(payload: IFormField[]) {
    this.formFields.splice(0)
    this.formFields.push(...payload)
  }

  @Mutation
  addAllTestCase(payload: ITestCase[]) {
    this.testCases.push(...payload)
  }

  @Mutation
  addAllTestGroupCases(payload: ITestGroupCase[]) {
    this.testGroupCases.splice(0)
    this.testGroupCases.push(...payload)
  }

  @Mutation
  addTestGroupCase(payload: ITestGroupCase) {
    this.testGroupCases.push(payload)
  }

  @Mutation
  deleteTestGroupCase(payload: ITestGroupCase) {
    const index = this.testGroupCases.findIndex((item: ITestGroupCase) => {
      return item.id === payload.id
    })
    this.testGroupCases.splice(index, 1)
  }

  @Mutation
  updateTestGroupCasePriority(payload: ITestGroupCase) {
    this.testGroupCases.forEach((item: ITestGroupCase) => {
      if (item.id === payload.id) {
        item.priority = payload.priority
      }
    })
  }

  @Mutation
  addTestGroup(payload: ITestGroup) {
    this.testGroups.push(payload)
  }

  @Mutation
  updateTestGroup(payload: ITestGroup) {
    const index = this.testGroups.findIndex((item: ITestGroup) => {
      return item.id === payload.id
    })
    console.log(index, 'index', payload)
    this.testGroups.splice(index, 1, payload)
  }

  @Mutation
  deleteTestGroup(id: string) {
    const index = this.testGroups.findIndex((item: ITestGroup) => {
      return item.id === id
    })
    this.testGroups.splice(index, 1)
  }

  @Mutation
  addFieldCategory(payload: IFieldCategory) {
    this.fieldCategoies.push(payload)
  }

  @Mutation
  addFormField(payload: IFormField) {
    this.formFields.push(payload)
  }

  @Mutation
  addTestCase(payload: ITestCase) {
    this.testCases.push(payload)
  }
}

export const FieldModule = getModule(Field)
