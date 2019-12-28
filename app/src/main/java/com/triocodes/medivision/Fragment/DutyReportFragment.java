package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.triocodes.medivision.Adapter.DutyReportAdapter;
import com.triocodes.medivision.Adapter.TaskAdapter;
import com.triocodes.medivision.Constants.Constants;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Enum.ServiceCallEnum;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.Model.DutyReportModel;
import com.triocodes.medivision.ParentFragment;
import com.triocodes.medivision.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by admin on 29-04-16.
 */
public class DutyReportFragment extends ParentFragment implements VolleyCallback,View.OnClickListener {
    HashMap<String, Object> extras;
    Handler mhandler;
    private ListView mListview;
    private Activity mActivity;
    private TextView mTvSearch;
    private EditText mEditFromDate;
    private EditText mEditToDate;
    private ArrayList<DutyReportModel> List = new ArrayList<DutyReportModel>();
    private DutyReportAdapter adapter;
    private ServiceCallEnum mServiceCallEnum;

    public DutyReportFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.duty_report_fragment_layout,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.initialize(mActivity, DutyReportFragment.this);
        extras = new HashMap<String, Object>();
        mhandler = new Handler();
        mListview = (ListView) mActivity.findViewById(R.id.listDutyReport);
        mEditFromDate = (EditText) mActivity.findViewById(R.id.edittext_duty_report_from_date);
        mEditToDate = (EditText) mActivity.findViewById(R.id.edittext_duty_report_to_date);
        mTvSearch = (TextView) mActivity.findViewById(R.id.text_duty_report_search);


        mTvSearch.setOnClickListener(this);

        mEditFromDate.setOnClickListener(this);
        mEditToDate.setOnClickListener(this);

       // passData();

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_duty_report_search:{
                if (super.isConnectingToInternet()) {
                    if (nullChecker()) {
                        passData();
                    }

                } else {
                    showError("Connectivity Issue", "No internet connection available",FragmentTransactionEnum.NONE);

                }
                break;
            }

            case R.id.edittext_duty_report_from_date:
                //Toast.makeText(mActivity,"here",Toast.LENGTH_LONG).show();
                super.setDate(R.id.edittext_duty_report_from_date);
                break;
            case R.id.edittext_duty_report_to_date:
                super.setDate(R.id.edittext_duty_report_to_date);
                break;
        }

    }


    private boolean nullChecker() {
        if (mEditFromDate.getText().toString().trim().length() == 0 || mEditFromDate.getText().toString() == "") {
            super.shakeEdittext(R.id.edittext_duty_report_from_date);
            return false;
        }
        if (mEditToDate.getText().toString().trim().length() == 0 || mEditToDate.getText().toString() == "") {
            super.shakeEdittext(R.id.edittext_duty_report_to_date);
            return false;
        }
        return true;
    }

    private void passData() {
        try{
            JSONObject mJsonObj=new JSONObject();
            mJsonObj.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mJsonObj.put("ClockInFromDate",mEditFromDate.getText().toString());
            mJsonObj.put("ClockInToDate",mEditToDate.getText().toString());
            super.showProgress("Please Wait");
            mServiceCallEnum = ServiceCallEnum.DUTYREPORT;
            super.establishConnection(Request.Method.POST, Constants.mDutyReportUrl, mJsonObj);

        }catch (Exception e){

        }
    }

    @Override
    public void volleyOnSuccess() {
        dismissAlert();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if (!extras.isEmpty()) {
                    try {
                        if (mServiceCallEnum == ServiceCallEnum.DUTYREPORT) {
                            String response = (String) extras.get("response");
                            handleDutyReport(response);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void handleDutyReport(String response) {
        List=new ArrayList<>();
        try{
            JSONObject mJsonObject=new JSONObject(response);
            String mJsonString=mJsonObject.getString("d");
            JSONArray mJsonArray=new JSONArray(mJsonString);

            for (int i=0;i<mJsonArray.length();i++){
                JSONObject mJson=mJsonArray.getJSONObject(i);
                int mDutyId = mJson.getInt("ClockID");
                String mDate=mJson.getString("ClockInDate");
                String mInLocation=mJson.getString("ClockInLocation");
                String mOutLocation=mJson.getString("ClockOutLocation");
                String inTime=mJson.getString("ClockInTime");
                String OutTime=mJson.getString("ClockOutTime");

                DutyReportModel mDutyReportModel=new DutyReportModel();
                mDutyReportModel.setDate(mDate);
                mDutyReportModel.setDutyId(mDutyId);
                mDutyReportModel.setInLocation(mInLocation);
                mDutyReportModel.setOutLocation(mOutLocation);
                mDutyReportModel.setTimeIn(inTime);
                mDutyReportModel.setTimeOut(OutTime);

                List.add(mDutyReportModel);
                if (List!=null && List.size()>0){
                    Collections.sort(List);
                    adapter = new DutyReportAdapter(mActivity, List);
                    mListview.setAdapter(adapter);
                }
            }

        }catch (Exception e){

        }
    }

    @Override
    public void volleyOnError() {
        dismissAlert();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if (!extras.isEmpty()) {
                    try {
                        String responseType = (String) extras.get(getResources().getString(R.string.string_server_response));
                        showError("Error", responseType, FragmentTransactionEnum.NONE);

                    } catch (Exception e) {

                    }

                }
            }
        });
    }

    @Override
    public HashMap<String, Object> getExtras() {
        return extras;
    }


}
