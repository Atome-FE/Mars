package com.syc.suda.mapper

import com.syc.suda.entity.TestGroup
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface TestGroupBaseMapper {

    @Insert('''
    insert into test_group(id, user_id, `name`, environment, material, concurrency)
    values(#{id}, #{userId}, #{name}, #{environment}, #{material}, #{concurrency})
    ''')
    int save(TestGroup testGroup)

    @Select('''
    select id,
    user_id as userId,
    concurrency,
    `name`,
    material,
    environment
    from test_group
    where user_id = #{userId} and deleted = 0
    ''')
    List<TestGroup> listTestGroup(@Param('userId') String userId)

    @Update('''
    update test_group
    set `name` = #{name},
    material = #{material},
    concurrency = #{concurrency}
    where id = #{id}
    ''')
    int update(TestGroup testGroup)

    @Update('''
    update test_group set deleted = 1
    where id = #{id}
    ''')
    int delete(@Param('id') String id)

}