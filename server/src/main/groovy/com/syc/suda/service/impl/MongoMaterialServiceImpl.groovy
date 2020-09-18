package com.syc.suda.service.impl

import com.syc.suda.entity.MongoMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.MongoMaterialMapper
import com.syc.suda.service.MongoMaterialService
import com.syc.suda.util.IdGenerator
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class MongoMaterialServiceImpl implements MongoMaterialService {

    private static final String ID_PREFIX = 'M'

    @Autowired
    MongoMaterialMapper mongoMaterialMapper

    @Override
    int save(MongoMaterial mongoMaterial) {
        mongoMaterial.id = IdGenerator.generate(ID_PREFIX)
        return mongoMaterialMapper.save(mongoMaterial)
    }

    @Override
    int update(MongoMaterial mongoMaterial) {
        return mongoMaterialMapper.update(mongoMaterial)
    }

    @Override
    MongoMaterial getById(String id) {
        return mongoMaterialMapper.getById(id)
    }

    @Override
    List<MongoMaterial> listByIds(List<String> ids) {
        return mongoMaterialMapper.listByIds(ids)
    }

    @Override
    List<MongoMaterial> listMongoMaterials(String userId) {
        List<MongoMaterial> list = mongoMaterialMapper.listMongoMaterials(userId)
        list.each { it.materialType = MaterialType.MONGO }
        return list
    }

    @Override
    int delete(String id) {
        return mongoMaterialMapper.delete(id)
    }
}
