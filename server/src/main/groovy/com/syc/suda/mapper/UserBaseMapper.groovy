package com.syc.suda.mapper

import com.syc.suda.entity.User
import com.syc.suda.security.EncryptStringField
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface UserBaseMapper {

    User get(@Param("id") String id)

    @Insert('''
    INSERT INTO `user` (
        `id`,
        `mobile_number`,
        `password`
    )
    VALUES (
        #{id},
        #{mobileNumber},
        #{password}
    )
    ''')
    int save(User user)

    int update(User user)

    @Select('''
    SELECT
    `id`,
    mobile_number as mobileNumber,
    `password`,
    create_timestamp as createTimestamp,
    update_timestamp as updateTimestamp
    FROM `user`
    WHERE mobile_number = #{mobileNumber}
    ''')
    User getByMobile(@Param("mobileNumber") String mobileNumber)

}