import { taskInstance as ajax } from '@/lib/http'

export const fetchLogs = (payload: any) => ajax.post('/task/logs', payload)

export const fetchLogsContext = (payload: any) => ajax.post('/task/logs/context', payload)

export const execMq = (payload: any) => ajax.post('/task/mq/exec', payload)

export const pullDocument = (payload: any) =>
  ajax.post('/task/document/pull', payload, {
    responseType: 'blob',
  })

export const pushDocument = (payload: any) => ajax.post('/task/document/push', payload)

export const runGroupTask = (payload: any) => ajax.post('/task/run-group', payload)

export const runHttpTask = (payload: any) => ajax.post('/task/http', payload)

export const runSqlTask = (payload: any) => ajax.post('/task/sql', payload)

export const runSqlCopyTask = (payload: any) => ajax.post('/task/sql/copy', payload)

export const runRedisTask = (payload: any) => ajax.post('/task/redis', payload)

export const runMqTask = (payload: any) => ajax.post('/task/mq', payload)

export const runMongoTask = (payload: any) => ajax.post(`/task/mongo/${payload.action}`, payload)
