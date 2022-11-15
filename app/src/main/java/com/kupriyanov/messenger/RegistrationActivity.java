package com.kupriyanov.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;
    private EditText editTextRegisterName;
    private EditText editTextRegisterLastName;
    private EditText editTextRegisterYear;
    private Button buttonSignUp;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextRegisterEmail.getText().toString().trim();
                String password = editTextRegisterPassword.getText().toString().trim();
                String name = editTextRegisterName.getText().toString().trim();
                String lastName = editTextRegisterLastName.getText().toString().trim();
                int age = Integer.parseInt(editTextRegisterYear.getText().toString().trim());
                if (
                        email.equals("")
                        || password.equals("")
                        || name.equals("")
                        || lastName.equals("")
                        || age <= 0
                ){
                    Toast.makeText(
                            RegistrationActivity.this,
                            "Fill in all the fields",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    viewModel.SignUn(email, password, name, lastName, age);
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(
                        RegistrationActivity.this,
                        errorMessage,
                        Toast.LENGTH_SHORT
                        ).show();
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(RegistrationActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void initViews() {
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextRegisterName = findViewById(R.id.editTextRegisterName);
        editTextRegisterLastName = findViewById(R.id.editTextRegisterLastName);
        editTextRegisterYear = findViewById(R.id.editTextRegisterYear);
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}