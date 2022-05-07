package pl.edu.pja.taskmanager.adapter;

import static androidx.recyclerview.widget.RecyclerView.*;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import pl.edu.pja.taskmanager.R;
import pl.edu.pja.taskmanager.model.Task;
import pl.edu.pja.taskmanager.repository.TaskRepository;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {


    private TaskRepository taskRepository;
    private Example example;
    private static final ItemCallback<Task> TASK_DIFFERENCE;
    private static final String TITLE_1 = "Tytuł";
    private static final String DESCRIPTION_1 = "Opis";
    private static final String PRIORITY_1 = "Priorytet";
    private static final String PROGRESS_1 = "Progres";
    private static final String COLON = ": ";
    private int count;

    public TaskAdapter(Application application) {
        super(TASK_DIFFERENCE);
        taskRepository = new TaskRepository(application);
        example = new Example();
        count = example.getId();
//        example.build();

    }

    public Task getTask(int position) {
        return getItem(position);
    }

    public static class TaskViewHolder extends ViewHolder {

        TextView title;
        TextView description;
        TextView priority;
        TextView progress;
        TextView date;
        ProgressBar progressBar;
        TextView progressText;

        public TaskViewHolder(View itemView) {
            super(itemView);
            //podpiecie view na adapterze
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            priority = itemView.findViewById(R.id.priority);
            progress = itemView.findViewById(R.id.progress);
            date = itemView.findViewById(R.id.date);
            progressBar = itemView.findViewById(R.id.idTvStatusBar);
            progressText = itemView.findViewById(R.id.idProgressText);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = getItem(position);


        // usuwanie
        holder.itemView.getRootView().setOnLongClickListener(view -> {
            taskRepository.deleteTask(task);
            return true;
        });
        //counter
        long currentTime = Instant.now().toEpochMilli();
        Long date = task.getDate();
        Instant instant = Instant.ofEpochMilli(date);

        long epochSevenDays = 7 * 86400000;

        long valueBetween = instant.toEpochMilli() - currentTime;

        long currentWithSeven = currentTime + epochSevenDays;

        Log.d("values", currentWithSeven + "  ,  " + instant.toEpochMilli() + "  ,  " + currentTime);


        if (currentWithSeven > instant.toEpochMilli() && currentTime < instant.toEpochMilli()) {
            count++;
            Log.d("adapter", String.valueOf(count));

        }
        // ONLY NEWEST THAN TODAY
        if (instant.toEpochMilli() > currentTime) {

            holder.title.setText(TITLE_1 + COLON + task.getTitle());
            holder.description.setText(DESCRIPTION_1 + COLON + task.getDescription());
            holder.priority.setText(PRIORITY_1 + COLON + String.valueOf(task.getPriority()));
            holder.progress.setText(PROGRESS_1 + COLON + String.valueOf(task.getProgress()) + "%");
            holder.progressText.setText(task.getProgress() + " %");
            holder.progressBar.setProgress((Integer.parseInt(String.valueOf(task.getProgress()))));

            // data from created task
            Log.d("adapter", String.valueOf(date));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            ZonedDateTime z = instant.atZone(ZoneId.of("Europe/London"));
            String formatted = z.format(formatter);
            Log.d("adapter", String.valueOf(formatted));

            Date date1 = new Date(date);
            holder.date.setText(formatted);
        } else {
            Log.d("adapter", "nothing to display data too much");

            taskRepository.deleteTask(task);
        }
    }
    //compare task
    static {
        TASK_DIFFERENCE = new ItemCallback<Task>() {
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
    }

    //Counter
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);

        Intent intent = new Intent("custom-message");
        intent.putExtra("count", String.valueOf(count));
        LocalBroadcastManager.getInstance(parent.getContext()).sendBroadcast(intent);


        return new TaskViewHolder(view);
    }

    public final class Example {

        private int count = 0;
        private boolean isBuilt = false;

        public int getId() {
            return count;
        }

        public void setId(int id) {
            if (isBuilt) throw new IllegalArgumentException("already built");
            this.count = id;
        }

        public void build() {
            isBuilt = true;
        }
    }

}
