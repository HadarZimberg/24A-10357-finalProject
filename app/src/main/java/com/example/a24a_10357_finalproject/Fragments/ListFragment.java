package com.example.a24a_10357_finalproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a24a_10357_finalproject.Adapters.WalkersAdapter;
import com.example.a24a_10357_finalproject.Interfaces.RatingCallBack;
import com.example.a24a_10357_finalproject.Models.DogWalkerItem;
import com.example.a24a_10357_finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private RecyclerView list_LST_walkers;
    private DatabaseReference walkersRef;
    private ArrayList<DogWalkerItem> walkersList;
    private WalkersAdapter walkersAdapter;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        walkersRef = FirebaseDatabase.getInstance().getReference("Dog Walkers");

        findViews(view);
        initViews(view);
        addDataToList();
        updateRating();

        return view;
    }

    private void updateRating() { // Updates the rating of the dog walkers in the Realtime Database when a user rates them
        walkersAdapter.setCallback(new RatingCallBack() {
            @Override
            public void onRatingBarClicked(DogWalkerItem dogWalkerItem, float rating) {
                walkersRef.child(dogWalkerItem.getuID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            double currentRating = snapshot.child("rating").getValue(Double.class);
                            double currentTotalRating = snapshot.child("totalRating").getValue(Double.class);
                            int numOfRaters = snapshot.child("numOfRaters").getValue(Integer.class);

                            double newTotalRating = currentTotalRating + (double)rating; // Calculate new total rating
                            numOfRaters++; // Increment the number of raters
                            double newAverageRating = newTotalRating / (double)numOfRaters; // Calculate new average rating
                            newAverageRating = Math.round(newAverageRating * 100.0) / 100.0;

                            // Update database:
                            walkersRef.child(dogWalkerItem.getuID()).child("rating").setValue(newAverageRating);
                            walkersRef.child(dogWalkerItem.getuID()).child("totalRating").setValue(newTotalRating);
                            walkersRef.child(dogWalkerItem.getuID()).child("numOfRaters").setValue(numOfRaters);

                            // Update local model and notify adapter:
                            dogWalkerItem.setRating(newAverageRating);
                            dogWalkerItem.setNumberOfRaters(numOfRaters);
                            walkersAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    private void addDataToList() { // Adds data to the list of dog walkers retrieved from the Realtime Database
        walkersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                walkersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DogWalkerItem dogWalkerItem = new DogWalkerItem()
                            .setFullName(dataSnapshot.child("firstName").getValue(String.class) + " "
                                    + dataSnapshot.child("lastName").getValue(String.class))
                            .setPhone(dataSnapshot.child("phoneNumber").getValue(String.class))
                            .setProfilePicture(dataSnapshot.child("profilePicture").getValue(String.class))
                            .setRating(dataSnapshot.child("rating").getValue(Double.class))
                            .setuID(dataSnapshot.getKey());

                    walkersList.add(dogWalkerItem);
                }
                walkersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initViews(View view) {
        list_LST_walkers.setHasFixedSize(true);
        list_LST_walkers.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        walkersList = new ArrayList<>();
        walkersAdapter = new WalkersAdapter(this.getActivity(), walkersList);
        list_LST_walkers.setAdapter(walkersAdapter);
    }

    private void findViews(View view) {
        list_LST_walkers = view.findViewById(R.id.list_LST_walkers);
    }
}