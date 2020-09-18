import _ from 'lodash'
import dayjs from 'dayjs'

import { generateRandom } from '@/lib/utils/uuid'
import { DataTransformUtils } from '@/lib/utils/data-transform-utils'

import { IHttpMaterial } from '@/interfaces/http-interface'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IDataMaterial } from '@/interfaces/data-material-interface'
import { IMaterial } from '@/interfaces/common-interface'

import { HttpParamEnum } from '@/enums/http-enum'
import { MqParamEnum } from '@/enums/mq-enum'
import { ParamReplaceTypeEnum } from '@/enums/common-enum'

import { AssertFailException } from '@/exceptions/AssertFailException'
import { AssertRuleException } from '@/exceptions/AssertRuleException'
import { ParamReplaceException } from '@/exceptions/ParamReplaceException'
import { IMongoMaterial } from '@/interfaces/mongo-interface'

class ConditionUtils {
  /**
   * 替换body参数 body:{userId}=root.b.c.value
   * 替换path参数 path:{userId}=root.b.c.name
   * 替换query参数 query:{userId}=root.b.c.name
   * @param data http请求的参数，包括body，url，query等
   * @param caseData 要替换的数据源
   * @param replaceRule 替换规则
   */
  static paramHttpReplace(data: IHttpMaterial, caseData: any, replaceRule: any) {
    try {
      this.replaceHttp(data, caseData, replaceRule)
      return true
    } catch (e) {
      // return false
      throw new ParamReplaceException('参数替换出错')
    }
  }

  /**
   * 替换exchange参数 exchange:{userId}=root.b.c.value
   * 替换content参数 content:{userId}=root.b.c.name
   * 替换routingKey参数 routingKey:{userId}=root.b.c.name
   * @param data mq请求的参数，包括exchange，content，routingKey等
   * @param caseData 要替换的数据源
   * @param replaceRule 替换规则
   */
  static paramMqReplace(data: IMqMaterial, caseData: any, replaceRule: any) {
    try {
      this.replaceMq(data, caseData, replaceRule)
      return true
    } catch (e) {
      // return false
      throw new ParamReplaceException('参数替换出错')
    }
  }

  /**
   * 替换参数 {userId}=root.b.c.value
   * @param data sql请求的参数
   * @param caseData 要替换的数据源
   * @param replaceRule 替换规则
   */
  static paramSqlReplace(data: ISqlMaterial, caseData: any, replaceRule: any) {
    try {
      this.replace(data, caseData, replaceRule)
      return true
    } catch (e) {
      // return false
      throw new ParamReplaceException('参数替换出错')
    }
  }

  /**
   * 替换参数 {userId}=root.b.c.value
   * @param data sql请求的参数
   * @param caseData 要替换的数据源
   * @param replaceRule 替换规则
   */
  static paramMongoReplace(data: IMongoMaterial, caseData: any, replaceRule: any) {
    try {
      this.replace(data, caseData, replaceRule)
      return true
    } catch (e) {
      // return false
      throw new ParamReplaceException('参数替换出错')
    }
  }

  /**
   * 替换参数 {userId}=root.b.c.value
   * @param data 数据源的参数
   * @param caseData 要替换的数据源
   * @param replaceRule 替换规则
   */
  static paramDataReplace(data: IDataMaterial, caseData: any, replaceRule: any) {
    try {
      this.replaceData(data, caseData, replaceRule)
      return true
    } catch (e) {
      // return false
      throw new ParamReplaceException('参数替换出错')
    }
  }

  /**
   * 替换参数 {userId}=root.b.c.value
   * @param data redis请求的参数
   * @param caseData 要替换的数据源
   * @param replaceRule 替换规则
   */
  static paramRedisReplace(data: IRedisMaterial, caseData: any, replaceRule: any) {
    try {
      this.replace(data, caseData, replaceRule)
      return true
    } catch (e) {
      // return false
      throw new ParamReplaceException('参数替换出错')
    }
  }

  /**
   * 校验用例结果是否正确
   * @param origin 用例返回的结果
   * @param condition 断言条件
   */
  static equals(origin: any, condition: string) {
    try {
      this.compare(origin, condition)
      return true
    } catch (e) {
      if (e instanceof AssertFailException) {
        return false
      }
      throw new AssertRuleException('规则错误')
    }
  }

  private static getRuleType(rule: any) {
    return rule.split(':')
  }

