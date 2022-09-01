package com.example.taskmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private final String uid;
    private List<Task> tasks = new ArrayList<>();
    private onItemClickListener listener;
    private onItemCheckedListener checkedListener;

    public TaskAdapter(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.todo_complete_view.setVisibility(View.GONE);

        if (uid.equals(currentTask.getUid())) {
            holder.todo_complete_view.setVisibility(View.VISIBLE);
            holder.complete.setOnCheckedChangeListener(null);
            holder.complete.setChecked(currentTask.isComplete());
            holder.complete.setOnCheckedChangeListener((compoundButton, b) -> currentTask.setComplete(b));
        }

        holder.title.setText(currentTask.getTitle());
        holder.time.setText(currentTask.getHour() + ":" + currentTask.getMinute());
        holder.content.setText(currentTask.getContent());
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemCheckedListener(onItemCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    public interface onItemClickListener {
        void onItemClick(Task task, int position);
    }


    public interface onItemCheckedListener {
        void onItemChecked(Task task, int position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView time;
        private final TextView content;
        private final CheckBox complete;
        private final LinearLayout todo_complete_view;

        public TaskHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            content = itemView.findViewById(R.id.content);
            complete = itemView.findViewById(R.id.complete);
            todo_complete_view = itemView.findViewById(R.id.todo_complete_view);


            complete.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (checkedListener != null && position != RecyclerView.NO_POSITION) {
                    checkedListener.onItemChecked(tasks.get(position), position);
                }
            });


            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position), position);
                }
            });
        }
    }

}
