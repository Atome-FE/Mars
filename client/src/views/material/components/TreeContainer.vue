<template>
  <div class="tree-container">
    <div class="add-catalog" @click="openCatalogAddModal">
      <i class="el-icon-circle-plus"></i>
    </div>
    <div class="tree-content">
      <TreeNode v-for="node in list" :key="node.id" :node="node" />
    </div>
    <el-dialog title="修改名称" :visible.sync="dialog.RENAME" width="30%">
      <el-input @keyup.enter.native="handleCatalogRename" ref="RENAME" v-model="catalogRename" size="small"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialog.RENAME = false">取 消</el-button>
        <el-button size="small" type="primary" @click="handleCatalogRename">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="添加目录" :visible.sync="dialog.ADD_FOLDER" width="30%">
      <el-input @keyup.enter.native="handleCatalogAdd" ref="ADD_FOLDER" v-model="catalogName" size="small"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialog.ADD_FOLDER = false">取 消</el-button>
        <el-button size="small" type="primary" @click="handleCatalogAdd">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="添加任务" :visible.sync="dialog.ADD_MATERIAL" width="30%">
      <el-input @keyup.enter.native="handleMaterialAdd" ref="ADD_MATERIAL" v-model="materialName" size="small"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialog.ADD_MATERIAL = false">取 消</el-button>
        <el-button size="small" type="primary" @click="handleMaterialAdd">确 定</el-button>
      </span>
    </el-dialog>

    <div v-show="dragState.showDropIndicator" class="el-tree__drop-indicator" ref="dropIndicator"></div>
  </div>
</template>
<script lang="ts">
import { Vue, Component } from 'vue-property-decorator'
import { NestedMaterialModule } from '@/store/modules/nested-material-module'
import { updateNestedMaterials, saveNestedMaterials, deleteNestedMaterials } from '@/apis/nested-material'
import { saveRedisMaterial, deleteRedisMaterial } from '@/apis/redis-api'
import { saveMongoMaterial, deleteMongoMaterial } from '@/apis/mongo-api'
import { saveMqMaterial, deleteMqMaterial } from '@/apis/mq-api'
import { saveDataMaterial, deleteDataMaterial } from '@/apis/data-material-api'
import { saveEnv, deleteEnv } from '@/apis/env-api'
import TreeNode from './TreeNode.vue'
import { INestedMaterial } from '../../../interfaces/nested-material-interface'
import { PopoverListItemActionEnum, NestedMaterialType, DropTypeEnum } from '../../../enums/nested-material-enum'
import { TreeUtils } from '../../../lib/utils/tree-utils'
import { saveHttpMaterial, deleteHttpMaterial } from '../../../apis/http-api'
import { IHttpMaterial } from '../../../interfaces/http-interface'
import { TaskTypeEnum } from '../../../enums/task-enum'
import { IMaterial } from '../../../interfaces/common-interface'
import { ReplaceUtils } from '../../../lib/utils/replace-utils'
import { ITaskResult } from '../../../interfaces/task-interface'
import { TaskRunner } from '../../../service/task-runner'
import { saveSqlMaterial, deleteSqlMaterial } from '../../../apis/sql-api'
import { ISqlMaterial } from '../../../interfaces/sql-interface'
import { IRedisMaterial } from '../../../interfaces/redis-interface'
import { IMongoMaterial } from '../../../interfaces/mongo-interface'
import { IMqMaterial } from '../../../interfaces/mq-interface'
import { IDataMaterial } from '../../../interfaces/data-material-interface'
import { IEnv } from '../../../interfaces/env-interface'
import { ITestCase } from '../../../interfaces/test-case-interface'
import { saveTestCaseMaterial, deleteTestCaseMaterial } from '../../../apis/test-case-api'
import { IShareMaterial } from '../../../interfaces/share-interface'
import { getShareMaterialByMaterialId, ShareMaterialModule } from '../../../store/modules/share-material-module'
import { UserModule } from '../../../store/modules/user-module'
import { RouteConstants } from '../../../constants/route-constants'
import { IMockMaterial } from '../../../interfaces/mock-interface'
import { saveMockMaterial, deleteMockMaterial } from '../../../apis/mock-api'

const { addClass, removeClass } = require('element-ui/src/utils/dom')

@Component({
  components: {
    TreeNode,
  },
})
export default class TreeContainer extends Vue {
  dialog = {
    RENAME: false,
    DELETE: false,
    COPY: false,
    RUN: false,
    ADD_FOLDER: false,
    ADD_MATERIAL: false,
    SHARE: false,
  }

