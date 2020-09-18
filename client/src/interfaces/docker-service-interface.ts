export interface IDockerServiceMapping {
  id?: string
  userId?: string
  cmd: string
  restartCmd: string
  stopCmd: string
  dockerName: string
  heathly?: boolean
  needCheck?: boolean
  environmentName: string
  status?: string
}
