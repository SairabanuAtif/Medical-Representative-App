package com.triocodes.medivision.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triocodes.medivision.Model.TaskModel;
import com.triocodes.medivision.R;

import java.util.ArrayList;

/**
 * Created by admin on 02-05-16.
 */
public class TaskAdapter extends BaseAdapter {
    customButtonListener customListner;

    public interface customButtonListener {
        public void onEditClickListener(int position);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private static LayoutInflater inflater = null;
    private Activity mActivity;
    private ArrayList<TaskModel> List;

    public TaskAdapter(Activity activity, ArrayList<TaskModel> List) {
        this.mActivity = activity;
        this.List = List;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frame_task_fragment, null);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.txtTask = (TextView) convertView
                .findViewById(R.id.text_task_task);

       // viewholder.txtPriority = (TextView) convertView.findViewById(R.id.text_task_priority);
       // viewholder.txtCategory=(TextView)convertView.findViewById(R.id.text_task_category);
        viewholder.txtDueDate = (TextView) convertView.findViewById(R.id.text_task_due_date);
        viewholder.txtAssignedBy=(TextView)convertView.findViewById(R.id.text_task_assigned_by);
        viewholder.txtTaskStatus=(TextView)convertView.findViewById(R.id.text_task_task_status);
        viewholder.txtEdit=(TextView)convertView.findViewById(R.id.text_task_edit);

       // viewholder.txtPriority.setText(List.get(position).getPriority());
        viewholder.txtTask.setText(List.get(position).getTask());
        viewholder.txtDueDate.setText(List.get(position).getDuedate());
        viewholder.txtAssignedBy.setText(List.get(position).getAssignedby());
        viewholder.txtTaskStatus.setText(List.get(position).getTaskstatus());
       // viewholder.txtCategory.setText(List.get(position).getCategory());

        viewholder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onEditClickListener(position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView txtTask;
       // TextView txtPriority;
        //TextView txtStatus;
        TextView txtDueDate;
        TextView txtAssignedBy;
        TextView txtTaskStatus;
        TextView txtEdit;

    }
}
