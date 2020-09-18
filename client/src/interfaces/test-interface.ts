export interface ITestCase {
  id: string
  name: string
  url: string
  requestData: string
  method: string
  body: string
  headers: string
  userId?: string
}

export interface IFormField {
  id: string
  fieldCategoryId: string
  fieldValue: string
}

export interface IFieldCategory {
  id: string
  dataType: string
  category: string
}

export interface ITestGroup {
  id: string
  name: string
  material?: any
  ui?: any
  concurrency?: boolean
}

export interface ITestGroupCase {
  id: string
  priority: number
  testGroupId: string
  testCaseId: string
  ui?: any
}
