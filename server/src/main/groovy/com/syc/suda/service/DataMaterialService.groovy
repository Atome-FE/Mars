package com.syc.suda.service

import com.syc.suda.entity.DataMaterial

interface DataMaterialService {

    int save(DataMaterial dataMaterial)

    int update(DataMaterial dataMaterial)

    DataMaterial getById(String id)

    List<DataMaterial> listByIds(List<String> ids)

    List<DataMaterial> list(String userId)

    int deleteById(String id)

}