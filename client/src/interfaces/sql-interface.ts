import { IMaterial } from '@/interfaces/common-interface'

export interface ISqlMaterial extends IMaterial {
  id: string
  name: string
  material: string
  datasource: string
  database: string
  groupName: string
  testGroupId?: string
}
