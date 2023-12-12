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
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorScheduleActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    CourseScheduleAdapter mCourseScheduleAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mScheduledCoursesReference;
    DatabaseReference mCoursesReference;
    String mProfessorUsername;
    Calendar mCalendar;
    List<Course> mCourses;
    HashMap<String, HashMap<String, TimeScheduler>> mScheduledCourses;
    List<CourseSchedule> mSchedule;

    public class VerticalSpacer extends RecyclerView.ItemDecoration {
        int mSpacerHeight;

        public VerticalSpacer(int spacerHeight) {
            this.mSpacerHeight = spacerHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mSpacerHeight;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.weekly_schedule);

        mProfessorUsername = getIntent().getStringExtra("username");
        mCourses = new ArrayList<>();
        mScheduledCourses = new HashMap<>();
        mSchedule = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mCoursesReference = mDatabase.getReference("Courses");
        mScheduledCoursesReference = mDatabase.getReference("ScheduledCourses");

        mCoursesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mCourses.add(snapshot.getValue(Course.class));
                setSchedule();
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

        mScheduledCoursesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String courseID = snapshot.getKey();

                if (courseID.split("_")[1].equals(mProfessorUsername)) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HashMap<String, TimeScheduler> localMap = new HashMap<>();
                        String day = dataSnapshot.getKey();
                        TimeScheduler timeScheduler = dataSnapshot.getValue(TimeScheduler.class);

                        localMap.put(day, timeScheduler);
                        mScheduledCourses.put(courseID, localMap);
                        setSchedule();
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

        mRecyclerView = findViewById(R.id.schedule);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpacer(20));
        mCourseScheduleAdapter = new CourseScheduleAdapter(mSchedule, R.layout.schedule, this, "");
        mRecyclerView.setAdapter(mCourseScheduleAdapter);
    }

    public String getDay(int day) {
        switch (day) {
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            default: return "Invalid";
        }
    }

    public void setSchedule() {
        mCalendar = GregorianCalendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int counter = 0;

        do {
            String currentDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));

            if (!currentDay.equals("Sunday") && !currentDay.equals("Saturday") && !currentDay.equals("Invalid")) {
                for (Course course : mCourses) {
                    String realCourseID = String.format("%s_%s", course.getCourseID(), course.getProfessorUsername());
                    HashMap<String, TimeScheduler> scheduledCourse = mScheduledCourses.get(realCourseID);

                    if (scheduledCourse != null) {
                        for (Map.Entry<String, TimeScheduler> mapEntry : scheduledCourse.entrySet()) {
                            if (currentDay.equals(mapEntry.getKey())) {
                                TimeScheduler timeScheduler = mapEntry.getValue();
                                String date = dateFormat.format(mCalendar.getTime());
                                String scheduleDate = String.format("%s; %s:%s - %s:%s", date, timeScheduler.getHoursFrom(), timeScheduler.getMinutesFrom(), timeScheduler.getHoursTo(), timeScheduler.getMinutesTo());

                                CourseSchedule courseSchedule = new CourseSchedule(course.getCourseID(), course.getCourseTitle(), course.getProfessor(), scheduleDate, course.getProfessorUsername());

                                boolean found = false;

                                for (CourseSchedule schedule : mSchedule) {
                                    if (courseSchedule.getCourseID().equals(schedule.getCourseID()) && courseSchedule.getCourseTitle().equals(schedule.getCourseTitle()) &&
                                        courseSchedule.getCourseProfessor().equals(schedule.getCourseProfessor()) && courseSchedule.getCourseDateTime().equals(schedule.getCourseDateTime()) &&
                                        courseSchedule.getCourseProfessorUsername().equals(schedule.getCourseProfessorUsername())) {
                                        found = true;
                                        break;
                                    }
                                }

                                if (!found) {
                                    mSchedule.add(courseSchedule);
                                    mSchedule.sort(new Comparator<CourseSchedule>() {
                                        @Override
                                        public int compare(CourseSchedule scheduleOne, CourseSchedule scheduleTwo) {
                                            return scheduleOne.getCourseDateTime().compareTo(scheduleTwo.getCourseDateTime());
                                        }
                                    });

                                    if (mCourseScheduleAdapter != null) {
                                        mCourseScheduleAdapter.setSchedule(mSchedule);
                                        mRecyclerView.setAdapter(mCourseScheduleAdapter);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            mCalendar.add(Calendar.DAY_OF_WEEK, 1);
            ++counter;
        } while (counter < 7);
    }
}