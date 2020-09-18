package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.DataMaterial
import com.syc.suda.service.DataMaterialService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/data-material')
class DataMaterialController {

    @Autowired
    DataMaterialService dataMaterialService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody DataMaterial dataMaterial) {
        dataMaterial.userId = sessionInfo.id
        dataMaterialService.save(dataMaterial)
        return ResponseBean.success(dataMaterial)
    }

    @GetMapping
    ResponseBean listSqlMaterial(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(dataMaterialService.list(sessionInfo.id))
    }

    @GetMapping('/{id}')
    ResponseBean getById(@PathVariable('id') String id) {
        return ResponseBean.success(dataMaterialService.getById(id))
    }

    @PutMapping
    ResponseBean update(@RequestBody DataMaterial dataMaterial) {
        dataMaterialService.update(dataMaterial)
        return ResponseBean.success(dataMaterial)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody DataMaterial dataMaterial) {
        dataMaterialService.deleteById(dataMaterial.id)
        return ResponseBean.success()
    }

}
