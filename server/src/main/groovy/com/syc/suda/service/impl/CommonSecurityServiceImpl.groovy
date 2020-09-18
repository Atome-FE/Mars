package com.syc.suda.service.impl

import com.syc.suda.enums.AvailabilityStatusEnum
import com.syc.suda.mapper.CommonSecurityMapper
import com.syc.suda.model.CommonAdmin
import com.syc.suda.model.CommonAdminRole
import com.syc.suda.model.CommonAuthority
import com.syc.suda.model.CommonRole
import com.syc.suda.model.CommonRoleAuthority
import com.syc.suda.model.CommonUserDetails
import com.syc.suda.service.CommonSecurityService
import com.syc.suda.util.IdGenerator
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class CommonSecurityServiceImpl implements CommonSecurityService {
    @Autowired
    CommonSecurityMapper commonSecurityMapper

    static final String ID_PREFIX = 'U'

    @Override
    String saveAdmin(CommonAdmin admin, Collection<Long> roleIds) {
        admin.id = IdGenerator.generate(ID_PREFIX)
        if (!commonSecurityMapper.saveAdmin(admin)) {
            return 0
        }
        updateAdminRoles(admin, roleIds)
        return admin.id
    }

    @Override
    CommonAdmin getByEmailAndPassword(String email, String password) {
        return commonSecurityMapper.getByEmailAndPassword(email, password)
    }

    @Override
    int removeAdminsByIds(Collection<String> adminIds) {
        commonSecurityMapper.removeAdminRoles(adminIds, null)
        return commonSecurityMapper.removeAdmins(adminIds)
    }

    @Override
    int updateAdmin(CommonAdmin admin, Collection<Long> roleIds) {
        if (roleIds != null) {
            updateAdminRoles(admin, roleIds)
        }
        return commonSecurityMapper.updateAdmin(admin)
    }

    @Override
    List<CommonAdmin> listAdmins(Collection<String> adminIds, Collection<String> usernames, Collection<String> realNames, AvailabilityStatusEnum status) {
        return commonSecurityMapper.listAdmins(adminIds, usernames, realNames, status)
    }

    @Override
    List<CommonAdmin> listAdminsByUsernames(Collection<String> usernames) {
        return commonSecurityMapper.listAdmins(null, usernames, null, null)
    }

    @Override
    List<CommonAdmin> listAdminsByRoleIds(Collection<Long> roleIds) {
        return commonSecurityMapper.listAdminsByRoleIds(roleIds)
    }

    @Override
    int saveRole(CommonRole role, Collection<Long> authorityIds) {
        if (!commonSecurityMapper.saveRole(role)) {
            return 0
        }
        role.id = commonSecurityMapper.getLastInsertId()
        updateRoleAuthorities(role, authorityIds)
        return 1
    }

    @Override
    int removeRolesByIds(Collection<Long> roleIds) {
        commonSecurityMapper.removeAdminRoles(null, roleIds)
        commonSecurityMapper.removeRoleAuthorities(roleIds, null)
        return commonSecurityMapper.removeRoles(roleIds)
    }

    @Override
    int updateRole(CommonRole role, Collection<Long> authorityIds) {
        if (authorityIds != null) {
            updateRoleAuthorities(role, authorityIds)
        }
        return commonSecurityMapper.updateRole(role)
    }

    @Override
    List<CommonRole> listRoles(Collection<Long> roleIds, Collection<String> roleNames, AvailabilityStatusEnum status) {
        return commonSecurityMapper.listRoles(roleIds, roleNames, status)
    }

    @Override
    List<CommonRole> listRolesByAdminId(String adminId) {
        if (adminId) {
            return commonSecurityMapper.listRolesByAdminIds([adminId])
        }
        return []
    }

    @Override
    CommonRole getRoleById(Long roleId) {
        List<CommonRole> roles = commonSecurityMapper.listRoles([roleId], null, null)
        return roles ? roles[0] : null
    }

    @Override
    CommonAdmin getAdminById(String adminId) {
        List<CommonAdmin> admins = commonSecurityMapper.listAdmins([adminId], null, null, null)
        return admins ? admins.first() : null
    }

    @Override
    List<CommonAuthority> listAuthorities(Collection<Long> authorityIds) {
        return commonSecurityMapper.listAuthorities(authorityIds)
    }

    @Override
    List<CommonAuthority> listAuthoritiesByRoleIds(Collection<Long> roleIds) {
        return commonSecurityMapper.listAuthoritiesByRoleIds(roleIds)
    }

    @Override
    List<CommonAdminRole> listAdminRolesByAdminIds(List<String> adminIds) {
        return commonSecurityMapper.listAdminRoles(adminIds, null)
    }

    @Override
    List<CommonAdminRole> listAdminRoleByRoleIds(List<Long> roleIds) {
        return commonSecurityMapper.listAdminRoles(null, roleIds)
    }

    @Override
    List<CommonRoleAuthority> listRoleAuthoritiesByRoleIds(List<Long> roleIds) {
        return commonSecurityMapper.listRoleAuthorities(roleIds, null)
    }

    @Override
    List<CommonRoleAuthority> listRoleAuthoritiesByAuthorityIds(List<Long> authorityIds) {
        return commonSecurityMapper.listRoleAuthorities(null, authorityIds)
    }

    @Override
    CommonUserDetails getAdminDetailsByUsername(String username) {
        List<CommonAdmin> admins = commonSecurityMapper.listAdmins(null, [username], null, null)

        if (admins) {
            CommonUserDetails userDetails = new CommonUserDetails(admins[0])
            if (userDetails.status == AvailabilityStatusEnum.ENABLED) {
                userDetails.roles = listRolesByAdminId(userDetails.id)
                        .findAll { it.status == AvailabilityStatusEnum.ENABLED }
                userDetails.authorities = listAuthoritiesByRoleIds(userDetails.roles*.id)
            }
            return userDetails
        }
        return null
    }

    private void updateAdminRoles(CommonAdmin admin, Collection<Long> roleIds) {
        commonSecurityMapper.removeAdminRoles([admin.id], null)
        if (roleIds) {
            List<CommonAdminRole> adminRoles = roleIds.collect {
                new CommonAdminRole(adminId: admin.id, roleId: it)
            }
            commonSecurityMapper.saveAdminRoles(adminRoles)
        }
    }

    private void updateRoleAuthorities(CommonRole role, Collection<Long> authorityIds) {
        commonSecurityMapper.removeRoleAuthorities([role.id], null)
        if (authorityIds) {
            List<CommonRoleAuthority> roleAuthorities = authorityIds.collect {
                new CommonRoleAuthority(roleId: role.id, authorityId: it)
            }
            commonSecurityMapper.saveRoleAuthorities(roleAuthorities)
        }
    }
}

