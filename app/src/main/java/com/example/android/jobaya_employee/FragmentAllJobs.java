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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.widget.SearchBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.MultipleToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class FragmentAllJobs extends Fragment {
    static AdapterAllJobs adapterAllJobs;
    static  List<Job> jobsList;
    static ImageView errorImage;
    static TextView errorText;
    static MaterialButton errorButton;
    static TextView showingSearchFor;

    static ToggleSwitch multipleToggleSwitch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // define static objects updated by volley requests
        jobsList =new ArrayList<>();
        adapterAllJobs = new AdapterAllJobs(getContext() , jobsList , true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alljobs_layout , container , false);

        // define list components
        final RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapterAllJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // request all jobs
        final JobayaApi jobayaApi = new JobayaApi(getContext());
        jobayaApi.getAllJobs();

        showingSearchFor = v.findViewById(R.id.showing_results_for_txt);



        // define the image view
        errorImage = v.findViewById(R.id.error_image_all_jobs);
        errorImage.setVisibility(View.INVISIBLE);

        // define the error text
        errorText = v.findViewById(R.id.error_text_all_jobs);
        errorText.setVisibility(View.INVISIBLE);

        // define the error button
        errorButton = v.findViewById(R.id.error_button_all_jobs);
        errorButton.setVisibility(View.INVISIBLE);
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().replace(R.id.main_frame , new FragmentAllJobs()).commit();
            }
        });

        // define the toggle
        multipleToggleSwitch = v.findViewById(R.id.category_toggle);
        multipleToggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position == 0) // get all jobs
                {
                    // set the list to view all the jobs
                    filter("*");
                   // Toast.makeText(getContext(), "all", Toast.LENGTH_SHORT).show();

                }else if(position == 1) // get part time jobs
                {

                   filter("art");
                 //   Toast.makeText(getContext(), "part", Toast.LENGTH_SHORT).show();

                }else {  // get ushering jobs

                    filter("sher");

                    //Toast.makeText(getContext(), "usher", Toast.LENGTH_SHORT).show();

                }
            }
        });




        return v;
    }

    // search filter
    private void filter(String text) {


        //new array list that will hold the filtered data
        ArrayList<Job> filterdNames = new ArrayList<>();
        if(text.equals("*"))
        {
            adapterAllJobs.filterList((ArrayList)jobsList);
        }
        else {

            //looping through existing elements
            for (Job s : jobsList) {
                //if the existing elements contains the search input
                if (s.category.toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                }
            }

            //calling a method of the adapter class and passing the filtered list
            adapterAllJobs.filterList(filterdNames);
        }
    }









}
