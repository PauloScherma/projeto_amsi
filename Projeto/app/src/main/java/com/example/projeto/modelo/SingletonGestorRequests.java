package com.example.projeto.modelo;

import android.content.Context;

import com.example.projeto.listeners.RequestListener;
import com.example.projeto.listeners.RequestsListener;

import java.util.ArrayList;

public class SingletonGestorRequests {
    private static SingletonGestorRequests instance = null;
    private ArrayList<Request> requests;
    private RequestBDHelper requestsDB;
    private RequestsListener requestsListener;
    private RequestListener requestListener;

    public static synchronized SingletonGestorRequests getInstance(Context context) {
        if (instance == null){
            instance = new SingletonGestorRequests(context);
        }
        return instance;
    }

    private SingletonGestorRequests(Context context) {
        requests = new ArrayList<>();
        requests.add(new Request(1, "CAPA", 2026, "TITULO", "SERIE", "AUTOR"));
        requests.add(new Request(2, "CAPA2", 2026, "TITULO2", "SERIE2", "AUTOR2"));
        requestsDB = new RequestBDHelper(context);
    }

    public void setRequestsListener(RequestsListener requestsListener) {
        this.requestsListener = requestsListener;
    }

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public Request getRequest(int id) {
        for (Request r : requests) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public void adicionarRequest(Request request) {
        int newId = 1;
        if (!requests.isEmpty()) {
            newId = requests.get(requests.size() - 1).getId() + 1;
        }
        request.setId(newId);
        requests.add(request);
    }

    public void editarRequest(Request request) {
        Request r = getRequest(request.getId());
        if (r != null) {
            r.setTitulo(request.getTitulo());
            r.setAutor(request.getAutor());
            r.setSerie(request.getSerie());
            r.setAno(request.getAno());
            r.setCapa(request.getCapa());
        }
    }

    public void removerRequest(int id) {
        Request request = getRequest(id);
        if (request != null) {
            requests.remove(request);
        }
    }

    public RequestBDHelper getRequestsDB() {
        return requestsDB;
    }
}
