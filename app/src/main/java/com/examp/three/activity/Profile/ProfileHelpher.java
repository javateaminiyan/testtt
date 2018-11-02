package com.examp.three.activity.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileHelpher {
    private static ProfileHelpher mProfileHelpher = null;
    Activity mactivity;
    SpinnerDialog spinnerDialog;
    ArrayList<String> mTitleList = new ArrayList<>();
    ArrayList<String> mStateList = new ArrayList<>();
    String mSelectedTitle;
    String mSelectedState;
    String mGeneratedOtp;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ProfileHelpher(Activity activity){
        this.mactivity = activity;
    }

    public static ProfileHelpher getInstance(Activity activity){
        if(mProfileHelpher == null){
            mProfileHelpher = new ProfileHelpher(activity);
        }
        if(mProfileHelpher.mactivity!=activity){
            mProfileHelpher.mactivity = activity;
        }
        return mProfileHelpher;
    }

    void getTitle(EditText editText){
        mTitleList.clear();
        mTitleList.add("Mr.");
        mTitleList.add("Mrs.");
        mTitleList.add("Miss.");
        mTitleList.add("Dr.");
        setSpinnerComman(mTitleList,editText,"Title");
    }

    //This method is for getting state list
    void getState(final EditText editText){
        final ProgressDialog pd = new ProgressDialog(mactivity);
        pd.setMessage("Verifying...");
        pd.setCancelable(false);
        pd.show();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofitPaypre().create(RetrofitInterface.class);
        Call result = retrofitInterface.getStates();
        Log.e("url==>",result.request().toString());
        result.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String response1 = new Gson().toJson(response.body());
                Log.e("re",response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mStateList.add(jsonObject.optString("StateName"));
                        }
                    }
                    setSpinnerComman(mStateList,editText,"State");

                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                String errorType = "Try again Later!";
                if (t instanceof IOException) {

                    errorType = "Timeout";
                    Log.e("errorr=>","No Internet Connection !");

                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Log.e("errorr=>",errorType);

                } else {
                    errorType = "Error";
                    Log.e("errorr=>",errorType);

                }
                Toast.makeText(mactivity, errorType+" Try again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //This method is for setting the common spinner
    public void setSpinnerComman(final ArrayList<String> arrayList, final EditText editText, final String title) {

        spinnerDialog = new SpinnerDialog(mactivity, arrayList, "Select " + title, R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                editText.setText(arrayList.get(i));
              switch (title) {
                    case "Title":
                        mSelectedTitle = arrayList.get(i);
                        break;
                    case "State":
                        mSelectedState = arrayList.get(i);
                        break;
                }
            }
        });
    }

    //This method is for checking and requesting permission
    public  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(mactivity,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(mactivity, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(mactivity, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mactivity,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    //This method is for sending otp to mobile number
    public void sendOtp(String sender, final TextInputLayout tiLayout){
        final ProgressDialog pd = new ProgressDialog(mactivity);
        pd.setMessage("Verifying...");
        pd.setCancelable(false);
        pd.show();
        Log.e("sender",sender);
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofitPaypre().create(RetrofitInterface.class);
        Call result = retrofitInterface.sendOTP(sender);
        Log.e("url==>",result.request().toString());
        result.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String response1 = new Gson().toJson(response.body());
                Log.e("re",response1);
                try {
                    JSONObject jsonObject = new JSONObject(response1);
                    if(jsonObject.optString("Message").equalsIgnoreCase("Success")){
                        mGeneratedOtp = String.valueOf(jsonObject.optInt("PassKey"));

                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                String errorType = "Try again Later!";
                if (t instanceof IOException) {

                     errorType = "Timeout";
                    Log.e("errorr=>","No Internet Connection !");

                } else if (t instanceof IllegalStateException) {
                     errorType = "ConversionError";
                    Log.e("errorr=>",errorType);

                } else {
                    errorType = "Error";
                    Log.e("errorr=>",errorType);

                }
                Toast.makeText(mactivity, errorType+" Try again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //This method is for checking the mobile number is valid or not
    public boolean isValidMobileNumber(String mobileNo) {

        String reg = "^(\\d)(?!\\1+$)\\d{10}$";

        String regex = "^[5-9]\\d{9}$";

        if (!mobileNo.matches(regex) && !mobileNo.matches(reg)) {
            return false;
        } else {
           return true;
        }
    }

    //This method is for checking the email id is valid or not
    public boolean isValidEmail(String email) {

        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //This method is for showing the snack bar in this activity
    public void showSnackbar(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snack.show();
    }

}
