package com.example.projeto.modelo;

import static com.example.projeto.utils.Constants.BASE_URL;
import static com.example.projeto.utils.Constants.KEY_BASE_URL;
import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projeto.CreateAccountActivity;
import com.example.projeto.FormProfileActivity;
import com.example.projeto.listeners.ProfileListener;
import com.example.projeto.listeners.RatingListener;
import com.example.projeto.listeners.RatingsListener;
import com.example.projeto.listeners.RequestListener;
import com.example.projeto.listeners.RequestsListener;
import com.example.projeto.utils.ProfileJsonParser;
import com.example.projeto.utils.RatingJsonParser;
import com.example.projeto.utils.RequestJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingletonGestorRequests {
    private static SingletonGestorRequests instance = null;
    private ArrayList<Request> requests;
    private ArrayList<Rating> ratings;
    private Request request;
    private Rating rating;
    private RequestsListener requestsListener;
    private RequestListener requestListener;
    private RatingsListener ratingsListener;
    private RatingListener ratingListener;
    private ProfileListener profileListener;

    private String userRequestsEndpoint = BASE_URL + "/request/requests";
    private String getRequestEndpoint = BASE_URL + "/request/request";
    private String notRatedRequestsEndpoint = BASE_URL + "/request/notrated/requests";
    private String createRequestEndpoint = BASE_URL + "/request/createrequest";
    private String updateRequestEndpoint = BASE_URL + "/request/updaterequest";
    private String deleteRequestEndpoint = BASE_URL + "/request/deleterequest";

    private String getUserRatingsEndpoint = BASE_URL + "/rating/ratings";
    private String getRatingEndpoint = BASE_URL + "/rating/rating";
    private String createRatingEndpoint = BASE_URL + "/rating/createrating";
    private String updateRatingEndpoint = BASE_URL + "/rating/updaterating";
    private String deleteRatingEndpoint = BASE_URL + "/rating/deleterating";

    private String getUserProfileEndpoint = BASE_URL + "/profile/profile";
    private String createProfileEndpoint = BASE_URL + "/profile/createprofile";
    private String updateProfileEndpoint = BASE_URL + "/profile/updateprofile";
    private String deleteProfileEndpoint = BASE_URL + "/profile/deleteprofile";

    private String signupEndpoint = BASE_URL + "/user/register";

    private static RequestQueue volleyQueue = null;
    private BDHelper bdHelper;

    public static synchronized SingletonGestorRequests getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorRequests(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }
    private SingletonGestorRequests(Context context) {
        requests = new ArrayList<>();
        ratings = new ArrayList<>();

        bdHelper = new BDHelper(context);
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
        bdHelper.adicionarRequestBD(request);
    }
    public void editarRequestBD(Request request) {
        bdHelper.editarRequestBD(request);
    }

    public void adicionarRatingBD(Rating rating) {
        bdHelper.adicionarRatingBD(rating);
    }
    public void adicionarRequestsBD(ArrayList<Request> requests) {
        bdHelper.removerAllRequestsBD();
        for (Request r : requests)
            bdHelper.adicionarRequestBD(r);
    }

    public void editarRatingBD(Rating rating) {
        bdHelper.editarRatingBD(rating);
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
    public void getUserRequestsAPI(final Context context) {
        RedefineEndpoints(context);
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
        RedefineEndpoints(context);
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
        RedefineEndpoints(context);
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
        RedefineEndpoints(context);
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
    public BDHelper getBdHelper() {
        return bdHelper;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }
    public Rating getRating(int id) {
        for (Rating r : ratings) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public Rating getRating(){
        return this.rating;
    }

    public void adicionarRatingsBD(ArrayList<Rating> ratings) {
        bdHelper.removerAllRatingsBD();
        for (Rating r : ratings)
            bdHelper.adicionarRatingBD(r);
    }
    public void adicionarRating(Rating rating) {
        int newId= 1;
        if (!this.ratings.isEmpty()) {
            newId = this.ratings.get(this.ratings.size() - 1).getId() + 1;
        }
        rating.setId(newId);
        this.ratings.add(rating);
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
            ratings.remove(ratingToRemove);
        }
    }
    public void getUserRatingsAPI(final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        String url = getUserRatingsEndpoint + "/" + userId + "?access-token=" + token;
        JsonObjectRequest rating = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ratingArray = response.getJSONArray("requests");
                            var ratings = RatingJsonParser.parserJsonRatings(ratingArray);

                            SingletonGestorRequests.getInstance(context).adicionarRatingsBD(ratings);

                            if(ratingsListener!=null)
                                ratingsListener.onRefreshListaRatings(ratings);
                        } catch (JSONException e) {
                            System.out.println("All Ratings not found");
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

        volleyQueue.add(rating);
    }

    public void getRatingByIdAPI(final Context context, int id) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String url = getRatingEndpoint + "/" + id + "?access-token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Rating rating = null;
                        try {
                            rating = RatingJsonParser.parserJsonRating(response.getJSONObject("request"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        SingletonGestorRequests.getInstance(context).adicionarRatingBD(rating);

                        SingletonGestorRequests.this.rating = rating;

                        if (ratingListener != null) {
                            ratingListener.onRefreshDetalhes();
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

    public void adicionarRatingAPI(final Rating rating, final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("request_id", rating.getRequestId());
            jsonBody.put("title", rating.getTitle());
            jsonBody.put("description", rating.getDescription());
            jsonBody.put("score", rating.getScore());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = createRatingEndpoint + "?access-token=" + token;
        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, jsonBody,
                s -> {
                    adicionarRatingBD(RatingJsonParser.parserJsonRating(s));

                    getUserRatingsAPI(context);

                    if(ratingListener!=null)
                        ratingListener.onRefreshDetalhes();
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(stringRequest);
    }

    public void editarRatingAPI(final Rating rating, final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("request_id", rating.getRequestId());
            jsonBody.put("title", rating.getTitle());
            jsonBody.put("description", rating.getDescription());
            jsonBody.put("score", rating.getScore());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = updateRatingEndpoint + "/" + rating.getId() + "?access-token=" + token;
        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.PUT, url, jsonBody,
                s -> {
                    editarRatingBD(rating);

                    getUserRatingsAPI(context);

                    if(ratingListener!=null)
                        ratingListener.onRefreshDetalhes();
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(stringRequest);
    }

    public void removerRatingAPI(final Rating rating, final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String url = deleteRatingEndpoint + "/" + rating.getId() + "?access-token=" + token;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.DELETE, url,
                s -> {
                    getUserRatingsAPI(context);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
                });

        volleyQueue.add(stringRequest);
    }

    public void setRatingsListener(RatingsListener ratingsListener) {
        this.ratingsListener = ratingsListener;
    }

    public void setRatingListener(RatingListener ratingListener) {
        this.ratingListener = ratingListener;
    }

    public void getNotRatedRequestsAPI(final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        String url = notRatedRequestsEndpoint +"/" + userId + "?access-token=" + token;
        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray requestsArray = response.getJSONArray("requests");
                            var requests = RequestJsonParser.parserJsonRequests(requestsArray);

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

        volleyQueue.add(stringRequest);
    }

    public void setProfileListener(ProfileListener profileListener) {
        this.profileListener = profileListener;
    }

    public void getProfileAPI(final Context context) {RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("user_id", "");

        if (userId.isEmpty()) {
            if (profileListener != null) {
                profileListener.onRefreshProfile(null);
            }
            return;
        }

        String url = getUserProfileEndpoint + "/" + userId + "?access-token=" + token;

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject profileJson = response.getJSONObject("profile");
                        Profile profile = ProfileJsonParser.parserJsonProfile(profileJson);
                        if (profileListener != null) {
                            profileListener.onRefreshProfile(profile);
                        }
                    } catch (JSONException e) {
                        System.out.println("Error parsing profile JSON: " + e.getMessage());
                        if (profileListener != null) {
                            profileListener.onRefreshProfile(null);
                        }
                    }
                },
                error -> {
                    System.out.println("Error getting profile: " + error.getMessage());
                    if (profileListener != null) {
                        profileListener.onRefreshProfile(null);
                    }
                });
        volleyQueue.add(request);
    }

    public void createProfileAPI(final Profile profile, final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("first_name", profile.getFirstName());
            jsonBody.put("last_name", profile.getLastName());
            jsonBody.put("phone", profile.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = createProfileEndpoint + "?access-token=" + token;
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, jsonBody,
                response -> {
                    // Atualiza o perfil depois de criar
                    getProfileAPI(context);
                },
                error -> System.out.println("Error creating profile: " + error.getMessage()));
        volleyQueue.add(req);
    }

    public void updateProfileAPI(final Profile profile, final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("first_name", profile.getFirstName());
            jsonBody.put("last_name", profile.getLastName());
            jsonBody.put("phone", profile.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = updateProfileEndpoint + "/" + profile.getId() + "?access-token=" + token;
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.PUT, url, jsonBody,
                response -> {
                    // Atualiza o perfil depois de editar
                    getProfileAPI(context);
                },
                error -> System.out.println("Error updating profile: " + error.getMessage()));
        volleyQueue.add(req);
    }

    public void deleteProfileAPI(final int profileId, final Context context) {
        RedefineEndpoints(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String url = deleteProfileEndpoint + "/" + profileId + "?access-token=" + token;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.DELETE, url,
                s -> {
                    if (profileListener != null) {
                        profileListener.onRefreshProfile(null);
                    }
                },
                error -> System.out.println("Error deleting profile: " + error.getMessage()));
        volleyQueue.add(stringRequest);
    }

    public void signupAPI(final User user, final Context context) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", user.getUsername());
            jsonBody.put("email", user.getEmail());
            jsonBody.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, signupEndpoint, jsonBody,
                response -> {
                    System.out.println("Signup successful: " + response);
                },
                error -> {
                    System.out.println("Signup failed: " + error.getMessage());
                });

        volleyQueue.add(req);
    }



    private void RedefineEndpoints(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String baseUrl = prefs.getString(KEY_BASE_URL, BASE_URL);

        this.signupEndpoint = baseUrl + "/user/register";

        this.userRequestsEndpoint = baseUrl + "/request/requests";
        this.getRequestEndpoint = baseUrl + "/request/request";
        this.notRatedRequestsEndpoint = baseUrl + "/request/notrated/requests";
        this.createRequestEndpoint = baseUrl + "/request/createrequest";
        this.updateRequestEndpoint = baseUrl + "/request/updaterequest";
        this.deleteRequestEndpoint = baseUrl + "/request/deleterequest";

        this.getUserRatingsEndpoint = baseUrl + "/rating/ratings";
        this.getRatingEndpoint = baseUrl + "/rating/rating";
        this.createRatingEndpoint = baseUrl + "/rating/createrating";
        this.updateRatingEndpoint = baseUrl + "/rating/updaterating";
        this.deleteRatingEndpoint = baseUrl + "/rating/deleterating";

        this.getUserProfileEndpoint = baseUrl + "/profile/profile";
        this.createProfileEndpoint = baseUrl + "/profile/createprofile";
        this.updateProfileEndpoint = baseUrl + "/profile/updateprofile";
        this.deleteProfileEndpoint = baseUrl + "/profile/deleteprofile";
    }
}
