import Vue from 'vue'

declare global {
  interface Window {
    vue: Vue
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    $cookie: any
    $copyText: any
    $handleError: (err: any) => void
  }
  interface VueConstructor<V extends Vue> {
    $cookie: any
    $handleError: (err: any) => void
  }
}
