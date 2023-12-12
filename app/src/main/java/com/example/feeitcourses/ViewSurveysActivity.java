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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewSurveysActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    SurveyAdapter mSurveyAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mSurveyReference;
    DatabaseReference mProfessorsReference;
    DatabaseReference mCoursesReference;
    String mProfessorUsername;
    String mProfessor;
    List<GlobalSurvey> mSurveyAnswers;
    List<String> mSurveyDateTime;
    List<String> mSurveyCourses;

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
        setContentView(R.layout.activity_view_surveys);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.view_surveys);

        mProfessorUsername = getIntent().getStringExtra("username");

        mSurveyAnswers = new ArrayList<>();
        mSurveyDateTime = new ArrayList<>();
        mSurveyCourses = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mProfessorsReference = mDatabase.getReference("Professors");
        mSurveyReference = mDatabase.getReference("Surveys");
        mCoursesReference = mDatabase.getReference("Courses");

        mProfessorsReference.child(mProfessorUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfessorStudent professor = snapshot.getValue(UserProfessorStudent.class);
                mProfessor = professor.getNameSurname();
                mSurveyAdapter.setProfessor(mProfessor);
                mRecyclerView.setAdapter(mSurveyAdapter);
                setSurveys();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRecyclerView = findViewById(R.id.surveys);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpacer(20));
        mSurveyAdapter = new SurveyAdapter(mSurveyAnswers, mSurveyDateTime, mSurveyCourses, mProfessor, mProfessorUsername, R.layout.survey, this);
        mRecyclerView.setAdapter(mSurveyAdapter);
    }

    public void setSurveys() {
        mSurveyReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String surveyKey = snapshot.getKey();
                String courseID = surveyKey.split("_")[0];
                String professorUsername = surveyKey.split("_")[1];
                String surveyDateTime = surveyKey.split("_")[2];

                if (professorUsername.equals(mProfessorUsername)) {
                    final int surveyAnswers[] = new int[4];
                    double counter = 0;

                    for (int i = 0; i < 4; ++i) {
                        surveyAnswers[i] = 0;
                    }

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SurveyAnswer surveyAnswer = dataSnapshot.getValue(SurveyAnswer.class);

                        surveyAnswers[0] += surveyAnswer.getImportance();
                        surveyAnswers[1] += surveyAnswer.getReadiness();
                        surveyAnswers[2] += surveyAnswer.getDifficulty();
                        surveyAnswers[3] += surveyAnswer.getMaterials();

                        ++counter;
                    }

                    GlobalSurvey globalSurvey = new GlobalSurvey((double) (surveyAnswers[0] / counter), (double) (surveyAnswers[1] / counter),
                            (double) (surveyAnswers[2] / counter), (double) (surveyAnswers[3] / counter));

                    mSurveyAnswers.add(globalSurvey);
                    mSurveyDateTime.add(surveyDateTime);
                    mSurveyCourses.add(courseID);

                    if (mSurveyAdapter != null) {
                        mSurveyAdapter.setSurveyAnswers(mSurveyAnswers);
                        mSurveyAdapter.setSurveyDateTime(mSurveyDateTime);
                        mSurveyAdapter.setCourses(mSurveyCourses);
                        mRecyclerView.setAdapter(mSurveyAdapter);
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