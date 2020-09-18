package com.syc.suda.service.impl

import com.syc.suda.entity.SqlMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.SqlMaterialMapper
import com.syc.suda.service.SqlMaterialService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SqlMaterialServiceImpl implements SqlMaterialService {

    private static final String ID_PREFIX = 'S'

    @Autowired
    SqlMaterialMapper sqlMaterialMapper

    @Override
    int save(SqlMaterial sqlMaterial) {
        sqlMaterial.id = IdGenerator.generate(ID_PREFIX)
        return sqlMaterialMapper.save(sqlMaterial)
    }

    @Override
    int update(SqlMaterial sqlMaterial) {
        return sqlMaterialMapper.update(sqlMaterial)
    }

    @Override
    SqlMaterial getById(String id) {
        return sqlMaterialMapper.getById(id)
    }

    @Override
    List<SqlMaterial> listByIds(List<String> ids) {
        return sqlMaterialMapper.listByIds(ids)
    }

    @Override
    List<SqlMaterial> listSqlMaterials(String userId) {
        List<SqlMaterial> list = sqlMaterialMapper.listSqlMaterials(userId)
        list.each { it.materialType = MaterialType.SQL }
        return list
    }

    @Override
    int deleteById(String id) {
        return sqlMaterialMapper.deleteById(id)
    }
}
