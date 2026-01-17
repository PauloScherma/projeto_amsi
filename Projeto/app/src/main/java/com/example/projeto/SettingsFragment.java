package com.example.projeto;

import static com.example.projeto.utils.Constants.KEY_BASE_URL;
import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private EditText edtBaseUrl;
    private Button btnSave;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        edtBaseUrl = view.findViewById(R.id.edtBaseUrl);
        btnSave = view.findViewById(R.id.btnSave);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String savedUrl = prefs.getString(KEY_BASE_URL, "");
        edtBaseUrl.setText(savedUrl);

        btnSave.setOnClickListener(v -> {
            String url = edtBaseUrl.getText().toString().trim();

            if (url.isEmpty()) {
                prefs.edit().remove(KEY_BASE_URL).apply();
            }
            else {
                prefs.edit().putString(KEY_BASE_URL, url).apply();
            }

            Toast.makeText(getContext(), "Configuração guardada", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
