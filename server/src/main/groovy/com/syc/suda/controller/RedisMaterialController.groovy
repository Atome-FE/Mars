package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.RedisMaterial
import com.syc.suda.service.RedisMaterialService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/redis-material')
class RedisMaterialController {
    @Autowired
    RedisMaterialService redisMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody RedisMaterial redisMaterial) {
        redisMaterial.userId = sessionInfo.id
        redisMaterialService.save(redisMaterial)
        return ResponseBean.success(redisMaterial)
    }

    @GetMapping
    ResponseBean listRedisMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(redisMaterialService.listRedisMaterial(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody RedisMaterial redisMaterial) {
        redisMaterialService.update(redisMaterial)
        return ResponseBean.success(redisMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody RedisMaterial redisMaterial) {
        redisMaterialService.deleteById(redisMaterial.id)
        return ResponseBean.success()
    }
}
