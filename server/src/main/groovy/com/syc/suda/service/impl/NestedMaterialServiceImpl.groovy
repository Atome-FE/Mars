package com.syc.suda.service.impl

import com.syc.suda.dto.NestedMaterialDTO
import com.syc.suda.entity.Material
import com.syc.suda.entity.NestedMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.NestedMaterialMapper
import com.syc.suda.service.DataMaterialService
import com.syc.suda.service.EnvService
import com.syc.suda.service.HttpMaterialService
import com.syc.suda.service.MockMaterialService
import com.syc.suda.service.MongoMaterialService
import com.syc.suda.service.MqMaterialService
import com.syc.suda.service.NestedMaterialService
import com.syc.suda.service.RedisMaterialService
import com.syc.suda.service.SqlMaterialService
import com.syc.suda.service.TestCaseService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NestedMaterialServiceImpl implements NestedMaterialService {
    @Autowired
    NestedMaterialMapper mapper
    @Autowired
    HttpMaterialService httpMaterialService
    @Autowired
    SqlMaterialService sqlMaterialService
    @Autowired
    RedisMaterialService redisMaterialService
    @Autowired
    MqMaterialService mqMaterialService
    @Autowired
    MongoMaterialService mongoMaterialService
    @Autowired
    DataMaterialService dataMaterialService
    @Autowired
    EnvService envService
    @Autowired
    TestCaseService testCaseService
    @Autowired
    MockMaterialService mockMaterialService

    static final String PRE_FIX = 'N'

    @Override
    int save(NestedMaterial nestedMaterial) {
        nestedMaterial.id = IdGenerator.generate(PRE_FIX)
        return mapper.save(nestedMaterial)
    }

    @Override
    int update(NestedMaterial nestedMaterial) {
        return mapper.update(nestedMaterial)
    }

    @Override
    int delete(String id) {
        return mapper.delete(id)
    }

    @Override
    List<NestedMaterialDTO> listByMaterialType(MaterialType materialType, String userId) {
        List<NestedMaterial> nestedMaterials = mapper.listByMaterialType(materialType, userId)
        if (!nestedMaterials) {
            return Collections.emptyList()
        }
        return handleNestedMaterial(nestedMaterials)
    }

    @Override
    Map<MaterialType, List<NestedMaterialDTO>> listAll(String userId) {
        List<NestedMaterial> nestedMaterials = mapper.listAll(userId)
        Map<MaterialType, List<NestedMaterial>> map = nestedMaterials.groupBy { it.materialType }
        Map<MaterialType, List<NestedMaterialDTO>> result = [:]
        map.each {
            result[it.key] = handleNestedMaterial(it.value)
        }
        return result
    }

    @Override
    NestedMaterial getByMaterialId(String materialId) {
        return null
    }

    @Override
    NestedMaterial getByUserId(String userId) {
        return null
    }

    private List<NestedMaterialDTO> handleNestedMaterial(List<NestedMaterial> nestedMaterials) {
        List<NestedMaterialDTO> nestedMaterialDTOS = nestedMaterials.collect { NestedMaterialDTO.toDTO(it) }
        attachMaterial(nestedMaterialDTOS)
        List<NestedMaterialDTO> result = buildTree(nestedMaterialDTOS)
        return result
    }

    private void attachMaterial(List<NestedMaterialDTO> dtos) {
        List<String> materialIds = dtos.collect { it.materialId }.findAll { it != null}
        if (!materialIds) {
            return
        }
        List<Material> materials
        MaterialType materialType = dtos[0].materialType
        switch (materialType) {
            case MaterialType.HTTP:
                materials = httpMaterialService.listByIds(materialIds)
                break
            case MaterialType.SQL:
                materials = sqlMaterialService.listByIds(materialIds)
                break
            case MaterialType.REDIS:
                materials = redisMaterialService.listByIds(materialIds)
                break
            case MaterialType.ENV:
                materials = envService.listByIds(materialIds)
                break
            case MaterialType.MONGO:
                materials = mongoMaterialService.listByIds(materialIds)
                break
            case MaterialType.MQ:
                materials = mqMaterialService.listByIds(materialIds)
                break
            case MaterialType.DATA:
                materials = dataMaterialService.listByIds(materialIds)
                break
            case MaterialType.CASE:
                materials = testCaseService.listByIds(materialIds)
                break
            case MaterialType.MOCK:
                materials = mockMaterialService.listByIds(materialIds)
        }
        dtos.each { dto ->
            if (dto.materialId) {
                dto.material = materials.find { it.id == dto.materialId }
            }
        }
    }

    static private List<NestedMaterialDTO> buildTree(List<NestedMaterialDTO> dtos) {
        HashMap<String, NestedMaterialDTO> map = new HashMap<>()
        dtos.each {
            map.put(it.id, it)
        }
        dtos.each {
            if (it.parentId) {
                NestedMaterialDTO dto = map.get(it.parentId)
                dto.children << it
            }
        }
        List<NestedMaterialDTO> result = map.collect {
            if (!it.value.parentId) {
                return it.value
            }
        }.findAll { it != null }
        return result
    }

}
