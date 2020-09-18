package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.Document
import com.syc.suda.entity.Env
import com.syc.suda.service.DocumentService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CompileStatic
@RequestMapping('/document')
class DocumentController {

    @Autowired
    DocumentService documentService

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody Document document) {
        document.userId = sessionInfo.id
        documentService.save(document)
        return ResponseBean.success(document)
    }

    @GetMapping
    ResponseBean listDocument() {
        return ResponseBean.success(documentService.listDocument())
    }

    @GetMapping('/{id}')
    ResponseBean getById(@PathVariable('id') String id) {
        return ResponseBean.success(documentService.getById(id))
    }

    @PutMapping('/{id}')
    ResponseBean updateLock(@PathVariable('id') String id, @RequestParam('lock') int lock) {
        documentService.updateDocumentLock(lock, id)
        return ResponseBean.success()
    }

    @PutMapping
    ResponseBean update(@RequestBody Document document) {
        documentService.update(document)
        return ResponseBean.success(document)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody Env env) {
        documentService.deleteById(env.id)
        return ResponseBean.success()
    }
}
