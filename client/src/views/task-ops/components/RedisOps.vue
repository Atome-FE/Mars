<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :runMaterial="runMaterial" :updateMaterial="updateMaterial"/>
    <AFormBar>
      <template slot="left">
        <el-input v-model="material.datasource" size="mini" class="a-input-width" placeholder="数据源"></el-input>
      </template>
      <template slot="right">
        <el-input v-model="material.database" size="mini" placeholder="数据库"></el-input>
      </template>
    </AFormBar>
    <AFormBar>
      <template slot="left">
        <el-select v-model="material.action" size="mini" class="a-input-width" placeholder="请选择动作">
          <el-option v-for="item in actions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </template>
      <template slot="right">
        <el-input v-model="material.key" size="mini" placeholder="请输入键"></el-input>
      </template>
    </AFormBar>
    <!--
    <div class="input-bar-container">
      <div class="a-select">
        <el-input v-model="material.datasource" size="mini" class="name-input" placeholder="数据源"></el-input>
      </div>
      <div class="a-input">
        <el-input v-model="material.database" size="mini" class="name-input" placeholder="数据库"></el-input>
      </div>
    </div>
    <div class="input-bar-container">
      <div class="a-select">
        <el-select v-model="material.action" size="mini" class="action-select" placeholder="请选择动作">
          <el-option v-for="item in actions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="a-input">
        <el-input v-model="material.key" size="mini" class="name-input" placeholder="请输入键"></el-input>
      </div>
    </div>
    -->
    <div class="task-content">
      <AEditor editorId="redisParamsReplaceId" title="参数替换" :height="200" language="yaml" v-model="material.paramReplace" />
    </div>
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'
import MaterialMixin from '@/mixins/material-mixin'

import { saveRedisMaterial, updateRedisMaterial, deleteRedisMaterial } from '@/apis/redis-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { mouseHandler, MouseDirection } from '@/lib/utils/mouse-handler'

import { RedisMaterialModule } from '@/store/modules/redis-material-module'

import { IRedisMaterial, IRedisAction } from '@/interfaces/redis-interface'
import { ITaskResult } from '@/interfaces/task-interface'

import TaskResult from './TaskResult.vue'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
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
export default class RedisOps extends Mixins(MaterialMixin) {
  material: IRedisMaterial = {
    id: '',
    name: '',
    action: '',
    key: '',
    database: 0,
    datasource: '',
    groupName: '',
    secondGroupName: '',
    paramReplace: '',
    materialType: TaskTypeEnum.REDIS,
  }

  actions: IRedisAction[] = []

  taskResult: ITaskResult = {
    pretty: '',
    type: '',
    origin: '',
    clipText: '',
  }

  resetForm() {
    DataTransformUtils.resetForm(this.material)
    this.material.materialType = TaskTypeEnum.REDIS
  }

  @Prop(Object)
  selectedMaterial!: IRedisMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IRedisMaterial) {
    if (!val.id) return
    DataTransformUtils.copyProperties(this.material, val)
  }

  initActions() {
    this.actions = [
      {
        value: 'get',
        label: 'get',
      },
      {
        value: 'del',
        label: 'del',
      },
    ]
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
    const res: ITaskResult = await TaskRunner.runRedis(param as IRedisMaterial)
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
      await updateRedisMaterial(this.material as IRedisMaterial)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新redis脚本成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  created() {
    this.initActions()
  }
}
</script>

<style lang="stylus" scoped>
*
  user-select none
.main
  position relative
</style>
