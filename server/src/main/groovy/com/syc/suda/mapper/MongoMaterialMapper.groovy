package com.syc.suda.mapper

import com.syc.suda.entity.MongoMaterial
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface MongoMaterialMapper {

    @Insert('''
    insert into mongo_material(id,
        user_id,
        action,
        `name`,
        material,
        datasource,
        `database`,
        group_name,
        second_group_name,
        param_replace,
        `schema`
    ) values(
        #{id},
        #{userId},
        #{action},
        #{name},
        #{material},
        #{datasource},
        #{database},
        #{groupName},
        #{secondGroupName},
        #{paramReplace},
        #{schema}
    )
    ''')
    int save(MongoMaterial mongoMaterial)

    @Update('''
    update mongo_material
    set action = #{action},
        `name` = #{name},
        material = #{material},
        datasource = #{datasource},
        `database` = #{database},
        group_name = #{groupName},
        second_group_name = #{secondGroupName},
        param_replace = #{paramReplace},
        `schema` = #{schema}
    where id = #{id}
    ''')
    int update(MongoMaterial mongoMaterial)

    @Update('''
    update  mongo_material set deleted = 1
    where id = #{id}
    ''')
    int delete(@Param('id') String id)

    @Select('''
    select id,
        action,
        `name`,
        material,
        datasource,
        `database`,
        group_name as groupName,
        second_group_name as secondGroupName,
        param_replace as paramReplace,
        `schema`
    from mongo_material
    where deleted = 0
    ''')
    List<MongoMaterial> listMongoMaterials(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
        action,
        `name`,
        material,
        datasource,
        `database`,
        group_name as groupName,
        second_group_name as secondGroupName,
        param_replace as paramReplace,
        `schema`
    from mongo_material
    where 
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    ''')
    List<MongoMaterial> listByIds(@Param('ids') List<String> ids)

    @Select('''
    select * from mongo_material
    where id = #{id} and deleted = 0
    ''')
    MongoMaterial getById(@Param('id') String id)

}