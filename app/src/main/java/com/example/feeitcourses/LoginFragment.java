package com.example.feeitcourses;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginFragment extends Fragment implements LifecycleObserver {
    Button mLoginButton;
    Button mRegisterButton;
    EditText mUsername;
    EditText mPassword;
    String mLoginType;
    String mEnteredUsername;
    String mEnteredPassword;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    public LoginFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getLifecycle().removeObserver(this);

        mLoginType = getActivity().getIntent().getStringExtra("loginType");
        mEnteredUsername = getActivity().getIntent().getStringExtra("username");
        mEnteredPassword = getActivity().getIntent().getStringExtra("password");

        mLoginButton = getActivity().findViewById(R.id.login_button);
        mRegisterButton = getActivity().findViewById(R.id.register_button);
        mUsername = getActivity().findViewById(R.id.username_login);
        mPassword = getActivity().findViewById(R.id.password_login);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRegisterButton.setVisibility(View.GONE);
        }

        if (mEnteredUsername != null && !mEnteredUsername.isEmpty()) {
            mUsername.setText(mEnteredUsername);
        }

        if (mEnteredPassword != null && !mEnteredPassword.isEmpty()) {
            mPassword.setText(mEnteredPassword);
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().getLifecycle().addObserver(this);
    }

    public void login() {
        mDatabase = FirebaseDatabase.getInstance();

        if (mLoginType.equals("Professor")) {
            mDatabaseReference = mDatabase.getReference("Professors");
        }
        else {
            mDatabaseReference = mDatabase.getReference("Students");
        }

        mEnteredUsername = mUsername.getText().toString();
        mEnteredPassword = mPassword.getText().toString();

        if (mEnteredUsername.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid username", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mEnteredPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabaseReference.child(mEnteredUsername).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap result = (HashMap) task.getResult().getValue();

                    if (result != null) {
                        String password = result.get("password").toString();

                        if (password.equals(mEnteredPassword)) {
                            Toast.makeText(getContext(), "Successful login!", Toast.LENGTH_SHORT).show();
                            Intent intent;

                            if (mLoginType.equals("Professor")) {
                                intent = new Intent(getActivity(), ProfessorCourseActivity.class);
                            }
                            else {
                                intent = new Intent(getActivity(), StudentCourseActivity.class);
                            }

                            intent.putExtra("username", mEnteredUsername);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getContext(), "Wrong password! Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "This user hasn't been registered!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Please check your credentials or register now!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Error when connecting to the database!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            intent.putExtra("loginType", mLoginType);
            startActivity(intent);
        }
    }
}