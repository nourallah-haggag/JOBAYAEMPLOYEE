package com.example.android.jobaya_employee;

import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.MultipleToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class JobDetailsActivity extends AppCompatActivity {


    // declare te views
    TextView name;
    TextView desc;
    TextView duration;
    TextView cat;
    TextView gender;
    static TextView hasAppliedTxt;
    static MaterialButton applyButton;
    String email;
    String ID;
    String skills;
    String exp;
    String lang;
    String empMail;
    TextView toggleDetail;
    ToggleSwitch toggleSwitch;
    TextView mailTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);



        // init the views
        name = findViewById(R.id.job_title_details);
        desc = findViewById(R.id.desc_content);
        duration = findViewById(R.id.duration_content);
        cat = findViewById(R.id.cat_content);
        gender = findViewById(R.id.gender_cont);
        hasAppliedTxt = findViewById(R.id.has_applied_txt);
        applyButton = findViewById(R.id.apply_button_detail);

        // set the vies
        name.setText(getIntent().getStringExtra("job"));
        desc.setText(getIntent().getStringExtra("desc"));
        duration.setText(getIntent().getStringExtra("duration"));
        cat.setText(getIntent().getStringExtra("cat"));
        gender.setText(getIntent().getStringExtra("gender"));
        hasAppliedTxt.setVisibility(View.INVISIBLE);
        toggleSwitch = findViewById(R.id.toggle_details);
        toggleDetail = findViewById(R.id.details_txt);
        mailTxt = findViewById(R.id.mail_content);

        // get  parameters for the apply request
         email = LoginActivity.userEmail;
         ID  = getIntent().getStringExtra("ID");

        // start volley request
        JobayaApi jobayaApi = new JobayaApi(this);
        jobayaApi.apply(email , ID);

        // toggle details
        skills = getIntent().getStringExtra("skills");
        exp = getIntent().getStringExtra("exp");
        lang = getIntent().getStringExtra("lang");
        empMail = getIntent().getStringExtra("empMail");

        toggleDetail.setText(skills);

        mailTxt.setText(empMail);

        // null checks
        if(getIntent().getStringExtra("gender") == null)
        {
            gender.setText("Not specified");
        }

        if(empMail == null)
        {
            mailTxt.setText("Not specified");
        }

        if(skills == null)
        {
            toggleDetail.setText("Not specified");
        }

        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position == 0) // skill
                {

                    toggleDetail.setText(skills);
                    if(skills == null)
                    {
                        toggleDetail.setText("Not specified");

                    }


                }
                else if(position == 1)//

                {

                    toggleDetail.setText(exp);
                    if(exp == null)
                    {
                        toggleDetail.setText("Not specified");

                    }



                }
                else  {  // lang
                    toggleDetail.setText(lang);
                    if(lang == null)
                    {
                        toggleDetail.setText("Not specified");

                    }



                }
            }
        });



    }

    public void applyFunction(View v )
    {

        JobayaApi jobayaApi = new JobayaApi(this);
        jobayaApi.postApplications( email, ID);

    }
}
