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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView textview;
    EditText mail, password;
    Button sign, reset;
    FirebaseAuth auth;
    FirebaseUser user;
    String psw, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textview = (TextView) findViewById(R.id.logintitle);
        mail = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.pwdText);
        sign = (Button) findViewById(R.id.accedibutt);
        reset = (Button) findViewById(R.id.recuperapwd);

        auth = FirebaseAuth.getInstance();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mail.getText().toString();
                psw = password.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this,"Enter email address!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this,"Enter password!",Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, psw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                Toast.makeText(LoginActivity.this, "Enter a password of at least six characters!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,"Logged successfully!",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LostPasswordActivity.class));
            }
        });
    }
}
