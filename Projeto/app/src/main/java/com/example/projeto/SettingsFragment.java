package com.example.projeto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.ipleiria.estg.dei.ourapppsiassist.R;

public class SettingsFragment extends Fragment {

    private EditText edtBaseUrl;
    private Button btnSave;
    private static final String PREFS_NAME = "APP_PREFS";
    private static final String KEY_BASE_URL = "base_url";
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        edtBaseUrl = view.findViewById(R.id.edtBaseUrl);
        btnSave = view.findViewById(R.id.btnSave);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Carregar valor guardado
        String savedUrl = prefs.getString(KEY_BASE_URL, "");
        edtBaseUrl.setText(savedUrl);

        btnSave.setOnClickListener(v -> {
            String url = edtBaseUrl.getText().toString().trim();

            if (url.isEmpty()) {
                Toast.makeText(getContext(), "A URL não pode estar vazia", Toast.LENGTH_SHORT).show();
                return;
            }

            prefs.edit().putString(KEY_BASE_URL, url).apply();
            Toast.makeText(getContext(), "Configuração guardada", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
