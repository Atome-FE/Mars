(function (window, document) {
  function resize() {
    let ww = window.innerWidth
    if (ww > window.screen.width) {
      window.requestAnimationFrame(resize)
    } else {
      if (ww > 720) {
        ww = 720
        document.documentElement!.style.fontSize = `${ww * 50 / 720}px`
      } else {
        document.documentElement!.style.fontSize = `${ww * 100 / 720}px`
      }
      document.body.style.opacity = '1'
    }
  }

  if (document.readyState !== 'loading') {
    resize()
  } else {
    document.addEventListener('DOMContentLoaded', resize)
  }

  window.addEventListener('resize', resize)
}(window, document))
