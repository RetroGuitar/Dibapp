package com.example.dibapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dibapp.module.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCourseFragment newInstance(String param1, String param2) {
        MyCourseFragment fragment = new MyCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ArrayList<String> mycourses = new ArrayList<>();
    ArrayList <String> courseId= new ArrayList<>();


    String id, name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_course, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ListView courseList =  (ListView)view.findViewById(R.id.mycoursesListView);
        final ArrayAdapter<String> myAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mycourses);




        //definire se l'utente è insegnante o studente
        final String uid=user.getUid();
        DatabaseReference dbTeach= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("teacher");
        dbTeach.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isTeacher=false;
                isTeacher=dataSnapshot.getValue(Boolean.class);

                if (isTeacher){
                    DatabaseReference dbTeachCourse =FirebaseDatabase.getInstance().getReference().child("teacher_courses").child(uid);
                    dbTeachCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                mycourses.clear();
                                courseId.clear();
                                for ( DataSnapshot dss : dataSnapshot.getChildren()) {
                                    courseList.setAdapter(myAdapter);
                                    id= dss.getKey();
                                    name=dss.child("nome").getValue(String.class);
                                    mycourses.add(name);
                                    courseId.add(id);


                                    courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                           String idc=courseId.get(position);
                                            Intent showDetails=new Intent(getContext(), TeacherCourseDataActivity.class);
                                            showDetails.putExtra("id", idc);
                                            startActivity(showDetails);
                                        }
                                    });






                                }
                            }else Toast.makeText(getContext(), getString(R.string.CoursesNotAvailable) , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    //Studente
                    DatabaseReference dbTeachCourse =FirebaseDatabase.getInstance().getReference().child("student_courses").child(uid);
                    dbTeachCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                mycourses.clear();
                                courseId.clear();
                                for ( DataSnapshot dss : dataSnapshot.getChildren()) {
                                    courseList.setAdapter(myAdapter);
                                    id= dss.getKey();
                                    name=dss.child("nome").getValue(String.class);
                                    mycourses.add(name);
                                    courseId.add(id);


                                    courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String idc=courseId.get(position);
                                            Intent showDetails=new Intent(getContext(), StudentRegisteredDataActivity.class);
                                            showDetails.putExtra("id", idc);
                                            startActivity(showDetails);
                                        }
                                    });






                                }
                            }else Toast.makeText(getContext(), getString(R.string.CoursesNotAvailable) , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

                // Inflate the layout for this fragment
        return view;



    }
}
