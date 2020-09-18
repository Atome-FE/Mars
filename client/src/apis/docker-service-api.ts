import { instance as ajax, appInstance } from '@/lib/http'
import { IDockerServiceMapping } from '@/interfaces/docker-service-interface'

const PRE_FIX = '/service-mapping'
const DOCKER_SERVICE_PRE_FIX = '/docker-service'
export const fetchDockerMapping = () => ajax.get<IDockerServiceMapping[]>(PRE_FIX)

export const saveDockerMapping = (payload: IDockerServiceMapping) => ajax.post<IDockerServiceMapping>(PRE_FIX, payload)

export const updateDockerMapping = (payload: IDockerServiceMapping) => ajax.put<IDockerServiceMapping>(PRE_FIX, payload)

export const deleteDockerMapping = (payload: IDockerServiceMapping) => ajax.delete(PRE_FIX, { data: payload })

export const restartDockerService = (payload: IDockerServiceMapping) =>
  appInstance.post(`${DOCKER_SERVICE_PRE_FIX}/restart`, payload)

export const stopDockerService = (payload: IDockerServiceMapping) =>
  appInstance.post(`${DOCKER_SERVICE_PRE_FIX}/stop`, payload)

export const healthCheckDockerService = (payload: IDockerServiceMapping[]) =>
  appInstance.post(`${DOCKER_SERVICE_PRE_FIX}/health-check`, payload)
