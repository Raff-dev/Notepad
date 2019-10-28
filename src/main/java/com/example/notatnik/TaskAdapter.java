package com.example.notatnik;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> taskArrayList;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        CheckBox taskCheckBox;
        MyDBHandler dbHandler;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.taskName);
            taskCheckBox = itemView.findViewById(R.id.taskCheckbox);
            dbHandler = new MyDBHandler(itemView.getContext(), null, null, 1);
        }

    }

    public TaskAdapter(ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task, parent, false);
        TaskViewHolder tvh = new TaskViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {
        Task task = taskArrayList.get(position);
        final int iddt = task.getDocumentId();
        final int idt = task.getId();

        holder.taskName.setText(task.getTaskName());
        holder.taskCheckBox.setChecked(holder.dbHandler.getTaskCheckValue(iddt, idt));
        if(holder.taskCheckBox.isChecked()){
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.dbHandler.updateTaskChecked(iddt, idt, !taskArrayList.get(position).isChecked());
                if(holder.taskCheckBox.isChecked()){
                    holder.taskName.setPaintFlags(holder.taskName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    holder.taskName.setPaintFlags(holder.taskName.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }


}
