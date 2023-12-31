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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnrollRemoveCourseFragment extends Fragment implements LifecycleObserver {
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    TextView mTextView;
    TextView mDescription;
    Button mEnrollButton;
    String mCourseID;
    String mCourseTitle;
    String mCourseDescription;
    String mProfessor;
    String mProfessorUsername;
    String mStudentUsername;
    String mActivityType;
    String mRealCourseID;

    public EnrollRemoveCourseFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getLifecycle().removeObserver(this);

        String activityType = getActivity().getIntent().getStringExtra("activityType");

        mCourseID = getActivity().getIntent().getStringExtra("courseID");
        mCourseTitle = getActivity().getIntent().getStringExtra("courseTitle");
        mCourseDescription = getActivity().getIntent().getStringExtra("courseDescription");
        mProfessor = getActivity().getIntent().getStringExtra("professor");
        mProfessorUsername = getActivity().getIntent().getStringExtra("professorUsername");
        mStudentUsername = getActivity().getIntent().getStringExtra("studentUsername");

        setActivityType(activityType);
        setRealCourseID(mCourseID, mProfessorUsername);
        setCourseToRemove(mCourseTitle, mCourseDescription, mProfessor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enroll_remove_course, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().getLifecycle().addObserver(this);
    }

    public void enroll() {
        mDatabaseReference = mDatabase.getReference("EnrolledCourses");

        Course course = new Course(mCourseID, mCourseTitle, mCourseDescription, mProfessor, mProfessorUsername);
        String enrollCourseID = String.format("%s_%s_%s", mCourseID, mProfessorUsername, mStudentUsername);

        mDatabaseReference.child(enrollCourseID).setValue(course);

        Intent intent = new Intent(getActivity(), StudentCourseActivity.class);
        intent.putExtra("username", mStudentUsername);
        startActivity(intent);
    }

    public void removeCourse() {
        if (mCourseID == null || mProfessorUsername == null || mStudentUsername == null) {
            Toast.makeText(getActivity(), "Please choose a course to remove!", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabaseReference = mDatabase.getReference("EnrolledCourses");
        String enrolledCourseID = String.format("%s_%s_%s", mCourseID, mProfessorUsername, mStudentUsername);

        DatabaseReference courseToRemove = mDatabaseReference.child(enrolledCourseID);
        courseToRemove.removeValue();

        Intent intent = new Intent(getActivity(), StudentCourseActivity.class);
        intent.putExtra("username", mStudentUsername);
        startActivity(intent);
    }

    public void setActivityType(String activityType) {
        mActivityType = activityType;
        mEnrollButton = getActivity().findViewById(R.id.enroll);

        if (mActivityType != null) {
            if (mActivityType.equals("Enroll")) {
                mEnrollButton.setText(R.string.enroll_course_title);
            }
            else {
                mEnrollButton.setText(R.string.remove_course);
            }
        }

        if (mEnrollButton != null) {
            mEnrollButton.setOnClickListener(view -> {
                if (mActivityType.equals("Enroll")) {
                    enroll();
                }
                else {
                    removeCourse();
                }
            });
        }
    }

    public void setStudentUsername(String studentUsername) {
        mStudentUsername = studentUsername;
    }

    public void setRealCourseID(String courseID, String professorUsername) {
        if (courseID == null || professorUsername == null) {
            return;
        }

        mRealCourseID = String.format("%s_%s", courseID, professorUsername);

        mCourseID = courseID;
        mProfessorUsername = professorUsername;
    }

    public void setCourseToRemove(String courseTitle, String courseDescription, String professor) {
        if (courseTitle == null || courseDescription == null || professor == null) {
            return;
        }

        if (getActivity().findViewById(R.id.course) != null) {
            mTextView = getActivity().findViewById(R.id.course).findViewById(R.id.course_id);
            mTextView.setText(String.format("Course ID: %s", mCourseID));

            mTextView = getActivity().findViewById(R.id.course).findViewById(R.id.course_title);
            mTextView.setText(String.format("Course title: %s", courseTitle));

            mTextView = getActivity().findViewById(R.id.course).findViewById(R.id.course_description);
            mTextView.setText(String.format("Course description: %s", courseDescription));

            mTextView = getActivity().findViewById(R.id.course).findViewById(R.id.professor);
            mTextView.setText(String.format("Professor %s", professor));
        }

        mCourseTitle = courseTitle;
        mCourseDescription = courseDescription;
        mProfessor = professor;

        setDescription();
    }

    public void setDescription() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("ScheduledCourses");

        mDescription = getActivity().findViewById(R.id.schedule);

        if (mDescription != null) {
            mDescription.setText("");
        }

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(mRealCourseID)) {
                    for (DataSnapshot localData : snapshot.getChildren()) {
                        mDescription.setText(String.format("%s\n%s: %s:%s - %s:%s", mDescription.getText(), localData.getKey().toString(), localData.child("hoursFrom").getValue().toString(),
                                                                                                            localData.child("minutesFrom").getValue().toString(), localData.child("hoursTo").getValue().toString(),
                                                                                                            localData.child("minutesTo").getValue().toString()));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}