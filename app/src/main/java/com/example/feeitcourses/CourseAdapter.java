package com.example.feeitcourses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    int mRowLayout;
    List<Course> mCourses;
    Context mContext;
    String mStudentUsername;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCourseID;
        TextView mCourseTitle;
        TextView mProfessor;
        TextView mCourseDescription;

        public ViewHolder(View view) {
            super(view);

            mCourseID = view.findViewById(R.id.course_id);
            mCourseTitle = view.findViewById(R.id.course_title);
            mProfessor = view.findViewById(R.id.professor);
            mCourseDescription = view.findViewById(R.id.course_description);
        }
    }

    public CourseAdapter() {

    }

    public CourseAdapter(List<Course> courses, int rowLayout, Context context) {
        mCourses = courses;
        mRowLayout = rowLayout;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        Course course = mCourses.get(index);

        viewHolder.mCourseID.setText(String.format("Course ID: %s", course.getCourseID()));
        viewHolder.mCourseTitle.setText(String.format("Course title: %s", course.getCourseTitle()));
        viewHolder.mCourseDescription.setText(String.format("Course description: %s", course.getCourseDescription()));
        viewHolder.mProfessor.setText(String.format("Professor: %s", course.getProfessor()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext.getClass() == ProfessorCourseActivity.class) {
                    Intent intent = new Intent(mContext, ScheduleCourseActivity.class);
                    intent.putExtra("courseID", course.getCourseID());
                    intent.putExtra("courseTitle", course.getCourseTitle());
                    intent.putExtra("username", course.getProfessorUsername());
                    mContext.startActivity(intent);
                }
                else if (mContext.getClass() == StudentCourseActivity.class) {
                    Intent intent = new Intent(mContext, EnrollRemoveCourseActivity.class);
                    intent.putExtra("activityType", "Enroll");
                    intent.putExtra("courseID", course.getCourseID());
                    intent.putExtra("courseTitle", course.getCourseTitle());
                    intent.putExtra("courseDescription", course.getCourseDescription());
                    intent.putExtra("professor", course.getProfessor());
                    intent.putExtra("professorUsername", course.getProfessorUsername());
                    intent.putExtra("studentUsername", mStudentUsername);
                    mContext.startActivity(intent);
                }
                else if (mContext.getClass() == EnrolledCoursesActivity.class) {
                    Intent intent = new Intent(mContext, EnrollRemoveCourseActivity.class);
                    intent.putExtra("activityType", "Remove");
                    intent.putExtra("courseID", course.getCourseID());
                    intent.putExtra("courseTitle", course.getCourseTitle());
                    intent.putExtra("courseDescription", course.getCourseDescription());
                    intent.putExtra("professor", course.getProfessor());
                    intent.putExtra("professorUsername", course.getProfessorUsername());
                    intent.putExtra("studentUsername", mStudentUsername);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses != null ? mCourses.size() : 0;
    }

    public void setStudentUsername(String studentUsername) {
        mStudentUsername = studentUsername;
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
    }
}