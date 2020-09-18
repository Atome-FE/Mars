package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.ServiceHealthCheckMapping
import com.syc.suda.service.ServiceHealthCheckMappingService
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
@RequestMapping('/service-mapping')
class ServiceHealthCheckMappingController {
    @Autowired
    ServiceHealthCheckMappingService mappingService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody ServiceHealthCheckMapping mapping) {
        mapping.userId = sessionInfo.id
        mappingService.save(mapping)
        return ResponseBean.success(mapping)
    }

    @GetMapping('/all')
    ResponseBean listAll() {
        println('all')
        return ResponseBean.success(mappingService.listAll())
    }

    @GetMapping
    ResponseBean listByUserId(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(mappingService.listByUserId(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody ServiceHealthCheckMapping mapping) {
        mappingService.update(mapping)
        return ResponseBean.success(mapping)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody ServiceHealthCheckMapping mapping) {
        mappingService.delete(mapping.id)
        return ResponseBean.success()
    }
}
