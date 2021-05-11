package com.example.finances.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.finances.MainActivity;
import com.example.finances.R;
import com.example.finances.ui.Home.HomeFragment;

import maes.tech.intentanim.CustomIntent;


public class CourseLength extends AppCompatActivity {

    ImageButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_length);


        close = findViewById(R.id.closeButton1);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseLength.this, MainActivity.class);
                startActivity(intent);
                CustomIntent.customType(CourseLength.this,"fadein-to-fadeout");
                finish();

                if(CourseName.instance != null) {
                    try {
                        CourseName.instance.finish();
                    } catch (Exception e) {}
                }

            }
        });
    }
}