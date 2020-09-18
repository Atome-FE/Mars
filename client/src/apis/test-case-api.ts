import { instance as ajax } from '@/lib/http'
import { ITestCase } from '@/interfaces/test-case-interface'

export const fetchTestCaseMaterials = () => ajax.get<ITestCase[]>('/test-case-material')

export const saveTestCaseMaterial = (payload: ITestCase) => ajax.post<ITestCase>('/test-case-material', payload)

export const updateTestCaseMaterial = (payload: ITestCase) => ajax.put<ITestCase>('/test-case-material', payload)

export const deleteTestCaseMaterial = (payload: ITestCase) => ajax.delete('/test-case-material', { data: payload })
