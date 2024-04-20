package com.example.a24a_10357_finalproject.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.a24a_10357_finalproject.R;
import com.example.a24a_10357_finalproject.UI_Controllers.StartActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class HomepageFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;
    private DatabaseReference ownerRef;
    private DatabaseReference walkerRef;
    private MaterialButton homepage_BTN_logOut;
    private MaterialTextView homepage_TXT_heading;
    private AppCompatImageView homepage_IMG_images;
    private static final int DURATION = 1000; // Duration of the animation in milliseconds
    private static final int BACKGROUND_CHANGE_INTERVAL = 5000; // Change image every 5 seconds
    private int currentBackgroundIndex = 0;
    private int[] backgrounds = {
            R.drawable.imageview1, R.drawable.imageview2, R.drawable.imageview3,
            R.drawable.imageview4,R.drawable.imageview5,R.drawable.imageview6,
            R.drawable.imageview7,R.drawable.imageview8,R.drawable.imageview9};
    private Timer backgroundTimer;

    public HomepageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            ownerRef = FirebaseDatabase.getInstance().getReference().child("Dog Owners").child(userId);
            walkerRef = FirebaseDatabase.getInstance().getReference().child("Dog Walkers").child(userId);
        }

        findViews(view);
        initLogOutButton();
        setUserName();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserName();
        startBackgroundChangeTask();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopBackgroundChangeTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopBackgroundChangeTask();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopBackgroundChangeTask();
    }

    private void startBackgroundChangeTask() {
        backgroundTimer = new Timer(); // Creates a timer to change background every few seconds
        backgroundTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> { // Change background to the next image
                    fadeBackground();
                });
            }
        }, 0, BACKGROUND_CHANGE_INTERVAL); // Start immediately and repeat every BACKGROUND_CHANGE_INTERVAL milliseconds
    }

    private void fadeBackground() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(DURATION);

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(DURATION);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { // Start from the beginning
                homepage_IMG_images.setImageResource(backgrounds[currentBackgroundIndex]);
                currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;
                homepage_IMG_images.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        homepage_IMG_images.startAnimation(fadeOut);
    }

    private void stopBackgroundChangeTask() { // Cancel the timer task if it's running
        if (backgroundTimer != null) {
            backgroundTimer.cancel();
            backgroundTimer = null;
        }
    }

    private void setUserName() {
        setName(walkerRef);
        setName(ownerRef);
    }

    private void setName(DatabaseReference userRef) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    if (firstName == null) // Means its not the correct type of reference
                        return;
                    else
                        homepage_TXT_heading.setText("Hello " + firstName + "!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void findViews(View view) {
        homepage_BTN_logOut = view.findViewById(R.id.homepage_BTN_logOut);
        homepage_TXT_heading = view.findViewById(R.id.homepage_TXT_heading);
        homepage_IMG_images = view.findViewById(R.id.homepage_IMG_images);
    }

    private void initLogOutButton() { // Defines log out's click listener to sign out the user from Firebase
        homepage_BTN_logOut.setOnClickListener(v -> {
            auth.signOut();
            stopBackgroundChangeTask();
            Intent intent = new Intent(this.getActivity(), StartActivity.class);
            startActivity(intent);
            this.getActivity().finish();
        });
    }
}