<template>
  <div
    class="tree-node"
    @dragstart.stop="handleDragStart"
    @dragover.stop="handleDragOver"
    @dragend.stop="handleDragEnd"
    @drop.stop="handleDrop"
    :draggable="true"
    ref="node"
  >
    <div :class="['tree-node-name', node.ui.selected ? 'tree-node-name__selected' : '']">
      <div v-if="node.materialId" class="catalog item">
        <div class="left" @click="clickMaterialName">
          <span class="tree-node__expand-icon space-icon"></span>
          <span class="node-name">{{ node.material ? node.material.name : '' }}</span>
        </div>
        <el-popover placement="right" width="100" trigger="hover">
          <PopoverList :list="materialMoreActions" />
          <span slot="reference" class="right">
            <i class="el-icon-more more-icon"></i>
          </span>
        </el-popover>
      </div>
      <div v-else class="catalog">
        <div class="left" @click="clickCatalogName">
          <span class="tree-node__expand-icon">
            <i v-if="!node.ui.expanded" class="el-icon-caret-right arrow-icon"></i>
            <i v-else class="el-icon-caret-bottom arrow-icon"></i>
          </span>
          <span class="node-name">{{node.name}}</span>
        </div>
        <el-popover placement="right" width="100" trigger="hover">
          <PopoverList :list="catalogMoreActions" />
          <span slot="reference" class="right">
            <i class="el-icon-more more-icon"></i>
          </span>
        </el-popover>
      </div>
    </div>
    <el-collapse-transition>
      <div class="tree-node-children" v-if="node.ui.expanded">
        <TreeNode v-for="localNode in node.children" :key="localNode.id" :node="localNode" />
      </div>
    </el-collapse-transition>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator'
import { INestedMaterial, PopoverListItem } from '../../../interfaces/nested-material-interface'
import PopoverList from './PopoverList.vue'
import { PopoverListItemActionEnum } from '../../../enums/nested-material-enum'
import { NestedMaterialModule } from '../../../store/modules/nested-material-module'
import { RouteConstants } from '../../../constants/route-constants'

@Component({
  name: 'TreeNode',
  components: {
    PopoverList,
  },
})
export default class TreeNode extends Vue {
  tree: any = null

  catalogMoreActions: PopoverListItem[] = [
    {
      title: '重命名',
      label: PopoverListItemActionEnum.RENAME,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
    {
      title: '添加目录',
      label: PopoverListItemActionEnum.ADD_FOLDER,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
    {
      title: '添加资源',
      label: PopoverListItemActionEnum.ADD_MATERIAL,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
  ]

  materialMoreActions: PopoverListItem[] = [
    {
      title: '运行',
      label: PopoverListItemActionEnum.RUN,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
    {
      title: '复制',
      label: PopoverListItemActionEnum.COPY,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
    {
      title: '分享',
      label: PopoverListItemActionEnum.SHARE,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
    {
      title: '删除',
      label: PopoverListItemActionEnum.DELETE,
      action: (label: PopoverListItemActionEnum) => {
        this.handleClick(this.node, label)
      },
    },
  ]

  @Prop({ type: Object, default: () => ({}) })
  node!: INestedMaterial

  clickMaterialName() {
    NestedMaterialModule.updateMaterialSelected(this.node)
    const { id } = this.node.material!
    const { materialId } = this.$route.params
    if (id === materialId) return
    this.$router.push({
      name: RouteConstants.MATERIALPAGE,
      params: {
        materialId: id,
      },
    })
  }

  clickCatalogName() {
    NestedMaterialModule.updateMaterialSelected(this.node)
  }

  handleClick(node: INestedMaterial, label: PopoverListItemActionEnum) {
    this.tree.$emit('handleClick', node, label)
  }

  handleDragStart(event: any) {
    if (!this.tree.draggable) return
    this.tree.$emit('tree-node-drag-start', event, this)
  }

  handleDragOver(event: any) {
    if (!this.tree.draggable) return
    this.tree.$emit('tree-node-drag-over', event, this)
    event.preventDefault()
  }

  handleDrop(event: any) {
    event.preventDefault()
  }

  handleDragEnd(event: any) {
    if (!this.tree.draggable) return
    this.tree.$emit('tree-node-drag-end', event, this)
  }

  created() {
    const parent = this.$parent as any

    if (parent.isTree) {
      this.tree = parent
    } else {
      this.tree = parent.tree
    }
  }
}
</script>
<style lang="stylus" scoped>
.tree-node
  position relative
  cursor pointer
  .tree-node-name__selected
    // background-color #f5f7fa
    background-color $bg-color1
  .tree-node-name
    .catalog
      position relative
      display flex
      align-items center
      justify-content space-between
      &:hover
        // background-color #f5f7fa
        background-color $bg-color1
      .left
        position relative
        display flex
        align-items center
        flex 1
        overflow hidden
        text-overflow ellipsis
        white-space nowrap
        .node-name
          white-space nowrap
          overflow hidden
          text-overflow ellipsis
        .tree-node__expand-icon
          padding 6px
          box-sizing border-box
        .icon-margin
          margin 0 5px
    .item
      position relative
  .tree-node-children
    padding-left 18px
.arrow-icon
  font-size 12px
  color #c0c4cc
.more-icon
  color #c0c4cc
.space-icon
  &:before
    opacity 0
    content '\e791'
</style>
