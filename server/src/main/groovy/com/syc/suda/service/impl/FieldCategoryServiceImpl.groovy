package com.syc.suda.service.impl

import com.syc.suda.entity.FieldCategory
import com.syc.suda.exception.FieldCategoryExistException
import com.syc.suda.mapper.FieldCategoryBaseMapper
import com.syc.suda.service.FieldCategoryService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FieldCategoryServiceImpl implements FieldCategoryService {

    private static final String ID_PREFIX = 'F'

    @Autowired
    FieldCategoryBaseMapper fieldCategoryBaseMapper

    @Override
    int save(FieldCategory fieldCategory) {
        List<FieldCategory> fieldCategories = fieldCategoryBaseMapper.listFieldCategory()
        FieldCategory result = fieldCategories.find {
            return it.category == fieldCategory.category
        }
        if (result) {
            throw new FieldCategoryExistException('字段类别已经存在')
        }
        fieldCategory.id = IdGenerator.generate(ID_PREFIX)
        return fieldCategoryBaseMapper.save(fieldCategory)
    }

    @Override
    List<FieldCategory> listFieldCategory() {
        return fieldCategoryBaseMapper.listFieldCategory()
    }
}
