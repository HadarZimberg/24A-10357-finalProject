package com.example.a24a_10357_finalproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.a24a_10357_finalproject.Interfaces.LocationCallBack;
import com.example.a24a_10357_finalproject.R;
import com.google.android.gms.maps.MapsInitializer;

public class MapFragment extends Fragment {

    private DataFragment dataFragment;
    private LocationsFragment locationsFragment;
    private FrameLayout map_FRAME_data;
    private FrameLayout map_FRAME_locations;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapsInitializer.initialize(this.getContext(), MapsInitializer.Renderer.LATEST, renderer -> {
        });

        findViews(view);

        dataFragment = new DataFragment();
        locationsFragment = new LocationsFragment();

        locationsFragment.setLocationCallBack(new LocationCallBack() { // Sets a callback for handling location clicks in the LocationsFragment
            @Override
            public void onLocationClicked(String dogName, String dogAge, String dogBreed, String profilePicture) {
                dataFragment.displayDogDetails(dogName, dogAge, dogBreed, profilePicture); // When a marker is clicked, the details are passed to the DataFragment for display
            }
        });

        FragmentTransaction dataTransaction = getChildFragmentManager().beginTransaction();
        dataTransaction.replace(R.id.map_FRAME_data, dataFragment).addToBackStack(null).commit();

        FragmentTransaction locationsTransaction = getChildFragmentManager().beginTransaction();
        locationsTransaction.replace(R.id.map_FRAME_locations, locationsFragment).addToBackStack(null).commit();

        return view;
    }

    private void findViews(View view) {
        map_FRAME_data = view.findViewById(R.id.map_FRAME_data);
        map_FRAME_locations = view.findViewById(R.id.map_FRAME_locations);
    }
}