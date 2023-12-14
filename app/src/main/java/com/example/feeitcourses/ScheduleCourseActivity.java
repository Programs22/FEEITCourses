package com.example.feeitcourses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class ScheduleCourseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.schedule_courses);
    }
}