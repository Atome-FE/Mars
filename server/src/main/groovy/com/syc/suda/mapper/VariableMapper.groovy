package com.syc.suda.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface VariableMapper {
    @Select("select value from variable where name = #{name}")
    String getValueByName(String name);

    @Update("update variable set value = #{value} where name = #{name}")
    int setValue(@Param("name") String name, @Param("value")String value);
}