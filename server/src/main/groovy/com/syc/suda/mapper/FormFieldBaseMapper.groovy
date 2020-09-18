package com.syc.suda.mapper

import com.syc.suda.entity.FormField
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select

interface FormFieldBaseMapper {

    @Insert('''
    insert into form_field(id, field_category_id, field_value)
    values(#{id}, #{fieldCategoryId}, #{fieldValue})
    ''')
    @Options(useGeneratedKeys = true, keyProperty = 'id')
    int save(FormField formField)

    @Select('''
    select id,
    field_category_id as fieldCategoryId,
    field_value as fieldValue
    from form_field
    ''')
    List<FormField> listFormField()

}