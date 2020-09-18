let startY: number = 0
let startX: number = 0
let moveY: number = 0
let moveX: number = 0
let x: number = 0
let y: number = 0
let canMove = false
let firstEvent = true

interface TouchOptions {
  el: Element,
  mouseStart?: () => void,
  mouseMove?: (direction: MouseDirection) => void,
  mouseEnd?: () => void,
}

enum MouseDirection {
  UP = 'UP',
  DOWN = 'DOWN',
  LEFT = 'LEFT',
  RIGHT = 'RIGHT'
}

function mouseHandler(options: TouchOptions) {
  const {
    el, mouseMove, mouseStart, mouseEnd,
  } = options
  el.addEventListener('mousedown', (e: any) => {
    canMove = true
    firstEvent = true
    startY = e.clientY
    startX = e.clientX
    if (typeof mouseStart === 'function') {
      mouseStart()
    }
  })
  if (typeof mouseMove === 'function') {
    el.addEventListener('mousemove', (e: any) => {
      if (!canMove) return
      moveY = e.clientY
      moveX = e.clientX
      x = moveX - startX
      y = moveY - startY
      if (!firstEvent) return
      if (Math.abs(x) > Math.abs(y) && x > 0 && Math.abs(x) > 100) {
        mouseMove(MouseDirection.RIGHT)
        firstEvent = false
      } else if (Math.abs(x) > Math.abs(y) && x < 0 && Math.abs(x) > 100) {
        mouseMove(MouseDirection.LEFT)
        firstEvent = false
      } else if (Math.abs(y) > Math.abs(x) && y > 0 && Math.abs(y) && Math.abs(y) > 100) {
        mouseMove(MouseDirection.DOWN)
        firstEvent = false
      } else if (Math.abs(y) > Math.abs(x) && y < 0 && Math.abs(y) && Math.abs(y) > 100) {
        // mouseMove(MouseDirection.UP)
        firstEvent = false
      }
    })
  }
  el.addEventListener('mouseup', (e: any) => {
    canMove = false
    firstEvent = false
    if (typeof mouseEnd === 'function') {
      mouseEnd()
    }
  })
}

export { mouseHandler, MouseDirection }
