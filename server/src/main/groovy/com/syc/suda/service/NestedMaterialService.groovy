package com.syc.suda.service

import com.syc.suda.dto.NestedMaterialDTO
import com.syc.suda.entity.NestedMaterial
import com.syc.suda.enums.MaterialType

interface NestedMaterialService {
    int save(NestedMaterial nestedMaterial)

    int update(NestedMaterial nestedMaterial)

    int delete(String id)

    List<NestedMaterialDTO> listByMaterialType(MaterialType materialType, String userId)

    Map<MaterialType, List<NestedMaterialDTO>> listAll(String userId)

    NestedMaterial getByMaterialId(String materialId)

    NestedMaterial getByUserId(String userId)
}