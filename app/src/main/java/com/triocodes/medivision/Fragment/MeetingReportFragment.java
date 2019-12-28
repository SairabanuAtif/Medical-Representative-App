package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.triocodes.medivision.Adapter.DutyReportAdapter;
import com.triocodes.medivision.Adapter.MeetReportAdapter;
import com.triocodes.medivision.Constants.Constants;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Enum.ServiceCallEnum;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.Model.DutyReportModel;
import com.triocodes.medivision.Model.MeetReportModel;
import com.triocodes.medivision.ParentFragment;
import com.triocodes.medivision.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Created by admin on 29-04-16.
 */
public class MeetingReportFragment extends ParentFragment implements VolleyCallback,View.OnClickListener {
    HashMap<String, Object> extras;
    android.os.Handler mhandler;
    private Activity mActivity;
    private ListView mListView;
    private EditText mEditFromDate;
    private EditText mEditToDate;
    private TextView mTextSearch;
    private ArrayList<MeetReportModel> List = new ArrayList<MeetReportModel>();
    private MeetReportAdapter adapter;
    private ServiceCallEnum mServiceCallEnum;

    public MeetingReportFragment(){

    }
    @Override
      public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.initialize(mActivity, MeetingReportFragment.this);
        extras = new HashMap<String, Object>();
        mhandler = new android.os.Handler();
        mListView = (ListView) mActivity.findViewById(R.id.listMeetReport);
        mEditFromDate = (EditText) mActivity.findViewById(R.id.edittext_meet_report_from_date);
        mEditToDate = (EditText) mActivity.findViewById(R.id.edittext_meet_report_to_date);
        mTextSearch = (TextView) mActivity.findViewById(R.id.text_meet_report_search);


        mTextSearch.setOnClickListener(this);
        mEditFromDate.setOnClickListener(this);
        mEditToDate.setOnClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meet_report_fragment_layout,null);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_meet_report_search:{
                if (super.isConnectingToInternet()) {
                    if (nullChecker()) {
                        passData();
                    }

                } else {
                    showError("Connectivity Issue", "No internet connection available",FragmentTransactionEnum.NONE);

                }
                break;
            }

            case R.id.edittext_meet_report_from_date:
                //Toast.makeText(mActivity,"here",Toast.LENGTH_LONG).show();
                super.setDate(R.id.edittext_meet_report_from_date);
                break;
            case R.id.edittext_meet_report_to_date:
                super.setDate(R.id.edittext_meet_report_to_date);
                break;
        }
    }

    private boolean nullChecker() {
        if (mEditFromDate.getText().toString().trim().length() == 0 || mEditFromDate.getText().toString() == "") {
            super.shakeEdittext(R.id.edittext_meet_report_from_date);
            return false;
        }
        if (mEditToDate.getText().toString().trim().length() == 0 || mEditToDate.getText().toString() == "") {
            super.shakeEdittext(R.id.edittext_meet_report_to_date);
            return false;
        }
        return true;
    }

    private void passData() {
        try{
            JSONObject mJsonObj=new JSONObject();
            mJsonObj.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mJsonObj.put("FromDate",mEditFromDate.getText().toString());
            mJsonObj.put("ToDate", mEditToDate.getText().toString());
            super.showProgress("Please Wait");
            mServiceCallEnum = ServiceCallEnum.MEETREPORT;
            super.establishConnection(Request.Method.POST, Constants.mMeetReportUrl, mJsonObj);

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
                        if (mServiceCallEnum == ServiceCallEnum.MEETREPORT) {
                            String response = (String) extras.get("response");
                            handleMeetReport(response);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void handleMeetReport(String response) {
        List=new ArrayList<>();
        try{
            JSONObject mJsonObject=new JSONObject(response);
            String mJsonString=mJsonObject.getString("d");
            JSONArray mJsonArray=new JSONArray(mJsonString);

            for (int i=0;i<mJsonArray.length();i++){
                JSONObject mJson=mJsonArray.getJSONObject(i);
                int mMeetId = mJson.getInt("MeetingID");
                String mDoctorsName=mJson.getString("DoctorsName");
                String mVisitTime=mJson.getString("MeetingTime");
                String mVisitHospital=mJson.getString("Hospital");
                String mLocation=mJson.getString("Location");
                String mDate=mJson.getString("MeetingDate");


                MeetReportModel mDutyReportModel=new MeetReportModel();
                mDutyReportModel.setDoctorName(mDoctorsName);
                mDutyReportModel.setMeetingId(mMeetId);
                mDutyReportModel.setLocation(mLocation);
                mDutyReportModel.setTime(mVisitTime);
                mDutyReportModel.setDate(mDate);
                mDutyReportModel.setHospital(mVisitHospital);



                List.add(mDutyReportModel);
                if (List!=null && List.size()>0){
                    Collections.sort(List);
                    adapter = new MeetReportAdapter(mActivity, List);
                    mListView.setAdapter(adapter);
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
