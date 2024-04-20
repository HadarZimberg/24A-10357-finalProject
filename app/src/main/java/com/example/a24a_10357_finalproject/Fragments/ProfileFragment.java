package com.example.a24a_10357_finalproject.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a24a_10357_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private String userId;
    private DatabaseReference ownerRef;
    private DatabaseReference walkerRef;
    private CircleImageView profile_IMG_picture;
    private MaterialTextView profile_MTV_mail;
    private MaterialTextView profile_MTV_id, profile_MTV_id1;
    private MaterialTextView profile_MTV_firstname, profile_MTV_firstname1;
    private MaterialTextView profile_MTV_lastname, profile_MTV_lastname1;
    private MaterialTextView profile_MTV_phone, profile_MTV_phone1;
    private MaterialTextView profile_MTV_dogname;
    private MaterialTextView profile_MTV_dogage;
    private MaterialTextView profile_MTV_dogbreed;
    private MaterialTextView profile_MTV_address;
    private MaterialTextView profile_MTV_rating;
    private MaterialTextView profile_MTV_numOfRaters;
    private LinearLayout profile_LL_walker;
    private LinearLayout profile_LL_owner;
    private Uri imageUri;
    private ActivityResultLauncher<String> galleryLauncher;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            ownerRef = FirebaseDatabase.getInstance().getReference().child("Dog Owners").child(userId);
            walkerRef = FirebaseDatabase.getInstance().getReference().child("Dog Walkers").child(userId);
        }

        initActivityResultLauncher(); // Initialize the ActivityResultLauncher for adding photo
        findViews(view);
        initViews(view);
        loadProfilePicture();
        loadUserInfo(ownerRef, walkerRef);

        return view;
    }

    private void initActivityResultLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        getImage(); // Retrieves the selected image from the device's gallery and displaying it
                        uploadToStorage(); //uploads the image to the firebase storage
                    }
                });
    }

    private void getImage() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri); // Retrieve the selected image from the device's gallery using the imageUri
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri); // Get the image's orientation using ExifInterface with an input stream
            if (inputStream != null) {
                ExifInterface exif = new ExifInterface(inputStream);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED); // Reads the orientation attribute from the Exif data
                inputStream.close();
                bitmap = rotateBitmap(bitmap, orientation); // Rotate the bitmap according to the orientation state
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        profile_IMG_picture.setImageBitmap(bitmap);
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) { // Method to rotate the bitmap based on the orientation
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
                return bitmap;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void uploadToStorage() {
        if (imageUri == null) {
            Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();

        String imagePath = "images/" + user + "/" + UUID.randomUUID().toString(); // Define the path in Firebase Storage where the images will be stored

        FirebaseStorage.getInstance().getReference(imagePath) // Upload the image to Firebase Storage
                .putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // Image uploaded successfully
                        addUriToUser(task, ownerRef); // Add the Uri to Realtime Database if the user is owner
                        addUriToUser(task, walkerRef); // Add the Uri to Realtime Database if the user is walker
                        Toast.makeText(getActivity(), "Photo Uploaded!", Toast.LENGTH_SHORT).show();
                    }
                    else { // Handle the error
                        String errorMessage = task.getException().getLocalizedMessage();
                        Toast.makeText(getActivity(), "Failed to upload image: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }

    private void loadUserInfo(DatabaseReference ownerRef, DatabaseReference walkerRef) {
        profile_MTV_mail.setText(user.getEmail());
        ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    if (firstName != null) {
                        profile_LL_owner.setVisibility(View.VISIBLE);
                        profile_LL_walker.setVisibility(View.INVISIBLE);
                        profile_MTV_id.setText(dataSnapshot.child("id").getValue(String.class));
                        profile_MTV_firstname.setText(dataSnapshot.child("firstName").getValue(String.class));
                        profile_MTV_lastname.setText(dataSnapshot.child("lastName").getValue(String.class));
                        profile_MTV_phone.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                        profile_MTV_address.setText(dataSnapshot.child("address").getValue(String.class));
                        profile_MTV_dogname.setText(dataSnapshot.child("dogName").getValue(String.class));
                        profile_MTV_dogage.setText(dataSnapshot.child("dogAge").getValue(String.class));
                        profile_MTV_dogbreed.setText(dataSnapshot.child("dogBreed").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle cancellation
            }
        });

        walkerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    if (firstName != null) {
                        profile_LL_owner.setVisibility(View.INVISIBLE);
                        profile_LL_walker.setVisibility(View.VISIBLE);
                        profile_MTV_id1.setText(dataSnapshot.child("id").getValue(String.class));
                        profile_MTV_firstname1.setText(dataSnapshot.child("firstName").getValue(String.class));
                        profile_MTV_lastname1.setText(dataSnapshot.child("lastName").getValue(String.class));
                        profile_MTV_phone1.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                        profile_MTV_rating.setText(String.valueOf(dataSnapshot.child("rating").getValue(Double.class)));
                        profile_MTV_numOfRaters.setText(String.valueOf(dataSnapshot.child("numOfRaters").getValue(Integer.class)));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle cancellation
            }
        });
    }

    private void initViews(View view) {
        profile_IMG_picture.setOnClickListener(v -> galleryLauncher.launch("image/*")); // Launch the gallery to pick an image
    }

    private void findViews(View view) {
        profile_IMG_picture = view.findViewById(R.id.profile_IMG_picture);
        profile_MTV_mail = view.findViewById(R.id.profile_MTV_mail);
        profile_MTV_id = view.findViewById(R.id.profile_MTV_id);
        profile_MTV_firstname = view.findViewById(R.id.profile_MTV_firstname);
        profile_MTV_lastname = view.findViewById(R.id.profile_MTV_lastname);
        profile_MTV_phone = view.findViewById(R.id.profile_MTV_phone);
        profile_MTV_dogname = view.findViewById(R.id.profile_MTV_dogname);
        profile_MTV_dogage = view.findViewById(R.id.profile_MTV_dogage);
        profile_MTV_dogbreed = view.findViewById(R.id.profile_MTV_dogbreed);
        profile_MTV_address = view.findViewById(R.id.profile_MTV_address);
        profile_LL_walker = view.findViewById(R.id.profile_LL_walker);
        profile_LL_owner = view.findViewById(R.id.profile_LL_owner);
        profile_MTV_id1 = view.findViewById(R.id.profile_MTV_id1);
        profile_MTV_firstname1 = view.findViewById(R.id.profile_MTV_firstname1);
        profile_MTV_lastname1 = view.findViewById(R.id.profile_MTV_lastname1);
        profile_MTV_phone1 = view.findViewById(R.id.profile_MTV_phone1);
        profile_MTV_rating = view.findViewById(R.id.profile_MTV_rating);
        profile_MTV_numOfRaters = view.findViewById(R.id.profile_MTV_numOfRaters);
    }

    private void loadProfilePicture() {
        setPicture(ownerRef);
        setPicture(walkerRef);
    }

    private void setPicture(DatabaseReference userRef) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    if (firstName != null) {
                        String profilePictureUrl = dataSnapshot.child("profilePicture").getValue(String.class);
                        if (profilePictureUrl != null && !profilePictureUrl.isEmpty())
                            Glide.with(requireContext()).load(profilePictureUrl).into(profile_IMG_picture); // Load the profile picture into the CircleImageView using the URL
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addUriToUser(Task<UploadTask.TaskSnapshot> prevTask, DatabaseReference ref) { // Adds the uploaded image URL to the user's profile picture field in the Realtime Database
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    if (firstName != null) {
                        prevTask.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful())
                                    ref.child("profilePicture").setValue(task.getResult().toString());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}