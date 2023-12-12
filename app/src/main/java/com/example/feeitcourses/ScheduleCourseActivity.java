package com.example.feeitcourses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScheduleCourseActivity extends AppCompatActivity {
    TextView mScheduleCourse;
    Spinner mDay;
    EditText mHoursFrom;
    EditText mMinutesFrom;
    EditText mHoursTo;
    EditText mMinutesTo;
    Button mScheduleButton;
    String mCourseID;
    String mCourseTitle;
    String mProfessorUsername;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.schedule_courses);

        mCourseID = getIntent().getStringExtra("courseID");
        mCourseTitle = getIntent().getStringExtra("courseTitle");
        mProfessorUsername = getIntent().getStringExtra("username");

        mScheduleCourse = findViewById(R.id.current_course);
        mDay = findViewById(R.id.day_picker);
        mHoursFrom = findViewById(R.id.hours_from);
        mMinutesFrom = findViewById(R.id.minutes_from);
        mHoursTo = findViewById(R.id.hours_to);
        mMinutesTo = findViewById(R.id.minutes_to);
        mScheduleButton = findViewById(R.id.set_schedule);

        mScheduleCourse.setText(String.format("%s %s (%s)", mScheduleCourse.getText(), mCourseTitle, mCourseID));

        mScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleCourse();
            }
        });
    }

    public void scheduleCourse() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("ScheduledCourses");

        String day = mDay.getSelectedItem().toString();
        String hoursFrom = mHoursFrom.getText().toString();
        String minutesFrom = mMinutesFrom.getText().toString();
        String hoursTo = mHoursTo.getText().toString();
        String minutesTo = mMinutesTo.getText().toString();

        if (hoursFrom.isEmpty() || hoursFrom.length() > 2 || hoursFrom.compareTo("24") > 0) {
            Toast.makeText(this, "Please enter a valid hour format", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (minutesFrom.isEmpty() || minutesFrom.length() > 2 || minutesFrom.compareTo("59") > 0) {
            Toast.makeText(this, "Please enter a valid minutes format", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (hoursTo.isEmpty() || hoursTo.length() > 2 || hoursTo.compareTo("24") > 0) {
            Toast.makeText(this, "Please enter a valid hour format", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (minutesTo.isEmpty() || minutesTo.length() > 2 || minutesTo.compareTo("59") > 0) {
            Toast.makeText(this, "Please enter a valid minutes format", Toast.LENGTH_SHORT).show();
            return;
        }

        String realCourseID = String.format("%s_%s", mCourseID, mProfessorUsername);
        TimeScheduler timeScheduler = new TimeScheduler(hoursFrom, minutesFrom, hoursTo, minutesTo);

        mDatabaseReference.child(realCourseID).child(day).setValue(timeScheduler);
    }
}