  private static replace(data: IMaterial, caseData: any, replaceRule: any) {
    const rules = this.splitAndTrim(replaceRule)
    Object.keys(data).forEach((key: string) => {
      rules.forEach((v: any) => {
        data[key] = this.doReplace(data[key], caseData, v)
      })
    })
  }

  private static replaceData(data: IDataMaterial, caseData: any, replaceRule: any) {
    const rules = this.splitAndTrim(replaceRule)
    rules.forEach((v: any) => {
      data.data = this.doReplace(data.data, caseData, v)
    })
  }

  private static replaceMq(data: IMqMaterial, caseData: any, replaceRule: any) {
    const rules = this.splitAndTrim(replaceRule)
    rules.forEach((v: any) => {
      const [type, rule] = this.getRuleType(v)
      switch (type) {
        case MqParamEnum.EXCHANGE:
          data.exchange = this.doReplace(data.exchange, caseData, rule)
          break
        case MqParamEnum.ROUTINGKEY:
          data.routingKey = this.doReplace(data.routingKey, caseData, rule)
          break
        case MqParamEnum.CONTENT:
          data.content = this.doReplace(data.content, caseData, rule)
          break
        default:
          console.log(0)
      }
    })
  }

  private static replaceHttp(data: IHttpMaterial, caseData: any, replaceRule: any) {
    const rules = this.splitAndTrim(replaceRule)
    rules.forEach((v: any) => {
      const [type, rule] = this.getRuleType(v)
      switch (type) {
        case HttpParamEnum.BODY:
          data.data = this.doReplace(data.data, caseData, rule)
          break
        case HttpParamEnum.PATH:
          data.url = this.doReplace(data.url, caseData, rule)
          break
        case HttpParamEnum.QUERY:
          data.url = this.doReplace(data.url, caseData, rule)
          break
        default:
          console.log(0)
      }
    })
  }

  /**
   * 替换占位符
   * ${*} 替换为随机数
   * {{timestamp}} 替换为当前时间戳
   * {{timestampformat}}
   * @param data
   */
  static replaceOps(data: IMaterial) {
    Object.keys(data).forEach((key: string) => {
      if (!data[key]) return
      data[key] = this.replaceTimestamp(data[key])
      data[key] = this.replaceTimestampFormat(data[key])
      data[key] = this.replaceRandom(data[key])
    })
  }

  private static replaceTimestampFormat(data: any) {
    return data.replace(/{{timestampformat}}/g, dayjs(Date.now()).format('YYYY-MM-DD HH:mm:ss'))
  }

  private static replaceTimestamp(data: any) {
    return data.replace(/{{timestamp}}/g, Date.now())
  }

  /**
   * 用占位符替换为随机数，里面是数字是随机数的位数
   * ${3, n} 长度为3位的数字
   * ${3, s} 长度为3位的小写字母
   * ${3, S} 长度为3位的大写字母
   * ${3, 6, n} 长度为3到6位的数字
   * ${5, ns} 长度为5位的数字加小写字母
   * ${5, nS} 长度为5位的数字加大写字母
   * ${5, sS} 长度为5位的小写字母加大写字母
   * ${5, nsS} 长度为5位的数字加小写字母加大写字母
   * @param data
   */
  private static replaceRandom(data: any) {
    return data.replace(/\${(.*?)}/g, (a: any, b: any, c: any, d: any, e: any) => {
      const rules = b.split(',').map((v: any) => {
        return v.trim()
      })
      if (rules.length === 2) {
        rules.splice(1, 0, rules[0])
      }
      return generateRandom(Number(rules[0]), Number(rules[1]), rules[2])
    })
  }

