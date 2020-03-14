package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dibapp.module.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {

    String lessonId, description, commentId;
    boolean isTeacher, isPrivate;
    float rating;

    ArrayList<String> commentList=new ArrayList<>();

    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        final ListView commentListView=(ListView)findViewById(R.id.allcommentslist);

        myAdapter=new ArrayAdapter<>(CommentListActivity.this, android.R.layout.simple_list_item_1, commentList);




        Intent intent = getIntent();
        lessonId=intent.getStringExtra("lessonId");
        isTeacher=intent.getBooleanExtra("isTeacher", false);

        DatabaseReference commentDB= FirebaseDatabase.getInstance().getReference().child("lesson_comments").child(lessonId);

        commentDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    commentList.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        commentListView.setAdapter(myAdapter);

                        description = dss.child("description").getValue(String.class);
                        isPrivate = dss.child("privatecomment").getValue(Boolean.class);
                        rating = dss.child("rating").getValue(Float.class);


                        if (isPrivate) {
                            if (isTeacher) {
                                commentList.add(String.valueOf(rating) + "/5.0 : " + description);
                                continue;
                            } else continue;
                        }


                        commentList.add(String.valueOf(rating) + "/5.0 : " + description);


                    }
                }else Toast.makeText(CommentListActivity.this, "There are not comments yet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}


