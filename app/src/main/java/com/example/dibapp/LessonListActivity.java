package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LessonListActivity extends AppCompatActivity {
    ArrayAdapter<String>myAdapter;
    ArrayList<String>descList=new ArrayList<>();
    ArrayList<String> idList= new ArrayList<>();
    boolean isTeacher=false;

    String lessonId;
    String lessonDesc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);
        final Intent intent=getIntent();
        final String courseId=intent.getStringExtra("id");

        final ListView lessonListView= (ListView) findViewById(R.id.alllessonslist);




        //definire tipo utente
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //definire se l'utente è insegnante o studente
        final String uid=user.getUid();
        DatabaseReference dbTeach= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("teacher");
        dbTeach.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isTeacher=dataSnapshot.getValue(Boolean.class);

                if (isTeacher) {
                    myAdapter=new ArrayAdapter<>(LessonListActivity.this, android.R.layout.simple_list_item_1, descList);
                    DatabaseReference dbList=FirebaseDatabase.getInstance().getReference().child("course_lessons").child(courseId);
                    dbList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                descList.clear();
                                idList.clear();
                                for ( DataSnapshot dss : dataSnapshot.getChildren()) {
                                    lessonListView.setAdapter(myAdapter);
                                    lessonId= dss.getKey();
                                    lessonDesc=dss.child("descrizione").getValue(String.class);
                                    descList.add(lessonDesc);
                                    idList.add(lessonId);

                                    lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String lessonIdInt=idList.get(position);
                                            Intent teacherIntent=new Intent(LessonListActivity.this, TeacherLessonDataActivity.class);
                                            teacherIntent.putExtra("lessonId", lessonIdInt);
                                            startActivity(teacherIntent);
                                        }
                                    });


                                }

                            }else Toast.makeText(LessonListActivity.this, getString(R.string.CoursesNotAvailable) , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    //student
                    myAdapter=new ArrayAdapter<>(LessonListActivity.this, android.R.layout.simple_list_item_1, descList);
                    DatabaseReference dbList=FirebaseDatabase.getInstance().getReference().child("course_lessons").child(courseId);
                    dbList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                descList.clear();
                                idList.clear();
                                for ( DataSnapshot dss : dataSnapshot.getChildren()) {
                                    lessonListView.setAdapter(myAdapter);
                                    lessonId= dss.getKey();
                                    lessonDesc=dss.child("descrizione").getValue(String.class);
                                    descList.add(lessonDesc);
                                    idList.add(lessonId);

                                    lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String lessonIdInt=idList.get(position);
                                            Intent studentIntent=new Intent(LessonListActivity.this, StudentLessonDataActivity.class);
                                            studentIntent.putExtra("lessonId", lessonIdInt);
                                            startActivity(studentIntent);
                                        }
                                    });


                                }

                            }else Toast.makeText(LessonListActivity.this, getString(R.string.LessonsNotAvailable) , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}
