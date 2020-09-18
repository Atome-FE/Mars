import { IMaterial } from '@/interfaces/common-interface'

export interface IMongoMaterial extends IMaterial {
  id: string
  action: string
  name: string
  material: string
  datasource: string
  database: string
  schema?: string
}
