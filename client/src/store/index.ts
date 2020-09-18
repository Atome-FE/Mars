import Vuex from 'vuex'
import Vue from 'vue'
import createLogger from 'vuex/dist/logger'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export interface IRootState {}

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store<IRootState>({
  strict: debug,
  /* 先注释掉，如果运行一段时间没问题，可以考虑删除下面注释的代码
  plugins: debug ? [createLogger({}), createPersistedState({
    paths: [],
  })] : [createPersistedState({
    paths: [],
  })],
  */
})
