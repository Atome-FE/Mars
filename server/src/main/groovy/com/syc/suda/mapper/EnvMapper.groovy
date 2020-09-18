package com.syc.suda.mapper

import com.syc.suda.entity.Env
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface EnvMapper {

    @Insert('''
    insert into env_config(
    id,
    `name`,
    user_id,
    http,
    `sql`,
    redis,
    mq,
    mongo,
    global_variable,
    `describe`
    )
    values(
    #{id},
    #{name},
    #{userId},
    #{http},
    #{sql},
    #{redis},
    #{mq},
    #{mongo},
    #{globalVariable},
    #{describe}
    )
    ''')
    int save(Env env)

    @Update('''
    update env_config
    set `name` = #{name},
    http = #{http},
    `sql` = #{sql},
    redis = #{redis},
    mq = #{mq},
    mongo = #{mongo},
    global_variable = #{globalVariable},
    `describe` = #{describe}
    where
    id = #{id}
    ''')
    int update(Env env)

    @Select('''
    select id,
    `name`,
    http,
    `sql`,
    redis,
    global_variable,
    mq,
    mongo,
    `describe`
    from env_config
    where
    user_id = #{userId} and deleted = 0
    ''')
    List<Env> listEnv(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
    `name`,
    http,
    `sql`,
    redis,
    global_variable,
    mq,
    mongo,
    `describe`
    from env_config
    where
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0 
    </script>
    ''')
    List<Env> listByIds(@Param('ids') List<String> ids)

    @Update('''
    update env_config set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

    @Select('''
    select * from env_config
    where id = #{id} and deleted = 0
    ''')
    Env getById(@Param('id') String id)
}