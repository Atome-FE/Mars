package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.MqMaterial
import com.syc.suda.service.MqMaterialService
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
@RequestMapping('/mq-material')
class MqMaterialController {

    @Autowired
    MqMaterialService mqMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody MqMaterial mqMaterial) {
        mqMaterial.userId = sessionInfo.id
        mqMaterialService.save(mqMaterial)
        return ResponseBean.success(mqMaterial)
    }

    @GetMapping
    ResponseBean listMqMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(mqMaterialService.listMqMaterials(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody MqMaterial mqMaterial) {
        mqMaterialService.update(mqMaterial)
        return ResponseBean.success(mqMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody MqMaterial mqMaterial) {
        mqMaterialService.deleteById(mqMaterial.id)
        return ResponseBean.success()
    }
}
