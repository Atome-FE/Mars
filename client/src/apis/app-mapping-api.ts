import { instance as ajax } from '@/lib/http'
import { IAppMapping } from '@/interfaces/app-mapping-interface'

const url = '/app-mapping'

export const fetchAppMapping = () => ajax.get<IAppMapping[]>(url)
