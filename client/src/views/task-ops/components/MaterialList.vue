<template>
  <div class="list-container">
    <div class="header">
      <el-input v-model="searchText" placeholder="搜索" size="mini"></el-input>
    </div>
    <div class="main">
      <el-collapse accordion>
        <el-collapse-item
          class="collapse-item"
          v-for="item in getFirstGroupData(true)"
          :key="item.groupName"
          :title="item.groupName"
          :name="item.groupName"
        >
          <el-collapse class="second-group" accordion>
            <el-collapse-item
              class="collapse-item"
              v-for="secondList in getSecondGroupData(item.list, true)"
              :key="secondList.secondGroupName + '2'"
              :title="secondList.secondGroupName"
              :name="secondList.secondGroupName + '2'"
            >
              <div class="item" v-for="material in secondList.list" :key="material.id">
                <MaterialListItem
                  @deleteTask="$emit('deleteTask')"
                  @runTask="$emit('runTask')"
                  @selectedItem="$emit('selectedItem', material)"
                  :selectedItem="selectedItem"
                  :material="material"
                />
              </div>
            </el-collapse-item>
          </el-collapse>
          <div class="item second-group" v-for="material in getSecondGroupData(item.list, false)" :key="material.id">
            <MaterialListItem
              @deleteTask="$emit('deleteTask')"
              @runTask="$emit('runTask')"
              @selectedItem="$emit('selectedItem', material)"
              :selectedItem="selectedItem"
              :material="material"
            />
          </div>
        </el-collapse-item>
      </el-collapse>
      <div class="item" v-for="material in getFirstGroupData(false)" :key="material.id">
        <MaterialListItem
          @deleteTask="$emit('deleteTask')"
          @runTask="$emit('runTask')"
          @selectedItem="$emit('selectedItem', material)"
          :selectedItem="selectedItem"
          :material="material"
        />
      </div>
    </div>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'

import { wrapSecondGroup } from '@/lib/utils/material-handler'

import { IHttpMaterial } from '@/interfaces/http-interface'
import { IMaterial } from '@/interfaces/common-interface'

import MaterialListItem from './MaterialListItem.vue'

@Component({
  components: {
    MaterialListItem,
  },
})
export default class MaterialList extends Vue {
  searchText: string = ''

  groupMap: { [name: string]: any } = {
    first: {
      label: 'groupName',
      value: '一级未分组',
    },
    second: {
      label: 'secondGroupName',
      value: '二级未分组',
    },
  }

  activeItem(id: string) {
    if (!this.selectedItem) return ''
    return id === this.selectedItem.id ? 'item-active' : ''
  }

  getSecondGroupData(list: IMaterial[], containGroup: boolean) {
    const arr = this.getSecondGroupOriginData(list)
    return this.getViewData(containGroup, arr as any, 'second')
  }

  getViewData(containGroup: boolean, arr: IMaterial[], groupLevel: string) {
    const { label, value } = this.groupMap[groupLevel]
    if (containGroup) {
      return arr.filter((item: any) => {
        return item[label] !== value
      })
    }
    return arr.filter((item: any) => {
      return item[label] === value
    })[0].list
  }

  getSecondGroupOriginData(list: IMaterial[]) {
    return wrapSecondGroup(list)
  }

  getFirstGroupData(containGroup: boolean) {
    return this.getViewData(containGroup, this.options, 'first')
  }

  @Prop(Object)
  selectedItem!: IMaterial

  @Prop(Array)
  options!: IHttpMaterial[]
}
</script>

<style lang='stylus' scoped>
.list-container
  position relative
  display flex
  flex-direction column
  view-rect(100%, 100%)
  padding 0 5px
  box-sizing border-box
  .main
    position relative
    font-size 14px
    line-height 20px
    overflow scroll
    margin 10px 0 20px 0
    padding 0 20px
    height 100%
    .item-active
      background-color $bg-color2 !important
      color white !important
    .second-group
      margin-left 10px
    .collapse-item
      .item
        position relative
        .item-content
          position relative
          margin-top 10px
          background-color #f5f5f5
          padding 10px
          border-radius 5px
          outline none
          span
            word-wrap break-word
  .header
    position relative
    display flex
    padding 0 20px
    .btn
      margin-left 10px
</style>
