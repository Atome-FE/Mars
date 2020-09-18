import { instance as ajax } from '@/lib/http'

export const fetchAuth = () => ajax.get('/admin/me')

export const fetchUsers = () => ajax.get('/admin/users')

export const fetchRoles = () => ajax.get('/admin/roles')

export const addUser = (payload: any) => ajax.post('/admin/users', payload)
