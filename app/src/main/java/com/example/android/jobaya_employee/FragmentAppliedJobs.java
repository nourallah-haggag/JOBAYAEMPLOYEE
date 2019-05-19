package com.example.android.jobaya_employee;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentAppliedJobs extends Fragment {

    // define the list and adapter for the recycler view as static to be accessed from the jobayaApi class
    static List<Job> appliedJobsLst;
    static AdapterAllJobs adapterAppliedJobs;

    //define static views for error handling
    static ImageView errorImage;
    static TextView errorText;
    static MaterialButton errorButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init the list and the adapter
        appliedJobsLst = new ArrayList<>();
        adapterAppliedJobs = new AdapterAllJobs(getContext() , appliedJobsLst , false);
        Collections.reverse(appliedJobsLst);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appledjobs_layout , container , false);

        // set the list components
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_applied);
        recyclerView.setAdapter(adapterAppliedJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // execute the volley request to get applied jobs
        JobayaApi jobayaApi = new JobayaApi(getContext());
        jobayaApi.getAppliedJobs();

        // define the error handling views
        errorImage = v.findViewById(R.id.error_image_applied_jobs);
        errorText = v.findViewById(R.id.error_txt_applied_jobs);
        errorButton = v.findViewById(R.id.error_button_applied_jobs);
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().replace(R.id.main_frame , new FragmentAppliedJobs()).commit();
            }
        });
        return v;
    }
}
