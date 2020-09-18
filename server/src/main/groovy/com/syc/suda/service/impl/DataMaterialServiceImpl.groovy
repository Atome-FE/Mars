package com.syc.suda.service.impl

import com.syc.suda.entity.DataMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.DataMaterialMapper
import com.syc.suda.service.DataMaterialService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataMaterialServiceImpl implements DataMaterialService {

    private static final String ID_PREFIX = 'D'

    @Autowired
    DataMaterialMapper dataMaterialMapper

    @Override
    int save(DataMaterial dataMaterial) {
        dataMaterial.id = IdGenerator.generate(ID_PREFIX)
        return dataMaterialMapper.save(dataMaterial)
    }

    @Override
    int update(DataMaterial dataMaterial) {
        return dataMaterialMapper.update(dataMaterial)
    }

    @Override
    DataMaterial getById(String id) {
        return dataMaterialMapper.getById(id)
    }

    @Override
    List<DataMaterial> listByIds(List<String> ids) {
        return dataMaterialMapper.listByIds(ids)
    }

    @Override
    List<DataMaterial> list(String userId) {
        List<DataMaterial> list = dataMaterialMapper.list(userId)
        list.each { it.materialType = MaterialType.DATA }
        return list
    }

    @Override
    int deleteById(String id) {
        return dataMaterialMapper.deleteById(id)
    }
}
