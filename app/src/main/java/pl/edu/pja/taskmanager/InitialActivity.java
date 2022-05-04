package pl.edu.pja.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial);

        new Handler().postDelayed (() -> {
                startActivity(new Intent(InitialActivity.this, MainActivity.class));
                finish();
        }, 1000);
    }
}