package com.syc.suda.mapper

import com.syc.suda.entity.SqlMaterial
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface SqlMaterialMapper {

    @Insert('''
    insert into sql_material(
    id,
    user_id,
    environment,
    `name`,
    `datasource`,
    param_replace,
    `database`,
    second_group_name,
    group_name,
    material)
    values(
    #{id},
    #{userId},
    #{environment},
    #{name},
    #{datasource},
    #{paramReplace},
    #{database},
    #{secondGroupName},
    #{groupName},
    #{material})
    ''')
    int save(SqlMaterial sqlMaterial)

    @Update('''
    update sql_material
    set `name` = #{name},
    material = #{material},
    param_replace = #{paramReplace},
    group_name = #{groupName},
    second_group_name = #{secondGroupName},
    `datasource` = #{datasource},
    `database` = #{database}
    where
    id = #{id}
    ''')
    int update(SqlMaterial sqlMaterial)

    @Select('''
    select id,
    `name`,
    material,
    `datasource`,
    param_replace as paramReplace,
    group_name as groupName,
    second_group_name as secondGroupName,
    `database`
    from sql_material
    where
    deleted = 0
    ''')
    List<SqlMaterial> listSqlMaterials(@Param('userId') String userId)

    @Select('''
    <script>
    select id,
    `name`,
    material,
    `datasource`,
    param_replace as paramReplace,
    group_name as groupName,
    second_group_name as secondGroupName,
    `database`
    from sql_material
    where
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    
    ''')
    List<SqlMaterial> listByIds(@Param('ids') List<String> ids)

    @Update('''
    update sql_material set deleted = 1
    where id = #{id} 
    ''')
    int deleteById(@Param('id') String id)

    @Select('''
    select * from sql_material
    where id = #{id} and deleted = 0
    ''')
    SqlMaterial getById(@Param('id') String id)

}