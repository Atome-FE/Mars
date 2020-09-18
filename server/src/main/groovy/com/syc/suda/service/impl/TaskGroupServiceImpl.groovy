package com.syc.suda.service.impl

import com.syc.suda.entity.TaskGroup
import com.syc.suda.mapper.TaskGroupMapper
import com.syc.suda.service.TaskGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskGroupServiceImpl implements TaskGroupService {

    @Autowired
    TaskGroupMapper taskGroupMapper

    @Override
    List<TaskGroup> listTaskGroups() {
        return taskGroupMapper.listTaskGroups()
    }
}
