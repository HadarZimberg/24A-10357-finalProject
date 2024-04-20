package com.example.a24a_10357_finalproject.Interfaces;

import com.example.a24a_10357_finalproject.Models.DogWalkerItem;

public interface RatingCallBack { // Defines a contract for handling rating bar click events
    void onRatingBarClicked(DogWalkerItem dogWalkerItem, float rating);
}
