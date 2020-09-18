import { RouteConstants } from '@/constants/route-constants'
import { RoleTypeEnum } from '@/enums/user-enum'

export default [
  {
    name: RouteConstants.DOCKER_SERVICE,
    path: '/docker-service',
    component: () => import('./pages/DockerServicePage.vue'),
    meta: {
      needAuth: true,
      authList: [RoleTypeEnum.ADMIN],
    },
  },
]
