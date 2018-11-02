package com.examp.three.activity.forgotpassword;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.examp.three.BuildConfig;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = ForgotPasswordActivity.class.getName();

    LinearLayout fp_ll_first, fp_ll_second, fp_root_layout, alert_root_layout;

    TextInputLayout fp_layout_mobile_email, fp_layout_otp, fp_layout_new_pswd, fp_layout_confirm_pswd;

    TextInputEditText fp_tie_mobile_email, fp_tie_otp, fp_tie_new_pswd, fp_tie_confirm_password;

    TextView fp_tv_send_again;

    String errorType;

    SpotsDialog spotsDialog;

    Button fp_btn_reset_pswd;

    String sOTPDeliveryMethod, sRbDeliveryMethod = "Password", sOTPMailMobile, password, sServerEmailId, sServerMobileNo, sServerUserId;
    int sOtpFromServer;
    boolean checked, TimeoutException, HttpException, GeneralException;

    String URL = "http://www.etownpanchayat.com/JavaService/Service.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //spotsdialog
        spotsDialog = new SpotsDialog(ForgotPasswordActivity.this);

        initViews();
        textWatcher();
        onClick();
    }
    //This method is for initializing all the view in layout
    public void initViews() {
        //linearlayout
        fp_ll_first = (LinearLayout) findViewById(R.id.fp_ll_first);
        fp_ll_second = (LinearLayout) findViewById(R.id.fp_ll_second);
        fp_root_layout = (LinearLayout) findViewById(R.id.fp_root_layout);

        //textinputlayout
        fp_layout_mobile_email = (TextInputLayout) findViewById(R.id.fp_layout_mobile_email);
        fp_layout_otp = (TextInputLayout) findViewById(R.id.fp_layout_otp);
        fp_layout_new_pswd = (TextInputLayout) findViewById(R.id.fp_layout_new_pswd);
        fp_layout_confirm_pswd = (TextInputLayout) findViewById(R.id.fp_layout_confirm_pswd);

        //textinputedittext
        fp_tie_mobile_email = (TextInputEditText) findViewById(R.id.fp_tie_mobile_email);
        fp_tie_otp = (TextInputEditText) findViewById(R.id.fp_tie_otp);
        fp_tie_new_pswd = (TextInputEditText) findViewById(R.id.fp_tie_new_pswd);
        fp_tie_confirm_password = (TextInputEditText) findViewById(R.id.fp_tie_confirm_password);

        //textview
        fp_tv_send_again = (TextView) findViewById(R.id.fp_tv_send_again);

        //button
        fp_btn_reset_pswd = (Button) findViewById(R.id.fp_btn_reset_pswd);

        //visibility
        fp_ll_first.setVisibility(View.VISIBLE);
        fp_ll_second.setVisibility(View.GONE);

        fp_tie_confirm_password.setEnabled(false);
        fp_tie_new_pswd.setEnabled(false);
        fp_btn_reset_pswd.setClickable(false);
    }

    public void onClick() {
        fp_btn_reset_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fp_tie_otp.getText().toString().isEmpty() && !fp_tie_new_pswd.getText().toString().isEmpty() &&
                        !fp_tie_confirm_password.getText().toString().isEmpty()) {

                    if (Common.isNetworkAvailable(getApplicationContext())) {

                        new resetPassword().execute();
                    } else {
                        Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    showSnackbar(alert_root_layout, "Please Fill the details");
                }
            }
        });

        fp_tv_send_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationSuccess();
            }
        });
    }

    //This method is for textwatcher
    public void textWatcher() {
        fp_tie_mobile_email.addTextChangedListener(new cTextWatcher(fp_tie_mobile_email));
        fp_tie_new_pswd.addTextChangedListener(new cTextWatcher(fp_tie_new_pswd));
        fp_tie_confirm_password.addTextChangedListener(new cTextWatcher(fp_tie_confirm_password));

        fp_tie_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 6) {
                    if (sOtpFromServer == Integer.parseInt(s.toString().trim())) {
                        fp_tie_confirm_password.setEnabled(true);
                        fp_tie_new_pswd.setEnabled(true);
                        fp_btn_reset_pswd.setClickable(true);
                        showSnackbar(fp_root_layout, "OTP Matched");
                        fp_tie_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle, 0);
                    } else {
                        fp_tie_confirm_password.setEnabled(false);
                        fp_tie_new_pswd.setEnabled(false);
                        fp_btn_reset_pswd.setClickable(false);
                        showSnackbar(fp_root_layout, "OTP doesn't Match");
                        fp_tie_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel_rounded_red, 0);

                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //This method is for sending otp
    public void sendOTP(View v) {
        validationSuccess();
    }

    //This method is for validating email id from user
    private void validationSuccess() {

        if (!fp_tie_mobile_email.getText().toString().isEmpty()) {

            if (sRbDeliveryMethod.equalsIgnoreCase("UserId")) {

                //to check email
                if (!validateEmail()) {
                    return;
                }
            } else if (sRbDeliveryMethod.equalsIgnoreCase("Password")) {

                if (!validateEmail()) {
                    return;
                }
            }
            if (Common.isNetworkAvailable(getApplicationContext())) {
                verifyEmailIdFromUser();
            } else {
                Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(fp_root_layout, "Enter Email Id !", Snackbar.LENGTH_SHORT).show();
        }
    }

    //This method is for showing snackbar in this activity
    public void showSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    //This method is for validating password
    public boolean validatePassword(String pass, TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        if (pass.length() < 8) {
            textInputLayout.setError("Password is too Short");
            textInputLayout.setHintEnabled(false);
            requestFocus(textInputEditText);
            return false;
        }

        if (!pass.matches(".*\\d.*")) {

            textInputLayout.setError("Password does not contain digits");
            textInputLayout.setHintEnabled(false);
            requestFocus(textInputEditText);
            return false;
        }

        if (!pass.matches(".*[!@#$%^&*+=?-].*")) {
            textInputLayout.setError("Password does not contain special characters");
            textInputLayout.setHintEnabled(false);
            requestFocus(textInputEditText);
            System.out.println("no special chars found");
            return false;
        }

        textInputLayout.setErrorEnabled(false);
        fp_tie_confirm_password.setEnabled(true);
        return true;
    }

    //This method is for showing error if password doesn't match
    public boolean validateMatch(TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        if (!textInputEditText.getText().toString().equals(fp_tie_new_pswd.getText().toString())) {
            textInputLayout.setError("Password doesn't Match");
            textInputLayout.setHintEnabled(false);
            requestFocus(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating email
    private boolean validateEmail() {
        String email = fp_tie_mobile_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            fp_layout_mobile_email.setError("Invalid Email Id");
            fp_layout_mobile_email.setHintEnabled(false);
            requestFocus(fp_tie_mobile_email);
            return false;
        } else {
            fp_layout_mobile_email.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {

        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    //This method is for radio button validations
    public void onRadioButtonClicked(View v) {
        checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {

            case R.id.fp_rb_userid:
                if (checked) {
                    fp_layout_mobile_email.setHint("Email Id");
                    sRbDeliveryMethod = "UserId";
                }
                break;
            case R.id.fp_rb_password:
                if (checked) {
                    fp_layout_mobile_email.setHint("Email Id / UserId");
                    sRbDeliveryMethod = "Password";
                }
                break;
        }
    }

    //This method is for viewing the alert for continuing
    private void alertContinueWith(final String email, final String mobileNo) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_continue_with, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        TextView fp_alert_tv_continue = (TextView) dialogView.findViewById(R.id.fp_alert_tv_continue);
        final TextView alert_tv_mobile = (TextView) dialogView.findViewById(R.id.alert_tv_mobile);
        final TextView alert_tv_email = (TextView) dialogView.findViewById(R.id.alert_tv_email);
        alert_root_layout = (LinearLayout) dialogView.findViewById(R.id.alert_root_layout);
        ImageView fp_alert_cancel = (ImageView) dialogView.findViewById(R.id.fp_alert_cancel);

        String replacedStringMobileNo = "*******" + mobileNo.substring(7, 10);

        alert_tv_mobile.setText(replacedStringMobileNo);
        alert_tv_email.setText(email);

        alert_tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sOTPDeliveryMethod = email;

                sOTPMailMobile = email;

                alert_tv_email.setTextColor(getResources().getColor(R.color.green));
                alert_tv_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_green, 0, 0, 0);
                alert_tv_mobile.setTextColor(getResources().getColor(R.color.textColorBlack));
                alert_tv_mobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);

            }
        });

        alert_tv_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sOTPDeliveryMethod = mobileNo;

                sOTPMailMobile = mobileNo;

                alert_tv_mobile.setTextColor(getResources().getColor(R.color.green));
                alert_tv_mobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_green, 0, 0, 0);
                alert_tv_email.setTextColor(getResources().getColor(R.color.textColorBlack));
                alert_tv_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail, 0, 0, 0);
            }
        });

        fp_alert_tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sOTPDeliveryMethod != null && !sOTPDeliveryMethod.isEmpty()) {
                    dialog.dismiss();

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        sendOTP();
                    } else {
                        Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    showSnackbar(alert_root_layout, "Please Choose Delivery Method");
                }
            }
        });
        fp_alert_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //This method is for verifying email from user
    public void verifyEmailIdFromUser() {

        spotsDialog.show();
        RetrofitInterface apiInterface = RetrofitInstance.getRetrofitEtown().create(RetrofitInterface.class);

        Call call = apiInterface.verifyEmailIdFromUser(fp_tie_mobile_email.getText().toString());

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {
                    if (BuildConfig.DEBUG) {

                        Log.e(TAG, "" + response.raw().toString());
                    }
                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONObject jsonObject = new JSONObject(sResponse);

                        int code = jsonObject.optInt("code");
                        String message = jsonObject.optString("message");

                        if (message.contains("success")) {

                            if (Common.isNetworkAvailable(getApplicationContext())) {

                                getUserDetails();

                            } else {
                                Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            showSnackbar(fp_root_layout, "Invalid Email Id !");
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(fp_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();
                if (t instanceof IOException) {
                    errorType = "Timeout";
                    Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(fp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                } else {
                    errorType = "Error";
                    Snackbar.make(fp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    //This method is for retrieving user details
    public void getUserDetails() {

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofitEtown().create(RetrofitInterface.class);

        Call call = apiInterface.getUserDetails(fp_tie_mobile_email.getText().toString());

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {

                    if (BuildConfig.DEBUG) {

                        Log.e(TAG, "" + response.raw().toString());
                    }

                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONArray jsonArray = new JSONArray(sResponse);
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                sServerEmailId = jsonObject.optString("emailId");
                                sServerMobileNo = jsonObject.optString("contactNo");
                                sServerUserId = jsonObject.optString("userId");

                                alertContinueWith(sServerEmailId, sServerMobileNo);
                            }
                        } else {
                            Snackbar.make(fp_root_layout, "No data found !", Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(fp_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();

                if (t instanceof IOException) {
                    errorType = "Timeout";
                    Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(fp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                } else {
                    errorType = "Error";
                    Snackbar.make(fp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    //This method is for sending otp for mobile number
    public void sendOTP() {

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofitPaypre().create(RetrofitInterface.class);

        Call call = apiInterface.sendOTP(sOTPMailMobile);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {

                    if (BuildConfig.DEBUG) {

                        Log.e(TAG, "" + response.raw().toString());
                    }
                    String sResponse = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(sResponse);
                        String Message = jsonObject.optString("Message");

                        if (Message.equalsIgnoreCase("Success")) {
                            sOtpFromServer = jsonObject.optInt("PassKey");
                            Log.e(TAG, "" + sOtpFromServer);

                            fp_ll_first.setVisibility(View.GONE);
                            fp_ll_second.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else

                {
                    Snackbar.make(fp_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();
                if (t instanceof IOException) {
                    errorType = "Timeout";
                    Snackbar.make(fp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(fp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                } else {
                    errorType = "Error";
                    Snackbar.make(fp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    //This method is for resetting password
    public class resetPassword extends AsyncTask<Void, Void, Void> {

        String emailId, userId;
        ProgressDialog dialog = new ProgressDialog(ForgotPasswordActivity.this);
        String status;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);
            emailId = sServerEmailId;
            userId = sServerUserId;
        }

        protected Void doInBackground(final Void... unused) {
            password = fp_tie_new_pswd.getText().toString();
            final String SOAP_ACTION = "http://tempuri.org/UpdatePasswordDetails";
            final String NAMESPACE = "http://tempuri.org/";
            final String METHOD_NAME = "UpdatePasswordDetails";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sUserId", userId);
            request.addProperty("sEmailId", emailId);
            request.addProperty("sNewPassword", password);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                String responseJSON = response.toString();
                status = responseJSON;
            } catch (SocketTimeoutException e) {
                TimeoutException = true;
                e.printStackTrace();
            } catch (UnknownHostException e) {
                HttpException = true;
                e.printStackTrace();
            } catch (Exception e) {
                GeneralException = true;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (status.equals("Y")) {
                Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();

            } else if (status.equals("N")) {
                showSnackbar(fp_root_layout, "Password not Updated");
            } else {
                showSnackbar(fp_root_layout, "Something went wrong");
            }
            if (TimeoutException) {

                showSnackbar(fp_root_layout, "Unable to connect to server");

            } else if (HttpException) {
                showSnackbar(fp_root_layout, "No Internet connection");

            } else if (GeneralException) {
                showSnackbar(fp_root_layout, "Please try later");

            }

            TimeoutException = false;
            HttpException = false;
            GeneralException = false;
        }
    }

    private class cTextWatcher implements TextWatcher {

        private View view;

        private cTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.fp_tie_mobile_email:
                    validateEmail();
                    break;
                case R.id.fp_tie_new_pswd:

                    validatePassword(fp_tie_new_pswd.getText().toString(), fp_layout_new_pswd, fp_tie_new_pswd);

                    break;
                case R.id.fp_tie_confirm_password:

                    if (!fp_tie_new_pswd.getText().toString().isEmpty()) {

                        validateMatch(fp_layout_confirm_pswd, fp_tie_confirm_password);

                        //  validatePassword(fp_tie_confirm_password.getText().toString(), fp_layout_confirm_pswd, fp_tie_confirm_password);
                    } else {
                        showSnackbar(fp_root_layout, "Please enter New Password");
                        fp_tie_confirm_password.setEnabled(false);
//                        fp_tie_confirm_password.setText("");
                    }

                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (spotsDialog != null) {
            spotsDialog.dismiss();
            spotsDialog = null;
        }
    }
}
