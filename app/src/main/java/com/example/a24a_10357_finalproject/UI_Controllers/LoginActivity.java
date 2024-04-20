package com.example.a24a_10357_finalproject.UI_Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.a24a_10357_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText login_TIET_email;
    private TextInputEditText login_TIET_password;
    private MaterialButton login_BTN_login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }

    private void login() { // Handles the login process by retrieving the email and password entered by the user
        String email = String.valueOf(login_TIET_email.getText());
        String password = String.valueOf(login_TIET_password.getText());
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initViews() {
        login_BTN_login.setOnClickListener(v -> {
            String email = String.valueOf(login_TIET_email.getText());
            String password = String.valueOf(login_TIET_password.getText());
            checkIsEmpty(email);
            checkIsEmpty(password);
            login();
        });
    }

    private void checkIsEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void findViews() {
        login_TIET_email = findViewById(R.id.login_TIET_email);
        login_TIET_password = findViewById(R.id.login_TIET_password);
        login_BTN_login = findViewById(R.id.login_BTN_login);
    }
}