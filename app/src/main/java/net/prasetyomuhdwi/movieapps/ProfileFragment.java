package net.prasetyomuhdwi.movieapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    DbHelper dbHelper;
    private ArrayList<User> listUsers = new ArrayList<>();

    private ProgressDialog pDialog;
    private static final String BASE_URL = "http://192.168.1.9/delete";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView username = view.findViewById(R.id.tv_profile_username);
        TextView fullname = view.findViewById(R.id.tv_profile_fullname);
        TextView email = view.findViewById(R.id.tv_profile_email);

        dbHelper = new DbHelper(requireActivity());
        listUsers = dbHelper.getAllUsers();
        if(!listUsers.isEmpty()) {
            username.setText(listUsers.get(0).getUsername());
            fullname.setText(listUsers.get(0).getFullname());
            email.setText(listUsers.get(0).getEmail());
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        Button btn_edit = view.findViewById(R.id.btn_edit_user);
        Button btn_delete = view.findViewById(R.id.btn_delete_user);

        dbHelper = new DbHelper(requireActivity());
        listUsers = dbHelper.getAllUsers();

        if(!listUsers.isEmpty()) {
            btn_edit.setOnClickListener(v -> {
                Bundle dataUser = new Bundle();
                dataUser.putInt("id", listUsers.get(0).getId());
                dataUser.putString("username", listUsers.get(0).getUsername());
                dataUser.putString("fullname", listUsers.get(0).getFullname());
                dataUser.putString("email", listUsers.get(0).getEmail());
                dataUser.putString("password", listUsers.get(0).getPassword());

                navController.navigate(R.id.action_profileFragment_to_editUserFragment,dataUser);
            });
            btn_delete.setOnClickListener(v -> {
                String[] data = {String.valueOf(listUsers.get(0).getId())};
                new DeleteTask().execute(data);
            });
        }

        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.action_profileFragment_to_homeFragment);
                break;
                case R.id.nav_setting:
                    navController.navigate(R.id.action_profileFragment_to_settingFragment);
                break;
                default:
                    return false;
            }
            return true;
        });
    }

    protected class DeleteTask extends AsyncTask<String, String, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            String id = strings[0];

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .build();

            Request request = new Request.Builder()
                    .url(BASE_URL + "/user")
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            Response response;

            String dataUser = null;

            try {
                response = call.execute();
                if(response.code() == 200){
                    dataUser = Objects.requireNonNull(response.body()).string();

                    JSONObject objData = new JSONObject(dataUser);
                    objData = objData.getJSONObject("body");
                    dataUser = String.valueOf(objData);

                    dbHelper = new DbHelper(requireActivity());
                    dbHelper.deleteUser(Integer.parseInt(id));
                }
            }catch (Exception e){e.printStackTrace();}

            if (dataUser != null){
                return new String[]{getResources().getString(R.string.delete_success),dataUser};
            }else{
                return new String[]{getResources().getString(R.string.on_failure_connect),null};
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(requireActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String[] responseData) {
            super.onPostExecute(responseData);
            pDialog.dismiss();
            if(responseData[1].isEmpty()){
                Toast.makeText(requireActivity(), responseData[0], Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireActivity(), responseData[0], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(),
                        LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}