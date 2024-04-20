package com.example.a24a_10357_finalproject.UI_Controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a24a_10357_finalproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private MaterialButton start_BTN_login;
    private MaterialButton start_BTN_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly:
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        start_BTN_login.setOnClickListener(v -> changeActivity(LoginActivity.class));
        start_BTN_registration.setOnClickListener(v -> changeActivity(RegistrationActivity.class));
    }

    private void changeActivity(Class<?> next) {
        Intent intent = new Intent(this, next);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        start_BTN_login = findViewById(R.id.start_BTN_login);
        start_BTN_registration = findViewById(R.id.start_BTN_registration);
    }
}