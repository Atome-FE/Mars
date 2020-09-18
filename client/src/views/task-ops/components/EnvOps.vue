<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :updateMaterial="updateMaterial"/>
    <div class="task-content">
      <AEditor editorId="envGlobalVariableId" title="globalVariable" :height="150" language="json" v-model="material.globalVariable" />
      <AEditor editorId="envHttpId" title="http" :height="150" language="json" v-model="material.http" />
      <AEditor editorId="envSqlId" title="sql" :height="150" language="json" v-model="material.sql" />
      <AEditor editorId="envRedisId" title="redis" :height="150" language="json" v-model="material.redis" />
      <AEditor editorId="envMqId" title="rabbitmq" :height="150" language="json" v-model="material.mq" />
      <AEditor editorId="envMongoId" title="mongodb" :height="150" language="json" v-model="material.mongo" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MaterialMixin from '@/mixins/material-mixin'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

import { saveEnv, updateEnv, deleteEnv } from '@/apis/env-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'

import { EnvModule } from '@/store/modules/env-module'

import { IEnv } from '@/interfaces/env-interface'
import { TaskTypeEnum } from '@/enums/task-enum'
import { ShareMaterialModule, getShareEnvConfigById } from '../../../store/modules/share-material-module'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
import AButtonBar from '@/components/AButtonBar.vue'
import AEditor from '@/components/AEditor.vue'

@Component({
  components: { MonacoEditor, AButtonBar, AEditor },
})
export default class SqlOps extends Mixins(MaterialMixin) {
  material: IEnv = {
    id: '',
    name: '',
    http: '',
    sql: '',
    redis: '',
    mq: '',
    mongo: '',
    describe: '',
    globalVariable: '',
    materialType: TaskTypeEnum.ENV,
  }

  @Prop(Object)
  selectedMaterial!: IEnv

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IEnv) {
    if (!val.id) return
    DataTransformUtils.copyProperties(this.material, val)
  }

  checkIsShareConfig(id: string) {
    const env = getShareEnvConfigById(id)
    if (env) {
      this.$notify({
        title: '失败',
        message: '无法修改别人分享的配置',
        type: 'warning',
      })
      throw new Error('分享')
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
    this.checkIsShareConfig(this.material.id)
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      await updateEnv(this.material as IEnv)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新环境配置成功',
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
