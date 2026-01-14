package com.example.projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;

public class AdicionarRequestActivity extends AppCompatActivity {

    public static final String ID_REQUEST = "ID";
    private EditText etTitulo, etAutor, etSerie, etAno;
    private Button btnGuardar;
    private Request request;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_request);

        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);
        etSerie = findViewById(R.id.etSerie);
        etAno = findViewById(R.id.etAno);
        btnGuardar = findViewById(R.id.btnGuardar);

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
                String titulo = etTitulo.getText().toString();
                String autor = etAutor.getText().toString();
                String serie = etSerie.getText().toString();
                String anoStr = etAno.getText().toString();

                if (!titulo.isEmpty() && !autor.isEmpty() && !anoStr.isEmpty()) {
                    int ano = Integer.parseInt(anoStr);

                    if (request != null) {
                        // Modo Edição
                        request.setTitulo(titulo);
                        request.setAutor(autor);
                        request.setSerie(serie);
                        request.setAno(ano);
                        SingletonGestorRequests.getInstance(getApplicationContext()).editarRequest(request);
                    } else {
                        // Modo Adição
                        Request newRequest = new Request(0, "", ano, titulo, serie, autor);
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
        etTitulo.setText(request.getTitulo());
        etAutor.setText(request.getAutor());
        etSerie.setText(request.getSerie());
        etAno.setText(String.valueOf(request.getAno()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
