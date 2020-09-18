// 这是所有语言的，但是体积会很大
/*
import hljs from 'highlight.js'
import 'highlight.js/styles/vs2015.css'
*/
// 设置单独设置语言的，体积相对较小
import hljs, { getLanguage } from 'highlight.js'
import 'highlight.js/styles/vs2015.css'

const javascript = getLanguage('javascript')
const java = getLanguage('java')
const python = getLanguage('python')
const sql = getLanguage('sql')
const shell = getLanguage('shell')
/*
import hljs from 'highlight.js/lib/highlight'
import javascript from 'highlight.js/lib/languages/javascript'
import java from 'highlight.js/lib/languages/java'
import python from 'highlight.js/lib/languages/python'
*/

hljs.registerLanguage('javascript', () => javascript)
hljs.registerLanguage('java', () => java)
hljs.registerLanguage('python', () => python)
hljs.registerLanguage('sql', () => sql)
hljs.registerLanguage('shell', () => shell)

export const hlCode = () => {
  const codeList = document.querySelectorAll('code')
  codeList.forEach(c => {
    hljs.highlightBlock(c)
  })
}

/*
export default {
  hlCode() {
    // 查询文档中所有的code标签
    const codeList = document.querySelectorAll('code')
    codeList.forEach(c => {
      hljs.highlightBlock(c)
    })
  },
}
*/