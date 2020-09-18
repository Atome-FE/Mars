package com.syc.suda.filter

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.enums.AvailabilityStatusEnum
import com.syc.suda.enums.ResponseCodeEnum
import com.syc.suda.model.CommonAdmin
import com.syc.suda.model.CommonUserDetails
import com.syc.suda.service.CommonSecurityService
import com.syc.suda.util.JsonUtil
import com.syc.suda.util.JwtUtils
import com.syc.suda.util.jwt.CookieUtils
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.AntPathMatcher

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@CompileStatic
class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils

    @Autowired
    CommonSecurityService commonSecurityService

    private static final String[] excludePathPatterns = [
            '/auth/register',
            '/auth/login',
            '/admins/me',
            '/service-mapping/all'
    ]

    private static final AntPathMatcher pathMatcher = new AntPathMatcher()

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 将 ServletRequest 转换为 HttpServletRequest 才能拿到请求头中的 token
        HttpServletRequest httpRequest = (HttpServletRequest) request
        String token = CookieUtils.getRaw(Constants.TOKEN_KEY) ?: httpRequest.getHeader(Constants.TOKEN_KEY)
        boolean isValidToken = jwtUtils.isValid(token)
        if (!authRequired(httpRequest)) {
            chain.doFilter(request, response)
        } else if (isValidToken) {
            // 解析token
            SessionInfo sessionInfo = jwtUtils.decode(token)
            request.setAttribute(Constants.SESSION_KEY, sessionInfo)
            // 刷新token
            jwtUtils.refresh(token)
            jwtUtils.saveAccessToken(token)
            // 如果上面解析 token 成功并且有效，并且本次会话的权限还未被写入
            // TODO 下面的代码需要删除
            SecurityContextHolder.getContext().setAuthentication(null)
            if (!SecurityContextHolder.getContext().getAuthentication()) {
                String username = sessionInfo.username
                CommonUserDetails userDetails = commonSecurityService.getAdminDetailsByUsername(username)
                if (userDetails == null) {
                    CommonAdmin newUser = new CommonAdmin(
                            username: username,
                            email: username,
                            realName: username,
                            status: AvailabilityStatusEnum.ENABLED,
                    )
                    commonSecurityService.saveAdmin(newUser, null)
                    userDetails = commonSecurityService.getAdminDetailsByUsername(username)
                }
                // 生成通过认证
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest))
                // 将权限写入本次会话
                SecurityContextHolder.getContext().setAuthentication(authentication)
            }
            chain.doFilter(request, response)
        } else {
            response.setContentType('application/json; charset=UTF-8')
            response.getWriter().print(JsonUtil.toString(ResponseBean.fail(ResponseCodeEnum.TOKEN_EXPIRED)))
        }
    }

    private static boolean authRequired(HttpServletRequest request) throws ServletException {
        String lookupPath = request.getServletPath()
        System.out.println(lookupPath)
        for (String pattern : excludePathPatterns) {
            if (pathMatcher.match(pattern, lookupPath))
                return false
        }
        return true
    }
}

