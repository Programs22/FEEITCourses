package com.example.feeitcourses;

public class AttendingStudents {
    String studentName;
    int academicYear;

    AttendingStudents() {

    }

    AttendingStudents(String studentName, int academicYear) {
        this.studentName = studentName;
        this.academicYear = academicYear;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getAcademicYear() {
        return academicYear;
    }
}
