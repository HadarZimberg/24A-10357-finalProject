package com.example.a24a_10357_finalproject.Models;

public class DogWalkerItem { // Item for RecycleView
    String uID = "";
    private String fullName = "";
    private String phone = "";
    private String profilePicture = "";
    private double rating = 0.0;
    private double totalRating = 0.0; // Total sum of ratings
    private int numberOfRaters = 0;   // Number of raters

    public DogWalkerItem() {
    }

    public String getFullName() {
        return fullName;
    }

    public DogWalkerItem setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public DogWalkerItem setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public DogWalkerItem setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public double getRating() {
        return rating;
    }

    public DogWalkerItem setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public DogWalkerItem setTotalRating(double totalRating) {
        this.totalRating = totalRating;
        return this;
    }

    public int getNumberOfRaters() {
        return numberOfRaters;
    }

    public DogWalkerItem setNumberOfRaters(int numberOfRaters) {
        this.numberOfRaters = numberOfRaters;
        return this;
    }

    public String getuID() {
        return uID;
    }

    public DogWalkerItem setuID(String uID) {
        this.uID = uID;
        return this;
    }
}
