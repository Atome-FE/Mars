function simpleUuid(len: number, radix: number) {
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
  const uuid = []
  let i
  radix = radix || chars.length
  for (i = 0; i < len; i++) {
    uuid[i] = chars[0 | Math.random() * radix]
  }
  return uuid.join('')
}

function createRandom(len: number, rule: string) {
  let chars: any = []
  switch (rule) {
    case 'n':
      chars = '0123456789'.split('')
      break
    case 's':
      chars = 'abcdefghijklmnopqrstuvwxyz'.split('')
      break
    case 'S':
      chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('')
      break
    case 'ns':
      chars = '0123456789abcdefghijklmnopqrstuvwxyz'.split('')
      break
    case 'nS':
      chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('')
      break
    case 'sS':
      chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
      break
    case 'nsS':
      chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
      break
    default:
  }
  const uuid = []
  let i
  const radix = chars.length
  for (i = 0; i < len; i++) {
    uuid[i] = chars[0 | Math.random() * radix]
  }
  return uuid.join('')
}

export const generateRandom = (min: number, max: number, rule: string) => {
  let len = 0
  if (min === max) {
    len = min
  } else {
    len = Math.floor(Math.random() * (max - min + 1) + min)
  }
  return createRandom(len, rule)
}

export const generateNumber = (len: number) => {
  return simpleUuid(len, 10)
}
