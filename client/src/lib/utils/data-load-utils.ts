import { fetchTestGroupCases, fetchTestGroups } from '@/apis/test-api'
import { fetchAuth } from '@/apis/setting-api'
import { fetchHttpMaterials } from '@/apis/http-api'
import { fetchSqlMaterials } from '@/apis/sql-api'
import { fetchMongoMaterials } from '@/apis/mongo-api'
import { fetchShareMaterials } from '@/apis/share-api'
import { fetchTestCaseMaterials } from '@/apis/test-case-api'
import { fetchRedisMaterials } from '@/apis/redis-api'
import { fetchMqMaterials } from '@/apis/mq-api'
import { fetchEnvs } from '@/apis/env-api'
import { fetchDataMaterials } from '@/apis/data-material-api'
import { fetchDocuments } from '@/apis/document-api'

import { FieldModule } from '@/store/modules/field-module'
import { HttpMaterialModule } from '@/store/modules/http-material-module'
import { SqlMaterialModule } from '@/store/modules/sql-material-module'
import { TestCaseMaterialModule } from '@/store/modules/test-case-material-module'
import { RedisMaterialModule } from '@/store/modules/redis-material-module'
import { MqMaterialModule } from '@/store/modules/mq-material-module'
import { EnvModule } from '@/store/modules/env-module'
import { DataMaterialModule } from '@/store/modules/data-material-module'
import { MongoMaterialModule } from '@/store/modules/mongo-material-module'
import { ShareMaterialModule } from '@/store/modules/share-material-module'
import { DocumentModule } from '@/store/modules/document-module'
import { UserModule } from '@/store/modules/user-module'
import { RoleTypeEnum } from '@/enums/user-enum'
import Vue from 'vue'

class DataLoadUtils {
  static async loadAllData() {
    const loading = Vue.prototype.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      ShareMaterialModule.initData()
      const [
        meAuth,
        testGroupCases,
        testGroups,
        httpMaterials,
        sqlMaterials,
        testCaseMaterials,
        redisMaterials,
        mqMaterials,
        envs,
        dataMaterials,
        mongoMaterials,
      ] = await Promise.all([
        fetchAuth(),
        fetchTestGroupCases(),
        fetchTestGroups(),
        fetchHttpMaterials(),
        fetchSqlMaterials(),
        fetchTestCaseMaterials(),
        fetchRedisMaterials(),
        fetchMqMaterials(),
        fetchEnvs(),
        fetchDataMaterials(),
        fetchMongoMaterials(),
      ])
      const isAdmin = meAuth.data.roles
        .filter((_: any) => _.status === 'ENABLED')
        .some((item: any) => {
          return item.name === RoleTypeEnum.ADMIN
        })
      UserModule.setRole(isAdmin ? RoleTypeEnum.ADMIN : RoleTypeEnum.NORMAL)
      FieldModule.addAllTestGroup(testGroups.data)
      FieldModule.addAllTestGroupCases(testGroupCases.data)
      HttpMaterialModule.addAllHttpMaterials(httpMaterials.data)
      SqlMaterialModule.addAllSqlMaterials(sqlMaterials.data)
      TestCaseMaterialModule.addAllTestCaseMaterials(testCaseMaterials.data)
      RedisMaterialModule.addAllRedisMaterials(redisMaterials.data)
      MqMaterialModule.addAllMqMaterials(mqMaterials.data)
      EnvModule.addAllEnvs(envs.data)
      DataMaterialModule.addAllDataMaterials(dataMaterials.data)
      MongoMaterialModule.addAllMongoMaterials(mongoMaterials.data)

      const [documents] = await Promise.all([fetchDocuments()])
      DocumentModule.addAllDocument(documents.data)
    } catch (e) {
      window.vue.$handleError(e)
    } finally {
      loading.close()
    }
  }
}

export { DataLoadUtils }
