import { instance as ajax } from '@/lib/http'
import { IHttpMaterial } from '@/interfaces/http-interface'
import { IMaterial } from '@/interfaces/common-interface'

export const fetchHttpMaterials = () => ajax.get<IMaterial[]>('/http-material')

export const saveHttpMaterial = (payload: IMaterial) => ajax.post<IMaterial>('/http-material', payload)

export const updateHttpMaterial = (payload: IMaterial) => ajax.put<IMaterial>('/http-material', payload)

export const deleteHttpMaterial = (payload: IMaterial) => ajax.delete('/http-material', { data: payload })
