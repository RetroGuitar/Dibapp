package com.example.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.dibapp.module.Comment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewCommentActivity extends AppCompatActivity {

    String lessonId, comment, commentId;
    Boolean privCheck;
    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        Intent i= new Intent();
        i=getIntent();
        lessonId=i.getStringExtra("lessonId");

        final RatingBar ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        final EditText commentText=(EditText) findViewById(R.id.editText);
        final CheckBox privateCheck=(CheckBox)findViewById(R.id.checkBox);
        Button insertComment=(Button) findViewById(R.id.commentinsertbutton);
        Button back=(Button) findViewById(R.id.backbutton);
        final DatabaseReference commentDB = FirebaseDatabase.getInstance().getReference().child("lesson_comments").child(lessonId);
        commentId=commentDB.push().getKey();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        insertComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentText.getText().toString().isEmpty()){
                    Toast.makeText(NewCommentActivity.this, getString(R.string.EmptyComment), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (privateCheck.isChecked()){
                    privCheck=true;
                }else privCheck=false;

                comment=commentText.getText().toString();
                rate=ratingBar.getRating();

                Comment commentObj=new Comment(commentId, rate, comment, privCheck);
                commentDB.child(commentId).setValue(commentObj);

                Toast.makeText(NewCommentActivity.this, getString(R.string.CommentPublished),Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }
}
