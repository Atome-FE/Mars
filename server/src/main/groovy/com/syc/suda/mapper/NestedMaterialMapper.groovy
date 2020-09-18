package com.syc.suda.mapper

import com.syc.suda.entity.NestedMaterial
import com.syc.suda.enums.MaterialType
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface NestedMaterialMapper {
    @Insert('''
    insert into nested_material(id, user_id, name, parent_id, material_id, material_type, type)
    values(#{id}, #{userId}, #{name}, #{parentId}, #{materialId}, #{materialType}, #{type})
    ''')
    int save(NestedMaterial nestedMaterial)

    @Select('''
    select * from nested_material
    where material_type = #{materialType}
    and user_id = #{userId} and deleted = 0
    ''')
    List<NestedMaterial> listByMaterialType(@Param('materialType') MaterialType materialType, @Param('userId') String userId)

    @Select('''
    select * from nested_material
    where user_id = #{userId} and deleted = 0
    ''')
    List<NestedMaterial> listAll(@Param('userId') String userId)

    @Update('''
    update nested_material set deleted = 1
    where id = #{id}
    ''')
    int delete(@Param('id') String id)

    @Update('''
    update nested_material
    set name = #{name},
    parent_id = #{parentId}
    where id = #{id}
    ''')
    int update(NestedMaterial nestedMaterial)

    @Select('''
    select * from nested_material
    where material_id = #{materialId} and deleted = 0
    ''')
    NestedMaterial getByMaterialId(@Param('materialId') String materialId)

    @Select('''
    select * from nested_material
    where user_id = #{userId} and deleted = 0
    ''')
    NestedMaterial getByUserId(@Param('userId') String userId)
}