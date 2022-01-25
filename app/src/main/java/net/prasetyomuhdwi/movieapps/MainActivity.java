package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make splash activity can cover the entire screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Make delay before switch to HomeActivity
        new Handler().postDelayed(() -> {
            // Intent to switch to LoginActivity.
            Intent intent=new Intent(MainActivity.this,
                    LoginActivity.class);

            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}