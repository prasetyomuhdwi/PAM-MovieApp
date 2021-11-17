package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // hide ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

        Fragment fragment = HomeFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment,"home_fragment").commit();

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