  addTopCatalog = false

  isTree = true

  catalogName = ''

  materialName = ''

  catalogRename = ''

  selectedNestedMaterial: INestedMaterial = {
    name: '',
    parent: null,
    children: [],
  }

  draggable = true

  dragState: any = {
    showDropIndicator: false,
    draggingNode: null,
    dropNode: null,
    allowDrop: true,
  }

  initTreeNodeDragEvent() {
    const { dragState } = this
    this.$on('tree-node-drag-start', (event: any, treeNode: any) => {
      event.dataTransfer.effectAllowed = 'move'
      try {
        event.dataTransfer.setData('text/plain', '')
      } catch (e) {
        console.log(e)
      }
      dragState.draggingNode = treeNode
      this.$emit('node-drag-start', treeNode.node, event)
    })

    this.$on('tree-node-drag-over', (event: any, treeNode: any) => {
      const dropNode = TreeUtils.findNearestComponent(event.target, 'TreeNode')
      const oldDropNode = dragState.dropNode
      if (oldDropNode && oldDropNode !== dropNode) {
        removeClass(oldDropNode.$el, 'is-drop-inner')
      }
      const draggingNode = dragState.draggingNode
      if (!draggingNode || !dropNode) return

      let dropPrev = true
      let dropInner = true
      let dropNext = true
      const userAllowDropInner = true

      event.dataTransfer.dropEffect = dropInner ? 'move' : 'none'

      if (dropPrev || dropInner || dropNext) {
        dragState.dropNode = dropNode
      }

      if (draggingNode.node === dropNode.node) {
        dropPrev = false
        dropInner = false
        dropNext = false
      }

      const targetPosition = dropNode.$el.getBoundingClientRect()
      const treePosition = this.$el.getBoundingClientRect()

      let dropType
      const prevPercent = dropPrev ? (dropInner ? 0.25 : dropNext ? 0.45 : 1) : -1
      const nextPercent = dropNext ? (dropInner ? 0.75 : dropPrev ? 0.55 : 0) : 1

      let indicatorTop = -9999
      const distance = event.clientY - targetPosition.top
      if (distance < targetPosition.height * prevPercent) {
        dropType = 'before'
      } else if (distance > targetPosition.height * nextPercent) {
        dropType = 'after'
      } else if (dropInner) {
        dropType = 'inner'
      } else {
        dropType = 'none'
      }

      const iconPosition = dropNode.$el.querySelector('.tree-node__expand-icon').getBoundingClientRect()
      const dropIndicator = this.$refs.dropIndicator as any
      if (dropType === 'before') {
        indicatorTop = iconPosition.top - treePosition.top
      } else if (dropType === 'after') {
        indicatorTop = iconPosition.bottom - treePosition.top
      }
      dropIndicator.style.top = `${indicatorTop}px`
      dropIndicator.style.left = `${iconPosition.right - treePosition.left}px`

      if (dropType === 'inner') {
        addClass(dropNode.$el, 'is-drop-inner')
      } else {
        removeClass(dropNode.$el, 'is-drop-inner')
      }

      dragState.showDropIndicator = dropType === 'before' || dropType === 'after'
      dragState.allowDrop = dragState.showDropIndicator || userAllowDropInner
      dragState.dropType = dropType
      this.$emit('node-drag-over', draggingNode.node, dropNode.node, event)
    })

    this.$on('tree-node-drag-end', (event: any) => {
      const { draggingNode, dropType, dropNode } = dragState
      event.preventDefault()
      event.dataTransfer.dropEffect = 'move'
      if (draggingNode.node === dropNode.node) return

      if (draggingNode && dropNode) {
        const type = dropType as DropTypeEnum
        const draggingNodeCopy = { ...draggingNode.node }
        if (dropType !== 'none') {
          NestedMaterialModule.remove({
            target: dropNode.node,
            draggingNode: draggingNode.node,
            dropType: type,
          })
        }
        NestedMaterialModule.addNode({
          target: dropNode.node,
          newNode: draggingNodeCopy,
          dropType: type,
        })

        removeClass(dropNode.$el, 'is-drop-inner')
      }

      dragState.showDropIndicator = false
      dragState.draggingNode = null
      dragState.dropNode = null
      dragState.allowDrop = true
    })
  }

  created() {
    this.initTreeNodeDragEvent()
    this.initTreeEvent()
  }

  initTreeEvent() {
    this.$on('handleClick', this.handleClick)
  }

