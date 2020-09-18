package com.syc.suda.service

import com.syc.suda.entity.SqlMaterial

interface SqlMaterialService {

    int save(SqlMaterial sqlMaterial)

    int update(SqlMaterial sqlMaterial)

    SqlMaterial getById(String id)

    List<SqlMaterial> listByIds(List<String> ids)

    List<SqlMaterial> listSqlMaterials(String userId)

    int deleteById(String id)

}