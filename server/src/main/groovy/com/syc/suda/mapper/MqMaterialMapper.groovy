package com.syc.suda.mapper

import com.syc.suda.entity.MqMaterial
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface MqMaterialMapper {

    @Insert('''
    insert into mq_material(
    id,
    user_id,
    environment,
    param_replace,
    `name`,
    exchange,
    routing_key,
    second_group_name,
    group_name,
    content
    ) values(
    #{id},
    #{userId},
    #{environment},
    #{paramReplace},
    #{name},
    #{exchange},
    #{routingKey},
    #{secondGroupName},
    #{groupName},
    #{content})
    ''')
    int save(MqMaterial mqMaterial)

    @Update('''
    update mq_material
    set
    `name` = #{name},
    param_replace = #{paramReplace},
    exchange = #{exchange},
    routing_key = #{routingKey},
    group_name = #{groupName},
    second_group_name = #{secondGroupName},
    content = #{content}
    where
    id = #{id}
    ''')
    int update(MqMaterial mqMaterial)

    @Select('''
    select id,
    param_replace as paramReplace,
    `name`,
    exchange,
    group_name as groupName,
    second_group_name as secondGroupName,
    routing_key as routingKey,
    content
    from mq_material
    where
    deleted = 0
    ''')
    List<MqMaterial> listMqMaterials(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
    param_replace as paramReplace,
    `name`,
    exchange,
    group_name as groupName,
    second_group_name as secondGroupName,
    routing_key as routingKey,
    content
    from mq_material
    where
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    ''')
    List<MqMaterial> listByIds(@Param('ids') List<String> ids)

    @Update('''
    update mq_material set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

    @Select('''
    select * from mq_material
    where id = #{id} and deleted = 0
    ''')
    MqMaterial getById(@Param('id') String id)

}