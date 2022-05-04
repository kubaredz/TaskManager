package pl.edu.pja.taskmanager.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import pl.edu.pja.taskmanager.repository.TaskRepository;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> countList;
    private LiveData<List<Task>> taskList;

    public TaskViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        countList = taskRepository.getAllTasks();
        taskList = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getCountList(){
        return taskList;
    }

    public LiveData<List<Task>> getAllTasks(){
        return taskList;
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
