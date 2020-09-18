<template>
  <div class="bg">
    <div class="main">
      <div class="content">
        <div class="title">
          <span>
            Mars
          </span>
        </div>
        <div class="input">
          <el-input
            v-model="loginForm.mobileNumber"
            size="mini"
            class="mobile-input"
            autofocus="true"
            @keyup.enter.native="submit"
            placeholder="请输入手机号"
          >
          </el-input>
          <!--
          <el-input
            v-model="loginForm.password"
            show-password
            size="mini"
            @keyup.enter.native="submit"
            placeholder="请输入密码">
          </el-input>
          -->
          <el-button class="submit-btn" size="mini" @click="submit" :loading="loading" type="primary">
            注册
          </el-button>
        </div>
        <div class="footer">
          <span>
            已有帐号？
          </span>
          <span class="register" @click="gotoLogin">
            登录
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="tsx">
import { Component, Vue } from 'vue-property-decorator'

import { register } from '@/apis/auth-api'

import { DataLoadUtils } from '@/lib/utils/data-load-utils'

import { UserModule } from '@/store/modules/user-module'

import { IRegisterParams } from '@/interfaces/auth-interface'

import { RouteConstants } from '@/constants/route-constants'

@Component
export default class LoginApp extends Vue {
  loginForm: IRegisterParams = {
    mobileNumber: '',
    password: '',
  }

  loading = false

  gotoLogin() {
    this.$router.push({
      name: RouteConstants.LOGIN,
    })
  }

  async submit() {
    try {
      this.loading = true
      const res = (await register(this.loginForm)) as any
      UserModule.setUserId(res.data)
      DataLoadUtils.loadAllData()
      this.$notify.success({
        message: '注册成功',
        title: '成功',
      })
      this.$router.push({
        name: RouteConstants.TESTGROUPHOME,
      })
    } catch (e) {
      this.$handleError(e)
    } finally {
      this.loading = false
    }
  }
}
</script>

<style lang="stylus" scoped>
.bg
  background-image url('~@/assets/images/register-bg.jpg')
  background-size 100% 100%
.main
  position relative
  display flex
  margin-top 13%
  justify-content center
  width 100%
  .content
    position relative
    display flex
    flex-direction column
    align-items center
    border-radius 10px
    padding 40px
    background-color rgba(238, 230, 230, 0.65)
    .title
      font-size 30px
      font-weight bold
      color #409eff
    .input
      position relative
      display flex
      flex-direction column
      margin-top 20px
      width 200px
      .submit-btn
        margin-top 20px
      .mobile-input
        margin-bottom 10px
    .footer
      margin-top 20px
      font-size 14px
      .register
        color black
        font-weight bold
</style>
