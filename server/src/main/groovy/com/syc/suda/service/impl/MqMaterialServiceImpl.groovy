package com.syc.suda.service.impl

import com.syc.suda.entity.MqMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.MqMaterialMapper
import com.syc.suda.service.MqMaterialService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MqMaterialServiceImpl implements MqMaterialService {

    private static final String ID_PREFIX = 'M'

    @Autowired
    MqMaterialMapper mqMaterialMapper

    @Override
    int save(MqMaterial mqMaterial) {
        mqMaterial.id = IdGenerator.generate(ID_PREFIX)
        return mqMaterialMapper.save(mqMaterial)
    }

    @Override
    int update(MqMaterial mqMaterial) {
        return mqMaterialMapper.update(mqMaterial)
    }

    @Override
    MqMaterial getById(String id) {
        return mqMaterialMapper.getById(id)
    }

    @Override
    List<MqMaterial> listByIds(List<String> ids) {
        return mqMaterialMapper.listByIds(ids)
    }

    @Override
    List<MqMaterial> listMqMaterials(String userId) {
        List<MqMaterial> list = mqMaterialMapper.listMqMaterials(userId)
        list.each { it.materialType = MaterialType.MQ }
        return list
    }

    @Override
    int deleteById(String id) {
        return mqMaterialMapper.deleteById(id)
    }
}
