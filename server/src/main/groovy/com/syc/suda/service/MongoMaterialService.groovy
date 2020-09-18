package com.syc.suda.service

import com.syc.suda.entity.MongoMaterial
import groovy.transform.CompileStatic

@CompileStatic
interface MongoMaterialService {

    int save(MongoMaterial mongoMaterial)

    int update(MongoMaterial mongoMaterial)

    MongoMaterial getById(String id)

    List<MongoMaterial> listByIds(List<String> ids)

    List<MongoMaterial> listMongoMaterials(String userId)

    int delete(String id)

}