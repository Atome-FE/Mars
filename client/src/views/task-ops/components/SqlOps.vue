<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :runMaterial="runMaterial" :runSqlCopyMaterial="runSqlCopyMaterial" :updateMaterial="updateMaterial"/>
    <AFormBar>
      <template slot="left">
        <el-input v-model="material.datasource" size="mini" class="a-input-width" placeholder="请输入数据源"></el-input>
      </template>
      <template slot="right">
        <el-input v-model="material.database" size="mini" placeholder="请输入数据库"></el-input>
      </template>
    </AFormBar>
    <!--
    <div class="input-bar-container">
      <div class="a-select">
        <el-input v-model="material.datasource" size="mini" class="name-input" placeholder="请输入数据源"></el-input>
      </div>
      <div class="a-input">
        <el-input v-model="material.database" size="mini" class="name-input" placeholder="请输入数据库"></el-input>
      </div>
    </div>
    -->
    <div class="task-content">
      <AEditor editorId="sqlMaterialId" title="执行语句" :height="300" language="mysql" v-model="material.material" />
      <AEditor editorId="sqlParamsReplaceId" title="参数替换" :height="200" language="yaml" v-model="material.paramReplace" />
    </div>
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

import { saveSqlMaterial, updateSqlMaterial, deleteSqlMaterial } from '@/apis/sql-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { mouseHandler, MouseDirection } from '@/lib/utils/mouse-handler'

import { SqlMaterialModule } from '@/store/modules/sql-material-module'

import { ISqlMaterial } from '@/interfaces/sql-interface'
import { ITaskResult } from '@/interfaces/task-interface'

import Clipboard from 'clipboard'
import MaterialMixin from '@/mixins/material-mixin'
import TaskResult from './TaskResult.vue'
import AButtonBar from '@/components/AButtonBar.vue'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
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
export default class SqlOps extends Mixins(MaterialMixin) {
  material: ISqlMaterial = {
    id: '',
    name: '',
    material: '',
    datasource: '',
    database: '',
    groupName: '',
    secondGroupName: '',
    paramReplace: '',
    materialType: TaskTypeEnum.SQL,
  }

  resetForm() {
    DataTransformUtils.resetForm(this.material)
    this.material.materialType = TaskTypeEnum.SQL
  }

  @Prop(Object)
  selectedMaterial!: ISqlMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: ISqlMaterial) {
    if (!val.id) return
    DataTransformUtils.copyProperties(this.material, val)
  }

  async doCopy(message: string) {
    this.$copyText(message).then(
      () => {
        this.$notify({
          title: '复制sql执行结果成功',
          message: '已复制到剪贴板',
          type: 'success',
        })
      },
      () => {
        this.$notify.error({
          title: '错误',
          message: '复制sql执行结果失败',
        })
      },
    )
  }

  async runSqlCopyMaterial() {
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
    const res: ITaskResult = await TaskRunner.runSqlCopy(param as ISqlMaterial)
    this.doCopy(res.origin)
    loading.close()
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
    const res: ITaskResult = await TaskRunner.runSql(param as ISqlMaterial)
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
      await updateSqlMaterial(this.material as ISqlMaterial)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新sql脚本成功',
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
