package com.syc.suda.service.impl

import com.syc.suda.entity.FormField
import com.syc.suda.mapper.FormFieldBaseMapper
import com.syc.suda.service.FormFieldService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FormFieldServiceImpl implements FormFieldService {

    private static final String ID_PREFIX = 'F'

    @Autowired
    FormFieldBaseMapper formFieldBaseMapper

    @Override
    int save(FormField formField) {
        formField.id = IdGenerator.generate(ID_PREFIX)
        return formFieldBaseMapper.save(formField)
    }

    @Override
    List<FormField> listFormField() {
        return formFieldBaseMapper.listFormField()
    }
}
