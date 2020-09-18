package com.syc.suda.controller

import com.syc.suda.ao.LoginAO
import com.syc.suda.entity.net.RegisterParams
import com.syc.suda.enums.ResponseCodeEnum
import com.syc.suda.exception.AccountExistException
import com.syc.suda.exception.UserNotFoundException
import com.syc.suda.service.AdminService
import com.syc.suda.service.AuthService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@CompileStatic
@RestController
@RequestMapping('/auth')
class AuthController {

    @Autowired
    AuthService authService

    @Autowired
    AdminService adminService

    @PostMapping('/logout')
    ResponseBean logout() {
        authService.logout()
        return ResponseBean.success()
    }

    /*
    @PostMapping("/register")
    ResponseBean register(@Valid @RequestBody RegisterParams registerRequest) {
        try {
            String id = authService.safeRegister(registerRequest)
            return ResponseBean.success(id)
        } catch (AccountExistException e) {
            return ResponseBean.fail(ResponseCodeEnum.ACCOUNT_EXIST, e.message)
        }
    }
    */

    @PostMapping("/login")
    ResponseBean login(@Valid @RequestBody LoginAO ao) {
        try {
            String id = authService.login(ao)
            return ResponseBean.success(id)
        } catch (UserNotFoundException e) {
            return ResponseBean.fail(ResponseCodeEnum.ACCOUNT_NOT_EXIST, e.message)
        }
    }

}
