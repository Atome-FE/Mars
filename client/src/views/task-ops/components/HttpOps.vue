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
        <el-input v-model="material.url" size="mini" class="a-input-item" placeholder="请输入url"></el-input>
      </div>
    </div>
    -->
    <DataTab class="data-tab" :tabs="dataTabs" @clickTab="clickTab" />
    <div class="task-content" v-if="activeDataTab === 'Body'">
      <div class="data-type">
        <el-radio class="radio-item" v-model="material.dataType" label="JSON">json</el-radio>
        <el-radio class="radio-item" v-model="material.dataType" label="FILE">文件</el-radio>
        <el-select size="mini" class="select" v-model="material.dataHandleType" placeholder="请选择数据压缩方式">
          <el-option v-for="item in getDataHandleType" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="data-content">
        <AEditor v-if="material.dataType === 'JSON'"
          editorId="httpBodyId"
          title="body参数" 
          :height="300"
          language="json" 
          v-model="material.data" 
        />
        <div v-if="material.dataType === 'FILE'">
          <el-upload
            class="upload-demo"
            :before-remove="beforeRemove"
            :on-success="handleFileSuccess"
            action="/t-api/v1/file/upload"
            :limit="1"
            :file-list="fileList"
          >
            <el-button size="mini" type="primary">点击上传</el-button>
          </el-upload>
          <span class="file-tip">data:</span>
          <span class="file-tip">{{ material.data }}</span>
        </div>
      </div>
      <AEditor editorId="httpParamsReplaceId" title="参数替换" :height="200" language="yaml" v-model="material.paramReplace" />
    </div>
    <Params v-if="activeDataTab === 'Params'" :options="params" />
    <Headers v-if="activeDataTab === 'Headers'" :options="headers" />
    <TaskResult :options="getTaskResult" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop, Mixins } from 'vue-property-decorator'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

import { saveHttpMaterial, updateHttpMaterial, deleteHttpMaterial } from '@/apis/http-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'
import { ConditionUtils } from '@/lib/utils/condition-utils'
import { ReplaceUtils } from '@/lib/utils/replace-utils'
import { mouseHandler, MouseDirection } from '@/lib/utils/mouse-handler'

import { HttpMaterialModule } from '@/store/modules/http-material-module'

import { IHttpMaterial, IHttpTab, IHttpHeader, IHttpMethod } from '@/interfaces/http-interface'
import { ITaskResult } from '@/interfaces/task-interface'
import { ISelectData } from '@/interfaces/common-interface'

import MaterialMixin from '@/mixins/material-mixin'
import DataTab from './DataTab.vue'
import Params from './Params.vue'
import Headers from './Headers.vue'
import TaskResult from './TaskResult.vue'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'

import AButtonBar from '@/components/AButtonBar.vue'
import AFormBar from '@/components/AFormBar.vue'
import AEditor from '@/components/AEditor.vue'

const qs = require('qs')

@Component({
  components: {
    MonacoEditor,
    DataTab,
    Params,
    Headers,
    TaskResult,
    AButtonBar,
    AEditor,
    AFormBar,
  },
})
export default class TestCaseAdd extends Mixins(MaterialMixin) {
  fileList: any[] = []

  getDataHandleType: ISelectData[] = []

  material: IHttpMaterial = {
    id: '',
    name: '',
    method: 'POST',
    data: '',
    url: '',
    params: '',
    headers: '',
    dataType: 'JSON',
    groupName: '',
    secondGroupName: '',
    paramReplace: '',
    dataHandleType: '',
    materialType: TaskTypeEnum.HTTP,
  }

  activeDataTab: string = 'Params'

  methods: IHttpMethod[] = []

  dataTabs: IHttpTab[] = []

  params: IHttpHeader[] = []

  headers: IHttpHeader[] = []

  updateUrlOnWatchParams = true

  updateUrlOnWatchUrl = true

  updateUrlFlag: any = null

  updateParamsFlag: any = null

  initDataHandleType() {
    this.getDataHandleType = [
      {
        label: '',
        value: '',
      },
      {
        label: 'gzip',
        value: 'gzip',
      },
    ]
  }

  handleFileSuccess(res: any, file: any) {
    this.material.data = res.data
  }

