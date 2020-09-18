package com.syc.suda.service

import com.syc.suda.entity.FieldCategory

interface FieldCategoryService {

    int save(FieldCategory fieldCategory)

    List<FieldCategory> listFieldCategory()

}