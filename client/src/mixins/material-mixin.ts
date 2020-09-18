import { Component, Vue } from 'vue-property-decorator'
import { NestedMaterialModule } from '@/store/modules/nested-material-module'
import { ITaskResult } from '@/interfaces/task-interface'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { TaskRunner } from '@/service/task-runner'
import { IMaterial } from '@/interfaces/common-interface'
import { TaskTypeEnum } from '@/enums/task-enum'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { IMongoMaterial } from '@/interfaces/mongo-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IDataMaterial } from '@/interfaces/data-material-interface'

@Component
export default class MaterialMixin extends Vue {
  taskResult: ITaskResult = {
    pretty: '',
    type: '',
    origin: '',
    clipText: '',
  }

  material: IMaterial = {
    id: '',
    name: '',
  }

  async runMaterial() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    const param = { ...this.material }
    try {
      ReplaceUtils.doReplace(param)
      let res: ITaskResult
      switch (NestedMaterialModule.materialType) {
        case TaskTypeEnum.HTTP:
          res = await TaskRunner.runHttp(param as IHttpMaterial)
          break
        case TaskTypeEnum.SQL:
          console.log('走到这里')
          res = await TaskRunner.runSql(param as ISqlMaterial)
          break
        case TaskTypeEnum.REDIS:
          res = await TaskRunner.runRedis(param as IRedisMaterial)
          break
        case TaskTypeEnum.MONGO:
          res = await TaskRunner.runMongo(param as IMongoMaterial)
          break
        case TaskTypeEnum.MQ:
          res = await TaskRunner.runMq(param as IMqMaterial)
          break
        case TaskTypeEnum.DATA:
          res = await TaskRunner.runData(param as IDataMaterial)
          break
        default:
          res = {}
          console.log(0)
      }
      this.taskResult = { ...res }
    } catch (e) {
      this.$notify.warning({
        title: '警告',
        message: e.message,
      })
    } finally {
      loading.close()
    }
  }

  get getTaskResult() {
    return NestedMaterialModule.taskResult.taskId ? NestedMaterialModule.taskResult : this.taskResult
  }
}
