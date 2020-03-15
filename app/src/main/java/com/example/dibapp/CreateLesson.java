package com.example.dibapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dibapp.module.Lesson;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.time.Clock;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class CreateLesson extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener timeStartListener;
    private TimePickerDialog.OnTimeSetListener timeEndListener;
    private String lessonId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        //Prendere i dati da layout (descrizione, data , inizio, fine, chiave) set iniziata=false
        final EditText descrizione =(EditText) findViewById(R.id.text_lesson_desc);
        final EditText data=(EditText) findViewById(R.id.text_lesson_date);
        final EditText orai=(EditText) findViewById(R.id.text_lesson_start);
        final  EditText oraf=(EditText) findViewById(R.id.text_lesson_end);

        final Button back=(Button) findViewById(R.id.backbutton);
        final Button createButton=(Button) findViewById(R.id.btn_lesson_save);







        //prendi l'id del corso
        Intent intent= getIntent();
        String courseId=intent.getStringExtra("id");

        //crea riferimento e prendi id lezione
        final DatabaseReference lessons=FirebaseDatabase.getInstance().getReference().child("lessons");
        lessonId=lessons.push().getKey();
        final DatabaseReference course_lessons= FirebaseDatabase.getInstance().getReference().child("course_lessons").child(courseId).child(lessonId);



        //DatePicker

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateLesson.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                data.setText(date);
            }
        };



        //TimePicker

        orai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog dialog = new TimePickerDialog(CreateLesson.this, timeStartListener, 0, 0, true );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        timeStartListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute==0 ||minute==5) {
                    time = hourOfDay + ":0" + minute;
                }
                else  time = hourOfDay + ":" + minute;
                orai.setText(time);
            }
        };



        oraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(CreateLesson.this, timeEndListener, 0, 0, true );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        timeEndListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute==0 ||minute==5) {
                    time = hourOfDay + ":0" + minute;
                }
                else  time = hourOfDay + ":" + minute;
                oraf.setText(time);
            }
        };


        //Creazione due oggetti
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc=descrizione.getText().toString();
                String date= data.getText().toString();
                String start=orai.getText().toString();
                String end=oraf.getText().toString();


                if (desc.isEmpty()) {
                    Toast.makeText(CreateLesson.this, getString(R.string.EmptyField), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date.isEmpty()) {
                    Toast.makeText(CreateLesson.this, getString(R.string.EmptyField), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (start.isEmpty()) {
                    Toast.makeText(CreateLesson.this, getString(R.string.EmptyField), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (end.isEmpty()) {
                    Toast.makeText(CreateLesson.this, getString(R.string.EmptyField), Toast.LENGTH_SHORT).show();
                    return;
                }


                Lesson completeLesson=new Lesson(lessonId, desc, date, start, end, false);
                Lesson courseLesson=new Lesson (lessonId, desc);

                //Push dei due oggetti

                lessons.child(lessonId).setValue(completeLesson);
                course_lessons.setValue(courseLesson);

                Toast.makeText(CreateLesson.this, getString(R.string.CourseSuccess), Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}

