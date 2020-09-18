package com.syc.suda.service

import com.syc.suda.entity.MockMaterial

interface MockMaterialService {
    int save(MockMaterial material)

    int update(MockMaterial material)

    MockMaterial getById(String id)

    List<MockMaterial> listByIds(List<String> ids)

    List<MockMaterial> listMockMaterials(String userId)

    int deleteById(String id)

}