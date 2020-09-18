<template>
  <div class="new-navigator">
    <div class="header">Mars</div>
    <el-menu
      class="menu-content"
      :default-active="activatedMenu"
      @open="handleOpen"
      @close="handleClose"
      @select="handleSelect"
      text-color="black"
      active-text-color="#409eff"
    >
      <el-menu-item index="1">
        <i class="el-icon-menu"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>资源</span>
        </template>
        <el-menu-item-group title="任务模块">
          <el-menu-item index="2-1">用例</el-menu-item>
          <el-menu-item index="2-2">HTTP</el-menu-item>
          <el-menu-item index="2-3">SQL</el-menu-item>
          <el-menu-item index="2-4">REDIS</el-menu-item>
          <el-menu-item index="2-5">MQ</el-menu-item>
          <el-menu-item index="2-6">MONGO</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="资源模块">
          <el-menu-item index="2-7">数据源</el-menu-item>
          <el-menu-item index="2-8">环境配置</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="Mock数据">
          <el-menu-item index="2-9">MOCK</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-menu-item index="3">
        <i class="el-icon-document"></i>
        <span slot="title">测试报告</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-star-off"></i>
        <span slot="title">分享模块</span>
      </el-menu-item>
      <el-menu-item index="5">
        <i class="el-icon-setting"></i>
        <span slot="title">权限管理</span>
      </el-menu-item>
      <el-menu-item index="6">
        <i class="el-icon-menu"></i>
        <span slot="title">服务监控</span>
      </el-menu-item>
      <el-menu-item index="7">
        <i class="el-icon-setting"></i>
        <span slot="title">自动化程序运行配置</span>
      </el-menu-item>
      <!--
      <el-menu-item index="6">
        <i class="el-icon-setting"></i>
        <span slot="title">设置</span>
      </el-menu-item>
      -->
    </el-menu>
    <div class="footer">
      <el-select size="mini" class="select" v-model="selectedEnvId" placeholder="请选择环境">
        <el-option v-for="item in getOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        <el-option v-for="item in getShareConfigs" :key="'share' + item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
      <el-button @click="logout" type="text">退出</el-button>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator'
import { Route } from 'vue-router'
import { RouteConstants } from '../constants/route-constants'
import { RoleTypeEnum } from '../enums/user-enum'
import { TaskTypeEnum } from '../enums/task-enum'
import { EnvModule } from '../store/modules/env-module'
import { IEnv } from '../interfaces/env-interface'
import { ShareMaterialModule } from '../store/modules/share-material-module'
import { logout } from '../apis/auth-api'

@Component
export default class NewNavigator extends Vue {
  selectedEnvId: string = ''

  created() {
    this.selectedEnvId = EnvModule.selectedEnv.id
  }

  activated() {
    this.selectedEnvId = EnvModule.selectedEnv.id
  }

  activatedMenu = ''

  @Watch('$route')
  watchRoute(newRoute: Route) {
    const { fullPath } = newRoute
    const { materialType } = newRoute.params

    Object.keys(this.menuAuthMap).forEach(key => {
      if (materialType) {
        if (this.menuAuthMap[key].materialType === materialType) {
          this.activatedMenu = key
        }
      } else if (this.menuAuthMap[key].pageName === newRoute.name) {
        this.activatedMenu = key
      }
    })
  }

  @Watch('selectedEnvId')
  selectedEnvIdChange(val: string) {
    EnvModule.setSelectedEnv(val)
  }

  get getOptions() {
    return EnvModule.envs.map((env: IEnv) => {
      return {
        value: env.id,
        label: env.name,
      }
    })
  }

  get getShareConfigs() {
    const list = ShareMaterialModule.listEnvConfig
    return list.map((env: IEnv) => {
      return {
        value: env.id,
        label: env.name,
      }
    })
  }

