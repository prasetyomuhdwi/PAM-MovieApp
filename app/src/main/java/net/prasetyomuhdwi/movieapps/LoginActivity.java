package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    DbHelper dbHelper;
    private ArrayList<User> listUsers = new ArrayList<>();

    private ProgressDialog pDialog;
    private static final String BASE_URL = "http://192.168.1.9/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DbHelper(this);

        EditText inputEmail = findViewById(R.id.input_login_email);
        EditText inputPassword = findViewById(R.id.input_login_password);

        listUsers = dbHelper.getAllUsers();
        if(!listUsers.isEmpty()) {
            String[] data = {listUsers.get(0).getEmail(),listUsers.get(0).getPassword()};
            new LoginTask().execute(data);
        }else {
            Button btnLogin = findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(v->{

                String email = "", password = "";

                if(!TextUtils.isEmpty(inputEmail.getText())&&!TextUtils.isEmpty(inputPassword.getText())) {
                    if (!Validation.isValidEmail(inputEmail.getText())) {
                        Toast.makeText(this, R.string.error_msg_email, Toast.LENGTH_SHORT)
                            .show();
                    } else {
                        email = String.valueOf(inputEmail.getText());
                    }

                    if (!Validation.isValidPassword(inputPassword.getText())) {
                        Toast.makeText(this, R.string.error_msg_password, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        password = String.valueOf(inputPassword.getText());
                    }

                    if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        String[] data = {email,password};
                        new LoginTask().execute(data);
                    }
                }else{
                    Toast.makeText(this, R.string.error_msg_empty, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        Button btnLinkRegister = findViewById(R.id.btn_link_register);
        btnLinkRegister.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
    protected class LoginTask extends AsyncTask<String, String, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            String email = strings[0];
            String password = strings[1];

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
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

                    dbHelper = new DbHelper(LoginActivity.this);
                    listUsers = dbHelper.getAllUsers();
                    if(listUsers.isEmpty()) {
                        dbHelper.addUserDetail(
                            objData.getInt("id"),
                            objData.getString("username"),
                            objData.getString("fullname"),
                            objData.getString("email"),
                            password
                        );
                    }
                }
            }catch (Exception e){e.printStackTrace();}
            if (dataUser != null){
                return new String[]{getResources().getString(R.string.login_success),dataUser};
            }else{
                return new String[]{getResources().getString(R.string.on_failure_connect),null};
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String[] responseData) {
            super.onPostExecute(responseData);
            pDialog.dismiss();
            if(!responseData[0].isEmpty()) {
                if (responseData[1].isEmpty()) {
                    Toast.makeText(LoginActivity.this, responseData[0], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, responseData[0], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}

