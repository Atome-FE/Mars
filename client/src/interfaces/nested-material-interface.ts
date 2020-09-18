import { IMaterial } from '@/interfaces/common-interface'
import { NestedMaterialType, PopoverListItemActionEnum } from '@/enums/nested-material-enum'
import { TaskTypeEnum } from '@/enums/task-enum'

type TNestedMaterialUI = {
  selected: boolean
  expanded: boolean
}
export interface INestedMaterial {
  id?: string
  userId?: string
  parentId?: string
  name?: string
  materialId?: string
  ui?: TNestedMaterialUI
  materialType?: TaskTypeEnum
  material?: IMaterial
  type?: NestedMaterialType
  parent?: INestedMaterial | null
  children?: INestedMaterial[]
}

export interface PopoverListItem {
  title: string
  label: PopoverListItemActionEnum
  action: (value: PopoverListItemActionEnum) => void
}
