package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.MongoMaterial
import com.syc.suda.service.MongoMaterialService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@CompileStatic
@RestController
@RequestMapping('/mongo-material')
class MongoMaterialController {

    @Autowired
    MongoMaterialService mongoMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody MongoMaterial mongoMaterial) {
        mongoMaterial.userId = sessionInfo.id
        mongoMaterialService.save(mongoMaterial)
        return ResponseBean.success(mongoMaterial)
    }

    @GetMapping
    ResponseBean listSqlMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(mongoMaterialService.listMongoMaterials(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody MongoMaterial sqlMaterial) {
        mongoMaterialService.update(sqlMaterial)
        return ResponseBean.success(sqlMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody MongoMaterial mongoMaterial) {
        mongoMaterialService.delete(mongoMaterial.id)
        return ResponseBean.success()
    }
}
