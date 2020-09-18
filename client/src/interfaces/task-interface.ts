export interface ITaskResult {
  type?: string
  pretty?: any
  origin?: any
  clipText?: string
  taskId?: string
  taskIndex?: number
  iterationId?: string
  execParams?: any
}

export interface ITaskParams {
  id: string
  name: string
  type: string
  delay: string
  assert: string
  paramReplaceRule: string
  paramIndex: string
  assertJump: string
  assertTrueJump: string
  loop: string
  result?: any
  extendIndexs: string
  loopStart?: string
  loopEnd?: string
}

export interface ITaskRecord {
  taskId: string
  taskType: string
  taskName: string
  taskDelay: string
  taskAssert: any
  assertResult: number
  taskParam: string
  taskResult: any
  testCaseId: string
  testGroupId?: string
  executeTime: any
  executeTaskTime?: any
  totalTaskCount: number
  currentTaskIndex: number
}

export interface ITaskDestResult {
  task: ITaskParams
  taskIndex: number
  destResult: any
  assertResult: boolean
}
