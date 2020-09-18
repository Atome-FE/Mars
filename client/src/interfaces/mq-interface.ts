import { IMaterial } from '@/interfaces/common-interface'

export interface IMqMaterial extends IMaterial {
  id: string
  name: string
  exchange: string
  routingKey: string
  content: string
  groupName: string
}
