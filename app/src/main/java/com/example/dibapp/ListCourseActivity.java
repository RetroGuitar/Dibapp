package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCourseActivity extends AppCompatActivity {

    ListView coursesList;
    DatabaseReference courseReference;
    ArrayList<String> courses=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);

        courseReference= FirebaseDatabase.getInstance().getReference().child("courses");
        coursesList= (ListView)findViewById(R.id.allcourseslist) ;
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ListCourseActivity.this, android.R.layout.simple_list_item_1, courses);


        //Controllare che esistano corsi
        courseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot courseSnapshot) {
                if (courseSnapshot.exists()){
                    courses.clear();
                    for (DataSnapshot dss: courseSnapshot.getChildren()){
                        coursesList.setAdapter(arrayAdapter);
                       String key= dss.getKey();
                       String nome=courseSnapshot.child(key).child("nome").getValue(String.class);
                       courses.add(nome);


                    }





                }
                else Toast.makeText(ListCourseActivity.this, getString(R.string.CoursesNotAvailable) ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}
