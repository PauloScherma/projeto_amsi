package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.projeto.listeners.ProfileListener;
import com.example.projeto.modelo.Profile;
import com.example.projeto.modelo.SingletonGestorRequests;

public class ProfileFragment extends Fragment implements ProfileListener {

    private TextView tvWelcome, tvFirstName, tvLastName, tvPhone;
    private Button btnEditProfile, btnCreateProfile;
    private LinearLayout profileDataContainer, noProfileContainer;
    private Profile currentProfile;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvFirstName = view.findViewById(R.id.tvFirstName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tvPhone = view.findViewById(R.id.tvPhone);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnCreateProfile = view.findViewById(R.id.btnCreateProfile);
        profileDataContainer = view.findViewById(R.id.profileDataContainer);
        noProfileContainer = view.findViewById(R.id.noProfileContainer);

        SingletonGestorRequests.getInstance(getContext()).setProfileListener(this);

        btnEditProfile.setOnClickListener(v -> navigateToForm());
        btnCreateProfile.setOnClickListener(v -> navigateToForm());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SingletonGestorRequests.getInstance(getContext()).getProfileAPI(getContext());
    }

    @Override
    public void onRefreshProfile(Profile profile) {

        // muda a visibilidade
        if (profile != null) {
            currentProfile = profile;

            profileDataContainer.setVisibility(View.VISIBLE);
            noProfileContainer.setVisibility(View.GONE);

            tvWelcome.setText("Welcome, " + profile.getFirstName());
            tvFirstName.setText(profile.getFirstName());
            tvLastName.setText(profile.getLastName());
            tvPhone.setText(profile.getPhone());
        } else {
            currentProfile = null;
            profileDataContainer.setVisibility(View.GONE);
            noProfileContainer.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToForm() {
        Intent intent = new Intent(getContext(), FormProfileActivity.class);
        if (currentProfile != null) {
            intent.putExtra(FormProfileActivity.PROFILE_ID, currentProfile.getId());
        }
        startActivity(intent);
    }
}
