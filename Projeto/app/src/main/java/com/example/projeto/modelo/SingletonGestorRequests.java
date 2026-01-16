package com.example.projeto.modelo;

import static com.example.projeto.utils.Constants.BASE_URL;
import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projeto.listeners.RequestListener;
import com.example.projeto.listeners.RequestsListener;
import com.example.projeto.utils.RequestJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingletonGestorRequests {
    private static SingletonGestorRequests instance = null;
    private ArrayList<Request> requests;
    private ArrayList<Rating> rating;
    private RequestBDHelper requestsDB;
    private static RequestQueue volleyQueue = null;

    private Request request;

    private RequestsListener requestsListener;
    private RequestListener requestListener;

    private final String userRequestsEndpoint = BASE_URL + "/request/requests";
    private final String createRequestEndpoint = BASE_URL + "/request/createrequest";
    private final String updateRequestEndpoint = BASE_URL + "/request/updaterequest";
    private final String getRequestEndpoint = BASE_URL + "/request/request";
    private final String deleteRequestEndpoint = BASE_URL + "/request/deleterequest";


    public static synchronized SingletonGestorRequests getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorRequests(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonGestorRequests(Context context) {
        requests = new ArrayList<>();

        rating = new ArrayList<>();
        rating.add(new Rating(1, 1, "Fix Server", "Server is down since morning", 5, "2024-05-20", ""));
        rating.add(new Rating(2, 2, "Broken Chair", "The office chair is broken", 3, "2024-05-21", ""));

        requestsDB = new RequestBDHelper(context);
    }

    public void setRequestsListener(RequestsListener requestsListener) {
        this.requestsListener = requestsListener;
    }

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    //region Requests
    public ArrayList<Request> getRequests() {
        return requests;
    }

    public Request getRequest() {
        return this.request;
    }

    public Request getRequest(int id) {
        for (Request r : requests) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public void adicionarRequestBD(Request request) {
        requestsDB.adicionarRequestBD(request);
    }

    public void editarRequestBD(Request request) {
        requestsDB.editarRequestBD(request);
    }

    public void adicionarRequestsBD(ArrayList<Request> requests) {
        requestsDB.removerAllRequestsBD();
        for (Request r : requests)
            requestsDB.adicionarRequestBD(r);
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
    //endregion

    //region Rating
    public ArrayList<Rating> getRatings() {
        return rating;
    }

    public Rating getRating(int id) {
        for (Rating r : rating) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public void adicionarRatingBD(ArrayList<Rating> rating) {
        requestsDB.removerAllRatingsBD();
        for (Rating r : rating)
            requestsDB.adicionarRatingBD(r);
    }

    public void adicionarRating(Rating rating) {
        int newId= 1;
        if (!this.rating.isEmpty()) {
            newId = this.rating.get(this.rating.size() - 1).getId() + 1;
        }
        rating.setId(newId);
        this.rating.add(rating);
    }

    public void editarRating(Rating rating) {
        Rating r = getRating(rating.getId());
        if (r != null) {
            r.setTitle(rating.getTitle());
            r.setDescription(rating.getDescription());
            r.setScore(rating.getScore());
        }
    }

    public void removerRating(int id) {
        Rating ratingToRemove = getRating(id);
        if (ratingToRemove != null) {
            rating.remove(ratingToRemove);
        }
    }
    //endregion

    public RequestBDHelper getRequestsDB() {
        return requestsDB;
    }

    public void getUserRequestsAPI(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        String url = userRequestsEndpoint + "/" + userId + "?access-token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray requestsArray = response.getJSONArray("requests");
                            var requests = RequestJsonParser.parserJsonRequests(requestsArray);

                            SingletonGestorRequests.getInstance(context).adicionarRequestsBD(requests);

                            if(requestsListener!=null)
                                requestsListener.onRefreshListaRequests(requests);
                        } catch (JSONException e) {
                            System.out.println("All Request not found");
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

            volleyQueue.add(request);
    }

    public void getRequestByIdAPI(final Context context, int id) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String url = getRequestEndpoint + "/" + id + "?access-token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Request request = null;
                        try {
                            request = RequestJsonParser.parserJsonRequest(response.getJSONObject("request"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        SingletonGestorRequests.getInstance(context).adicionarRequestBD(request);

                        SingletonGestorRequests.this.request = request;

                        if (requestListener != null) {
                            requestListener.onRefreshDetalhes();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(request);
    }

    public void adicionarRequestAPI(final Request request, final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", request.getTitle());
            jsonBody.put("description", request.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = createRequestEndpoint + "?access-token=" + token;
        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, jsonBody,
                s -> {
                    adicionarRequestBD(RequestJsonParser.parserJsonRequest(s));

                    getUserRequestsAPI(context);

                    if(requestListener!=null)
                        requestListener.onRefreshDetalhes();
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(stringRequest);
    }

    public void editarRequestAPI(final Request request, final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", request.getTitle());
            jsonBody.put("description", request.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = updateRequestEndpoint + "/" + request.getId() + "?access-token=" + token;
        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.PUT, url, jsonBody,
                s -> {
                    editarRequestBD(request);

                    getUserRequestsAPI(context);

                    if(requestListener!=null)
                        requestListener.onRefreshDetalhes();
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(stringRequest);
    }

    public void removerRequestAPI(final Request request, final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String url = deleteRequestEndpoint + "/" + request.getId() + "?access-token=" + token;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.DELETE, url,
                s -> {
                    getUserRequestsAPI(context);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(stringRequest);
    }
}