  beforeRemove(file: any, fileList: any) {
    return this.$confirm(`确定移除 ${file.name}？`)
  }

  resetForm() {
    DataTransformUtils.resetForm(this.material)
    this.material.materialType = TaskTypeEnum.HTTP
    this.params.splice(0)
    this.headers.splice(0)
  }

  @Watch('params', {
    deep: true,
    immediate: true,
  })
  paramsChange(val: IHttpHeader[]) {
    this.addKeyValueIfNeed(val, this.params)
    if (!this.updateUrlOnWatchParams) return
    this.handlerParams()

    this.updateUrlOnWatchUrl = false
    clearTimeout(this.updateUrlFlag)
    this.updateUrlFlag = setTimeout(() => {
      this.updateUrlOnWatchUrl = true
    }, 500)
  }

  @Watch('material.url')
  watchUrl(val: string) {
    if (!this.updateUrlOnWatchUrl) return
    const matched = val.match(/\?.*/g)
    if (matched) {
      const search = matched[0].substring(1)
      const arr = qs.parse(search)
      const checkedParams = Object.keys(arr).map(key => {
        return {
          checked: true,
          key: key,
          value: arr[key],
          description: '',
        }
      })
      this.params = checkedParams
    }
    this.updateUrlOnWatchParams = false
    clearTimeout(this.updateParamsFlag)
    this.updateParamsFlag = setTimeout(() => {
      this.updateUrlOnWatchParams = true
    }, 500)
  }

  @Watch('headers', {
    deep: true,
    immediate: true,
  })
  headersChange(val: IHttpHeader[]) {
    this.addKeyValueIfNeed(val, this.headers)
  }

  @Prop(Object)
  selectedMaterial!: IHttpMaterial

  @Watch('selectedMaterial', {
    immediate: true,
  })
  selectedMaterialChange(val: IHttpMaterial) {
    if (!val.id) return
    Object.keys(this.material).forEach((key: string) => {
      this.material[key] = val[key]
    })
    this.params = DataTransformUtils.jsonify(this.material.params)
    this.headers = DataTransformUtils.jsonify(this.material.headers)
  }

  addKeyValueIfNeed(val: IHttpHeader[], arr: IHttpHeader[]) {
    const result = val.every((item: IHttpHeader) => {
      const isNeed = item.key || item.value || item.description
      return !!isNeed
    })
    if (result) {
      arr.push({
        checked: true,
        key: '',
        value: '',
        description: '',
      })
    }
  }

  handlerParams() {
    const params = this.params
      .filter((item: IHttpHeader) => {
        return item.checked && item.key
      }).map(item => {
        const itemValue = item.value || ''
        const arr = itemValue.split(',').map(v => v.trim()).filter(v => v)
        if (arr.length) {
          return arr.map(value => `${item.key}=${value}`).join('&')
        }
        return `${item.key}=${item.value}`
      })
    const query = params.join('&')
    if (!query) return
    if (!this.material.url.includes('?')) {
      this.material.url += '?'
    }
    this.material.url = this.material.url.replace(/\?.*/g, `?${query}`)
  }

  initDataTabs() {
    this.dataTabs = [
      {
        name: 'Params',
        active: true,
      },
      {
        name: 'Headers',
        active: false,
      },
      {
        name: 'Body',
        active: false,
      },
    ]
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
      this.material.params = JSON.stringify(this.params)
      this.material.headers = JSON.stringify(this.headers)
      await updateHttpMaterial(this.material)
      NestedMaterialModule.updateMaterial({ ...this.material })
      this.$notify({
        title: '成功',
        message: '更新http请求成功',
        type: 'success',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  clickTab(tab: IHttpTab) {
    this.dataTabs.forEach((item: IHttpTab) => {
      item.active = false
    })
    tab.active = true
    this.activeDataTab = tab.name
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
    this.initDataTabs()
    this.initDataHandleType()
  }
}
</script>

<style lang="stylus" scoped>
*
  user-select none
.main
  position relative
.save-content
  position relative
  display flex
  align-items center
  justify-content flex-end
.data-type
  position relative
  display flex
  align-items center
  width 100%
  .radio-item
    margin-right 20px
.data-content
  position relative
  margin-top 10px
  width 100%
  .file-tip
    font-size 16px
</style>
