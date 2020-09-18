package com.syc.suda.service.impl

import com.syc.suda.entity.TaskRecord
import com.syc.suda.mapper.TaskRecordMapper
import com.syc.suda.service.TaskRecordService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskRecordServiceImpl implements TaskRecordService {

    private static final String ID_PREFIX = 'T'

    @Autowired
    TaskRecordMapper taskRecordMapper

    @Override
    int save(TaskRecord taskRecord) {
        taskRecord.id = IdGenerator.generate(ID_PREFIX)
        return taskRecordMapper.save(taskRecord)
    }

    @Override
    List<TaskRecord> listTaskRecords(String userId) {
        return taskRecordMapper.listTaskRecords(userId)
    }
}
