import Vue from 'vue'
import Router from 'vue-router'
import TaskOpsRoutes from '@/views/task-ops/router'
import AuthRoutes from '@/views/auth/router'
import MaterialRoutes from '@/views/material/router'
import DockerServiceRoutes from '@/views/docker-service/router'

const originalPush = Router.prototype.push
Router.prototype.push = function push(location: any) {
  return (originalPush.call(this, location) as any).catch((err: any) => {
    console.log(err)
  })
}

const originalReplace = Router.prototype.replace
Router.prototype.replace = function replace(location: any) {
  return (originalReplace.call(this, location) as any).catch((err: any) => {
    console.log(err)
  })
}

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      name: 'AppHome',
      path: '/',
      redirect: '/test/group/home',
    },
    ...MaterialRoutes,
    ...TaskOpsRoutes,
    ...AuthRoutes,
    ...DockerServiceRoutes,
  ],
})
