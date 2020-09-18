package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.NestedMaterial
import com.syc.suda.enums.MaterialType
import com.syc.suda.service.NestedMaterialService
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/nested-material')
class NestedMaterialController {
    @Autowired
    NestedMaterialService nestedMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody NestedMaterial nestedMaterial) {
        nestedMaterial.userId = sessionInfo.id
        nestedMaterialService.save(nestedMaterial)
        return ResponseBean.success(nestedMaterial)
    }

    @GetMapping
    ResponseBean listByMaterialType(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                                    @RequestParam('materialType') MaterialType materialType) {
        return ResponseBean.success(nestedMaterialService.listByMaterialType(materialType, sessionInfo.id))
    }

    @GetMapping('/all')
    ResponseBean listAll(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(nestedMaterialService.listAll(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody NestedMaterial nestedMaterial) {
        nestedMaterialService.update(nestedMaterial)
        return ResponseBean.success(nestedMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody NestedMaterial nestedMaterial) {
        nestedMaterialService.delete(nestedMaterial.id)
        return ResponseBean.success()
    }
}