  /**
   * sql:{name}=xiao 替换sql语句参数
   * body:{name}=xiao 替换http中body参数
   * query:{name}=xiao 替换http中query参数
   * path:{name}=xiao 替换http中path参数
   * key:{name}=xiao 替换redis中key参数
   * exchange:{name}=xiao 替换mq中exchange参数
   * routingKey:{name}=xiao 替换mq中routingKey参数
   * content:{name}=xiao 替换mq中content参数
   * @param data
   */
  static taskParamReplace(data: IMaterial) {
    const rules = this.splitAndTrim(data.paramReplace)
    rules.forEach((rule: string) => {
      const replaceData = this.getRuleType(rule)
      const type = replaceData.length === 2 ? replaceData[0] : ParamReplaceTypeEnum.NORMAL
      switch (type) {
        case ParamReplaceTypeEnum.BODY:
          data.data = this.doTaskReplace(data.data, replaceData.pop())
          break
        case ParamReplaceTypeEnum.PATH:
          data.url = this.doTaskReplace(data.url, replaceData.pop())
          break
        case ParamReplaceTypeEnum.QUERY:
          data.url = this.doTaskReplace(data.url, replaceData.pop())
          break
        case ParamReplaceTypeEnum.SQL:
          data.material = this.doTaskReplace(data.material, replaceData.pop())
          break
        case ParamReplaceTypeEnum.KEY:
          data.key = this.doTaskReplace(data.key, replaceData.pop())
          break
        case ParamReplaceTypeEnum.EXCHANGE:
          data.exchange = this.doTaskReplace(data.exchange, replaceData.pop())
          break
        case ParamReplaceTypeEnum.ROUTINGKEY:
          data.routingKey = this.doTaskReplace(data.routingKey, replaceData.pop())
          break
        case ParamReplaceTypeEnum.CONTENT:
          data.content = this.doTaskReplace(data.content, replaceData.pop())
          break
        default:
          console.log(0)
      }
    })
  }

  private static doTaskReplace(data: string, rule: any) {
    const [postRule, value] = this.getPostAndPreRule(rule)
    const regex = new RegExp(postRule.trim(), 'g')
    return data.replace(regex, value)
  }

  private static getPostAndPreRule(rule: string) {
    let postRule: any = ''
    let preRule = ''
    const arr = rule.split('=')
    if (arr.length === 2) {
      postRule = arr[0]
      preRule = arr[1]
    } else {
      postRule = arr.shift()
      preRule = arr.join('=')
    }
    return [postRule, preRule]
  }

  private static doReplace(data: string, preData: any, rule: any) {
    const [postRule, preRule] = this.getPostAndPreRule(rule)
    const preKeys = preRule.trim().split('.')
    const preValue = this.getOriginValue(preData, preKeys)
    const regex = new RegExp(postRule.trim(), 'g')
    return data.replace(regex, DataTransformUtils.stringIfObj(preValue))
  }

  private static splitAndTrim(str: any) {
    return str
      .split('\n')
      .map((v: any) => v.trim())
      .filter((v: any) => v)
  }

  private static getRealValue(value: any) {
    switch (value) {
      case 'null':
        return null
      case 'true':
        return true
      case 'false':
        return false
      default:
        return value
    }
  }

  private static compare(origin: any, condition: string) {
    const asserts = this.splitAndTrim(condition)
    asserts.forEach((assert: any) => {
      const [keys, value, type] = this.getKeyAndValue(assert)
      const realValue = this.getRealValue(value)
      const orginValue = this.getOriginValue(origin, keys)
      switch (type) {
        case '==':
          if (orginValue != realValue) {
            // throw new Error('断言失败')
            throw new AssertFailException('断言失败')
          }
          break
        case '!=':
          if (!orginValue) {
            // throw new Error('断言失败')
            throw new AssertFailException('断言失败')
          }
          break
        case '<=':
          if (orginValue > realValue) {
            // throw new Error('断言失败')
            throw new AssertFailException('断言失败')
          }
          break
        case '>=':
          if (orginValue < realValue) {
            // throw new Error('断言失败')
            throw new AssertFailException('断言失败')
          }
          break
        default:
          console.log(0)
      }
    })
  }

  private static getKeyAndValue(assert: string) {
    const matched = assert.match(/[=!<>]\=/) as any
    const type = matched[0]
    const [keyStr, value] = assert.split(type)
    const keys = keyStr.trim().split('.')
    return [keys, value.trim(), type]
  }

  private static getOriginValue(origin: any, keys: any) {
    let result: any = origin
    keys.splice(0, 1)
    keys.forEach((v: any) => {
      if (v.match('---')) {
        /**
         * 支持参数替换获取某个字段某个范围的值，比如data: 'abcdef', 可以获取'de'
         * 使用方法
         * {} = root.a.b.c.xxx---xxx
         * xxx是一些字符，用来定位具体的值
         */
        const matched = result.match(v.replace('---', '(.*)'))
        if (matched) {
          result = matched[1]
        } else {
          result = ''
        }
      } else {
        result = result[v]
      }
    })
    return result
  }
}

export { ConditionUtils }
