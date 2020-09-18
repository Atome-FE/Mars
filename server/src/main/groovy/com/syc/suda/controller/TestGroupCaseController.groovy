package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.TestGroupCase
import com.syc.suda.service.TestGroupCaseService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
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
@RequestMapping('/test-group-case')
class TestGroupCaseController {

    @Autowired
    TestGroupCaseService testGroupCaseService

    @GetMapping
    ResponseBean listTestGroupCase(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        return ResponseBean.success(testGroupCaseService.listTestGroupCase(sessionInfo.id))
    }

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @Validated @RequestBody TestGroupCase testGroupCase) {
        testGroupCase.userId = sessionInfo.id
        testGroupCaseService.save(testGroupCase)
        return ResponseBean.success(testGroupCase)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody TestGroupCase testGroupCase) {
        testGroupCaseService.deleteById(testGroupCase.id)
        return ResponseBean.success()
    }

    @PutMapping
    ResponseBean updatePriorityById(@RequestBody TestGroupCase testGroupCase) {
        testGroupCaseService.updatePriorityById(testGroupCase.id, testGroupCase.priority)
        return ResponseBean.success()
    }
}
