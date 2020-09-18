import { IMaterial } from '@/interfaces/common-interface'

export interface IRedisMaterial extends IMaterial {
  id: string
  name: string
  action: string
  key: string
  datasource: string
  database: number
  groupName: string
}

export interface IRedisAction {
  value: string
  label: string
}
