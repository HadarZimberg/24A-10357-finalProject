package com.example.a24a_10357_finalproject.Models;

import java.util.HashMap;

public class Owners {

    private static Owners instance;
    private HashMap<String, DogOwner> allDogOwners = new HashMap<>();

    private Owners() {
    }

    public static Owners getInstance() {
        if (instance == null)
            instance = new Owners();
        return instance;
    }

    public HashMap<String, DogOwner> getAllDogOwners() {
        return allDogOwners;
    }

    public Owners setAllDogOwners(HashMap<String, DogOwner> allDogOwners) {
        this.allDogOwners = allDogOwners;
        return this;
    }
}
