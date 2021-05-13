package com.example.finances.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.finances.MainActivity;
import com.example.finances.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import maes.tech.intentanim.CustomIntent;


public class CourseLength extends AppCompatActivity {






    ImageButton close;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_length);


        final EditText contentEditText = findViewById(R.id.editTextLength);

        next = findViewById(R.id.buttonNext1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileLengthName = "LENTGH_COURSE";
                String content = contentEditText.getText().toString();
                FileOutputStream fos;

                try {
                    fos = openFileOutput(fileLengthName, Context.MODE_PRIVATE); // открываем файл для записи
                    fos.write(content.getBytes()); // записываем данные
                    fos.close(); // закрываем файл

                    // выводим сообщение
                    Toast.makeText(getApplicationContext(),
                            "Файл " + fileLengthName + " сохранён", Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


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