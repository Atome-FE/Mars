package com.syc.suda.service

import com.syc.suda.entity.MqMaterial

interface MqMaterialService {

    int save(MqMaterial mqMaterial)

    int update(MqMaterial mqMaterial)

    MqMaterial getById(String id)

    List<MqMaterial> listByIds(List<String> ids)

    List<MqMaterial> listMqMaterials(String userId)

    int deleteById(String id)

}