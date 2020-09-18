package com.syc.suda.service

import com.syc.suda.entity.FormField

interface FormFieldService {

    int save(FormField formField)

    List<FormField> listFormField()

}