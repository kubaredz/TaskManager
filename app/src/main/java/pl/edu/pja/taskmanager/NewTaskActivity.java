package pl.edu.pja.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class NewTaskActivity extends AppCompatActivity {

    public static final String ID = "Id";
    public static final String TITLE = "Tytuł";
    public static final String DESCRIPTION = "Opis";
    public static final String PRIORITY = "Priorytet";
    public static final String YEAR = "Rok";
    public static final String DAY = "Dzień";
    public static final String MONTH = "Miesiąc";
    public static final String PROGRESS = "Progres";

    private EditText titleField;
    private EditText descriptionField;
    private NumberPicker priorityField;
    private EditText progressField;
    private DatePicker dateField;
    private Button saveTask;
    private Intent dataIntent = new Intent();


    //Ustawienie pól
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingActionBar();

        //Zaladowanie ekranu dla kazdego tasku
        setContentView(R.layout.new_task);

        //Podpiecie pol przy dodawaniu taska
        titleField = findViewById(R.id.title);
        descriptionField = findViewById(R.id.description);
        priorityField = findViewById(R.id.priority);
        dateField = findViewById(R.id.datePicker);
        progressField = findViewById(R.id.progress);
        setMinNumberPickerValue(priorityField, 1);
        setMaxNumberPickerValue(priorityField, 5);
        saveTask = findViewById(R.id.saveTask);


        //Ustawienie pola przy edycji zadania
        Intent editIntent = getIntent();
        if (editIntent.hasExtra(ID)) {
            setTitle("Edytuj zadanie");
            titleField.setText(editIntent.getStringExtra(TITLE));
            descriptionField.setText(editIntent.getStringExtra(DESCRIPTION));
            priorityField.setValue(editIntent.getIntExtra(PRIORITY, 1));
        }

        //Po wcisnieciu button dodaj, zadanie czytanie wpisanych wartosci
        saveTask.setOnClickListener(view -> {

            String title = titleField.getText().toString();
            String description = descriptionField.getText().toString();
            int priority = priorityField.getValue();
            int progress = Integer.parseInt(progressField.getText().toString());

            int year = dateField.getYear();
            int month = dateField.getMonth() + 1;
            int day = dateField.getDayOfMonth() + 1;

            dataIntent.putExtra(TITLE, title);
            dataIntent.putExtra(DESCRIPTION, description);
            dataIntent.putExtra(PRIORITY, priority);
            dataIntent.putExtra(YEAR, year);
            dataIntent.putExtra(MONTH, month);
            dataIntent.putExtra(DAY, day);
            dataIntent.putExtra(PROGRESS, progress);

            //When editing task to show a new version on task list
            int id = getIntent().getIntExtra(ID, -1);
            if (id != -1) {
                dataIntent.putExtra(ID, id);
            }
            //ADD NEW TASK
            setResult(RESULT_OK, dataIntent);
            finish();
        });
    }

    //Share button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareButton:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                sharingIntent.setType("text/plain");
                String shareBody = "Your Body Here";
                String shareSubject = "Your Subject Here";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                // passing subject of the content
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //ActionBar
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