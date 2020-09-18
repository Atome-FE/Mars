package com.syc.suda.controller

import com.syc.suda.entity.FieldCategory
import com.syc.suda.enums.ResponseCodeEnum
import com.syc.suda.exception.FieldCategoryExistException
import com.syc.suda.service.FieldCategoryService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/field-category')
class FieldCategoryController {

    @Autowired
    FieldCategoryService fieldCategoryService

    @GetMapping
    ResponseBean listFieldCategory() {
        try {
            List<FieldCategory> fieldCategories = fieldCategoryService.listFieldCategory()
            return ResponseBean.success(fieldCategories)
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    @PostMapping
    ResponseBean save(@Validated @RequestBody FieldCategory fieldCategory) {
        try {
            fieldCategoryService.save(fieldCategory)
            return ResponseBean.success(fieldCategory)
        } catch (FieldCategoryExistException e) {
            return ResponseBean.fail(ResponseCodeEnum.ERROR, e.message)
        }
    }
}
