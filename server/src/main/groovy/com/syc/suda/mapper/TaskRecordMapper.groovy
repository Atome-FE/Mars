package com.syc.suda.mapper

import com.syc.suda.entity.TaskRecord
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface TaskRecordMapper {

    @Insert('''
    insert into task_record(
    id,
    task_id,
    task_type,
    task_delay,
    task_assert,
    assert_result,
    task_result,
    test_case_id,
    test_group_id,
    execute_time,
    user_id,
    environment,
    task_param,
    task_name
    )
    values(
    #{id},
    #{taskId},
    #{taskType},
    #{taskDelay},
    #{taskAssert},
    #{assertResult},
    #{taskResult},
    #{testCaseId},
    #{testGroupId},
    #{executeTime},
    #{userId},
    #{environment},
    #{taskParam},
    #{taskName}
    )
    ''')
    int save(TaskRecord taskRecord)

    @Select('''
    select t1.id,
    t1.task_id as taskId,
    t1.task_type as taskType,
    t1.task_delay as taskDelay,
    t1.task_assert as taskAssert,
    t1.assert_result as assertResult,
    t1.task_result as taskResult,
    t1.test_case_id as testCaseId,
    t1.test_group_id as testGroupId,
    t1.execute_time as executeTime,
    t1.task_param as taskParam,
    t1.task_name as taskName,
    t2.name as testCaseName,
    t3.name as testGroupName
    from task_record t1
    left join test_case t2
    on t1.test_case_id = t2.id
    left join test_group t3
    on t1.test_group_id = t3.id
    where
    t1.user_id = #{userId}
    and
    DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(FROM_UNIXTIME(t1.execute_time / 1000))
    order by t1.execute_time desc
    ''')
    List<TaskRecord> listTaskRecords(@Param('userId') String userId)
}