<template>
  <div class="material-main">
    <AButtonBar v-model="material.name" :updateMaterial="updateMaterial"/>
    <div class="task-content">
      <AEditor editorId="dataMaterialId" title="data" :height="200" language="json" v-model="material.data" />
      <AEditor editorId="dataDescribeId" title="参数替换" :height="100" language="plaintext" v-model="material.describe" />
    </div>
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MaterialMixin from '@/mixins/material-mixin'

import { updateDataMaterial } from '@/apis/data-material-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'

import { IDataMaterial } from '@/interfaces/data-material-interface'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { ITaskResult } from '@/interfaces/task-interface'
import TaskResult from './TaskResult.vue'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
import AButtonBar from '@/components/AButtonBar.vue'
import AEditor from '@/components/AEditor.vue'

@Component({
  components: {
    TaskResult,
    AButtonBar,
    AEditor,
  },
})
export default class DataOps extends Mixins(MaterialMixin) {
  material: IDataMaterial = {
    id: '',
    name: '',
    data: '',
    describe: '',
    groupName: '',
    secondGroupName: '',
  }

  @Prop(Object)
  selectedMaterial!: IDataMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IDataMaterial) {
    if (!val.id) return
    DataTransformUtils.copyProperties(this.material, val)
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
      await updateDataMaterial(this.material as IDataMaterial)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新数据成功',
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
