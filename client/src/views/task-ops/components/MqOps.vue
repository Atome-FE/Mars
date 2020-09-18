<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :runMaterial="runMaterial" :updateMaterial="updateMaterial"/>
    <AFormBar>
      <template slot="left">
        <el-input class="a-input-width" size="mini" v-model="material.exchange" placeholder="请输入exchange"></el-input>
      </template>
      <template slot="right">
        <el-input size="mini" v-model="material.routingKey" placeholder="请输入routingkey"></el-input>
      </template>
    </AFormBar>
    <!--
    <div class="input-bar-container">
      <div class="a-select">
        <el-input size="mini" v-model="material.exchange" placeholder="请输入exchange"></el-input>
      </div>
      <div class="a-input">
        <el-input size="mini" v-model="material.routingKey" placeholder="请输入routingkey"></el-input>
      </div>
    </div>
    -->
    <div class="task-content">
      <AEditor editorId="mqContentId" title="数据" :height="300" language="json" v-model="material.content" />
      <AEditor editorId="mqParamsReplaceId" title="参数替换" :height="200" language="yaml" v-model="material.paramReplace" />
    </div>
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'
import MaterialMixin from '@/mixins/material-mixin'

import { saveMqMaterial, updateMqMaterial, deleteMqMaterial } from '@/apis/mq-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { mouseHandler, MouseDirection } from '@/lib/utils/mouse-handler'

import { MqMaterialModule } from '@/store/modules/mq-material-module'

import { IMqMaterial } from '@/interfaces/mq-interface'
import { ITaskResult } from '@/interfaces/task-interface'

import TaskResult from './TaskResult.vue'
import { TaskTypeEnum } from '../../../enums/task-enum'
import AButtonBar from '@/components/AButtonBar.vue'
import AEditor from '@/components/AEditor.vue'
import { TaskRunner } from '../../../service/task-runner'
import AFormBar from '@/components/AFormBar.vue'

@Component({
  components: {
    TaskResult,
    MonacoEditor,
    AButtonBar,
    AEditor,
    AFormBar,
  },
})
export default class MqOps extends Mixins(MaterialMixin) {
  material: IMqMaterial = {
    id: '',
    name: '',
    exchange: '',
    routingKey: '',
    content: '',
    groupName: '',
    secondGroupName: '',
    paramReplace: '',
    materialType: TaskTypeEnum.MQ,
  }

  resetForm() {
    DataTransformUtils.resetForm(this.material)
    this.material.materialType = TaskTypeEnum.MQ
  }

  @Prop(Object)
  selectedMaterial!: IMqMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IMqMaterial) {
    if (!val.id) return
    DataTransformUtils.copyProperties(this.material, val)
  }

  async runMaterial() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    const param = { ...this.material }
    try {
      ReplaceUtils.doReplace(param)
    } catch (e) {
      this.$notify.warning({
        title: '警告',
        message: e.message,
      })
    }
    const res: ITaskResult = await TaskRunner.runMq(param as IMqMaterial)
    this.taskResult = { ...res }
    loading.close()
  }

  async updateMaterial() {
    if (!this.material.id) {
      this.$notify.warning({
        title: '警告',
        message: '创建之后才能更新',
      })
      return
    }
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      await updateMqMaterial(this.material as IMqMaterial)
      MqMaterialModule.updateMqMaterial({ ...this.material } as IMqMaterial)
      this.$notify({
        title: '成功',
        message: '更新mq脚本成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }
}
</script>

<style lang="stylus" scoped>
*
  user-select none
</style>
