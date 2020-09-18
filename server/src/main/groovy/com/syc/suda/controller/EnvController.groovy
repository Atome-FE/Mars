package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.Env
import com.syc.suda.service.EnvService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/env')
class EnvController {

    @Autowired
    EnvService envService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody Env env) {
        env.userId = sessionInfo.id
        envService.save(env)
        return ResponseBean.success(env)
    }

    @GetMapping
    ResponseBean listSqlMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(envService.listEnv(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody Env env) {
        envService.update(env)
        return ResponseBean.success(env)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody Env env) {
        envService.deleteById(env.id)
        return ResponseBean.success()
    }
}
