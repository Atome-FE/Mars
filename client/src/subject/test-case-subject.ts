import { Subject } from 'rxjs'
import { ITaskRecord, ITaskDestResult } from '@/interfaces/task-interface'

const testCaseSubject = new Subject<ITaskRecord>()

const taskScheduleSubject = new Subject<ITaskDestResult>()

export { testCaseSubject, taskScheduleSubject }
