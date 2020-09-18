import { TaskTypeEnum } from '@/enums/task-enum'
import { IMaterial } from '@/interfaces/common-interface'

export interface IEnv extends IMaterial {
  id: string
  userId?: string
  name: string
  http: string
  sql: string
  redis: string
  globalVariable: string
  mq: string
  mongo: string
  describe: string
  materialType: TaskTypeEnum
}
