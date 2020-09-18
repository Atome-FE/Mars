package com.syc.suda.mapper

import com.syc.suda.entity.RedisMaterial
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface RedisMaterialMapper {

    @Insert('''
    insert into redis_material(
    id,
    `name`,
    action,
    param_replace,
    `key`,
    user_id,
    second_group_name,
    group_name,
    environment,
    `datasource`,
    `database`)
    values(
    #{id},
    #{name},
    #{action},
    #{paramReplace},
    #{key},
    #{userId},
    #{secondGroupName},
    #{groupName},
    #{environment},
    #{datasource},
    #{database})
    ''')
    int save(RedisMaterial redisMaterial)

    @Update('''
    update redis_material
    set `name` = #{name},
    param_replace = #{paramReplace},
    action = #{action},
    `key` = #{key},
    group_name = #{groupName},
    second_group_name = #{secondGroupName},
    `datasource` = #{datasource},
    `database` = #{database}
    where id = #{id}
    ''')
    int update(RedisMaterial redisMaterial)

    @Select('''
    select id,
    `name`,
    action,
    `key`,
    param_replace as paramReplace,
    `datasource`,
    group_name as groupName,
    second_group_name as secondGroupName,
    `database`
    from redis_material
    where
    deleted = 0
    ''')
    List<RedisMaterial> listRedisMaterial(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
    `name`,
    action,
    `key`,
    param_replace as paramReplace,
    `datasource`,
    group_name as groupName,
    second_group_name as secondGroupName,
    `database`
    from redis_material
    where
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    
    ''')
    List<RedisMaterial> listByIds(@Param('ids') List<String> ids)

    @Update('''
    update redis_material set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

    @Select('''
    select * from redis_material
    where id = #{id} and deleted = 0
    ''')
    RedisMaterial getById(@Param('id') String id)

}