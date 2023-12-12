package com.example.feeitcourses;

public class UserProfessorStudent {
    public String nameSurname, username, password, expertise;
    public int academicYear;

    public UserProfessorStudent() {

    }

    public UserProfessorStudent(String nameSurname, String username, String password, String expertise, int academicYear) {
        this.nameSurname = nameSurname;
        this.username = username;
        this.password = password;
        this.expertise = expertise;
        this.academicYear = academicYear;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExpertise() {
        return expertise;
    }

    public int getAcademicYear() {
        return academicYear;
    }
}
