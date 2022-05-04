package pl.edu.pja.taskmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import pl.edu.pja.taskmanager.R;
import pl.edu.pja.taskmanager.model.Task;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private static final String TITLE = "Tytul: ";
    private static final String DESCRIPTION = "Opis: ";
    private static final String PRIORITY = "Priorytet: ";
    private static final String PROGRESS = "Progres: ";
    private static final String DATE = "Data do: ";

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    public Task getTask(int position){
        return getItem(position);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, priority, date, progress;
        public TaskViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            priority = itemView.findViewById(R.id.priority);
            progress = itemView.findViewById(R.id.progress);
            date = itemView.findViewById(R.id.date);
        }
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = getItem(position);
        holder.title.setText(TITLE + task.getTitle());
        holder.description.setText(DESCRIPTION + task.getDescription());
        holder.priority.setText(PRIORITY + String.valueOf(task.getPriority()));
        holder.progress.setText(PROGRESS + String.valueOf(task.getProgress()) + "%");
        Long date = task.getDate();
        Date date1 = new Date(date);
        //// TODO: 01/05/2022
        holder.date.setText(DATE + date1.getDay() + "-" + date1.getMonth()  +"-2022");
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(Task oldItem, Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Task oldItem, Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority() &&
                    oldItem.getProgress() == newItem.getProgress() &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        return new TaskViewHolder(view);
    }
}
