package com.syc.suda.service

import com.syc.suda.entity.TaskRecord

interface TaskRecordService {

    int save(TaskRecord taskRecord)

    List<TaskRecord> listTaskRecords(String userId)

}