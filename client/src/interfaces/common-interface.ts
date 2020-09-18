import { TaskTypeEnum } from '@/enums/task-enum'

export interface ISelectData {
  value: string
  label: string
}

export interface IMaterial {
  [name: string]: any
  id: string
  name: string
  paramReplace?: string
  groupName?: string
  secondGroupName?: string
  materialType?: TaskTypeEnum
}
