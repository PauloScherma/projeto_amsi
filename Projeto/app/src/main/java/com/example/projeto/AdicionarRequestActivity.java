package com.example.projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.modelo.Priority;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.example.projeto.modelo.Status;

public class AdicionarRequestActivity extends AppCompatActivity {

    public static final String ID_REQUEST = "ID";
    private EditText etTitle, etDescription, etCustomerId, etTechnicianId;
    private Spinner spPriority, spStatus;
    private Button btnGuardar;
    private Request request;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_request);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etCustomerId = findViewById(R.id.etCustomerId);
        etTechnicianId = findViewById(R.id.etTechnicianId);
        spPriority = findViewById(R.id.spPriority);
        spStatus = findViewById(R.id.spStatus);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Initialize Spinners with Enum values
        spPriority.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Priority.values()));
        spStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Status.values()));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        id = getIntent().getIntExtra(ID_REQUEST, -1);
        if (id != -1) {
            setTitle("Editar Request");
            request = SingletonGestorRequests.getInstance(this).getRequest(id);
            if (request != null) {
                carregarRequest();
            }
        } else {
            setTitle("Adicionar Request");
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String custIdStr = etCustomerId.getText().toString();
                String techIdStr = etTechnicianId.getText().toString();

                if (!title.isEmpty() && !description.isEmpty()) {
                    int custId = custIdStr.isEmpty() ? 0 : Integer.parseInt(custIdStr);
                    int techId = techIdStr.isEmpty() ? 0 : Integer.parseInt(techIdStr);
                    Priority priority = (Priority) spPriority.getSelectedItem();
                    Status status = (Status) spStatus.getSelectedItem();

                    if (request != null) {
                        // Modo Edição
                        request.setTitle(title);
                        request.setDescription(description);
                        request.setCustomerId(custId);
                        request.setCurrentTechnicianId(techId);
                        request.setPriority(priority);
                        request.setStatus(status);
                        SingletonGestorRequests.getInstance(getApplicationContext()).editarRequest(request);
                    } else {
                        // Modo Adição
                        Request newRequest = new Request(0, custId, title, description,
                                priority, status, techId, null, 0, null, null);

                        SingletonGestorRequests.getInstance(getApplicationContext()).adicionarRequest(newRequest);
                    }
                    finish();
                } else {
                    Toast.makeText(AdicionarRequestActivity.this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void carregarRequest() {
        etTitle.setText(request.getTitle());
        etDescription.setText(request.getDescription());
        etCustomerId.setText(String.valueOf(request.getCustomerId()));
        etTechnicianId.setText(String.valueOf(request.getCurrentTechnicianId()));

        // Set Spinner selections
        spPriority.setSelection(((ArrayAdapter)spPriority.getAdapter()).getPosition(request.getPriority()));
        spStatus.setSelection(((ArrayAdapter)spStatus.getAdapter()).getPosition(request.getStatus()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}