package com.examp.three.activity.Trade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.paymentgateway.activity.WebViewActivity;
import com.examp.three.activity.paymentgateway.utility.AvenuesParams;
import com.examp.three.activity.paymentgateway.utility.Params;
import com.examp.three.common.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.AMOUNT;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_EMAIL;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_TEL;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.ORDER_ID;
import static com.examp.three.activity.paymentgateway.utility.Params.userId;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_CCAVENUEENTRYNO;
import static com.examp.three.common.Common.API_CCAVENUE_CHECK_BANK;
import static com.examp.three.common.Common.API_CCAVENUE_MAILCREDENTIAL;
import static com.examp.three.common.Common.API_CCAVENUE_MERCHANTACCESSCODE;
import static com.examp.three.common.Common.API_CCAVUENUERATEDETAILSLIST;
import static com.examp.three.common.Common.API_CCAVUENUE_GET_ALLBANKS;
import static com.examp.three.common.Common.API_ONLINE_TRADE_ONLINEBEFORETRANSACTIONDETAILS;
import static com.examp.three.common.Common.AccessCode_CC_Avuenue;
import static com.examp.three.common.Common.CURRENCY_CC_Avuenue;
import static com.examp.three.common.Common.MERCHANTID_CC_Avuenue;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_address;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_city;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_contact;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_country;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_email;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_lastname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_pincode;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_state;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_streetname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_title;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class PaymentGateway_OnlinePayment extends AppCompatActivity {
    String TAG = PaymentGateway_OnlinePayment.class.getSimpleName();
    Toolbar toolbar;
    LinearLayout rootlayout;
    String CC_debitAmount, CC_hdfc_NetBank, CC_cashCard, CC_serviceTax, CC_bankCharges,
            CC_effectiveDate, CC_rateStatus, CC_master_Creditcard, CC_amex_Creditcard,
            CC_master_DebitCard_2KBelow, CC_master_DebitCard_2KAbove, CC_others_NetBank, CC_bankName, CC_paymentmode;
    String TaxType;
    private String mOnline_Entry_Value, mOnline_Bank_Status;
    private HashMap<String, String> mBankHashTable = new LinkedHashMap<>();
    private ArrayList<String> mBankNameList = new ArrayList<>();
    String mIntent_Type, mIntent_TotalAmount;
    Button msubmit, mclear;
    TextInputEditText medt_payment_type, medt_paymentbank;
    TextView mtxt__payment_detail_user, mtxt_applicationno, mtxt_building_amount, mtxt_tradesno, mtxt_district, mtxt_pachayat, mtxt_tradefee, mtxt_penaltyamount, mtxt_pfaamount, mtxt_advt_amount, mtxt_servicecharges,
            mtxt_totalamount, mtxt_demand_amount, mtxt_trans_amount, mtxt_gst_amount, mtxt_bank_charges, mtxt_total_amounttobepaid;
    android.app.AlertDialog waitingDialog;
    private ArrayList<String> mPaymentTypeList = new ArrayList<String>();

    private ArrayList<String> mPaymentTypeBanks = new ArrayList<String>();
    SpinnerDialog spinnerDialog_Payment_type, spinnerDialog_Payment_banks;
    CardView cv_buildinglicencefee, cv_check_payment_details;
    double totalDemandAmount;
    String CARD_TYPE, PAYMENT_OPTION;
    double transactionCost = 0, amountToBePaid = 0;
    double servicetax = 0;
    Intent intent;
    String serviceTax_temp = "";
    String mMail, mMobileno, mDistrict, mPanchayat, mApplicationreferenceno;
    String muserid, mtitle, mfirstname, mlastname, mcontactno, mstreetname, mcityname, mstate, mpincode, maddress, memail, mcountry;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    String finyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway__online_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(R.string.payment_gateway_online);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        muserid = sharedPreference.getString(pref_login_userid, "");
        mtitle = sharedPreference.getString(pref_login_title, "");
        mfirstname = sharedPreference.getString(pref_login_firstname, "");
        mlastname = sharedPreference.getString(pref_login_lastname, "");

        mcontactno = sharedPreference.getString(pref_login_contact, "");
        mstreetname = sharedPreference.getString(pref_login_streetname, "");

        mcityname = sharedPreference.getString(pref_login_city, "");
        mstate = sharedPreference.getString(pref_login_state, "");

        mpincode = sharedPreference.getString(pref_login_pincode, "");
        maddress = sharedPreference.getString(pref_login_address, "");

        memail = sharedPreference.getString(pref_login_email, "");
        mcountry = sharedPreference.getString(pref_login_country, "");

        intent = getIntent();
        mtxt__payment_detail_user = (TextView) findViewById(R.id.tv_payment_detail_user);

        mtxt_applicationno = (TextView) findViewById(R.id.txt_application_no);
        mtxt_tradesno = (TextView) findViewById(R.id.txt_tradesno);
        mtxt_district = (TextView) findViewById(R.id.txt_district);
        mtxt_pachayat = (TextView) findViewById(R.id.txt_panchayat);
        mtxt_tradefee = (TextView) findViewById(R.id.txt_tradefee);
        mtxt_penaltyamount = (TextView) findViewById(R.id.txt_penaltyamount);
        mtxt_pfaamount = (TextView) findViewById(R.id.txt_pfaamount);
        mtxt_advt_amount = (TextView) findViewById(R.id.txt_advt_amount);
        mtxt_servicecharges = (TextView) findViewById(R.id.txt_servicecharge);
        mtxt_totalamount = (TextView) findViewById(R.id.txt_total_amount);
        mtxt_demand_amount = (TextView) findViewById(R.id.pay_demand_amount);
        mtxt_trans_amount = (TextView) findViewById(R.id.pay_transcost_amount);
        mtxt_gst_amount = (TextView) findViewById(R.id.pay_gst_amount);
        mtxt_bank_charges = (TextView) findViewById(R.id.pay_bankcharges_amount);
        mtxt_total_amounttobepaid = (TextView) findViewById(R.id.pay_total_amount);
        medt_payment_type = (TextInputEditText) findViewById(R.id.et_payment_type);
        medt_paymentbank = (TextInputEditText) findViewById(R.id.et_payment_banks);
        cv_buildinglicencefee = (CardView) findViewById(R.id.buildinglicencefee);
        cv_check_payment_details = (CardView) findViewById(R.id.check_payment_details);
        rootlayout = (LinearLayout) findViewById(R.id.rootlayout);
        msubmit = (Button) findViewById(R.id.submit);

        getCCAvenue_MerchantDetails();
        getCCAvenueRateDetailsList();
        getCCAvenue_MailCredential();

        mclear = (Button) findViewById(R.id.clear);

        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medt_payment_type.getText().toString().isEmpty()) {

                    Snackbar.make(rootlayout, "Enter Payment Type", Snackbar.LENGTH_SHORT).show();

                    return;
                }
                if (medt_paymentbank.getText().toString().isEmpty()) {

                    Snackbar.make(rootlayout, "Enter Payment Bank", Snackbar.LENGTH_SHORT).show();

                    return;
                }
                if (Common.isNetworkAvailable(getApplicationContext())) {
                    getCCAvenueOnlineEntry();


                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();


            }
        });
        mclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medt_payment_type.setText("");
                medt_paymentbank.setText("");


                mtxt_gst_amount.setText("0.00");
                mtxt_trans_amount.setText("0.00");
                mtxt_total_amounttobepaid.setText("0.00");


            }
        });

        medt_payment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerDialog_Payment_type.showSpinerDialog();
                medt_paymentbank.setText("");
            }
        });

        medt_paymentbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (medt_payment_type.getText().toString().isEmpty()) {

                    Snackbar.make(rootlayout, "Enter Payment Type", Snackbar.LENGTH_SHORT).show();

                    return;
                }
                servicetax = 0;
                mtxt_gst_amount.setText("0.00");
                mtxt_trans_amount.setText("0.00");
                mtxt_total_amounttobepaid.setText("0.00");

                spinnerDialog_Payment_banks.showSpinerDialog();

            }
        });
        mPaymentTypeList.add("CreditCard");
        mPaymentTypeList.add("DebitCard");
        mPaymentTypeList.add("NetBanking");
        mPaymentTypeList.add("CashCard");
        mPaymentTypeList.add("MobilePayment");
        mPaymentTypeList.add("Wallet");

        spinnerDialog_Payment_type = new SpinnerDialog(PaymentGateway_OnlinePayment.this, mPaymentTypeList, " Search Payment Type", "Close");// With No Animation

        spinnerDialog_Payment_type.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                mtxt_gst_amount.setText("0.00");
                mtxt_trans_amount.setText("0.00");
                mtxt_total_amounttobepaid.setText("0.00");
                medt_paymentbank.setText("");

                serviceTax_temp = "";
                medt_payment_type.setText(item);
                mPaymentTypeBanks.clear();

                for (Map.Entry<String, String> entry : mBankHashTable.entrySet()) {

                    if (entry.getValue().equals(medt_payment_type.getText().toString().trim())) {
                        Log.e(TAG, "District Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());

                        mPaymentTypeBanks.add(entry.getKey());

                    }

                }

                switch (item) {
                    case "CreditCard":
                        CARD_TYPE = "CRDC";
                        PAYMENT_OPTION = "OPTCRDC";
                        break;
                    case "DebitCard":
                        CARD_TYPE = "DBCRD";
                        PAYMENT_OPTION = "OPTDBCRD";
                        break;
                    case "NetBanking":
                        CARD_TYPE = "NBK";
                        PAYMENT_OPTION = "OPTNBK";
                        break;
                    case "CashCard":
                        CARD_TYPE = "CASHC";
                        PAYMENT_OPTION = "OPTCASHC";
                        break;
                    case "MobilePayment":
                        CARD_TYPE = "MOBP";
                        PAYMENT_OPTION = "OPTMOBP";
                        break;
                    case "Wallet":
                        CARD_TYPE = "WLT";
                        PAYMENT_OPTION = "OPTWLT";
                        break;
                }

            }
        });

        spinnerDialog_Payment_banks = new SpinnerDialog(PaymentGateway_OnlinePayment.this, mPaymentTypeBanks, "Search Payment Banks", "Close");// With No Animation
        spinnerDialog_Payment_banks.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                transactionCost = 0;
                amountToBePaid = 0;
                medt_paymentbank.setText(item);

                plusCharges(medt_payment_type.getText().toString().trim());
            }
        });
        mtxt_building_amount = findViewById(R.id.txt_building_amount);

        if (intent != null) {
            mIntent_Type = intent.getStringExtra("OnlinePaymentType");
            mIntent_TotalAmount = intent.getStringExtra("TotalAmount");
            totalDemandAmount = Double.parseDouble(mIntent_TotalAmount);

            switch (mIntent_Type) {
                case "Building":
                    mtxt_building_amount.setText(mIntent_TotalAmount);
                    mtxt_totalamount.setText(mIntent_TotalAmount);
                    mtxt_demand_amount.setText(mIntent_TotalAmount);
                    //  totalDemandAmount = Double.parseDouble(mtxt_building_amount.getText().toString());
                    cv_check_payment_details.setVisibility(View.GONE);
                    cv_buildinglicencefee.setVisibility(View.VISIBLE);
                    mtxt__payment_detail_user.setVisibility(View.VISIBLE);


                    TaxType = "Building";
                    mtxt__payment_detail_user.setText("Hi! " + intent.getStringExtra("user") + " Your Application No : " + intent.getStringExtra("ApplicationRefNo") + " Applicant Name :" + intent.getStringExtra("applicantname"));

                    break;
                case "Trade":
                    mtxt_totalamount.setText(mIntent_TotalAmount);
                    mtxt_demand_amount.setText(mIntent_TotalAmount);

                    mtxt_building_amount.setText(mIntent_TotalAmount);

                    TaxType = "Trade";

                    cv_check_payment_details.setVisibility(View.VISIBLE);
                    cv_buildinglicencefee.setVisibility(View.GONE);
                    mtxt__payment_detail_user.setVisibility(View.GONE);


                    mDistrict = intent.getStringExtra("District");
                    mPanchayat = intent.getStringExtra("Panchayat");
                    mtxt_applicationno.setText(Html.fromHtml("<b>Application No :</b>" + intent.getStringExtra("Onlineapplicationno")));
                    mApplicationreferenceno = intent.getStringExtra("Onlineapplicationno");
                    mtxt_district.setText(Html.fromHtml("<b>District :</b>" + intent.getStringExtra("District")));
                    mtxt_pachayat.setText(Html.fromHtml("<b>Town Panchayat :</b>" + intent.getStringExtra("Panchayat")));
                    mtxt_pfaamount.setText(Html.fromHtml("<b>PFA Amount :</b>" + intent.getStringExtra("PFA")));
                    mtxt_advt_amount.setText(Html.fromHtml("<b>Advt Amount :</b>" + intent.getStringExtra("Advertisement")));
                    mtxt_servicecharges.setText(Html.fromHtml("<b>Service Charge :</b>" + intent.getStringExtra("ServiceCharge")));
                    mtxt_penaltyamount.setText(Html.fromHtml("<b>Penalty Amount :</b>" + intent.getStringExtra("Penalty")));
                    mtxt_tradesno.setText(Html.fromHtml("<b>Trades No:</b>" + intent.getStringExtra("TradersCode")));
                    mtxt_tradefee.setText(Html.fromHtml("<b>Trade Fee :</b>" + intent.getStringExtra("TradeRate")));
                    //    mtxt_totalamount.setText(Html.fromHtml("<b>Total Amount :</b>" + mIntent_TotalAmount));

                     finyear = intent.getStringExtra("Finyear");

                     Log.e("findddd",""+finyear);

                    mMail = intent.getStringExtra("Mail");

                    mMobileno = intent.getStringExtra("Mobileno");

                    break;
            }
        }

    }

    //This method is for getting CCAvenue rate details list
    private void getCCAvenueRateDetailsList() {

        waitingDialog = new SpotsDialog(PaymentGateway_OnlinePayment.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";


        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_CCAVUENUERATEDETAILSLIST, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.e(TAG, response.toString());


                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);
                            CC_debitAmount = district_jsonObject.getString("debitAmount");
                            CC_hdfc_NetBank = district_jsonObject.getString("hdfc_NetBank");
                            CC_cashCard = district_jsonObject.getString("cashCard");
                            CC_serviceTax = district_jsonObject.getString("serviceTax");
                            CC_bankCharges = district_jsonObject.getString("bankCharges");
                            CC_effectiveDate = district_jsonObject.getString("effectiveDate");
                            CC_rateStatus = district_jsonObject.getString("rateStatus");
                            CC_master_Creditcard = district_jsonObject.getString("master_Creditcard");
                            CC_amex_Creditcard = district_jsonObject.getString("amex_Creditcard");
                            CC_master_DebitCard_2KBelow = district_jsonObject.getString("master_DebitCard_2KBelow");
                            CC_master_DebitCard_2KAbove = district_jsonObject.getString("master_DebitCard_2KAbove");
                            CC_others_NetBank = district_jsonObject.getString("others_NetBank");


                            double roundofbank = Math.abs(Double.parseDouble(CC_bankCharges));

                            mtxt_bank_charges.setText(String.format("%.2f", roundofbank));


                        }
                        getCCAvenuegetBankAll();

                    } else Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();


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
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);


    }

    //This method is for adding additional charges
    public void plusCharges(String sPaymentMode) {

        switch (sPaymentMode) {
            case "---select Payment mode---":
                transactionCost = 0;
                break;
            case "CreditCard":
                if (medt_paymentbank.getText().toString().equals("Amex") || medt_paymentbank.getText().toString().equals("Amex ezeClick")) {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_amex_Creditcard)) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_master_Creditcard)) / 100;
                }

                break;
            case "DebitCard":
                if (totalDemandAmount <= 2000) {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_master_DebitCard_2KBelow)) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_master_DebitCard_2KAbove)) / 100;
                }
                break;
            case "NetBanking":
                if (medt_paymentbank.getText().toString().equals("HDFC Bank")) {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_hdfc_NetBank)) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_others_NetBank)) / 100;
                }
                break;

            default:
                transactionCost = (totalDemandAmount * Double.parseDouble(CC_cashCard)) / 100;
                break;
        }


        serviceTax_temp = String.valueOf(transactionCost * Double.parseDouble(CC_serviceTax) / 100);
        servicetax = transactionCost * Double.parseDouble(serviceTax_temp) / 100;
        Log.e(TAG, transactionCost + "---" + serviceTax_temp);

        amountToBePaid = Double.parseDouble(String.valueOf(totalDemandAmount)) +
                Double.parseDouble(String.valueOf(transactionCost)) +
                Double.parseDouble(serviceTax_temp) +
                Double.parseDouble(CC_bankCharges);


        mtxt_demand_amount.setText(String.format("%.2f", totalDemandAmount));
        mtxt_gst_amount.setText(String.format("%.2f", servicetax));
        mtxt_trans_amount.setText(String.format("%.2f", transactionCost));
        mtxt_total_amounttobepaid.setText(String.format("%.2f", amountToBePaid));

    }

    //This method is for geting list of all banks from CCAvenue
    private void getCCAvenuegetBankAll() {

        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_CCAVUENUE_GET_ALLBANKS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.e(TAG, response.toString());


                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);
                            CC_bankName = district_jsonObject.getString("bankName");
                            CC_paymentmode = district_jsonObject.getString("paymentmode");

                            mBankHashTable.put(CC_bankName, CC_paymentmode);
                            mBankNameList.add(CC_bankName);
                        }


                    } else Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);


    }

    //This method is for getting CCAvenue online entry
    private void getCCAvenueOnlineEntry() {

        waitingDialog = new SpotsDialog(PaymentGateway_OnlinePayment.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_CCAVENUEENTRYNO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e(TAG, "random code" + response);


                mOnline_Entry_Value = response;
                try {
                    getCCAvenue_getDataAcceptNew();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, REQUEST_TAG);

    }

    //This method is for getting CCAvenue merchant details
    private void getCCAvenue_MerchantDetails() {

        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_CCAVENUE_MERCHANTACCESSCODE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.e(TAG, response.toString());


                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);
                            AccessCode_CC_Avuenue = district_jsonObject.getString("accessCode");
                            Common.CURRENCY_CC_Avuenue = district_jsonObject.getString("curreny");
                            MERCHANTID_CC_Avuenue = district_jsonObject.getString("merchantId");

                            Log.e(TAG, AccessCode_CC_Avuenue + "---" + MERCHANTID_CC_Avuenue);
                        }


                    } else Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);


    }

    //This method is for getting CCAvenue mail credentials
    private void getCCAvenue_MailCredential() {

        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_CCAVENUE_MAILCREDENTIAL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.e(TAG, response.toString());


                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);
                            Common.MAIL_CREDENTIAL_PASSWORD = district_jsonObject.getString("password");
                            Common.MAIL_CREDENTIAL_MAILID = district_jsonObject.getString("emailId");
                            Common.MAIL_CREDENTIAL_CONTACTNUMBER = district_jsonObject.getString("contactNo");

                            Log.e(TAG, AccessCode_CC_Avuenue + "---" + MERCHANTID_CC_Avuenue);
                        }


                    } else Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);


    }

    //This method is for getting CCAvenue data new accept
    private void getCCAvenue_getDataAcceptNew() throws UnsupportedEncodingException {

        String REQUEST_TAG = "apiDistrictDetails_Request";
        String payment_type = URLEncoder.encode(medt_payment_type.getText().toString(), "UTF-8");
        String payment_bankname = URLEncoder.encode(medt_paymentbank.getText().toString(), "UTF-8");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_CCAVENUE_CHECK_BANK + "PaymentMode=" + payment_type + "&BankName=" + payment_bankname + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                mOnline_Bank_Status = response;
                waitingDialog.dismiss();
                Log.e(TAG, "status of bank" + response);


                if (!mOnline_Bank_Status.isEmpty() && !mOnline_Entry_Value.isEmpty())

                {

                    postToAvenues();


                    Snackbar.make(rootlayout, "Found" + mOnline_Bank_Status + mOnline_Entry_Value, Snackbar.LENGTH_SHORT).show();

                } else

                    Snackbar.make(rootlayout, "Bank and Token not Found", Snackbar.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, REQUEST_TAG);

    }

    //This method is for showing anack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for posting values to CCAvenue
    public void postToAvenues() {

        Intent intent_send = new Intent(this, WebViewActivity.class);

        intent_send.putExtra(AvenuesParams.BILLING_NAME, mfirstname);
        intent_send.putExtra(AvenuesParams.BILLING_ADDRESS, maddress);
        intent_send.putExtra(AvenuesParams.BILLING_COUNTRY, mcountry);
        intent_send.putExtra(AvenuesParams.BILLING_STATE, mstate);
        intent_send.putExtra(AvenuesParams.BILLING_CITY, mcityname);
        intent_send.putExtra(AvenuesParams.BILLING_ZIP, mpincode);
        //user mail id
        intent_send.putExtra(AvenuesParams.DELIVERY_NAME, mfirstname);
        intent_send.putExtra(AvenuesParams.DELIVERY_ADDRESS, maddress);
        intent_send.putExtra(AvenuesParams.DELIVERY_COUNTRY, mcountry);
        intent_send.putExtra(AvenuesParams.DELIVERY_STATE, mstate);
        intent_send.putExtra(AvenuesParams.DELIVERY_CITY, mcityname);
        intent_send.putExtra(AvenuesParams.DELIVERY_ZIP, mpincode);
        intent_send.putExtra(AvenuesParams.DELIVERY_TEL, mcontactno);   //user Contactno id
        intent_send.putExtra(AvenuesParams.ORDER_ID, mOnline_Entry_Value);


        intent_send.putExtra(AvenuesParams.CARD_NUMBER, "");
        intent_send.putExtra(AvenuesParams.EXPIRY_YEAR, "");
        intent_send.putExtra(AvenuesParams.EXPIRY_MONTH, "");
        intent_send.putExtra(AvenuesParams.CVV, "");

        intent_send.putExtra(AvenuesParams.REDIRECT_URL, Common.URL_REDIRECT);
        intent_send.putExtra(AvenuesParams.CANCEL_URL, Common.URL_CANCEL);
        intent_send.putExtra(AvenuesParams.RSA_KEY_URL, Common.URL_RSA_KEY);
        intent_send.putExtra(AvenuesParams.PAYMENT_OPTION, PAYMENT_OPTION.trim());
        intent_send.putExtra(AvenuesParams.ISSUING_BANK, medt_paymentbank.getText().toString());
        intent_send.putExtra(AvenuesParams.CARD_TYPE, CARD_TYPE);
        intent_send.putExtra(AvenuesParams.CARD_NAME, "");
        intent_send.putExtra(AvenuesParams.DATA_ACCEPTED_AT, mOnline_Bank_Status);  //y
        intent_send.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "Nil");
        intent_send.putExtra(AvenuesParams.CURRENCY, CURRENCY_CC_Avuenue);
        intent_send.putExtra(AvenuesParams.SAVE_CARD, "N");
        intent_send.putExtra(AvenuesParams.ISSUING_BANK, medt_paymentbank.getText().toString());
        intent_send.putExtra(Params.orderId, mOnline_Entry_Value);
        intent_send.putExtra(AvenuesParams.ACCESS_CODE, Common.AccessCode_CC_Avuenue);
        intent_send.putExtra(AvenuesParams.MERCHANT_ID, Common.MERCHANTID_CC_Avuenue);


        switch (mIntent_Type) {
            case "Building":


                intent_send.putExtra(BILLING_EMAIL, intent.getStringExtra(BILLING_EMAIL));
                intent_send.putExtra(BILLING_TEL, intent.getStringExtra(BILLING_TEL));
                intent_send.putExtra(Params.userId, intent.getStringExtra(Params.userId));
                intent_send.putExtra(AvenuesParams.AMOUNT, String.valueOf(mtxt_total_amounttobepaid.getText().toString()));
                intent_send.putExtra(Params.panchayatName, intent.getStringExtra(Params.panchayatName));
                intent_send.putExtra("ApplicationRefNo", intent.getStringExtra("ApplicationRefNo"));
                intent_send.putExtra(Params.districtName, intent.getStringExtra(Params.districtName));


                break;
            case "Trade":

                intent_send.putExtra(BILLING_TEL, mMobileno); ////user Contactno id
                intent_send.putExtra(BILLING_EMAIL, mMail);
                intent_send.putExtra(Params.userId, muserid);
                intent_send.putExtra(AvenuesParams.AMOUNT, String.valueOf(mtxt_total_amounttobepaid.getText().toString())); //
                intent_send.putExtra(Params.panchayatName, mPanchayat);
                intent_send.putExtra("ApplicationRefNo", mApplicationreferenceno);
                intent_send.putExtra(Params.districtName, mDistrict);
                intent_send.putExtra(Params.financialYear, finyear);

                intent_send.putExtra("demandAmount", String.valueOf(totalDemandAmount));


                break;
        }


        intent_send.putExtra(Params.taxType, TaxType);


        getTransactionBeforeSaveDetails(intent_send);
    }

    //This method is for saving transaction details befor transaction
    private void getTransactionBeforeSaveDetails(final Intent intent) {

        waitingDialog = new SpotsDialog(PaymentGateway_OnlinePayment.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "getTransactionBeforeSaveDetails";

        JsonObjectRequest getTransaction = new JsonObjectRequest(API_ONLINE_TRADE_ONLINEBEFORETRANSACTIONDETAILS + "OrderNo=" + ORDER_ID + "&District=" + mDistrict + "&Panchayat=" + mPanchayat + "&ApplicationRefNo=" + mApplicationreferenceno + "&FinalTax=" + AMOUNT + "&PaymentOption=" + CARD_TYPE + "&ContactNo=" + BILLING_TEL + "&EmailId=" + BILLING_EMAIL + "&TotalAmount=" + mIntent_TotalAmount + "&EntryUserId=" + userId + "&EntryType=A", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    Log.e(TAG, "" + response);


                    String code = response.getString("code");

                    String message = response.getString("message");


                    waitingDialog.dismiss();
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    waitingDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());


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

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getTransaction, REQUEST_TAG);

    }


}
