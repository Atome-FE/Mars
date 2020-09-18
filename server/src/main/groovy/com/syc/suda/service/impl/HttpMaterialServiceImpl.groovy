package com.syc.suda.service.impl

import com.syc.suda.entity.HttpMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.HttpMaterialMapper
import com.syc.suda.service.HttpMaterialService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HttpMaterialServiceImpl implements HttpMaterialService {

    private static final String ID_PREFIX = 'H'

    @Autowired
    HttpMaterialMapper httpMaterialMapper

    @Override
    int save(HttpMaterial httpMaterial) {
        httpMaterial.id = IdGenerator.generate(ID_PREFIX)
        return httpMaterialMapper.save(httpMaterial)
    }

    @Override
    List<HttpMaterial> listHttpMaterial(String userId) {
        List<HttpMaterial> list = httpMaterialMapper.listHttpMaterial(userId)
        list.each { it.materialType = MaterialType.HTTP }
        return list
    }

    @Override
    List<HttpMaterial> listByIds(List<String> ids) {
        return httpMaterialMapper.listByIds(ids)
    }

    @Override
    HttpMaterial getById(String id) {
        return httpMaterialMapper.getById(id)
    }

    @Override
    int update(HttpMaterial httpMaterial) {
        return httpMaterialMapper.update(httpMaterial)
    }

    @Override
    int deleteById(String id) {
        return httpMaterialMapper.deleteById(id)
    }
}
