class Timer {
  static async sleep(t: number) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve()
      }, t)
    })
  }
}

export {
  Timer,
}
