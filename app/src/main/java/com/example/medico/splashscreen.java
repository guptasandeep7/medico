package com.example.medico;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


    public class splashscreen extends AppCompatActivity {
        private final int SPLASH_DISPLAY_TIME = 3000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.splashscreen);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Intent for start mainActivity
                    Intent splashIntent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(splashIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_TIME);

        }


}
