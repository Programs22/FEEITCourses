package com.example.feeitcourses;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CourseFragment extends Fragment implements LifecycleObserver {
    EditText mNewCourseID;
    EditText mNewCourseTitle;
    EditText mNewCourseDescription;
    Button mAssignNewCourse;
    FirebaseDatabase mDatabase;
    DatabaseReference mCoursesReference;
    DatabaseReference mProfessorsReference;
    String mProfessorUsername;

    public CourseFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getLifecycle().removeObserver(this);

        mNewCourseID = getActivity().findViewById(R.id.new_course_id);
        mNewCourseTitle = getActivity().findViewById(R.id.new_course_title);
        mNewCourseDescription = getActivity().findViewById(R.id.new_course_description);
        mAssignNewCourse = getActivity().findViewById(R.id.assign_course);

        mProfessorUsername = getActivity().getIntent().getStringExtra("username");

        mAssignNewCourse.setOnClickListener(newView -> assignCourse());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().getLifecycle().addObserver(this);
    }

    public void assignCourse() {
        mDatabase = FirebaseDatabase.getInstance();
        mCoursesReference = mDatabase.getReference("Courses");
        mProfessorsReference = mDatabase.getReference("Professors");

        String newCourseID = mNewCourseID.getText().toString();
        String newCourseTitle = mNewCourseTitle.getText().toString();
        String newCourseDescription = mNewCourseDescription.getText().toString();

        if (newCourseID.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid course ID", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (newCourseTitle.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid course title", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (newCourseDescription.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid course description", Toast.LENGTH_SHORT).show();
            return;
        }

        String realCourseID = newCourseID.concat("_" + mProfessorUsername);

        mCoursesReference.child(realCourseID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                HashMap result = (HashMap) task.getResult().getValue();

                if (result != null) {
                    Toast.makeText(getContext(), "This course already exists!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Please enter a new course ID!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProfessorsReference.child(mProfessorUsername).get().addOnCompleteListener(dataSnapshotTask -> {
                        if (dataSnapshotTask.isSuccessful()) {
                            HashMap result1 = (HashMap) dataSnapshotTask.getResult().getValue();

                            if (result1 != null) {
                                String professor = result1.get("nameSurname").toString();

                                Course course = new Course(newCourseID, newCourseTitle, newCourseDescription, professor, mProfessorUsername);
                                mCoursesReference.child(realCourseID).setValue(course);
                                courseAssigned();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Error when connecting to the database for professors!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                Toast.makeText(getContext(), "Error when connecting to the database for courses!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void courseAssigned() {
        Intent intent = new Intent(getActivity(), ProfessorCourseActivity.class);
        intent.putExtra("username", mProfessorUsername);
        startActivity(intent);
    }
}