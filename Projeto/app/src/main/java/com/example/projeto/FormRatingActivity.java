package com.example.projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.adaptadores.ListaRequestsAdaptador;
import com.example.projeto.adaptadores.RequestSpinnerAdapter;
import com.example.projeto.listeners.RatingListener;
import com.example.projeto.listeners.RequestsListener;
import com.example.projeto.modelo.Rating;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;

import java.util.ArrayList;

public class FormRatingActivity extends AppCompatActivity implements RatingListener, RequestsListener {
    public static final String ID_RATING = "ID";
    private EditText etTitle, etDescription;
    private RatingBar rbRating;
    private Button btnGuardar;
    private Rating rating;
    private Spinner spRequests;
    private int id;
    private int idSelectedRequest = -1;
    private ListaRequestsAdaptador adapter;
    private RequestSpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_rating);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        rbRating = findViewById(R.id.ratingBar);
        spRequests = findViewById(R.id.spRequests);
        btnGuardar = findViewById(R.id.btnGuardar);

        spRequests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Request requestSelecionado = (Request) parent.getItemAtPosition(position);

                if (requestSelecionado != null) {
                    idSelectedRequest = requestSelecionado.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idSelectedRequest = -1;
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        id = getIntent().getIntExtra(ID_RATING, -1);

        if (id != -1) {
            setTitle("Editar Rating");
            SingletonGestorRequests.getInstance(this).setRatingListener(this);
            SingletonGestorRequests.getInstance(this).getRatingByIdAPI(getApplicationContext(), id);
        } else {
            setTitle("Adicionar Rating");
            SingletonGestorRequests.getInstance(this).setRequestsListener(this);
            SingletonGestorRequests.getInstance(this).getNotRatedRequestsAPI(getApplicationContext());
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                int ratingScore = Math.round(rbRating.getRating());

                if (!title.isEmpty() && !description.isEmpty()) {
                    if (rating != null) {
                        // Modo Edição
                        rating.setTitle(title);
                        rating.setDescription(description);
                        rating.setScore(ratingScore);
                        SingletonGestorRequests.getInstance(getApplicationContext()).editarRatingAPI(rating, getApplicationContext());
                    } else {
                        if(idSelectedRequest == -1) {
                            Toast.makeText(FormRatingActivity.this, "Por favor, selecione um pedido.", Toast.LENGTH_SHORT).show();
                        }

                        Rating newRating = new Rating(0, idSelectedRequest, title, description, ratingScore, null, null);

                        SingletonGestorRequests.getInstance(getApplicationContext()).adicionarRatingAPI(newRating, getApplicationContext());
                    }
                    finish();
                } else {
                    Toast.makeText(FormRatingActivity.this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void carregarRating() {
        etTitle.setText(rating.getTitle());
        etDescription.setText(rating.getDescription());
        rbRating.setRating(rating.getScore());
    }

    @Override
    public void onRefreshDetalhes() {
        this.rating = SingletonGestorRequests.getInstance(this).getRating();

        if (this.rating != null) {
            carregarRating();
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

    @Override
    public void onRefreshListaRequests(ArrayList<Request> requests) {
        if (requests!=null) {
            spinnerAdapter = new RequestSpinnerAdapter(this, requests);
            spRequests.setAdapter(spinnerAdapter);
        }
    }
}
