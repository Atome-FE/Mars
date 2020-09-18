import { RouteConstants } from '@/constants/route-constants'

export default [
  {
    name: RouteConstants.LOGIN,
    path: '/user/login',
    component: () => import('./pages/LoginApp.vue'),
  },
  {
    name: RouteConstants.REGISTER,
    path: '/user/register',
    component: () => import('./pages/RegisterApp.vue'),
  },
]
