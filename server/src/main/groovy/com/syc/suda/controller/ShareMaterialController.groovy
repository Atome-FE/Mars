package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.ShareMaterial
import com.syc.suda.entity.ShareMaterialParams
import com.syc.suda.service.ShareMaterialService
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
@RequestMapping('/share-material')
class ShareMaterialController {
    @Autowired
    ShareMaterialService shareMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody ShareMaterial shareMaterial) {
        shareMaterial.userId = sessionInfo.id
        shareMaterialService.save(shareMaterial)
        return ResponseBean.success(shareMaterial)
    }

    @GetMapping
    ResponseBean listShareMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(shareMaterialService.listShareMaterials(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody ShareMaterial shareMaterial) {
        shareMaterialService.update(shareMaterial)
        return ResponseBean.success(shareMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody ShareMaterial shareMaterial) {
        shareMaterialService.deleteById(shareMaterial.id)
        return ResponseBean.success()
    }

    @PostMapping('/params')
    ResponseBean saveShareMaterialParams(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                                         @RequestBody ShareMaterialParams shareMaterialParams) {
        shareMaterialParams.userId = sessionInfo.id
        ShareMaterialParams params = shareMaterialService.getShareMaterialParamsByUserIdAndShareMaterialId(shareMaterialParams.userId, shareMaterialParams.shareMaterialId)
        if (params) {
            return ResponseBean.success(params)
        }
        shareMaterialService.saveShareMaterialParams(shareMaterialParams)
        return ResponseBean.success(shareMaterialParams)
    }

    @PutMapping('/params')
    ResponseBean updateShareMaterialParams(@RequestBody ShareMaterialParams shareMaterialParams) {
        shareMaterialService.updateShareMaterialParams(shareMaterialParams)
        return ResponseBean.success(shareMaterialParams)
    }

    @DeleteMapping('/params')
    ResponseBean deleteShareMaterialParamsById(@RequestBody ShareMaterialParams shareMaterialParams) {
        shareMaterialService.deleteShareMaterialParamsById(shareMaterialParams.id)
        return ResponseBean.success()
    }
}
