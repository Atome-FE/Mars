package com.syc.suda.service.impl

import com.syc.suda.entity.RedisMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.RedisMaterialMapper
import com.syc.suda.service.RedisMaterialService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RedisMaterialServiceImpl implements RedisMaterialService {

    private static final String ID_PREFIX = 'R'

    @Autowired
    RedisMaterialMapper redisMaterialMapper

    @Override
    int save(RedisMaterial redisMaterial) {
        redisMaterial.id = IdGenerator.generate(ID_PREFIX)
        return redisMaterialMapper.save(redisMaterial)
    }

    @Override
    int update(RedisMaterial redisMaterial) {
        return redisMaterialMapper.update(redisMaterial)
    }

    @Override
    RedisMaterial getById(String id) {
        return redisMaterialMapper.getById(id)
    }

    @Override
    List<RedisMaterial> listByIds(List<String> ids) {
        return redisMaterialMapper.listByIds(ids)
    }

    @Override
    List<RedisMaterial> listRedisMaterial(String userId) {
        List<RedisMaterial> list = redisMaterialMapper.listRedisMaterial(userId)
        list.each { it.materialType = MaterialType.REDIS }
        return list
    }

    @Override
    int deleteById(String id) {
        return redisMaterialMapper.deleteById(id)
    }
}
