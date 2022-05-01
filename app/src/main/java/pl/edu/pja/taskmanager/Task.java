package pl.edu.pja.taskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;
    private String date;

    public Task(String title, String description, int priority, String date) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
