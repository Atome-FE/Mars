<template>
  <div class="monaco-base-contarner" :style="{ height: height ? `${height}px` : '400px' }">
    <div :id="editorId" class="monaco-base-main"></div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch, Model } from 'vue-property-decorator'
import * as monaco from 'monaco-editor'
import 'monaco-editor/esm/vs/basic-languages/java/java.contribution'
import theme from './theme'

@Component
export default class MonacoEditor extends Vue {
  @Prop({ type: Boolean }) readonly isPostPage!: boolean

  @Prop({ type: String }) readonly language!: string

  @Prop({ type: String }) readonly editorId!: string

  @Prop({ type: Number }) readonly height!: number

  @Prop({ type: Boolean, default: false }) readonly modalVisible!: boolean

  @Model('change', { type: String }) readonly value!: string

  editor: any = null

  prevLineCount: number = -1

  initView() {
    if (this.editor) {
      this.editor.dispose()
    }
    this.editor = monaco.editor.create(document.getElementById(this.editorId) as any, {
      language: this.language,
      value: this.value,
      fontSize: 15,
      lineNumbers: 'off',
      theme: 'vs-dark',
      minimap: {
        enabled: false,
      },
      wordWrap: 'on',
      cursorWidth: 2,
      cursorSmoothCaretAnimation: true,
      cursorBlinking: 'smooth',
      colorDecorators: true,
      folding: false,
      highlightActiveIndentGuide: false,
      renderIndentGuides: false,
      renderLineHighlight: 'none',
      tabSize: 2,
      detectIndentation: false,
      scrollbar: {
        vertical: 'auto',
        horizontal: 'hidden',
        verticalScrollbarSize: 4,
        alwaysConsumeMouseWheel: false,
      },
      lineHeight: 20,
      scrollBeyondLastLine: false,
      wordBasedSuggestions: false,
      snippetSuggestions: 'none',
      lineDecorationsWidth: 0,
      occurrencesHighlight: false,
      automaticLayout: true,
      fontFamily: 'Menlo, Monaco, "Courier New", monospace',
    })

    setTimeout(this.setEditorHeight, 0)

    this.editor.onDidChangeModelContent(() => {
      setTimeout(this.setEditorHeight, 0)
      const value = this.editor.getValue()
      if (this.value !== value) {
        this.$emit('change', value)
      }
    })
    this.editor.onKeyDown(() => {
      this.$emit('keydown')
    })
  }

  activated() {
    this.initView()
  }

  mounted() {
    this.initView()
  }

  setEditorHeight() {
    const lines = document.querySelectorAll('.view-line') as any
    if (lines) {
      if (lines.length === 1 && !lines[0].innerText.trim()) {
        lines[0].classList.add('input-holder')
      } else if (lines[0].classList.contains('input-holder')) {
        lines[0].classList.remove('input-holder')
      }
    }
  }

  @Watch('value')
  onValueChanged(newValue: any) {
    if (this.editor && newValue !== this.editor.getValue()) {
      this.editor.setValue(newValue)
    }
  }

  @Watch('modalVisible')
  watchModalVisible(newValue: any) {
    if (newValue) {
      this.initView()
    }
  }
}
</script>

<style lang="stylus" scoped>
.monaco-base-contarner
  width 100%
  margin 0 auto
  border 1px solid $border-color1
  padding 10px
  box-sizing border-box
  border-radius 5px
  .monaco-base-main
    height 100%
    background-color red
/deep/.context-view .monaco-scrollable-element
  box-shadow 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06) !important
  border-radius 4px
/deep/ .monaco-menu .monaco-action-bar.vertical .action-item
  border none
/deep/ .action-menu-item
  color #718096 !important
  &:hover
    color #744210 !important
    background #fffff0 !important
/deep/ .decorationsOverviewRuler
  display none !important
/deep/ .monaco-menu .monaco-action-bar.vertical .action-label.separator
  border-bottom-color #e2e8f0 !important
/deep/ .input-holder
  &:before
    content '请输入...'
    color rgba(208, 211, 217, 0.6)
/deep/ .monaco-editor
  .scrollbar
    .slider
      background #eee
  .scroll-decoration
    box-shadow #efefef 0 2px 2px -2px inset
/deep/ .view-lines
  background-color $bg-color3
</style>
