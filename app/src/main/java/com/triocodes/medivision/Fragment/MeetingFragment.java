package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
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
import com.triocodes.medivision.common.AppLocationService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by admin on 29-04-16.
 */
public class MeetingFragment extends ParentFragment implements VolleyCallback,View.OnClickListener,AdapterView.OnItemSelectedListener {
    Spinner mSpinnerDoctors;
    ArrayList<SpinnerModel> mSpinnerDoctorsList;
    TextView mTextVisit;
    Activity mActivity;
    HashMap<String, Object> extras;
    Handler mhandler;
    private Boolean flag = false;
    Double MyLat = 0.00;
    Double MyLong = 0.00;
    public String LocationAddress = "";
    private ServiceCallEnum mServicecallEnum;
    private static int ClockInBit = 0;
    long networkTS;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    String txtmLatitude,txtmLongitude,txtmLocationAddress;
    String formatted = "06/01/2016 00:00:00";
    AppLocationService appLocationService;
    int mSpinnerSelectedDoctorsId;

    public MeetingFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.initialize(mActivity, MeetingFragment.this);
        extras = new HashMap<String, Object>();
        mhandler = new Handler();
        mActivity.setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        mSpinnerDoctors=(Spinner)mActivity.findViewById(R.id.spinner_meeting_doctors);
        mSpinnerDoctors.setOnItemSelectedListener(this);
        mTextVisit=(TextView)mActivity.findViewById(R.id.text_meeting_visit);
        mTextVisit.setOnClickListener(this);
        passDataForSpinnerDoctors();
        locationManager = (LocationManager)
                mActivity.getSystemService(Context.LOCATION_SERVICE);

        appLocationService = new AppLocationService(mActivity);


    }

    private void passDataForSpinnerDoctors() {
        JSONObject mJobDetails = new JSONObject();
        try {
            mJobDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            showProgress("Please Wait");
             mServicecallEnum = ServiceCallEnum.DOCTORSSPINNER;
            super.establishConnection(Request.Method.POST, Constants.mDoctorsListSpinnerUrl, mJobDetails);
        } catch (Exception e) {

        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meeting_fragment_layout,null);
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
                try {
                    String responseType = (String) extras.get(getResources().getString(R.string.string_server_response));
                    if (mServicecallEnum == ServiceCallEnum.DOCTORSVISIT) {

                        JSONObject mServerResponse = new JSONObject(responseType);
                        // .net webservice automatically ads 'd' in the response json for security purpose
                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        //String LocationAddress = json.getString("Location");

                        if (mMessage.equalsIgnoreCase("true")) {
                            showSuccess("Success", "Doctors Visit Acknowledged", FragmentTransactionEnum.HOME);
                           // Toast.makeText(mActivity, "Doctors Visit Acknowledged", Toast.LENGTH_SHORT).show();

                        } else {
                            showMessage("Sorry", "Something went wrong");
                        }

                    }
                    if (mServicecallEnum == ServiceCallEnum.DOCTORSSPINNER) {
                        setmSpinnerDoctorsList(responseType);
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();//--------------------------
                }
            }
        });

    }

    private void setmSpinnerDoctorsList(String responseType) {

        mSpinnerDoctorsList = new ArrayList<>();
        try {
            JSONObject mRequestbObject = new JSONObject(responseType);
            String mJobDetails = mRequestbObject.getString("d");
            JSONArray mJobArray = new JSONArray(mJobDetails);

            for (int i = 0; i < mJobArray.length(); i++) {
                JSONObject json_obj = mJobArray.getJSONObject(i);
                int mId = json_obj.getInt("DoctorsId");
                String mName = json_obj.getString("DoctorsName");

                SpinnerModel mSpinnerModel = new SpinnerModel();
                mSpinnerModel.setId(mId);
                mSpinnerModel.setText(mName);

                mSpinnerDoctorsList.add(mSpinnerModel);

            }
        } catch (Exception e) {

        }
        Collections.sort(mSpinnerDoctorsList);
        SpinnerAdapter adapterLevel = new SpinnerAdapter(mActivity, mSpinnerDoctorsList);
        mSpinnerDoctors.setAdapter(adapterLevel);

    }


    @Override
    public void volleyOnError() {
        dismissAlert();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if(!extras.isEmpty()){
                    try{
                        String responseType=(String)extras.get("response");
                        showError("Error",responseType,FragmentTransactionEnum.NONE);
                    }catch (Exception e){

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
        switch (v.getId()) {
            case R.id.text_meeting_visit: {

                if (super.isConnectingToInternet()) {

                    flag = displayGpsStatus();
                    if (flag) {
                        getMyCurrentTimeFirst();
                        getMyCurrentTime();
                        MarkClockIn();
                    } else {
                        alertbox("Gps Status!!", "Your GPS is: OFF");
                    }
                    break;
                } else {
                    showError("Connectivity Issue", "No internet connection available", FragmentTransactionEnum.NONE);
                }
            }
        }
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = mActivity.getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    private void alertbox(String title, String myMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void getMyCurrentLocation() {

        Location gpsLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

        if (gpsLocation != null) {
            MyLat = gpsLocation.getLatitude();
            MyLong = gpsLocation.getLongitude();
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(networkTS);
        formatted = format.format(date);
        //Toast.makeText(mActivity, "" + formatted, Toast.LENGTH_LONG).show();//-------------------------
        txtmLatitude=(MyLat.toString());
        txtmLongitude=(MyLong.toString());

    }

    void getMyCurrentLocationFirst() {
        Location gpsLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (gpsLocation != null) {
        }

    }
    void getMyCurrentTime() {
        networkTS = appLocationService.getTime(LocationManager.NETWORK_PROVIDER);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(networkTS);
        formatted = format.format(date);

        getMyCurrentLocation();
    }

    void getMyCurrentTimeFirst() {
        networkTS = appLocationService.getTime(LocationManager.NETWORK_PROVIDER);
        getMyCurrentLocationFirst();
    }

    private void MarkClockIn() {

        JSONObject mClockinDetails = new JSONObject();
        try {
            if (txtmLongitude == "0.0") {
                txtmLongitude="-18.5333";
            }
            if (txtmLatitude== "0.0") {
                txtmLatitude="65.9667";
            }
            mClockinDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mClockinDetails.put("ClockTime", formatted);
            mClockinDetails.put("DoctorID",mSpinnerSelectedDoctorsId);
            mClockinDetails.put("Longitude", txtmLongitude);
            mClockinDetails.put("Latitude", txtmLatitude);
            mClockinDetails.put("Direction", 1);
//mSpinnerSelectedDoctorsId
            //Toast.makeText(mActivity,formatted,Toast.LENGTH_LONG).show();

            // Toast.makeText(mActivity,"long: "+txtmLongitude.getText().toString()+" lat: "+txtmLatitude.getText().toString(),Toast.LENGTH_LONG).show();
            // showProgress("Please Wait");
            mServicecallEnum = ServiceCallEnum.DOCTORSVISIT;
            super.establishConnection(Request.Method.POST, Constants.mDoctorsVisitUrl, mClockinDetails);
        } catch (Exception e) {
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spinner_meeting_doctors: {
                mSpinnerSelectedDoctorsId=mSpinnerDoctorsList.get(mSpinnerDoctors.getSelectedItemPosition()).getId();
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
