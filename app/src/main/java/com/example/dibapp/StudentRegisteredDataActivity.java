package com.example.dibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StudentRegisteredDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registered_data);

        Button back=(Button) findViewById(R.id.backbutton);

        final TextView nome=(TextView) findViewById(R.id.pschedanome);
        final TextView prof=(TextView) findViewById(R.id.pschedaprof);

        final TextView desc=(TextView) findViewById(R.id.pschedadesc);
        final TextView laurea=(TextView) findViewById(R.id.pschedalaurea);
        final Button lessonList= (Button) findViewById(R.id.lessonlistbutton);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid= user.getUid();
        final DatabaseReference studentCourse=FirebaseDatabase.getInstance().getReference().child("student_courses").child(uid);
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
                final String name, teacher, description, degree;
               name= dataSnapshot.child("nome").getValue().toString();
               teacher=dataSnapshot.child("professore").getValue().toString();
               description=dataSnapshot.child("descrizione").getValue().toString();
               degree=dataSnapshot.child("laurea").getValue().toString();

               nome.setText(name);
               prof.setText(getString(R.string.teacher)+ ": "+teacher);
               desc.setText(getString(R.string.Description)+ ": "+description);
               laurea.setText(getString(R.string.DegreeCourse)+ ": "+degree);

                lessonList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent listl =new Intent (StudentRegisteredDataActivity.this, LessonListActivity.class);
                        listl.putExtra("id", id);
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
