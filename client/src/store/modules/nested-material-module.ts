import { VuexModule, Module, Mutation, getModule, Action } from 'vuex-module-decorators'

import store from '@/store'

import { INestedMaterial } from '@/interfaces/nested-material-interface'
import { TaskTypeEnum } from '@/enums/task-enum'
import { fetchAllNestedMaterials, updateNestedMaterials } from '@/apis/nested-material'
import { TreeUtils } from '@/lib/utils/tree-utils'
import { DropTypeEnum, NestedMaterialType } from '@/enums/nested-material-enum'
import { ITaskResult } from '@/interfaces/task-interface'
import { IMaterial } from '@/interfaces/common-interface'
import { RouteConstants } from '@/constants/route-constants'
import { DataTransformUtils } from '@/lib/utils/data-transform-utils'

export type TNestedMaterials = {
  [index: string]: INestedMaterial[]
}
@Module({ dynamic: true, store, name: 'nestedMaterial' })
class NestedMaterial extends VuexModule {
  allNestedMaterials: TNestedMaterials = {
    CASE: [],
    HTTP: [],
    SQL: [],
    REDIS: [],
    MONGO: [],
    MQ: [],
    DATA: [],
    ENV: [],
    MOCK: [],
  }

  materialType: TaskTypeEnum = TaskTypeEnum.HTTP

  materialId: string = ''

  selectedNestedMaterial: INestedMaterial = {}

  taskResult: ITaskResult = {}

  get currentNestedMaterials() {
    return this.allNestedMaterials[this.materialType] || []
  }

  @Mutation
  updateMaterialId(materialId: string) {
    this.materialId = materialId
  }

  @Mutation
  updateMaterialType(materialType: string) {
    this.materialType = materialType.toUpperCase() as TaskTypeEnum
  }

  @Mutation
  setAllNestedMaterials(data: TNestedMaterials) {
    DataTransformUtils.copyProperties(this.allNestedMaterials, data)
  }

  @Mutation
  updateMaterialSelected(item: INestedMaterial) {
    const { selectedNestedMaterial } = this
    item.ui!.expanded = !item.ui!.expanded
    if (item.type === NestedMaterialType.ITEM) {
      if (selectedNestedMaterial && selectedNestedMaterial.ui) {
        selectedNestedMaterial.ui.selected = false
      }
      item.ui!.selected = !item.ui!.selected
      this.selectedNestedMaterial = item
      this.materialId = item.material!.id
    }
  }

  @Mutation
  addCatalog({
    parent,
    newItem,
    addTopCatalog,
  }: {
    parent: INestedMaterial
    newItem: INestedMaterial
    addTopCatalog: boolean
  }) {
    if (addTopCatalog) {
      if (!this.allNestedMaterials[this.materialType]) {
        this.allNestedMaterials[this.materialType] = []
      }
      this.allNestedMaterials[this.materialType].push(newItem)
    } else {
      newItem.parent = parent
      parent.children?.push(newItem)
    }
  }

  @Mutation
  updateTaskResult(taskResult: ITaskResult) {
    this.taskResult = taskResult
  }

  @Mutation
  updateMaterial(material: IMaterial) {
    this.selectedNestedMaterial.material = material
  }

  @Mutation
  updateCatalogName({ item, newName }: { item: INestedMaterial; newName: string }) {
    item.name = newName
  }

  @Mutation
  removeNode(node: INestedMaterial) {
    if (node && node.parent) {
      const index = node.parent.children?.findIndex(n => n === node)
      if (index !== -1 && index !== undefined) {
        node.parent.children?.splice(index, 1)
      }
    } else if (node) {
      const list = this.allNestedMaterials[this.materialType] || []
      const index = list.findIndex(n => n === node)
      if (index !== -1 && index !== undefined) {
        list.splice(index, 1)
      }
    }
    if (node === this.selectedNestedMaterial) {
      window.vue.$router.push({
        name: RouteConstants.MATERIAL,
        params: {
          materialType: this.materialType,
        },
      })
      this.selectedNestedMaterial = {}
    }
  }

  @Mutation
  remove({
    target,
    draggingNode,
    dropType,
  }: {
    target: INestedMaterial
    draggingNode: INestedMaterial
    dropType: DropTypeEnum
  }) {
    if (!target.parent && dropType !== DropTypeEnum.INNER) return
    if (target.type === NestedMaterialType.ITEM && dropType === DropTypeEnum.INNER) return
    if (draggingNode && draggingNode.parent) {
      const index = draggingNode.parent.children?.findIndex(n => n === draggingNode)
      if (index !== -1 && index !== undefined) {
        draggingNode.parent.children?.splice(index, 1)
      }
    } else if (draggingNode) {
      const list = this.allNestedMaterials[this.materialType] || []
      const index = list.findIndex(n => n === draggingNode)
      if (index !== -1 && index !== undefined) {
        list.splice(index, 1)
      }
    }
  }

  @Mutation
  addNode({
    target,
    newNode,
    dropType,
  }: {
    target: INestedMaterial
    newNode: INestedMaterial
    dropType: DropTypeEnum
  }) {
    if (!target.parent && dropType !== DropTypeEnum.INNER) return
    let parentId = ''
    switch (dropType) {
      case DropTypeEnum.AFTER:
      case DropTypeEnum.BEFORE:
        target.parent?.children?.push(newNode)
        parentId = target.parentId!
        break
      case DropTypeEnum.INNER:
        if (target.type === NestedMaterialType.CATALOG) {
          target.children?.push(newNode)
          parentId = target.id!
        }
        break
      default:
    }

    const params = TreeUtils.buildParams({ ...newNode, parentId: parentId })
    updateNestedMaterials(params)
  }

  @Mutation
  expandParent(node: INestedMaterial) {
    let parent = node.parent
    while (parent) {
      parent.ui!.expanded = true
      parent = parent.parent
    }
  }

  @Action
  async loadAllNestedMaterials() {
    try {
      const { data } = await fetchAllNestedMaterials()
      TreeUtils.wrapTreeData(data)
      this.setAllNestedMaterials(data)
      const initSelectedNode = TreeUtils.getNodeByMaterialId(this.currentNestedMaterials, this.materialId)
      if (initSelectedNode) {
        this.updateMaterialSelected(initSelectedNode)
        this.expandParent(initSelectedNode)
      }
    } catch (e) {
      console.log(e, 'e')
    }
  }
}
export const NestedMaterialModule = getModule(NestedMaterial)
