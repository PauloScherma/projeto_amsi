package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.listeners.RatingListener;
import com.example.projeto.modelo.Rating;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetalhesRatingActivity extends AppCompatActivity implements RatingListener {

    public static final String ID_RATING = "ID_RATING";
    private static final int EDIT_RATING_CODE = 1;

    private TextView tvTitle, tvDescription;
    private RatingBar ratingBar;
    private FloatingActionButton fabEdit;
    private Rating rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_rating);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        ratingBar = findViewById(R.id.ratingBar);
        fabEdit = findViewById(R.id.fabEdit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int ratingId = getIntent().getIntExtra(ID_RATING, -1);

        SingletonGestorRequests.getInstance(this).setRatingListener(this);
        SingletonGestorRequests.getInstance(this).getRatingByIdAPI(this, ratingId);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesRatingActivity.this, FormRatingActivity.class);
                intent.putExtra(FormRatingActivity.ID_RATING, ratingId);
                startActivityForResult(intent, EDIT_RATING_CODE);
            }
        });
    }

    private void popularDados() {
        if (rating != null) {
            setTitle("Detalhes do Rating");
            tvTitle.setText(rating.getTitle());
            tvDescription.setText(rating.getDescription());
            ratingBar.setRating(rating.getScore());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_RATING_CODE) {
            popularDados();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRefreshDetalhes() {
        rating = SingletonGestorRequests.getInstance(this).getRating();

        popularDados();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SingletonGestorRequests.getInstance(this).setRatingListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SingletonGestorRequests.getInstance(this).setRatingListener(this);
    }
}
