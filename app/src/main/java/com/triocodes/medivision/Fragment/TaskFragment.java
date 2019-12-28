package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.triocodes.medivision.Adapter.TaskAdapter;
import com.triocodes.medivision.Constants.Constants;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Enum.ServiceCallEnum;
import com.triocodes.medivision.Interface.FragmentListenerCallback;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.Model.TaskModel;
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
public class TaskFragment extends ParentFragment implements VolleyCallback ,View.OnClickListener,AdapterView.OnItemClickListener,TaskAdapter.customButtonListener {
    private Activity mActivity;
    HashMap<String, Object> extras;
    Handler mhandler;
    private ListView mListJob;
    private ArrayList<TaskModel> List = new ArrayList<TaskModel>();
    private TaskAdapter adapter;
    private ServiceCallEnum mServiceCallEnum;
    int mUnreadCount = 0;

    public TaskFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.initialize(mActivity, TaskFragment.this);
        extras = new HashMap<String, Object>();
        mhandler = new Handler();
        mListJob = (ListView) mActivity.findViewById(R.id.listTask);
        mListJob.setOnItemClickListener(this);
        if (isConnectingToInternet()) {
            passData();

        } else {
            showError("Connectivity Issue", "No internet connection available", FragmentTransactionEnum.NONE);
        }
    }


    public void passData() {
        JSONObject mJobDetails = new JSONObject();
        try {
            mJobDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mJobDetails.put("TaskID", 0);
            showProgress("Please Wait");
            mServiceCallEnum = ServiceCallEnum.TASK;
            super.establishConnection(Request.Method.POST, Constants.mTaskUrl, mJobDetails);
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment_layout,null);
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
    public void volleyOnSuccess() {
        dismissAlert();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if (!extras.isEmpty()) {
                    try {
                        if (mServiceCallEnum == ServiceCallEnum.TASK) {
                            String response = (String) extras.get("response");
                            handleTask(response);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    public void handleTask(String data) {
        List = new ArrayList<>();
        try {
            JSONObject mJobObject = new JSONObject(data);
            String mJobDetails = mJobObject.getString("d");
            JSONArray mJobArray = new JSONArray(mJobDetails);

            for (int i = 0; i < mJobArray.length(); i++) {
                JSONObject json_obj = mJobArray.getJSONObject(i);
                int mTaskId = json_obj.getInt("TaskId");

                String mTask = json_obj.getString("TaskName");
               // String mCategory = json_obj.getString("RequirementDesc");
                String mStatus = json_obj.getString("Status");
                String mDueDate = json_obj.getString("DueDate");
                String mAssignedBy = json_obj.getString("AssignedTo");
                String mTaskStatus = json_obj.getString("Status");

                TaskModel mJobModel = new TaskModel();
                mJobModel.setTaskId(mTaskId);
                mJobModel.setTask(mTask);
                mJobModel.setStatus(mStatus);
              //  mJobModel.setPriority(mPriority);
                mJobModel.setDuedate(mDueDate);
                mJobModel.setAssignedby(mAssignedBy);
                mJobModel.setTaskstatus(mTaskStatus);

                List.add(mJobModel);
            }
        } catch (Exception e) {

        }
        if (List != null && List.size() > 0) {
            Collections.sort(List);
            adapter = new TaskAdapter(mActivity, List);
            mListJob.setAdapter(adapter);
            adapter.setCustomButtonListner(TaskFragment.this);

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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onEditClickListener(int position) {
       // toFragment(FragmentTransactionEnum.TASKSTATUS);
        int TaskId=List.get(position).getTaskId();
        String Status=List.get(position).getStatus();
        //Toast.makeText(mActivity,Leadid+"",Toast.LENGTH_SHORT).show();
        Fragment mTaskStatusFragment = new TaskStatusFragment();
        Bundle args = new Bundle();
        args.putInt("TASKID", TaskId);
        args.putString("STATUS",Status);
        mTaskStatusFragment.setArguments(args);
        if (mTaskStatusFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
            fragmentTransaction.replace(R.id.container, mTaskStatusFragment);
            fragmentTransaction.commit();
        }
    }
}
