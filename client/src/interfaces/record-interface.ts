export interface IRecord {
  status: string
  result: boolean
  total: number
  errorTotal: number
  sqlErrorTotal: number
  sqlTotal: number
  httpErrorTotal: number
  httpTotal: number
  redisErrorTotal: number
  redisTotal: number
  mqTotal: number
  mqErrorTotal: number
  mongoTotal: number
  mongoErrorTotal: number
}
