package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.TaskRecord
import com.syc.suda.service.TaskRecordService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/task-record')
class TaskRecordController {

    @Autowired
    TaskRecordService taskRecordService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody TaskRecord taskRecord) {
        taskRecord.userId = sessionInfo.id
        taskRecordService.save(taskRecord)
        return ResponseBean.success(taskRecord)
    }

    @GetMapping
    ResponseBean listTaskRecords(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        List<TaskRecord> taskRecords = taskRecordService.listTaskRecords(sessionInfo.id)
        return ResponseBean.success(taskRecords)
    }

}
