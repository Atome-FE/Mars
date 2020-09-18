import { RouteConstants } from '@/constants/route-constants'
import { RoleTypeEnum } from '@/enums/user-enum'

export default [
  {
    name: RouteConstants.MATERIAL,
    path: '/material/:materialType',
    component: () => import('./pages/MaterialHome.vue'),
    children: [
      {
        name: RouteConstants.MATERIALPAGE,
        path: ':materialId',
        component: () => import('./pages/MaterialContent.vue'),
        meta: {
          needAuth: true,
          authList: [RoleTypeEnum.ADMIN],
        },
      },
    ],
    meta: {
      needAuth: true,
      authList: [RoleTypeEnum.ADMIN],
    },
  },
]
