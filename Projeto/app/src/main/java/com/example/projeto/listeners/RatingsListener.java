package com.example.projeto.listeners;

import com.example.projeto.modelo.Rating;

import java.util.ArrayList;

public interface RatingsListener {
    void onRefreshListaRatings(ArrayList<Rating> ratings);

}
