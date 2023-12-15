package com.example.feeitcourses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicInteger;

public class AlarmReceiver extends BroadcastReceiver {
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    public static class NotificationID {
        private final static AtomicInteger notificationID = new AtomicInteger(0);

        public static int getNotificationID() {
            return notificationID.incrementAndGet();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String courseID = intent.getStringExtra("courseID");

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Courses");

        if (courseID != null) {
            Task<DataSnapshot> dataSnapshotTask = mDatabaseReference.child(courseID).get();
            while (!dataSnapshotTask.isComplete());
            Course course = dataSnapshotTask.getResult().getValue(Course.class);

            if (course != null) {
                showNotification(context, course.getCourseTitle());
            }
        }

    }

    private void showNotification(Context context, String course) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.certificate)
                .setContentTitle("Attendance reminder!")
                .setContentText(String.format("The course \"%s\" is starting in 2 hours!", course))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(NotificationID.getNotificationID(), notificationBuilder.build());
        }
    }
}
