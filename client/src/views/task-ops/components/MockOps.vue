<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :runMaterial="runMaterial" :updateMaterial="updateMaterial"/>
    <AFormBar>
      <template slot="left">
        <el-select class="a-input-width" v-model="material.method" size="mini" placeholder="请选择方法">
          <el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </template>
      <template slot="right">
        <el-input v-model="material.url" size="mini" placeholder="请输入url"></el-input>
      </template>
    </AFormBar>
    <!--
    <div class="input-bar-container">
      <div class="a-select">
        <el-select v-model="material.method" size="mini" placeholder="请选择方法">
          <el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="a-input">
        <el-input v-model="material.url" size="mini" class="url-input" placeholder="请输入url"></el-input>
      </div>
    </div>
    -->
    <div class="task-content">
      <AEditor editorId="mockMaterialId" title="Mock数据" :height="200" language="json" v-model="material.material" />
    </div>
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

import { saveMockMaterial, updateMockMaterial, deleteMockMaterial } from '@/apis/mock-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { mouseHandler, MouseDirection } from '@/lib/utils/mouse-handler'

import { IMockMaterial } from '@/interfaces/mock-interface'
import { IHttpMethod } from '@/interfaces/http-interface'
import { ITaskResult } from '@/interfaces/task-interface'

import MaterialMixin from '@/mixins/material-mixin'
import TaskResult from './TaskResult.vue'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
import { TaskRunner } from '../../../service/task-runner'
import AButtonBar from '@/components/AButtonBar.vue'
import AEditor from '@/components/AEditor.vue'
import AFormBar from '@/components/AFormBar.vue'

@Component({
  components: {
    MonacoEditor,
    TaskResult,
    AButtonBar,
    AEditor,
    AFormBar,
  },
})
export default class MockOps extends Mixins(MaterialMixin) {
  material: IMockMaterial = {
    id: '',
    name: '',
    method: 'POST',
    url: '',
    material: '',
    materialType: TaskTypeEnum.MOCK,
  }

  methods: IHttpMethod[] = []

  @Prop(Object)
  selectedMaterial!: IMockMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IMockMaterial) {
    if (!val.id) return
    Object.keys(this.material).forEach((key: string) => {
      this.material[key] = val[key]
    })
  }

  async runMaterial() {
    const loading = this.$loading({ text: '运行中' })
    const param = { ...this.material }
    try {
      ReplaceUtils.doReplace(param)
    } catch (e) {
      this.$notify.warning({
        title: '警告',
        message: e.message,
      })
    }
    const res: ITaskResult = await TaskRunner.runMock(param)
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
      await updateMockMaterial(this.material)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新mock成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  initMethods() {
    this.methods = [
      {
        value: 'POST',
        label: 'POST',
      },
      {
        value: 'GET',
        label: 'GET',
      },
      {
        value: 'PUT',
        label: 'PUT',
      },
      {
        value: 'DELETE',
        label: 'DELETE',
      },
    ]
  }

  created() {
    this.initMethods()
  }
}
</script>

<style lang="stylus" scoped>
*
  user-select none
</style>
