package com.examp.three.activity.Trade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.common.Common;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_ONLINEPAYMENT_SEARCHDETAILS;
import static com.examp.three.common.Common.API_ONLINEPAYMENT_TRADE;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class OnlinePayment extends AppCompatActivity {
    Toolbar toolbar;
    Button clear, submit, btn_make_payment;
    LinearLayout rootlayout;
    TextInputEditText met_search;
    String muserid;
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    TextView mtxt_ApplicationNo, mtxt_ApplicantName, mtxt_FinancialYear, mtxt_LicenceValidation, mtxt_District, mtxt_mPanchayat, mtxt_TradeName, mtxtNameofEstablish, mtxtTradeAmount, mtxt_PFAAmount, mtxt_Adv_Amount, mtxt_ServiceCharge, mtxt_PenaltyAmount, mtxt_isMotor_install, mtxt_MotorCount, mtxt_Motor_Hrs_Count, mtxt_Building_Type, mtxt_Paid_Rent, mtxt_total_amount, mtxt_Application_Status;
    LinearLayout linear_search_result;
    android.app.AlertDialog waitingDialog;
    private ArrayList<String> mReferenceNumber = new ArrayList<String>();
    SpinnerDialog spinnerReferenceno;
    String Onlineapplicationno, TradersCode, District, Panchayat, TradeRate, Advertisement, PFA, Penalty, ServiceCharge, Email, MobileNo;
    String FinancialYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  nontax_newassessment_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.Online_Payment);
        met_search = findViewById(R.id.et_search);
        rootlayout = (LinearLayout) findViewById(R.id.rootlayout);

        clear = findViewById(R.id.clear);
        submit = findViewById(R.id.submit);
        btn_make_payment = findViewById(R.id.btn_make_payment);

        mtxt_ApplicationNo = findViewById(R.id.txt_application_no);
        mtxt_ApplicantName = findViewById(R.id.txt_applicant_name);
        mtxt_FinancialYear = findViewById(R.id.txt_financial_no);
        mtxt_LicenceValidation = findViewById(R.id.txt_licencevalidation);
        mtxt_District = findViewById(R.id.txt_district);
        mtxt_mPanchayat = findViewById(R.id.txt_panchayat);
        mtxt_TradeName = findViewById(R.id.txt_tradename);
        mtxtNameofEstablish = findViewById(R.id.txt_nameofestablish);
        mtxtTradeAmount = findViewById(R.id.txt_tradeamount);
        mtxt_PFAAmount = findViewById(R.id.txt_pfaamount);
        mtxt_Adv_Amount = findViewById(R.id.txt_adv_amount);
        mtxt_ServiceCharge = findViewById(R.id.txt_servicecharge);
        mtxt_PenaltyAmount = findViewById(R.id.txt_penalityamount);
        mtxt_isMotor_install = findViewById(R.id.txt_ismotorinstall);
        mtxt_MotorCount = findViewById(R.id.txt_motor_count);
        mtxt_Motor_Hrs_Count = findViewById(R.id.txt_motor_count_hrs);
        mtxt_Building_Type = findViewById(R.id.txt_build_type);
        mtxt_Paid_Rent = findViewById(R.id.txt_paid_rent);
        mtxt_Application_Status = findViewById(R.id.txt_applicationstatus);
        mtxt_total_amount = findViewById(R.id.txt_total_amount);

        linear_search_result = findViewById(R.id.linear_search_result);
        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        muserid = sharedPreference.getString(pref_login_userid, "");

        linear_search_result.setVisibility(View.GONE);

        add_Dropdown_Load();

        met_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerReferenceno.showSpinerDialog();
            }
        });

        met_search.setEnabled(false);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                met_search.setText("");
                linear_search_result.setVisibility(View.GONE);
            }
        });

        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), PaymentGateway_OnlinePayment.class);
                i.putExtra("OnlinePaymentType", "Trade");
                i.putExtra("TotalAmount", mtxt_total_amount.getText().toString());
                i.putExtra("Onlineapplicationno", mtxt_total_amount.getText().toString());
                i.putExtra("TradersCode", TradersCode);
                i.putExtra("District", District);
                i.putExtra("Panchayat", Panchayat);
                i.putExtra("TradeRate", TradeRate);
                i.putExtra("Advertisement", Advertisement);
                i.putExtra("PFA", PFA);
                i.putExtra("Penalty", Penalty);
                i.putExtra("ServiceCharge", ServiceCharge);
                i.putExtra("Mail", Email);
                i.putExtra("Mobileno", MobileNo);
                i.putExtra("Finyear", FinancialYear);
                startActivity(i);
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            }
        });


        spinnerReferenceno = new SpinnerDialog(OnlinePayment.this, mReferenceNumber, "Select or Search District", "Close");// With No Animation

        spinnerReferenceno.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                met_search.setText(item);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (met_search.getText().toString().isEmpty()) {

                    Snackbar.make(rootlayout, "Enter Reference Number", Snackbar.LENGTH_SHORT).show();

                    return;
                }

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    search_founddetails();
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    //This method is for retrieving online application numbers
    private void add_Dropdown_Load() {
        mReferenceNumber.clear();
        waitingDialog = new SpotsDialog(OnlinePayment.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_ONLINEPAYMENT_TRADE + "UserId=" + muserid + "", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonObject = response.getJSONObject(i);

                            String Onlineapplicationno = jsonObject.optString("OnlineapplicationNo");

                            mReferenceNumber.add(Onlineapplicationno);
                            met_search.setEnabled(true);
                        }

                    } else{
                        Snackbar.make(rootlayout, "No Reference Number found", Snackbar.LENGTH_SHORT).show();
                        met_search.setEnabled(false);
                    }

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    waitingDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                waitingDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    SnackShowTop("Time out", rootlayout);

                } else if (error instanceof AuthFailureError) {

                    SnackShowTop("Connection Time out", rootlayout);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", rootlayout);

                } else if (error instanceof NetworkError) {

                    SnackShowTop("Please check the internet connection", rootlayout);

                } else if (error instanceof ParseError) {

                    SnackShowTop("Parse Error", rootlayout);

                } else {

                    SnackShowTop(error.getMessage(), rootlayout);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);

    }

    //This method is for retrieving data
    private void search_founddetails() {
        waitingDialog = new SpotsDialog(OnlinePayment.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_ONLINEPAYMENT_SEARCHDETAILS + "ApplicationNo=" + met_search.getText().toString().trim(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonObject = response.getJSONObject(i);

                            Onlineapplicationno = jsonObject.optString("OnlineapplicationNo");
                            String apStreet = jsonObject.optString("ApStreet");
                            String ApCity = jsonObject.optString("ApCity");
                            String ApPIN = jsonObject.optString("ApPIN");
                            String Sno = jsonObject.optString("Sno");
                            String TradeCode = jsonObject.optString("TradeCode");
                            String ApplicantName = jsonObject.optString("ApplicantName");
                            String TradeDescription = jsonObject.optString("TradeDescription");
                            TradersCode = jsonObject.optString("TradersCode");
                            String EstablishmentName = jsonObject.optString("EstablishmentName");
                            TradeRate = jsonObject.optString("TradeRate");
                            Penalty = jsonObject.optString("Penalty");

                            PFA = jsonObject.optString("PFA");
                            Advertisement = jsonObject.optString("Advertisement");
                            ServiceCharge = jsonObject.optString("ServiceCharge");
                            String TotalAmount = jsonObject.optString("TotalAmount");
                            String WardNo = jsonObject.optString("WardNo");
                            String StreetName = jsonObject.optString("StreetName");
                            String DoorNo = jsonObject.optString("DoorNo");
                            String ApplicationDate = jsonObject.optString("ApplicationDate");
                            FinancialYear = jsonObject.optString("FinancialYear");
                            String LicenceYear = jsonObject.optString("LicenceYear");
                            String ValidFrom = jsonObject.optString("ValidFrom");
                            String ValidTo = jsonObject.optString("ValidTo");
                            MobileNo = jsonObject.optString("MobileNo");
                            Email = jsonObject.optString("Email");
                            String Reason = jsonObject.optString("Reason");
                            String EntryUser = jsonObject.optString("EntryUser");
                            String EntryDate = jsonObject.optString("EntryDate");
                            String motorInstalled = jsonObject.optString("motorInstalled");
                            String motorTotalQty = jsonObject.optString("motorTotalQty");
                            String HorsePowerTotal = jsonObject.optString("HorsePowerTotal");
                            String BuldingType = jsonObject.optString("BuldingType");
                            String RentPaid = jsonObject.optString("RentPaid");
                            Panchayat = jsonObject.optString("Panchayat");
                            District = jsonObject.optString("District");
                            String AppStatus = jsonObject.optString("AppStatus");

                            linear_search_result.setVisibility(View.VISIBLE);

                            mtxt_ApplicationNo.setText(Html.fromHtml("<b>Application Date :</b>" + ApplicationDate));
                            mtxt_ApplicantName.setText(Html.fromHtml("<b>Applicant Name :</b>" + ApplicantName));
                            mtxt_FinancialYear.setText(Html.fromHtml("<b>Financial Year :</b>" + FinancialYear));
                            mtxt_LicenceValidation.setText(Html.fromHtml("<b>Licence Validity :</b>" + LicenceYear));
                            mtxt_District.setText(Html.fromHtml("<b>District :</b>" + District));
                            mtxt_mPanchayat.setText(Html.fromHtml("<b>Town Panchayat :</b>" + Panchayat));
                            mtxt_TradeName.setText(Html.fromHtml("<b>TradeName :</b> " + TradeDescription));
                            mtxtNameofEstablish.setText(Html.fromHtml("<b>Name of Estblish :</b>" + EstablishmentName));
                            mtxtTradeAmount.setText(Html.fromHtml("<b>Trade Amount :</b>" + TradeRate));
                            mtxt_PFAAmount.setText(Html.fromHtml("<b>PFA Amount :</b>" + PFA));
                            mtxt_Adv_Amount.setText(Html.fromHtml("<b>Advt Amount :</b>" + Advertisement));
                            mtxt_ServiceCharge.setText(Html.fromHtml("<b>Service Charge :</b>" + ServiceCharge));
                            mtxt_PenaltyAmount.setText(Html.fromHtml("<b>Penalty Amount :</b>" + Penalty));
                            mtxt_isMotor_install.setText(Html.fromHtml("<b>Is motor installed :</b>" + motorInstalled));
                            mtxt_MotorCount.setText(Html.fromHtml("<b>Motor count :</b>" + motorTotalQty));
                            mtxt_Motor_Hrs_Count.setText(Html.fromHtml("<b>Motor Hrs count :</b>" + HorsePowerTotal));
                            mtxt_Building_Type.setText(Html.fromHtml("<b>Bulding Type :</b>" + BuldingType));
                            mtxt_Paid_Rent.setText(Html.fromHtml("<b>Paid Rent :</b>" + RentPaid));
                            mtxt_Application_Status.setText(Html.fromHtml("<b>Application Status :</b>" + AppStatus));

                            double total = Double.parseDouble(TradeRate) + Double.parseDouble(PFA) + Double.parseDouble(Advertisement) + Double.parseDouble(ServiceCharge) + Double.parseDouble(Penalty);
                            mtxt_total_amount.setText("" + total);

                        }

                    } else {
                        linear_search_result.setVisibility(View.GONE);
                        Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();
                    }

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    waitingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                waitingDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    SnackShowTop("Time out", rootlayout);

                } else if (error instanceof AuthFailureError) {

                    SnackShowTop("Connection Time out", rootlayout);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", rootlayout);

                } else if (error instanceof NetworkError) {

                    SnackShowTop("Please check the internet connection", rootlayout);

                } else if (error instanceof ParseError) {

                    SnackShowTop("Parse Error", rootlayout);

                } else {

                    SnackShowTop(error.getMessage(), rootlayout);

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);

    }

    //This method is for showing the snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }
}