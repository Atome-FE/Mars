package com.syc.suda.service

import com.syc.suda.entity.Document

interface DocumentService {

    int save(Document document)

    int update(Document document)

    List<Document> listDocument()

    int deleteById(String id)

    Document getById(String id)

    int updateDocumentLock(int lock, String id)

}