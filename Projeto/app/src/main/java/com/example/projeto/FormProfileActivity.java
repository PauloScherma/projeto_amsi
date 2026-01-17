package com.example.projeto;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.listeners.ProfileListener;
import com.example.projeto.modelo.Profile;
import com.example.projeto.modelo.SingletonGestorRequests;

public class FormProfileActivity extends AppCompatActivity implements ProfileListener {

    public static final String PROFILE_ID = "profile_id";

    private EditText etFirstName, etLastName, etPhone;
    private TextView tvFormTitle;
    private Button btnSaveProfile;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        tvFormTitle = findViewById(R.id.tvFormTitle);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        SingletonGestorRequests.getInstance(this).setProfileListener(this);

        int profileId = getIntent().getIntExtra(PROFILE_ID, -1);

        if (profileId != -1) {
            setTitle("Edit Profile");
            SingletonGestorRequests.getInstance(this).getProfileAPI(this);
        } else {
            setTitle("Create Profile");
            this.profile = new Profile(0, 0, "", "", "", "", "");
        }

        btnSaveProfile.setOnClickListener(v -> {
            if (isDataValid()) {
                profile.setFirstName(etFirstName.getText().toString());
                profile.setLastName(etLastName.getText().toString());
                profile.setPhone(etPhone.getText().toString());

                if (profile.getId() != 0) {
                    SingletonGestorRequests.getInstance(this).updateProfileAPI(profile, this);
                } else {
                    SingletonGestorRequests.getInstance(this).createProfileAPI(profile, this);
                }
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isDataValid() {
        return !etFirstName.getText().toString().isEmpty() &&
               !etLastName.getText().toString().isEmpty() &&
               !etPhone.getText().toString().isEmpty();
    }

    private void carregarProfile(){
        etFirstName.setText(profile.getFirstName());
        etLastName.setText(profile.getLastName());
        etPhone.setText(profile.getPhone());
    }

    @Override
    public void onRefreshProfile(Profile profile) {
        if (profile != null) {
            this.profile = profile;
            carregarProfile();
        } else {
            Toast.makeText(this, "Could not load profile details.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
