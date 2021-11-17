package net.prasetyomuhdwi.movieapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // hide ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

        String url = "https://api.themoviedb.org/3/movie/popular?"+
                "api_key=434f297aa1bc200c813ea38732f514dd&language=en-US&page=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(HomeActivity.this,getResources().getString(R.string.on_failure_connect),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseData = Objects.requireNonNull(response.body()).string();
                Fragment fragment = HomeFragment.newInstance(responseData);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment,"home_fragment").commit();
            }
        });


        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            String tag;

            switch (item.getItemId()){
                case R.id.nav_upcoming:
                    selectedFragment= new UpcomingFragment();
                    tag = "search_fragment";
                    break;
                case R.id.nav_setting:
                    selectedFragment = new SettingFragment();
                    tag = "setting_fragment";
                    break;
                default:
                    selectedFragment = new HomeFragment();
                    tag = "home_fragment";
                    break;
            }
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            transaction1.replace(R.id.fragment_container,selectedFragment,tag).commit();

            return true;
        });
    }
}