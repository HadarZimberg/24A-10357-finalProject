package com.example.a24a_10357_finalproject.Models;

public class DogOwner {

    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String dogName = "";
    private String dogAge = "";
    private String dogBreed = "";
    private String profilePicture = "";
    private String address = "";

    public DogOwner() {
    }

    public String getId() {
        return id;
    }

    public DogOwner setId(String id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public DogOwner setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public DogOwner setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DogOwner setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getDogName() {
        return dogName;
    }

    public DogOwner setDogName(String dogName) {
        this.dogName = dogName;
        return this;
    }

    public String getDogAge() {
        return dogAge;
    }

    public DogOwner setDogAge(String dogAge) {
        this.dogAge = dogAge;
        return this;
    }

    public String getDogBreed() {
        return dogBreed;
    }

    public DogOwner setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public DogOwner setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public DogOwner setAddress(String address) {
        this.address = address;
        return this;
    }
}
