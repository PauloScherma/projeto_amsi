package com.example.projeto.utils;

import com.example.projeto.modelo.Priority;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestJsonParser {
    public static ArrayList<Request> parserJsonRequests(JSONArray response) {
        ArrayList<Request> requests= new ArrayList<>();
        for (int i=0;i<response.length();i++){
            try {
                JSONObject request = (JSONObject) response.get(i);
                int id = request.getInt("id");
                int customerId=request.getInt("customer_id");
                String title=request.getString("title");
                String description=request.getString("description");
                Priority priority=Priority.valueOf(request.getString("priority").toUpperCase());
                Status status= Status.valueOf(request.getString("status").toUpperCase());
                int currentTechnicianId=request.optInt("current_technician_id");
                String canceledAt=request.optString("canceled_at");
                int canceledBy=request.optInt("canceled_by");
                String createdAt=request.optString("created_at");
                String updatedAt=request.optString("updated_at");
                Request auxRequest=new Request(id, customerId, title, description, priority, status, currentTechnicianId, canceledAt, canceledBy, createdAt, updatedAt);
                requests.add(auxRequest);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return requests;
    }

    public static Request parserJsonRequest(JSONObject response) {
        Request auxRequest=null;
        try {
            int id = response.getInt("id");
            int customerId=response.getInt("customer_id");
            String title=response.getString("title");
            String description=response.optString("description");
            Priority priority=Priority.valueOf(response.getString("priority").toUpperCase());
            Status status= Status.valueOf(response.getString("status").toUpperCase());
            int currentTechnicianId=response.optInt("current_technician_id");
            String canceledAt=response.optString("canceled_at");
            int canceledBy=response.optInt("canceled_by");
            String createdAt=response.optString("created_at");
            String updatedAt=response.optString("updated_at");
            auxRequest=new Request(id, customerId, title, description, priority, status, currentTechnicianId, canceledAt, canceledBy, createdAt, updatedAt);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return auxRequest;
    }
}
