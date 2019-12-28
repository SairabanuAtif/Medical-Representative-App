package com.triocodes.medivision.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.triocodes.medivision.Model.DutyReportModel;
import com.triocodes.medivision.Model.MeetReportModel;
import com.triocodes.medivision.R;

import java.util.ArrayList;

/**
 * Created by admin on 05-05-16.
 */
public class MeetReportAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity mActivity;
    private ArrayList<MeetReportModel> List;
    private  static int ListPosition = 0;

    public MeetReportAdapter(Activity activity, ArrayList<MeetReportModel> List) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frame_meeting_report_layout, null);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }



        viewholder.DoctorsName=(TextView) convertView.findViewById(R.id.text_meet_report_doctors_name);
        viewholder.VisitTime=(TextView) convertView.findViewById(R.id.text_meet_report_meeting_time);
        viewholder.VisitLocation=(TextView) convertView.findViewById(R.id.text_meet_report_location);
        viewholder.Date=(TextView) convertView.findViewById(R.id.text_meet_report_meeting_date);
        viewholder.Hospital=(TextView) convertView.findViewById(R.id.text_meet_report_hospital);

        viewholder.DoctorsName.setText(List.get(position).getDoctorName());
        viewholder.VisitTime.setText(List.get(position).getTime());
        viewholder.VisitLocation.setText(List.get(position).getLocation());
        viewholder.Date.setText(List.get(position).getDate());
        viewholder.Hospital.setText(List.get(position).getHospital());

        //ListPosition = ListPosition + 1;

        return convertView;
    }
    static class ViewHolder {
        TextView DoctorsName;
        TextView VisitTime;
        TextView VisitLocation;
        TextView Hospital;
        TextView Date;

    }
}
