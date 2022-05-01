package pl.edu.pja.taskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> taskList;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        taskList = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return taskList;
    }

    public void deleteAll(){
        taskRepository.deleteAllTasks();
    }

    public void insert(Task task){
        taskRepository.insertTask(task);
    }

    public void delete(Task task){
        taskRepository.deleteTask(task);
    }

    public void update(Task task){
        taskRepository.updateTask(task);
    }
}
