package com.example.a24a_10357_finalproject.UI_Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.a24a_10357_finalproject.Models.DogOwner;
import com.example.a24a_10357_finalproject.Models.Owners;
import com.example.a24a_10357_finalproject.databinding.ActivityDogOwnerSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DogOwnerSignUpActivity extends AppCompatActivity {

    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String dogName = "";
    private String dogAge = "";
    private String dogBreed = "";
    private String address = "";
    private @NonNull ActivityDogOwnerSignUpBinding binding;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDogOwnerSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNewDogOwner();
    }

    private void initInfo() {
        id = binding.ownerETId.getText().toString();
        firstName = binding.ownerETFirstname.getText().toString();
        lastName = binding.ownerETLastname.getText().toString();
        phoneNumber = binding.ownerETPhone.getText().toString();
        dogName = binding.ownerETDogname.getText().toString();
        dogAge = binding.ownerETDogage.getText().toString();
        dogBreed = binding.ownerETDogbreed.getText().toString();
        address = binding.ownerETAddress.getText().toString();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createNewDogOwner() { // Handles the sign-up process for a new dog owner
        Owners dogOwners = Owners.getInstance();
        binding.ownerBTNSignup.setOnClickListener(v -> {
            initInfo();
            String ownerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            dogOwners.getAllDogOwners().put(
                    ownerId,
                    new DogOwner()
                            .setId(id)
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .setPhoneNumber(phoneNumber)
                            .setDogName(dogName)
                            .setDogAge(dogAge)
                            .setDogBreed(dogBreed)
                            .setProfilePicture("")
                            .setAddress(address)
            );

            db = FirebaseDatabase.getInstance();
            ref = db.getReference("Dog Owners").child(ownerId);
            ref.setValue(dogOwners.getAllDogOwners().get(ownerId)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) // Database operation was successful, change activity
                    changeActivity();
                else // Database operation failed, show error message
                    Toast.makeText(this, "Failed to create dog owner: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}