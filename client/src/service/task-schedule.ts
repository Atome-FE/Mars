import { CaseService } from '@/service/case-service'
import { ITestCase } from '@/interfaces/test-case-interface'
import { getTestCaseMaterialById } from '@/store/modules/test-case-material-module'

export class TaskSchedule {
  static async runTestGroupCase(testCaseId: string, executeTime: number, testGroupId: string) {
    const testCase: ITestCase = getTestCaseMaterialById(testCaseId) as ITestCase
    const caseService = new CaseService(testCase, executeTime, testGroupId)
    const result = await caseService.run()
    caseService.close()
    return result
  }

  static async runTestCase(testCase: ITestCase) {
    const caseService = new CaseService(testCase, new Date().getTime())
    const result = await caseService.run()
    caseService.close()
    return result
  }
}
