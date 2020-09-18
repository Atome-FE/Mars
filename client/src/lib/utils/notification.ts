export class TNotification {
  static requestPermission() {
    Notification.requestPermission((status: string) => {
      console.log(status)
    })
  }

  static send(msg: string, options: any) {
    const n = new Notification(msg, options)
    n.onclick = (e) => {
      window.focus()
    }
  }
}
