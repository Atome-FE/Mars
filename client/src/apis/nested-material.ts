import { instance as ajax } from '@/lib/http'
import { INestedMaterial } from '@/interfaces/nested-material-interface'
import { TNestedMaterials } from '@/store/modules/nested-material-module'

const PER_FIX = '/nested-material'

export const fetchAllNestedMaterials = () => ajax.get<TNestedMaterials>(`${PER_FIX}/all`)

export const fetchNestedMaterials = () => ajax.get<INestedMaterial[]>(PER_FIX)

export const saveNestedMaterials = (payload: INestedMaterial) => ajax.post<INestedMaterial>(PER_FIX, payload)

export const updateNestedMaterials = (payload: INestedMaterial) => ajax.put<INestedMaterial>(PER_FIX, payload)

export const deleteNestedMaterials = (payload: INestedMaterial) => ajax.delete(PER_FIX, { data: payload })
