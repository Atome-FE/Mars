import { RouteConstants } from '@/constants/route-constants'
import { RoleTypeEnum } from '@/enums/user-enum'

export default [
  {
    name: RouteConstants.SHAREHOME,
    path: '/test/share',
    component: () => import('./pages/ShareMaterialHome.vue'),
    meta: {
      reload: true,
      needAuth: true,
      authList: [RoleTypeEnum.ADMIN, RoleTypeEnum.NORMAL],
    },
  },
  {
    name: RouteConstants.TESTGROUPHOME,
    path: '/test/group/home',
    component: () => import('./pages/TestGroupHome.vue'),
    meta: {
      reload: true,
      needAuth: true,
      authList: [RoleTypeEnum.ADMIN, RoleTypeEnum.NORMAL],
    },
  },
  {
    name: RouteConstants.TASKRECORDHOME,
    path: '/test/record/home',
    component: () => import('./pages/TaskRecordHome.vue'),
    meta: {
      needAuth: true,
      authList: [RoleTypeEnum.ADMIN],
    },
  },
  {
    path: '/setting',
    component: () => import('./pages/Setting.vue'),
    children: [
      {
        name: RouteConstants.AUTHUSER,
        path: 'user',
        component: () => import('./pages/UserManagement.vue'),
        meta: {
          needAuth: true,
          authList: [RoleTypeEnum.ADMIN],
        },
      },
      {
        name: RouteConstants.AUTHROLE,
        path: 'role',
        component: () => import('./pages/RoleManagement.vue'),
      },
    ],
  },
  {
    name:RouteConstants.AUTOTESTCONFIGURATION,
    path: '/test/configuration/home',
    component: () => import('./pages/AutoTestConfigurationHome.vue'),
    meta: {
      needAuth: true,
      authList: [RoleTypeEnum.ADMIN],
    }
  }
]
