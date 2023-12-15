package com.example.feeitcourses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class EnrollRemoveCourseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_remove_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        String activityType = getIntent().getStringExtra("activityType");

        if (activityType != null) {
            if (activityType.equals("Enroll")) {
                toolbar.setTitle(R.string.enroll_course_title);
            }
            else {
                toolbar.setTitle(R.string.remove_course_title);
            }
        }
    }
}