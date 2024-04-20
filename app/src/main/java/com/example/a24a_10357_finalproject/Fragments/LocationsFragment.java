package com.example.a24a_10357_finalproject.Fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a24a_10357_finalproject.Interfaces.LocationCallBack;
import com.example.a24a_10357_finalproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationsFragment extends Fragment {
    private Context context; // Store the context of the fragment
    private GoogleMap googleMap;
    private DatabaseReference ownerRef;
    private LocationCallBack locationCallBack;
    private Map<Marker, DogDetails> markerDogDetailsMap = new HashMap<>(); //Store markers and their associated dog details

    public LocationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        ownerRef = FirebaseDatabase.getInstance().getReference().child("Dog Owners");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.locations_MAP_map); //find the map view
        initZoom(mapFragment);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // Store the context when fragment is attached
    }

    @Override
    public void onResume() {
        super.onResume();
        addDogsLocations(); // Add markers
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null; // Release the stored context when fragment is detached
    }

    private void initZoom(SupportMapFragment mapFragment) { //initializes the GoogleMap object and moves the camera to a specific location
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap map) {
                googleMap = map;
                LatLng israelLocation = new LatLng(32.109333, 34.855499); // Center of Israel
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israelLocation, 10.5f));
            }
        });
    }

    private void addDogsLocations() { // Retrieves dog owner data and adds a marker to the map
        ownerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    plotAddress(dataSnapshot.child("address").getValue(String.class),
                            dataSnapshot.child("dogName").getValue(String.class),
                            dataSnapshot.child("dogAge").getValue(String.class),
                            dataSnapshot.child("dogBreed").getValue(String.class),
                            dataSnapshot.child("profilePicture").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void plotAddress(String address, String dogName, String dogAge, String dogBreed, String profilePicture) { // Convert an address string into latitude and longitude coordinates
        if (this.context == null) {
            return; // Check if context is null to avoid NullPointerException
        }
        Geocoder geocoder = new Geocoder(this.context); // Transforming an address or other description of a location into a (latitude, longitude) coordinate
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1); // Contains lat and lon coordinates of the address typing by the user
            if (!addressList.isEmpty()) {
                Address location = addressList.get(0); // The first result only
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
                DogDetails dogDetails = new DogDetails(dogName, dogAge, dogBreed, profilePicture);
                markerDogDetailsMap.put(marker, dogDetails);
                setMarkerClickListener(marker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMarkerClickListener(Marker marker) { // Sets a click listener for each marker on the map
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clickedMarker) {
                DogDetails dogDetails = markerDogDetailsMap.get(clickedMarker);
                if (dogDetails != null) // Sets an interface to communicate with the parent fragment
                    locationCallBack.onLocationClicked(dogDetails.dogName, dogDetails.dogAge, dogDetails.dogBreed, dogDetails.profilePicture);
                return false;
            }
        });
    }

    public void setLocationCallBack(LocationCallBack locationCallBack) {
        this.locationCallBack = locationCallBack;
    }

    private static class DogDetails { //Encapsulates information about a dog
        String dogName;
        String dogAge;
        String dogBreed;
        String profilePicture;

        DogDetails(String dogName, String dogAge, String dogBreed, String profilePicture) {
            this.dogName = dogName;
            this.dogAge = dogAge;
            this.dogBreed = dogBreed;
            this.profilePicture = profilePicture;
        }
    }
}