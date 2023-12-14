package com.example.feeitcourses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AttendCourseActivity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationCallback mLocationCallback;
    Button mAttendButton;
    Button mSurveyButton;
    TextView mTextView;
    String mCourseID;
    String mCourseTitle;
    String mProfessor;
    String mStudentUsername;
    String mProfessorUsername;
    String mCourseDateTime;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.attend_course);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mAttendButton = findViewById(R.id.attend);
        mAttendButton.setVisibility(View.GONE);

        mSurveyButton = findViewById(R.id.survey);
        mSurveyButton.setVisibility(View.GONE);

        mCourseID = getIntent().getStringExtra("courseID");
        mCourseTitle = getIntent().getStringExtra("courseTitle");
        mProfessor = getIntent().getStringExtra("professor");
        mStudentUsername = getIntent().getStringExtra("username");
        mProfessorUsername = getIntent().getStringExtra("professorUsername");
        mCourseDateTime = getIntent().getStringExtra("courseDateTime");

        mTextView = findViewById(R.id.course).findViewById(R.id.course_id);
        mTextView.setText(String.format("Course ID: %s", mCourseID));

        mTextView = findViewById(R.id.course).findViewById(R.id.course_title);
        mTextView.setText(String.format("Course Title: %s", mCourseTitle));

        mTextView = findViewById(R.id.course).findViewById(R.id.professor);
        mTextView.setText(String.format("Professor: %s", mProfessor));

        mTextView = findViewById(R.id.course).findViewById(R.id.schedule);
        mTextView.setText(String.format("Schedule: %s", mCourseDateTime));

        mAttendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendCourse();
            }
        });

        mSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSurvey();
            }
        });

        checkAttendanceAvailability();
        checkSurveyAvailability();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MainActivity.REQ_CODE);
        }
        else {
            checkLocationSettings();
        }
    }

    private void checkAttendanceAvailability() {
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(calendar.getTime());

        String courseDateTime = mCourseDateTime.replaceAll("\\s", "");

        String dateString = courseDateTime.split(";")[0];
        String timeString = courseDateTime.split(";")[1];
        String startTime = timeString.split("-")[0];
        String endTime = timeString.split("-")[1];

        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int startMinutes = Integer.parseInt(startTime.split(":")[1]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);

        if (today.equals(dateString)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) >= startHour && calendar.get(Calendar.HOUR_OF_DAY) < endHour ) {
                if (calendar.get(Calendar.MINUTE) >= startMinutes) {
                    mAttendButton.setVisibility(View.VISIBLE);
                    mAttendButton.setEnabled(false);
                    mAttendButton.setBackgroundColor(getResources().getColor(R.color.disabled, null));
                }
            }
        }
    }

    private void checkSurveyAvailability() {
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(calendar.getTime());

        String courseDateTime = mCourseDateTime.replaceAll("\\s", "");

        String dateString = courseDateTime.split(";")[0];
        String timeString = courseDateTime.split(";")[1];
        String endTime = timeString.split("-")[1];

        int endHour = Integer.parseInt(endTime.split(":")[0]);
        int endMinutes = Integer.parseInt(endTime.split(":")[1]);

        if (today.equals(dateString)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) >= endHour && calendar.get(Calendar.MINUTE) >= endMinutes) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mDatabase.getReference("Attendance");
                String attendanceID = String.format("%s_%s_%s", mCourseID, mProfessorUsername, mCourseDateTime.replaceAll("\\s", "").replaceAll("/", "")
                                                                                                              .replaceAll(";", "").replaceAll(":", "").replaceAll("-", ""));

                mDatabaseReference.child(attendanceID).child(mStudentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            mSurveyButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    startLocationUpdates();
                }
                catch (ApiException e) {
                    Toast.makeText(AttendCourseActivity.this, "Location settings aren't turned on!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AttendCourseActivity.this, "Please turn on your location settings!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplication(), StudentScheduleActivity.class);
                    intent.putExtra("username", mStudentUsername);
                    startActivity(intent);
                }
            }
        });
    }

    private LocationRequest createLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setMinUpdateIntervalMillis(1000)
                .build();
    }

    private void startLocationUpdates() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {
                    double userLatitude = location.getLatitude();
                    double userLongitude = location.getLongitude();

                    checkIfInsideFaculty(userLatitude, userLongitude);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), mLocationCallback, Looper.getMainLooper());
        }
    }

    private void checkIfInsideFaculty(double userLatitude, double userLongitude) {
        double facultyLatitude = 42.0050404;
        double facultyLongitude = 21.4079131;
        float[] distance = new float[2];

        Location.distanceBetween(userLatitude, userLongitude, facultyLatitude, facultyLongitude, distance);
        double distanceInMeters = distance[0];
        double radiusInMeters = 300;

        if (distanceInMeters <= radiusInMeters) {
            mAttendButton.setEnabled(true);
            mAttendButton.setBackgroundColor(getResources().getColor(R.color.primary_blue, null));
        }
        else {
            mAttendButton.setEnabled(false);
            mAttendButton.setBackgroundColor(getResources().getColor(R.color.disabled, null));
        }
    }

    public void attendCourse() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Attendance");

        String attendanceID = String.format("%s_%s_%s", mCourseID, mProfessorUsername, mCourseDateTime.replaceAll("\\s", "").replaceAll("/", "")
                                                                                                      .replaceAll(";", "").replaceAll(":", "").replaceAll("-", ""));
        mDatabaseReference.child(attendanceID).child(mStudentUsername).setValue("Present");

        Intent intent = new Intent(this, StudentCourseActivity.class);
        intent.putExtra("username", mStudentUsername);
        startActivity(intent);
    }

    public void startSurvey() {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putExtra("courseID", mCourseID);
        intent.putExtra("professorUsername", mProfessorUsername);
        intent.putExtra("studentUsername", mStudentUsername);
        intent.putExtra("courseDateTime", mCourseDateTime);
        startActivity(intent);
    }
}