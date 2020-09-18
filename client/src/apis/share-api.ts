import { instance as ajax } from '@/lib/http'
import { IShareMaterial, IShareMaterialDTO, IShareMaterialParams } from '@/interfaces/share-interface'

export const fetchShareMaterials = () => ajax.get<IShareMaterialDTO>('/share-material')

export const saveShareMaterial = (payload: IShareMaterial) => ajax.post('/share-material', payload)

export const saveShareMaterialParams = (payload: IShareMaterialParams) => ajax.post('/share-material/params', payload)

export const updateShareMaterial = (payload: IShareMaterial) => ajax.put('/share-material', payload)

export const deleteShareMaterial = (payload: IShareMaterial) => ajax.delete('/share-material', { data: payload })
