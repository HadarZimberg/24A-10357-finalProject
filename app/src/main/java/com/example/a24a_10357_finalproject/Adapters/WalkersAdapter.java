package com.example.a24a_10357_finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a24a_10357_finalproject.Interfaces.RatingCallBack;
import com.example.a24a_10357_finalproject.Models.DogWalkerItem;
import com.example.a24a_10357_finalproject.R;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalkersAdapter extends RecyclerView.Adapter<WalkersAdapter.WalkersViewHolder> {

    private Context context;
    private ArrayList<DogWalkerItem> walkersList;
    private RatingCallBack ratingCallBack;

    public WalkersAdapter(Context context, ArrayList<DogWalkerItem> walkersList) {
        this.context = context;
        this.walkersList = walkersList;
    }

    @NonNull
    @Override
    public WalkersAdapter.WalkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dogwalker_item, parent, false);
        return new WalkersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalkersAdapter.WalkersViewHolder holder, int position) { // Binds data to each item view in the RecyclerView
        DogWalkerItem dogWalkerItem = walkersList.get(position);
        holder.item_LBL_name.setText(dogWalkerItem.getFullName());
        holder.item_LBL_phone.setText(dogWalkerItem.getPhone());
        holder.item_LBL_rating.setText(String.valueOf(dogWalkerItem.getRating()));
        holder.item_RTNG_rating.setRating((float) (dogWalkerItem.getRating()));
        if (!Objects.equals(dogWalkerItem.getProfilePicture(), ""))
            Picasso.get().load(dogWalkerItem.getProfilePicture()).into(holder.item_IMG_picture); // User profile picture
        else
            Picasso.get().load(R.drawable.user).into(holder.item_IMG_picture); // Default picture
        holder.item_RTNG_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    ratingCallBack.onRatingBarClicked(dogWalkerItem, rating); // Passing the values for the realtime database
                    Toast.makeText(context, "Thanks for the rating", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return walkersList == null ? 0 : walkersList.size();
    }

    public void setCallback(RatingCallBack ratingCallBack) {
        this.ratingCallBack = ratingCallBack;
    }

    public class WalkersViewHolder extends RecyclerView.ViewHolder { // Representing the view holder for each item in the RecyclerView

        private MaterialTextView item_LBL_name;
        private MaterialTextView item_LBL_phone;
        private MaterialTextView item_LBL_rating;
        private AppCompatRatingBar item_RTNG_rating;
        private CircleImageView item_IMG_picture;

        public WalkersViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View itemView) {
            item_LBL_name = itemView.findViewById(R.id.item_LBL_name);
            item_LBL_phone = itemView.findViewById(R.id.item_LBL_phone);
            item_LBL_rating = itemView.findViewById(R.id.item_LBL_rating);
            item_RTNG_rating = itemView.findViewById(R.id.item_RTNG_rating);
            item_IMG_picture = itemView.findViewById(R.id.item_IMG_picture);
        }
    }
}
