package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.HttpMaterial
import com.syc.suda.exception.BizException
import com.syc.suda.service.HttpMaterialService
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
@RequestMapping('/http-material')
class HttpMaterialController {

    @Autowired
    HttpMaterialService httpMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody HttpMaterial httpMaterial) {
        httpMaterial.userId = sessionInfo.id
        httpMaterialService.save(httpMaterial)
        return ResponseBean.success(httpMaterial)
    }

    @GetMapping
    ResponseBean listHttpMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(httpMaterialService.listHttpMaterial(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody HttpMaterial httpMaterial) {
        httpMaterialService.update(httpMaterial)
        return ResponseBean.success(httpMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody HttpMaterial httpMaterial) {
        httpMaterialService.deleteById(httpMaterial.id)
        return ResponseBean.success()
    }
}
