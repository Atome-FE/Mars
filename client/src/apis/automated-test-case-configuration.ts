import { instance as ajax } from '@/lib/http'
import { IAutomatedConfiguration } from '@/interfaces/test-case-configuration'

const url = '/testcase/configuration'

export const addNewTestCase = (testCase:IAutomatedConfiguration) => ajax.post(`${url}/add`, testCase)
export const updateTestCase = (testCase:IAutomatedConfiguration) => ajax.post(`${url}/update`, testCase)
export const deleteTestCase = (testCase:IAutomatedConfiguration) => ajax.post(`${url}/delete`, testCase)
export const getTestCases = (businessLine:string) => ajax.get(`${url}/businessLine/${businessLine}`)
export const getAllBusinessLine = () => ajax.get(`${url}/all/businessLine`)
export const updateBusinessLineName = (data:any) => ajax.post(`${url}/businessLine/update/name`, data)
export const manualRunTasks = () => ajax.get('/task/automatedTestTask/run/run')
export const manualRunCurrentBusinessLineTask = (data:string) => ajax.post(`${url}/execute/${data}`)
export const getSleepValue = () => ajax.get('/variable/sleep')
export const setSleepValue = (value:string) => ajax.post(`/variable/sleep?newValue=${value}`)
