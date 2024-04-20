package com.example.a24a_10357_finalproject.UI_Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.a24a_10357_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText registration_TIET_email;
    private TextInputEditText registration_TIET_password;
    private MaterialButton registration_BTN_signUp;
    private RadioButton registration_RB_dogWalker;
    private RadioButton registration_RB_dogOwner;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }

    private void createUser() {
        String email = String.valueOf(registration_TIET_email.getText());
        String password = String.valueOf(registration_TIET_password.getText());
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            changeActivity();
                        }
                        else {
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(), "Password should be at least 6 chars", Toast.LENGTH_LONG).show();
                            }
                            catch (FirebaseAuthEmailException e){
                                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                            }
                            catch (FirebaseAuthException e){
                                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void changeActivity() {
        Intent intent;
        if (registration_RB_dogWalker.isChecked())
            intent = new Intent(RegistrationActivity.this, DogWalkerSignUpActivity.class);
        else // registration_RB_dogOwner.isChecked()
            intent = new Intent(RegistrationActivity.this, DogOwnerSignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {
        registration_BTN_signUp.setOnClickListener(v -> {
            String email = String.valueOf(registration_TIET_email.getText());
            String password = String.valueOf(registration_TIET_password.getText());
            checkIsEmpty(email);
            checkIsEmpty(password);
            createUser();
        });
    }

    private void checkIsEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void findViews() {
        registration_TIET_email = findViewById(R.id.registration_TIET_email);
        registration_TIET_password = findViewById(R.id.registration_TIET_password);
        registration_BTN_signUp = findViewById(R.id.registration_BTN_signUp);
        registration_RB_dogWalker = findViewById(R.id.registration_RB_dogWalker);
        registration_RB_dogOwner = findViewById(R.id.registration_RB_dogOwner);
    }
}