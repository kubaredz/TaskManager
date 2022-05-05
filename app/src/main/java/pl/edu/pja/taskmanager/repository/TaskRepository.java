package pl.edu.pja.taskmanager.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import pl.edu.pja.taskmanager.db.TaskDAO;
import pl.edu.pja.taskmanager.db.TaskDatabase;
import pl.edu.pja.taskmanager.model.Task;

public class TaskRepository {
    private TaskDAO taskDAO;
    private LiveData<List<Task>> taskList;

//TODO
    public TaskRepository(Application core){
        TaskDatabase taskDatabase = TaskDatabase.buildApplication(core);
        taskDAO = taskDatabase.taskDAO();
        taskList = taskDAO.getAllTasks();

    }
    public LiveData<List<Task>> getAllTasks(){
        return taskList;
    }

    public void insertTask(Task task){
        new InsertAsyncTask(taskDAO).execute(task);
    }
    public void updateTask(Task task){
        new UpdateAsyncTask(taskDAO).execute(task);
    }
    public void deleteTask(Task task){
        new DeleteAsyncTask(taskDAO).execute(task);
    }


    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void>{

        private TaskDAO taskDAO;

        public InsertAsyncTask(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.insert(tasks[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Task, Void, Void>{

        private TaskDAO taskDAO;

        public DeleteAsyncTask(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.delete(tasks[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void>{

        private TaskDAO taskDAO;

        public UpdateAsyncTask(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.update(tasks[0]);
            return null;
        }
    }
}
