package pl.edu.pja.taskmanager.adapter;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import pl.edu.pja.taskmanager.R;
import pl.edu.pja.taskmanager.model.Task;
import pl.edu.pja.taskmanager.repository.TaskRepository;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {


    private TaskRepository taskRepository;
    private Example example;
    private static final DiffUtil.ItemCallback<Task> TASK_DIFFERENCE;
    private static final String TITLE = "Tytuł";
    private static final String DESCRIPTION = "Opis";
    private static final String PRIORITY = "Priorytet";
    private static final String PROGRESS = "Progres";
    private static final String DATE = "Data do";
    private static final String COLON = ": ";
    private int count;

    public TaskAdapter(Application application) {
        super(TASK_DIFFERENCE);
        taskRepository = new TaskRepository(application);
        example = new Example();

        count = example.getId();
//        example.build();

    }

    public Task getTask(int position){
        return getItem(position);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView description;
        TextView priority;
        TextView progress;
        TextView date;

        public TaskViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            priority = itemView.findViewById(R.id.priority);
            progress = itemView.findViewById(R.id.progress);
            date = itemView.findViewById(R.id.date);
        }
    }

    // policzyc ilosc elementow na liscie tylko tych ktore koncza sie do 7 dni (EPOCH)


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        Task task = getItem(position);
//        Date currentTime = Calendar.getInstance().getTime();
        long currentTime = Instant.now().toEpochMilli();
        // create Instant from millis value
        Long date = task.getDate();
        Instant instant = Instant.ofEpochMilli(date);

        long epochSevenDays = 86400000;
        // policzenie
        // aktualna data + 7 dni do przodu i sie miesci instant
        // TERAZ + 7 dni > CELU AND (CEL - current time <= 7 dni
//        CEL <teraz, 7dni +>

        long valueBetween = instant.toEpochMilli()-currentTime;

        long lastValue = valueBetween - epochSevenDays;

        // TODO
        if(instant.toEpochMilli() > currentTime && lastValue > 0) {
            count++;
            Log.d("adapter", String.valueOf(count));
        }

        if (instant.toEpochMilli()>currentTime)
        {
            holder.title.setText(TITLE + COLON + task.getTitle());
            holder.description.setText(DESCRIPTION + COLON + task.getDescription());
            holder.priority.setText(PRIORITY + COLON + String.valueOf(task.getPriority()));
            holder.progress.setText(PROGRESS + COLON + String.valueOf(task.getProgress()) + "%");

            // data DO > aktualna data w epoch

            // data from created task
            Log.d("adapter", String.valueOf(date));

//
// use correct format ('S' for milliseconds)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
// convert to timezone
            // godzina do tylu
            ZonedDateTime z = instant.atZone(ZoneId.of("Europe/London"));
// format
            String formatted = z.format(formatter);
//
            Log.d("adapter", String.valueOf(formatted));

            Date date1 = new Date(date);
            //// TODO: 01/05/2022
//        holder.date.setText(DATE + COLON + date1.getDay() + "-" + date1.getMonth()  +"-2022");
            holder.date.setText(DATE + COLON + formatted);
        } else {
            Log.d("adapter", "nothing to display data too much");

            taskRepository.deleteTask(task);
        }
    }

    static {
        TASK_DIFFERENCE = new DiffUtil.ItemCallback<Task>() {
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

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        return new TaskViewHolder(view);
    }

    public final class Example {

        private int count;
        private boolean isBuilt;

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
