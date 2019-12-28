package com.triocodes.medivision.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triocodes.medivision.Model.DutyReportModel;
import com.triocodes.medivision.Model.JourneyModel;
import com.triocodes.medivision.R;

import java.util.ArrayList;

/**
 * Created by admin on 05-05-16.
 */
public class JourneyReportAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity mActivity;
    private ArrayList<JourneyModel> List;
    private  static int ListPosition = 0;

    public JourneyReportAdapter(Activity activity, ArrayList<JourneyModel> List) {
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
            convertView = inflater.inflate(R.layout.frame_journey_report_layout, null);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }



        viewholder.TextDate=(TextView) convertView.findViewById(R.id.text_journey_report_date);
        viewholder.TextLocation=(TextView) convertView.findViewById(R.id.text_journey_report_location);
        viewholder.TextTime=(TextView) convertView.findViewById(R.id.text_journey_report_time);
        viewholder.TextJourney=(TextView) convertView.findViewById(R.id.text_journey_report_journey);




        viewholder.TextDate.setText(List.get(position).getDate());
        viewholder.TextLocation.setText(List.get(position).getLocation());
        viewholder.TextTime.setText(List.get(position).getTime());
        viewholder.TextJourney.setText(List.get(position).getJourney());

        //ListPosition = ListPosition + 1;

        return convertView;
    }
    static class ViewHolder {
        TextView TextDate;
        TextView TextLocation;
        TextView TextTime;
        TextView TextJourney;
    }
}
