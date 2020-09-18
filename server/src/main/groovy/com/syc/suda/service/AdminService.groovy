package com.syc.suda.service

import com.syc.suda.dto.AdminWithRolesDTO
import com.syc.suda.dto.RoleWithAuthoritiesDTO
import com.syc.suda.model.CommonAuthority
import groovy.transform.CompileStatic

@CompileStatic
interface AdminService {
    List<CommonAuthority> getAuthorities()

    List<RoleWithAuthoritiesDTO> getRoles()

    RoleWithAuthoritiesDTO getRoleById(Long roleId)

    AdminWithRolesDTO getAdminById(String adminId)

    void saveRole(RoleWithAuthoritiesDTO role)

    void updateRole(Long roleId, RoleWithAuthoritiesDTO role)

    void removeRoleById(Long roleId)

    List<AdminWithRolesDTO> getAdmins()

    String saveAdmin(AdminWithRolesDTO admin)

    void updateAdmin(String adminId, AdminWithRolesDTO admin)
}
