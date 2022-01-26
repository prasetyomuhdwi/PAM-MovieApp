package net.prasetyomuhdwi.movieapps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditUserFragment extends Fragment {

    DbHelper dbHelper;
    private ProgressDialog pDialog;
    private static final String BASE_URL = "http://192.168.1.9/update";

    public NavController navController;
    private Integer mId;
    private String mUsername;
    private String mFullname;
    private String mEmail;
    private String mPassword;

    public EditUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt("id");
            mUsername = getArguments().getString("username");
            mFullname = getArguments().getString("fullname");
            mEmail = getArguments().getString("email");
            mPassword = getArguments().getString("password");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        EditText inputUsername = view.findViewById(R.id.input_edit_username);
        EditText inputFullname = view.findViewById(R.id.input_edit_fullname);
        EditText inputEmail = view.findViewById(R.id.input_edit_email);
        EditText inputPassword = view.findViewById(R.id.input_edit_password);

        inputUsername.setText(mUsername);
        inputFullname.setText(mFullname);
        inputEmail.setText(mEmail);
        inputPassword.setText(mPassword);

        Button btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(v->{
            int id = mId;
            String email = "", password = "", username = "", fullname = "";

            if(!TextUtils.isEmpty(inputEmail.getText()) && !TextUtils.isEmpty(inputPassword.getText())
                    && !TextUtils.isEmpty(inputFullname.getText()) && !TextUtils.isEmpty(inputUsername.getText())) {

                if (!Validation.isValidEmail(inputEmail.getText())) {
                    Toast.makeText(requireActivity(), R.string.error_msg_email, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    email = String.valueOf(inputEmail.getText());
                }

                if (!Validation.isValidUsername(inputUsername.getText())) {
                    Toast.makeText(requireActivity(), R.string.error_msg_username, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    username = String.valueOf(inputUsername.getText());
                }

                if (!Validation.isValidFullname(inputFullname.getText())) {
                    Toast.makeText(requireActivity(), R.string.error_msg_fullname, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    fullname = String.valueOf(inputFullname.getText());
                }

                if (!Validation.isValidPassword(inputPassword.getText())) {
                    Toast.makeText(requireActivity(), R.string.error_msg_password, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    password = String.valueOf(inputPassword.getText());
                }

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(fullname)) {
                    String[] data = {String.valueOf(id),email,password,username,fullname};
                    new EditTask().execute(data);
                }
            }else{
                Toast.makeText(requireActivity(), R.string.error_msg_empty, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.action_editUserFragment_to_homeFragment);
                    break;
                case R.id.nav_setting:
                    navController.navigate(R.id.action_editUserFragment_to_settingFragment);
                    break;
                case R.id.nav_profile:
                    navController.navigate(R.id.action_editUserFragment_to_profileFragment);
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    protected class EditTask extends AsyncTask<String, String, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            String id = strings[0];
            String email = strings[1];
            String password = strings[2];
            String username = strings[3];
            String fullname = strings[4];

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("username", username)
                    .add("fullname", fullname)
                    .add("email", email)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(BASE_URL + "/user")
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            Response response = null;

            String dataUser = null;

            try {
                response = call.execute();
                if(response.code() == 200){
                    dataUser = Objects.requireNonNull(response.body()).string();

                    JSONObject objData = new JSONObject(dataUser);
                    objData = objData.getJSONObject("body");
                    dataUser = String.valueOf(objData);

                    dbHelper = new DbHelper(requireActivity());
                    dbHelper.updateUser(
                            objData.getInt("id"),
                            objData.getString("username"),
                            objData.getString("fullname"),
                            objData.getString("email"),
                            password
                    );
                }
            }catch (Exception e){e.printStackTrace();}

            if (dataUser != null){
                return new String[]{getResources().getString(R.string.edit_success),dataUser};
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
                navController.navigate(R.id.action_editUserFragment_to_profileFragment);
            }
        }
    }
}