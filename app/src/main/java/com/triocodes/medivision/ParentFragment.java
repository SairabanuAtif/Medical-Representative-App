package com.triocodes.medivision;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.triocodes.medivision.Enum.FragmentTransactionEnum;
import com.triocodes.medivision.Interface.FragmentListenerCallback;
import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.volley.CustomJSONObjectRequest;
import com.triocodes.medivision.volley.CustomVolleyRequestQueue;


import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ParentFragment extends Fragment implements Response.Listener,
        Response.ErrorListener {
    private Activity mActivity;
    private VolleyCallback mVolleyCallback;
    private FragmentListenerCallback mAlertCallback;
    private RequestQueue mQueue;
    private CustomJSONObjectRequest CustomRequest;
    public static final String REQUEST_TAG = "MainVolleyActivity";
    SweetAlertDialog mAlertDialog;
    private final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    private static final String SHARED_DETAILS = "SHARED_DETAILS";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    Calendar myCalendar = Calendar.getInstance();
    private EditText mEditText;



    public void initialize(Activity mActivity, VolleyCallback mVolleyCallback) {
        this.mActivity = mActivity;
        this.mVolleyCallback = mVolleyCallback;
        this.mAlertCallback = (FragmentListenerCallback) mActivity;

        this.appSharedPrefs = mActivity.getSharedPreferences(SHARED_DETAILS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();

    }


    public void establishConnection(int method, String url, JSONObject jsonRequest) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.mActivity)
                .getRequestQueue();
        CustomRequest = new CustomJSONObjectRequest(method, url,
                jsonRequest, this, this);
        CustomRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomRequest.setTag(REQUEST_TAG);
        mQueue.add(CustomRequest);

    }

    public boolean emailValidation(String str) {
        if (str.equals("")
                || EMAIL_ADDRESS_PATTERN.matcher(str).matches() == false)
            return false;
        else
            return true;
    }

    public boolean mobileValidation(String strmob) {

        if (strmob.matches("[0-9]{10}"))
            return true;
        else
            return false;
    }

    public void showProgress(String message) {

        mAlertDialog = new SweetAlertDialog(this.mActivity, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(message);
        mAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#0AB4AB"));
        mAlertDialog.show();
        mAlertDialog.setCancelable(false);

    }


    public void dismissProgressWithSuccess(String message, final FragmentTransactionEnum mCalltype) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.setTitleText(message)
                    .setConfirmText("OK")
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    toFragment(mCalltype);
                    dismissAlert();
                }
            });
        }
    }

    public void dismissProgressWithFailure(String message, final FragmentTransactionEnum mCalltype) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.setTitleText(message)
                    .setConfirmText("OK")
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    toFragment(mCalltype);
                    dismissAlert();
                }
            });
        }
    }

    public void dismissAlert() {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public void showError(String header, String message, final FragmentTransactionEnum mCalltype) {

        mAlertDialog = new SweetAlertDialog(this.mActivity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(header)
                .setContentText(message);
        mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                toFragment(mCalltype);
                dismissAlert();
            }
        });
        mAlertDialog.show();
    }

    public void showSuccess(String header, String message, final FragmentTransactionEnum mCalltype) {

        mAlertDialog = new SweetAlertDialog(this.mActivity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(header)
                .setContentText(message);
        mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                toFragment(mCalltype);
                dismissAlert();
            }
        });
        mAlertDialog.show();
    }

    public void showMessage(String header, String message) {

        mAlertDialog = new SweetAlertDialog(this.mActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(header)
                .setContentText(message);

        mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                //toFragment(mCalltype);
                dismissAlert();

            }
        });
        mAlertDialog.show();
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) this.mActivity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;

      /*  NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                // Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                // Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        }*/


    }

    public void shakeEdittext(int widgetId) {
        YoYo.with(Techniques.Bounce)
                .duration(700)
                .playOn(this.mActivity.findViewById(widgetId));
        EditText mEdittext = (EditText) this.mActivity.findViewById(widgetId);
        mEdittext.setHintTextColor(Color.RED);
        mEdittext.requestFocus();
    }

    public void shakeSpinner(int widgetId) {
        YoYo.with(Techniques.Bounce)
                .duration(700)
                .playOn(this.mActivity.findViewById(widgetId));
        Spinner mSpinnertext = (Spinner) this.mActivity.findViewById(widgetId);
        mSpinnertext.setBackgroundResource(R.drawable.border_spinner_error);
        mSpinnertext.requestFocus();

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        String mErrorMessage;

        NetworkResponse response = volleyError.networkResponse;
        if (response != null && response.data != null) {
            mErrorMessage = String.valueOf(response.statusCode);
            switch (response.statusCode) {

                case HttpURLConnection.HTTP_NOT_FOUND:
                    mErrorMessage = mErrorMessage + " error page not found";
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    mErrorMessage = mErrorMessage + " error internal server error";
                    break;

            }

        } else {
            mErrorMessage = "Unexpected error from server " + volleyError.getMessage().toString();


        }
        this.mVolleyCallback.getExtras().put(this.mActivity.getResources().getString(R.string.string_server_response), mErrorMessage);
        this.mVolleyCallback.volleyOnError();


    }

    @Override
    public void onResponse(Object response) {

        this.mVolleyCallback.getExtras().put(mActivity.getResources().getString(R.string.string_server_response), response.toString());
        this.mVolleyCallback.volleyOnSuccess();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    public void toFragment(FragmentTransactionEnum mType) {
        switch (mType) {

            case LOGIN:
                mAlertCallback.loadLogin();
                break;
            case HOME:
                mAlertCallback.loadHome();
                break;
            case MEETING:
                mAlertCallback.loadMeeting();
                break;
            case TASK:
                mAlertCallback.loadTask();
                break;
            case TASKSTATUS:
                mAlertCallback.loadTaskStatus();
                break;
            case MEETINGREPORT:
                mAlertCallback.loadMeetingReport();
                break;
            case DUTYREPORT:
                mAlertCallback.loadDutyReport();
                break;
            case JOURNEYREPORT:
                mAlertCallback.loadJourneyReport();
                break;
            case NONE:
                break;
        }

    }


    public String getSharedvalueString(String key) {
        return appSharedPrefs.getString(key, "error");

    }


    public void setSharedvalueString(String key, String string) {
        prefsEditor.putString(key, string).commit();
    }

    public int getSharedvalueInteger(String key) {
        return appSharedPrefs.getInt(key, 0);

    }

    public void setSharedvalueInteger(String key, int value) {
        prefsEditor.putInt(key, value).commit();
    }

    public void clearSp() {
        this.appSharedPrefs = mActivity.getSharedPreferences(SHARED_DETAILS,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.clear();
    }

    DatePickerDialog.OnDateSetListener date= new DatePickerDialog.OnDateSetListener()  {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    public void setDate(int widgetId) {
        mEditText = (EditText) this.mActivity.findViewById(widgetId);
        new DatePickerDialog(mActivity,date,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditText.setText(sdf.format(myCalendar.getTime()));
    }


}
