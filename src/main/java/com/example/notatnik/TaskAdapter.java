package com.example.notatnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends BaseAdapter {

    private Context mContext;
    private List<Task> mTaskList;
    private LayoutInflater mInflater;

    public TaskAdapter(Context mContext, List<Task> mTaskList) {
        this.mContext = mContext;
        this.mTaskList = mTaskList;
        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.single_task, null);
        TextView taskName = (TextView) v.findViewById(R.id.taskText);
        CheckBox taskCheck = (CheckBox) v.findViewById(R.id.taskCheckbox);

        taskName.setText(String.format("%s", mTaskList.get(position).getTaskText()));
        taskCheck.setChecked(mTaskList.get(position).isChecked());

        v.setTag(mTaskList.get(position).getId());
        return v;
    }
}

