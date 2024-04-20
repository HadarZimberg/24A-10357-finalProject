package com.example.a24a_10357_finalproject.Models;

public class DogWalker {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String profilePicture = "";
    private double rating = 0.0;
    private double totalRating = 0.0;
    private int numOfRaters = 0;

    public DogWalker() {
    }

    public String getId() {
        return id;
    }

    public DogWalker setId(String id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public DogWalker setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public DogWalker setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DogWalker setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public DogWalker setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public double getRating() {
        return rating;
    }

    public DogWalker setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public int getNumOfRaters() {
        return numOfRaters;
    }

    public DogWalker setNumOfRaters(int numOfRaters) {
        this.numOfRaters = numOfRaters;
        return this;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public DogWalker setTotalRating(double totalRating) {
        this.totalRating = totalRating;
        return this;
    }
}
