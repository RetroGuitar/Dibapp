package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherCourseDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_data);

        Button back=(Button) findViewById(R.id.backbutton);

        final TextView nome=(TextView) findViewById(R.id.pschedanome);
        final TextView prof=(TextView) findViewById(R.id.pschedaprof);
        final Button createlesson=(Button) findViewById(R.id.lessoncreatebutton);
        final Button listlesson=(Button) findViewById(R.id.lessonlistbutton);
        final TextView desc=(TextView) findViewById(R.id.pschedadesc);
        final TextView laurea=(TextView) findViewById(R.id.pschedalaurea);


        Intent intent= getIntent();
        final String id=intent.getStringExtra("id");
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("courses").child(id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String idc=dataSnapshot.child("id").getValue().toString();
                final String name= dataSnapshot.child("nome").getValue().toString();
                final String teacher=dataSnapshot.child("professore").getValue().toString();
                final String  description=dataSnapshot.child("descrizione").getValue().toString();
                final String degree=dataSnapshot.child("laurea").getValue().toString();

                nome.setText(name);
                prof.setText(getString(R.string.teacher)+ ": "+teacher);
                desc.setText(getString(R.string.Description)+ ": "+description);
                laurea.setText(getString(R.string.DegreeCourse)+ ": "+degree);

                createlesson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent createl =new Intent (TeacherCourseDataActivity.this, CreateLesson.class);
                        createl.putExtra("id", idc);

                        startActivity(createl);

                    }
                });

                listlesson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent listl =new Intent (TeacherCourseDataActivity.this, LessonListActivity.class);
                        listl.putExtra("id", idc);
                        startActivity(listl);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
