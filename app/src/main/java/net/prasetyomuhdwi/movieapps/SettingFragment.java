package net.prasetyomuhdwi.movieapps;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends Fragment {

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

        Button btnLanguage = (Button) view.findViewById(R.id.btn_language);
        btnLanguage.setOnClickListener(v->{
            Intent intentLang = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intentLang);
        });

        return view;
    }
}