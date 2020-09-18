package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.MockMaterial
import com.syc.suda.service.MockMaterialService
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
@RequestMapping('/mock-material')
class MockMaterialController {

    @Autowired
    MockMaterialService mockMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody MockMaterial material) {
        material.userId = sessionInfo.id
        mockMaterialService.save(material)
        return ResponseBean.success(material)
    }

    @GetMapping
    ResponseBean listMockMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(mockMaterialService.listMockMaterials(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody MockMaterial material) {
        mockMaterialService.update(material)
        return ResponseBean.success(material)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody MockMaterial material) {
        mockMaterialService.deleteById(material.id)
        return ResponseBean.success()
    }
}
