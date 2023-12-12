package com.example.feeitcourses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {
    int mRowLayout;
    List<GlobalSurvey> mSurveyAnswers;
    List<String> mSurveyDateTime;
    List<String> mCourses;
    String mProfessor;
    String mProfessorUsername;
    Context mContext;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mImportance;
        TextView mReadiness;
        TextView mDifficulty;
        TextView mMaterials;
        TextView mCourse;
        TextView mDateTime;
        TextView mProfessor;

        public ViewHolder(View view) {
            super(view);

            mImportance = view.findViewById(R.id.importance_rating);
            mReadiness = view.findViewById(R.id.readiness_rating);
            mDifficulty = view.findViewById(R.id.difficulty_rating);
            mMaterials = view.findViewById(R.id.materials_rating);
            mCourse = view.findViewById(R.id.survey_course);
            mDateTime = view.findViewById(R.id.survey_date_time);
            mProfessor = view.findViewById(R.id.professor);
        }
    }

    public SurveyAdapter() {

    }

    public SurveyAdapter(List<GlobalSurvey> survey, List<String> surveyDateTime, List<String> courses, String professor, String professorUsername, int rowLayout, Context context) {
        mSurveyAnswers = survey;
        mSurveyDateTime = surveyDateTime;
        mCourses = courses;
        mProfessor = professor;
        mProfessorUsername = professorUsername;
        mRowLayout = rowLayout;
        mContext = context;

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Courses");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        GlobalSurvey survey = mSurveyAnswers.get(index);
        String dateTime = mSurveyDateTime.get(index);
        String course = mCourses.get(index);

        String realCourseID = String.format("%s_%s", course, mProfessorUsername);
        mDatabaseReference.child(realCourseID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewHolder.mCourse.setText(String.format("Course name: %s", snapshot.getValue(Course.class).getCourseTitle()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String date = dateTime.substring(0, 8);
        String time = dateTime.substring(8);

        String day = date.substring(0, 2);
        String month = date.substring(2, 4);
        String year = date.substring(4);
        String surveyDate = String.format("%s/%s/%s", day, month, year);

        String startHour = time.substring(0, 2);
        String startMinutes = time.substring(2, 4);
        String endHour = time.substring(4, 6);
        String endMinutes = time.substring(6);
        String surveyTime = String.format("%s:%s - %s:%s", startHour, startMinutes, endHour, endMinutes);

        viewHolder.mImportance.setText(String.format("Subject importance global rating: %s", Double.toString(survey.getImportance())));
        viewHolder.mReadiness.setText(String.format("Professor readiness global rating: %s", Double.toString(survey.getReadiness())));
        viewHolder.mDifficulty.setText(String.format("Subject difficulty global rating: %s", Double.toString(survey.getDifficulty())));
        viewHolder.mMaterials.setText(String.format("Subject materials available global rating: %s", Double.toString(survey.getMaterials())));
        viewHolder.mDateTime.setText(String.format("Survey course schedule: %s: %s", surveyDate, surveyTime));
        viewHolder.mProfessor.setText(String.format("Assigned professor: %s", mProfessor));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AttendanceSurveyActivity.class);
                intent.putExtra("professorUsername", mProfessorUsername);
                intent.putExtra("courseID", course);
                intent.putExtra("courseDateTime", dateTime);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSurveyAnswers != null ? mSurveyAnswers.size() : 0;
    }

    public void setSurveyAnswers(List<GlobalSurvey> survey) {
        mSurveyAnswers = survey;
    }

    public void setSurveyDateTime(List<String> surveyDateTime) {
        mSurveyDateTime = surveyDateTime;
    }

    public void setCourses(List<String> courses) {
        mCourses = courses;
    }

    public void setProfessor(String professor) {
        mProfessor = professor;
    }
}
