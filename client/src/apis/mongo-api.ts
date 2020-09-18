import { instance as ajax } from '@/lib/http'
import { IMongoMaterial } from '@/interfaces/mongo-interface'

export const fetchMongoMaterials = () => ajax.get<IMongoMaterial[]>('/mongo-material')

export const saveMongoMaterial = (payload: IMongoMaterial) => ajax.post<IMongoMaterial>('/mongo-material', payload)

export const updateMongoMaterial = (payload: IMongoMaterial) => ajax.put<IMongoMaterial>('/mongo-material', payload)

export const deleteMongoMaterial = (payload: IMongoMaterial) => ajax.delete('/mongo-material', { data: payload })
