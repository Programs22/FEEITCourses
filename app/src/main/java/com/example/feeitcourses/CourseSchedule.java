package com.example.feeitcourses;

public class CourseSchedule {
    String courseID, courseTitle, courseProfessor, courseDateTime, courseProfessorUsername;

    public CourseSchedule() {

    }

    public CourseSchedule(String courseID, String courseTitle, String courseProfessor, String courseDateTime, String courseProfessorUsername) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseProfessor = courseProfessor;
        this.courseDateTime = courseDateTime;
        this.courseProfessorUsername = courseProfessorUsername;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setCourseProfessor(String courseProfessor) {
        this.courseProfessor = courseProfessor;
    }

    public void setCourseDateTime(String courseDateTime) {
        this.courseDateTime = courseDateTime;
    }

    public void setCourseProfessorUsername(String courseProfessorUsername) {
        this.courseProfessorUsername = courseProfessorUsername;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseProfessor() {
        return courseProfessor;
    }

    public String getCourseDateTime() {
        return courseDateTime;
    }

    public String getCourseProfessorUsername() {
        return courseProfessorUsername;
    }
}
