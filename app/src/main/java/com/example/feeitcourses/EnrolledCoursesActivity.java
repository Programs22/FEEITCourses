package com.example.feeitcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EnrolledCoursesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    CourseAdapter mCourseAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    List<Course> mCourses;
    String mStudentUsername;

    public static class VerticalSpacer extends RecyclerView.ItemDecoration {
        int mSpacerHeight;

        public VerticalSpacer(int spacerHeight) {
            this.mSpacerHeight = spacerHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.getAdapter() != null) {
                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = mSpacerHeight;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled_courses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.enrolled_courses);

        mStudentUsername = getIntent().getStringExtra("username");
        mCourses = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("EnrolledCourses");

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);
                String key = snapshot.getKey();
                String studentUsername = key.split("_")[2];

                if (mStudentUsername.equals(studentUsername)) {
                    mCourses.add(course);

                    if (mCourseAdapter != null) {
                        mCourseAdapter.setCourses(mCourses);
                        mRecyclerView.setAdapter(mCourseAdapter);
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

        mRecyclerView = findViewById(R.id.courses);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpacer(20));
        mCourseAdapter = new CourseAdapter(mCourses, R.layout.course, this);
        mCourseAdapter.setStudentUsername(mStudentUsername);
        mRecyclerView.setAdapter(mCourseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            EnrollRemoveCourseFragment enrollRemoveCourseFragment = (EnrollRemoveCourseFragment) getSupportFragmentManager().findFragmentById(R.id.enroll_course_fragment);

            if (enrollRemoveCourseFragment != null) {
                enrollRemoveCourseFragment.setActivityType("Remove");
            }
        }
    }
}