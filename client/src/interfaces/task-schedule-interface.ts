import { ITaskParams, ITaskResult, ITaskRecord } from '@/interfaces/task-interface'
import { ITestCase } from '@/interfaces/test-case-interface'

export interface ITaskScheduleBase {
  data: ITestCase
  arr: ITaskResult[]
  testGroupId?: string
  executeTime: number
  iterationId: string
}

export interface IIterationTask extends ITaskScheduleBase {
  tasks: ITaskParams[]
}

export interface IWhileTask extends ITaskScheduleBase {
  task: ITaskParams
  i: number
}
