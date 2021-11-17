package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Objects;

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

        String local = String.valueOf(Locale.getDefault().toLanguageTag());
        String[] url = {"https://api.themoviedb.org/3/movie/popular?api_key=434f297aa1bc200c813ea38732f514dd&language="+local+"&page=1",
                "https://api.themoviedb.org/3/genre/movie/list?api_key=434f297aa1bc200c813ea38732f514dd&language="+local};


        new HomeTask().execute(url);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            String tag;

            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            if (item.getItemId() == R.id.nav_setting) {
                selectedFragment = SettingFragment.newInstance();
                tag = "setting_fragment";
                transaction1.replace(R.id.fragment_container, selectedFragment, tag).commit();
            } else {
                new HomeTask().execute(url);
            }

            return true;
        });
    }

    private class HomeTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... url) {
            OkHttpClient client = new OkHttpClient();

            // Movies Data
            Request request = new Request.Builder().url(String.valueOf(url[0])).build();
            String moviesData = null;
            try (Response response = client.newCall(request).execute()) {
                moviesData = Objects.requireNonNull(response.body()).string();
            }catch (Exception e){e.printStackTrace();}

            // Data Genres List
            request = new Request.Builder().url(String.valueOf(url[1])).build();
            String genreData = null;
            try (Response response = client.newCall(request).execute()) {
                genreData = Objects.requireNonNull(response.body()).string();
            }catch (Exception e){e.printStackTrace();}

            if (moviesData != null && genreData != null){
                return new String[]{moviesData,genreData};
            }else{
             return new String[]{getResources().getString(R.string.on_failure_connect)};
            }
        }

        @Override
        protected void onPostExecute(String[] responseData) {
            super.onPostExecute(responseData);
            Fragment fragment = HomeFragment.newInstance(responseData);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,fragment,"home_fragment").commit();
        }
    }
}