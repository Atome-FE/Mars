<template>
  <div class="group-case-container">
    <draggable
      :list="options"
      ghost-class="ghost"
      group="case"
      @change="dragListChange"
      @start="dragging = true"
      @end="dragEnd"
    >
      <div class="case" v-for="(item, index) in getOptions" :key="item.id">
        <div class="main">
          <div class="content">
            <div class="tip" v-if="item.ui.status === 'LOADING'">
              <i class="el-icon-loading"></i>
            </div>
            <div class="tip" v-if="item.ui.status === 'SUCCESS'">
              <i class="el-icon-check"></i>
            </div>
            <div class="text">
              <span>{{ item.name }}</span>
            </div>
          </div>
          <div class="ops">
            <el-button type="primary" @click="seeCase(item.testCaseId)" size="mini" icon="el-icon-view">查看</el-button>
            <el-button @click="deleteCase(item, index)" type="info" size="mini" icon="el-icon-delete">删除</el-button>
          </div>
        </div>
        <div class="record" v-if="item.response">
          <TaskResult v-for="(item, index) in item.response" :key="index" :options="item" />
        </div>
      </div>
    </draggable>
  </div>
</template>

<script lang="tsx">
import { Component, Vue, Prop } from 'vue-property-decorator'

import { deleteTestGroupCase, updateTestGroupCasePriority } from '@/apis/test-api'

import { DataTransformUtils } from '@/lib/utils/data-transform-utils'

import { FieldModule } from '@/store/modules/field-module'
import { getTestCaseMaterialById } from '@/store/modules/test-case-material-module'

import { ITestGroupCase } from '@/interfaces/test-interface'

import { RouteConstants } from '@/constants/route-constants'

import TaskResult from './TaskResult.vue'
import Preview from './Preview.vue'
import { TaskTypeEnum } from '../../../enums/task-enum'

const VueJsonPretty = require('vue-json-pretty').default
const draggable = require('vuedraggable')

@Component({
  components: {
    VueJsonPretty,
    draggable,
    TaskResult,
    Preview,
  },
})
export default class TestGroupCaseList extends Vue {
  dragging: boolean = true

  @Prop(Array)
  options!: ITestGroupCase[]

  get getOptions() {
    return this.options.filter((item: ITestGroupCase) => {
      return !!item.ui
    })
  }

  getCase(item: ITestGroupCase) {
    const result = getTestCaseMaterialById(item.testCaseId)
    return DataTransformUtils.objPropertyToJson(result)
  }

  dragListChange(data: any) {
    if (data.added) {
      this.$emit('dragAddCase', data.added)
    }
  }

  seeCase(id: string) {
    this.$router.push({
      name: RouteConstants.MATERIALPAGE,
      params: {
        materialType: TaskTypeEnum.CASE,
        materialId: id,
      },
    })
  }

  async dragEnd() {
    for (let i = 0; i < this.options.length; i++) {
      const item = this.options[i]
      if (item.priority !== i) {
        item.priority = i
        try {
          await updateTestGroupCasePriority(item)
          FieldModule.updateTestGroupCasePriority(item)
        } catch (e) {
          this.$handleError(e)
        }
      }
    }
  }

  async deleteCase(item: ITestGroupCase, index: number) {
    try {
      await this.$confirm('确定删除吗?')
    } catch (e) {
      return
    }
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      await deleteTestGroupCase(item)
      this.$notify.success({
        title: '成功',
        message: '删除成功!',
      })
      FieldModule.deleteTestGroupCase(item)
      this.options.splice(index, 1)
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }
}
</script>

<style lang="stylus" scoped>
.group-case-container
  position relative
  margin-top 20px
  .ghost
    opacity 0.5
    background #c8ebfb
  .case
    position relative
    padding 5px
    margin-bottom 10px
    border 1px solid $border-color2
    border-radius 5px
    .record
      position relative
      background-color #DDD
      padding 5px
      margin-top 10px
      border-radius 5px
    .main
      position relative
      display flex
      align-items center
      justify-content space-between
      .content
        position relative
        display flex
        align-items center
        .tip
          margin 0 10px
      .ops
        position relative
        display flex
        align-items center
        .preview
          margin-right 20px
</style>
