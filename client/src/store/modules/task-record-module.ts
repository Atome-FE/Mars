import { VuexModule, Module, Mutation, getModule } from 'vuex-module-decorators'

import store from '@/store'

import { ITaskRecord } from '@/interfaces/task-interface'

@Module({ dynamic: true, store, name: 'taskRecordModule' })
class TaskRecord extends VuexModule {
  taskRecords: ITaskRecord[] = []

  @Mutation
  addTaskRecord(payload: ITaskRecord) {
    this.taskRecords.push(payload)
  }

  @Mutation
  addAllTaskRecord(payload: ITaskRecord[]) {
    this.taskRecords.push(...payload)
  }

  @Mutation
  removeAllRecords() {
    this.taskRecords.splice(0)
  }
}
export const TaskRecordModule = getModule(TaskRecord)
