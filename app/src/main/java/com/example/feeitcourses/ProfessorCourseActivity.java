package com.example.feeitcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProfessorCourseActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    CourseAdapter mCourseAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    List<Course> mCourses;
    String mProfessorUsername;

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
        setContentView(R.layout.activity_professor_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.professor_courses);
        setSupportActionBar(toolbar);

        mProfessorUsername = getIntent().getStringExtra("username");
        mCourses = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Courses");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);

                if (course != null) {
                    if (course.professorUsername.equals(mProfessorUsername)) {
                        mCourses.add(course);

                        if (mCourseAdapter != null) {
                            mCourseAdapter.setCourses(mCourses);
                            mRecyclerView.setAdapter(mCourseAdapter);
                        }
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
        mRecyclerView.setAdapter(mCourseAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.new_course) {
            Intent intent = new Intent(this, AddCourseActivity.class);
            intent.putExtra("username", mProfessorUsername);
            startActivity(intent);

            return true;
        }
        else if (menuItem.getItemId() == R.id.view_surveys) {
            Intent intent = new Intent(this, ViewSurveysActivity.class);
            intent.putExtra("username", mProfessorUsername);
            startActivity(intent);

            return true;
        }
        else if (menuItem.getItemId() == R.id.schedule_alert) {
            Intent intent = new Intent(this, ProfessorScheduleActivity.class);
            intent.putExtra("username", mProfessorUsername);
            startActivity(intent);

            return true;
        }
        else if (menuItem.getItemId() == R.id.log_out) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            Toast.makeText(this, "You have logged out!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScheduleCourseFragment scheduleCourseFragment = (ScheduleCourseFragment) getSupportFragmentManager().findFragmentById(R.id.schedule_course);

            if (scheduleCourseFragment != null) {
                scheduleCourseFragment.setProfessorUsername(mProfessorUsername);
            }
        }
    }
}