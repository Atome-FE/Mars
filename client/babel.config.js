module.exports = {
  presets: [
    [
      '@vue/app',
      {
        useBuiltIns: 'entry',
        polyfills: ['es6.promise', 'es6.symbol'],
        targets: {
          browsers: ['Android >= 4.0', 'ios >= 6'],
        },
      },
    ],
  ],
  plugins: ['@babel/plugin-proposal-optional-chaining'],
}
