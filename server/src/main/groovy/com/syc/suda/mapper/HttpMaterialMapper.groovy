package com.syc.suda.mapper

import com.syc.suda.entity.HttpMaterial
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface HttpMaterialMapper {

    @Insert('''
    insert into http_material(
    id,
    user_id,
    environment,
    url,
    `name`,
    data_type,
    method,
    headers,
    data_handle_type,
    params,
    param_replace,
    group_name,
    second_group_name,
    `data`)
    values(
    #{id},
    #{userId},
    #{environment},
    #{url},
    #{name},
    #{dataType},
    #{method},
    #{headers},
    #{dataHandleType},
    #{params},
    #{paramReplace},
    #{groupName},
    #{secondGroupName},
    #{data})
    ''')
    int save(HttpMaterial httpMaterial)

    @Select('''
    select id,
    user_id as userId,
    url,
    param_replace as paramReplace,
    `name`,
    data_type as dataType,
    method,
    headers,
    params,
    group_name as groupName,
    second_group_name as secondGroupName,
    data_handle_type as dataHandleType,
    `data`
    from http_material
    where
    deleted = 0
    ''')
    List<HttpMaterial> listHttpMaterial(@Param('userId') String userId)

    @Select('''
    <script>
        select id,
        user_id as userId,
        url,
        param_replace as paramReplace,
        `name`,
        data_type as dataType,
        method,
        headers,
        params,
        group_name as groupName,
        second_group_name as secondGroupName,
        data_handle_type as dataHandleType,
        `data`
        from http_material
        where
        `id` IN (
            <foreach collection="ids" item="id" separator=",">
                #{id}
            </foreach>
        ) and deleted = 0
    </script>
    ''')
    List<HttpMaterial> listByIds(@Param('ids') List<String> ids)

    @Select('''
    select * from http_material
    where id = #{id} and deleted = 0
    ''')
    HttpMaterial getById(@Param('id') String id)

    @Update('''
    update http_material
    set `name` = #{name},
    group_name = #{groupName},
    second_group_name = #{secondGroupName},
    param_replace = #{paramReplace},
    data_type = #{dataType},
    method = #{method},
    url = #{url},
    data_handle_type = #{dataHandleType},
    headers = #{headers},
    params = #{params},
    `data` = #{data}
    where id = #{id}
    ''')
    int update(HttpMaterial httpMaterial)

    @Update('''
    update http_material set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)
}