package hieuntph22081.fpoly.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(OpeningActivity.this, LoginActivity.class));
            finish();
        },2500);
    }
}