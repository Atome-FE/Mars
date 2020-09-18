import { ITestCase } from '@/interfaces/test-case-interface'
import { ITaskResult, ITaskParams, ITaskRecord, ITaskDestResult } from '@/interfaces/task-interface'
import { getUuidV4 } from '@/lib/http'
import { ParamIndexException } from '@/exceptions/ParamIndexException'
import { IMaterial } from '@/interfaces/common-interface'
import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { testCaseSubject, taskScheduleSubject } from '@/subject/test-case-subject'
import { TaskRecordModule } from '@/store/modules/task-record-module'
import { saveTaskRecord } from '@/apis/task-record-api'
import { Subscription } from 'rxjs'
import { TaskService } from './task-service'

export class CaseService {
  loopData: any[] = []

  loopStart: number = 0

  loopEnd: number = 0

  loopIndex: number = 0

  totalTaskCount: number = 0

  testGroupId?: string

  executeTime: number = 0

  taskResults: ITaskResult[] = []

  iterationId: string = ''

  tasks: ITaskParams[] = []

  testCase: ITestCase

  taskScheduleSubscription!: Subscription

  constructor(testCase: ITestCase, executeTime: number, testGroupId?: string) {
    this.testCase = testCase
    this.testGroupId = testGroupId
    this.executeTime = executeTime
  }

  resetLoopParam() {
    this.loopData.splice(0)
    this.loopIndex = 0
    this.loopStart = 0
    this.loopEnd = 0
  }

  async run() {
    this.taskScheduleSubscription = taskScheduleSubject.subscribe({
      next: data => this.handleTaskResult(data),
    })
    return await this.runTestCase()
  }

  close() {
    this.taskScheduleSubscription.unsubscribe()
  }

  async runTestCase() {
    console.time('开始执行用例')
    this.resetLoopParam()
    this.tasks = JSON.parse(this.testCase.material)
    this.totalTaskCount = this.tasks.length
    this.iterationId = getUuidV4()
    try {
      await this.iterationProcessTask(0, this.tasks.length)
    } catch (e) {
      console.log(e)
    }
    console.timeEnd('开始执行用例')
    return this.taskResults
  }

  async iterationProcessTask(startIndex: number, endIndex: number) {
    for (let i = startIndex; i < endIndex; ) {
      const task = this.tasks[i]
      let assertResult: boolean = true
      if (task.loopStart) {
        this.loopStart = Number(task.loopStart)
        this.loopEnd = Number(task.loopEnd)
        this.loopIndex = i
      }

      const originResult = this.getOriginResult(task, i)
      const taskService = new TaskService(task, i, originResult)
      assertResult = await taskService.run()

      if (task.loopStart) {
        i = this.loopEnd + 1
        await this.loopProcessTask(this.loopStart, i)
        break
      }
      i = this.setRunIndex(i, assertResult, task)
    }
  }

  handleTaskExecParams(params: any) {
    delete params.id
    delete params.describe
    delete params.groupName
    delete params.secondGroupName
    delete params.name
    delete params.materialType
    return params
  }

  handleTaskResult(data: ITaskDestResult) {
    const taskParam: IMaterial = data.destResult.taskParam
    const result: ITaskResult = data.destResult.result
    const paramReplaceResult = data.destResult.paramReplaceResult
    result.taskIndex = data.taskIndex
    result.iterationId = this.iterationId
    result.execParams = this.handleTaskExecParams({ ...data.destResult.material })
    if (this.loopStart && this.loopIndex === data.taskIndex) {
      this.loopData.push(...result.origin)
    }
    this.taskResults.push(result)
    if (data.task.extendIndexs) {
      this.extendResult(result.pretty, data.task.extendIndexs)
    }
    this.addTaskRecord(data.task, result, taskParam, paramReplaceResult, data.assertResult)
  }

  getOriginResult(task: ITaskParams, i: number) {
    const paramIndex = task.paramIndex ? Number(task.paramIndex) : i - 1
    const preResult = this.getPreResult(paramIndex)
    let origin
    if (this.loopStart === i && this.loopData.length) {
      origin = this.loopData.shift()
    } else {
      origin = preResult.origin
    }
    return origin
  }

  private setRunIndex(i: number, assertResult: boolean, task: ITaskParams) {
    if (assertResult) {
      if (task.assertTrueJump) {
        const jump = Number(task.assertTrueJump)
        this.addEmptyResult(i, jump)
        return jump
      }
      return i + 1
    }
    if (task.assertJump) {
      const jump = Number(task.assertJump)
      this.addEmptyResult(i, jump)
      return jump
    }
    return i + 1
  }

  async addEmptyResult(cur: number, des: number) {
    for (let i = cur + 1; i < des; i++) {
      const result: ITaskResult = DataTransformUtils.toPrettyData('未执行')
      result.taskIndex = i
      this.taskResults.push(result)
    }
  }

  async loopProcessTask(startIndex: number, endIndex: number) {
    return new Promise((resolve, reject) => {
      const len = this.loopData.length
      let n = len
      for (let i = 0; i < len; i++) {
        this.iterationProcessTask(startIndex, endIndex)
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

  private getPreResult(paramIndex: number) {
    if (paramIndex < 0) return {} as any
    let result = this.taskResults
      .filter((item: ITaskResult) => {
        return item.taskIndex === paramIndex && item.iterationId === this.iterationId
      })
      .pop() as ITaskResult
    if (result) return result
    result = this.taskResults
      .filter((item: ITaskResult) => {
        return item.taskIndex === paramIndex
      })
      .pop() as ITaskResult
    if (result) return result
    throw new ParamIndexException('error')
  }

  private extendResult(currentResult: any, extendIndexs: string) {
    if (!currentResult) return false
    try {
      const indexs = extendIndexs.split(',').map((i: string) => i.trim())
      indexs.forEach((i: string) => {
        const preResult = this.getPreResult(Number(i))
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

  private addTaskRecord(
    task: ITaskParams,
    result: ITaskResult,
    taskParam: IMaterial,
    paramReplaceResult: boolean,
    assertResult: boolean,
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
      testCaseId: this.testCase.id,
      testGroupId: this.testGroupId,
      executeTime: this.executeTime,
      executeTaskTime: Date.now(),
      totalTaskCount: this.totalTaskCount,
      currentTaskIndex: result.taskIndex || 0,
    }
    testCaseSubject.next(obj)
    TaskRecordModule.addTaskRecord(obj)
    saveTaskRecord(obj)
  }
}
