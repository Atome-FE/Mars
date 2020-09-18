package com.syc.suda.mapper

import com.syc.suda.entity.TestGroupCase
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface TestGroupCaseBaseMapper {

    @Insert('''
    insert into test_group_case(id, user_id,
    test_group_id,
    test_case_id, delay,
    assert_condition, pre_condition,
    post_condition,
    environment,
    priority)
    values(#{id}, #{userId},
    #{testGroupId},
    #{testCaseId}, #{delay},
    #{assertCondition}, #{preCondition},
    #{postCondition},
    #{environment},
    #{priority})
    ''')
    int save(TestGroupCase testGroupCase)

    @Select('''
    select id,
    user_id as userId,
    test_group_id as testGroupId,
    test_case_id as testCaseId,
    delay,
    assert_condition as assertCondition,
    pre_condition as preCondition,
    post_condition as postCondition,
    priority
    from test_group_case
    where user_id = #{userId} and deleted = 0
    ''')
    List<TestGroupCase> listTestGroupCase(@Param('userId') String userId)

    @Update('''
    update test_group_case set deleted = 1
    where id = #{id} 
    ''')
    int deleteById(@Param('id') String id)

    @Update('''
    update test_group_case
    set priority = #{priority}
    where id = #{id}
    ''')
    int updatePriorityById(@Param('id') String id, @Param('priority') int priority)

}