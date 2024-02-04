package com.kairon007.pestotasks.models


class TaskModel {
    var taskId: String? = null
        private set
    var title: String? = null
        private set
    var description: String? = null
        private set
    var status: String? = null
        private set

    constructor()
    constructor(taskId: String?, title: String?, description: String?, status: String?) {
        this.taskId = taskId
        this.title = title
        this.description = description
        this.status = status
    }
}
