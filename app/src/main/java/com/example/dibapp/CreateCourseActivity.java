package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dibapp.module.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        final TextView corso=(TextView)findViewById(R.id.nomecorsotext);
        final TextView descrizione=(TextView)findViewById(R.id.descrizionecorsotext);
        final TextView laurea=(TextView)findViewById(R.id.cdltext);
        final TextView chiave=(TextView)findViewById(R.id.chiavecorsotext);



        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String uid= firebaseUser.getUid();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        final DatabaseReference pushDB=FirebaseDatabase.getInstance().getReference().child("courses");
        final DatabaseReference teacherCourse=FirebaseDatabase.getInstance().getReference().child("teacher_courses").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Button create=(Button) findViewById(R.id.creacorsobutton);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String coursename=corso.getText().toString();
                        String description=descrizione.getText().toString();
                        String degree=laurea.getText().toString();
                        String key=chiave.getText().toString();
                        String pushid=pushDB.push().getKey();;
                        Course course;


                        String teacher=dataSnapshot.child("name").getValue().toString();
                        if (TextUtils.isEmpty(coursename)) {
                            Toast.makeText(CreateCourseActivity.this, getString(R.string.EnterName), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(description)){
                            Toast.makeText(CreateCourseActivity.this, R.string.EnterDescription, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(degree)) {
                            Toast.makeText(CreateCourseActivity.this, getString(R.string.EnterDegree), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (chiave.length()<6) {
                            Toast.makeText(CreateCourseActivity.this, getString(R.string.EnterPwd), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        course=new Course (pushid, coursename, description , teacher, degree ,key);


                        pushDB.child(pushid).setValue(course);
                        teacherCourse.child(pushid).setValue(coursename);

                        Toast.makeText(CreateCourseActivity.this, getString(R.string.CourseSuccess), Toast.LENGTH_SHORT).show();
                        finish();







                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Button back = (Button) findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
