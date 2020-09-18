module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: ['plugin:vue/essential', '@vue/airbnb', '@vue/typescript'],
  rules: {
    'operator-linebreak': 0,
    'no-restricted-syntax': 0,
    'no-nested-ternary': 0,
    'no-underscore-dangle': 0,
    'import/no-cycle': 0,
    'consistent-return': 0,
    'implicit-arrow-linebreak': 0,
    'no-new-func': 0,
    'no-eval': 0,
    'space-in-parens': 0,
    'arrow-parens': 0,
    'object-curly-newline': 0,
    'no-await-in-loop': 0,
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // js 和 vue 不需要检查 import 的文件后缀
    'import/extensions': [
      'error',
      'always',
      {
        js: 'never',
        ts: 'never',
      },
    ],
    'prefer-destructuring': 0,
    // 可以 debugger
    'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
    // 不要分号
    semi: [2, 'never'],
    // 全部单引号
    quotes: [2, 'single'],
    // 对象缩写
    'object-shorthand': 0,
    // 可以使用 console
    'no-console': 0,
    // 允许使用匿名函数
    'func-names': 0,
    // 允许属性的 key 值加引号
    'quote-props': 0,
    // 允许对函数的参数赋值
    'no-param-reassign': 0,
    // 函数的参数可以不使用
    'no-unused-vars': 0,
    // 不用强制 export default
    'import/prefer-default-export': 0,
    // 不禁止箭头函数直接return对象
    'arrow-body-style': 0,
    // 允许空行
    'no-trailing-spaces': ['error', { skipBlankLines: true }],
    // 允许short circuit evaluations
    'no-unused-expressions': ['error', { allowShortCircuit: true, allowTernary: true }],
    // 最长字符
    'max-len': ['error', { code: 500 }],
    'vue/no-parsing-error': [
      2,
      {
        'invalid-first-character-of-tag-name': false,
      },
    ],
    // no-plusplus
    'no-plusplus': 0,
    'class-methods-use-this': 0,
    'global-require': 0,
    'import/no-extraneous-dependencies': 0,
    'no-fallthrough': 0,
    'no-bitwise': 0,
    'no-case-declarations': 0,
    'no-return-await': 0,
    'no-useless-escape': 0,
    eqeqeq: 0,
    'no-useless-constructor': 0,
  },
  parserOptions: {
    parser: '@typescript-eslint/parser',
  },
}
