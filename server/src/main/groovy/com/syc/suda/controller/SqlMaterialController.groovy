package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.SqlMaterial
import com.syc.suda.service.SqlMaterialService
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
@RequestMapping('/sql-material')
class SqlMaterialController {

    @Autowired
    SqlMaterialService sqlMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody SqlMaterial sqlMaterial) {
        sqlMaterial.userId = sessionInfo.id
        sqlMaterialService.save(sqlMaterial)
        return ResponseBean.success(sqlMaterial)
    }

    @GetMapping
    ResponseBean listSqlMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(sqlMaterialService.listSqlMaterials(sessionInfo.id))
    }

    @PutMapping
    ResponseBean update(@RequestBody SqlMaterial sqlMaterial) {
        sqlMaterialService.update(sqlMaterial)
        return ResponseBean.success(sqlMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody SqlMaterial sqlMaterial) {
        sqlMaterialService.deleteById(sqlMaterial.id)
        return ResponseBean.success()
    }
}
