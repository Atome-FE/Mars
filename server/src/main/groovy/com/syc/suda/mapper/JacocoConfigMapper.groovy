package com.syc.suda.mapper

import com.syc.suda.entity.AutoTestCaseConfiguration
import com.syc.suda.entity.JacocoDto
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

@Mapper
@CompileStatic
interface JacocoConfigMapper {

    @Insert('''insert into jacoco_config(ip, ports, project_directory,report_directory,ssh_identity,ssh_known_host,branch_name,new_tag,old_tag,remote_uri,need_compile,deleted) 
               values (#{ip}, #{ports}, #{projectDirectory}, #{reportDirectory}, #{identity}, #{knownHost}, #{branchName}, #{newTag}, #{oldTag}, #{remoteUri}, #{needCompile}, 
               #{deleted})''')
    int add(JacocoDto jacocoDto)

    @Update("update jacoco_config set deleted = 1 where id = #{id}")
    int delete(int id)

    @Update('''update jacoco_config set ip = #{ip}, ports = #{ports}, project_directory = #{projectDirectory}, report_directory = #{reportDirectory}, ssh_identity = #{identity}, 
             ssh_known_host = #{knownHost}, branch_name = #{branchName}, new_tag = #{newTag},old_tag = #{oldTag}, remote_uri = #{remoteUri}, need_compile = #{needCompile} 
             where id = #{id}''')
    int update(JacocoDto jacocoDto)

    @Update("update jacoco_config set need_compile = #{needCompile} where id = #{id}")
    int updateIsNeedCompile(@Param("id") int id, @Param("needCompile") int needCompile)

    @Update("update jacoco_config set branch_name = #{branchName},new_tag = #{newTag},old_tag = #{oldTag},need_compile = #{needCompile} where id = #{id}")
    int updateJacocoBranch(@Param("id") int id, @Param("branchName") String branchName,@Param("newTag") String newTag, @Param("oldTag") String oldTag,@Param("needCompile") int needCompile)
}