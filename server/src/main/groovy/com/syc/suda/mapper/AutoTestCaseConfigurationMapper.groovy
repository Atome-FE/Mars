package com.syc.suda.mapper

import com.syc.suda.dto.AutomatedTestCaseConfigurationDTO
import com.syc.suda.entity.AutoTestCaseConfiguration
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
@CompileStatic
interface AutoTestCaseConfigurationMapper {

    @Insert("insert into auto_test_case_configuration(business_line,ssh_visit, absolute_dir, main_commend,relative_dir,parameter) values (#{businessLine}, #{sshVisit}, #{absoluteDir}, #{mainCommend}, #{relativeDir}, #{parameter})")
    int add(AutoTestCaseConfiguration autoTestCaseConfiguration)

    @Update("update auto_test_case_configuration set deleted = 1 where id = #{id}")
    int delete(int id)

    @Update('''update auto_test_case_configuration set business_line = #{businessLine}, ssh_visit = #{sshVisit}, 
            absolute_dir = #{absoluteDir}, main_commend = #{mainCommend}, relative_dir = #{relativeDir}, 
            parameter = #{parameter} where id = #{id}''')
    int update(AutoTestCaseConfiguration autoTestCaseConfiguration)

    @Select('''select * from auto_test_case_configuration where business_line = #{businessLine} and deleted = 0''')
    ArrayList<AutoTestCaseConfiguration> getAutoTestCaseConfigurationByBusinessLine(@Param("businessLine")String businessLine)

    @Select('''select distinct business_line from auto_test_case_configuration where deleted = 0''')
    ArrayList<String> getAllBusinessLine()

    @Update('''update auto_test_case_configuration set business_line = #{newName} where business_line = #{oldName} ''')
    int updateBusinessLineName(@Param("newName") String newBusinessLineName, @Param("oldName") String oldBusinessLineName)
}