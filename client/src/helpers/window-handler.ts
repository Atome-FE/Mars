export const locationTo = (url: string) => {
  window.open(decodeURIComponent(url), '_self')
}
