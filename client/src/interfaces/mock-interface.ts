import { IMaterial } from '@/interfaces/common-interface'

export interface IMockMaterial extends IMaterial {
  url: string
  method: string
  material: string
}
