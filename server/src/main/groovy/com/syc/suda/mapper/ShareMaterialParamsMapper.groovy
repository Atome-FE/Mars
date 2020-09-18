package com.syc.suda.mapper

import com.syc.suda.entity.ShareMaterialParams
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@CompileStatic
interface ShareMaterialParamsMapper {
    @Insert('''
    insert into share_material_params(
    id,
    share_material_id,
    data_material_id,
    user_id)
    values(
    #{id},
    #{shareMaterialId},
    #{dataMaterialId},
    #{userId}
    )
    ''')
    int save(ShareMaterialParams shareMaterialParams)

    @Update('''
    update share_material_params
    set `data_material_id` = #{dataMaterialId}
    where
    id = #{id}
    ''')
    int update(ShareMaterialParams shareMaterialParams)

    @Select('''
    select * from share_material_params
    where
    share_material_id = #{shareMaterialId}
    and
    user_id = #{userId} and deleted = 0
    ''')
    ShareMaterialParams getByUserIdAndShareMaterialId(@Param('userId') String userId, @Param('shareMaterialId') String shareMaterialId)

    @Update('''
    update share_material_params set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

}