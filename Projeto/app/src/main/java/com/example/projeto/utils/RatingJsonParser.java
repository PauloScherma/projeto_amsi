package com.example.projeto.utils;

import com.example.projeto.modelo.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RatingJsonParser {
    public static ArrayList<Rating> parserJsonRatings(JSONArray response) {
        ArrayList<Rating> ratings= new ArrayList<>();
        for (int i=0;i<response.length();i++){
            try {
                JSONObject rating = (JSONObject) response.get(i);

                int id = rating.getInt("id");
                int requestId = rating.getInt("request_id");
                String title = rating.getString("title");
                String description = rating.getString("description");
                int score = rating.getInt("score");
                String createdAt = rating.getString("created_at");
                String createdBy = rating.getString("created_by");

                Rating auxRating = new Rating(id, requestId, title, description, score, createdAt, createdBy);

                ratings.add(auxRating);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return ratings;
    }

    public static Rating parserJsonRating(JSONObject response) {
        try {
            int id = response.getInt("id");
            int requestId = response.getInt("request_id");
            String title = response.getString("title");
            String description = response.getString("description");
            int score = response.getInt("score");
            String createdAt = response.getString("created_at");
            String createdBy = response.getString("created_by");

            return new Rating(id, requestId, title, description, score, createdAt, createdBy);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
