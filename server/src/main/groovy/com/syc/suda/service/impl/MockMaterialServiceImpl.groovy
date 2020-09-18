package com.syc.suda.service.impl

import com.syc.suda.entity.MockMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.MockMaterialMapper
import com.syc.suda.service.MockMaterialService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MockMaterialServiceImpl implements MockMaterialService {
    private static final String ID_PREFIX = 'M'

    @Autowired
    MockMaterialMapper mapper

    @Override
    int save(MockMaterial material) {
        material.id = IdGenerator.generate(ID_PREFIX)
        return mapper.save(material)
    }

    @Override
    int update(MockMaterial material) {
        return mapper.update(material)
    }

    @Override
    MockMaterial getById(String id) {
        return mapper.getById(id)
    }

    @Override
    List<MockMaterial> listByIds(List<String> ids) {
        return mapper.listByIds(ids)
    }

    @Override
    List<MockMaterial> listMockMaterials(String userId) {
        List<MockMaterial> list = mapper.listMockMaterials(userId)
        list.each { it.materialType = MaterialType.MOCK }
        return list
    }

    @Override
    int deleteById(String id) {
        return mapper.deleteById(id)
    }
}
