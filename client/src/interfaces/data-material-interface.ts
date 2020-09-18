import { IMaterial } from '@/interfaces/common-interface'

export interface IDataMaterial extends IMaterial {
  id: string
  name: string
  data: string
  describe: string
  groupName: string
  secondGroupName: string
  testGroupId?: string
}
