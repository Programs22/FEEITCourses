package com.example.feeitcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class StudentCourseActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    CourseAdapter mCourseAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mCourseReference;
    DatabaseReference mEnrolledCoursesReference;
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
        setContentView(R.layout.activity_student_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.available_courses);
        setSupportActionBar(toolbar);

        mStudentUsername = getIntent().getStringExtra("username");
        mCourses = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mCourseReference = mDatabase.getReference("Courses");
        mEnrolledCoursesReference = mDatabase.getReference("EnrolledCourses");

        mCourseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);

                mEnrolledCoursesReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.getKey().split("_")[2].equals(mStudentUsername)) {
                            Course enrolledCourse = snapshot.getValue(Course.class);

                            if (course != null && enrolledCourse != null) {
                                if (course.getCourseID().equals(enrolledCourse.getCourseID()) && course.getProfessorUsername().equals(enrolledCourse.getProfessorUsername())) {
                                    mCourses.remove(course);

                                    if (mCourseAdapter != null) {
                                        mCourseAdapter.setCourses(mCourses);
                                        mRecyclerView.setAdapter(mCourseAdapter);
                                    }
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

                if (!mCourses.contains(course)) {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.enrolled_courses) {
            Intent intent = new Intent(this, EnrolledCoursesActivity.class);
            intent.putExtra("username", mStudentUsername);
            startActivity(intent);

            return true;
        }
        else if (menuItem.getItemId() == R.id.schedule_alert) {
            Intent intent = new Intent(this, StudentScheduleActivity.class);
            intent.putExtra("username", mStudentUsername);
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
}