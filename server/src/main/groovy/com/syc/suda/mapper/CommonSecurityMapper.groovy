package com.syc.suda.mapper

import com.syc.suda.enums.AvailabilityStatusEnum
import com.syc.suda.model.CommonAdmin
import com.syc.suda.model.CommonAdminRole
import com.syc.suda.model.CommonAuthority
import com.syc.suda.model.CommonRole
import com.syc.suda.model.CommonRoleAuthority
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@CompileStatic
interface CommonSecurityMapper {
    @Insert('''
    INSERT IGNORE INTO `common_admin` (
        id, username, password, email, real_name, mobile, status, create_timestamp, update_timestamp
    )
    VALUES (
        #{id},
        #{username},
        #{password},
        #{email},
        #{realName},
        #{mobile},
        #{status},
        ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000),
        ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000)
    )
    ''')
    int saveAdmin(CommonAdmin admin)

    @Select('''
    SELECT * FROM `common_admin`
    WHERE email = #{email}
    AND password = #{password}
    ''')
    CommonAdmin getByEmailAndPassword(@Param('email') String email, @Param('password') String password)

    @Delete('''
    <script>
        DELETE FROM `common_admin`
        WHERE `id` IN
        <foreach collection="adminIds" item="adminId" open="(" close=")" separator=",">
            #{adminId}
        </foreach>
    </script>
    ''')
    int removeAdmins(@Param('adminIds') Collection<String> adminIds)

    @Update('''
    <script>
        UPDATE `common_admin`
        SET `id` = #{id}
        <if test="username != null">
            ,`username` = #{username}
        </if>
        <if test="password != null">
            ,`password` = #{password}
        </if>
        <if test="email != null">
            ,`email` = #{email}
        </if>
        <if test="realName != null">
            ,`real_name` = #{realName}
        </if>
        <if test="mobile != null">
            ,`mobile` = #{mobile}
        </if>
        <if test="status != null">
            ,`status` = #{status}
        </if>
        WHERE `id` = #{id}
    </script>
    ''')
    int updateAdmin(CommonAdmin admin)

    @Select('''
    <script>
        SELECT
        id, username, password, email, real_name, mobile, status, create_timestamp, update_timestamp
        FROM `common_admin`
        WHERE 1 = 1
        <if test="adminIds != null and !adminIds.isEmpty()">
            AND `id` IN
            <foreach collection="adminIds" item="adminId" open="(" close=")" separator=",">
                #{adminId}
            </foreach>
        </if>
        <if test="usernames != null and !usernames.isEmpty()">
            AND `username` IN
            <foreach collection="usernames" item="username" open="(" close=")" separator=",">
                #{username}
            </foreach>
        </if>
        <if test="realNames != null and !realNames.isEmpty()">
            AND `real_name` IN
            <foreach collection="realNames" item="realName" open="(" close=")" separator=",">
                #{realName}
            </foreach>
        </if>
        <if test="status != null">
            AND `status` = #{status}
        </if>
    </script>
    ''')
    List<CommonAdmin> listAdmins(@Param('adminIds') Collection<String> adminIds,
                                 @Param('usernames') Collection<String> usernames,
                                 @Param('realNames') Collection<String> realNames,
                                 @Param('status') AvailabilityStatusEnum status)

    @Select('''
    <script>
        SELECT DISTINCT
        id, username, password, email, real_name, mobile, status, create_timestamp, update_timestamp
        FROM `common_admin` AS A
        INNER JOIN `common_admin_role` AS AR ON A.`id` = AR.`admin_id`
        WHERE AR.`role_id` IN (0
        <if test="roleIds != null and !roleIds.isEmpty()">
            <foreach collection="roleIds" item="roleId">
                ,#{roleId}
            </foreach>
        </if>
        )
    </script>
    ''')
    List<CommonAdmin> listAdminsByRoleIds(@Param('roleIds') Collection<Long> roleIds)

