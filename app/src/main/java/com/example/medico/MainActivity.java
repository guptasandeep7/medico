package com.example.medico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;
    private String mUsername;
    private TextView username_TextView;
    private Button signoutButton;
    private LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (LinearLayout)findViewById(R.id.mainActivity);
        username_TextView = (TextView)findViewById(R.id.username);
        signoutButton = (Button)findViewById(R.id.signoutButton);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //sign in
                    onSignedINInisilise(user.getDisplayName());
                    mainLayout.setVisibility(View.VISIBLE);

                } else {
                    //sign out
                    onSignedOutInisilise();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    ))
                                    .setLogo(R.drawable.logo)
                                    .setTheme(R.style.Theme_Medico)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this);
                finish();

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedOutInisilise() {

        mUsername = "Unknown";
        username_TextView.setText(mUsername);

    }

    private void onSignedINInisilise(String username) {

        mUsername = username;
        username_TextView.setText(mUsername);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "You Successfully Signed In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Sign In Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}