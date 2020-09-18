import { instance as ajax } from '@/lib/http'
import { IEnv } from '@/interfaces/env-interface'

export const fetchEnvs = () => ajax.get<IEnv[]>('/env')

export const saveEnv = (payload: IEnv) => ajax.post<IEnv>('/env', payload)

export const updateEnv = (payload: IEnv) => ajax.put<IEnv>('/env', payload)

export const deleteEnv = (payload: IEnv) => ajax.delete('/env', { data: payload })
