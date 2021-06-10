package com.example.medico;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.medico.MainActivity.getPatientId;

public class HomeScreen extends AppCompatActivity {
public String userPhotoUrl;
private String patientId;
MenuItem imageIcon;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signoutMenu:
                AuthUI.getInstance().signOut(getApplicationContext());
                finish();
                break;
            case R.id.image:
                startActivity(new Intent(this,UserProfile.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );

        imageIcon = menu.findItem(R.id.image);

        patientId = getPatientId();
        final Bitmap[] myBitmap = {null};
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(patientId).child("uri");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userPhotoUrl = snapshot.getValue(String.class);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(userPhotoUrl);
                            myBitmap[0] = BitmapFactory.decodeStream(url.openStream());
                        } catch (IOException e) {
                            System.out.println(e);
                        }


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(userPhotoUrl==null||userPhotoUrl==""){
                                    imageIcon.setIcon(R.drawable.doctor);
                                }
                                else {


                                    imageIcon.setIcon(new BitmapDrawable(getResources(), myBitmap[0]));
                                }
                            }
                        });
                    }
                });



            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        return true;

    }



}
