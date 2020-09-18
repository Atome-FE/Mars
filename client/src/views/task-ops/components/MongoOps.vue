<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :runMaterial="runMaterial" :updateMaterial="updateMaterial"/>
    <AFormBar>
      <template slot="left">
        <el-input class="a-input-width" v-model="material.database" size="mini" placeholder="请输入数据库"></el-input>
      </template>
      <template slot="right">
        <el-input v-model="material.datasource" size="mini" placeholder="请输入数据源"></el-input>
      </template>
    </AFormBar>
    <AFormBar>
      <template slot="left">
        <el-select class="a-input-width" v-model="material.action" size="mini" placeholder="请选择动作">
          <el-option v-for="item in actions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </template>
    </AFormBar>
    <!--
    <div class="input-bar-container">
      <div class="a-select">
        <el-input v-model="material.database" size="mini" placeholder="请输入数据库"></el-input>
      </div>
      <div class="a-input">
        <el-input v-model="material.datasource" size="mini" placeholder="请输入数据源"></el-input>
      </div>
    </div>
    <div class="input-bar-container">
      <div class="a-select">
        <el-select v-model="material.action" size="mini" placeholder="请选择动作">
          <el-option v-for="item in actions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
    </div>
    -->
    <div class="task-content">
      <AEditor editorId="mongoMaterialId" title="操作脚本" :height="300" language="plaintext" v-model="material.material" />
      <AEditor editorId="mongoParamReplaceId" title="参数替换" :height="300" language="yaml" v-model="material.paramReplace" />
      <AEditor editorId="mongoSchemaId" title="模型" :height="300" language="plaintext" v-model="material.schema" />
    </div>
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MaterialMixin from '@/mixins/material-mixin'

import { saveMongoMaterial, updateMongoMaterial, deleteMongoMaterial } from '@/apis/mongo-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'

import { MongoMaterialModule } from '@/store/modules/mongo-material-module'

import { IMongoMaterial } from '@/interfaces/mongo-interface'
import { ITaskResult } from '@/interfaces/task-interface'
import { mouseHandler, MouseDirection } from '@/lib/utils/mouse-handler'

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
    AButtonBar,
    AEditor,
    AFormBar,
  },
})
export default class MongoOps extends Mixins(MaterialMixin) {
  material: IMongoMaterial = {
    id: '',
    name: '',
    action: '',
    material: '',
    datasource: '',
    database: '',
    groupName: '',
    secondGroupName: '',
    paramReplace: '',
    schema: '',
    materialType: TaskTypeEnum.MONGO,
  }

  opsAction = ''

  actions = [
    {
      label: 'find',
      value: 'find',
    },
    {
      label: 'insert',
      value: 'save',
    },
    {
      label: 'update',
      value: 'update',
    },
    {
      label: 'delete',
      value: 'remove',
    },
    {
      label: 'find one',
      value: 'findOne',
    },
  ]

  rules = {
    datasource: [{ required: true, message: '请输入数据源', trigger: 'change' }],
    database: [{ required: true, message: '请输入数据库', trigger: 'change' }],
    action: [{ required: true, message: '请输入动作', trigger: 'change' }],
    material: [{ required: true, message: '请输入操作', trigger: 'change' }],
  }

  resetForm() {
    DataTransformUtils.resetForm(this.material)
    this.material.materialType = TaskTypeEnum.MONGO
  }

  initTask() {
    this.taskResult = {
      pretty: '',
      type: '',
      origin: '',
      clipText: '',
    }
  }

  @Prop(Object)
  selectedMaterial!: IMongoMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IMongoMaterial) {
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
    try {
      const el: any = this.$refs.formdata
      el.validate(async (validate: boolean) => {
        if (!validate) return
        this.initTask()
        const param = { ...this.material }
        if (param.paramReplace) {
          ConditionUtils.taskParamReplace(param)
        }
        const res: any = await TaskRunner.runMongo(param as IMongoMaterial)
        this.taskResult = { ...res }
      })
    } catch (error) {
      this.$handleError(error)
    } finally {
      loading.close()
    }
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
      await updateMongoMaterial(this.material as IMongoMaterial)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新mongo脚本成功',
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
</style>
