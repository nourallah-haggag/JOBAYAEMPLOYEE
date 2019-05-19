package com.example.android.jobaya_employee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cdflynn.android.library.checkview.CheckView;

public class JobayaApi {
    private String allJobsUrl = "https://jobayaback.herokuapp.com/jobs/all";
    private String appliedJobsUrl = "https://jobayaback.herokuapp.com/applications/"+LoginActivity.userEmail;
    private String applyForJobUrl = "https://jobayaback.herokuapp.com/applications";
    private String registerApiUrl = "https://jobayaback.herokuapp.com/register";
    private String applyUrl = "https://jobayaback.herokuapp.com/apply";
     Context context;

     public JobayaApi(Context context)
     {
         this.context = context;
     }

    public  void getAllJobs()
    {

        // start the loading progress
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        // start the volley request
        StringRequest allJobsRequest = new StringRequest(Request.Method.GET, allJobsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // cancel the loading on success
                progressDialog.cancel();

                // request successful , parse the json response
                Log.i("response" , response);
                try {
                    JSONArray jobsArray = new JSONArray(response);
                    // clear the all jobs list first to prevent duplicates
                    MainActivity.AllJobsList.clear();
                    FragmentAllJobs.jobsList.clear();
                    for(int i =0 ; i<jobsArray.length() ; i++)
                    {

                        JSONObject retrievedJob = jobsArray.getJSONObject(i);
                        String gender = retrievedJob.isNull("gender") ? null : retrievedJob.getString("gender");
                        String title = retrievedJob.isNull("title") ? null : retrievedJob.getString("title");
                        String desc = retrievedJob.isNull("description") ? null : retrievedJob.getString("description");
                        String cat = retrievedJob.isNull("category") ? null : retrievedJob.getString("category");
                        String duration = retrievedJob.isNull("duration") ? null : retrievedJob.getString("duration");
                        String age = retrievedJob.isNull("age") ? null : retrievedJob.getString("age");
                        String ID = retrievedJob.isNull("_id") ? null : retrievedJob.getString("_id");
                        String skills = retrievedJob.isNull("skills") ? null : retrievedJob.getString("skills");
                        String exp = retrievedJob.isNull("experience") ? null : retrievedJob.getString("experience");
                        String lang = retrievedJob.isNull("language") ? null : retrievedJob.getString("language");
                        String empMail = retrievedJob.isNull("employer_email") ? null : retrievedJob.getString("employer_email");
                        Job job = new Job(title , desc , cat , duration , age ,ID , gender , skills , exp , lang , empMail );
                        FragmentAllJobs.jobsList.add(job);
                        // send the data to the host activity
                        MainActivity.AllJobsList.add(job); // will be used later by the applied job function

                    }


                    Collections.reverse(FragmentAllJobs.jobsList);
                    FragmentAllJobs.adapterAllJobs.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // cancel the loading on fail
                progressDialog.cancel();

                // request failed
                // will return null list
                //Log.i("error" , error.getMessage());
                FragmentAllJobs.errorImage.setVisibility(View.VISIBLE);
                FragmentAllJobs.errorText.setVisibility(View.VISIBLE);
                FragmentAllJobs.errorText.setText("Please check internet connection");
                FragmentAllJobs.errorButton.setVisibility(View.VISIBLE);



            }
        });

