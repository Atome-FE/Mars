import { IMaterial } from '@/interfaces/common-interface'

export interface IDocument extends IMaterial {
  content: string
  mdContent: string
  extId: string
  lock: number
}
