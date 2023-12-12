package com.example.feeitcourses;

public class GlobalSurvey {
    double importance, readiness, difficulty, materials;

    public GlobalSurvey() {
    }

    public GlobalSurvey(double importance, double readiness, double difficulty, double materials) {
        this.importance = importance;
        this.readiness = readiness;
        this.difficulty = difficulty;
        this.materials = materials;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public void setReadiness(double readiness) {
        this.readiness = readiness;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public void setMaterials(double materials) {
        this.materials = materials;
    }

    public double getImportance() {
        return importance;
    }

    public double getReadiness() {
        return readiness;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public double getMaterials() {
        return materials;
    }
}
