class DataTransformUtils {
  static objPropertyToJson(data: any) {
    Object.keys(data).forEach((key: string) => {
      data[key] = this.jsonify(data[key])
    })
    return data
  }

  static handleHeaders(data: string) {
    return this.jsonify(data)
      .filter((v: any) => {
        return v.checked && v.key && v.value
      })
      .reduce((a: any, b: any) => {
        a[b.key] = b.value
        return a
      }, {})
  }

  static notObData(data: any) {
    try {
      return JSON.parse(JSON.stringify(data))
    } catch (e) {
      return data
    }
  }

  static jsonify(data: any) {
    try {
      return JSON.parse(data)
    } catch (e) {
      return data
    }
  }

  static stringIfObj(data: any) {
    if (data instanceof Object) {
      return JSON.stringify(data)
    }
    return data
  }

  static stringify(data: any) {
    try {
      return JSON.stringify(data)
    } catch (e) {
      return data
    }
  }

  static toPrettyData(data: any, taskId?: string) {
    if (data instanceof Object) {
      return {
        taskId,
        origin: data,
        clipText: JSON.stringify(data),
        pretty: data,
        type: 'PRETTY',
      }
    }
    return {
      taskId,
      origin: data,
      clipText: data,
      type: 'ORIGIN',
    }
  }

  static resetForm(data: any) {
    Object.keys(data).forEach((key: any) => {
      data[key] = ''
    })
  }

  static copyProperties(target: any, source: any) {
    // TODO 这样换顺序之后，可能会有问题
    /*
    Object.keys(source).forEach((key: string) => {
      target[key] = source[key]
    })
    */
    Object.keys(target).forEach((key: string) => {
      target[key] = source[key]
    })
  }
}

export { DataTransformUtils }