        Volley.newRequestQueue(context).add(allJobsRequest);
    }

    public void getAppliedJobs()
    {
        // start the loading progress
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // get the ids of the applied jobs and compare them to view only the jobs with the ids retrieved from the api call
        final List<String> jobIds;
        final List<Job> jobsList;

        jobIds = new ArrayList<>();
        //jobsList = new ArrayList<>();

        jobsList = MainActivity.AllJobsList; // get the list from the host activity after it has been populated in the all jobs fragment

        // applied Jobs List
        final List<Job> appliedJobsList = new ArrayList<>();

        // volley request to get applied jobs
        StringRequest appliedJobsRequest = new StringRequest(Request.Method.GET, appliedJobsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();


                try {
                    MainActivity.AppliedJobsList.clear();
                    FragmentAppliedJobs.appliedJobsLst.clear();
                    JSONArray idsArray = new JSONArray(response);
                    // check if there is any applied jobs or not
                    if(idsArray.length() == 0)
                    {
                        Toast.makeText(context, "apply for jobs", Toast.LENGTH_SHORT).show();
                        FragmentAppliedJobs.errorImage.setVisibility(View.VISIBLE);
                        FragmentAppliedJobs.errorImage.setImageResource(R.drawable.noapply);
                        FragmentAppliedJobs.errorText.setVisibility(View.VISIBLE);
                        FragmentAppliedJobs.errorText.setText("You have not applied for any job yet !");
                    }
                    for(int i =0 ; i<idsArray.length() ; i++)
                    {
                        JSONObject idJSON = idsArray.getJSONObject(i);
                        jobIds.add(idJSON.getString("job_ID"));
                        for (int j =0 ; j<jobsList.size() ; j++)
                        {
                            if(jobsList.get(j).ID.equals(idJSON.getString("job_ID"))){
                               // appliedJobsList.add(jobsList.get(j));
                               // adapterAllJobs.notifyDataSetChanged();
                                Log.i("applied" , jobsList.get(j).title);
                                appliedJobsList.add(jobsList.get(j));
                                MainActivity.AppliedJobsList.add(jobsList.get(j));
                                FragmentAppliedJobs.appliedJobsLst.add(jobsList.get(j));



                            }

                            //MainActivity.AllJobsList = appliedJobsList;
                        }

                    }
                    Collections.reverse(FragmentAppliedJobs.appliedJobsLst);
                    FragmentAppliedJobs.adapterAppliedJobs.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();

                FragmentAppliedJobs.errorImage.setVisibility(View.VISIBLE);
                FragmentAppliedJobs.errorImage.setImageResource(R.drawable.xmark);
                FragmentAppliedJobs.errorText.setVisibility(View.VISIBLE);
                FragmentAppliedJobs.errorText.setText("Please check internet connection");
                FragmentAppliedJobs.errorButton.setVisibility(View.VISIBLE);

            }
        });


        // init the request
        Volley.newRequestQueue(context).add(appliedJobsRequest);





    }

    public void register(String fullName , String userName , final String pass , final String email , String age , String gender)
    {

        // start the loading progress
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("creating account...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // define the json body that will be sent in the post request

        JSONObject regBody = new JSONObject();
        try {
            regBody.put("name" , fullName );
            regBody.put("username" , userName );
            regBody.put("password" , pass );
            regBody.put("email" , email );
            regBody.put("age" , age );
            regBody.put("gender" , gender );

            // start the request after the body is populated
            JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, registerApiUrl, regBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.cancel();

                   // Toast.makeText(context, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context , LoginActivity.class);
                    ((SignupActivity)context).finish();
                    intent.putExtra("email" , email);
                    intent.putExtra("pass" , pass);
                    context.startActivity(intent);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    Toast.makeText(context, "failed, please check internet connection", Toast.LENGTH_LONG).show();

                }
            });

            Volley.newRequestQueue(context).add(registerRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void apply(String email , String ID)
    {
        // start the loading progress
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // define the json body that will be sent in the post request

        JSONObject applyBody = new JSONObject();

        try {
            applyBody.put("email" , email);
            applyBody.put("job_ID" , ID);

            // start the request
            JsonObjectRequest applyRequest = new JsonObjectRequest(Request.Method.POST, applyUrl, applyBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.cancel();

                    try {
                        String  result = response.isNull("found") ? null : response.getString("found");
                        if (result.equals("true"))
                        {
                            JobDetailsActivity.applyButton.setVisibility(View.INVISIBLE);
                            JobDetailsActivity.hasAppliedTxt.setVisibility(View.VISIBLE);

                        }else {

                            JobDetailsActivity.applyButton.setVisibility(View.VISIBLE);
                            JobDetailsActivity.hasAppliedTxt.setVisibility(View.INVISIBLE);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   // Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();

                }
            });

            Volley.newRequestQueue(context).add(applyRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void postApplications(String email , String ID)
    {
        // start the loading progress
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Applying for job...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // body of request
        JSONObject body = new JSONObject();
        try {
            body.put("email" , email);
            body.put("job_ID" , ID);


            // start the request
            JsonObjectRequest applyForJobRequest = new JsonObjectRequest(Request.Method.POST, applyForJobUrl, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.cancel();

                    // remove apply option
                    JobDetailsActivity.applyButton.setVisibility(View.INVISIBLE);
                    JobDetailsActivity.hasAppliedTxt.setVisibility(View.VISIBLE);

                    View v = LayoutInflater.from(context).inflate(R.layout.apply_success_view , null , false);
                    CheckView checkView = v.findViewById(R.id.check);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   // builder.setIcon(R.drawable.ic_ticked);
                    builder.setView(v);
                    builder.setCancelable(false);
                    builder.setTitle("Applied for job successfully");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    checkView.check();


                    //builder.setTitle("")

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();

                    Toast.makeText(context, "failed, please check internet connection !", Toast.LENGTH_SHORT).show();

                }
            });

            Volley.newRequestQueue(context).add(applyForJobRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
