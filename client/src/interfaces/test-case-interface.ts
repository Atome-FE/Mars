import { IMaterial } from './common-interface'

export interface ITestCase extends IMaterial {
  // id: string
  // name: string
  material: string
  // groupName: string
  userId?: string
  concurrency?: boolean
}
