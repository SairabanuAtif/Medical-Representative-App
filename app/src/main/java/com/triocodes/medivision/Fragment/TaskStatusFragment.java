package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.triocodes.medivision.Adapter.SpinnerAdapter;
import com.triocodes.medivision.Constants.Constants;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Enum.ServiceCallEnum;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.Model.SpinnerModel;
import com.triocodes.medivision.ParentFragment;
import com.triocodes.medivision.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by admin on 02-05-16.
 */
public class TaskStatusFragment extends ParentFragment implements VolleyCallback, View.OnClickListener, AdapterView.OnItemSelectedListener {

    HashMap<String, Object> extras;
    Handler mhandler;
    private Activity mActivity;
    private Spinner mSpinnerTaskStatus;
    private TextView mTextSave;
    private ServiceCallEnum mServiceCallEnum;
    ArrayList<SpinnerModel> mSpinnerTaskStatusList;
    int strTaskId;
    String strStatus;
    int StatusId;
    String mSpinnerSelectedStatus;


    public TaskStatusFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.initialize(mActivity, TaskStatusFragment.this);
        extras = new HashMap<>();
        mhandler = new Handler();
        strTaskId = getArguments().getInt("TASKID");
        strStatus=getArguments().getString("STATUS");
        switch (strStatus){
            case "Not Started":
                StatusId=1;
                break;
            case "In Progress":
                StatusId=2;
                break;
            case "Completed":
                StatusId=3;
                break;
            case "Waiting On Someone Else":
                StatusId=4;
                break;

        }
        mTextSave = (TextView) mActivity.findViewById(R.id.text_task_status_save);
        mSpinnerTaskStatus = (Spinner) mActivity.findViewById(R.id.spinner_status);
        mTextSave.setOnClickListener(this);
        mSpinnerTaskStatus.setOnItemSelectedListener(this);
       // Toast.makeText(mActivity, strStatus , Toast.LENGTH_LONG).show();
        passData();
    }

    private void passData() {
        try {
            //JSONObject mJsonObj = new JSONObject();
           // mJsonObj.put("TaskId", strTaskId);
          //  mJsonObj.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mServiceCallEnum = ServiceCallEnum.TASKSTATUSSPINNER;
            showProgress("Please Wait");
            super.establishConnection(Request.Method.POST, Constants.mSpinnerStatusUrl, new JSONObject());
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_status_fragment_layout, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_task_status_save:
                if (super.isConnectingToInternet()) {
                    passDataForStatusSave();
                } else {
                    showError("Connectivity Issue", "No internet connection available", FragmentTransactionEnum.NONE);
                }
                break;
        }

    }

    private void passDataForStatusSave() {
        try {
            JSONObject mJsonObj = new JSONObject();
            mJsonObj.put("Status", mSpinnerSelectedStatus);
            mJsonObj.put("TaskID", strTaskId);
            mServiceCallEnum = ServiceCallEnum.TASKSTATUSSAVE;
            super.showProgress("Please Wait");
            super.establishConnection(Request.Method.POST, Constants.mTaskStatusSaveUrl, mJsonObj);
        } catch (Exception e) {

        }

    }

    @Override
    public void volleyOnSuccess() {
        dismissAlert();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if (!extras.isEmpty()) {
                    String responseType = (String) extras.get("response");
                    if (mServiceCallEnum == ServiceCallEnum.TASKSTATUSSPINNER) {
                        setmSpinnerTaskStatus(responseType,StatusId);
                    }
                    if (mServiceCallEnum == ServiceCallEnum.TASKSTATUSSAVE) {
                        handlemStatusSave(responseType);
                    }

                }

            }
        });
    }

    private void handlemStatusSave(String responseType) {
        try {
            JSONObject mServerResponse = new JSONObject(responseType);
            // .net webservice automatically ads 'd' in the response json for security purpose
            String mMessageObject = mServerResponse.getString("d");
            JSONArray objJsonAry = new JSONArray(mMessageObject);
            JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
            String mMessage = json.getString("status");
            //String LocationAddress = json.getString("Location");

            if (mMessage.equalsIgnoreCase("true")) {
                showSuccess("Success", "Status Saved Successfully", FragmentTransactionEnum.TASK);
                // Toast.makeText(mActivity, "Status Saved", Toast.LENGTH_SHORT).show();

            } else {
                showMessage("Sorry", "Something went wrong");
            }
        } catch (Exception e) {

        }
    }

    private void setmSpinnerTaskStatus(String responseType,int mStatusId) {
        mStatusId=mStatusId-1;
        mSpinnerTaskStatusList = new ArrayList<>();
        try {
            JSONObject mRequestbObject = new JSONObject(responseType);
            String mJobDetails = mRequestbObject.getString("d");
            JSONArray mJobArray = new JSONArray(mJobDetails);

            for (int i = 0; i < mJobArray.length(); i++) {
                JSONObject json_obj = mJobArray.getJSONObject(i);
                int mId = json_obj.getInt("TaskStatusId");
                String mName = json_obj.getString("TaskStatus");

                SpinnerModel mSpinnerModel = new SpinnerModel();
                mSpinnerModel.setId(mId);
                mSpinnerModel.setText(mName);

                mSpinnerTaskStatusList.add(mSpinnerModel);

            }
        } catch (Exception e) {

        }
        Collections.sort(mSpinnerTaskStatusList);
        SpinnerAdapter adapterLevel = new SpinnerAdapter(mActivity, mSpinnerTaskStatusList);
        mSpinnerTaskStatus.setAdapter(adapterLevel);
        mSpinnerTaskStatus.setSelection(mStatusId);
    }

    @Override
    public void volleyOnError() {
        dismissAlert();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if (!extras.isEmpty()) {
                    try {
                        String responseType = (String) extras.get("response");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spinner_status: {
                mSpinnerSelectedStatus = mSpinnerTaskStatusList.get(mSpinnerTaskStatus.getSelectedItemPosition()).getText();
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
