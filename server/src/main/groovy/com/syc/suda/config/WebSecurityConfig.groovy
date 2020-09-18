package com.syc.suda.config

import com.syc.suda.filter.AuthenticationTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.access.ExceptionTranslationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LocalAuthorityScanner localAuthorityGenerator

    private static final String DELIMITER = ':'

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        def registry = http.authorizeRequests()
        localAuthorityGenerator.findLocalAuthorities().with {
            localAuthorityGenerator.updateCommonAuthorityTable(it)
        }.each {
            String[] arr = it.authority.split(DELIMITER)
            if (arr[0])
                registry.antMatchers(HttpMethod.valueOf(arr[0]), arr[1]).hasAuthority(it.authority)
            else
                registry.antMatchers(arr[1]).hasAuthority(it.authority)
        }
        // 开发登录接口
        http.authorizeRequests().antMatchers(HttpMethod.POST, '/auth/login').permitAll()
        http.authorizeRequests().antMatchers(HttpMethod.GET, '/service-mapping/all').permitAll()

        registry.anyRequest().authenticated()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        // 允许匿名访问
        // http.anonymous().disable()
        // 过滤器
        http.addFilterAfter(authenticationTokenFilterBean(), ExceptionTranslationFilter.class)
    }

    @Bean
    AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter()
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean())
        return authenticationTokenFilter
    }
}

