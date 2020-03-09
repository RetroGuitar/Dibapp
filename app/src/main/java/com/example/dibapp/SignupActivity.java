package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dibapp.module.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    Button signinbtn;
    EditText nomebt, matricolabt, mailbt, passbt;
    TextView textview;
    CheckBox teach;
    String name, matricola, mail, password;
    Boolean teacher = false;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signinbtn = (Button) findViewById(R.id.registratibutt);
        textview = (TextView) findViewById(R.id.registratititle);
        nomebt = (EditText) findViewById(R.id.nomereg);
        matricolabt = (EditText) findViewById(R.id.matricolareg);
        mailbt = (EditText) findViewById(R.id.emailreg);
        passbt = (EditText) findViewById(R.id.pwdText);
        teach = (CheckBox) findViewById(R.id.teacherreg);


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nomebt.getText().toString();
                matricola = matricolabt.getText().toString();
                mail = mailbt.getText().toString();
                password = passbt.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignupActivity.this, getString(R.string.EnterName),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(matricola)){
                    Toast.makeText(SignupActivity.this,getString(R.string.EnterRegNumber),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(SignupActivity.this,getString(R.string.EnterEmail),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){
                    Toast.makeText(SignupActivity.this,getString(R.string.EnterPwd),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(teach.isChecked()){
                    teacher = true;
                }

                auth = FirebaseAuth.getInstance();

                auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"Authentication Failed!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignupActivity.this,"All right, check your email!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            User user1 = new User(user.getUid(), name, matricola, teacher);

                            DatabaseReference myRef = database.getReference("users").child(user.getUid());

                            myRef.setValue(user1);

                            sendVerificationEmail();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        }
                    }
                });
            }
        });
    }

    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                        else
                        {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
