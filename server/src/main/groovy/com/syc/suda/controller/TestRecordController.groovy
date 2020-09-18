package com.syc.suda.controller

import com.syc.suda.entity.TestRecord
import com.syc.suda.service.TestRecordService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/test-record')
class TestRecordController {

    @Autowired
    TestRecordService testRecordService

    @PostMapping
    ResponseBean save(@RequestBody TestRecord testRecord) {
        testRecordService.save(testRecord)
        return ResponseBean.success(testRecord)
    }
}
