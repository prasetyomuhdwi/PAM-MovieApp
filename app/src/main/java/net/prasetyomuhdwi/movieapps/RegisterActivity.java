package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    DbHelper dbHelper;
    private ArrayList<User> listUsers = new ArrayList<>();

    private ProgressDialog pDialog;
    private static final String BASE_URL = "http://192.168.1.9/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText inputUsername = findViewById(R.id.input_register_username);
        EditText inputFullname = findViewById(R.id.input_register_fullname);
        EditText inputEmail = findViewById(R.id.input_register_email);
        EditText inputPassword = findViewById(R.id.input_register_password);

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v->{
            String email = "", password = "", username = "", fullname = "";

            if(!TextUtils.isEmpty(inputEmail.getText()) && !TextUtils.isEmpty(inputPassword.getText())
                    && !TextUtils.isEmpty(inputFullname.getText()) && !TextUtils.isEmpty(inputUsername.getText())) {

                if (!Validation.isValidEmail(inputEmail.getText())) {
                    Toast.makeText(this, R.string.error_msg_email, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    email = String.valueOf(inputEmail.getText());
                }

                if (!Validation.isValidUsername(inputUsername.getText())) {
                    Toast.makeText(this, R.string.error_msg_username, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    username = String.valueOf(inputUsername.getText());
                }

                if (!Validation.isValidFullname(inputFullname.getText())) {
                    Toast.makeText(this, R.string.error_msg_fullname, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    fullname = String.valueOf(inputFullname.getText());
                }

                if (!Validation.isValidPassword(inputPassword.getText())) {
                    Toast.makeText(this, R.string.error_msg_password, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    password = String.valueOf(inputPassword.getText());
                }

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(fullname)) {
                    String[] data = {email,password,username,fullname};
                    new RegisterActivity.RegisterTask().execute(data);
                }
            }else{
                Toast.makeText(this, R.string.error_msg_empty, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        Button btnLinkRegister = findViewById(R.id.btn_link_login);
        btnLinkRegister.setOnClickListener(v->{
            Intent intent = new Intent(RegisterActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    protected class RegisterTask extends AsyncTask<String, String, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            String email = strings[0];
            String password = strings[1];
            String username = strings[2];
            String fullname = strings[3];

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
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

                    dbHelper = new DbHelper(RegisterActivity.this);
                    dbHelper.addUserDetail(
                            objData.getInt("id"),
                            objData.getString("username"),
                            objData.getString("fullname"),
                            objData.getString("email"),
                            password
                    );
                }
            }catch (Exception e){e.printStackTrace();}

            if (dataUser != null){
                return new String[]{getResources().getString(R.string.register_success),dataUser};
            }else{
                return new String[]{getResources().getString(R.string.on_failure_connect),null};
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String[] responseData) {
            super.onPostExecute(responseData);
            pDialog.dismiss();
            if(!responseData[0].isEmpty()){
                if(responseData[1].isEmpty()){
                    Toast.makeText(RegisterActivity.this, responseData[0], Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, responseData[0], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}