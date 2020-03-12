package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentCourseDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_data);

        Button back=(Button) findViewById(R.id.backbutton);

        final TextView nome=(TextView) findViewById(R.id.pschedanome);
        final TextView prof=(TextView) findViewById(R.id.pschedaprof);

        final TextView desc=(TextView) findViewById(R.id.pschedadesc);
        final TextView laurea=(TextView) findViewById(R.id.pschedalaurea);
        Intent intent= getIntent();
        String id=intent.getStringExtra("id");
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
                String name, teacher, description, degree;
               name= dataSnapshot.child("nome").getValue().toString();
               teacher=dataSnapshot.child("professore").getValue().toString();
               description=dataSnapshot.child("descrizione").getValue().toString();
               degree=dataSnapshot.child("laurea").getValue().toString();

               nome.setText(name);
               prof.setText(getString(R.string.teacher)+ ": "+teacher);
               desc.setText(getString(R.string.Description)+ ": "+description);
               laurea.setText(getString(R.string.DegreeCourse)+ ": "+degree);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
