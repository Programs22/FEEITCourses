package com.example.feeitcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.AlarmManagerCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "Reminder";
    public static final int REQ_CODE = 999;
    ImageButton mProfessor;
    ImageButton mStudent;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    List<String> mScheduledCourses;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        mProfessor = findViewById(R.id.choose_professor);
        mStudent = findViewById(R.id.choose_student);
        mScheduledCourses = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("ScheduledCourses");

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    setAlarm(snapshot.getKey(), dataSnapshot.getKey(), dataSnapshot.getValue(TimeScheduler.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cancelAlarm(snapshot.getKey(), dataSnapshot.getKey(), dataSnapshot.getValue(TimeScheduler.class));
                }
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQ_CODE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.POST_NOTIFICATIONS
            }, REQ_CODE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.USE_EXACT_ALARM
            }, REQ_CODE);
        }

        mProfessor.setOnClickListener(view -> login(mProfessor.getContentDescription().toString()));

        mStudent.setOnClickListener(view -> login(mStudent.getContentDescription().toString()));
    }

    public void login(String loginType) {
        Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
        intent.putExtra("loginType", loginType);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence channelName = "Reminder for each courses";
        String description = "A reminder is sent for each course 2 hours before the course should start";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        notificationChannel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void setAlarm(String courseID, String day, TimeScheduler timeScheduler) {
        String courseIDDay = String.format("%s_%s", courseID, day);
        mScheduledCourses.add(courseIDDay);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("courseID", courseID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mScheduledCourses.size() - 1, intent, PendingIntent.FLAG_IMMUTABLE);

        int calendarDay = getDay(day);
        int calendarHour = Integer.parseInt(timeScheduler.getHoursFrom()) - 2;
        int calendarMinutes = Integer.parseInt(timeScheduler.getMinutesFrom());

        if (calendarDay != -1) {
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            calendar.set(Calendar.HOUR_OF_DAY, calendarHour);
            calendar.set(Calendar.MINUTE, calendarMinutes);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            long exactTime = calendar.getTimeInMillis();
            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, exactTime, pendingIntent);
        }
    }

    private void cancelAlarm(String courseID, String day, TimeScheduler timeScheduler) {
        String courseIDDay = String.format("%s_%s", courseID, day);
        int index = mScheduledCourses.indexOf(courseIDDay);

        if (index != -1) {
            mScheduledCourses.remove(courseIDDay);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, index, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.cancel(pendingIntent);
            setAlarm(courseID, day, timeScheduler);
        }
    }

    public int getDay(String day) {
        switch (day) {
            case "Monday": return 2;
            case "Tuesday": return 3;
            case "Wednesday": return 4;
            case "Thursday": return 5;
            case "Friday": return 6;
            default: return -1;
        }
    }
}