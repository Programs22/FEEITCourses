package com.example.feeitcourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    int mRowLayout;
    List<AttendingStudents> mAttendees;
    Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mStudentName;
        TextView mAcademicYear;

        public ViewHolder(View view) {
            super(view);

            mStudentName = view.findViewById(R.id.student_name);
            mAcademicYear = view.findViewById(R.id.student_year);
        }
    }

    public AttendanceAdapter() {

    }

    public AttendanceAdapter(List<AttendingStudents> attendees, int rowLayout, Context context) {
        mAttendees = attendees;
        mRowLayout = rowLayout;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        AttendingStudents student = mAttendees.get(index);

        viewHolder.mStudentName.setText(String.format("Student: %s", student.getStudentName()));
        viewHolder.mAcademicYear.setText(String.format("Academic year: %s", String.valueOf(student.getAcademicYear())));
    }

    @Override
    public int getItemCount() {
        return mAttendees != null ? mAttendees.size() : 0;
    }

    public void setAttendees(List<AttendingStudents> attendees) {
        mAttendees = attendees;
    }
}
