package com.syc.suda.service.impl

import com.syc.suda.entity.Document
import com.syc.suda.mapper.DocumentMapper
import com.syc.suda.service.DocumentService
import com.syc.suda.util.IdGenerator
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class DocumentServiceImpl implements DocumentService {

    private static final String ID_PREFIX = 'D'

    @Autowired
    DocumentMapper documentMapper

    @Override
    int save(Document document) {
        document.id = IdGenerator.generate(ID_PREFIX)
        return documentMapper.save(document)
    }

    @Override
    int update(Document document) {
        return documentMapper.update(document)
    }

    @Override
    List<Document> listDocument() {
        return documentMapper.listDocument()
    }

    @Override
    int deleteById(String id) {
        return documentMapper.deleteById(id)
    }

    @Override
    Document getById(String id) {
        return documentMapper.getById(id)
    }

    @Override
    int updateDocumentLock(int lock, String id) {
        return documentMapper.updateDocumentLock(lock, id)
    }
}
