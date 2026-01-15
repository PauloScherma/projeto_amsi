package com.example.projeto.modelo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.projeto.listeners.RequestListener;
import com.example.projeto.listeners.RequestsListener;

import java.util.ArrayList;

public class SingletonGestorRequests {
    private static SingletonGestorRequests instance = null;
    private ArrayList<Request> requests;
    private RequestBDHelper requestsDB;

    private static RequestQueue volleyQueue = null;

    private RequestsListener requestsListener;
    private RequestListener requestListener;

    public static synchronized SingletonGestorRequests getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorRequests(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonGestorRequests(Context context) {
        requests = new ArrayList<>();
        // Updated sample data to use new Request constructor
        requests.add(new Request(1, 101, "Fix Server", "Server is down since morning",
                Priority.HIGH, Status.NEW, 0, null, 0, "2024-05-20", "2024-05-20"));
        requests.add(new Request(2, 102, "Broken Chair", "The office chair is broken",
                Priority.LOW, Status.PENDING, 5, null, 0, "2024-05-21", "2024-05-22"));

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
            r.setTitle(request.getTitle());
            r.setDescription(request.getDescription());
            r.setPriority(request.getPriority());
            r.setStatus(request.getStatus());
            r.setCustomerId(request.getCustomerId());
            r.setCurrentTechnicianId(request.getCurrentTechnicianId());
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
