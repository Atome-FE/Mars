<template>
  <div class="list-container">
    <div class="header">
      <div class="select" v-if="group != 'case'">
        <el-select v-model="taskType" size="mini" @change="typeSelected" placeholder="请选择类型">
          <el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <el-button class="close-btn" size="mini" @click="$emit('closeMaterialList')" type="text">隐藏</el-button>
      </div>
      <div class="search">
        <el-input size="mini" v-model="searchText" placeholder="搜索关键词"></el-input>
      </div>
    </div>
    <div class="main">
      <draggable :group="{ name: group, pull: 'clone', put: false }" ghost-class="ghost" :list="filterMaterials">
        <div
          class="item"
          @click="$emit('selectedItem', material)"
          v-for="material in filterMaterials"
          :key="material.id"
        >
          <span>{{ material.name }}</span>
        </div>
      </draggable>
    </div>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'

import { wrapData } from '@/lib/utils/material-handler'

import { HttpMaterialModule } from '@/store/modules/http-material-module'
import { SqlMaterialModule } from '@/store/modules/sql-material-module'
import { RedisMaterialModule } from '@/store/modules/redis-material-module'
import { DataMaterialModule } from '@/store/modules/data-material-module'
import { MqMaterialModule } from '@/store/modules/mq-material-module'
import { TestCaseMaterialModule } from '@/store/modules/test-case-material-module'

import { IHttpMaterial } from '@/interfaces/http-interface'
import { IMaterial, ISelectData } from '@/interfaces/common-interface'
import { ISqlMaterial } from '@/interfaces/sql-interface'
import { IRedisMaterial } from '@/interfaces/redis-interface'
import { ITaskParams, ITaskResult } from '@/interfaces/task-interface'
import { IMqMaterial } from '@/interfaces/mq-interface'
import { IDataMaterial } from '@/interfaces/data-material-interface'

import { TaskTypeEnum } from '@/enums/task-enum'
import { MongoMaterialModule } from '../../../store/modules/mongo-material-module'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
import { TreeUtils } from '../../../lib/utils/tree-utils'

const draggable = require('vuedraggable')

@Component({
  components: {
    draggable,
  },
})
export default class MaterialAddList extends Vue {
  taskType: TaskTypeEnum = TaskTypeEnum.HTTP

  types: ISelectData[] = []

  searchText = ''

  @Prop(Object)
  selectedItem!: IMaterial

  @Prop(String)
  group!: string

  materials: IMaterial[] = []

  get filterMaterials() {
    if (this.searchText) {
      return this.materials.filter(v => v.name.includes(this.searchText))
    }
    return this.materials
  }

  @Watch('taskType', {
    immediate: true,
  })
  watchTaskType(val: TaskTypeEnum) {
    if (this.group === 'case') {
      this.materials = TreeUtils.getMaterialsByMaterialType(NestedMaterialModule.allNestedMaterials, TaskTypeEnum.CASE)
    } else {
      this.materials = TreeUtils.getMaterialsByMaterialType(NestedMaterialModule.allNestedMaterials, this.taskType)
    }
  }

  get getOptions() {
    if (this.group === 'case') {
      return wrapData(TestCaseMaterialModule.testCaseMaterials)
    }
    switch (this.taskType) {
      case TaskTypeEnum.HTTP:
        return wrapData(HttpMaterialModule.httpMaterials)
      case TaskTypeEnum.SQL:
        return wrapData(SqlMaterialModule.sqlMaterials)
      case TaskTypeEnum.REDIS:
        return wrapData(RedisMaterialModule.redisMaterials)
      case TaskTypeEnum.MQ:
        return wrapData(MqMaterialModule.mqMaterials)
      case TaskTypeEnum.DATA:
        return wrapData(DataMaterialModule.dataMaterials)
      case TaskTypeEnum.MONGO:
        return wrapData(MongoMaterialModule.mongoMaterials)
      default:
        return []
    }
  }

  initTypes() {
    this.types = [
      {
        value: 'HTTP',
        label: 'http',
      },
      {
        value: 'SQL',
        label: 'sql',
      },
      {
        value: 'REDIS',
        label: 'redis',
      },
      {
        value: 'MQ',
        label: 'mq',
      },
      {
        value: 'DATA',
        label: 'data',
      },
      {
        value: 'MONGO',
        label: 'mongo',
      },
    ]
  }

  typeSelected(item: ISelectData) {
    this.$emit('typeSelected', item)
  }

  activeItem(id: string) {
    if (!this.selectedItem) return ''
    return id === this.selectedItem.id ? 'item-active' : ''
  }

  created() {
    this.initTypes()
  }
}
</script>

<style lang='stylus' scoped>
.list-container
  position relative
  display flex
  flex-direction column
  view-rect(100%, 100%)
  box-sizing border-box
  overflow hidden
  .main
    position relative
    font-size 14px
    line-height 20px
    overflow scroll
    padding 0 10px
    height 100%
    .ghost
      opacity 0.5
      background #c8ebfb
    .item-active
      background-color $bg-color2 !important
      color white !important
    .item
      position relative
      background-color $bg-color3
      font-size 14px
      padding 3px 10px
      word-wrap break-word
      word-break break-all
      color $text-color4
      margin-bottom 5px
      cursor pointer
      span
        word-wrap break-word
        word-break break-all
  .header
    position relative
    display flex
    padding 0 10px
    flex-direction column
    .select
      display flex
      position relative
      align-items center
    .close-btn
      margin-left 20px
    .btn
      margin-left 10px
    .search
      position relative
      margin 5px 0
</style>
