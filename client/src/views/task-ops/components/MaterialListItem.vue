<template>
  <el-popover placement="right" trigger="click">
    <div class="item-popup">
      <el-button size="mini" icon="el-icon-delete" type="danger" @click="$emit('deleteTask')" class="button" circle></el-button>
      <el-button
        v-if="runnerble"
        size="mini"
        type="primary"
        @click="$emit('runTask')"
        class="button"
        icon="el-icon-caret-right"
        circle
      ></el-button>
    </div>
    <div
      :class="['item-content', activeItem(material.id)]"
      class="materil-item"
      @click="$emit('selectedItem', material)"
      slot="reference"
    >
      <span>{{ material.name }}</span>
      <span @click.stop="onShare(material)">
        <i :class="[`el-icon-star-${isShare ? 'on' : 'off'}`, 'materil-item-share']"></i>
      </span>
    </div>
  </el-popover>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'
import { IMaterial } from '@/interfaces/common-interface'
import { ShareMaterialModule, getShareMaterialByMaterialId } from '@/store/modules/share-material-module'
import message from '@/locale'
import { IShareMaterial } from '@/interfaces/share-interface'
import { TestCaseMaterialModule } from '../../../store/modules/test-case-material-module'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { UserModule } from '../../../store/modules/user-module'

@Component
export default class MaterialListItem extends Vue {
  @Prop(Object)
  selectedItem!: IMaterial

  @Prop(Object)
  material!: IMaterial

  runnerble = true

  shareMaterialId = ''

  get isShare() {
    return ShareMaterialModule.allShareMaterialIds.includes(this.material.id)
  }

  async onShare(item: any) {
    const params: IShareMaterial = {
      type: item.materialType,
      sharedUserIds: null,
      materialId: item.id,
    }
    if (!this.isShare) {
      ShareMaterialModule.add(params)
    } else {
      const shareMaterial = getShareMaterialByMaterialId(this.material.id)
      if (shareMaterial.type === TaskTypeEnum.ENV && shareMaterial.userId !== UserModule.userId) {
        this.$notify.warning({
          title: '警告',
          message: '无法操作别人的分享',
        })
        return
      }
      params.id = shareMaterial.id
      ShareMaterialModule.del(params)
    }
    this.$message.success('suceess')
  }

  activeItem(id: string) {
    if (!this.selectedItem) return ''
    return id === this.selectedItem.id ? 'item-active' : ''
  }

  mounted() {
    if (this.$route.meta && this.$route.meta.noRunnerble) {
      this.runnerble = false
    }
  }
}
</script>

<style lang='stylus' scoped>
.item-active
  background-color $bg-color2 !important
  color white !important
.item-content
  position relative
  margin-top 10px
  background-color #f5f5f5
  padding 10px
  border-radius 5px
  outline none
  span
    word-wrap break-word
.materil-item
  display flex
  justify-content space-between
.materil-item-share
  color #F8BF40
  font-size 18px
</style>
