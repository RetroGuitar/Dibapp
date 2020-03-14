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

import com.example.dibapp.module.Lesson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLessonDataActivity extends AppCompatActivity {
    TextView schedaDesc, date, start, end;
    EditText key;
    Button present, valutations, insertD, back;
    String descrizione, data, inizio, fine, chiave;
    Boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_lesson_data);

        schedaDesc = (TextView) findViewById(R.id.pschedadesc);
        date = (TextView) findViewById(R.id.pschedadate);
        start = (TextView) findViewById(R.id.pschedastart);
        end = (TextView) findViewById(R.id.pschedaend);
        key = (EditText) findViewById(R.id.pschedachiave);
        present = (Button) findViewById(R.id.lessonlistbutton);
        valutations = (Button) findViewById(R.id.commentlistbutton);
        insertD = (Button) findViewById(R.id.commentinsertbutton);
        back = (Button) findViewById(R.id.backbutton);

        Intent i = getIntent();
        final String lessonId = i.getStringExtra("lessonId");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference().child("lessons").child(lessonId);
        final String Uid = user.getUid();
        final DatabaseReference lessonstudent = FirebaseDatabase.getInstance().getReference().child("lesson_students").child(lessonId).child(Uid);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lezioneSnapshot) {
                isStarted = lezioneSnapshot.child("iniziata").getValue(Boolean.class);
                descrizione = lezioneSnapshot.child("descrizione").getValue(String.class);
                data = lezioneSnapshot.child("data").getValue(String.class);
                inizio = lezioneSnapshot.child("ora_i").getValue(String.class);
                fine = lezioneSnapshot.child("ora_f").getValue(String.class);
                chiave = lezioneSnapshot.child("chiave").getValue(String.class);

                schedaDesc.setText(descrizione);
                date.setText(getString(R.string.Date)+": "+data);
                start.setText(getString(R.string.Start_time)+": "+inizio);
                end.setText(getString(R.string.End_time)+": "+fine);

                present.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isStarted) {
                            if (key.getText().toString().isEmpty()) {
                                Toast.makeText(StudentLessonDataActivity.this, "Insert the lesson key!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (key.getText().toString().equals(chiave)) {
                                lessonstudent.setValue(Uid);
                                Toast.makeText(StudentLessonDataActivity.this, "Present!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StudentLessonDataActivity.this, "The key is incorrect!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        else {
                            Toast.makeText(StudentLessonDataActivity.this,"The lesson is not in progress!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                });
                insertD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lessonstudent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Intent i = new Intent(StudentLessonDataActivity.this, NewCommentActivity.class);
                                    i.putExtra("lessonId", lessonId);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(StudentLessonDataActivity.this,"You must be present!",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                valutations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference userdatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);

                        userdatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Boolean isTeacher = dataSnapshot.child("teacher").getValue(Boolean.class);
                                Intent valutations = new Intent(StudentLessonDataActivity.this, CommentListActivity.class);
                                valutations.putExtra("lessonId", lessonId);
                                valutations.putExtra("isTeacher", isTeacher);
                                startActivity(valutations);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
