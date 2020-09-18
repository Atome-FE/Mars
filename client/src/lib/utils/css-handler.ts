export const getCSSClassStyles = (className: string) => {
  for (let i = 0; i < document.styleSheets.length; i++) {
    const rules = (document.styleSheets[i] as any).cssRules
    for (let j = 0; j < rules.length; j++) {
      if (rules[j].selectorText === className) {
        return rules[j].style
      }
    }
  }
  return undefined
}
