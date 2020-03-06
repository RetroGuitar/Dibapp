package com.example.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //definizione del pulsante per recupero password ed intent
        Button lost=(Button)findViewById(R.id.recuperapwd);
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lostpwd= new Intent(LoginActivity.this, LostPasswordActivity.class);
                startActivity(lostpwd);
            }
        });

        //test button
        Button b=(Button)findViewById(R.id.accedibutt);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test= new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(test);
            }
        });

    }
}
