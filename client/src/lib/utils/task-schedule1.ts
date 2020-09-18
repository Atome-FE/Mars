import _ from 'lodash'

import {
  runGroupTask,
  runHttpTask,
  runSqlTask,
  runMongoTask,
  runRedisTask,
  runMqTask,
  runSqlCopyTask,
} from '@/apis/task-api'
import { saveTaskRecord } from '@/apis/task-record-api'

import { Timer } from '@/lib/utils/timer'
import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'
import { getUuidV4 } from '@/lib/http'

import { getHttpMaterialById } from '@/store/modules/http-material-module'
import { getSqlMaterialById } from '@/store/modules/sql-material-module'
import { getMongoMaterialById } from '@/store/modules/mongo-material-module'
import { getMqMaterialById } from '@/store/modules/mq-material-module'
import { getRedisMaterialById } from '@/store/modules/redis-material-module'
import { getTestCaseMaterialById } from '@/store/modules/test-case-material-module'
import { getDataMaterialById } from '@/store/modules/data-material-module'
import { TaskRecordModule } from '@/store/modules/task-record-module'

import { IIterationTask, IWhileTask } from '@/interfaces/task-schedule-interface'
import { ITaskParams, ITaskResult, ITaskRecord } from '@/interfaces/task-interface'
import { ITestCase } from '@/interfaces/test-case-interface'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { IDataMaterial } from '@/interfaces/data-material-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IMaterial } from '@/interfaces/common-interface'
import { IResponse } from '@/interfaces/response-interface'

import { TaskTypeEnum } from '@/enums/task-enum'
import { ResponseCodeEnum } from '@/enums/response-enum'

import { ParamIndexException } from '@/exceptions/ParamIndexException'
import { AssertRuleException } from '@/exceptions/AssertRuleException'
import { ParamReplaceException } from '@/exceptions/ParamReplaceException'
import { IMongoMaterial } from '@/interfaces/mongo-interface'
import { testCaseSubject } from '@/subject/test-case-subject'
import { ReplaceUtils } from './replace-utils'
import { AssertUtils } from './assert-utils'
import { IMockMaterial } from '@/interfaces/mock-interface'

class TaskSchedule {
  static loopData: any[] = []

  static loopStart: number = 0

  static loopEnd: number = 0

  static loopIndex: number = 0

  static totalTaskCount: number = 0

  static resetLoopParam() {
    this.loopData.splice(0)
    this.loopIndex = 0
    this.loopStart = 0
    this.loopEnd = 0
  }

  static async runTestGroupCase(testCaseId: string, executeTime: number, testGroupId: string) {
    const testCase: ITestCase = getTestCaseMaterialById(testCaseId) as ITestCase
    return await this.runTestCase(testCase, executeTime, testGroupId)
  }

