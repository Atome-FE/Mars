package com.syc.suda.controller

import com.syc.suda.service.AppMappingService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CompileStatic
@RequestMapping('/app-mapping')
class AppMappingController {

    @Autowired
    AppMappingService appMappingService

    @GetMapping
    ResponseBean listDocument() {
        return ResponseBean.success(appMappingService.listAppMapping())
    }
}
