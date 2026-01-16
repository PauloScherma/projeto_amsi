package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.projeto.adaptadores.ListaRatingAdaptador;
import com.example.projeto.adaptadores.ListaRequestsAdaptador;
import com.example.projeto.listeners.RatingListener;
import com.example.projeto.listeners.RatingsListener;
import com.example.projeto.modelo.Rating;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RatingFragment extends Fragment implements RatingsListener {

    private ListView lvRatings;
    private ArrayList<Rating> ratings;
    private FloatingActionButton fabAdd;
    private ListaRatingAdaptador adapter;
    private String baseUrl = "http://10.0.2.2/projeto/projeto_v1/backend/web/api";
    //private String baseUrl = "http://172.22.21.234/Projeto/projeto_v1/backend/web/api";
    private final String userrequestsEndpoint = baseUrl + "/request/requests/";
    private static final String PREFS_NAME = "APP_PREFS";
    private static final String KEY_BASE_URL = "base_url";

    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        fabAdd = view.findViewById(R.id.fabAdd);
        lvRatings = view.findViewById(R.id.lvRatings);

        SingletonGestorRequests.getInstance(getContext()).setRatingsListener(this);
        SingletonGestorRequests.getInstance(getContext()).getUserRatignsAPI(getContext());

        adapter = new ListaRatingAdaptador(getContext(), ratings);
        lvRatings.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarRatingActivity.class);
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