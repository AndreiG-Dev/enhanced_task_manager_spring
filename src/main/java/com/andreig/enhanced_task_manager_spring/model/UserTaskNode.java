package com.andreig.enhanced_task_manager_spring.model;

import org.springframework.data.relational.core.sql.In;

public class UserTaskNode {
    Integer userId;
    Integer taskId;

    public UserTaskNode() {
    }

    public UserTaskNode(Integer userId, Integer taskId) {
        this.userId = userId;
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
