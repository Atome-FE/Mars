package com.syc.suda.mapper

import com.syc.suda.entity.ServiceHealthCheckMapping
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface ServiceHealthCheckMappingMapper {
    @Insert('''
    insert into service_health_check_mapping(id, user_id, docker_name, cmd, restart_cmd, stop_cmd, environment_name)
    values(#{id}, #{userId}, #{dockerName}, #{cmd}, #{restartCmd}, #{stopCmd}, #{environmentName})
    ''')
    int save(ServiceHealthCheckMapping serviceHealthCheckMapping)

    @Select('''
    select * from service_health_check_mapping
    where user_id = #{userId} and deleted = 0
    ''')
    List<ServiceHealthCheckMapping> listByUserId(@Param('userId') String userId)

    @Select('''
    select * from service_health_check_mapping where deleted = 0
    ''')
    List<ServiceHealthCheckMapping> listAll()

    @Update('''
    update service_health_check_mapping set deleted = 1
    where id = #{id}
    ''')
    int delete(@Param('id') String id)

    @Update('''
    update service_health_check_mapping
    set docker_name = #{dockerName},
    cmd = #{cmd},
    restart_cmd = #{restartCmd},
    stop_cmd = #{stopCmd},
    environment_name = #{environmentName}
    where id = #{id} and deleted = 0
    ''')
    int update(ServiceHealthCheckMapping serviceHealthCheckMapping)

}