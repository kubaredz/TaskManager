package pl.edu.pja.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import androidx.appcompat.app.AppCompatActivity;

public class NewTaskActivity extends AppCompatActivity {
//TODO
    public static final String EXTRA_TITLE = "pl.edu.pja.taskmanager.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "pl.edu.pja.taskmanager.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "pl.edu.pja.taskmanager.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "pl.edu.pja.taskmanager.EXTRA_ID";
    public static final String EXTRA_YEAR = "pl.edu.pja.taskmanager.EXTRA_YEAR";
    public static final String EXTRA_DAY = "pl.edu.pja.taskmanager.EXTRA_DAY";
    public static final String EXTRA_MONTH = "pl.edu.pja.taskmanager.EXTRA_MONTH";
    public static final String EXTRA_PROGRESS = "pl.edu.pja.taskmanager.EXTRA_PROGRESS";

    private EditText titleField;
    private EditText descriptionField;
    private NumberPicker priorityField;
    private EditText progressField;
    private DatePicker dateField;
    private Button saveTask;
    private Intent dataIntent = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingActionBar();
        setContentView(R.layout.new_task);
        titleField = findViewById(R.id.title);
        descriptionField = findViewById(R.id.description);
        priorityField = findViewById(R.id.priority);
        dateField = findViewById(R.id.datePicker);
        progressField = findViewById(R.id.progress);
        setMinNumberPickerValue(priorityField, 1);
        setMaxNumberPickerValue(priorityField, 5);
        saveTask = findViewById(R.id.saveTask);

        Intent editIntent = getIntent();
        if (editIntent.hasExtra(EXTRA_ID)) {
            setTitle("Edytuj zadanie");
            titleField.setText(editIntent.getStringExtra(EXTRA_TITLE));
            descriptionField.setText(editIntent.getStringExtra(EXTRA_DESCRIPTION));
            priorityField.setValue(editIntent.getIntExtra(EXTRA_PRIORITY, 1));
//            progressField.setText(Integer.parseInt(String.valueOf(editIntent.getIntExtra(EXTRA_PROGRESS, 0))));
//            dateField.setText(editIntent.getStringExtra(EXTRA_DATE));
//            tvw.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
        }

        //add new task
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// TODO
                String title = titleField.getText().toString();
                String description = descriptionField.getText().toString();
                int priority = priorityField.getValue();
                int date = dateField.getDayOfMonth();
                //check
                int progress = Integer.parseInt(progressField.getText().toString());

                int year = dateField.getYear();
                int month = dateField.getMonth()+1;
                int day = dateField.getDayOfMonth()+1;

                dataIntent.putExtra(EXTRA_TITLE, title);
                dataIntent.putExtra(EXTRA_DESCRIPTION, description);
                dataIntent.putExtra(EXTRA_PRIORITY, priority);
                dataIntent.putExtra(EXTRA_YEAR, year);
                dataIntent.putExtra(EXTRA_MONTH, month);
                dataIntent.putExtra(EXTRA_DAY, day);
                dataIntent.putExtra(EXTRA_PROGRESS, progress);

                //When editing task to show a new version on task list
                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    dataIntent.putExtra(EXTRA_ID, id);
                }
                //ADD NEW TASK
                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // We are using switch case because multiple icons can be kept
        switch (item.getItemId()) {
            case R.id.shareButton:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                // type of the content to be shared
                sharingIntent.setType("text/plain");

                // Body of the content
                String shareBody = "Your Body Here";

                // subject of the content. you can share anything
                String shareSubject = "Your Subject Here";

                // passing body of the content
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                // passing subject of the content
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingActionBar() {
        setTitle("Dodaj zadanie");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        setContentView(R.layout.new_task);
    }

    private void setMinNumberPickerValue(NumberPicker field, int value) {
        field.setMinValue(value);
    }

    private void setMaxNumberPickerValue(NumberPicker field, int value) {
        field.setMaxValue(value);
    }
}