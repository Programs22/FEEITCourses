package com.example.feeitcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ActiveAttendanceActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    AttendanceAdapter mAttendanceAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mStudentsReference;
    DatabaseReference mAttendanceReference;
    String mProfessorUsername;
    String mCourseID;
    String mCourseDateTime;
    List<UserProfessorStudent> mStudents;
    List<String> mActiveAttending;
    List<AttendingStudents> mAttendees;
    List<String> mRecordedAttendees;
    Calendar mCalendar;
    SimpleDateFormat mDateFormat;

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
        setContentView(R.layout.activity_active_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.active_attendance);

        mProfessorUsername = getIntent().getStringExtra("professorUsername");
        mCourseID = getIntent().getStringExtra("courseID");
        mCourseDateTime = getIntent().getStringExtra("courseDateTime");

        mStudents = new ArrayList<>();
        mActiveAttending = new ArrayList<>();
        mAttendees = new ArrayList<>();
        mRecordedAttendees = new ArrayList<>();

        mCalendar = GregorianCalendar.getInstance();
        mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        mDatabase = FirebaseDatabase.getInstance();
        mStudentsReference = mDatabase.getReference("Students");

        String attendanceID = String.format("%s_%s_%s", mCourseID, mProfessorUsername, mCourseDateTime.replaceAll("\\s", "").replaceAll("/", "")
                                                                                                      .replaceAll(";", "").replaceAll(":", "").replaceAll("-", ""));
        mAttendanceReference = mDatabase.getReference("Attendance").child(attendanceID);

        mStudentsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mStudents.add(snapshot.getValue(UserProfessorStudent.class));
                setAttendance();
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

        mAttendanceReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mActiveAttending.add(snapshot.getKey());
                setAttendance();
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

        mRecyclerView = findViewById(R.id.attendance);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpacer(20));
        mAttendanceAdapter = new AttendanceAdapter(mAttendees, R.layout.active_student, this);
        mRecyclerView.setAdapter(mAttendanceAdapter);
    }

    public void setAttendance() {
        String today = mDateFormat.format(mCalendar.getTime());
        String courseDateTime = mCourseDateTime.replaceAll("\\s", "");

        String dateString = courseDateTime.split(";")[0];
        String timeString = courseDateTime.split(";")[1];
        String startTime = timeString.split("-")[0];
        String endTime = timeString.split("-")[1];

        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int startMinutes = Integer.parseInt(startTime.split(":")[1]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);

        if (today.equals(dateString)) {
            if (mCalendar.get(Calendar.HOUR_OF_DAY) >= startHour && mCalendar.get(Calendar.HOUR_OF_DAY) < endHour) {
                if (mCalendar.get(Calendar.MINUTE) >= startMinutes) {
                    for (UserProfessorStudent student : mStudents) {
                        for (String studentID : mActiveAttending) {
                            if (studentID.equals(student.getUsername())) {
                                AttendingStudents attendee = new AttendingStudents(student.getNameSurname(), student.getAcademicYear());

                                if (!mRecordedAttendees.contains(studentID)) {
                                    mAttendees.add(attendee);
                                    mRecordedAttendees.add(studentID);
                                }

                                if (mAttendanceAdapter != null) {
                                    mAttendanceAdapter.setAttendees(mAttendees);
                                    mRecyclerView.setAdapter(mAttendanceAdapter);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}