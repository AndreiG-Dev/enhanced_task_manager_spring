package com.andreig.enhanced_task_manager_spring.model;

public class Task {
    private int taskId;
    private String title;
    private String description;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(int taskId, String title, String description) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(){
        return title +  "\n" + description + "\n -------------";
    }
}
