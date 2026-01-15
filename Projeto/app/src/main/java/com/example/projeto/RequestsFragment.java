package com.example.projeto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.projeto.adaptadores.ListaRequestsAdaptador;
import com.example.projeto.listeners.RequestsListener;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RequestsFragment extends Fragment implements RequestsListener {
    private ListView lvRequests;
    private ArrayList<Request> requests;
    private FloatingActionButton fabAdd;
    private ListaRequestsAdaptador adapter;

    private static final String PREFS_NAME = "APP_PREFS";
    private static final String KEY_BASE_URL = "base_url";

    public RequestsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        lvRequests = view.findViewById(R.id.lvRequests);
        fabAdd = view.findViewById(R.id.fabAdd);
        
        requests = SingletonGestorRequests.getInstance(getContext()).getRequests();

        adapter = new ListaRequestsAdaptador(getContext(), requests);
        lvRequests.setAdapter(adapter);

        lvRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetalhesRequestActivity.class);
                intent.putExtra(DetalhesRequestActivity.ID_REQUEST, (int) l);
                startActivity(intent);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarRequestActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUrl = prefs.getString(KEY_BASE_URL, "");
        System.out.println("URL: " + savedUrl);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefreshListaRequests(ArrayList<Request> listaRequests) {
        if (listaRequests!=null) {
            adapter = new ListaRequestsAdaptador(getContext(), listaRequests);
            lvRequests.setAdapter(adapter);
        }
    }
}
