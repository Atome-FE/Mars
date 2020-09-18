### 只有一个事件名
```
@Tracking('some-event')
demo() {
}
```

### 事件名需要加一些简单的参数
```
@Tracking({
  action: 'some-event',
  extra: {
    mobile_no: '131xxxxxxx'
  }
})
demo() {
}
```

### 较复杂的埋点，根据方法内部逻辑来埋
```
@Tracking
demo(p: any[]) {
  // other code
  p.push({
    action: 'some-event',
    extra: {
      login: true
    }
  })
  // other code
}
```

### 简单埋点可以实现与业务逻辑解耦，但是复杂埋点无法做到。