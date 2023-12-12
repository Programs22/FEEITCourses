package com.example.feeitcourses;

public class SurveyAnswer {
    int importance, readiness, difficulty, materials;

    SurveyAnswer() {

    }

    SurveyAnswer(int importance, int readiness, int difficulty, int materials) {
        this.importance = importance;
        this.readiness = readiness;
        this.difficulty = difficulty;
        this.materials = materials;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setReadiness(int readiness) {
        this.readiness = readiness;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setMaterials(int materials) {
        this.materials = materials;
    }

    public int getImportance() {
        return importance;
    }

    public int getReadiness() {
        return readiness;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMaterials() {
        return materials;
    }
}
