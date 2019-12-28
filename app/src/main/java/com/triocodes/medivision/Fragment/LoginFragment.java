package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.triocodes.medivision.Constants.Constants;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.ParentFragment;
import com.triocodes.medivision.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by admin on 29-04-16.
 */
public class LoginFragment extends ParentFragment implements VolleyCallback,View.OnClickListener{
    TextView mTextLogin;
    EditText mEditUserId,mEditPassword;
    private Activity mActivity;
    HashMap<String, Object> extras;
    Handler mhandler;
    private Boolean flag = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment_layout,null);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
        super.initialize(mActivity, LoginFragment.this);
        extras = new HashMap<String, Object>();
        mhandler = new Handler();
        mEditUserId = (EditText) mActivity.findViewById(R.id.edit_employee_code);
        mEditPassword = (EditText) mActivity.findViewById(R.id.edit_password);
        mTextLogin = (TextView) mActivity.findViewById(R.id.text_login);
        mTextLogin.setOnClickListener(this);

        dismissAlert();

        flag = displayGpsStatus();
        if (flag) {
        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }

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
            case R.id.text_login: {
                if (super.isConnectingToInternet()) {
                    if (nullChecker()) {
                        connectToServer();
                    }
                } else {
                    showError("Connectivity Issue", "No internet connection available", FragmentTransactionEnum.NONE);
                }
                break;
            }


        }
    }

    public boolean nullChecker() {

        if (mEditUserId.getText().toString().trim().length() == 0 || mEditUserId.getText().toString().trim() == "") {
            shakeEdittext(R.id.edit_employee_code);
            return false;
        } else if (mEditPassword.getText().toString().trim().length() == 0 || mEditPassword.getText().toString().trim() == "") {
            shakeEdittext(R.id.edit_password);
            return false;
        }
        return true;
    }

    private void connectToServer() {
        JSONObject mLoginDetails = new JSONObject();

        try {
            mLoginDetails.put("EmployeeCode", mEditUserId.getText().toString());
            mLoginDetails.put("Password", mEditPassword.getText().toString());
            showProgress("Please Wait");
            super.establishConnection(Request.Method.POST, Constants.mCheckLoginUrl, mLoginDetails);

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
                    try {
                        String responseType = (String) extras.get(getResources().getString(R.string.string_server_response));
                       // String mUserID = mEditUserId.getText().toString();
                        String Password = mEditPassword.getText().toString();
                        JSONObject mServerResponse = new JSONObject(responseType);
                       String mMessageObject = mServerResponse.getString("d");
                      //  showMessage("success",mMessageObject);
                         JSONArray objJsonAry = new JSONArray(mMessageObject);
                        JSONObject json = new JSONObject(String.valueOf(objJsonAry.get(0)));
                        String mMessage = json.getString("Status");
                        String mUserID = json.getString("EmployeeCode");
                       // String Password = json.getString("Password");

                        if (mMessage.equalsIgnoreCase("true")) {
                            DataBaseQueryHelper.getInstance().insertEntry(mUserID, Password);
                            showSuccess("Success", "You are successfully login", FragmentTransactionEnum.HOME);
                            //dismissProgressWithSuccess("Login Success",FragmentTransactionEnum.NONE);
                            //startActivity(new Intent(mActivity, MainActivity.class));
                        } else {
                            showMessage("Invalid Login", "Employee Code or Password incorrect");
                            //dismissProgressWithFailure("Invalid Login",FragmentTransactionEnum.NONE);
                        }
                    } catch (Exception e) {

                    }
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


}
