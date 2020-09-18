package com.syc.suda.dto

import com.syc.suda.entity.Material
import com.syc.suda.entity.ShareMaterial
import com.syc.suda.entity.ShareMaterialParams

class ShareMaterialDTO extends ShareMaterial {
    Material material
    ShareMaterialParams shareMaterialParams

    static ShareMaterialDTO toDTO(ShareMaterial shareMaterial) {
        if (!shareMaterial) {
            return null
        } else {
            ShareMaterialDTO dto = new ShareMaterialDTO()
            dto.id = shareMaterial.id
            dto.userId = shareMaterial.userId
            dto.type = shareMaterial.type
            dto.sharedUserIds = shareMaterial.sharedUserIds
            dto.materialId = shareMaterial.materialId
            dto.userEmail = shareMaterial.userEmail
            dto.createTimestamp = shareMaterial.createTimestamp
            return dto
        }
    }
}
