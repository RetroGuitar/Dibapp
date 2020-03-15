package com.example.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dibapp.module.Position;
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
    TextView schedaDesc, date, start, end, key, pres;
    Button lessonstart, lessonend, comment, back;
    String descrizione, data, inizio, fine, chiave;
    Boolean isStarted;
    Position position;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_lesson_data);

        Intent i = getIntent();
        final String lessonId = i.getStringExtra("lessonId");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference().child("lessons").child(lessonId);
        final DatabaseReference presRef=FirebaseDatabase.getInstance().getReference().child("lesson_students").child(lessonId);

          schedaDesc = (TextView) findViewById(R.id.pschedadesc);
          date = (TextView) findViewById(R.id.pschedadate);
          start = (TextView) findViewById(R.id.pschedastart);
          end = (TextView) findViewById(R.id.pschedaend);
          key = (TextView) findViewById(R.id.pschedakey);
          pres=(TextView) findViewById(R.id.pschedapres);
          lessonstart = (Button) findViewById(R.id.lessonstart);
          lessonend = (Button) findViewById(R.id.lessonend);
          comment = (Button) findViewById(R.id.commentlistbutton);
          back = (Button) findViewById(R.id.backbutton);
          locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
          locationListener=new LocationListener() {
              @Override
              public void onLocationChanged(Location location) {
                  position= new Position(location.getAccuracy(), location.getLatitude(), location.getLongitude());
              }

              @Override
              public void onStatusChanged(String provider, int status, Bundle extras) {

              }

              @Override
              public void onProviderEnabled(String provider) {

              }

              @Override
              public void onProviderDisabled(String provider) {

              }
          };

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

                presRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            pres.setText(getString(R.string.PresentT)+": "+String.valueOf(dataSnapshot.getChildrenCount()));
                        } else pres.setText(getString(R.string.PresentT)+": 0");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                lessonstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isStarted){
                            Toast.makeText(TeacherLessonDataActivity.this, "The lesson is in progress!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (ActivityCompat.checkSelfPermission(TeacherLessonDataActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TeacherLessonDataActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                            }else {

                                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

                                FirebaseDatabase.getInstance().getReference().child("lessons").child(lessonId).child("position").setValue(position);
                                Map<String, Object> hopperUpdates = new HashMap<>();
                                hopperUpdates.put("iniziata", true);

                                lessonRef.updateChildren(hopperUpdates);
                                Toast.makeText(TeacherLessonDataActivity.this,"Lesson started successfully!",Toast.LENGTH_SHORT).show();
                            }



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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case 10:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }

    }
}
