import { IMaterial } from '@/interfaces/common-interface'

export interface IShareMaterialParams {
  id?: string
  shareMaterialId: string
  userId?: string
  dataMaterialId: string
}
export interface IShareMaterial {
  id?: string
  type: string // 可能的🈯值 SQL,HTTP,DATA,REDIS,MQ,MONGO,CASE
  materialId: string // 任务id，（http，sql，case这些的id）
  sharedUserIds: null | string // 如果想分享给所有人就是null，如果分享给特定的用户，需要userIds.join(',')
  material?: IMaterial
  shareMaterialParams?: IShareMaterialParams
}

export interface IShareMaterialDTO {
  CASE: IShareMaterial[]
  ENV: IShareMaterial[]
}
/*
export interface IShareMaterial extends IMaterial {
  id: string
  action: string
  name: string
  material: string
  datasource: string
  database: string
  schema?: string
}
*/
