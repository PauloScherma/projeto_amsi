package com.example.projeto;

import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.listeners.RequestListener;
import com.example.projeto.modelo.Priority;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.example.projeto.modelo.Status;

public class FormRequestActivity extends AppCompatActivity implements RequestListener {

    public static final String ID_REQUEST = "ID";
    private EditText etTitle, etDescription;
    private Button btnGuardar;
    private Request request;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_request);

        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnGuardar = findViewById(R.id.btnGuardar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        id = getIntent().getIntExtra(ID_REQUEST, -1);

        if (id != -1) {
            setTitle("Editar Request");
            SingletonGestorRequests.getInstance(this).setRequestListener(this);
            SingletonGestorRequests.getInstance(this).getRequestByIdAPI(getApplicationContext(), id);
        } else {
            setTitle("Adicionar Request");
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String custIdStr = userId;

                if (!title.isEmpty() && !description.isEmpty()) {
                    int custId = custIdStr.isEmpty() ? 0 : Integer.parseInt(custIdStr);
                    int techId = 0;
                    Priority priority = Priority.MEDIUM;
                    Status status = Status.NEW;

                    if (request != null) {
                        // Modo Edição
                        request.setTitle(title);
                        request.setDescription(description);
                        request.setCustomerId(custId);
                        request.setCurrentTechnicianId(techId);
                        request.setPriority(priority);
                        request.setStatus(status);
                        SingletonGestorRequests.getInstance(getApplicationContext()).editarRequestAPI(request, getApplicationContext());
                    } else {
                        // Modo Adição
                        Request newRequest = new Request(0, custId, title, description,
                                priority, status, techId, null, 0, null, null);

                        SingletonGestorRequests.getInstance(getApplicationContext()).adicionarRequestAPI(newRequest, getApplicationContext());
                    }
                    finish();
                } else {
                    Toast.makeText(FormRequestActivity.this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void carregarRequest() {
        etTitle.setText(request.getTitle());
        etDescription.setText(request.getDescription());
    }

    @Override
    public void onRefreshDetalhes() {
        this.request = SingletonGestorRequests.getInstance(this).getRequest();

        if (this.request != null) {
            carregarRequest();
        } else {
            Toast.makeText(this, "Não foi possível carregar os detalhes do pedido.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}