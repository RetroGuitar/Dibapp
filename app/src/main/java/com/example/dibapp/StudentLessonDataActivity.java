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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dibapp.module.Lesson;
import com.example.dibapp.module.Position;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLessonDataActivity extends AppCompatActivity {
    TextView schedaDesc, date, start, end;

    Button present, valutations, insertC, back;
    String descrizione, data, inizio, fine, chiave;
    Boolean isStarted;
    Position position,tPosition;
    LocationManager locationManager;
    LocationListener locationListener;
    double tAcc, tLt, tLn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_lesson_data);

        schedaDesc = (TextView) findViewById(R.id.pschedadesc);
        date = (TextView) findViewById(R.id.pschedadate);
        start = (TextView) findViewById(R.id.pschedastart);
        end = (TextView) findViewById(R.id.pschedaend);

        present = (Button) findViewById(R.id.lessonlistbutton);
        valutations = (Button) findViewById(R.id.commentlistbutton);
        insertC = (Button) findViewById(R.id.commentinsertbutton);
        back = (Button) findViewById(R.id.backbutton);

        Intent i = getIntent();
        final String lessonId = i.getStringExtra("lessonId");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference().child("lessons").child(lessonId);
        final String Uid = user.getUid();
        final DatabaseReference lessonstudent = FirebaseDatabase.getInstance().getReference().child("lesson_students").child(lessonId).child(Uid);
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
                Toast.makeText(StudentLessonDataActivity.this, "Network provider is disabled!", Toast.LENGTH_SHORT).show();
            }
        };

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (ActivityCompat.checkSelfPermission(StudentLessonDataActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StudentLessonDataActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        }else {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lezioneSnapshot) {
                isStarted = lezioneSnapshot.child("iniziata").getValue(Boolean.class);
                descrizione = lezioneSnapshot.child("descrizione").getValue(String.class);
                data = lezioneSnapshot.child("data").getValue(String.class);
                inizio = lezioneSnapshot.child("ora_i").getValue(String.class);
                fine = lezioneSnapshot.child("ora_f").getValue(String.class);
               if (lezioneSnapshot.child("position").exists()){
                   tAcc=lezioneSnapshot.child("position").child("accuracy").getValue(Double.class);
                   tLt=lezioneSnapshot.child("position").child("latitude").getValue(Double.class);
                   tLn= lezioneSnapshot.child("position").child("longitude").getValue(Double.class);
                   tPosition=new Position(tAcc, tLt, tLn);
               }


                schedaDesc.setText(descrizione);
                date.setText(getString(R.string.Date)+": "+data);
                start.setText(getString(R.string.Start_time)+": "+inizio);
                end.setText(getString(R.string.End_time)+": "+fine);


                present.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isStarted) {
                            if (position==null){
                                Toast.makeText(StudentLessonDataActivity.this, "There are problems with the network provider", Toast.LENGTH_SHORT).show();
                                return;
                            }
                           if(position.distanceTo(tPosition)<35) {
                               lessonstudent.setValue(Uid);
                               Toast.makeText(StudentLessonDataActivity.this, "Present!", Toast.LENGTH_SHORT).show();
                           }else Toast.makeText(StudentLessonDataActivity.this, "You're not in the Classroom!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(StudentLessonDataActivity.this,"The lesson is not in progress!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                });
                insertC.setOnClickListener(new View.OnClickListener() {
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

                                Intent valutations = new Intent(StudentLessonDataActivity.this, CommentListActivity.class);
                                valutations.putExtra("lessonId", lessonId);
                                valutations.putExtra("isTeacher", false);
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
