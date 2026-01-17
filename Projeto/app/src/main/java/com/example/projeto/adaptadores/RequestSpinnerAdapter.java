package com.example.projeto.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.projeto.R;
import com.example.projeto.modelo.Request;

import java.util.ArrayList;

public class RequestSpinnerAdapter extends ArrayAdapter<Request> {

    public RequestSpinnerAdapter(@NonNull Context context, @NonNull ArrayList<Request> requestsList) {
        super(context, 0, requestsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_request, parent, false);
        }

        Request currentRequest = getItem(position);

        if (currentRequest != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(currentRequest.getTitle());
        }

        return convertView;
    }
}
