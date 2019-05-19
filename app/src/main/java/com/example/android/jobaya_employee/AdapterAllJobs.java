package com.example.android.jobaya_employee;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllJobs extends RecyclerView.Adapter<AdapterAllJobs.JobHolder> {
    Context context;
    List<Job> jobsList;
    boolean showApply;

    public void filterList(ArrayList<Job> filterdNames) {
        this.jobsList = filterdNames;
        notifyDataSetChanged();
    }


    public AdapterAllJobs(Context context , List<Job> jobsList , boolean showApply)
    {
        this.context = context;
        this.jobsList = jobsList;
        this.showApply = showApply;
    }
    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_alljobs , viewGroup , false);
        JobHolder holder = new JobHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JobHolder jobHolder, int i) {
        final Job job = jobsList.get(i);
        jobHolder.title.setText(job.title);
        jobHolder.cat.setText(job.category);
        jobHolder.duration.setText(job.duration);
        if(showApply)
        {
            jobHolder.applyBtn.setText("APPLY");
        }
        else {
            jobHolder.applyBtn.setText("DETAILS");

        }
        jobHolder.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , JobDetailsActivity.class);
                intent.putExtra("job" , job.title);
                intent.putExtra("desc" , job.description);
                intent.putExtra("duration" , job.duration);
                intent.putExtra("cat" , job.category);
                intent.putExtra("ID" , job.ID);
                intent.putExtra("gender" , job.gender);
                intent.putExtra("skills" , job.skills);
                intent.putExtra("exp" , job.exp);
                intent.putExtra("lang" , job.lang);
                intent.putExtra("empMail" , job.empMail);

                context.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    class JobHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView cat;
        TextView duration;
        MaterialButton applyBtn;

        public JobHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.job_title);
            cat = itemView.findViewById(R.id.job_cat);
            duration = itemView.findViewById(R.id.job_duration);
            applyBtn = itemView.findViewById(R.id.apply_button);
        }
    }
}
