package com.syc.suda.mapper

import com.syc.suda.entity.TestCase
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface TestCaseBaseMapper {

    @Insert('''
    insert into test_case(id, `name`, url, method, request_data,
    user_id,
    body,
    environment,
    second_group_name,
    group_name,
    material)
    values(#{id}, #{name}, #{url}, #{method}, #{requestData},
    #{userId},
    #{body},
    #{environment},
    #{secondGroupName},
    #{groupName},
    #{material})
    ''')
    @Options(useGeneratedKeys = true, keyProperty = 'id')
    int save(TestCase testCase)

    @Update('''
    update test_case
    set `name` = #{name},
    group_name = #{groupName},
    second_group_name = #{secondGroupName},
    material = #{material}
    where id = #{id}
    ''')
    int update(TestCase testCase)

    @Select('''
    select id,
    user_id as userId,
    `name`,
    url,
    method,
    material,
    group_name as groupName,
    second_group_name as secondGroupName,
    request_data as requestData,
    body
    from test_case
    where user_id = #{userId} and deleted = 0
    ''')
    List<TestCase> listTestCase(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
    user_id as userId,
    `name`,
    url,
    method,
    material,
    group_name as groupName,
    second_group_name as secondGroupName,
    request_data as requestData,
    body
    from test_case
    where 
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    ''')
    List<TestCase> listByIds(@Param('ids') List<String> ids)

    @Select('''
    select * from test_case
    where
    id = #{id} and deleted = 0
    ''')
    TestCase getById(@Param('id') String id)

    @Update('''
    update test_case set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

}