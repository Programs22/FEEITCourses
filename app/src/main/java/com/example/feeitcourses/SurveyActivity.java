package com.example.feeitcourses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SurveyActivity extends AppCompatActivity {
    RadioButton mRadioButton;
    String mCourseID;
    String mProfessorUsername;
    String mStudentUsername;
    String mCourseDateTime;
    RadioGroup mSubjectImportance;
    RadioGroup mProfessorReadiness;
    RadioGroup mSubjectDifficulty;
    RadioGroup mSubjectMaterials;
    Button mSubmitSurvey;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.submit_survey);

        mCourseID = getIntent().getStringExtra("courseID");
        mProfessorUsername = getIntent().getStringExtra("professorUsername");
        mStudentUsername = getIntent().getStringExtra("studentUsername");
        mCourseDateTime = getIntent().getStringExtra("courseDateTime");

        mSubjectImportance = findViewById(R.id.subject_importance_rating);
        mProfessorReadiness = findViewById(R.id.professor_readiness_rating);
        mSubjectDifficulty = findViewById(R.id.subject_difficulty_rating);
        mSubjectMaterials = findViewById(R.id.subject_materials_rating);
        mSubmitSurvey = findViewById(R.id.submit_survey);

        mSubmitSurvey.setOnClickListener(view -> submitSurvey());

        mRadioButton = findViewById(R.id.importance_one);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.importance_two);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.importance_three);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.importance_four);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.importance_five);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.readiness_one);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.readiness_two);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.readiness_three);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.readiness_four);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.readiness_five);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.difficulty_one);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.difficulty_two);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.difficulty_three);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.difficulty_four);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.difficulty_five);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.materials_one);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.materials_two);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.materials_two);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.materials_three);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.materials_four);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        mRadioButton = findViewById(R.id.materials_five);

        if (mRadioButton != null && mRadioButton.getButtonDrawable() != null) {
            mRadioButton.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }
    }

    public void submitSurvey() {
        int importance, readiness, difficulty, materials;

        int selectedSubjectImportance = mSubjectImportance.getCheckedRadioButtonId();
        int selectedProfessorReadiness = mProfessorReadiness.getCheckedRadioButtonId();
        int selectedSubjectDifficulty = mSubjectDifficulty.getCheckedRadioButtonId();
        int selectedSubjectMaterials = mSubjectMaterials.getCheckedRadioButtonId();

        if (selectedSubjectImportance == -1 || selectedProfessorReadiness == -1 || selectedSubjectDifficulty == -1 || selectedSubjectMaterials == -1) {
            Toast.makeText(this, "Please make sure to complete the survey", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSubjectImportance == R.id.importance_one) {
            importance = 1;
        }
        else if (selectedSubjectImportance == R.id.importance_two) {
            importance = 2;
        }
        else if (selectedSubjectImportance == R.id.importance_three) {
            importance = 3;
        }
        else if (selectedSubjectImportance == R.id.importance_four) {
            importance = 4;
        }
        else {
            importance = 5;
        }

        if (selectedProfessorReadiness == R.id.readiness_one) {
            readiness = 1;
        }
        else if (selectedProfessorReadiness == R.id.readiness_two) {
            readiness = 2;
        }
        else if (selectedProfessorReadiness == R.id.readiness_three) {
            readiness = 3;
        }
        else if (selectedProfessorReadiness == R.id.readiness_four) {
            readiness = 4;
        }
        else {
            readiness = 5;
        }

        if (selectedSubjectDifficulty == R.id.difficulty_one) {
            difficulty = 1;
        }
        else if (selectedSubjectDifficulty == R.id.difficulty_two) {
            difficulty = 2;
        }
        else if (selectedSubjectDifficulty == R.id.difficulty_three) {
            difficulty = 3;
        }
        else if (selectedSubjectDifficulty == R.id.difficulty_four) {
            difficulty = 4;
        }
        else {
            difficulty = 5;
        }

        if (selectedSubjectMaterials == R.id.materials_one) {
            materials = 1;
        }
        else if (selectedSubjectMaterials == R.id.materials_two) {
            materials = 2;
        }
        else if (selectedSubjectMaterials == R.id.materials_three) {
            materials = 3;
        }
        else if (selectedSubjectMaterials == R.id.materials_four) {
            materials = 4;
        }
        else {
            materials = 5;
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Surveys");

        SurveyAnswer answer = new SurveyAnswer(importance, readiness, difficulty, materials);
        String surveyID = String.format("%s_%s_%s", mCourseID, mProfessorUsername, mCourseDateTime.replaceAll("\\s", "").replaceAll("/", "")
                                                                                                  .replaceAll(";", "").replaceAll(":", "").replaceAll("-", ""));

        mDatabaseReference.child(surveyID).child(mStudentUsername).setValue(answer);

        Intent intent = new Intent(this, StudentCourseActivity.class);
        intent.putExtra("username", mStudentUsername);
        startActivity(intent);
    }
}