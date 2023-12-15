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

public class CourseScheduleAdapter extends RecyclerView.Adapter<CourseScheduleAdapter.ViewHolder> {
    int mRowLayout;
    List<CourseSchedule> mSchedule;
    Context mContext;
    String mStudentUsername;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCourseID;
        TextView mCourseTitle;
        TextView mProfessor;
        TextView mCourseSchedule;

        public ViewHolder(View view) {
            super(view);

            mCourseID = view.findViewById(R.id.course_id);
            mCourseTitle = view.findViewById(R.id.course_title);
            mProfessor = view.findViewById(R.id.professor);
            mCourseSchedule = view.findViewById(R.id.schedule);
        }
    }

    public CourseScheduleAdapter() {

    }

    public CourseScheduleAdapter(List<CourseSchedule> schedule, int rowLayout, Context context, String studentUsername) {
        mSchedule = schedule;
        mRowLayout = rowLayout;
        mContext = context;
        mStudentUsername = studentUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        CourseSchedule courseSchedule = mSchedule.get(index);

        viewHolder.mCourseID.setText(String.format("Course ID: %s", courseSchedule.getCourseID()));
        viewHolder.mCourseTitle.setText(String.format("Course Title: %s", courseSchedule.getCourseTitle()));
        viewHolder.mProfessor.setText(String.format("Professor: %s", courseSchedule.getCourseProfessor()));
        viewHolder.mCourseSchedule.setText(String.format("Schedule: %s", courseSchedule.getCourseDateTime()));

        viewHolder.itemView.setOnClickListener(view -> {
            if (mContext.getClass() == StudentScheduleActivity.class) {
                Intent intent = new Intent(mContext, AttendCourseActivity.class);
                intent.putExtra("username", mStudentUsername);
                intent.putExtra("courseID", courseSchedule.getCourseID());
                intent.putExtra("courseTitle", courseSchedule.getCourseTitle());
                intent.putExtra("professor", courseSchedule.getCourseProfessor());
                intent.putExtra("professorUsername", courseSchedule.getCourseProfessorUsername());
                intent.putExtra("courseDateTime", courseSchedule.getCourseDateTime());
                mContext.startActivity(intent);
            }
            else if (mContext.getClass() == ProfessorScheduleActivity.class) {
                Intent intent = new Intent(mContext, ActiveAttendanceActivity.class);
                intent.putExtra("professorUsername", courseSchedule.getCourseProfessorUsername());
                intent.putExtra("courseID", courseSchedule.getCourseID());
                intent.putExtra("courseDateTime", courseSchedule.getCourseDateTime());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSchedule != null ? mSchedule.size() : 0;
    }

    public void setSchedule(List<CourseSchedule> schedule) {
        mSchedule = schedule;
    }
}
