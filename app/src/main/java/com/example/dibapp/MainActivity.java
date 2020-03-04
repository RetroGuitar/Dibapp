package com.example.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Pulsante per accedere al proprio profilo
        Button login = (Button)findViewById(R.id.accedi);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class) ;
                startActivity(loginIntent);
            }
        });

        //Pulsante per registrarsi
        Button signup = (Button)findViewById(R.id.registrati);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(MainActivity.this, SignupActivity.class) ;
                startActivity(registerIntent);
            }
        });

    }

}
