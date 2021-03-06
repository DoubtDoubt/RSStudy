package com.example.finances.ui.Account.course;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finances.MainActivity;
import com.example.finances.R;
import com.example.finances.course.CourseLength;
import com.example.finances.database.Course;
import com.example.finances.database.DBHelper;
import com.example.finances.database.Lesson;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;


public class LessonDateActivity extends AppCompatActivity {

    Calendar dateAndTime;
    TextView currentDateTime;
    EditText duration;
    Button next;
    int COURSE_ID;
    int LESSONS;
    int CURRENT_LESSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setlessondate);
        Intent i = getIntent();
        dateAndTime = Calendar.getInstance();
        COURSE_ID = i.getIntExtra("COURSE_ID", -1);
        LESSONS = i.getIntExtra("LESSONS", -1);
        CURRENT_LESSON = i.getIntExtra("CURRENT_LESSON", -1);

        currentDateTime=(TextView)findViewById(R.id.currentDateTime);
        duration = (EditText) findViewById(R.id.editTextLessonDuration);

        next = findViewById(R.id.buttonLessonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                DBHelper dbHelper = new DBHelper(getApplicationContext());
                Intent i = getIntent();
                Lesson lesson = new Lesson();
                Course course = dbHelper.getCourse(COURSE_ID);
                String lessonName = course.getName() + " " + currentDateTime.getText();
                lesson.setName(lessonName);
                lesson.setCourseId(COURSE_ID);
                long dat = dateAndTime.getTimeInMillis()/1000;
                lesson.setDate(dat);
                lesson.setDuration(Integer.parseInt(duration.getText().toString()));

                Log.e("CURRENT", String.valueOf(CURRENT_LESSON));
                Log.e("ALL", String.valueOf(LESSONS));

                CURRENT_LESSON++;
                if(dbHelper.insertLesson(lesson) && (CURRENT_LESSON<LESSONS)) {
                    Snackbar snackbar = Snackbar.make(view1, "Record inserted successfully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Intent intent = new Intent(LessonDateActivity.this, LessonDateActivity.class);
                    intent.putExtra("COURSE_ID", COURSE_ID);
                    intent.putExtra("LESSONS", LESSONS);
                    intent.putExtra("CURRENT_LESSON", CURRENT_LESSON);
                    finish();
                    startActivity(intent);
                    CustomIntent.customType(LessonDateActivity.this,"left-to-right");
                }
                else {
                    Snackbar snackbar = Snackbar.make(view1, "Record not inserted", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    Intent intent = new Intent(LessonDateActivity.this, MainActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(LessonDateActivity.this,"left-to-right");
                    finish();
                }
            }
        });

        /*
        close = findViewById(R.id.closeButton3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LessonDateActivity.this, MainActivity.class);
                startActivity(intent);
                CustomIntent.customType(LessonDateActivity.this,"fadein-to-fadeout");
                finish();
            }
        });*/
    }

    // ???????????????????? ???????????????????? ???????? ?????? ???????????? ????????
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // ???????????????????? ???????????????????? ???????? ?????? ???????????? ??????????????
    public void setTime(View v) {
        new TimePickerDialog(this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // ?????????????????? ?????????????????? ???????? ?? ??????????????
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    // ?????????????????? ?????????????????????? ???????????? ??????????????
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // ?????????????????? ?????????????????????? ???????????? ????????
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
}