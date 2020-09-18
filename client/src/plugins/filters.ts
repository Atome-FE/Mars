import dayjs from 'dayjs'
import { VueConstructor } from 'vue'
// 格式化货币
export function formatMoney(value: string | number) {
  try {
    if (typeof value === 'string') {
      value = Number(value) - 0
    }
    return value
      .toFixed(0)
      .replace(/\B(?=(\d{3})+$)/g, '.')
      .replace(/^/, 'Rp ')
  } catch (error) {
    return 'Rp -'
  }
}

function formatBank(value: string) {
  if (!value) return '-'
  if (typeof value === 'string') {
    value += ''
  }
  return value.replace(/\B(?=(\d{4})+$)/g, ' ')
}

function formatTimestamp(value: string) {
  if (!value) {
    return '-'
  }
  try {
    return dayjs(value).format('DD/MM/YYYY HH:mm:ss')
  } catch (error) {
    return '-'
  }
}

export default {
  install(Vue: VueConstructor, options = {}) {
    Vue.filter('formatMoney', formatMoney)
    Vue.filter('formatBank', formatBank)
    Vue.filter('formatTimestamp', formatTimestamp)
  },
}
