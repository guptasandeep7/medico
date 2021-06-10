package com.example.medico;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static com.example.medico.MainActivity.getPatientId;


public class UserProfile extends AppCompatActivity {

    private String patientId;
    private dataholder userData;
    private TextView userName,userProfession,userGender,userDob,userNumber,userAddress;
    private ShapeableImageView userPhoto;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

        patientId = getPatientId();

        userName = (TextView)findViewById(R.id.userName);
        userProfession = (TextView)findViewById(R.id.userProfession);
        userGender = (TextView)findViewById(R.id.userGender);
        userDob = (TextView)findViewById(R.id.userDob);
        userNumber = (TextView)findViewById(R.id.userNumber);
        userAddress = (TextView)findViewById(R.id.userAddress);
        userPhoto = (ShapeableImageView)findViewById(R.id.userPhoto);



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(patientId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userData = snapshot.getValue(dataholder.class);
                userName.setText(userData.getName());
                userProfession.setText(userData.getProfession());
                userGender.setText(userData.getGender());
                userDob.setText(userData.getDate());
                userNumber.setText(userData.getNumber());
                userAddress.setText(userData.getAddress());

                if(userData.getUri()==null||userData.getUri()==""){

                }
                else{
                    Context context = userPhoto.getContext();
                    Glide.with(context)
                            .load(userData.getUri())
                            .asBitmap()
                            .fitCenter()
                            .into(userPhoto);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }
}
