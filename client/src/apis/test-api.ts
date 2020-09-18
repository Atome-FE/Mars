import { instance as ajax } from '@/lib/http'
import {
  ITestCase, ITestGroup, ITestGroupCase, IFieldCategory, IFormField,
} from '@/interfaces/test-interface'

export const fetchFieldCategories = () => ajax.get<IFieldCategory[]>('/field-category')

export const saveFieldCategory = (payload: IFieldCategory) => ajax.post('/field-category', payload)

export const fetchFormFields = () => ajax.get<IFormField[]>('/form-field')

export const saveFormField = (payload: IFormField) => ajax.post('/form-field', payload)

export const fetchTestCases = () => ajax.get('/test-case')

export const saveTestCase = (payload: ITestCase) => ajax.post('/test-case', payload)

export const fetchTestGroupCases = () => ajax.get<ITestGroupCase[]>('/test-group-case')

export const saveTestGroupCase = (payload: ITestGroupCase) => ajax.post<ITestGroupCase>('/test-group-case', payload)

export const deleteTestGroupCase = (payload: ITestGroupCase) => ajax.delete('/test-group-case', { data: payload })

export const updateTestGroupCasePriority = (payload: ITestGroupCase) => ajax.put('/test-group-case', payload)

export const fetchTestGroups = () => ajax.get<ITestGroup[]>('/test-group')

export const saveTestGroup = (payload: ITestGroup) => ajax.post<ITestGroup>('/test-group', payload)

export const updateTestGroup = (payload: ITestGroup) => ajax.put<ITestGroup>('/test-group', payload)

export const deleteTestGroup = (payload: ITestGroup) => ajax.delete('/test-group', { data: payload })
