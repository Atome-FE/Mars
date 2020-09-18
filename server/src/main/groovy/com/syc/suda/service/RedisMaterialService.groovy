package com.syc.suda.service

import com.syc.suda.entity.RedisMaterial

interface RedisMaterialService {

    int save(RedisMaterial redisMaterial)

    int update(RedisMaterial redisMaterial)

    RedisMaterial getById(String id)

    List<RedisMaterial> listByIds(List<String> ids)

    List<RedisMaterial> listRedisMaterial(String userId)

    int deleteById(String id)

}