package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TeacherLessonDataActivity extends AppCompatActivity {
    TextView schedaDesc, date, start, end, key;
    Button lessonstart, lessonend, comment, back;
    String descrizione, data, inizio, fine, chiave;
    Boolean isStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_lesson_data);

        Intent i = getIntent();
        final String lessonId = i.getStringExtra("lessonId");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference().child("lessons").child(lessonId);

          schedaDesc = (TextView) findViewById(R.id.pschedadesc);
          date = (TextView) findViewById(R.id.pschedadate);
          start = (TextView) findViewById(R.id.pschedastart);
          end = (TextView) findViewById(R.id.pschedaend);
          key = (TextView) findViewById(R.id.pschedakey);
          lessonstart = (Button) findViewById(R.id.lessonstart);
          lessonend = (Button) findViewById(R.id.lessonend);
          comment = (Button) findViewById(R.id.commentlistbutton);
          back = (Button) findViewById(R.id.backbutton);

          back.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  finish();
              }
          });

        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lessonSnapshot) {
                isStarted = lessonSnapshot.child("iniziata").getValue(Boolean.class);

                descrizione = lessonSnapshot.child("descrizione").getValue(String.class);
                data = lessonSnapshot.child("data").getValue(String.class);
                inizio = lessonSnapshot.child("ora_i").getValue(String.class);
                fine = lessonSnapshot.child("ora_f").getValue(String.class);
                chiave = lessonSnapshot.child("chiave").getValue(String.class);

                schedaDesc.setText(descrizione);
                date.setText(getString(R.string.Date)+": "+data);
                start.setText(getString(R.string.Start_time)+": "+inizio);
                end.setText(getString(R.string.End_time)+": "+fine);
                key.setText(getString(R.string.LessonKey)+": "+chiave);

                lessonstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isStarted){
                            Toast.makeText(TeacherLessonDataActivity.this, "The lesson is in progress!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String, Object> hopperUpdates = new HashMap<>();
                            hopperUpdates.put("iniziata", true);

                            lessonRef.updateChildren(hopperUpdates);
                            Toast.makeText(TeacherLessonDataActivity.this,"Lesson started successfully!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                lessonend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isStarted){
                            Map<String, Object> hopperUpdates = new HashMap<>();
                            hopperUpdates.put("iniziata", false);

                            lessonRef.updateChildren(hopperUpdates);
                            Toast.makeText(TeacherLessonDataActivity.this,"Lesson stopped successfully!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(TeacherLessonDataActivity.this,"The lesson is not in progress!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                                Intent valutations = new Intent(TeacherLessonDataActivity.this, CommentListActivity.class);
                                valutations.putExtra("lessonId",lessonId);
                                valutations.putExtra("isTeacher", true);
                                startActivity(valutations);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
