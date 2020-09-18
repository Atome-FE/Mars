package com.syc.suda.mapper

import com.syc.suda.entity.DataMaterial
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface DataMaterialMapper {

    @Insert('''
    insert into data_material(
    id,
    `name`,
    `describe`,
    group_name,
    second_group_name,
    `data`,
    user_id)
    values(
    #{id},
    #{name},
    #{describe},
    #{groupName},
    #{secondGroupName},
    #{data},
    #{userId}
    )
    ''')
    int save(DataMaterial dataMaterial)

    @Update('''
    update data_material
    set `name` = #{name},
    `describe` = #{describe},
    group_name = #{groupName},
    second_group_name = #{secondGroupName},
    `data` = #{data}
    where
    id = #{id}
    ''')
    int update(DataMaterial dataMaterial)

    @Select('''
    select id,
    `name`,
    `describe`,
    group_name as groupName,
    second_group_name as secondGroupName,
    `data`
    from data_material
    where
    deleted = 0
    ''')
    List<DataMaterial> list(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
    `name`,
    `describe`,
    group_name as groupName,
    second_group_name as secondGroupName,
    `data`
    from data_material
    where
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    ''')
    List<DataMaterial> listByIds(@Param('ids') List<String> ids)

    @Update('''
    update data_material set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

    @Select('''
    select * from data_material
    where id = #{id} and deleted = 0
    ''')
    DataMaterial getById(@Param('id') String id)

}