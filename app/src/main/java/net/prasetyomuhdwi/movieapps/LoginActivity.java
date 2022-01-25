package net.prasetyomuhdwi.movieapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText inputEmail = findViewById(R.id.input_login_email);
        EditText inputPassword = findViewById(R.id.input_login_password);

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
                    Log.d("EMAIL :", email.toLowerCase());
                    Log.d("PASSWORD :", password);
                }
            }else{
                Toast.makeText(this, R.string.error_msg_empty, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        Button btnLinkRegister = findViewById(R.id.btn_link_register);
        btnLinkRegister.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(intent);
            finish();
        });
//        new LoginActivity.LoginTask().execute(url);

    }

}

