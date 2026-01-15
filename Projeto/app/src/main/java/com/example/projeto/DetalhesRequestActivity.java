package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetalhesRequestActivity extends AppCompatActivity {

    public static final String ID_REQUEST = "ID";
    private static final int EDIT_REQUEST_CODE = 1;
    
    private TextView tvTitle, tvDescription;
    private FloatingActionButton fabEdit;
    private Request request;
    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_request);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        fabEdit = findViewById(R.id.fabEdit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        requestId = getIntent().getIntExtra(ID_REQUEST, -1);
        carregarRequestEPopular();

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesRequestActivity.this, AdicionarRequestActivity.class);
                intent.putExtra(AdicionarRequestActivity.ID_REQUEST, requestId);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    private void carregarRequestEPopular() {
        if (requestId != -1) {
            request = SingletonGestorRequests.getInstance(this).getRequest(requestId);
            if (request != null) {
                setTitle("Detalhes: " + request.getTitle());
                tvTitle.setText(request.getTitle());
                tvDescription.setText(request.getDescription());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE) {
            // Recarrega os dados caso tenham sido alterados
            carregarRequestEPopular();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
