package com.example.feeitcourses;

public class Course {
    String courseID, courseTitle, courseDescription, professor, professorUsername;

    public Course() {

    }

    public Course(String courseID, String courseTitle, String courseDescription, String professor, String professorUsername) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.professor = professor;
        this.professorUsername = professorUsername;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setProfessorUsername(String professorUsername) {
        this.professorUsername = professorUsername;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getProfessor() {
        return professor;
    }

    public String getProfessorUsername() {
        return professorUsername;
    }
}
