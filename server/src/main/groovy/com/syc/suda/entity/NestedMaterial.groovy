package com.syc.suda.entity

import com.syc.suda.enums.MaterialType
import com.syc.suda.enums.NestedMaterialType
import groovy.transform.CompileStatic

@CompileStatic
class NestedMaterial {
    String id
    String userId
    String parentId
    String name
    String materialId
    MaterialType materialType
    NestedMaterialType type
}
