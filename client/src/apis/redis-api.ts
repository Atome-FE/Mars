import { instance as ajax } from '@/lib/http'
import { IRedisMaterial } from '@/interfaces/redis-interface'

export const fetchRedisMaterials = () => ajax.get<IRedisMaterial[]>('/redis-material')

export const saveRedisMaterial = (payload: IRedisMaterial) => ajax.post<IRedisMaterial>('/redis-material', payload)

export const updateRedisMaterial = (payload: IRedisMaterial) => ajax.put<IRedisMaterial>('/redis-material', payload)

export const deleteRedisMaterial = (payload: IRedisMaterial) => ajax.delete('/redis-material', { data: payload })
