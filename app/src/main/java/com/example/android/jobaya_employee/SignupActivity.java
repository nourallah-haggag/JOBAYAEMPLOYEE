package com.example.android.jobaya_employee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class SignupActivity extends AppCompatActivity {

    // init the views
    EditText fullNameTxt;
    EditText userNameTxt;
    EditText passwordTxt;
    EditText passAgainTxt;
    EditText emailTxt;
    EditText ageTxt;
    ToggleSwitch genderSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // init the views
        fullNameTxt = findViewById(R.id.full_name_txt);
        userNameTxt = findViewById(R.id.user_name_txt);
        passwordTxt = findViewById(R.id.password_txt);
        passAgainTxt = findViewById(R.id.pass_again_txt);
        emailTxt = findViewById(R.id.email_txt);
        ageTxt = findViewById(R.id.age_txt);
        genderSelector = findViewById(R.id.gender_selector);


    }

    public void createAccount(View v)
    {
        if(fullNameTxt.getText().toString().equals("") || userNameTxt.getText().toString().equals("") || passwordTxt.getText().toString().equals("") || passAgainTxt.getText().toString().equals("") || emailTxt.getText().toString().equals("") || ageTxt.getText().toString().equals(""))
        {
            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if (passwordTxt.getText().toString().equals(passAgainTxt.getText().toString()))
            {
                String gender;
                if(genderSelector.getCheckedTogglePosition() == 0)
                {
                    gender = "male";
                }
                else {
                    gender = "female";
                }
                // create account
                JobayaApi jobayaApi = new JobayaApi(this);
                jobayaApi.register(fullNameTxt.getText().toString(), userNameTxt.getText().toString() , passwordTxt.getText().toString() , emailTxt.getText().toString() , ageTxt.getText().toString() , gender);
            }
            else {
                Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
