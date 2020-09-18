package com.syc.suda.controller

import com.syc.suda.entity.FormField
import com.syc.suda.service.FormFieldService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@CompileStatic
@RestController
@RequestMapping('/form-field')
class FormFieldController {

    @Autowired
    FormFieldService formFieldService

    @GetMapping
    ResponseBean listFormField() {
        log.info("request form field list")
        return ResponseBean.success(formFieldService.listFormField())
    }

    @PostMapping
    ResponseBean save(@RequestBody FormField formField) {
        log.info("add form field")
        formFieldService.save(formField)
        return ResponseBean.success(formField)
    }

}
