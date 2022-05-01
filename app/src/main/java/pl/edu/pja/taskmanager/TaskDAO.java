package pl.edu.pja.taskmanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task  ORDER BY priority ASC")
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
