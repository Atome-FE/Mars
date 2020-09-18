export const handleError = (error: { code: string; response: any; message: string; request: any }) => {
  const msg = '服务器出错，稍后再试!'
  if (
    error.code === 'UNAUTHORIZED' ||
    error.code === 'TOKEN_EXPIRED' ||
    (error.response && error.response.status === 401)
  ) {
    const { pathname } = window.location
    console.log(window.location.href, 'location.href')
    window.vue.$router.push({ name: 'Login', query: { redirect: pathname } })
  } else if (error.code) {
    window.vue.$notify({ message: error.message || msg, type: 'error', title: 'error' })
  } else if (error.response) {
    window.vue.$notify({
      message: error.response.data ? error.response.data.message : msg,
      type: 'error',
      title: 'error',
    })
  } else if (error.request) {
    window.vue.$notify({ message: msg, type: 'error', title: 'error' })
  } else {
    window.vue.$notify({ message: error.message || msg, type: 'error', title: 'error' })
  }
}

export default {
  install(Vue: any) {
    Vue.prototype.$handleError = handleError
    Vue.$handleError = handleError
  },
}
