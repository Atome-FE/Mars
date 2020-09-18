import axios, { AxiosRequestConfig, AxiosResponse } from 'axios'

import * as uuid from 'uuid'

import { EnvModule } from '@/store/modules/env-module'
import { UserModule } from '@/store/modules/user-module'

import { ResponseCodeEnum } from '@/enums/response-enum'
import { handleError } from '@/helpers/error-handler'

const qs = require('qs')

const CancelToken = axios.CancelToken
let cancel: any

export const getUuidV4 = () => {
  return uuid.v4()
}

export const getUuid = () => {
  if (!localStorage.getItem('webAdvertisingId')) {
    localStorage.setItem('webAdvertisingId', uuid.v4())
  }
  return localStorage.getItem('webAdvertisingId')
}

const instance = axios.create({
  baseURL: '/t-api/v1',
  timeout: 60000, // 改为60s
  paramsSerializer(params) {
    return qs.stringify(params, { arrayFormat: 'repeat' })
  },
})

const taskInstance = axios.create({
  baseURL: '/t-api/v2',
  timeout: 60000, // 改为60s
  paramsSerializer(params) {
    return qs.stringify(params, { arrayFormat: 'repeat' })
  },
})

const appInstance = axios.create({
  baseURL: '/t-api/v3',
  timeout: 60000, // 改为60s
  paramsSerializer(params) {
    return qs.stringify(params, { arrayFormat: 'repeat' })
  },
})

const responseHandlerInterceptor = (response: any) => {
  if (response.config.ignoreResponseHandler) {
    return response
  }
  if (response.data.code !== ResponseCodeEnum.SUCCESS) {
    const e = {} as any
    e.message = response.data.message
    e.code = response.data.code
    e.extra = response.data.extra || ''
    throw e
  }
  return response.data
}

const taskResponseHandlerInterceptor = (response: any) => {
  if (response.config.ignoreResponseHandler) {
    return response
  }
  if (response.data.code !== ResponseCodeEnum.SUCCESS) {
    const e = {} as any
    e.message = response.data.message
    e.code = response.data.code
    e.extra = response.data.extra || ''
    throw e
  }
  return response.data
}

const requestInterceptor = (config: AxiosRequestConfig) => {
  if (!EnvModule.selectedEnv.id && config.baseURL === '/t-api/v2') {
    window.vue.$notify.warning({
      title: '警告',
      message: '请先选择环境变量',
    })

    config.cancelToken = new CancelToken(c => {
      cancel = c
    })
    if (cancel) {
      cancel() // cancel request
    }

    return config
  }
  config.headers = { ENVIRONMENT: 'staging', 'ENV-CONFIG': EnvModule.selectedEnv.id, 'USER-ID': UserModule.userId }
  return config
}

interface IResponseError {
  code: string
  message: string
  extra?: any
}

const errorResponse = (error: any) => {
  const response = error.response as AxiosResponse<IResponseError>
  handleError({
    code: response.data.code,
    response: null,
    message: response.data.message,
    request: null,
  })
  return Promise.reject(error)
}
const taskErrorResponse = (error: any) => {
  const response = error.response as AxiosResponse<IResponseError>
  handleError({
    code: response.data.code,
    response: null,
    message: response.data.message,
    request: null,
  })
  return Promise.reject(error)
}

instance.interceptors.response.use(responseHandlerInterceptor, errorResponse)
instance.interceptors.request.use(requestInterceptor)

taskInstance.interceptors.response.use(taskResponseHandlerInterceptor, taskErrorResponse)
taskInstance.interceptors.request.use(requestInterceptor)

appInstance.interceptors.response.use(taskResponseHandlerInterceptor, taskErrorResponse)
appInstance.interceptors.request.use(requestInterceptor)

export { instance, taskInstance, appInstance }
