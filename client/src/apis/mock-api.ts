import { instance as ajax } from '@/lib/http'
import { IMaterial } from '@/interfaces/common-interface'

const URL_PREFIX = '/mock-material'
export const fetchMockMaterials = () => ajax.get<IMaterial[]>(URL_PREFIX)

export const saveMockMaterial = (payload: IMaterial) => ajax.post<IMaterial>(URL_PREFIX, payload)

export const updateMockMaterial = (payload: IMaterial) => ajax.put<IMaterial>(URL_PREFIX, payload)

export const deleteMockMaterial = (payload: IMaterial) => ajax.delete(URL_PREFIX, { data: payload })
