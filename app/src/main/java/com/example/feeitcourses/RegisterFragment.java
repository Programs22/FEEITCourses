package com.example.feeitcourses;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterFragment extends Fragment implements LifecycleObserver {
    Button mConfirmRegistration;
    String mLoginType;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    EditText mNameSurname;
    EditText mUsername;
    EditText mPassword;
    EditText mExpertise;
    RadioGroup mAcademicYearChoice;

    public RegisterFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getLifecycle().removeObserver(this);

        mConfirmRegistration = getActivity().findViewById(R.id.confirm_registration);
        mLoginType = getActivity().getIntent().getStringExtra("loginType");

        mNameSurname = getActivity().findViewById(R.id.name_surname);
        mUsername = getActivity().findViewById(R.id.username_register);
        mPassword = getActivity().findViewById(R.id.password_register);
        mExpertise = getActivity().findViewById(R.id.expertise_register);
        mAcademicYearChoice = getActivity().findViewById(R.id.academic_year_control);

        RadioButton yearOne = getActivity().findViewById(R.id.first_year);

        if (yearOne != null) {
            yearOne.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        RadioButton yearTwo = getActivity().findViewById(R.id.second_year);

        if (yearTwo != null) {
            yearTwo.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        RadioButton yearThree = getActivity().findViewById(R.id.third_year);

        if (yearThree != null) {
            yearThree.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        RadioButton yearFour = getActivity().findViewById(R.id.fourth_year);

        if (yearFour != null) {
            yearFour.getButtonDrawable().setColorFilter(getResources().getColor(R.color.primary_blue, null), PorterDuff.Mode.SRC_IN);
        }

        if (mLoginType.equals("Professor") && mAcademicYearChoice != null) {
            mAcademicYearChoice.setVisibility(View.GONE);
        }
        else if (mExpertise != null) {
            mExpertise.setVisibility(View.GONE);
        }

        if (mConfirmRegistration != null) {
            mConfirmRegistration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmRegistration();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().getLifecycle().addObserver(this);
    }

    public void confirmRegistration() {
        mDatabase = FirebaseDatabase.getInstance();

        if (mLoginType.equals("Professor")) {
            mDatabaseReference = mDatabase.getReference("Professors");
        }
        else {
            mDatabaseReference = mDatabase.getReference("Students");
        }

        String nameSurname = mNameSurname.getText().toString();
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        String expertise = "";
        int academicYear = 0;

        if (mExpertise.getVisibility() == View.VISIBLE) {
            expertise = mExpertise.getText().toString();
        }
        else {
            int checkedAcademicYear = mAcademicYearChoice.getCheckedRadioButtonId();

            if (checkedAcademicYear == R.id.first_year) {
                academicYear = 1;
            }
            else if (checkedAcademicYear == R.id.second_year) {
                academicYear = 2;
            }
            else if (checkedAcademicYear == R.id.third_year) {
                academicYear = 3;
            }
            else {
                academicYear = 4;
            }
        }

        if (nameSurname.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid name and surname", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (username.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid username", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mExpertise.getVisibility() == View.VISIBLE && expertise.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid expertise of your field", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mAcademicYearChoice.getVisibility() == View.VISIBLE && academicYear == 0) {
            Toast.makeText(getContext(), "Please choose a valid academic year", Toast.LENGTH_SHORT).show();
            return;
        }

        String finalExpertise = expertise;
        int finalAcademicYear = academicYear;
        mDatabaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap result = (HashMap) task.getResult().getValue();

                    if (result != null) {
                        Toast.makeText(getContext(), "This user has already been registered!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Please login with these credentials!", Toast.LENGTH_SHORT).show();

                        String localUsername = result.get("username").toString();
                        String localPassword = result.get("password").toString();
                        login(localUsername, localPassword);
                    }
                    else {
                        UserProfessorStudent user = new UserProfessorStudent(nameSurname, username, password, finalExpertise, finalAcademicYear);

                        mDatabaseReference.child(username).setValue(user);
                        login(username, password);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Error when connecting to the database!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String username, String password) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
            intent.putExtra("loginType", mLoginType);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }
        else {
            EditText loginUsername = getActivity().findViewById(R.id.username_login);
            EditText loginPassword = getActivity().findViewById(R.id.password_login);

            if (loginUsername != null) {
                loginUsername.setText(username);
            }

            if (loginPassword != null) {
                loginPassword.setText(password);
            }
        }
    }
}