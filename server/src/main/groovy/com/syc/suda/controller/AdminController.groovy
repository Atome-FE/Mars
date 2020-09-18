package com.syc.suda.controller

import com.syc.suda.dto.AdminWithRolesDTO
import com.syc.suda.dto.BackendUserDTO
import com.syc.suda.dto.RoleWithAuthoritiesDTO
import com.syc.suda.enums.ResponseCodeEnum
import com.syc.suda.exception.AccountExistException
import com.syc.suda.exception.AuthorizationCustomException
import com.syc.suda.service.AdminService
import com.syc.suda.util.ContextHolder
import com.syc.suda.util.JsonUtil
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@CompileStatic
@RestController
@RequestMapping('/admin')
class AdminController {
    @Autowired
    AdminService adminService

    @GetMapping('/me')
    ResponseBean getMe() {
        return ResponseBean.success(new BackendUserDTO(ContextHolder.currentUser))
    }

    @GetMapping('/authorities')
    ResponseBean getAuthorities() {
        return ResponseBean.success(adminService.getAuthorities())
    }

    @GetMapping('/roles')
    ResponseBean getRoles() {
        return ResponseBean.success(adminService.getRoles())
    }

    @GetMapping('/roles/{roleId}')
    ResponseBean getRole(@PathVariable('roleId') Long roleId) {
        return ResponseBean.success(adminService.getRoleById(roleId))
    }

    @PostMapping('/roles')
    ResponseBean addRole(@RequestBody RoleWithAuthoritiesDTO role) {
        try {
            adminService.saveRole(role)
        } catch (AuthorizationCustomException e) {
            return ResponseBean.fail(ResponseCodeEnum.ERROR, e.message)
        }
        return ResponseBean.success()
    }

    @PostMapping('/roles/{roleId}/update')
    ResponseBean updateRole(@PathVariable('roleId') Long roleId, @RequestBody RoleWithAuthoritiesDTO role) {
        try {
            log.info("admin : {}, update role: {}, role-authorities: {}",
                    ContextHolder.userName, roleId, JsonUtil.toString(role))
            adminService.updateRole(roleId, role)
        } catch (AuthorizationCustomException e) {
            return ResponseBean.fail(ResponseCodeEnum.ERROR, e.message)
        }
        return ResponseBean.success()
    }

    @PostMapping('/roles/{roleId}/delete')
    ResponseBean deleteRole(@PathVariable('roleId') Long roleId) {
        adminService.removeRoleById(roleId)
        return ResponseBean.success()
    }

    @GetMapping('/users')
    ResponseBean getAdmins() {
        return ResponseBean.success(adminService.getAdmins())
    }

    @GetMapping('/users/{userId}')
    ResponseBean getAdmin(@PathVariable('userId') String userId) {
        return ResponseBean.success(adminService.getAdminById(userId))
    }

    @PostMapping('/users')
    ResponseBean addAdmin(@RequestBody AdminWithRolesDTO admin) {
        try {
            return ResponseBean.success(adminService.saveAdmin(admin))
        } catch (AccountExistException e) {
            return ResponseBean.fail(ResponseCodeEnum.ERROR, e.message)
        } catch (AuthorizationCustomException e) {
            return ResponseBean.fail(ResponseCodeEnum.ERROR, e.message)
        } catch (Exception ignore) {
            ignore.printStackTrace()
            return ResponseBean.error()
        }
    }

    @PostMapping('/users/{userId}/update')
    ResponseBean updateAdmin(@PathVariable('userId') String userId, @RequestBody AdminWithRolesDTO admin) {
        try {
            adminService.updateAdmin(userId, admin)
        } catch (AuthorizationCustomException e) {
            return ResponseBean.fail(ResponseCodeEnum.ERROR, e.message)
        }
        return ResponseBean.success()
    }
}
