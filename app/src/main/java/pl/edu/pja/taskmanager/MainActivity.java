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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import pl.edu.pja.taskmanager.adapter.TaskAdapter;
import pl.edu.pja.taskmanager.model.Task;
import pl.edu.pja.taskmanager.model.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    //TODO
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
        setContentView(R.layout.activity_main);

        // TODO: 30/04/2022 refactor button name
        recyclerView = findViewById(R.id.itemList);
        floatingActionButton = findViewById(R.id.addNewTaskButton);
        textView = findViewById(R.id.textView);
        // TODO : 30/04/2022 change place of methods
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(getApplication());
        recyclerView.setAdapter(taskAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
            }
        });
        taskViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.submitList(tasks);
            }
        });

//        recyclerView.setOnLongClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                taskViewModel.delete(taskAdapter.getTask(view.getVerticalScrollbarPosition()));
//                taskAdapter.notifyDataSetChanged();
//
//            }
//        });
// TODO na klikniecie w pozycje pobieram ID + metoda do kasowania z odpowiednim ID
//        recyclerView

        // TODO: 30/04/2022 15:37
        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                taskViewModel.delete(taskAdapter.getTask(view.getVerticalScrollbarPosition()));
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //TODO

        //onclick button {}
//        String smsBody="Sms Body";
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("sms_body", smsBody);
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        startActivity(sendIntent);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    taskViewModel.delete(taskAdapter.getTask(viewHolder.getAdapterPosition()));
                    // TO DO Odswiezyc glowny ekran
//                    finish();
//                    startActivity(getIntent());
                } else {
                    Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                    intent.putExtra(NewTaskActivity.EXTRA_ID, taskAdapter.getTask(viewHolder.getAdapterPosition()).getId());
                    intent.putExtra(NewTaskActivity.EXTRA_TITLE, taskAdapter.getTask(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra(NewTaskActivity.EXTRA_DESCRIPTION, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDescription());
                    intent.putExtra(NewTaskActivity.EXTRA_PRIORITY, taskAdapter.getTask(viewHolder.getAdapterPosition()).getPriority());
                    intent.putExtra(NewTaskActivity.EXTRA_PROGRESS, taskAdapter.getTask(viewHolder.getAdapterPosition()).getProgress());
                    intent.putExtra(NewTaskActivity.EXTRA_YEAR, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDate());
                    intent.putExtra(NewTaskActivity.EXTRA_MONTH, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDate());
                    intent.putExtra(NewTaskActivity.EXTRA_DAY, taskAdapter.getTask(viewHolder.getAdapterPosition()).getDate());

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
//            if (example.getId() >= 0) {

                Log.d("main", "is IN");
//                textView.setText(String.valueOf(example.getId()));
//                finish();
//                startActivity(getIntent());
//            }
        } catch (NullPointerException e) {
            System.err.println("Null pointer exception");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(NewTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NewTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(NewTaskActivity.EXTRA_PRIORITY, 1);
            int year = data.getIntExtra(NewTaskActivity.EXTRA_YEAR, 2000);
            int monthFromZero = data.getIntExtra(NewTaskActivity.EXTRA_MONTH, 1);
            int month = monthFromZero;
            int day = data.getIntExtra(NewTaskActivity.EXTRA_DAY, 25);
//            String date = data.getStringExtra(NewTaskActivity.EXTRA_DATE);

            LocalDate date = LocalDate.of(year, month, day);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date1 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
            int progress = data.getIntExtra(NewTaskActivity.EXTRA_PROGRESS, 0);

            Task task = new Task(title, description, priority, progress, date1.getTime());
            taskViewModel.insert(task);

        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(NewTaskActivity.EXTRA_ID, -1);

            String title = data.getStringExtra(NewTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NewTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(NewTaskActivity.EXTRA_PRIORITY, 1);
            int year = data.getIntExtra(NewTaskActivity.EXTRA_YEAR, 2000);
            int month = data.getIntExtra(NewTaskActivity.EXTRA_MONTH, 1);
            int day = data.getIntExtra(NewTaskActivity.EXTRA_DAY, 25);

//            String date = data.getStringExtra(NewTaskActivity.EXTRA_DATE);

            LocalDate date = LocalDate.of(year, month, day);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date1 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
            int progress = data.getIntExtra(NewTaskActivity.EXTRA_PROGRESS, 0);

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
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String countFromReceiver = intent.getStringExtra("count");
            textView.setText(countFromReceiver);
        }
    };
}