  static async processTask(task: ITaskParams, origin: any, testGroupId?: string) {
    const destResult: any = {
      paramReplaceResult: true,
    }
    switch (task.type) {
      case TaskTypeEnum.DATA:
        const dataParam: IDataMaterial = getDataMaterialById(task.id) as IDataMaterial
        dataParam.testGroupId = testGroupId
        destResult.taskParam = dataParam
        if (task.paramReplaceRule) {
          // destResult.paramReplaceResult = ConditionUtils.paramDataReplace(dataParam, origin, task.paramReplaceRule)
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(dataParam, origin, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(dataParam)
        // ConditionUtils.replaceOps(dataParam)
        destResult.result = await this.runData(dataParam)
        break
      case TaskTypeEnum.SQL:
        const sqlParam: ISqlMaterial = getSqlMaterialById(task.id) as ISqlMaterial
        sqlParam.testGroupId = testGroupId
        destResult.taskParam = sqlParam
        if (task.paramReplaceRule) {
          // destResult.paramReplaceResult = ConditionUtils.paramSqlReplace(sqlParam, origin, task.paramReplaceRule)
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(sqlParam, origin, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(sqlParam)
        // ConditionUtils.replaceOps(sqlParam)
        destResult.result = await this.runSql(sqlParam)
        break
      case TaskTypeEnum.HTTP:
        const httpParam: IHttpMaterial = getHttpMaterialById(task.id) as IHttpMaterial
        httpParam.testGroupId = testGroupId
        destResult.taskParam = httpParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(httpParam, origin, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(httpParam)
        // ConditionUtils.replaceOps(httpParam)
        destResult.result = await this.runHttp(httpParam)
        break
      case TaskTypeEnum.REDIS:
        const redisParam: IRedisMaterial = getRedisMaterialById(task.id) as IRedisMaterial
        redisParam.testGroupId = testGroupId
        destResult.taskParam = redisParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(redisParam, origin, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(redisParam)
        // ConditionUtils.replaceOps(redisParam)
        destResult.result = await this.runRedis(redisParam)
        break
      case TaskTypeEnum.MQ:
        const mqParam: IMqMaterial = getMqMaterialById(task.id) as IMqMaterial
        mqParam.testGroupId = testGroupId
        destResult.taskParam = mqParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(mqParam, origin, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(mqParam)
        // ConditionUtils.replaceOps(mqParam)
        destResult.result = await this.runMq(mqParam)
        break
      case TaskTypeEnum.MONGO:
        const mongoParam: IMongoMaterial = getMongoMaterialById(task.id) as IMongoMaterial
        mongoParam.testGroupId = testGroupId
        destResult.taskParam = mongoParam
        if (task.paramReplaceRule) {
          destResult.paramReplaceResult = ReplaceUtils.caseParamReplace(mongoParam, origin, task.paramReplaceRule)
        }
        ReplaceUtils.replaceOps(mongoParam)
        // ConditionUtils.replaceOps(mongoParam)
        destResult.result = await this.runMongo(mongoParam)
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

  static async whileProcessTask(config: IWhileTask) {
    let assertResult: boolean = true
    let result: ITaskResult = {}
    let loop = config.task.loop ? Number(config.task.loop) : 1
    while (loop) {
      try {
        let paramReplaceResult: boolean = true
        if (config.task.delay) {
          await Timer.sleep(Number.parseInt(config.task.delay, 10))
        }
        const paramIndex = config.task.paramIndex ? Number(config.task.paramIndex) : config.i - 1
        const preResult = this.getPreResult(config.arr, paramIndex, config.iterationId)
        let origin
        if (this.loopStart === config.i && this.loopData.length) {
          origin = this.loopData.shift()
        } else {
          origin = preResult.origin
        }

        const destResult = await this.processTask(config.task, origin, config.testGroupId)
        const taskParam: IMaterial = destResult.taskParam
        result = destResult.result
        paramReplaceResult = destResult.paramReplaceResult

        result.taskIndex = config.i
        result.iterationId = config.iterationId
        if (this.loopStart && this.loopIndex === config.i) {
          this.loopData.push(...result.origin)
        }
        config.arr.push(result)
        if (config.task.extendIndexs) {
          this.extendResult(config.arr, result.pretty, config.task.extendIndexs, config.iterationId)
        }
        if (config.task.assert) {
          // assertResult = ConditionUtils.equals(result.origin, config.task.assert)
          assertResult = AssertUtils.assert(result.origin, config.task.assert, null)
        }
        this.addTaskRecord(
          config.task,
          result,
          taskParam,
          paramReplaceResult,
          assertResult,
          config.data,
          config.testGroupId!,
          config.executeTime,
        )
        loop--
      } catch (e) {
        if (e instanceof ParamIndexException) {
          window.vue.$notify.warning({
            message: `${config.task.name} 参数位置填写错误`,
            title: '警告',
          })
        } else if (e instanceof AssertRuleException) {
          window.vue.$notify.warning({
            message: `${config.task.name} 断言规则填写错误`,
            title: '警告',
          })
        } else if (e instanceof ParamReplaceException) {
          window.vue.$notify.warning({
            message: `${config.task.name} 参数替换填写错误`,
            title: '警告',
          })
        } else {
          window.vue.$notify.warning({
            message: `${config.task.name} 运行报错 ${e.message}`,
            title: '警告',
          })
        }
        throw e
      }
    }
    return assertResult
  }

  static async iterationProcessTask(startIndex: number, endIndex: number, config: IIterationTask) {
    for (let i = startIndex; i < endIndex; ) {
      const task = config.tasks[i]
      let assertResult: boolean = true
      if (task.loopStart) {
        this.loopStart = Number(task.loopStart)
        this.loopEnd = Number(task.loopEnd)
        this.loopIndex = i
      }
      assertResult = await this.whileProcessTask({
        task,
        i,
        testGroupId: config.testGroupId,
        executeTime: config.executeTime,
        data: config.data,
        arr: config.arr,
        iterationId: config.iterationId,
      })
      if (task.loopStart) {
        i = this.loopEnd + 1
        await this.loopProcessTask(this.loopStart, i, config)
        break
      }
      i = this.setRunIndex(config.arr, i, assertResult, task)
    }
  }

  static async loopProcessTask(startIndex: number, endIndex: number, config: IIterationTask) {
    return new Promise((resolve, reject) => {
      const len = this.loopData.length
      let n = len
      for (let i = 0; i < len; i++) {
        this.iterationProcessTask(startIndex, endIndex, { ...config, iterationId: getUuidV4() })
          // eslint-disable-next-line no-loop-func
          .then(res => {
            n -= 1
            if (n <= 0) {
              resolve(true)
            }
          })
          .catch(e => {
            window.vue.$notify.warning({
              message: `startIndex: ${startIndex} endIndex: ${endIndex} i: ${i} 循环任务运行报错 ${e.message}`,
              title: '警告',
            })
            reject(e)
          })
      }
    })
  }

  static async runTestCase(data: ITestCase, executeTime: number, testGroupId?: string) {
    console.time('开始执行用例')
    this.resetLoopParam()
    const arr: ITaskResult[] = []
    const tasks: ITaskParams[] = JSON.parse(data.material)
    this.totalTaskCount = tasks.length
    try {
      // await runGroupTask({
      //   data,
      //   executeTime,
      //   testGroupId,
      // })
      await this.iterationProcessTask(0, tasks.length, {
        arr,
        testGroupId,
        executeTime,
        data,
        tasks,
        iterationId: getUuidV4(),
      })
    } catch (e) {
      console.log(e)
    }
    console.timeEnd('开始执行用例')
    return arr
  }

  static async addEmptyResult(arr: ITaskResult[], cur: number, des: number) {
    for (let i = cur + 1; i < des; i++) {
      const result: ITaskResult = DataTransformUtils.toPrettyData('未执行')
      result.taskIndex = i
      arr.push(result)
    }
  }

  static async runHttp(data: IHttpMaterial) {
    let result: IResponse
    try {
      const res: any = await runHttpTask({
        uri: data.url,
        method: data.method,
        headers: DataTransformUtils.handleHeaders(data.headers),
        dataType: data.dataType,
        dataHandleType: data.dataHandleType,
        body: DataTransformUtils.jsonify(data.data),
        testGroupId: data.testGroupId,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      result = e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
  }

  static async runMq(data: IMqMaterial) {
    let result: IResponse
    try {
      const res: any = await runMqTask({
        exchange: data.exchange,
        routingKey: data.routingKey,
        content: data.content,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      result = e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
  }

  static async runRedis(data: IRedisMaterial) {
    let result: IResponse
    try {
      const res: any = await runRedisTask({
        action: data.action,
        key: data.key,
        datasource: data.datasource,
        database: data.database,
        testGroupId: data.testGroupId,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      result = e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
  }

  static async runMock(data: IMockMaterial) {
    const jsonify = DataTransformUtils.jsonify(data.data)
    return DataTransformUtils.toPrettyData(jsonify, data.id)
  }

  static async runData(data: IDataMaterial) {
    const jsonify = DataTransformUtils.jsonify(data.data)
    return DataTransformUtils.toPrettyData(jsonify, data.id)
  }

  static async runSqlCopy(data: ISqlMaterial) {
    let result: IResponse
    try {
      const res: any = await runSqlCopyTask({
        material: data.material,
        datasource: data.datasource,
        database: data.database,
        testGroupId: data.testGroupId,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      result = e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
  }

  static async runSql(data: ISqlMaterial) {
    let result: IResponse
    try {
      const res: any = await runSqlTask({
        material: data.material,
        datasource: data.datasource,
        database: data.database,
        testGroupId: data.testGroupId,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      result = e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
  }

  static async runMongo(data: IMongoMaterial) {
    let result: IResponse
    try {
      const res: any = await runMongoTask({
        action: data.action,
        material: data.material,
        datasource: data.datasource,
        database: data.database,
        testGroupId: data.testGroupId,
        schema: data.schema,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      // result = e
      throw e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
  }

  private static setRunIndex(arr: ITaskResult[], i: number, assertResult: boolean, task: ITaskParams) {
    if (assertResult) {
      if (task.assertTrueJump) {
        const jump = Number(task.assertTrueJump)
        this.addEmptyResult(arr, i, jump)
        return jump
      }
      return i + 1
    }
    if (task.assertJump) {
      const jump = Number(task.assertJump)
      this.addEmptyResult(arr, i, jump)
      return jump
    }
    return i + 1
  }

  private static addTaskRecord(
    task: ITaskParams,
    result: ITaskResult,
    taskParam: IMaterial,
    paramReplaceResult: boolean,
    assertResult: boolean,
    data: ITestCase,
    testGroupId: string,
    executeTime: number,
  ) {
    const obj: ITaskRecord = {
      taskId: task.id,
      taskName: task.name,
      taskType: task.type,
      taskDelay: task.delay,
      taskAssert: task.assert,
      taskResult: DataTransformUtils.stringify(result.origin),
      taskParam: DataTransformUtils.stringify(taskParam),
      assertResult: Number(paramReplaceResult && assertResult),
      testCaseId: data.id,
      testGroupId: testGroupId,
      executeTime: executeTime,
      executeTaskTime: Date.now(),
      totalTaskCount: this.totalTaskCount,
      currentTaskIndex: result.taskIndex || 0,
    }
    testCaseSubject.next(obj)
    TaskRecordModule.addTaskRecord(obj)
    saveTaskRecord(obj)
  }

  private static getPreResult(arr: ITaskResult[], paramIndex: number, iterationId: string) {
    if (paramIndex < 0) return {} as any
    let result = arr
      .filter((item: ITaskResult) => {
        return item.taskIndex === paramIndex && item.iterationId === iterationId
      })
      .pop() as ITaskResult
    if (result) return result
    result = arr
      .filter((item: ITaskResult) => {
        return item.taskIndex === paramIndex
      })
      .pop() as ITaskResult
    if (result) return result
    throw new ParamIndexException('error')
  }

  /**
   * 参数 0,1,2 用逗号分隔不同的任务顺序
   * 当前结果               之前的结果         最终结果
   * { "name": "hh" }      "xx"             { "name": "hh", "extendProperty": "xx" }
   * { "name": "hh" }      { "age": 32 }    { "name": "hh", "age": 32 }
   * { "name": "hh" }      []               { "name": "hh", "extendArray": [] }
   * [ { "name": "hh" } ]  "xx"             [{ "extendProperty": "xx" }, { "name": "hh" }]
   * [ { "name": "hh" } ]  { "age": 32 }    [{ "age": 32 }, { "name": "hh" }]
   * [ { "name": "hh" } ]  [{ "age": 32 }]  [{ "age": 32 }, { "name": "hh" }]
   * @param arr
   * @param currentResult
   * @param extendIndexs
   */
  private static extendResult(arr: ITaskResult[], currentResult: any, extendIndexs: string, iterationId: string) {
    if (!currentResult) return false
    try {
      const indexs = extendIndexs.split(',').map((i: string) => i.trim())
      indexs.forEach((i: string) => {
        const preResult = this.getPreResult(arr, Number(i), iterationId)
        const desResult = preResult.pretty || preResult.origin
        if (Array.isArray(desResult)) {
          if (Array.isArray(currentResult)) {
            currentResult.push(...desResult)
          } else {
            if (!currentResult.extendArray) {
              currentResult.extendArray = []
            }
            currentResult.extendArray.push(...desResult)
          }
        } else if (desResult instanceof Object) {
          if (Array.isArray(currentResult)) {
            currentResult.unshift(desResult)
          } else {
            Object.assign(currentResult, desResult)
          }
        } else if (Array.isArray(currentResult)) {
          currentResult.unshift({
            extendProperty: desResult,
          })
        } else {
          currentResult.extendProperty = desResult
        }
      })
      return true
    } catch (e) {
      if (e instanceof ParamIndexException) {
        window.vue.$notify.warning({
          message: '结果扩展填写错误',
          title: '警告',
        })
      }
      return false
    }
  }
}

export { TaskSchedule }
