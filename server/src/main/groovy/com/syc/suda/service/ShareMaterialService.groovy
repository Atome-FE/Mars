package com.syc.suda.service

import com.syc.suda.dto.ShareMaterialDTO
import com.syc.suda.entity.ShareMaterial
import com.syc.suda.entity.ShareMaterialParams
import com.syc.suda.enums.MaterialType

interface ShareMaterialService {
    int save(ShareMaterial shareMaterial)

    int update(ShareMaterial shareMaterial)

    Map<MaterialType, List<ShareMaterialDTO>> listShareMaterials(String userId)

    int deleteById(String id)

    int saveShareMaterialParams(ShareMaterialParams shareMaterialParams)

    int updateShareMaterialParams(ShareMaterialParams shareMaterialParams)

    ShareMaterialParams getShareMaterialParamsByUserIdAndShareMaterialId(String userId, String shareMaterialId)

    int deleteShareMaterialParamsById(String id)

}