    @Select('''
    <script>
        SELECT DISTINCT
        id, name, status, description
        FROM `common_role` AS R
        INNER JOIN `common_admin_role` AS AR ON R.`id` = AR.`role_id`
        WHERE AR.`admin_id` IN (
        <if test="adminIds != null and !adminIds.isEmpty()">
            <foreach collection="adminIds" item="adminId" separator=",">
                #{adminId}
            </foreach>
        </if>
        )
    </script>
    ''')
    List<CommonRole> listRolesByAdminIds(@Param('adminIds') Collection<String> adminIds)

    @Insert('''
    <script>
        INSERT IGNORE INTO `common_admin_role` (admin_id, role_id)
        VALUES
        <foreach collection="adminRoles" item="adminRole" separator=",">
            (#{adminRole.adminId}, #{adminRole.roleId})
        </foreach>
    </script>
    ''')
    int saveAdminRoles(@Param('adminRoles') Collection<CommonAdminRole> adminRoles)

    @Delete('''
    <script>
        DELETE FROM `common_admin_role`
        WHERE 1 = 1
        <if test="adminIds != null and !adminIds.isEmpty()">
            AND `admin_id` IN
            <foreach collection="adminIds" item="adminId" open="(" close=")" separator=",">
                #{adminId}
            </foreach>
        </if>
        <if test="roleIds != null and !roleIds.isEmpty()">
            AND `role_id` IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
        </if>
    </script>
    ''')
    int removeAdminRoles(@Param('adminIds') Collection<String> adminIds,
                         @Param('roleIds') Collection<Long> roleIds)

    @Select('''
    <script>
        SELECT
        admin_id, role_id
        FROM `common_admin_role`
        WHERE 1 = 1
        <if test="adminIds != null and !adminIds.isEmpty()">
            AND `admin_id` IN
            <foreach collection="adminIds" item="adminId" open="(" close=")" separator=",">
                #{adminId}
            </foreach>
        </if>
        <if test="roleIds != null and !roleIds.isEmpty()">
            AND `role_id` IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
        </if>
    </script>
    ''')
    List<CommonAdminRole> listAdminRoles(@Param('adminIds') Collection<String> adminIds,
                                         @Param('roleIds') Collection<Long> roleIds)

    @Insert('''
    INSERT IGNORE INTO `common_role` (name, status, description)
    VALUES (#{name}, #{status}, #{description})
    ''')
    @Options(useGeneratedKeys=true, keyProperty = "id")
    int saveRole(CommonRole role)

    @Delete('''
    <script>
        DELETE FROM `common_role`
        WHERE 1 = 1
        <if test="roleIds != null and !roleIds.isEmpty()">
            AND `id` IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
        </if>
    </script>
    ''')
    int removeRoles(@Param('roleIds') Collection<Long> roleIds)

    @Update('''
    <script>
        UPDATE `common_role`
        SET `id` = #{id}
        <if test="name != null">
            ,`name` = #{name}
        </if>
        <if test="status != null">
            ,`status` = #{status}
        </if>
        <if test="description != null">
            ,`description` = #{description}
        </if>
        WHERE `id` = #{id}
    </script>
    ''')
    int updateRole(CommonRole role)


    @Select('''
    <script>
        SELECT
        *
        FROM `common_role`
        WHERE 1 = 1
        <if test="roleIds != null and !roleIds.isEmpty()">
            AND `id` IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
        </if>
        <if test="roleNames != null and !roleNames.isEmpty()">
            AND `name` IN
            <foreach collection="roleNames" item="roleName" open="(" close=")" separator=",">
                #{roleName}
            </foreach>
        </if>
        <if test="status != null">
            AND `status` = #{status}
        </if>
    </script>
    ''')
    List<CommonRole> listRoles(@Param('roleIds') Collection<Long> roleIds,
                               @Param('roleNames') Collection<String> roleNames,
                               @Param('status') AvailabilityStatusEnum status)

