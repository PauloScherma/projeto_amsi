package com.example.projeto.utils;

import com.example.projeto.modelo.Profile;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileJsonParser {
    public static Profile parserJsonProfile(JSONObject response) {
        Profile auxProfile = null;
        try {
            int id = response.getInt("id");
            int userId = response.getInt("user_id");
            String firstName = response.getString("first_name");
            String lastName = response.getString("last_name");
            String phone = response.getString("phone");
            String createdAt = response.optString("created_at");
            String updatedAt = response.optString("updated_at");
            auxProfile = new Profile(id, userId, firstName, lastName, phone, createdAt, updatedAt);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return auxProfile;
    }
}
