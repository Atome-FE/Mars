import {
  VuexModule,
  Module,
  Mutation,
  getModule,
} from 'vuex-module-decorators'

import store from '@/store'

import { IDocument } from '@/interfaces/document-interface'

@Module({ dynamic: true, store, name: 'document' })
class Document extends VuexModule {
  documents: IDocument[] = []

  @Mutation
  getDocumentById(id: string) {
    return this.documents.find((v: IDocument) => {
      return v.id === id
    })
  }

  @Mutation
  addDocument(payload: IDocument) {
    this.documents.push(payload)
  }

  @Mutation
  deleteDocument(id: string) {
    const index = this.documents.findIndex((item: IDocument) => {
      return item.id === id
    })
    this.documents.splice(index, 1)
  }

  @Mutation
  addAllDocument(payload: IDocument[]) {
    this.documents.splice(0)
    this.documents.push(...payload)
  }

  @Mutation
  replaceDocument(payload: IDocument) {
    const index = this.documents.findIndex(doc => {
      return doc.id === payload.id
    })
    this.documents[index] = payload
  }

  @Mutation
  updateDocument(payload: IDocument) {
    const index = this.documents.findIndex((item: IDocument) => {
      return item.id === payload.id
    })
    this.documents.splice(index, 1, payload)
  }
}

export const DocumentModule = getModule(Document)

export const getDocumentById = (id: string) => {
  const result = DocumentModule.documents.find((v: IDocument) => {
    return v.id === id
  })
  return { ...result }
}
