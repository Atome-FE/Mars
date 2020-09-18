<template>
  <div class="condition">
    <div>
      <div class="ops">
        <el-button size="mini" @click="$emit('updateTestCaseMaterial')" type="primary" icon="el-icon-edit">更新用例</el-button>
        <el-button size="mini" @click="$emit('seeTask')" type="primary" icon="el-icon-view">查看任务</el-button>
        <el-button size="mini" @click="$emit('deleteTask')" type="info" icon="el-icon-delete">删除</el-button>
      </div>
    </div>
    <div class="item">
      <span class="title">结果扩展</span>
      <el-input
        size="mini"
        @blur="$emit('inputBlur')"
        @focus="$emit('inputFocus')"
        placeholder="不填的话运行结果是此任务的结果"
        v-model="option.extendIndexs"
      ></el-input>
    </div>
    <div class="item">
      <span class="title">参数位置</span>
      <el-input
        size="mini"
        @blur="$emit('inputBlur')"
        @focus="$emit('inputFocus')"
        placeholder="不填的话就是上一个任务的结果, 从0开始"
        v-model="option.paramIndex"
      ></el-input>
    </div>
    <div class="item">
      <span class="title">参数替换</span>
      <MonacoEditor
        :modalVisible="modalVisible"
        :height="120"
        editorId="caseParamsReplaceId"
        v-model="option.paramReplaceRule"
        language="yaml"
      />
    </div>
    <div class="item">
      <span class="title">断言</span>
      <MonacoEditor
        :modalVisible="modalVisible"
        :height="200"
        editorId="caseAssertId"
        v-model="option.assert"
        language="javascript"
      />
    </div>
    <div class="item">
      <span class="title">断言失败跳转</span>
      <el-input
        size="mini"
        @blur="$emit('inputBlur')"
        @focus="$emit('inputFocus')"
        placeholder="请输入断言失败时跳转的位置"
        v-model="option.assertJump"
      ></el-input>
    </div>
    <div class="item">
      <span class="title">断言成功跳转</span>
      <el-input
        size="mini"
        @blur="$emit('inputBlur')"
        @focus="$emit('inputFocus')"
        placeholder="请输入断言成功时跳转的位置"
        v-model="option.assertTrueJump"
      ></el-input>
    </div>
    <div class="item">
      <span class="title">延迟执行时间</span>
      <el-input
        size="mini"
        type="number"
        @blur="$emit('inputBlur')"
        @focus="$emit('inputFocus')"
        placeholder="请输入时间"
        v-model="option.delay"
      ></el-input>
    </div>
    <div class="item">
      <span class="title">执行次数</span>
      <el-input
        size="mini"
        @blur="$emit('inputBlur')"
        @focus="$emit('inputFocus')"
        placeholder="不填的话就是执行一次"
        v-model="option.loop"
      ></el-input>
    </div>
  </div>
</template>

<script lang="tsx">
import { Component, Vue, Prop } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

import { ITaskParams } from '@/interfaces/task-interface'

@Component({
  components: {
    MonacoEditor,
  },
})
export default class CaseTaskCondition extends Vue {
  @Prop(Object)
  option!: ITaskParams

  @Prop({ type: Boolean, default: false }) modalVisible!: boolean
}
</script>

<style lang="stylus" scoped>
.condition
  .ops
    position relative
    display flex
    align-items center
    justify-content flex-end
  position relative
  padding 10px
  border-radius 10px
  .more-input
    position relative
    display flex
    .row-input-item
      margin-left 20px
  .item
    position relative
    margin-top 20px
    .title
      position relative
      display flex
      margin-bottom 5px
      font-size 16px
      font-weight bold
</style>
