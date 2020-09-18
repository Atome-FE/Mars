export const wrapData = (arr: any) => {
  const obj = arr.reduce((a: any, b: any) => {
    if (b.groupName) {
      if (b.groupName in a) {
        a[b.groupName].push(b)
      } else {
        a[b.groupName] = []
        a[b.groupName].push(b)
      }
    } else {
      a['一级未分组'].push(b)
    }
    return a
  }, {
    '一级未分组': [],
  })
  const result = Object.keys(obj).map((v: string) => {
    return {
      groupName: v,
      list: obj[v],
    }
  })
  return result
}

export const wrapSecondGroup = (arr: any) => {
  const obj = arr.reduce((a: any, b: any) => {
    if (b.secondGroupName) {
      if (b.secondGroupName in a) {
        a[b.secondGroupName].push(b)
      } else {
        a[b.secondGroupName] = []
        a[b.secondGroupName].push(b)
      }
    } else {
      a['二级未分组'].push(b)
    }
    return a
  }, {
    '二级未分组': [],
  })
  const result = Object.keys(obj).map((v: string) => {
    return {
      secondGroupName: v,
      list: obj[v],
    }
  })
  return result
}
