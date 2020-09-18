import { TNestedMaterials } from '@/store/modules/nested-material-module'
import { INestedMaterial } from '@/interfaces/nested-material-interface'
import { NestedMaterialType } from '@/enums/nested-material-enum'
import { TaskTypeEnum } from '@/enums/task-enum'
import { IMaterial } from '@/interfaces/common-interface'

export class TreeUtils {
  static traverseGetMaterial(nestedMaterials: INestedMaterial[], arr: IMaterial[]) {
    nestedMaterials.forEach(v => {
      if (v.type === NestedMaterialType.ITEM) {
        arr.push(v.material!)
      } else {
        this.traverseGetMaterial(v.children || [], arr)
      }
    })
  }

  static getMaterialsByMaterialType(allNestedMaterials: TNestedMaterials, materialType: TaskTypeEnum) {
    const nestedMaterials = allNestedMaterials[materialType]
    const arr: IMaterial[] = []
    this.traverseGetMaterial(nestedMaterials || [], arr)
    return arr
  }

  static buildParams(data: INestedMaterial) {
    const result: INestedMaterial = {
      name: data.name,
      type: data.type,
      materialId: data.materialId,
      materialType: data.materialType,
      id: data.id,
      userId: data.userId,
      parentId: data.parentId,
      children: [],
    }
    return result
  }

  static findNearestComponent(element: any, componentName: any) {
    let target = element
    while (target && target.tagName !== 'BODY') {
      if (target.__vue__ && target.__vue__.$options.name === componentName) {
        return target.__vue__
      }
      target = target.parentNode
    }
    return null
  }

  static wrapSingleNode(data: INestedMaterial) {
    Object.assign(data, {
      ui: {
        selected: false,
        expanded: false,
      },
    })
    data.children = data.children || []
  }

  static sorted(arr?: INestedMaterial[]) {
    if (!arr || !arr.length) {
      return
    }
    arr.sort((a, b) => {
      if (a.type === NestedMaterialType.CATALOG) {
        return -1
      }
      return 1
    })
    arr.forEach(child => this.sorted(child.children))
  }

  static wrapTreeData(data: TNestedMaterials) {
    Object.keys(data).forEach(key => {
      data[key].forEach(item => {
        this.traverse(item, null)
      })
      this.sorted(data[key])
    })
  }

  /*
  static expandAllParent(node: INestedMaterial) {
    console.log(node, 'node')
    if (node.parent) {
      node.parent.ui!.expanded = true
      this.expandAllParent(node.parent)
    }
  }
  */

  static getNodeByMaterialId(arr: INestedMaterial[], materialId: string): INestedMaterial | undefined {
    let result: INestedMaterial | undefined
    for (const node of arr) {
      if (node.material && node.material.id === materialId) {
        return node
      }
      result = this.getNodeByMaterialId(node.children || [], materialId)
      if (result) return result
    }
    return undefined
  }

  static traverse(node: INestedMaterial, parentNode: INestedMaterial | null) {
    this.wrapSingleNode(node)
    node.parent = parentNode
    node.children?.forEach(item => {
      this.traverse(item, node)
    })
  }
}
