package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.projeto.adaptadores.ListaRatingAdaptador;
import com.example.projeto.listeners.RatingsListener;
import com.example.projeto.modelo.Rating;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RatingsFragment extends Fragment implements RatingsListener {

    private ListView lvRatings;
    private ArrayList<Rating> ratings = new ArrayList<>();
    private FloatingActionButton fabAdd;
    private ListaRatingAdaptador adapter;

    public RatingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        fabAdd = view.findViewById(R.id.fabAdd);
        lvRatings = view.findViewById(R.id.lvRatings);

        SingletonGestorRequests.getInstance(getContext()).setRatingsListener(this);
        SingletonGestorRequests.getInstance(getContext()).getUserRatingsAPI(getContext());

        adapter = new ListaRatingAdaptador(getContext(), ratings);
        lvRatings.setAdapter(adapter);

        lvRatings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 'id' é o ID do item, retornado pelo adapter.getItemId()
                int ratingId = (int) id;

                Intent intent = new Intent(getContext(), DetalhesRatingActivity.class);
                intent.putExtra(DetalhesRatingActivity.ID_RATING, ratingId);
                startActivity(intent);
            }
        });


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FormRatingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onRefreshListaRatings(ArrayList<Rating> listaRatings) {
        if (listaRatings!=null) {
            adapter = new ListaRatingAdaptador(getContext(), listaRatings);
            lvRatings.setAdapter(adapter);
        }
    }
}