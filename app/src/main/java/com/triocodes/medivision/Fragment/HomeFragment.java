package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.triocodes.medivision.Constants.Constants;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Enum.ServiceCallEnum;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.ParentFragment;
import com.triocodes.medivision.R;
import com.triocodes.medivision.common.AppLocationService;
import com.triocodes.medivision.common.Locations;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by admin on 29-04-16.
 */
public class HomeFragment extends ParentFragment implements VolleyCallback,View.OnClickListener {
    ImageView mImgOnDuty,mImgOffDuty,mImgJourney,mImgDestination,mImgMeeting,
            mImgMeetReport,mImgDutyReport,mImgTask,mImgReminder,mImgTarget,mImgJourneyReport
            ,mImgOnDutyClicked,mImgOffDutyClicked,mImgJorneyClicked,mImgDestinationClicked,mImgMeetingClicked;
    private Activity mActivity;
    HashMap<String, Object> extras;
    Handler mhandler;
    private static final String TAG = "Debug";
    private Boolean flag = false;
    Double MyLat = 0.00;
    Double MyLong = 0.00;
    public String LocationAddress = "";
    private ServiceCallEnum mServicecallEnum;
    private static int ClockInBit = 0;
    private static int JourneyBit = 0;
    long networkTS;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    Locations objLoc = new Locations();

    String txtmLatitude,txtmLongitude,txtmLocationAddress;

    String formatted = "06/01/2016 00:00:00";

