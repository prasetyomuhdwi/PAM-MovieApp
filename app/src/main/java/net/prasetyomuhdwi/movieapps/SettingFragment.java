package net.prasetyomuhdwi.movieapps;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SettingFragment extends Fragment {

    DbHelper dbHelper;
    private ArrayList<User> listUsers = new ArrayList<>();

    public static SettingFragment newInstance() {

        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        BottomNavigationView bottomNavigation = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.action_settingFragment_to_homeFragment);
                    break;
                case R.id.nav_profile:
                    navController.navigate(R.id.action_settingFragment_to_profileFragment);
                    break;
                default:
                    return false;
            }

            return true;
        });

        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v->{
            dbHelper = new DbHelper(requireActivity());
            listUsers = dbHelper.getAllUsers();
            dbHelper.deleteUser(listUsers.get(0).getId());
            Toast.makeText(requireActivity(), getResources().getString(R.string.logout), android.widget.Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireActivity(),
                    LoginActivity.class);
            startActivity(intent);
        });

        Button btnLanguage = view.findViewById(R.id.btn_language);
        btnLanguage.setOnClickListener(v->{
            Intent intentLang = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intentLang);
        });
    }
}