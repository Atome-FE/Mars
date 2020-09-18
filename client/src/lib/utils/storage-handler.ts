export const getItem = (key: string) => {
  return localStorage.getItem(key)
}

export const setItem = (key: string, value: any) => {
  localStorage.setItem(key, value)
}

export const removeItem = (key: string) => {
  localStorage.removeItem(key)
}

export const getBaseItem = (name: string, defaultValue: string) => {
  return JSON.parse(getItem(name) || defaultValue)
}

export const getThirdPartyParam = (key: string) => {
  return getBaseItem('thirdPartyParams', '{}')[key]
}
