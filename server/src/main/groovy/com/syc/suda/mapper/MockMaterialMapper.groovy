package com.syc.suda.mapper

import com.syc.suda.entity.MockMaterial
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface MockMaterialMapper {
    @Insert('''
    insert into mock_material(
    id,
    user_id,
    `name`,
    material,
    method,
    url
    ) values(
    #{id},
    #{userId},
    #{name},
    #{material},
    #{method},
    #{url})
    ''')
    int save(MockMaterial material)

    @Update('''
    update mock_material
    set
    `name` = #{name},
    material = #{material},
    method = #{method},
    url = #{url}
    where
    id = #{id}
    ''')
    int update(MockMaterial material)

    @Select('''
    select * from mock_material
    where
    user_id = #{userId} and deleted = 0
    ''')
    List<MockMaterial> listMockMaterials(@Param('userId') String userId)

    @Select('''
    <script>
    select *
    from mock_material
    where
    `id` IN (
        <foreach collection="ids" item="id" separator=",">
            #{id}
        </foreach>
    )
    and deleted = 0
    </script>
    ''')
    List<MockMaterial> listByIds(@Param('ids') List<String> ids)

    @Update('''
    update mock_material set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

    @Select('''
    select * from mock_material
    where id = #{id} and deleted = 0
    ''')
    MockMaterial getById(@Param('id') String id)
}