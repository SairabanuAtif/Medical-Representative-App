package com.triocodes.medivision.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.triocodes.medivision.Model.SpinnerModel;
import com.triocodes.medivision.R;

import java.util.ArrayList;

/**
 * Created by Atholi on 11/16/2015.
 */
public class SpinnerAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList<SpinnerModel> List;
    private static LayoutInflater inflater = null;

    public SpinnerAdapter(Activity activity, ArrayList<SpinnerModel> List) {

        this.mActivity = activity;
        this.List = List;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewholder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frame_spinner, null);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.txtName = (TextView) convertView
                .findViewById(R.id.txt_item);


        viewholder.txtName.setText(List.get(position).getText());



        return convertView;
    }

    static class ViewHolder {

        TextView txtName;

    }

}
