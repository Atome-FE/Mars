package com.syc.suda.service

import com.syc.suda.entity.Env

interface EnvService {

    int save(Env env)

    int update(Env env)

    Env getById(String id)

    List<Env> listByIds(List<String> ids)

    List<Env> listEnv(String userId)

    int deleteById(String id)

}