package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences
                = this.getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences
                .getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Make splash activity can cover the entire screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Make delay before switch to HomeActivity
        new Handler().postDelayed(() -> {
                // Intent to switch to LoginActivity.
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);

                startActivity(intent);
                finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}