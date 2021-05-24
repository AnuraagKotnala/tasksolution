package com.example.tasksolution;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class PersonLeaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {
    private Activity mActivity;
    private ArrayList<String> dateList;
    private ArrayList<String> reasonList;

    public PersonLeaveAdapter(Activity activity, ArrayList<String>  list1, ArrayList<String>  list2) {
        mActivity = activity;
        dateList = list1;
        reasonList = list2;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        return new MyViewHolder(layoutInflater.inflate(R.layout.person_leave_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder  holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        String dateValue = dateList.get(position);
        String reasonValue = reasonList.get(position);

        viewHolder.date.setText(dateValue);
        viewHolder.reason.setText(reasonValue);

    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView reason;

        public MyViewHolder(View view) {
            super(view);
            date=view.findViewById(R.id.date);
            reason=view.findViewById(R.id.reason);
        }
    }

}
