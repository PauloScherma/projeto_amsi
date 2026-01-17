package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.listeners.RequestListener;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetalhesRequestActivity extends AppCompatActivity implements RequestListener {

    public static final String ID_REQUEST = "ID";
    private static final int EDIT_REQUEST_CODE = 1;
    private TextView tvTitle, tvDescription, tvStatus, tvPriority, tvCreatedAt;
    private FloatingActionButton fabEdit;
    private Request request;
    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_request);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        tvPriority = findViewById(R.id.tvPriority);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        fabEdit = findViewById(R.id.fabEdit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        requestId = getIntent().getIntExtra(ID_REQUEST, -1);

        SingletonGestorRequests.getInstance(this).setRequestListener(this);
        SingletonGestorRequests.getInstance(this).getRequestByIdAPI(this, requestId);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesRequestActivity.this, FormRequestActivity.class);
                intent.putExtra(FormRequestActivity.ID_REQUEST, requestId);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    private void popularDados() {

        if (request != null) {
            setTitle("Detalhes: " + request.getTitle());
            tvTitle.setText(request.getTitle());
            tvDescription.setText(request.getDescription());
            tvStatus.setText(request.getStatus().name());
            tvPriority.setText(request.getPriority().name());
            tvCreatedAt.setText(request.getCreatedAt());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE) {
            popularDados();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onRefreshDetalhes() {
        request = SingletonGestorRequests.getInstance(this).getRequest();

        popularDados();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SingletonGestorRequests.getInstance(this).setRequestListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SingletonGestorRequests.getInstance(this).setRequestListener(this);
    }
}
