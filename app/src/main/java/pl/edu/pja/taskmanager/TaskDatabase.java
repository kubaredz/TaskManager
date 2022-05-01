package pl.edu.pja.taskmanager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Task.class, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase taskDatabase;
    public abstract TaskDAO taskDAO();

    public static synchronized TaskDatabase buildApplication(Context data){
    if (taskDatabase == null){
        taskDatabase = Room.databaseBuilder(data.getApplicationContext()
                    ,TaskDatabase.class , "task_manager")
                    .fallbackToDestructiveMigration()
                    .build();
        }
            return taskDatabase;
    }
}
