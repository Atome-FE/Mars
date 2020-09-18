package com.syc.suda.service

import com.syc.suda.entity.HttpMaterial

interface HttpMaterialService {
    int save(HttpMaterial httpMaterial)

    List<HttpMaterial> listHttpMaterial(String userId)

    List<HttpMaterial> listByIds(List<String> ids)

    HttpMaterial getById(String id)

    int update(HttpMaterial httpMaterial)

    int deleteById(String id)
}