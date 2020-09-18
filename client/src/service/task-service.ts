import { ITaskParams } from '@/interfaces/task-interface'
import { Timer } from '@/lib/utils/timer'
import { AssertUtils } from '@/lib/utils/assert-utils'
import { ParamIndexException } from '@/exceptions/ParamIndexException'
import { ParamReplaceException } from '@/exceptions/ParamReplaceException'
import { AssertRuleException } from '@/exceptions/AssertRuleException'
import { TaskTypeEnum } from '@/enums/task-enum'
import { IDataMaterial } from '@/interfaces/data-material-interface'
import { getDataMaterialById } from '@/store/modules/data-material-module'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { getSqlMaterialById } from '@/store/modules/sql-material-module'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { getHttpMaterialById } from '@/store/modules/http-material-module'
import { IMongoMaterial } from '@/interfaces/mongo-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { getRedisMaterialById } from '@/store/modules/redis-material-module'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { getMqMaterialById } from '@/store/modules/mq-material-module'
import { getMongoMaterialById } from '@/store/modules/mongo-material-module'
import { taskScheduleSubject } from '@/subject/test-case-subject'
import { TaskRunner } from './task-runner'

export class TaskService {
  task: ITaskParams

  taskIndex: number

  originResult: any

  constructor(task: ITaskParams, taskIndex: number, originResult: any) {
    this.task = task
    this.taskIndex = taskIndex
    this.originResult = originResult
  }

  async run() {
    let assertResult: boolean = true
    let loop = this.task.loop ? Number(this.task.loop) : 1
    while (loop) {
      try {
        if (this.task.delay) {
          await Timer.sleep(Number.parseInt(this.task.delay, 10))
        }

        const destResult = await this.processTask()

        if (this.task.assert) {
          assertResult = AssertUtils.assert(destResult.result.origin, this.task.assert, this.originResult)
        }
        taskScheduleSubject.next({
          destResult,
          task: this.task,
          taskIndex: this.taskIndex,
          assertResult: assertResult,
        })
        loop--
      } catch (e) {
        const { name } = this.task
        if (e instanceof ParamIndexException) {
          window.vue.$notify.warning({
            message: `${name} 参数位置填写错误`,
            title: '警告',
          })
        } else if (e instanceof AssertRuleException) {
          window.vue.$notify.warning({
            message: `${name} 断言规则填写错误`,
            title: '警告',
          })
        } else if (e instanceof ParamReplaceException) {
          window.vue.$notify.warning({
            message: `${name} 参数替换填写错误`,
            title: '警告',
          })
        } else {
          window.vue.$notify.warning({
            message: `${name} 运行报错 ${e.message}`,
            title: '警告',
          })
        }
        throw e
      }
    }
    return assertResult
  }

  async processTask() {
    const task = this.task
    const originResult = this.originResult
    const destResult: any = {
      paramReplaceResult: true,
    }

    switch (task.type) {
      case TaskTypeEnum.DATA:
        const dataParam: IDataMaterial = getDataMaterialById(task.id) as IDataMaterial
        destResult.taskParam = dataParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(dataParam, originResult, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(dataParam)
        destResult.result = await TaskRunner.runData(dataParam)
        destResult.material = dataParam
        break
      case TaskTypeEnum.SQL:
        const sqlParam: ISqlMaterial = getSqlMaterialById(task.id) as ISqlMaterial
        destResult.taskParam = sqlParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(sqlParam, originResult, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(sqlParam)
        destResult.result = await TaskRunner.runSql(sqlParam)
        destResult.material = sqlParam
        break
      case TaskTypeEnum.HTTP:
        const httpParam: IHttpMaterial = getHttpMaterialById(task.id) as IHttpMaterial
        destResult.taskParam = httpParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(httpParam, originResult, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(httpParam)
        destResult.result = await TaskRunner.runHttp(httpParam)
        destResult.material = httpParam
        break
      case TaskTypeEnum.REDIS:
        const redisParam: IRedisMaterial = getRedisMaterialById(task.id) as IRedisMaterial
        destResult.taskParam = redisParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(redisParam, originResult, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(redisParam)
        destResult.result = await TaskRunner.runRedis(redisParam)
        destResult.material = redisParam
        break
      case TaskTypeEnum.MQ:
        const mqParam: IMqMaterial = getMqMaterialById(task.id) as IMqMaterial
        destResult.taskParam = mqParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(mqParam, originResult, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(mqParam)
        destResult.result = await TaskRunner.runMq(mqParam)
        destResult.material = mqParam
        break
      case TaskTypeEnum.MONGO:
        const mongoParam: IMongoMaterial = getMongoMaterialById(task.id) as IMongoMaterial
        destResult.taskParam = mongoParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(mongoParam, originResult, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(mongoParam)
        destResult.result = await TaskRunner.runMongo(mongoParam)
        destResult.material = mongoParam
        break
      default:
        destResult.taskParam = {
          id: '0',
          name: '0',
          groupName: '',
          secondGroupName: '',
        }
    }
    return destResult
  }
}
