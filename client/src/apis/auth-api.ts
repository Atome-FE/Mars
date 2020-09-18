import { instance as ajax } from '@/lib/http'
import { ILoginParams, IRegisterParams } from '@/interfaces/auth-interface'

export const login = (payload: ILoginParams) => ajax.post('/auth/login', payload)

export const register = (payload: IRegisterParams) => ajax.post('/auth/register', payload)

export const logout = () => ajax.post('/auth/logout')