  async logout() {
    try {
      await this.$confirm('确定退出登录吗')
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
      await logout()
      this.$router.push({
        name: RouteConstants.LOGIN,
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      loading.close()
    }
  }

  menuAuthMap: any = {
    '1': {
      pageName: RouteConstants.TESTGROUPHOME,
      content: 'Mars',
      authList: [RoleTypeEnum.ADMIN],
    },
    '2-1': {
      pageName: RouteConstants.MATERIAL,
      content: '用例',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.CASE,
    },
    '2-2': {
      pageName: RouteConstants.MATERIAL,
      content: 'http',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.HTTP,
    },
    '2-3': {
      pageName: RouteConstants.MATERIAL,
      content: 'sql',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.SQL,
    },
    '2-4': {
      pageName: RouteConstants.MATERIAL,
      content: 'redis',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.REDIS,
    },
    '2-5': {
      pageName: RouteConstants.MATERIAL,
      content: 'rabbitmq',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.MQ,
    },
    '2-6': {
      pageName: RouteConstants.MATERIAL,
      content: 'mongo',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.MONGO,
    },
    '2-7': {
      pageName: RouteConstants.MATERIAL,
      content: '数据源',
      authList: [RoleTypeEnum.ADMIN],
      materialType: TaskTypeEnum.DATA,
    },
    '2-8': {
      pageName: RouteConstants.MATERIAL,
      content: '环境配置',
      authList: [RoleTypeEnum.ADMIN, RoleTypeEnum.NORMAL],
      materialType: TaskTypeEnum.ENV,
    },
    '2-9': {
      pageName: RouteConstants.MATERIAL,
      content: 'MOCK',
      authList: [RoleTypeEnum.ADMIN, RoleTypeEnum.NORMAL],
      materialType: TaskTypeEnum.MOCK,
    },
    '3': {
      pageName: RouteConstants.TASKRECORDHOME,
      content: '报告',
      authList: [RoleTypeEnum.ADMIN],
    },
    '4': {
      pageName: RouteConstants.SHAREHOME,
      content: '分享列表',
      authList: [RoleTypeEnum.ADMIN, RoleTypeEnum.NORMAL],
    },
    '5': {
      pageName: RouteConstants.AUTHUSER,
      content: '权限配置',
      authList: [RoleTypeEnum.ADMIN],
    },
    '6': {
      pageName: RouteConstants.DOCKER_SERVICE,
      content: '服务监控',
      authList: [RoleTypeEnum.ADMIN],
    },
    '7': {
      pageName: RouteConstants.AUTOTESTCONFIGURATION,
      content: '自动化程序配置',
      authList: [RoleTypeEnum.ADMIN],
    },
    // TODO
    '12': {
      pageName: RouteConstants.DOCUMENTHOME,
      content: '更多',
      authList: [RoleTypeEnum.ADMIN],
    },
  }

  handleOpen(key: string, keyPath: string) {
    console.log(key, keyPath)
  }

  handleClose(key: string, keyPath: string) {
    console.log(key, keyPath)
  }

  handleSelect(key: any, keyPath: any) {
    if (key === '2') return
    const { pageName, materialType } = this.menuAuthMap[key]
    if (!pageName) return
    if (this.$route.name === pageName) {
      const routeMaterialType = this.$route.params.materialType
      if (routeMaterialType) {
        if (routeMaterialType === materialType) return
      } else {
        return
      }
    }
    this.$router.push({
      name: pageName,
      params: {
        materialType: materialType,
      },
    })
  }
}
</script>

<style lang="stylus" scoped>
.new-navigator
  position relative
  display flex
  flex-direction column
  height 100%
  user-select none
  overflow scroll
  .menu-content
    flex 1
  .header
    color $text-color3
    font-size 30px
    font-weight bold
    display flex
    align-items center
    justify-content center
    background-color $bg-color3
    padding 20px 0
  .footer
    position relative
    display flex
    justify-content center
    align-items center
    background-color $bg-color3
    padding-bottom 40px
    .select
      margin-right 10px
</style>
