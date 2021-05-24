package com.example.tasksolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static int SPLASH_TIME_OUT =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!=null)
                {
                    Intent intent=new Intent(MainActivity.this,Dashboard.class);
                    startActivity(intent);
                    // startActivity(new Intent(MainActivity.this,Dashboard.class));
                }

            }
        },SPLASH_TIME_OUT);
        mAuth = FirebaseAuth.getInstance();
    }
        public void tSignIn(View view)
        {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
        public void tSignUp(View view)
        {
            startActivity(new Intent(MainActivity.this,Signup.class));
        }
    }
