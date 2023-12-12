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

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 999;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    public void onReceive(Context context, Intent intent) {
        String courseID = intent.getStringExtra("courseID");

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Courses");

        Task<DataSnapshot> dataSnapshotTask = mDatabaseReference.child(courseID).get();
        while (!dataSnapshotTask.isComplete());
        Course course = dataSnapshotTask.getResult().getValue(Course.class);

        showNotification(context, course.getCourseTitle());
    }

    private void showNotification(Context context, String course) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.certificate)
                .setContentTitle("Attendance reminder!")
                .setContentText(String.format("The course \"%s\" is starting in 2 hours!", course))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
}
