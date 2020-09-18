import { instance as ajax } from '@/lib/http'
import { ISqlMaterial } from '@/interfaces/sql-interface'

export const fetchSqlMaterials = () => ajax.get<ISqlMaterial[]>('/sql-material')

export const saveSqlMaterial = (payload: ISqlMaterial) => ajax.post<ISqlMaterial>('/sql-material', payload)

export const updateSqlMaterial = (payload: ISqlMaterial) => ajax.put<ISqlMaterial>('/sql-material', payload)

export const deleteSqlMaterial = (payload: ISqlMaterial) => ajax.delete('/sql-material', { data: payload })
