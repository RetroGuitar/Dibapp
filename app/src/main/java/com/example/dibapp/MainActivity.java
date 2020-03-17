package com.example.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.dibapp.module.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim, buttonAnim;
    TextView textview;
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        buttonAnim = AnimationUtils.loadAnimation(this, R.anim.blink_anim);

        textview = (TextView) findViewById(R.id.dibapp);
        login = (Button) findViewById(R.id.accedi);
        signup = (Button) findViewById(R.id.registrati);

        textview.startAnimation(topAnim);
        signup.startAnimation(topAnim);
        login.startAnimation(bottomAnim);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);

        }

        //Pulsante per accedere al proprio profilo
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.startAnimation(buttonAnim);
                Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class) ;
                startActivity(loginIntent);

            }
        });

        //Pulsante per registrarsi
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.startAnimation(buttonAnim);
                Intent registerIntent=new Intent(MainActivity.this, SignupActivity.class) ;
                startActivity(registerIntent);

            }
        });

    }

}
