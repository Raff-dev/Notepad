package com.example.notatnik;

public class Task {
    private int id;
    private String taskText;
    private boolean isChecked;

    public Task(int id, String taskText, boolean isChecked) {
        this.id = id;
        this.taskText = taskText;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
