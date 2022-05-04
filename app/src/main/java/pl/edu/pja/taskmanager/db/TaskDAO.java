package pl.edu.pja.taskmanager.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.edu.pja.taskmanager.model.Task;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task ORDER by date ASC")
    LiveData<List<Task>> getAllTasks();

    @Insert
    public void insert(Task task);

    @Update
    public void update(Task task);

    @Delete
    public void delete(Task task);

    @Query("DELETE FROM task")
    public void deleteAll();
}
