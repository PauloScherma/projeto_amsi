package com.example.projeto;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.modelo.SingletonGestorRequests;
import com.example.projeto.modelo.User;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(CreateAccountActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User(0, username, email, password, null, null, null, null, null, null, null);

                SingletonGestorRequests.getInstance(getApplicationContext()).signupAPI(newUser, CreateAccountActivity.this);

                // After the call, you can finish the activity or show a confirmation
                Toast.makeText(CreateAccountActivity.this, "Registration request sent", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
