import { instance as ajax } from '@/lib/http'
import { IDocument } from '@/interfaces/document-interface'

const url = '/document'

export const fetchDocuments = () => ajax.get<IDocument[]>(url)

export const fetchDocument = (id: string) => ajax.get<IDocument>(`${url}/${id}`)

export const saveDocument = (payload: IDocument) => ajax.post<IDocument>(url, payload)

export const updateDocument = (payload: IDocument) => ajax.put<IDocument>(url, payload)

export const updateDocumentLock = (payload: any) => ajax.put<void>(`${url}/${payload.id}?lock=${payload.lock}`)

export const deleteDocment = (payload: IDocument) => ajax.delete(url, { data: payload })
