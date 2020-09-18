package com.syc.suda.mapper

import com.syc.suda.entity.AppMapping
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Select

@CompileStatic
interface AppMappingMapper {

    @Select('''
        select id,
        app_name as appName,
        `host` as host,
        app_path as appPath,
        app_start_shell as appStartShell,
        docker_name as dockerName
        from app_mapping
    ''')
    List<AppMapping> listAppMapping()
}