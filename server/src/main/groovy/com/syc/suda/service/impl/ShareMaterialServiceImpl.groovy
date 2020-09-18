package com.syc.suda.service.impl

import com.syc.suda.dto.ShareMaterialDTO
import com.syc.suda.entity.Material
import com.syc.suda.entity.ShareMaterial
import com.syc.suda.entity.ShareMaterialParams
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.ShareMaterialMapper
import com.syc.suda.mapper.ShareMaterialParamsMapper
import com.syc.suda.service.DataMaterialService
import com.syc.suda.service.EnvService
import com.syc.suda.service.HttpMaterialService
import com.syc.suda.service.MqMaterialService
import com.syc.suda.service.RedisMaterialService
import com.syc.suda.service.ShareMaterialService
import com.syc.suda.service.SqlMaterialService
import com.syc.suda.service.TestCaseService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShareMaterialServiceImpl implements ShareMaterialService {
    private static final String ID_PREFIX = 'S'

    @Autowired
    ShareMaterialMapper shareMaterialMapper

    @Autowired
    ShareMaterialParamsMapper shareMaterialParamsMapper

    @Autowired
    HttpMaterialService httpMaterialService

    @Autowired
    DataMaterialService dataMaterialService

    @Autowired
    SqlMaterialService sqlMaterialService

    @Autowired
    RedisMaterialService redisMaterialService

    @Autowired
    MongoMaterialServiceImpl mongoMaterialService

    @Autowired
    MqMaterialService mqMaterialService

    @Autowired
    TestCaseService testCaseService

    @Autowired
    EnvService envService

    @Override
    int save(ShareMaterial shareMaterial) {
        shareMaterial.id = IdGenerator.generate(ID_PREFIX)
        return shareMaterialMapper.save(shareMaterial)
    }

    @Override
    int update(ShareMaterial shareMaterial) {
        return shareMaterialMapper.update(shareMaterial)
    }

    @Override
    Map<MaterialType, List<ShareMaterialDTO>> listShareMaterials(String userId) {
        Map<MaterialType, List<ShareMaterialDTO>> result = [:]
        List<ShareMaterial> shareMaterials = shareMaterialMapper.list(userId)
        shareMaterials.each {
            ShareMaterialDTO dto = ShareMaterialDTO.toDTO(it)
            Material material = null
            switch (it.type) {
                case MaterialType.HTTP:
                    material = httpMaterialService.getById(it.materialId)
                    break
                case MaterialType.SQL:
                    material = sqlMaterialService.getById(it.materialId)
                    break
                case MaterialType.REDIS:
                    material = redisMaterialService.getById(it.materialId)
                    break
                case MaterialType.DATA:
                    material = dataMaterialService.getById(it.materialId)
                    break
                case MaterialType.MQ:
                    material = mqMaterialService.getById(it.materialId)
                    break
                case MaterialType.MONGO:
                    material = mongoMaterialService.getById(it.materialId)
                    break
                case MaterialType.CASE:
                    material = testCaseService.getById(it.materialId)
                    break
                case MaterialType.ENV:
                    material = envService.getById(it.materialId)
                    break
            }
            if (!result[it.type]) {
                result[it.type] = []
            }
            dto.material = material
            dto.shareMaterialParams = shareMaterialParamsMapper.getByUserIdAndShareMaterialId(userId, it.id)
            result[it.type] << dto
        }
        return result
    }

    @Override
    int deleteById(String id) {
        return shareMaterialMapper.deleteById(id)
    }

    @Override
    int saveShareMaterialParams(ShareMaterialParams shareMaterialParams) {
        shareMaterialParams.id = IdGenerator.generate(ID_PREFIX)
        return shareMaterialParamsMapper.save(shareMaterialParams)
    }

    @Override
    int updateShareMaterialParams(ShareMaterialParams shareMaterialParams) {
        return shareMaterialParamsMapper.update(shareMaterialParams)
    }

    @Override
    ShareMaterialParams getShareMaterialParamsByUserIdAndShareMaterialId(String userId, String shareMaterialId) {
        return shareMaterialParamsMapper.getByUserIdAndShareMaterialId(userId, shareMaterialId)
    }

    @Override
    int deleteShareMaterialParamsById(String id) {
        return shareMaterialParamsMapper.deleteById(id)
    }
}
