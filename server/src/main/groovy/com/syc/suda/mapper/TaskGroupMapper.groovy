package com.syc.suda.mapper

import com.syc.suda.entity.TaskGroup

interface TaskGroupMapper {

    List<TaskGroup> listTaskGroups()

    List<TaskGroup> listHttpMaterial()

    List<TaskGroup> listSqlMaterial()

    List<TaskGroup> listRedisMaterial()

    List<TaskGroup> listMqMaterial()

}