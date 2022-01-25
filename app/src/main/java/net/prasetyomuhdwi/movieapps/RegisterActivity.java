package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

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
                    Log.d("EMAIL :", email.toLowerCase());
                    Log.d("PASSWORD :", password);
                    Log.d("Username :", username);
                    Log.d("Fullname :", fullname);
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
}