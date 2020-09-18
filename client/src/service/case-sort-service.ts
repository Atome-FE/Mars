import { ITaskParams } from '@/interfaces/task-interface'

const keys = ['paramIndex', 'extendIndexs']

const resetValue = (task: any, key: string, value: number, i: number) => {
  task[key] = String(value)
  if (i <= value) {
    window.vue.$notify.warning({
      title: '警告',
      message: `任务：${task.name} 更新关联索引报错`,
    })
  }
}

const handleInsert = (task: any, index: number, i: number) => {
  keys.forEach(key => {
    if (task[key]) {
      const value = Number(task[key])
      if (value >= index) {
        resetValue(task, key, value + 1, i)
      }
    }
  })
}

export const updateRelativeOnAdded = (tasks: ITaskParams[], index: number) => {
  for (let i = index + 1; i < tasks.length; i++) {
    handleInsert(tasks[i], index, i)
  }
}

const handleMove = (task: any, newIndex: number, oldIndex: number, i: number) => {
  keys.forEach(key => {
    const isUp = newIndex < oldIndex
    if (task[key]) {
      const value = Number(task[key])
      if (value === oldIndex) {
        resetValue(task, key, newIndex, i)
      } else if (isUp) {
        if (value >= newIndex && value < oldIndex) {
          resetValue(task, key, value + 1, i)
        }
      } else if (value > oldIndex && value <= newIndex) {
        resetValue(task, key, value - 1, i)
      }
    }
  })
}

export const updateRelativeOnMoved = (tasks: ITaskParams[], newIndex: number, oldIndex: number) => {
  const startIndex = newIndex < oldIndex ? newIndex : oldIndex
  for (let i = startIndex; i < tasks.length; i++) {
    handleMove(tasks[i], newIndex, oldIndex, i)
  }
}
