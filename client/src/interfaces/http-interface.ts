import { IMaterial } from '@/interfaces/common-interface'

export interface IHttpMaterial extends IMaterial {
  [key: string]: any
  id: string
  name: string
  method: string
  headers: string
  params: string
  data: string
  url: string
  dataType?: string
  dataHandleType: string
}

export interface IHttpTab {
  name: string
  active: boolean
}

export interface IHttpHeader {
  checked: boolean
  key: string
  value: string
  description: string
}

export interface IHttpMethod {
  label: string
  value: string
}
