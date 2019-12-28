package com.triocodes.medivision.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triocodes.medivision.Model.DutyReportModel;
import com.triocodes.medivision.R;

import java.util.ArrayList;

/**
 * Created by admin on 03-05-16.
 */
public class DutyReportAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity mActivity;
    private ArrayList<DutyReportModel> List;
    private  static int ListPosition = 0;

    public DutyReportAdapter(Activity activity, ArrayList<DutyReportModel> List) {
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
            convertView = inflater.inflate(R.layout.frame_duty_report_layout, null);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }



        viewholder.TextDate=(TextView) convertView.findViewById(R.id.text_duty_report_date);
        viewholder.TextInLocation=(TextView) convertView.findViewById(R.id.text_duty_report_location_in);
        viewholder.TextOutLocation=(TextView) convertView.findViewById(R.id.text_duty_report_location_out);
        viewholder.TextTimeIn=(TextView) convertView.findViewById(R.id.text_duty_report_in_time);
        viewholder.TextTimeOut=(TextView) convertView.findViewById(R.id.text_duty_report_out_time);



        viewholder.TextDate.setText(List.get(position).getDate());
        viewholder.TextInLocation.setText(List.get(position).getInLocation());
        viewholder.TextOutLocation.setText(List.get(position).getOutLocation());
        viewholder.TextTimeIn.setText(List.get(position).getTimeIn());
        viewholder.TextTimeOut.setText(List.get(position).getTimeOut());
        //ListPosition = ListPosition + 1;

        return convertView;
    }
    static class ViewHolder {
        TextView TextDate;
        TextView TextInLocation;
        TextView TextOutLocation;
        TextView TextTimeIn;
        TextView TextTimeOut;
    }
}
