package com.syc.suda.service.impl

import com.syc.suda.entity.Env
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.EnvMapper
import com.syc.suda.service.EnvService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EnvServiceImpl implements EnvService {

    private static final String ID_PREFIX = 'E'

    @Autowired
    EnvMapper envMapper

    @Override
    int save(Env env) {
        env.id = IdGenerator.generate(ID_PREFIX)
        return envMapper.save(env)
    }

    @Override
    int update(Env env) {
        return envMapper.update(env)
    }

    @Override
    Env getById(String id) {
        return envMapper.getById(id)
    }

    @Override
    List<Env> listByIds(List<String> ids) {
        return envMapper.listByIds(ids)
    }

    @Override
    List<Env> listEnv(String userId) {
        List<Env> list = envMapper.listEnv(userId)
        list.each { it.materialType = MaterialType.ENV }
        return list
    }

    @Override
    int deleteById(String id) {
        return envMapper.deleteById(id)
    }
}
