package com.example.a24a_10357_finalproject.Models;

import java.util.HashMap;

public class Walkers {

    private static Walkers instance;
    private HashMap<String, DogWalker> allDogWalkers = new HashMap<>();

    private Walkers() {
    }

    public static Walkers getInstance() {
        if (instance == null)
            instance = new Walkers();
        return instance;
    }

    public HashMap<String, DogWalker> getAllDogWalkers() {
        return allDogWalkers;
    }

    public Walkers setAllDogWalkers(HashMap<String, DogWalker> allDogWalkers) {
        this.allDogWalkers = allDogWalkers;
        return this;
    }
}
