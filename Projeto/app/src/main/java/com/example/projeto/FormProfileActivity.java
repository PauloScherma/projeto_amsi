package com.example.projeto;

import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.modelo.SingletonGestorRequests;

public class FormProfileActivity extends AppCompatActivity {

    public static final String ID_PROFILE = "ID";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_profile);

        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        id = getIntent().getIntExtra(ID_PROFILE, -1);

        if (id != -1) {
            setTitle("Editar Request");
            SingletonGestorRequests.getInstance(this).setProfileListener(this);
            SingletonGestorRequests.getInstance(this).getProfileByIdAPI(getApplicationContext(), id);
        } else {
            setTitle("Adicionar Request");
        }
    }
}