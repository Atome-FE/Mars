import '@babel/polyfill'
import { Component } from 'vue-property-decorator'
import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueClipboard from 'vue-clipboard2'

import store from '@/store/index'
import '@/lib/common/icon-handler'
import filters from '@/plugins/filters'

import { DataLoadUtils } from '@/lib/utils/data-load-utils'
import { RouteConstants } from '@/constants/route-constants'
import { UserModule } from '@/store/modules/user-module'
import errorHandler from './helpers/error-handler'
import router from './router'
import App from './App.vue'

// import 'highlight.js/styles/vs2015.css'

VueClipboard.config.autoSetContainer = true
Vue.use(VueClipboard)

Vue.config.productionTip = false

Vue.use(errorHandler)

Vue.use(ElementUI)
Vue.use(filters)

Component.registerHooks(['beforeRouteEnter', 'beforeRouteLeave', 'beforeRouteUpdate'])

const vConsole = require('vconsole')

if (process.env.VUE_APP_CONSOLE === 'Yes') {
  /* eslint-disable-next-line */
  new vConsole()
}
let hasFetchUserInfo = false
router.beforeEach(async (to, from, next) => {
  // 登录、及第一次加载页面需要load data；没有权限的页面不进行跳转
  if (from && from.name === RouteConstants.LOGIN) {
    hasFetchUserInfo = false
  }
  if (!hasFetchUserInfo && ![RouteConstants.LOGIN, RouteConstants.REGISTER].includes(to.name || '')) {
    await DataLoadUtils.loadAllData()
    hasFetchUserInfo = true
  }
  if ([RouteConstants.LOGIN, RouteConstants.REGISTER].includes(to.name || '')) {
    next()
  } else if (to.meta.needAuth && to.meta.authList && to.meta.authList.includes(UserModule.role)) {
    next()
  } else {
    next(false)
  }
})
window.vue = new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
