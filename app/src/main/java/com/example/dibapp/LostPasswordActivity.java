package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LostPasswordActivity extends AppCompatActivity {

    TextView textview;
    EditText mailtxt;
    Button resetpsw;
    String mail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);

        resetpsw = (Button) findViewById(R.id.lostbutton);
        textview = (TextView) findViewById(R.id.losttext);
        mailtxt = (EditText) findViewById(R.id.lostemail);

        auth = FirebaseAuth.getInstance();

        resetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mail = mailtxt.getText().toString();

                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(LostPasswordActivity.this,"Enter email address!",Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LostPasswordActivity.this,"We have sent you the instructions to reset your password!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LostPasswordActivity.this, LoginActivity.class));
                        }
                        else {
                            Toast.makeText(LostPasswordActivity.this,"Failed to sent reset email!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
