import { IDataMaterial } from '@/interfaces/data-material-interface'
import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IResponse } from '@/interfaces/response-interface'
import { runSqlCopyTask, runSqlTask, runMongoTask, runHttpTask, runRedisTask, runMqTask } from '@/apis/task-api'
import { ResponseCodeEnum } from '@/enums/response-enum'
import { IMongoMaterial } from '@/interfaces/mongo-interface'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IMockMaterial } from '@/interfaces/mock-interface'
import MockJS from 'mockjs'

export class TaskRunner {
  static async runMock(data: IMockMaterial) {
    const jsonify = DataTransformUtils.jsonify(data.material)
    return DataTransformUtils.toPrettyData(MockJS.mock(jsonify), data.id)
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
        schema: data.schema,
      })
      result = res.code === ResponseCodeEnum.SUCCESS ? res.data : res
    } catch (e) {
      throw e
    }
    return DataTransformUtils.toPrettyData(result, data.id)
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
}
