package pl.edu.pja.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class NewTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "pl.edu.pja.taskmanager.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "pl.edu.pja.taskmanager.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "pl.edu.pja.taskmanager.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "pl.edu.pja.taskmanager.EXTRA_ID";
    public static final String EXTRA_YEAR = "pl.edu.pja.taskmanager.EXTRA_YEAR";
    public static final String EXTRA_DAY = "pl.edu.pja.taskmanager.EXTRA_DAY";
    public static final String EXTRA_MONTH = "pl.edu.pja.taskmanager.EXTRA_MONTH";
    public static final String EXTRA_PROGRESS = "pl.edu.pja.taskmanager.EXTRA_PROGRESS";


    private EditText titleField, descriptionField;
    private Button saveTask;
    private NumberPicker priorityField;
    private DatePicker dateField;
    private EditText progressField;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        titleField = findViewById(R.id.title);
        descriptionField = findViewById(R.id.description);
        priorityField = findViewById(R.id.priority);
        dateField = findViewById(R.id.datePicker);
        progressField = findViewById(R.id.progress);

        saveTask = findViewById(R.id.saveTask);

        priorityField.setMinValue(1);
        priorityField.setMaxValue(5);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        setTitle("Add note");

        Intent editIntent = getIntent();
        if (editIntent.hasExtra(EXTRA_ID)){
            setTitle("Edit task");
            titleField.setText(editIntent.getStringExtra(EXTRA_TITLE));
            descriptionField.setText(editIntent.getStringExtra(EXTRA_DESCRIPTION));
            priorityField.setValue(editIntent.getIntExtra(EXTRA_PRIORITY, 1));
//            progressField.setText(Integer.parseInt(String.valueOf(editIntent.getIntExtra(EXTRA_PROGRESS, 0))));
//            dateField.setText(editIntent.getStringExtra(EXTRA_DATE));
//            tvw.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());

        }else {
            setTitle("Adding task");
        }

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleField.getText().toString();
                String description = descriptionField.getText().toString();
                int priority = priorityField.getValue();
                int date = dateField.getDayOfMonth();
                //check
                int progress = Integer.parseInt(progressField.getText().toString());

                if(title.trim().isEmpty() || description.trim().isEmpty()){
                    Toast.makeText(NewTaskActivity.this, "Empty fields are not allowed",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                int year = dateField.getYear();
                int month = dateField.getMonth();
                int day = dateField.getDayOfMonth();

                Intent dataIntent = new Intent();
                dataIntent.putExtra(EXTRA_TITLE, title);
                dataIntent.putExtra(EXTRA_DESCRIPTION, description);
                dataIntent.putExtra(EXTRA_PRIORITY, priority);
                //// TODO: 01/05/2022 check is not null
                dataIntent.putExtra(EXTRA_YEAR, year);
                dataIntent.putExtra(EXTRA_MONTH, month);
                dataIntent.putExtra(EXTRA_DAY, day);
                dataIntent.putExtra(EXTRA_PROGRESS, progress);
                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1){
                    dataIntent.putExtra(EXTRA_ID, id);
                }

                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }
}