    AppLocationService appLocationService;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity)getActivity()).getSupportActionBar().show();
        super.initialize(mActivity, HomeFragment.this);
        extras = new HashMap<String, Object>();
        mhandler = new Handler();
        mActivity.setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        mImgOnDuty=(ImageView)mActivity.findViewById(R.id.home_on_duty);
        mImgOffDuty=(ImageView)mActivity.findViewById(R.id.home_off_duty);
        mImgJourney=(ImageView)mActivity.findViewById(R.id.home_journey);
        mImgDestination=(ImageView)mActivity.findViewById(R.id.home_destination);
        mImgDutyReport=(ImageView)mActivity.findViewById(R.id.home_duty_report);
        mImgMeetReport=(ImageView)mActivity.findViewById(R.id.home_meeting_report);
        mImgMeeting=(ImageView)mActivity.findViewById(R.id.home_meeting);
        mImgJourneyReport=(ImageView)mActivity.findViewById(R.id.home_journey_report);
       // mImgReminder=(ImageView)mActivity.findViewById(R.id.home_reminder);
       // mImgTarget=(ImageView)mActivity.findViewById(R.id.home_target);
        mImgTask=(ImageView)mActivity.findViewById(R.id.home_task);

        mImgOnDutyClicked=(ImageView)mActivity.findViewById(R.id.home_on_duty_clicked);
        mImgOnDutyClicked.setVisibility(View.GONE);
        mImgOffDutyClicked=(ImageView)mActivity.findViewById(R.id.home_off_duty_clicked);
        mImgOffDutyClicked.setVisibility(View.GONE);
        mImgJorneyClicked=(ImageView)mActivity.findViewById(R.id.home_journey_clicked);
        mImgJorneyClicked.setVisibility(View.GONE);
        mImgDestinationClicked=(ImageView)mActivity.findViewById(R.id.home_destination_clicked);
        mImgDestinationClicked.setVisibility(View.GONE);
        mImgMeetingClicked=(ImageView)mActivity.findViewById(R.id.home_meeting_clicked);
        mImgMeetingClicked.setVisibility(View.GONE);

       Check_ClockIn();
        //Check_Journey();
       /* if(ClockInBit==1){
            Check_Journey();
        }else{
            mImgJourney.setEnabled(false);
            mImgDestination.setEnabled(false);
        }*/


        mImgOnDuty.setOnClickListener(this);
        mImgOffDuty.setOnClickListener(this);
        mImgJourney.setOnClickListener(this);
        mImgDestination.setOnClickListener(this);
        mImgDutyReport.setOnClickListener(this);
        mImgMeetReport.setOnClickListener(this);
        mImgMeeting.setOnClickListener(this);
       // mImgReminder.setOnClickListener(this);
      //  mImgTarget.setOnClickListener(this);
        mImgTask.setOnClickListener(this);
        mImgJourneyReport.setOnClickListener(this);

        locationManager = (LocationManager)
                mActivity.getSystemService(Context.LOCATION_SERVICE);

        appLocationService = new AppLocationService(mActivity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    void Check_ClockIn() {
        mServicecallEnum = ServiceCallEnum.ONDUTYCHECK;
        JSONObject mClockinCheckDetails = new JSONObject();
        try {
            mClockinCheckDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            //showProgress("Please Wait");
            super.establishConnection(Request.Method.POST, Constants.mCheckClockInUrl, mClockinCheckDetails);
        } catch (Exception e) {

        }
    }
    void Check_Journey() {
        mServicecallEnum = ServiceCallEnum.JOURNEYCHECK;
        JSONObject mClockinCheckDetails = new JSONObject();
        try {
            mClockinCheckDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            //showProgress("Please Wait");
            super.establishConnection(Request.Method.POST, Constants.mCheckJourneyUrl, mClockinCheckDetails);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_on_duty: {

                if (super.isConnectingToInternet()) {

                    flag = displayGpsStatus();
                    if (flag) {

                        MyLat = 0.00;
                        MyLong = 0.00;
                        getMyCurrentTimeFirst();
                        getMyCurrentTime();

                        if (ClockInBit == 0) {

                            MarkClockIn();
                        }

                    } else {
                        alertbox("Gps Status!!", "Your GPS is: OFF");
                    }
                    break;
                }
                else{
                    showError("Connectivity Issue", "No internet connection available", FragmentTransactionEnum.NONE);
                }
                break;
            }
            case R.id.home_off_duty:{
                if(super.isConnectingToInternet()){
                    flag=displayGpsStatus();
                    if (flag){
                        MyLat=0.00;
                        MyLong=0.00;
                        getMyCurrentTimeFirst();
                        getMyCurrentTime();
                        if(ClockInBit==1) {

                            MarkClockOut();
                        }

                    }else{
                        alertbox("Gps Status!!", "Your GPS is: OFF");
                    }
                    break;
                }
                else{
                    showError("Connectivity Issue", "No internet connection available", FragmentTransactionEnum.NONE);
                }
                break;
            }
            case R.id.home_journey: {
                if (super.isConnectingToInternet()) {

                    flag = displayGpsStatus();
                    if (flag) {

                        MyLat = 0.00;
                        MyLong = 0.00;
                        getMyCurrentTimeFirst();
                        getMyCurrentTime();

                            MarkJourney();


                    } else {
                        alertbox("Gps Status!!", "Your GPS is: OFF");
                    }

                }
                break;
            }

            case R.id.home_destination: {
                if (super.isConnectingToInternet()) {

                    flag = displayGpsStatus();
                    if (flag) {

                        MyLat = 0.00;
                        MyLong = 0.00;
                        getMyCurrentTimeFirst();
                        getMyCurrentTime();

                            MarkDestination();


                    } else {
                        alertbox("Gps Status!!", "Your GPS is: OFF");
                    }

                }
                break;
            }
            case R.id.home_meeting:{
                toFragment(FragmentTransactionEnum.MEETING);
                break;

            }
            case R.id.home_task:{
                toFragment(FragmentTransactionEnum.TASK);
                break;
            }
            case R.id.home_journey_report:{
                toFragment(FragmentTransactionEnum.JOURNEYREPORT);
                break;
            }

            case R.id.home_duty_report:{
                toFragment(FragmentTransactionEnum.DUTYREPORT);
                break;
            }
            case R.id.home_meeting_report:{
                toFragment(FragmentTransactionEnum.MEETINGREPORT);
                break;
            }
        }
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
            mClockinDetails.put("ClockInLocation", "");
            mClockinDetails.put("ClockInLong", txtmLongitude);
            mClockinDetails.put("ClockInLat", txtmLatitude);
            mClockinDetails.put("ClockInTime", formatted);

            //Toast.makeText(mActivity,formatted,Toast.LENGTH_LONG).show();

            // Toast.makeText(mActivity,"long: "+txtmLongitude.getText().toString()+" lat: "+txtmLatitude.getText().toString(),Toast.LENGTH_LONG).show();
           // showProgress("Please Wait");
            mServicecallEnum = ServiceCallEnum.ONDUTY;
            super.establishConnection(Request.Method.POST, Constants.mClockInUrl, mClockinDetails);
        } catch (Exception e) {
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void MarkClockOut() {

        JSONObject mClockOutDetails = new JSONObject();

        try {

            if(txtmLongitude == "0.0")
            {
                txtmLongitude="-18.5333";
            }
            if (txtmLatitude == "0.0") {
                txtmLatitude="65.9667";
            }
            LocationAddress ="nil";
            mClockOutDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mClockOutDetails.put("ClockOutLocation", LocationAddress);
            mClockOutDetails.put("ClockOutLong", txtmLongitude);
            mClockOutDetails.put("ClockOutLat", txtmLatitude);
            mClockOutDetails.put("ClockOutTime", formatted);
            mClockOutDetails.put("CreatedBy", DataBaseQueryHelper.getInstance().getUserId());

            // Toast.makeText(mActivity,formatted,Toast.LENGTH_LONG).show();

            // Toast.makeText(mActivity,"long: "+txtmLongitude.getText().toString()+" lat: "+txtmLatitude.getText().toString(),Toast.LENGTH_LONG).show();

           // showProgress("Please Wait");
            mServicecallEnum = ServiceCallEnum.OFFDUTY;

            //Toast.makeText(mActivity, mClockOutDetails.toString(), Toast.LENGTH_LONG).show();
            super.establishConnection(Request.Method.POST, Constants.mClockOutUrl, mClockOutDetails);

        } catch (Exception e) {
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void MarkJourney() {

        JSONObject mClockinDetails = new JSONObject();
        try {
            if (txtmLongitude == "0.0") {
                txtmLongitude="-18.5333";
            }
            if (txtmLatitude== "0.0") {
                txtmLatitude="65.9667";
            }
            mClockinDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mClockinDetails.put("ClockInLocation", "");
            mClockinDetails.put("ClockInLong", txtmLongitude);
            mClockinDetails.put("ClockInLat", txtmLatitude);
            mClockinDetails.put("ClockInTime", formatted);
            mClockinDetails.put("JourneyStart",1);
            mClockinDetails.put("JourneyEnd",0);

            //Toast.makeText(mActivity,formatted,Toast.LENGTH_LONG).show();

            // Toast.makeText(mActivity,"long: "+txtmLongitude.getText().toString()+" lat: "+txtmLatitude.getText().toString(),Toast.LENGTH_LONG).show();
           // showProgress("Please Wait");
            mServicecallEnum = ServiceCallEnum.JOURNEY;
            //Toast.makeText(mActivity,mClockinDetails.toString(),Toast.LENGTH_LONG).show();
            super.establishConnection(Request.Method.POST, Constants.mJourneyUrl, mClockinDetails);
        } catch (Exception e) {
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private void MarkDestination() {

        JSONObject mClockinDetails = new JSONObject();
        try {
            if (txtmLongitude == "0.0") {
                txtmLongitude="-18.5333";
            }
            if (txtmLatitude== "0.0") {
                txtmLatitude="65.9667";
            }
            mClockinDetails.put("EmployeeCode", DataBaseQueryHelper.getInstance().getUserId());
            mClockinDetails.put("ClockInLocation", "");
            mClockinDetails.put("ClockInLong", txtmLongitude);
            mClockinDetails.put("ClockInLat", txtmLatitude);
            mClockinDetails.put("ClockInTime", formatted);
            mClockinDetails.put("JourneyStart",0);
            mClockinDetails.put("JourneyEnd",1);

            //Toast.makeText(mActivity,formatted,Toast.LENGTH_LONG).show();

            // Toast.makeText(mActivity,"long: "+txtmLongitude.getText().toString()+" lat: "+txtmLatitude.getText().toString(),Toast.LENGTH_LONG).show();
          //  showProgress("Please Wait");
            mServicecallEnum = ServiceCallEnum.DESTINATION;
            //Toast.makeText(mActivity,mClockinDetails.toString(),Toast.LENGTH_LONG).show();
            super.establishConnection(Request.Method.POST, Constants.mJourneyUrl, mClockinDetails);
        } catch (Exception e) {
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
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

    private Boolean displayGpsStatus() {

        ContentResolver contentResolver = mActivity.getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void volleyOnSuccess() {
        dismissAlert();
        mhandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                //  if (!extras.isEmpty()) {
                try {
                    String responseType = (String) extras.get(getResources().getString(R.string.string_server_response));
                    if (mServicecallEnum == ServiceCallEnum.ONDUTY) {

                        JSONObject mServerResponse = new JSONObject(responseType);
                        // .net webservice automatically ads 'd' in the response json for security purpose
                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        String LocationAddress = json.getString("Location");

                        if (mMessage.equalsIgnoreCase("true")) {
                            //showSuccess("Success", "You are successfully Clock In with " + LocationAddress + " Address", FragmentTransactionEnum.NONE);
                            Toast.makeText(mActivity,"On Duty Acknowledged",Toast.LENGTH_SHORT).show();
                            ClockInBit = 1;
                           // mImgOnDuty.setEnabled(false);
                            mImgOnDuty.setVisibility(View.GONE);
                            mImgOnDutyClicked.setVisibility(View.VISIBLE);
                           // mImgOffDuty.setEnabled(true);
                            mImgOffDuty.setVisibility(View.VISIBLE);
                            mImgOffDutyClicked.setVisibility(View.GONE);
                            //mImgMeeting.setEnabled(true);
                            mImgMeeting.setVisibility(View.VISIBLE);
                            mImgMeetingClicked.setVisibility(View.GONE);
                            Check_Journey();
                           // mServicecallEnum=ServiceCallEnum.JOURNEYCHECKFROMCLOCKIN;
                        } else {
                            showMessage("Sorry", "Something went wrong");
                        }

                    }
                     if (mServicecallEnum == ServiceCallEnum.ONDUTYCHECK) {
                        JSONObject mServerResponse = new JSONObject(responseType);
                        // .net webservice automatically ads 'd' in the response json for security purpose
                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        if (mMessage.equalsIgnoreCase("true")) {

                           ClockInBit = 1;
                           // mImgOnDuty.setEnabled(false);
                            mImgOnDuty.setVisibility(View.GONE);
                            mImgOnDutyClicked.setVisibility(View.VISIBLE);
                           // mImgOffDuty.setEnabled(true);
                            mImgOffDuty.setVisibility(View.VISIBLE);
                            mImgOffDutyClicked.setVisibility(View.GONE);
                           // mImgMeeting.setEnabled(true);
                            mImgMeeting.setVisibility(View.VISIBLE);
                            mImgMeetingClicked.setVisibility(View.GONE);
                           // mServicecallEnum=ServiceCallEnum.JOURNEYCHECKFROMCLOCKIN;

                            Check_Journey();

                        } else if (mMessage.equalsIgnoreCase("both")) {
                            ClockInBit = 0;
                          //  mImgOnDuty.setEnabled(true);
                            mImgOnDuty.setVisibility(View.VISIBLE);
                            mImgOnDutyClicked.setVisibility(View.GONE);

                           // mImgOffDuty.setEnabled(false);
                            mImgOffDuty.setVisibility(View.GONE);
                            mImgOffDutyClicked.setVisibility(View.VISIBLE);

                            //mImgMeeting.setEnabled(false);
                            mImgMeeting.setVisibility(View.GONE);
                            mImgMeetingClicked.setVisibility(View.VISIBLE);
                           // mImgJourney.setEnabled(false);
                            mImgJourney.setVisibility(View.GONE);
                            mImgJorneyClicked.setVisibility(View.VISIBLE);
                           // mImgDestination.setEnabled(false);
                            mImgDestination.setVisibility(View.GONE);
                            mImgDestinationClicked.setVisibility(View.VISIBLE);

                        } else {

                            ClockInBit = 0;
                            mImgOnDuty.setVisibility(View.VISIBLE);
                            mImgOnDutyClicked.setVisibility(View.GONE);

                            // mImgOffDuty.setEnabled(false);
                            mImgOffDuty.setVisibility(View.GONE);
                            mImgOffDutyClicked.setVisibility(View.VISIBLE);

                            //mImgMeeting.setEnabled(false);
                            mImgMeeting.setVisibility(View.GONE);
                            mImgMeetingClicked.setVisibility(View.VISIBLE);
                            // mImgJourney.setEnabled(false);
                            mImgJourney.setVisibility(View.GONE);
                            mImgJorneyClicked.setVisibility(View.VISIBLE);
                            // mImgDestination.setEnabled(false);
                            mImgDestination.setVisibility(View.GONE);
                            mImgDestinationClicked.setVisibility(View.VISIBLE);
                        }

                    }
                     if (mServicecallEnum == ServiceCallEnum.OFFDUTY) {
                        JSONObject mServerResponse = new JSONObject(responseType);

                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        String LocationAddress = json.getString("Location");
                        if (mMessage.equalsIgnoreCase("true")) {
                            //showSuccess("Success", "You are successfully Clock Out with " + LocationAddress + " Address", FragmentTransactionEnum.NONE);
                            Toast.makeText(mActivity,"Off Duty Acknowledged",Toast.LENGTH_SHORT).show();
                            ClockInBit = 0;
                           // mImgOnDuty.setEnabled(true);

                            mImgOnDuty.setVisibility(View.VISIBLE);
                            mImgOnDutyClicked.setVisibility(View.GONE);

                           // mImgOffDuty.setEnabled(false);
                            mImgOffDuty.setVisibility(View.GONE);
                            mImgOffDutyClicked.setVisibility(View.VISIBLE);

                           // mImgMeeting.setEnabled(false);
                            mImgMeeting.setVisibility(View.GONE);
                            mImgMeetingClicked.setVisibility(View.VISIBLE);
                           // mImgJourney.setEnabled(false);
                            mImgJourney.setVisibility(View.GONE);
                            mImgJorneyClicked.setVisibility(View.VISIBLE);
                           // mImgDestination.setEnabled(false);
                            mImgDestination.setVisibility(View.GONE);
                            mImgDestinationClicked.setVisibility(View.VISIBLE);

                        } else {
                            showMessage("Sorry", "Something went wrong");
                        }
                    }
                     if(mServicecallEnum==ServiceCallEnum.JOURNEY){
                        JSONObject mServerResponse = new JSONObject(responseType);
                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        String LocationAddress = json.getString("Location");

                        if (mMessage.equalsIgnoreCase("true")) {
                           // showSuccess("Success", "Journey started with " + LocationAddress + " Address", FragmentTransactionEnum.NONE);
                            Toast.makeText(mActivity,"Journey Acknowledged",Toast.LENGTH_SHORT).show();
                            JourneyBit=1;
                            //mImgJourney.setEnabled(false);
                            mImgJourney.setVisibility(View.GONE);
                            mImgJorneyClicked.setVisibility(View.VISIBLE);
                           // mImgDestination.setEnabled(true);
                            mImgDestination.setVisibility(View.VISIBLE);
                            mImgDestinationClicked.setVisibility(View.GONE);

                        } else {
                            showMessage("Sorry", "Something went wrong");
                        }

                    }

                     if(mServicecallEnum==ServiceCallEnum.DESTINATION){
                        JSONObject mServerResponse = new JSONObject(responseType);
                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        String LocationAddress = json.getString("Location");

                        if (mMessage.equalsIgnoreCase("true")) {
                           // showSuccess("Success", "Your Destination : " + LocationAddress, FragmentTransactionEnum.NONE);
                            Toast.makeText(mActivity,"Destination Acknowledged",Toast.LENGTH_SHORT).show();
                            JourneyBit=0;
                            //mImgJourney.setEnabled(true);
                            mImgJourney.setVisibility(View.VISIBLE);
                            mImgJorneyClicked.setVisibility(View.GONE);
                           // mImgDestination.setEnabled(false);
                            mImgDestination.setVisibility(View.GONE);
                            mImgDestinationClicked.setVisibility(View.VISIBLE);


                        } else {
                            showMessage("Sorry", "Something went wrong");
                        }

                    }
                     if (mServicecallEnum == ServiceCallEnum.JOURNEYCHECKFROMCLOCKIN) {
                         //responseType.equalsIgnoreCase("");
                        Check_Journey();// for checking the clock in status
                    }

                     if (mServicecallEnum == ServiceCallEnum.JOURNEYCHECK) {
                        JSONObject mServerResponse = new JSONObject(responseType);
                        String mMessageObject = mServerResponse.getString("d");
                        JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("status");
                        if (mMessage.equalsIgnoreCase("true")) {

                           /* JourneyBit = 1;
                           // mImgJourney.setEnabled(false);
                            mImgJourney.setVisibility(View.GONE);
                            mImgJorneyClicked.setVisibility(View.VISIBLE);
                           // mImgDestination.setEnabled(true);
                            mImgDestination.setVisibility(View.VISIBLE);
                            mImgDestinationClicked.setVisibility(View.GONE);*/
                            JourneyBit = 0;
                            //mImgJourney.setEnabled(true);
                            mImgJourney.setVisibility(View.VISIBLE);
                            mImgJorneyClicked.setVisibility(View.GONE);
                            // mImgDestination.setEnabled(false);
                            mImgDestination.setVisibility(View.GONE);
                            mImgDestinationClicked.setVisibility(View.VISIBLE);
                        } else  {
                           /* JourneyBit = 0;
                            //mImgJourney.setEnabled(true);
                            mImgJourney.setVisibility(View.VISIBLE);
                            mImgJorneyClicked.setVisibility(View.GONE);
                            // mImgDestination.setEnabled(false);
                            mImgDestination.setVisibility(View.GONE);
                            mImgDestinationClicked.setVisibility(View.VISIBLE);*/
                            JourneyBit = 1;
                            // mImgJourney.setEnabled(false);
                            mImgJourney.setVisibility(View.GONE);
                            mImgJorneyClicked.setVisibility(View.VISIBLE);
                            // mImgDestination.setEnabled(true);
                            mImgDestination.setVisibility(View.VISIBLE);
                            mImgDestinationClicked.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();//--------------------------
                }
            }
        });
    }


    @Override
    public void volleyOnError() {
        dismissAlert();
        mhandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!extras.isEmpty()) {
                    try {
                        String responseType = (String) extras.get(getResources().getString(R.string.string_server_response));
                        showError("Error", responseType,FragmentTransactionEnum.NONE);
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

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
