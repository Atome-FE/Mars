import { AssertRuleException } from '@/exceptions/AssertRuleException'

export class AssertUtils {
  private static runCodeWithFunction(root: any, condition: any, preRoot: any) {
    const func = `function(root, preRoot){ ${condition} }`
    return Function(`"use strict";return (${func})`)()(root, preRoot)
  }

  static assert(origin: any, condition: string, preRoot: any) {
    try {
      const result = this.runCodeWithFunction(origin, condition, preRoot)
      return !!result
    } catch (e) {
      throw new AssertRuleException('断言规则错误!')
    }
  }
}
