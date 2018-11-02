package com.examp.three.activity.Trade;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examp.three.BuildConfig;
import com.examp.three.R;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class TL_ApplicationStatus_Activity extends AppCompatActivity {
    private static final String TAG = "Application status";

    //views
    TextInputLayout as_til_referenceNo_layout;

    EditText as_et_referenceNo;

    TextView as_tv_app_date, as_tv_applicant_name;
    TextView as_tv_tradeName, as_tv_buildingtype, as_tv_license_type, as_tv_fin_year,
            as_tv_license_validity, as_tv_name_of_establ, as_tv_trade_amt, as_tv_adver_amt,
            as_tv_penalty_amt, as_tv_pfa_amt, as_tv_total_amt, as_tv_paid_rent,as_tv_statuss,
            as_tv_total_motor_qty,as_tv_total_horsepower,as_tv_motor_installed,as_tv_service_charge;

    Toolbar as_toolbar;

    Button as_btn_search;

    RelativeLayout  rootlayout;

    LinearLayout as_ll_app_details,as_ll_motor_installed,as_ll_total_motor_qty,as_ll_total_horse_power;

    CardView as_cd_trade_details;

    SpotsDialog spotsDialog;

    String errorType,login_userId;

    SpinnerDialog spinnerDialog;

    ArrayList<String> sReferenceList;


    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tl_application_status);

        spotsDialog = new SpotsDialog(TL_ApplicationStatus_Activity.this);
        sReferenceList = new ArrayList<>();


        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");
        init();
        initVisibility(View.GONE);
        onClick();

        getReferenceNo(login_userId);

    }

    //This method is for initializing view in the layout activity
    public void init() {
        // textinputlayout
        as_til_referenceNo_layout = (TextInputLayout) findViewById(R.id.as_til_referenceNo_layout);

        //edittext
        as_et_referenceNo = (EditText) findViewById(R.id.as_et_referenceNo);

        //toolbar
        as_toolbar = (Toolbar) findViewById(R.id.as_toolbar);

        //button
        as_btn_search = (Button) findViewById(R.id.as_btn_search);


        as_tv_app_date = (TextView) findViewById(R.id.as_tv_app_date);
        as_tv_applicant_name = (TextView) findViewById(R.id.as_tv_applicant_name);

        //---

        as_tv_tradeName = (TextView) findViewById(R.id.as_tv_tradeName);
        as_tv_buildingtype = (TextView) findViewById(R.id.as_tv_buildingtype);
        as_tv_license_type = (TextView) findViewById(R.id.as_tv_license_type);
        as_tv_fin_year = (TextView) findViewById(R.id.as_tv_fin_year);
        as_tv_license_validity = (TextView) findViewById(R.id.as_tv_license_validity);
        as_tv_name_of_establ = (TextView) findViewById(R.id.as_tv_name_of_establ);
        as_tv_trade_amt = (TextView) findViewById(R.id.as_tv_trade_amt);
        as_tv_adver_amt = (TextView) findViewById(R.id.as_tv_adver_amt);
        as_tv_penalty_amt = (TextView) findViewById(R.id.as_tv_penalty_amt);
        as_tv_pfa_amt = (TextView) findViewById(R.id.as_tv_pfa_amt);
        as_tv_total_amt = (TextView) findViewById(R.id.as_tv_total_amt);
        as_tv_paid_rent = (TextView) findViewById(R.id.as_tv_paid_rent);
        as_tv_statuss = (TextView) findViewById(R.id.as_tv_statuss);
        as_tv_total_motor_qty = (TextView) findViewById(R.id.as_tv_total_motor_qty);
        as_tv_total_horsepower = (TextView) findViewById(R.id.as_tv_total_horsepower);
        as_tv_motor_installed = (TextView) findViewById(R.id.as_tv_motor_installed);
        as_tv_service_charge = (TextView) findViewById(R.id.as_tv_service_charge);

        //relative layout
        rootlayout = (RelativeLayout) findViewById(R.id.root_layout);


        //linearlayout
        as_ll_app_details = (LinearLayout) findViewById(R.id.as_ll_app_details);
        as_ll_motor_installed = (LinearLayout) findViewById(R.id.as_ll_motor_installed);
        as_ll_total_motor_qty = (LinearLayout) findViewById(R.id.as_ll_total_motor_qty);
        as_ll_total_horse_power = (LinearLayout) findViewById(R.id.as_ll_total_horse_power);

        //cardview
        as_cd_trade_details = (CardView) findViewById(R.id.as_cd_trade_details);
    }

    //This method is for visibility
    public void initVisibility(int Visibility) {
        as_ll_app_details.setVisibility(Visibility);
        as_cd_trade_details.setVisibility(Visibility);
    }

    //This method is for onclick listener
    public void onClick() {

        setSupportActionBar(as_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_applicationStatus);


        as_et_referenceNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                as_et_referenceNo.setText("");
                initVisibility(View.GONE);

                if(Common.isNetworkAvailable(getApplicationContext())) {

                    if (sReferenceList.size() > 0) {
                        setSpinnerForPanchayat(sReferenceList);
                    } else {
                        Snackbar.make(rootlayout, "No Reference number found !", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        as_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateData(as_et_referenceNo, as_til_referenceNo_layout, "Please Enter the Reference No.")) {
                    initVisibility(View.VISIBLE);

                    if(Common.isNetworkAvailable(getApplicationContext())) {
                        getApplicationStatus(as_et_referenceNo.getText().toString());
                    }
                    else
                    {
                        Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    initVisibility(View.GONE);
                }
            }
        });
    }

    //This method is for getting reference number list
    public void getReferenceNo(String userid) {

        sReferenceList.clear();

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getReferenceNo(Common.ACCESS_TOKEN,userid);

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

                                String OnlineapplicationNo = jsonObject.getString("OnlineapplicationNo");


                                sReferenceList.add(OnlineapplicationNo);

                                if (BuildConfig.DEBUG) {

                                    Log.e(TAG, "" + OnlineapplicationNo + " -- ");
                                }
                            }



                        } else {
                            Snackbar.make(rootlayout, "No Reference Number found !", Snackbar.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Snackbar.make(rootlayout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();
                if (t instanceof IOException) {
                    errorType = "Timeout";

                    Snackbar.make(rootlayout, "Please Check network Connection", Snackbar.LENGTH_SHORT).show();


                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(rootlayout, errorType, Snackbar.LENGTH_SHORT).show();

                } else {
                    errorType = "Error";
                    Snackbar.make(rootlayout, errorType, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    //This method is for getting application status for a reference number
    public void getApplicationStatus(String sReferenceNo) {

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getApplicationStatus(Common.ACCESS_TOKEN,sReferenceNo);

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


                                String ApplicantName = jsonObject.optString("ApplicantName");
                                String ApplicationDate = jsonObject.optString("ApplicationDate");
                                String TradeDescription = jsonObject.optString("TradeDescription");
                                String BuldingType = jsonObject.optString("BuldingType");
                                String LicenceValidity = jsonObject.optString("LicenceValidity");
                                String FinancialYear = jsonObject.optString("FinancialYear");
                                String EstablishmentName = jsonObject.optString("EstablishmentName");
                                String LicenceTypeNameEnglish = jsonObject.optString("LicenceTypeNameEnglish");
                                String AppStatus = jsonObject.optString("AppStatus");
                                String motorInstalled = jsonObject.optString("motorInstalled");
                                int TradeRate = jsonObject.optInt("TradeRate");
                                int Advertisement = jsonObject.optInt("Advertisement");
                                int Penalty = jsonObject.optInt("Penalty");
                                int PFA = jsonObject.optInt("PFA");
                                int TotalAmount = jsonObject.optInt("TotalAmount");
                                int RentPaid = jsonObject.optInt("RentPaid");
                                int ServiceCharge = jsonObject.optInt("ServiceCharge");
                                int motorTotalQty = jsonObject.optInt("motorTotalQty");
                                int HorsePowerTotal = jsonObject.optInt("HorsePowerTotal");

                                as_tv_applicant_name.setText(ApplicantName);
                                as_tv_app_date.setText(ApplicationDate);
                                as_tv_buildingtype.setText(BuldingType);
                                as_tv_license_validity.setText(LicenceValidity);
                                as_tv_fin_year.setText(FinancialYear);
                                as_tv_name_of_establ.setText(EstablishmentName);
                                as_tv_license_type.setText(LicenceTypeNameEnglish);
                                as_tv_statuss.setText("Status : "+ AppStatus);
                                as_tv_trade_amt.setText("\u20B9 "+String.valueOf(TradeRate));
                                as_tv_adver_amt.setText("\u20B9 "+String.valueOf(Advertisement));
                                as_tv_penalty_amt.setText("\u20B9 "+String.valueOf(Penalty));
                                as_tv_pfa_amt.setText("\u20B9 "+String.valueOf(PFA));
                                as_tv_total_amt.setText("\u20B9 "+String.valueOf(TotalAmount));
                                as_tv_paid_rent.setText("\u20B9 "+String.valueOf(RentPaid));
                                as_tv_service_charge.setText("\u20B9 "+String.valueOf(ServiceCharge));
                                as_tv_total_motor_qty.setText(String.valueOf(motorTotalQty));
                                as_tv_total_horsepower.setText(String.valueOf(HorsePowerTotal));
                                as_tv_motor_installed.setText(motorInstalled);
                                as_tv_tradeName.setText(TradeDescription);

                                if(motorInstalled.equalsIgnoreCase("Yes"))
                                {
                                    as_ll_total_horse_power.setVisibility(View.VISIBLE);
                                    as_ll_total_motor_qty.setVisibility(View.VISIBLE);
                                }
                                else if(motorInstalled.equalsIgnoreCase("No"))
                                {
                                    as_ll_total_horse_power.setVisibility(View.GONE);
                                    as_ll_total_motor_qty.setVisibility(View.GONE);
                                }

                                if (BuildConfig.DEBUG) {

                                    Log.e(TAG, "" + RentPaid + " -- ");
                                }
                            }


                        } else {
                            Snackbar.make(rootlayout, "No data found !", Snackbar.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Snackbar.make(rootlayout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();
                if (t instanceof IOException) {
                    errorType = "Timeout";

                    Snackbar.make(rootlayout, "Please Check network Connection", Snackbar.LENGTH_SHORT).show();


                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(rootlayout, errorType, Snackbar.LENGTH_SHORT).show();

                } else {
                    errorType = "Error";
                    Snackbar.make(rootlayout, errorType, Snackbar.LENGTH_SHORT).show();

                }
            }
        });

    }

    //This method is for setting spinner for panchayat list
    public void setSpinnerForPanchayat(ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(TL_ApplicationStatus_Activity.this, arrayList, "Select Panchayat", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                as_et_referenceNo.setText(sReferenceList.get(i));

                if (BuildConfig.DEBUG) {

                    Log.e(TAG, "" + sReferenceList.get(i));
                }
            }
        });
    }

    //This method is for validating data
    private boolean validateData(EditText editText, TextInputLayout layout, String error) {

        boolean result = true;

        String name = editText.getText().toString();

        if (name.isEmpty()) {
            layout.setError(error);
            layout.setHintEnabled(false);
            result = false;
        } else {
            layout.setErrorEnabled(false);
        }
        return result;

    }

}
