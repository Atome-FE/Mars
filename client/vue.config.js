const path = require('path')

const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin')

const resolve = file => path.resolve(__dirname, file)
const ProxyAgent = require('https-proxy-agent')

const agent = new ProxyAgent('http://127.0.0.1:1087')

// 保存数据的接口
const target = 'http://localhost:9098'

// 执行任务的接口
// const target = 'http://localhost:3000'

module.exports = {
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: false,
  configureWebpack: {
    plugins: [
      new MonacoWebpackPlugin({
        languages: [
          'javascript',
          'typescript',
          'css',
          'html',
          'json',
          'python',
          'java',
          'groovy',
          'markdown',
          'mysql',
          'plaintext',
          'json',
        ],
      }),
    ],
  },
  chainWebpack: config => {
    config.merge({ devtool: 'source-map' })
    config.resolve.modules.add(path.resolve(__dirname, 'src'))
    const svgRule = config.module.rule('svg')
    config.resolve.alias.set('@web', resolve('src/views/web'))

    svgRule.uses.clear()

    svgRule.include
      .add(path.resolve(__dirname, 'src/assets/images'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .end()
      .use('svgo-loader')
      .loader('svgo-loader')

    config.module.rule('ts')
    config.module.rule('ts').use('ts-loader')
    config.module.rule('ts').use('babel-loader')
    config.module.rule('ts').use('cache-loader')
    config.plugin('fork-ts-checker')
  },
  css: {
    loaderOptions: {
      stylus: {
        import: [resolve('src/assets/styles/main.styl')],
      },
    },
  },
  devServer: {
    proxy: {
      '/t-api/v1': {
        target,
        changeOrigin: true,
        secure: false,
        xfwd: false,
      },
      '/t-api/v2': {
        target,
        changeOrigin: true,
        secure: false,
        xfwd: false,
      },
      '/t-api/v3': {
        target,
        changeOrigin: true,
        secure: false,
        xfwd: false,
      },
    },
  },
}
