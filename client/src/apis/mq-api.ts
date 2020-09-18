import { instance as ajax } from '@/lib/http'
import { IMqMaterial } from '@/interfaces/mq-interface'

export const fetchMqMaterials = () => ajax.get<IMqMaterial[]>('/mq-material')

export const saveMqMaterial = (payload: IMqMaterial) => ajax.post<IMqMaterial>('/mq-material', payload)

export const updateMqMaterial = (payload: IMqMaterial) => ajax.put<IMqMaterial>('/mq-material', payload)

export const deleteMqMaterial = (payload: IMqMaterial) => ajax.delete('/mq-material', { data: payload })
