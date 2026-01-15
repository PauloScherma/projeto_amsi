package com.example.projeto.listeners;

import com.example.projeto.modelo.Request;

import java.util.ArrayList;

public interface RequestsListener {
    void onRefreshListaRequests(ArrayList<Request> requests);
}
