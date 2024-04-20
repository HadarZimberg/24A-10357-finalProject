package com.example.a24a_10357_finalproject.UI_Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a24a_10357_finalproject.Models.DogWalker;
import com.example.a24a_10357_finalproject.Models.Walkers;
import com.example.a24a_10357_finalproject.databinding.ActivityDogWalkerSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DogWalkerSignUpActivity extends AppCompatActivity {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private ActivityDogWalkerSignUpBinding binding;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDogWalkerSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNewDogWalker();
    }

    private void initInfo() {
        id = binding.walkerETId.getText().toString();
        firstName = binding.walkerETFirstname.getText().toString();
        lastName = binding.walkerETLastname.getText().toString();
        phoneNumber = binding.walkerETPhone.getText().toString();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createNewDogWalker() { // Handles the sign-up process for a new dog walker
        Walkers dogWalkers = Walkers.getInstance();
        binding.walkerBTNSignup.setOnClickListener(v -> {
            initInfo();
            String walkerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            dogWalkers.getAllDogWalkers().put(
                    walkerId,
                    new DogWalker()
                            .setId(id)
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .setPhoneNumber(phoneNumber)
                            .setProfilePicture("")
                            .setRating(0.0)
                            .setNumOfRaters(0)
                            .setTotalRating(0.0)
            );

            db = FirebaseDatabase.getInstance();
            ref = db.getReference("Dog Walkers").child(walkerId);
            ref.setValue(dogWalkers.getAllDogWalkers().get(walkerId)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) // Database operation was successful, change activity
                    changeActivity();
                else // Database operation failed
                    Toast.makeText(this, "Failed to create dog walker: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}