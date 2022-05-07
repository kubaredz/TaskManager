package pl.edu.pja.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import pl.edu.pja.taskmanager.adapter.TaskAdapter;
import pl.edu.pja.taskmanager.model.Task;
import pl.edu.pja.taskmanager.model.TaskViewModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private TaskAdapter.Example example;
    private TextView textView;
    private TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel;
    private static final int ADD_TASK_REQUEST_CODE = 1;
    private static final int EDIT_TASK_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_to_do);
        super.onCreate(savedInstanceState);

        //PODPIECIE WIDOKU - GLOWNY EKRAN
        setContentView(R.layout.activity_main);

        //PODPIECIE POL - GLOWNY EKRAN
        recyclerView = findViewById(R.id.itemList);
        floatingActionButton = findViewById(R.id.addNewTaskButton);
        textView = findViewById(R.id.textView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(getApplication());
        recyclerView.setAdapter(taskAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));


        //ADD PRZYCISK - GLOWNY EKRAN
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
        });
        taskViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory)
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> taskAdapter.submitList(tasks));

        //USUNIECIE NA LONG CLICK
        recyclerView.setOnLongClickListener(view -> {
            taskViewModel.delete(taskAdapter.getTask(view.getVerticalScrollbarPosition()));
            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        //TODO
        //onclick button {}
        //        String smsBody="Sms Body";
        //        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        //        sendIntent.putExtra("sms_body", smsBody);
        //        sendIntent.setType("vnd.android-dir/mms-sms");
        //        startActivity(sendIntent);

        //USUNIECIE / EDYCJA SWIPE DELETE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //USUNIECIE NA SWIPE
                if (direction == ItemTouchHelper.RIGHT) {
                    taskViewModel.delete(taskAdapter.getTask(viewHolder.getAdapterPosition()));
                    //TODO Odswiezyc glowny ekran
                    finish();
                    startActivity(getIntent());
                } else {
                    //EDYCJA NA SWIPE
                    //PRZEJSCIE NA WIDOK - EDYCJA TASKA
                    Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                    intent.putExtra(NewTaskActivity.ID, taskAdapter.getTask(viewHolder.getAdapterPosition()).getId());
                    intent.putExtra(NewTaskActivity.TITLE, taskAdapter.getTask(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra(NewTaskActivity.DESCRIPTION, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDescription());
                    intent.putExtra(NewTaskActivity.PRIORITY, taskAdapter.getTask(viewHolder.getAdapterPosition()).getPriority());
                    intent.putExtra(NewTaskActivity.PROGRESS, taskAdapter.getTask(viewHolder.getAdapterPosition()).getProgress());
                    intent.putExtra(NewTaskActivity.YEAR, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDate());
                    intent.putExtra(NewTaskActivity.MONTH, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDate());
                    intent.putExtra(NewTaskActivity.DAY, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDate());

                    startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("main", "is IN RESUME");
        try {
            Log.d("main", "is IN");

        } catch (NullPointerException e) {
            System.err.println("Null pointer exception");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //DODAJ NOWY TASK -> PRZEKAZANIE PO WCISNIECIU PRZYCISKU
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {

            String title = data.getStringExtra(NewTaskActivity.TITLE);
            String description = data.getStringExtra(NewTaskActivity.DESCRIPTION);
            int priority = data.getIntExtra(NewTaskActivity.PRIORITY, 1);
            int year = data.getIntExtra(NewTaskActivity.YEAR, 2000);
            int monthFromZero = data.getIntExtra(NewTaskActivity.MONTH, 1);
            int month = monthFromZero;
            int day = data.getIntExtra(NewTaskActivity.DAY, 25);

            LocalDate date = LocalDate.of(year, month, day);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date1 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
            int progress = data.getIntExtra(NewTaskActivity.PROGRESS, 0);

            Task task = new Task(title, description, priority, progress, date1.getTime());
            //UTWORZENIE NOWEGO TASKA
            taskViewModel.insert(task);

        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            //EDYCJA ISTNIEJACEGO TASKA
            int id = data.getIntExtra(NewTaskActivity.ID, -1);

            String title = data.getStringExtra(NewTaskActivity.TITLE);
            String description = data.getStringExtra(NewTaskActivity.DESCRIPTION);
            int priority = data.getIntExtra(NewTaskActivity.PRIORITY, 1);
            int year = data.getIntExtra(NewTaskActivity.YEAR, 2000);
            int month = data.getIntExtra(NewTaskActivity.MONTH, 1);
            int day = data.getIntExtra(NewTaskActivity.DAY, 25);

            LocalDate date = LocalDate.of(year, month, day);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date1 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
            int progress = data.getIntExtra(NewTaskActivity.PROGRESS, 0);

            Task task = new Task(title, description, priority, progress, date1.getTime());
            task.setId(id);
            taskViewModel.update(task);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime nowDate(long time) {
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(time),
                        TimeZone.getDefault().toZoneId());
        return triggerTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Long changeTypeOfData(LocalDateTime time) {
        ZoneId zoneId = ZoneId.systemDefault();
        time = time.with(LocalTime.of(0, 0));
        return time.atZone(zoneId).toInstant().toEpochMilli();
    }

    //Counter
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String countFromReceiver = intent.getStringExtra("count");
            textView.setText(countFromReceiver);
        }
    };
}


