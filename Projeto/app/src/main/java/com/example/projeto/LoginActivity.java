package com.example.projeto;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.example.projeto.utils.Constants.BASE_URL;
import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    private final String loginEndpoint = BASE_URL + "/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null && !token.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, MenuHomeActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Login");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void onClickLogin(View view) {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (!isPasswordValida(pass)) {
            etPassword.setError("Password invalida");
            return;
        }

        JSONObject loginBody = new JSONObject();
        try {
            loginBody.put("username", email);
            loginBody.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginEndpoint, loginBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            prefs.edit().putString("token", response.getString("access_token")).apply();
                            prefs.edit().putString("user_id", response.getString("user_id")).apply();
                        } catch (JSONException e) {
                        throw new RuntimeException(e);
                        }

                        Intent intent = new Intent(LoginActivity.this, MenuHomeActivity.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Erro no Login: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    public void onClickCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isEmailValido(String email) {
        if (email == null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String pass) {
        if (pass == null)
            return false;
        return pass.length() >= 4;
    }
}
