import { instance as ajax } from '@/lib/http'
import { ITaskRecord } from '@/interfaces/task-interface'

export const fetchTaskRecord = () => ajax.get<ITaskRecord[]>('/task-record')

export const saveTaskRecord = (payload: ITaskRecord) => ajax.post<ITaskRecord>('/task-record', payload)
