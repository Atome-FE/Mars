import { ITaskResult, ITaskParams } from '@/interfaces/task-interface'
import { TaskSchedule } from '@/service/task-schedule'
import { getUuidV4 } from '@/lib/http'
import { IShareMaterial, IShareMaterialParams } from '@/interfaces/share-interface'
import { ITestCase } from '@/interfaces/test-case-interface'
import { TaskTypeEnum } from '@/enums/task-enum'
import { ReplaceUtils } from './replace-utils'
import { IMaterial } from '@/interfaces/common-interface'
import { TaskRunner } from '@/service/task-runner'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IMongoMaterial } from '@/interfaces/mongo-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'

export class ShareMaterialSchedule {
  shareMaterial: IShareMaterial

  shareMaterialParams: IShareMaterialParams

  paramReplace?: string

  constructor(shareMaterial: IShareMaterial) {
    this.shareMaterial = shareMaterial
    this.shareMaterialParams = shareMaterial.shareMaterialParams as IShareMaterialParams
  }

  getTasks(tasks: ITaskParams[]) {
    const firstTask: ITaskParams = {
      id: this.shareMaterialParams.dataMaterialId,
      name: this.shareMaterial.material?.name || '',
      type: 'DATA',
      delay: '',
      assert: '',
      paramReplaceRule: '',
      paramIndex: '',
      assertJump: '',
      assertTrueJump: '',
      extendIndexs: '',
      loop: '',
    }
    tasks.shift()
    tasks.unshift(firstTask)
    return tasks
  }

  async runCaseTask() {
    let arr: ITaskResult[] = []
    try {
      const testCaseMaterial = this.shareMaterial.material as ITestCase
      const data: ITestCase = {
        id: testCaseMaterial.id,
        name: testCaseMaterial.name,
        material: testCaseMaterial.material,
        groupName: testCaseMaterial.groupName,
      }
      const tasks: ITaskParams[] = this.getTasks(JSON.parse(data.material))
      data.material = JSON.stringify(tasks)
      arr = await TaskSchedule.runTestCase({ ...data })
    } catch (e) {
      console.log(e)
    }
    return arr
  }

  async runSingleTask() {
    const param: IMaterial = { ...this.shareMaterial.material! }
    param.paramReplace = this.paramReplace
    ReplaceUtils.doReplace(param)
    let res: ITaskResult
    const { type } = this.shareMaterial
    switch (type) {
      case TaskTypeEnum.HTTP:
        res = await TaskRunner.runHttp(param as IHttpMaterial)
        break
      case TaskTypeEnum.SQL:
        res = await TaskRunner.runSql(param as ISqlMaterial)
        break
      case TaskTypeEnum.MONGO:
        res = await TaskRunner.runMongo(param as IMongoMaterial)
        break
      case TaskTypeEnum.MQ:
        res = await TaskRunner.runMq(param as IMqMaterial)
        break
      case TaskTypeEnum.REDIS:
        res = await TaskRunner.runRedis(param as IRedisMaterial)
        break
      default:
        console.log(0)
    }
    return [res!]
  }

  async runShareMaterial() {
    let arr: ITaskResult[] = []
    const { type } = this.shareMaterial
    switch (type) {
      case TaskTypeEnum.CASE:
        arr = await this.runCaseTask()
        break
      case TaskTypeEnum.HTTP:
      case TaskTypeEnum.SQL:
      case TaskTypeEnum.MONGO:
      case TaskTypeEnum.MQ:
      case TaskTypeEnum.REDIS:
        arr = await this.runSingleTask()
        break
      default:
        console.log(0)
    }
    return arr
  }
}
