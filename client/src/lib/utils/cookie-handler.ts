const VueCookie = require('vue-cookie')

export const setCookie = VueCookie.set

export const deleteCookie = VueCookie.delete

export const getCookie = VueCookie.get

export const getThirdPartyParamCookie = (key: string) => {
  return JSON.parse(getCookie('thirdPartyParams') || '{}')[key]
}
