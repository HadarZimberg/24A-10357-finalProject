package com.example.a24a_10357_finalproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.a24a_10357_finalproject.R;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataFragment extends Fragment {
    private RelativeLayout data_RL_text;
    private RelativeLayout data_RL_data;
    private CircleImageView data_IMG_picture;
    private MaterialTextView data_LBL_name;
    private MaterialTextView data_LBL_age;
    private MaterialTextView data_LBL_breed;


    public DataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_data, container, false);

        findViews(view);

        return view;
    }

    public void displayDogDetails(String dogName, String dogAge, String dogBreed,String profilePicture) { // Updates the view according to the information received from the location fragment
        data_LBL_age.setText(dogAge);
        data_LBL_name.setText(dogName);
        data_LBL_breed.setText(dogBreed);
        data_RL_text.setVisibility(View.INVISIBLE);
        data_RL_data.setVisibility(View.VISIBLE);
        data_IMG_picture.setVisibility(View.VISIBLE);
        if (!Objects.equals(profilePicture, "")) // If the user has profile picture
            Picasso.get().load(profilePicture).into(data_IMG_picture);
        else
            data_IMG_picture.setImageResource(R.drawable.dog_profile);
    }

    private void findViews(View view) {
        data_RL_text = view.findViewById(R.id.data_RL_text);
        data_IMG_picture = view.findViewById(R.id.data_IMG_picture);
        data_RL_data = view.findViewById(R.id.data_RL_data);
        data_LBL_name = view.findViewById(R.id.data_LBL_name);
        data_LBL_age = view.findViewById(R.id.data_LBL_age);
        data_LBL_breed = view.findViewById(R.id.data_LBL_breed);
    }
}