    @Select('''
    <script>
        SELECT DISTINCT
        id, name, status, description
        FROM `common_role` AS R
        INNER JOIN `common_role_authority` AS RA ON R.`id` = RA.`role_id`
        WHERE RA.`authority_id` IN (0
        <if test="authorityIds != null and !authorityIds.isEmpty()">
            <foreach collection="authorityIds" item="authorityId">
                ,#{authorityId}
            </foreach>
        </if>
        )
    </script>
    ''')
    List<CommonRole> listRolesByAuthorityIds(@Param('authorityIds') Collection<Long> authorityIds)

    @Insert('''
    <script>
    INSERT IGNORE INTO `common_role_authority` (role_id, authority_id)
    VALUES
    <foreach collection="roleAuthorities" item="roleAuthority" separator=",">
        (#{roleAuthority.roleId}, #{roleAuthority.authorityId})
    </foreach>
    </script>
    ''')
    int saveRoleAuthorities(@Param('roleAuthorities') Collection<CommonRoleAuthority> roleAuthorities)

    @Delete('''
    <script>
        DELETE FROM `common_role_authority`
        WHERE 1 = 1
        <if test="roleIds != null and !roleIds.isEmpty()">
            AND `role_id` IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
        </if>
        <if test="authorityIds != null and !authorityIds.isEmpty()">
            AND `authority_id` IN
            <foreach collection="authorityIds" item="authorityId" open="(" close=")" separator=",">
                #{authorityId}
            </foreach>
        </if>
    </script>
    ''')
    int removeRoleAuthorities(@Param('roleIds') Collection<Long> roleIds,
                              @Param('authorityIds') Collection<Long> authorityIds)

    @Select('''
    <script>
        SELECT
        role_id, authority_id
        FROM `common_role_authority`
        WHERE 1 = 1
        <if test="roleIds != null and !roleIds.isEmpty()">
            AND `role_id` IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
        </if>
        <if test="authorityIds != null and !authorityIds.isEmpty()">
            AND `authority_id` IN
            <foreach collection="authorityIds" item="authorityId" open="(" close=")" separator=",">
                #{authorityId}
            </foreach>
        </if>
    </script>
    ''')
    List<CommonRoleAuthority> listRoleAuthorities(@Param('roleIds') Collection<Long> roleIds,
                                                  @Param('authorityIds') Collection<Long> authorityIds)

    @Insert('''
    INSERT INTO `common_authority` (name, authority, description, method)
    VALUES (#{name}, #{authority}, #{description}, #{method})
    ON DUPLICATE KEY UPDATE
    name        = #{name},
    description = #{description}
    ''')
    @Options(useGeneratedKeys=true, keyProperty = "id")
    int saveAuthority(CommonAuthority authority)

    @Update('''
    update `common_authority`
    set 
    name        = #{name},
    description = #{description}
    where 
    id = #{id}
    ''')
    int updateAuthority(CommonAuthority authority)

    @Select('''
    <script>
        SELECT
        id, name, authority, description, method
        FROM `common_authority`
        <if test="authorityIds != null and !authorityIds.isEmpty()">
            WHERE `id` IN
            <foreach collection="authorityIds" item="authorityId" open="(" close=")" separator=",">
                #{authorityId}
            </foreach>
        </if>
    </script>
    ''')
    List<CommonAuthority> listAuthorities(@Param('authorityIds') Collection<Long> authorityIds)

    @Select('''
    <script>
        SELECT DISTINCT
        id, name, authority, description, method
        FROM `common_authority` AS A
        INNER JOIN `common_role_authority` AS RA ON A.`id` = RA.`authority_id`
        WHERE RA.`role_id` IN (0
        <if test="roleIds != null and !roleIds.isEmpty()">
            <foreach collection="roleIds" item="roleId">
                ,#{roleId}
            </foreach>
        </if>
        )
    </script>
    ''')
    List<CommonAuthority> listAuthoritiesByRoleIds(@Param('roleIds') Collection<Long> roleIds)

    @Select('''
    SELECT LAST_INSERT_ID()
    ''')
    long getLastInsertId()
}

