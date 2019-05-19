package com.example.android.jobaya_employee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    // declare the views
    TextInputEditText emailTxt;
    TextInputEditText passTxt;
    TextView signupTxt;

    // declare the shared preferences object
    static SharedPreferences sharedPreferences;
    static String userEmail;
    static String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check if user is logged in or not
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        checkSharedPref();

        // init the views
        emailTxt = findViewById(R.id.email_txt_login);
        passTxt = findViewById(R.id.pass_txt_login);
        signupTxt = findViewById(R.id.signup_txt_login);

        // fill fields when coming from signup activity
        if(getIntent().getStringExtra("email")!=null)
        {
            emailTxt.setText(getIntent().getStringExtra("email"));
            passTxt.setText(getIntent().getStringExtra("pass"));
        }

        // set event on signup text click
        signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open signuo activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    // action when pressing the login button
    public void loginFunction(View v) throws JSONException, MalformedURLException {

        // show progress loading
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // convert the text from the text fields to json
        JSONObject jsonObject = new JSONObject();
        Gson gson = new Gson();
        final String email = gson.toJson(emailTxt.getText().toString());
        final String pass = gson.toJson(passTxt.getText().toString());
        jsonObject.put("email", emailTxt.getText().toString());
        jsonObject.put("password", passTxt.getText().toString());

        // api end point
        String url = "https://jobayaback.herokuapp.com/login";


        // send POST volley request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.cancel();


                //  handle the response (login or prevent access)
                try {
                    String result = response.getString("found");
                    if (result.equals("true")) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        userEmail = emailTxt.getText().toString();

                        // login success - place data in shared pref
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedIn" , "true");
                        editor.putString("email" , emailTxt.getText().toString());
                        editor.commit();
                        intent.putExtra("email", emailTxt.getText().toString());
                        finish();
                        startActivity(intent);
                    } else {

                        Toast.makeText(LoginActivity.this, "failed, incorrect email or password", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();

                // volley error
                Toast.makeText(LoginActivity.this, "failed, please check network connection", Toast.LENGTH_SHORT).show();
            }


        });

        // start the volley request
        Volley.newRequestQueue(this).add(loginRequest);
    }

    // check for shared prefrences instead of logging in every time
    public void checkSharedPref()
    {


        if(sharedPreferences.getString("loggedIn" , "false").equals("true"))
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // if logged in get the email from shared pref to pass to later activities and fragments
            userEmail = sharedPreferences.getString("email" , null);
            finish();
            startActivity(intent);

        }
    }
}