  get list() {
    return NestedMaterialModule.currentNestedMaterials
  }

  async addMaterial() {
    let material
    let params

    switch (NestedMaterialModule.materialType) {
      case TaskTypeEnum.HTTP:
        let materialParams: IHttpMaterial = {
          data: '',
          dataHandleType: '',
          dataType: 'JSON',
          groupName: '',
          headers: '[]',
          id: '',
          materialType: TaskTypeEnum.HTTP,
          method: 'GET',
          name: this.materialName,
          paramReplace: '',
          params: '[]',
          secondGroupName: '',
          url: '',
        }
        if (this.dialog.COPY) {
          materialParams = { ...this.selectedNestedMaterial.material } as IHttpMaterial
        }
        const { data: httpRes } = await saveHttpMaterial(materialParams)
        material = httpRes
        break
      case TaskTypeEnum.SQL:
        let sqlMaterialParams: ISqlMaterial = {
          id: '',
          name: this.materialName,
          material: '',
          datasource: '',
          database: '',
          groupName: '',
          secondGroupName: '',
          paramReplace: '',
          materialType: TaskTypeEnum.SQL,
        }
        if (this.dialog.COPY) {
          sqlMaterialParams = { ...this.selectedNestedMaterial.material } as ISqlMaterial
        }
        const { data: sqlRes } = await saveSqlMaterial(sqlMaterialParams)
        material = sqlRes
        break
      case TaskTypeEnum.REDIS:
        let redisMaterialParams: IRedisMaterial = {
          id: '',
          name: this.materialName,
          action: '',
          key: '',
          database: 0,
          datasource: '',
          groupName: '',
          secondGroupName: '',
          paramReplace: '',
          materialType: TaskTypeEnum.REDIS,
        }
        if (this.dialog.COPY) {
          redisMaterialParams = { ...this.selectedNestedMaterial.material } as IRedisMaterial
        }
        const { data: redisRes } = await saveRedisMaterial(redisMaterialParams)
        material = redisRes
        break
      case TaskTypeEnum.MONGO:
        let mongoMaterialParams: IMongoMaterial = {
          id: '',
          name: this.materialName,
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
        if (this.dialog.COPY) {
          mongoMaterialParams = { ...this.selectedNestedMaterial.material } as IMongoMaterial
        }
        const { data: mongoRes } = await saveMongoMaterial(mongoMaterialParams)
        material = mongoRes
        break
      case TaskTypeEnum.MQ:
        let mqMaterialParams: IMqMaterial = {
          id: '',
          name: this.materialName,
          exchange: '',
          routingKey: '',
          content: '',
          groupName: '',
          secondGroupName: '',
          paramReplace: '',
          materialType: TaskTypeEnum.MQ,
        }
        if (this.dialog.COPY) {
          mqMaterialParams = { ...this.selectedNestedMaterial.material } as IMqMaterial
        }
        const { data: mqRes } = await saveMqMaterial(mqMaterialParams)
        material = mqRes
        break
      case TaskTypeEnum.DATA:
        let dataMaterialParams: IDataMaterial = {
          id: '',
          name: this.materialName,
          data: '',
          describe: '',
          groupName: '',
          secondGroupName: '',
          materialType: TaskTypeEnum.DATA,
        }
        if (this.dialog.COPY) {
          dataMaterialParams = { ...this.selectedNestedMaterial.material } as IDataMaterial
        }
        const { data: dataRes } = await saveDataMaterial(dataMaterialParams)
        material = dataRes
        break
      case TaskTypeEnum.ENV:
        let envParams: IEnv = {
          id: '',
          name: this.materialName,
          http: '',
          sql: '',
          redis: '',
          mq: '',
          mongo: '',
          describe: '',
          globalVariable: '',
          materialType: TaskTypeEnum.ENV,
        }
        if (this.dialog.COPY) {
          envParams = { ...this.selectedNestedMaterial.material } as IEnv
        }
        const { data: envRes } = await saveEnv(envParams)
        material = envRes
        break
      case TaskTypeEnum.CASE:
        let testCaseMaterialParams: ITestCase = {
          id: '',
          name: this.materialName,
          material: '[]',
          groupName: '',
          secondGroupName: '',
          materialType: TaskTypeEnum.CASE,
        }
        if (this.dialog.COPY) {
          testCaseMaterialParams = { ...this.selectedNestedMaterial.material } as ITestCase
        }
        const { data: caseRes } = await saveTestCaseMaterial(testCaseMaterialParams)
        material = caseRes
        break
      case TaskTypeEnum.MOCK:
        let mockMaterialParams: IMockMaterial = {
          id: '',
          name: this.materialName,
          material: '',
          method: 'GET',
          url: '',
          materialType: TaskTypeEnum.MOCK,
        }
        if (this.dialog.COPY) {
          mockMaterialParams = { ...this.selectedNestedMaterial.material } as IMockMaterial
        }
        const { data: mockRes } = await saveMockMaterial(mockMaterialParams)
        material = mockRes
        break
      default:
        console.log(0)
    }
    return material as IMaterial
  }

  async handleMaterialAdd() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const material = await this.addMaterial()
      const parentId = this.dialog.COPY ? this.selectedNestedMaterial.parentId : this.selectedNestedMaterial.id
      const params: INestedMaterial = {
        name: '',
        type: NestedMaterialType.ITEM,
        materialType: NestedMaterialModule.materialType,
        parentId,
        materialId: material.id,
        material: material,
      }
      const { data } = await saveNestedMaterials(params)
      params.id = data.id
      params.userId = data.userId
      TreeUtils.wrapSingleNode(params)
      const parent = this.dialog.COPY ? this.selectedNestedMaterial.parent : this.selectedNestedMaterial
      NestedMaterialModule.addCatalog({
        parent: parent!,
        newItem: params,
        addTopCatalog: this.addTopCatalog,
      })

      NestedMaterialModule.updateMaterialSelected(params)
      const { id } = params.material!
      const { materialId } = this.$route.params
      if (id === materialId) return
      this.$router.push({
        name: RouteConstants.MATERIALPAGE,
        params: {
          materialId: id,
        },
      })

      this.dialog.COPY = false
    } catch (e) {
      console.log(e)
    } finally {
      loading.close()
      this.dialog.ADD_MATERIAL = false
    }
  }

  async handleCatalogAdd() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const params: INestedMaterial = {
        name: this.catalogName,
        type: NestedMaterialType.CATALOG,
        materialType: NestedMaterialModule.materialType,
        // parentId: this.selectedNestedMaterial.id,
      }
      if (!this.addTopCatalog) {
        params.parentId = this.selectedNestedMaterial.id
      }
      const { data } = await saveNestedMaterials(params)
      TreeUtils.wrapSingleNode(data)
      NestedMaterialModule.addCatalog({
        parent: this.selectedNestedMaterial,
        newItem: data,
        addTopCatalog: this.addTopCatalog,
      })
    } catch (e) {
      console.log(e)
    } finally {
      loading.close()
      this.dialog.ADD_FOLDER = false
    }
  }

  async handleCatalogRename() {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const params = TreeUtils.buildParams({ ...this.selectedNestedMaterial, name: this.catalogRename })
      await updateNestedMaterials(params)
      NestedMaterialModule.updateCatalogName({ item: this.selectedNestedMaterial, newName: this.catalogRename })
    } catch (e) {
      console.log(e)
    } finally {
      loading.close()
      this.dialog.RENAME = false
    }
  }

  async processTaskCopy(item: INestedMaterial, label: PopoverListItemActionEnum) {
    this.processNestedMaterialAction(item, label)
    this.handleMaterialAdd()
  }

  async processTaskRun(item: INestedMaterial) {
    const { materialType } = item
    // const loading = this.$loading({ text: '运行中' })
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const material = { ...item.material } as IMaterial
      ReplaceUtils.doReplace(material)
      let res: ITaskResult
      switch (materialType) {
        case TaskTypeEnum.HTTP:
          res = await TaskRunner.runHttp(material as IHttpMaterial)
          break
        case TaskTypeEnum.SQL:
          res = await TaskRunner.runSql(material as ISqlMaterial)
          break
        case TaskTypeEnum.REDIS:
          res = await TaskRunner.runRedis(material as IRedisMaterial)
          break
        case TaskTypeEnum.MONGO:
          res = await TaskRunner.runMongo(material as IMongoMaterial)
          break
        case TaskTypeEnum.MQ:
          res = await TaskRunner.runMq(material as IMqMaterial)
          break
        case TaskTypeEnum.DATA:
          res = await TaskRunner.runData(material as IDataMaterial)
          break
        case TaskTypeEnum.MOCK:
          res = await TaskRunner.runMock(material as IMockMaterial)
          break
        default:
          res = {}
          console.log(0)
      }
      const taskResult = { ...res }
      NestedMaterialModule.updateTaskResult(taskResult)
    } catch (e) {
      this.$notify.warning({
        title: '警告',
        message: e.message,
      })
    } finally {
      loading.close()
    }
  }

  async processTaskShare(item: INestedMaterial) {
    const params: IShareMaterial = {
      type: item.materialType!,
      sharedUserIds: null,
      materialId: item.materialId!,
    }
    const shareMaterial = getShareMaterialByMaterialId(params.materialId)
    if (!shareMaterial) {
      ShareMaterialModule.add(params)
      this.$notify.success({
        title: '成功',
        message: '分享成功!',
      })
    } else {
      if (shareMaterial.type === TaskTypeEnum.ENV && shareMaterial.userId !== UserModule.userId) {
        this.$notify.warning({
          title: '警告',
          message: '无法操作别人的分享',
        })
        return
      }
      params.id = shareMaterial.id
      ShareMaterialModule.del(params)
      this.$notify.success({
        title: '成功',
        message: '取消分享成功!',
      })
    }
  }

  async processTaskDelete(item: INestedMaterial, label: PopoverListItemActionEnum) {
    try {
      await this.$confirm('确定删除吗?')
    } catch {
      return
    }
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      const { materialType } = item
      switch (materialType) {
        case TaskTypeEnum.HTTP:
          await deleteHttpMaterial(item.material as IHttpMaterial)
          break
        case TaskTypeEnum.SQL:
          await deleteSqlMaterial(item.material as ISqlMaterial)
          break
        case TaskTypeEnum.REDIS:
          await deleteRedisMaterial(item.material as IRedisMaterial)
          break
        case TaskTypeEnum.MONGO:
          await deleteMongoMaterial(item.material as IMongoMaterial)
          break
        case TaskTypeEnum.MQ:
          await deleteMqMaterial(item.material as IMqMaterial)
          break
        case TaskTypeEnum.DATA:
          await deleteDataMaterial(item.material as IDataMaterial)
          break
        case TaskTypeEnum.CASE:
          await deleteTestCaseMaterial(item.material as ITestCase)
          break
        case TaskTypeEnum.MOCK:
          await deleteMockMaterial(item.material as IMockMaterial)
          break
        default:
          console.log(0)
      }
      await deleteNestedMaterials(TreeUtils.buildParams(item))
      NestedMaterialModule.removeNode(item)
      this.$notify.success({
        title: '成功',
        message: '删除成功!',
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  openCatalogAddModal() {
    this.addTopCatalog = true
    this.dialog.ADD_FOLDER = true
    this.catalogName = ''
    this.$nextTick(() => {
      const el = this.$refs.ADD_FOLDER as any
      el.focus()
    })
  }

  processNestedMaterialAction(item: INestedMaterial, label: PopoverListItemActionEnum) {
    this.addTopCatalog = false
    this.selectedNestedMaterial = item
    this.dialog[label] = true
    this.catalogRename = item.name || ''
    this.catalogName = ''
    this.materialName = ''
    this.$nextTick(() => {
      const el = this.$refs[label] as any
      if (el) {
        el.focus()
      }
    })
  }

  handleClick(item: INestedMaterial, label: PopoverListItemActionEnum) {
    switch (label) {
      case PopoverListItemActionEnum.RENAME:
      case PopoverListItemActionEnum.ADD_FOLDER:
      case PopoverListItemActionEnum.ADD_MATERIAL:
        this.processNestedMaterialAction(item, label)
        break
      case PopoverListItemActionEnum.RUN:
        this.processTaskRun(item)
        break
      case PopoverListItemActionEnum.COPY:
        this.processTaskCopy(item, label)
        break
      case PopoverListItemActionEnum.DELETE:
        this.processTaskDelete(item, label)
        break
      case PopoverListItemActionEnum.SHARE:
        this.processTaskShare(item)
        break
      default:
        console.log(0)
    }
  }
}
</script>
<style lang="stylus" scoped>
.tree-container
  position relative
  height 100%
  display flex
  flex-direction column
  padding 10px
  box-sizing border-box
  font-size 14px
  line-height 16px
  overflow hidden
  user-select none
  color $text-color2
  border-right 1px solid $bg-color1
  .tree-content
    position relative
    overflow scroll
    flex 1
    margin 10px 0
  .add-catalog
    position relative
    display flex
    align-items center
    justify-content center
    padding 6px
    margin 0 20px
    box-sizing border-box
    border 1px solid $border-color1
    border-radius 4px
.el-tree__drop-indicator
  position absolute
  right 0
  height 1px
  background-color #409eff
  margin-right 10px
</style>
