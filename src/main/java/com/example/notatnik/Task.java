package com.example.notatnik;

public class Task {
    private int documentId;
    private int id;
    private String taskName;
    private boolean isChecked;


    public Task(int documentId, int id, String taskName, boolean isChecked) {
        this.documentId = documentId;
        this.id = id;
        this.taskName = taskName;
        this.isChecked = isChecked;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskText(String taskText) {
        this.taskName = taskText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
