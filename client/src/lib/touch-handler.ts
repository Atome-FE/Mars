let startY: number = 0
let startX: number = 0
let moveY: number = 0
let moveX: number = 0
let x: number = 0
let y: number = 0

interface TouchOptions {
  el: Element,
  touchStart?: () => void,
  touchMove?: (direction: number) => void,
  touchEnd?: () => void,
}

enum TouchDirection {
  UP,
  DOWN,
  LEFT,
  RIGHT
}

function touchHandler(options: TouchOptions) {
  const {
    el, touchMove, touchStart, touchEnd,
  } = options
  el.addEventListener('touchstart', (e: any) => {
    startY = e.changedTouches[0].pageY
    startX = e.changedTouches[0].pageX
    if (typeof touchStart === 'function') {
      touchStart()
    }
  })
  if (typeof touchMove === 'function') {
    el.addEventListener('touchmove', (e: any) => {
      moveY = e.changedTouches[0].pageY
      moveX = e.changedTouches[0].pageX
      x = moveX - startX
      y = moveY - startY
      if (Math.abs(x) > Math.abs(y) && x > 0) {
        touchMove(TouchDirection.RIGHT)
      } else if (Math.abs(x) > Math.abs(y) && x < 0) {
        touchMove(TouchDirection.LEFT)
      } else if (Math.abs(y) > Math.abs(x) && y > 0) {
        touchMove(TouchDirection.DOWN)
      } else if (Math.abs(y) > Math.abs(x) && y < 0) {
        touchMove(TouchDirection.UP)
      }
    })
  }
  el.addEventListener('touchend', (e: any) => {
    if (typeof touchEnd === 'function') {
      touchEnd()
    }
  })
}

export { touchHandler }
