import { instance as ajax } from '@/lib/http'
import { IDataMaterial } from '@/interfaces/data-material-interface'

export const fetchDataMaterials = () => ajax.get<IDataMaterial[]>('/data-material')

export const getDataMaterial = (id: string) => ajax.get<IDataMaterial>(`/data-material/${id}`)

export const saveDataMaterial = (payload: IDataMaterial) => ajax.post<IDataMaterial>('/data-material', payload)

export const updateDataMaterial = (payload: IDataMaterial) => ajax.put<IDataMaterial>('/data-material', payload)

export const deleteDataMaterial = (payload: IDataMaterial) => ajax.delete('/data-material', { data: payload })
