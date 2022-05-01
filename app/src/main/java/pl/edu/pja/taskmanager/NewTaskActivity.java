package pl.edu.pja.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "pl.edu.pja.taskmanager.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "pl.edu.pja.taskmanager.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "pl.edu.pja.taskmanager.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "pl.edu.pja.taskmanager.EXTRA_ID";
    public static final String EXTRA_DATE = "pl.edu.pja.taskmanager.EXTRA_DATE";


    private EditText titleField, descriptionField;
    private Button saveTask;
    private NumberPicker priorityField;
    private EditText dateField;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        titleField = findViewById(R.id.title);
        descriptionField = findViewById(R.id.description);
        priorityField = findViewById(R.id.priority);
        dateField = findViewById(R.id.date);

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
            dateField.setText(editIntent.getStringExtra(EXTRA_DATE));
        }else {
            setTitle("Adding task");
        }

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleField.getText().toString();
                String description = descriptionField.getText().toString();
                int priority = priorityField.getValue();
                String date = dateField.getText().toString();

                if(title.trim().isEmpty() || description.trim().isEmpty()){
                    Toast.makeText(NewTaskActivity.this, "Empty fields are not allowed",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Intent dataIntent = new Intent();
                dataIntent.putExtra(EXTRA_TITLE, title);
                dataIntent.putExtra(EXTRA_DESCRIPTION, description);
                dataIntent.putExtra(EXTRA_PRIORITY, priority);
                dataIntent.putExtra(EXTRA_DATE, date);

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
