package com.syc.suda.cleaner.service.impl

import com.syc.suda.cleaner.mapper.CleanerMapper
import com.syc.suda.cleaner.service.CleanerService
import com.syc.suda.entity.DataMaterial
import com.syc.suda.entity.Env
import com.syc.suda.entity.HttpMaterial
import com.syc.suda.entity.Material
import com.syc.suda.entity.MongoMaterial
import com.syc.suda.entity.MqMaterial
import com.syc.suda.entity.NestedMaterial
import com.syc.suda.entity.RedisMaterial
import com.syc.suda.entity.SqlMaterial
import com.syc.suda.entity.TestCase
import com.syc.suda.enums.MaterialType
import com.syc.suda.enums.NestedMaterialType
import com.syc.suda.service.NestedMaterialService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CleanerServiceImpl implements CleanerService {
    @Autowired
    CleanerMapper cleanerMapper

    @Autowired
    NestedMaterialService nestedMaterialService

    @Override
    void cleanerToNestedMaterial() {
        List<HttpMaterial> httpMaterials = cleanerMapper.allHttpMaterial()
        first(httpMaterials, MaterialType.HTTP)

        List<SqlMaterial> sqlMaterials = cleanerMapper.allSqlMaterial()
        first(sqlMaterials, MaterialType.SQL)

        List<RedisMaterial> redisMaterials = cleanerMapper.allRedisMaterial()
        first(redisMaterials, MaterialType.REDIS)

        List<MqMaterial> mqMaterials = cleanerMapper.allMqMaterial()
        first(mqMaterials, MaterialType.MQ)

        List<MongoMaterial> mongoMaterials = cleanerMapper.allMongoMaterial()
        first(mongoMaterials, MaterialType.MONGO)

        List<DataMaterial> dataMaterials = cleanerMapper.allDataMaterial()
        first(dataMaterials, MaterialType.DATA)

        List<TestCase> caseMaterials = cleanerMapper.allCaseMaterial()
        first(caseMaterials, MaterialType.CASE)

        List<Env> envMaterials = cleanerMapper.allEnvMaterial()
        first(envMaterials, MaterialType.ENV)
    }

    private void first(List<Material> materials, MaterialType materialType) {
        materials.groupBy {it.userId}.each { user ->

            user.value.groupBy {it.groupName}.each {
                NestedMaterial nestedMaterial = new NestedMaterial()
                nestedMaterial.materialType = materialType
                nestedMaterial.userId = user.key
                nestedMaterial.type = NestedMaterialType.CATALOG
                if (it.key == null) {
                    nestedMaterial.name = '未分组'
                } else {
                    nestedMaterial.name = it.key
                }
                second(nestedMaterial, it.value, materialType)
            }
        }
    }
    private void second(NestedMaterial nestedMaterial, List<Material> materials, MaterialType materialType) {
        nestedMaterialService.save(nestedMaterial)
        if (nestedMaterial.name == '未分组') {
            materials.each {
                NestedMaterial a = new NestedMaterial()
                a.userId = it.userId
                a.materialType = materialType
                a.materialId = it.id
                a.parentId = nestedMaterial.id
                a.type = NestedMaterialType.ITEM
                a.name = ''
                nestedMaterialService.save(a)
            }
        } else {
            materials.groupBy {it.secondGroupName}.each {
                NestedMaterial b = new NestedMaterial()
                b.materialType = materialType
                b.userId = nestedMaterial.userId
                b.type = NestedMaterialType.CATALOG
                b.parentId = nestedMaterial.id
                if (!it.key || !it.key.trim()) {
                    b.name = ''
                    third(nestedMaterial, it.value, materialType)
                } else {
                    b.name = it.key
                    nestedMaterialService.save(b)
                    third(b, it.value, materialType)
                }
            }
        }
    }

    private void third(NestedMaterial nestedMaterial, List<Material> materials, MaterialType materialType) {
        // nestedMaterialService.save(nestedMaterial)
        materials.each {
            NestedMaterial a = new NestedMaterial()
            a.userId = it.userId
            a.materialType = materialType
            a.materialId = it.id
            a.parentId = nestedMaterial.id
            a.type = NestedMaterialType.ITEM
            a.name = ''
            nestedMaterialService.save(a)
        }
    }

    static void main(String[] args) {
        println(' ew ewew  '.trim())
    }
}
