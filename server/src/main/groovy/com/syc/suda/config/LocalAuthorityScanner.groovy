package com.syc.suda.config

import com.syc.suda.enums.AvailabilityStatusEnum
import com.syc.suda.mapper.CommonSecurityMapper
import com.syc.suda.model.CommonAdmin
import com.syc.suda.model.CommonAuthority
import com.syc.suda.model.CommonRole
import com.syc.suda.model.CommonRoleAuthority
import com.syc.suda.service.CommonSecurityService
import com.syc.suda.util.IdGenerator
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Slf4j
@Component
@CompileStatic
class LocalAuthorityScanner {
    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping
    @Autowired
    CommonSecurityMapper commonSecurityMapper
    @Autowired
    CommonSecurityService commonSecurityService

    List<CommonAuthority> findLocalAuthorities() throws IllegalStateException {
        List<CommonAuthority> list = requestMappingHandlerMapping.handlerMethods.collect {
            RequestMappingInfo requestMappingInfo = it.key
            HandlerMethod handlerMethod = it.value
            if (requestMappingInfo.patternsCondition.patterns.size() != 1) {
                throw new IllegalStateException("You should not annotate empty or multiple patterns to the same method: [${it}]")
            }

            String methodName = handlerMethod.method.name
            String authorityUrl = requestMappingInfo.patternsCondition.patterns.first()
            Set methods = requestMappingInfo.methodsCondition.methods
            String method = methods ? methods.first() : ''

            return new CommonAuthority(name: methodName, authority: "${method}:${authorityUrl}", method: method)
        }
        return list
    }

    List<CommonRole> initFirstAdmin() {
        CommonRole adminRole = new CommonRole(name: 'ADMIN', status: AvailabilityStatusEnum.ENABLED)
        commonSecurityMapper.saveRole(adminRole)

        CommonRole normalRole = new CommonRole(name: 'NORMAL', status: AvailabilityStatusEnum.ENABLED)
        commonSecurityMapper.saveRole(normalRole)

        List<CommonRole> roles = [adminRole, normalRole]

        CommonAdmin admin = new CommonAdmin(password: '123456', email: 'admin', username: 'admin', status: AvailabilityStatusEnum.ENABLED)
        commonSecurityService.saveAdmin(admin, roles.collect { it.id })

        return roles
    }

    List<CommonAuthority> updateCommonAuthorityTable(List<CommonAuthority> commonAuthorities) {
        def savedAuthorities = commonSecurityMapper.listAuthorities(null)
        def intersectAuthorities = commonAuthorities.intersect(savedAuthorities as Iterable<CommonAuthority>)

        def deprecatedAuthorities = savedAuthorities - intersectAuthorities
        def newAuthorities = commonAuthorities - savedAuthorities

        deprecatedAuthorities.each {
            if (!it.description?.startsWith('Deprecated')) {
                it.description = "Deprecated since ${new Date()}"
                commonSecurityMapper.updateAuthority(it)
            }
        }

        List<CommonRole> roles = initFirstAdmin()

        List<CommonRoleAuthority> commonRoleAuthorities = []

        newAuthorities.each { authority ->
            if (!authority.authority.startsWith(':/error')) {
                authority.description = "Added since ${new Date()}"
                commonSecurityMapper.saveAuthority(authority)
                roles.each {
                    CommonRoleAuthority commonRoleAuthority = new CommonRoleAuthority(roleId: it.id, authorityId: authority.id)
                    commonRoleAuthorities << commonRoleAuthority
                }
            }
        }

        if (commonRoleAuthorities) {
            commonSecurityMapper.saveRoleAuthorities(commonRoleAuthorities)
        }

        return commonAuthorities
    }
}

