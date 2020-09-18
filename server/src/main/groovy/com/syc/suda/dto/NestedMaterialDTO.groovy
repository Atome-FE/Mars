package com.syc.suda.dto

import com.syc.suda.entity.Material
import com.syc.suda.entity.NestedMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.enums.NestedMaterialType

class NestedMaterialDTO {
    String id
    String userId
    String parentId
    String name
    String materialId
    MaterialType materialType
    Material material
    NestedMaterialType type
    List<NestedMaterialDTO> children

    static NestedMaterialDTO toDTO(NestedMaterial nestedMaterial) {
        NestedMaterialDTO dto = new NestedMaterialDTO()
        dto.id = nestedMaterial.id
        dto.userId = nestedMaterial.userId
        dto.parentId = nestedMaterial.parentId
        dto.name = nestedMaterial.name
        dto.materialId = nestedMaterial.materialId
        dto.materialType = nestedMaterial.materialType
        dto.type = nestedMaterial.type
        dto.children = []
        return dto
    }
}
