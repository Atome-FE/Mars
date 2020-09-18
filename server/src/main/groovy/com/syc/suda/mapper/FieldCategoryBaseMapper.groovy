package com.syc.suda.mapper

import com.syc.suda.entity.FieldCategory
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select

interface FieldCategoryBaseMapper {

    @Insert('''
    insert into field_category(id, data_type, category)
    values(#{id}, #{dataType}, #{category})
    ''')
    @Options(useGeneratedKeys = true, keyProperty = 'id')
    int save(FieldCategory fieldCategory)

    @Select('''
    select id, 
    data_type as dataType,
    category
    from field_category
    ''')
    List<FieldCategory> listFieldCategory()

}