<template>
  <div class="bg login-bg">
    <div class="main">
      <div class="content">
        <div class="title">
          <span>Mars</span>
        </div>
        <div class="input">
          <el-input
            v-model="loginForm.email"
            autofocus="true"
            size="mini"
            class="mobile-input"
            @keyup.enter.native="submit"
            placeholder="请输入email"
          ></el-input>
          <el-input
            v-model="loginForm.password"
            size="mini"
            show-password
            @keyup.enter.native="submit"
            placeholder="请输入密码"
          ></el-input>
          <el-button class="submit-btn" size="mini" :loading="loading" @click="submit" type="primary">登录</el-button>
        </div>
        <!-- <div class="footer">
          <span>
            没有帐号？
          </span>
          <span class="register"
            @click="gotoRegister">
            注册
          </span>
        </div>-->
      </div>
    </div>
  </div>
</template>

<script lang="tsx">
import { Component, Vue } from 'vue-property-decorator'

import { login } from '@/apis/auth-api'

import { UserModule } from '@/store/modules/user-module'

import { ILoginParams } from '@/interfaces/auth-interface'

import { RouteConstants } from '@/constants/route-constants'

@Component
export default class LoginApp extends Vue {
  loginForm: ILoginParams = {
    email: '',
    password: '',
  }

  loading = false

  gotoRegister() {
    this.$router.push({
      name: RouteConstants.REGISTER,
    })
  }

  async submit() {
    try {
      this.loading = true
      const res = (await login(this.loginForm)) as any
      UserModule.setUserId(res.data)
      this.$notify.success({
        message: '登录成功',
        title: '成功',
      })
      const { redirect } = this.$route.query
      if (redirect) {
        this.$router.push(redirect as string)
      } else {
        this.$router.push({
          name: RouteConstants.TESTGROUPHOME,
        })
      }
    } catch (e) {
      this.$handleError(e)
    } finally {
      this.loading = false
    }
  }
}
</script>

<style lang="stylus" scoped>
.login-bg
  position fixed
  top 0
  left 0
  width 100vw
  height 100vh
.bg
  // background-image url('~@/assets/images/login-bg.jpg')
  background-image url('~@/assets/images/suc-login-bg.jpg')
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
