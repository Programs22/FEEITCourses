package com.example.feeitcourses;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScheduleCourseFragment extends Fragment implements LifecycleObserver {
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

    public ScheduleCourseFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getLifecycle().removeObserver(this);

        mScheduleCourse = getActivity().findViewById(R.id.current_course);
        mDay = getActivity().findViewById(R.id.day_picker);
        mHoursFrom = getActivity().findViewById(R.id.hours_from);
        mMinutesFrom = getActivity().findViewById(R.id.minutes_from);
        mHoursTo = getActivity().findViewById(R.id.hours_to);
        mMinutesTo = getActivity().findViewById(R.id.minutes_to);
        mScheduleButton = getActivity().findViewById(R.id.set_schedule);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mCourseID = getActivity().getIntent().getStringExtra("courseID");
            mCourseTitle = getActivity().getIntent().getStringExtra("courseTitle");
            mProfessorUsername = getActivity().getIntent().getStringExtra("username");

            if (mScheduleCourse != null) {
                setViewDescription(mCourseTitle, mCourseID);
            }
        }

        if (mScheduleButton != null) {
            mScheduleButton.setOnClickListener(scheduleButtonView -> scheduleCourse());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_course, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().getLifecycle().addObserver(this);
    }

    public void setViewDescription(String courseTitle, String courseID) {
        mCourseTitle = courseTitle;
        mCourseID = courseID;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mScheduleCourse.setText(String.format("%s %s (%s)", mScheduleCourse.getText(), courseTitle, courseID));
        }
        else {
            mScheduleCourse.setText(String.format("Set schedule for course: %s (%s)", courseTitle, courseID));
        }
    }

    public void setProfessorUsername(String professorUsername) {
        mProfessorUsername = professorUsername;
    }

    public void scheduleCourse() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("ScheduledCourses");

        String day = mDay.getSelectedItem().toString();
        String hoursFrom = mHoursFrom.getText().toString();
        String minutesFrom = mMinutesFrom.getText().toString();
        String hoursTo = mHoursTo.getText().toString();
        String minutesTo = mMinutesTo.getText().toString();

        if (mCourseID == null) {
            Toast.makeText(getContext(), "Please choose a course to schedule!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hoursFrom.isEmpty() || hoursFrom.length() > 2 || hoursFrom.compareTo("24") > 0) {
            Toast.makeText(getContext(), "Please enter a valid hour format!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (minutesFrom.isEmpty() || minutesFrom.length() > 2 || minutesFrom.compareTo("59") > 0) {
            Toast.makeText(getContext(), "Please enter a valid minutes format!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (hoursTo.isEmpty() || hoursTo.length() > 2 || hoursTo.compareTo("24") > 0) {
            Toast.makeText(getContext(), "Please enter a valid hour format!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (minutesTo.isEmpty() || minutesTo.length() > 2 || minutesTo.compareTo("59") > 0) {
            Toast.makeText(getContext(), "Please enter a valid minutes format!", Toast.LENGTH_SHORT).show();
            return;
        }

        String realCourseID = String.format("%s_%s", mCourseID, mProfessorUsername);
        TimeScheduler timeScheduler = new TimeScheduler(hoursFrom, minutesFrom, hoursTo, minutesTo);

        mDatabaseReference.child(realCourseID).child(day).setValue(timeScheduler);

        Toast.makeText(getContext(), "Course scheduled successfully!", Toast.LENGTH_SHORT).show();
    }
}