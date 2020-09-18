package com.syc.suda.mapper

import com.syc.suda.entity.ShareMaterial
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@CompileStatic
interface ShareMaterialMapper {
    @Insert('''
    insert ignore into share_material(
    id,
    `type`,
    material_id,
    shared_user_ids,
    user_id)
    values(
    #{id},
    #{type},
    #{materialId},
    #{sharedUserIds},
    #{userId}
    )
    ''')
    int save(ShareMaterial shareMaterial)

    @Update('''
    update share_material
    set `type` = #{type},
    `shared_user_ids` = #{sharedUserIds}
    where
    id = #{id}
    ''')
    int update(ShareMaterial shareMaterial)

    @Select('''
    select t1.*, t2.email as user_email from share_material t1
    left join common_admin t2 on t2.id = t1.user_id
    where 
    (shared_user_ids IS NULL OR shared_user_ids LIKE CONCAT('%',#{userId},'%')) and deleted = 0
    ''')
    List<ShareMaterial> list(@Param('userId') String userId)

    @Update('''
    update share_material set deleted = 0
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

}