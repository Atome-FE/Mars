import yaml from 'js-yaml'
import Mock from 'mockjs'
import { IMaterial } from '@/interfaces/common-interface'
import { ParamReplaceTypeEnum, ParamReplaceTaskEnum } from '@/enums/common-enum'
import { generateRandom } from '@/lib/utils/uuid'
import dayjs from 'dayjs'
import { ParamReplaceException } from '@/exceptions/ParamReplaceException'
import { EnvModule } from '@/store/modules/env-module'
import { RegRexConstants } from '@/constants/regrex-constants'

export class ReplaceUtils {
  private static getOriginValue(preResult: any, value: string) {
    const keys = value
      .split('.')
      .map(v => v.trim())
      .filter(v => v)
    let result: any = preResult
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

  private static recursiveAssignment(rule: any, preResult: any) {
    if (typeof rule !== 'object') {
      return
    }
    Object.keys(rule).forEach(key => {
      if (typeof rule[key] !== 'object') {
        rule[key] = this.getOriginValue(preResult, rule[key])
      } else {
        this.recursiveAssignment(rule[key], preResult)
      }
    })
  }

  static caseParamReplace(material: IMaterial, preResult: any, replaceRule: any) {
    try {
      const rule = yaml.load(replaceRule as string)
      this.recursiveAssignment(rule, preResult)
      this.doCaseReplace(material, rule)
      return true
    } catch (e) {
      throw new ParamReplaceException('参数替换出错')
    }
  }

  private static doCaseReplace(material: IMaterial, rule: any) {
    this.taskParamReplace(material, rule)
  }

  static doReplace(material: IMaterial) {
    if (material.paramReplace) {
      this.taskParamReplace(material)
    }
    this.replaceOps(material)
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
      data[key] = this.replaceGlobalVariable(data[key])
      data[key] = this.replaceMockjs(data[key])
    })
  }

  private static replaceMockjs(data: any) {
    return data.replace(RegRexConstants.RE_PLACEHOLDER, (a: any, b: any, c: any, d: any, e: any) => {
      return Mock.mock(a)
    })
  }

  private static replaceGlobalVariable(data: any) {
    const { globalVariable } = EnvModule.selectedEnv
    if (!globalVariable) return data
    const value = JSON.parse(globalVariable)
    return data.replace(/\#{(.*?)}/g, (a: any, b: any, c: any, d: any, e: any) => {
      return value[b]
    })
  }

  private static replaceParam(material: IMaterial, key: string, rule: any) {
    Object.keys(rule).forEach(k => {
      const regex = new RegExp(`{${k}}`, 'g')
      /* 如果出bug了，可以替换为下面的一行代码
      material[key] = material[key].replace(regex, rule[k])
      */
      if ((key === 'url' || key === 'path') && typeof rule[k] === 'string') {
        const value = rule[k] || ''
        const arr = value.split(',').map((v: any) => v.trim()).filter((v: any) => v)
        if (arr.length > 1) {
          const querys = arr.map((v: any) => {
            return `${k}=${v}`
          })
          const result = querys.join('&')
          const reg = new RegExp(`${k}={${k}}`, 'g')
          material[key] = material[key].replace(reg, result)
        } else {
          material[key] = material[key].replace(regex, rule[k])
        }
      } else {
        material[key] = material[key].replace(regex, rule[k])
      }
    })
  }

  private static replaceHttpParam(material: IMaterial, rule: any) {
    Object.keys(rule).forEach(key => {
      switch (key) {
        case ParamReplaceTypeEnum.BODY:
          this.replaceParam(material, 'data', rule[key])
          break
        case ParamReplaceTypeEnum.QUERY:
          this.replaceParam(material, 'url', rule[key])
          break
        case ParamReplaceTypeEnum.PATH:
          this.replaceParam(material, 'url', rule[key])
          break
        case ParamReplaceTypeEnum.HEADERS:
          this.replaceParam(material, 'headers', rule[key])
          break
        default:
          console.log(0)
      }
    })
  }

  private static replaceSqlParam(material: IMaterial, rule: any) {
    Object.keys(rule).forEach(key => {
      switch (key) {
        case ParamReplaceTypeEnum.STATEMENT:
          this.replaceParam(material, 'material', rule[key])
          break
        default:
          console.log(0)
      }
    })
  }

  private static replaceRedisParam(material: IMaterial, rule: any) {
    Object.keys(rule).forEach(key => {
      switch (key) {
        case ParamReplaceTypeEnum.KEY:
          this.replaceParam(material, 'key', rule[key])
          break
        default:
          console.log(0)
      }
    })
  }

  private static replaceMqParam(material: IMaterial, rule: any) {
    Object.keys(rule).forEach(key => {
      switch (key) {
        case ParamReplaceTypeEnum.EXCHANGE:
          this.replaceParam(material, 'exchange', rule[key])
          break
        case ParamReplaceTypeEnum.ROUTINGKEY:
          this.replaceParam(material, 'routingKey', rule[key])
          break
        case ParamReplaceTypeEnum.CONTENT:
          this.replaceParam(material, 'content', rule[key])
          break
        default:
          console.log(0)
      }
    })
  }

  private static replaceMongoParam(material: IMaterial, rule: any) {}

  private static replaceDataParam(material: IMaterial, rule: any) {
    Object.keys(rule).forEach(key => {
      switch (key) {
        case ParamReplaceTypeEnum.SOURCE:
          this.replaceParam(material, 'data', rule[key])
          break
        default:
          console.log(0)
      }
    })
  }

  static taskParamReplace(material: IMaterial, replaceRule?: any) {
    let rule = replaceRule
    if (!rule) {
      const { paramReplace } = material
      rule = yaml.load(paramReplace as string)
    }
    Object.keys(rule).forEach(key => {
      switch (key) {
        case ParamReplaceTaskEnum.HTTP:
          this.replaceHttpParam(material, rule[key])
          break
        case ParamReplaceTaskEnum.SQL:
          this.replaceSqlParam(material, rule[key])
          break
        case ParamReplaceTaskEnum.REDIS:
          this.replaceRedisParam(material, rule[key])
          break
        case ParamReplaceTaskEnum.MONGO:
          this.replaceMongoParam(material, rule[key])
          break
        case ParamReplaceTaskEnum.MQ:
          this.replaceMqParam(material, rule[key])
          break
        case ParamReplaceTaskEnum.DATA:
          this.replaceDataParam(material, rule[key])
          break
        default:
          console.log(0)
      }
      /*
      switch (key) {
        case ParamReplaceTypeEnum.BODY:
          // data.data = this.doTaskReplace(data.data, replaceData.pop())
          break
        case ParamReplaceTypeEnum.PATH:
          // data.url = this.doTaskReplace(data.url, replaceData.pop())
          break
        case ParamReplaceTypeEnum.QUERY:
          // data.url = this.doTaskReplace(data.url, replaceData.pop())
          break
        case ParamReplaceTypeEnum.SQL:
          // data.material = this.doTaskReplace(data.material, replaceData.pop())
          break
        case ParamReplaceTypeEnum.KEY:
          // data.key = this.doTaskReplace(data.key, replaceData.pop())
          break
        case ParamReplaceTypeEnum.EXCHANGE:
          // data.exchange = this.doTaskReplace(data.exchange, replaceData.pop())
          break
        case ParamReplaceTypeEnum.ROUTINGKEY:
          // data.routingKey = this.doTaskReplace(data.routingKey, replaceData.pop())
          break
        case ParamReplaceTypeEnum.CONTENT:
          // data.content = this.doTaskReplace(data.content, replaceData.pop())
          break
        default:
          console.log(0)
      }
      */
    })
  }
}
