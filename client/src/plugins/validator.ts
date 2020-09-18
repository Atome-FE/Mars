import VeeValidate, { Validator } from 'vee-validate'
import { VueConstructor } from 'vue'

const zh_CN = require('vee-validate/dist/locale/zh_CN')
const en = require('vee-validate/dist/locale/en')

export default (Vue: VueConstructor) => {
  Vue.use(VeeValidate, {
    locale: 'zh_CN',
  })

  Validator.localize({
    zh_CN,
    'en-us': en,
  })

  Validator.localize({
    'en-us': {
      messages: {
        required: 'The field is required.',
      },
    },
    'zh_CN': {
      messages: {
        required: '该字段是必填',
      },
    },
  })

  const validateRules = {
    password: {
      messages: {
        'en-us': (field: any) => {
          return 'Password must be at least 8 characters'
        },
        'zh_CN': (field: any) => {
          return '密码至少8位'
        },
      },
      validate(value: any) {
        return /^.{8,}$/.test(value)
      },
    },
  }

  // custom validator
  Object.keys(validateRules).forEach((key) => {
    Validator.extend(key, (validateRules as any)[key].validate)

    // merge the validator messages
    Object.keys((validateRules as any)[key].messages).forEach((locale) => {
      Validator.localize({
        [locale]: {
          messages: {
            [key]: (validateRules as any)[key].messages[locale],
          },
        },
      })
    })
  })
}
