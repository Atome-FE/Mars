package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.TestGroup
import com.syc.suda.service.TestGroupService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
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

@Slf4j
@CompileStatic
@RestController
@RequestMapping('/test-group')
class TestGroupController {

    @Autowired
    TestGroupService testGroupService

    @GetMapping
    ResponseBean listTestGroup(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        log.info('获取测试组列表: {}', sessionInfo.id)
        return ResponseBean.success(testGroupService.listTestGroup(sessionInfo.id))
    }

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @Validated @RequestBody TestGroup testGroup) {
        testGroup.userId = sessionInfo.id
        testGroupService.save(testGroup)
        return ResponseBean.success(testGroup)
    }

    @PutMapping
    ResponseBean update(@RequestBody TestGroup testGroup) {
        testGroupService.update(testGroup)
        return ResponseBean.success(testGroup)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody TestGroup testGroup) {
        testGroupService.deleteById(testGroup.id)
        return ResponseBean.success()
    }
}
