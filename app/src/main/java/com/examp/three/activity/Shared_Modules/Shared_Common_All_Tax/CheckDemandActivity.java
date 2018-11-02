package com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.paymentgateway.activity.WebViewActivity;
import com.examp.three.activity.paymentgateway.utility.AvenuesParams;
import com.examp.three.activity.paymentgateway.utility.Params;
import com.examp.three.common.Common;
import com.examp.three.model.SharedBean.GetDemandEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_EMAIL;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_TEL;
import static com.examp.three.common.Common.API_CCAVENUE_CHECK_BANK;
import static com.examp.three.common.Common.API_CCAVENUE_MERCHANTACCESSCODE;
import static com.examp.three.common.Common.API_CCAVUENUERATEDETAILSLIST;
import static com.examp.three.common.Common.API_CCAVUENUE_GET_ALLBANKS;
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
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class CheckDemandActivity extends AppCompatActivity {

    private ArrayList<String> mPaymentTypeList = new ArrayList<String>();
    private ArrayList<String> mPaymentTypeBanks = new ArrayList<String>();

    String orderId, sDistrictName, sPanchayatName, sTaxType, sAssessmentNumber, sAssessmentName, sBankName, sPaymentMode = "---select Payment mode---",
            PAYMENT_OPTION, CARD_TYPE, sCardNumber, sExpMonth, sExpYear, sCVV, sDoorNo, sStreetName, sWardNo,
            sUserId,  sContactNum, sEmail, sFinancialYear = "", sSno = "", sTerm = "",
            sCessAmount = "", sMaintenanceAmount = "", sWaterCharges = "", sPenaltyAmount = "";
    private String mOnline_Entry_Value, mOnline_Bank_Status;

    String sSWMAmount = "", sPropertyTax = "", sSWMSno = "", sSWMmonth = "", sSWMFinalMonth = "", sSWMFinalSno = "";

    TextView tvTotalDemand, tvPenalty, tvTotalDemandAmount, tvTransactionCost, tvServiceCharge, tvBankCharge,
            tvTotalAmtToBePaid, tvTaxType, tvAssessmentNo, tvDistrict, tvAssessmentName, tvPanchayat;  // tvCardTitle
    double totalDemand = 0, totalDemandAmount = 0, transactionCost = 0, serviceCharge = 0,
            bankCharges = 0, amountToBePaid = 0, penaltyAmount = 0, sSWMAmounttemp = 0, prpertyamt = 0; // penalty = 0,
    //    Table design declaration
    TableLayout tableLayoutTerm;
    TableRow.LayoutParams layoutParams;

    float totalBalance = 0;
    CheckBox[] checkBoxes;
    CheckBox checkBoxAll;

    private static String TAG = CheckDemandActivity.class.getSimpleName();

    int iBox;
    ArrayList<GetDemandEntity> demandEntityList = new ArrayList<>();
    TableRow tableRow;

    Double Amex_Creditcard;

    private HashMap<String, String> mBankHashTable = new LinkedHashMap<>();
    private ArrayList<String> mBankNameList = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    LinearLayout rootlayout;

    SpinnerDialog spinnerDialog_Payment_type, spinnerDialog_Payment_banks;

    TextInputEditText medt_payment_type, medt_paymentbank;

    String CC_debitAmount, CC_hdfc_NetBank, CC_cashCard, CC_serviceTax, CC_bankCharges,
            CC_effectiveDate, CC_rateStatus, CC_master_Creditcard, CC_amex_Creditcard,
            CC_master_DebitCard_2KBelow, CC_master_DebitCard_2KAbove, CC_others_NetBank, CC_bankName, CC_paymentmode;

    SpotsDialog waitingDialog;


    String muserid, mtitle, mfirstname, mlastname, mcontactno, mstreetname, mcityname, mstate, mpincode, maddress,
            memail, mcountry;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_amount);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        sDistrictName = bundle.getString(Params.districtName);
        sPanchayatName = bundle.getString(Params.panchayatName);
        sTaxType = bundle.getString(Params.taxType);
        sAssessmentNumber = bundle.getString(Params.assessmentNumber);

        sAssessmentName = bundle.getString(Params.assessmentName);
        sDoorNo = bundle.getString(Params.doorNo);
        sStreetName = bundle.getString(Params.streetName);
        sWardNo = bundle.getString(Params.wardNo);
        sUserId = bundle.getString(Params.userId);

        sContactNum = bundle.getString(Params.contactNo);
        sEmail = bundle.getString(Params.emailId);

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

        initToolbar();

        medt_payment_type = (TextInputEditText) findViewById(R.id.et_payment_type);
        medt_paymentbank = (TextInputEditText) findViewById(R.id.et_payment_banks);
        rootlayout = (LinearLayout) findViewById(R.id.rootLayout);
        tvBankCharge = (TextView) findViewById(R.id.textView_bank_charges);
        tvTotalDemand = (TextView) findViewById(R.id.textView_totalDemand);
        tvAssessmentNo = (TextView) findViewById(R.id.textView_assessmentNo);
        tvAssessmentName = (TextView) findViewById(R.id.textView_assessmentName);
        tvDistrict = (TextView) findViewById(R.id.textView_district);
        tvPanchayat = (TextView) findViewById(R.id.textView_panchayat);
        tvPenalty = (TextView) findViewById(R.id.textView_penalty);
        tvTotalDemandAmount = (TextView) findViewById(R.id.textView_totalDemand_withPenalty);
        tvTransactionCost = (TextView) findViewById(R.id.textView_Transaction_Cost);
        tvServiceCharge = (TextView) findViewById(R.id.textView_service_charge);
        tvBankCharge = (TextView) findViewById(R.id.textView_bank_charges);
        tvTotalAmtToBePaid = (TextView) findViewById(R.id.textView_totalAmt_tobe_paid);
        tvTaxType = (TextView) findViewById(R.id.textView_taxType);

        tvAssessmentNo.setText("Assessment No : " + sAssessmentNumber);
        tvDistrict.setText("District : " + sDistrictName);
        tvPanchayat.setText("Panchayat : " + sPanchayatName);

        getCCAvenue_MerchantDetails();
        getCCAvenueRateDetailsList();

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
                spinnerDialog_Payment_banks.showSpinerDialog();

            }
        });

        mPaymentTypeList.add("CreditCard");
        mPaymentTypeList.add("DebitCard");
        mPaymentTypeList.add("NetBanking");
        mPaymentTypeList.add("CashCard");
        mPaymentTypeList.add("MobilePayment");
        mPaymentTypeList.add("Wallet");

        spinnerDialog_Payment_type = new SpinnerDialog(CheckDemandActivity.this, mPaymentTypeList, " Search Payment Type", "Close");// With No Animation

        spinnerDialog_Payment_type.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                sPaymentMode = item;

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

        spinnerDialog_Payment_banks = new SpinnerDialog(CheckDemandActivity.this, mPaymentTypeBanks, "Search Payment Banks", "Close");// With No Animation
        spinnerDialog_Payment_banks.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                transactionCost = 0;
                amountToBePaid = 0;
                medt_paymentbank.setText(item);

                sBankName = item;

                plusCharges(totalDemand);
//
            }
        });

//        Table design
        tableLayoutTerm = (TableLayout) findViewById(R.id.tableLayout_term);

        tableRow = (TableRow) findViewById(R.id.row);
        layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(7, -7, 15, 15);

        switch (sTaxType) {
            case "P":

                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                    getpropertyDemand();
                } else {
                    alertBox1();
                }
                break;
            case "PR":

                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                    getprofessionalDemand();
                } else {
                    alertBox1();
                }
                break;
            case "W":

                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                    getWaterBalanceDetails();
                } else {
                    alertBox1();
                }
                break;
            case "N":

                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                    getNonTaxBalanceDetails();
                } else {
                    alertBox1();
                }
                break;
            default:
                break;
        }
    }

    //This method is for initializing toolbar
    private void initToolbar() {

        setSupportActionBar(mtoolbar);

        if (sTaxType.equalsIgnoreCase("P")) {
            getSupportActionBar().setTitle("Property Tax");

        } else if (sTaxType.equalsIgnoreCase("PR")) {
            getSupportActionBar().setTitle("Profession Tax");

        } else if (sTaxType.equalsIgnoreCase("W")) {
            getSupportActionBar().setTitle("Water Charges");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    //This method is for redirecting to check demands based on types
    public void alertBox1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckDemandActivity.this);
//        builder.setTitle("Registration successful");
        builder.setMessage("Please connect to Internet and try again, do not go back.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (sTaxType) {
                            case "P":
                                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                                    getpropertyDemand();
                                } else {
                                    alertBox1();
                                }
                                break;
                            case "PR":
                                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                                    getprofessionalDemand();
                                } else {
                                    alertBox1();
                                }
                                break;
                            case "W":
                                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                                    getWaterBalanceDetails();
                                } else {
                                    alertBox1();
                                }
                                break;
                            case "N":
                                if (Common.isNetworkAvailable(CheckDemandActivity.this)) {
                                    getNonTaxBalanceDetails();
                                    break;
                                } else {
                                    alertBox1();
                                }
                            default:

                                break;
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //This method is for getting CCAvenue rate list
    private void getCCAvenueRateDetailsList() {

        waitingDialog = new SpotsDialog(CheckDemandActivity.this);
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
                            tvBankCharge.setText(String.format("%.2f", roundofbank));

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

    //This method is for getting bank all list
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

                //  waitingDialog.dismiss();
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

    //This method is for showing snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for payment
    public void payment(View view) {

        sCardNumber = "";
        sExpMonth = "";
        sExpYear = "";
        sCVV = "";

        if (amountToBePaid <= 0) {
            showToast("Please select atleast one demand");
            return;
        }
        if (medt_payment_type.getText().toString().isEmpty()) {
            showToast("Select Payment Mode");

            return;
        }
         if (medt_paymentbank.getText().toString().isEmpty()) {
             showToast("Select Payment Bank");
            return;

        }
        if (Common.isNetworkAvailable(getApplicationContext())) {
            getOrderId();
        } else
            showToast("No Internet Connection");

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //This method is for retrieving non tax balance details
    private void getNonTaxBalanceDetails() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_NONTAXBAL_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(CheckDemandActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS
                + "?" + "district=" + sDistrictName + Common.PARAMETER_SEPERATOR_AMP + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
//                            System.out.println("jsonArray = " + jsonArray);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString(Params.financialYear));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));
                                        getDemandEntity.setSwaAmount(jsonObject.getString(Params.cSWAAmount));
                                        getDemandEntity.setPropertyTax(jsonObject.getString(Params.cProprtyTax));
                                        demandEntityList.add(getDemandEntity);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                loadDataInTable();
                            } else {
                                alertBox();
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
    }

    //This method is for retrieving water tax balance details
    private void getWaterBalanceDetails() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_WATERBAL_DETAILS;

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ProgressDialog dialog = new ProgressDialog(CheckDemandActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS + "?"
                + "district=" + sDistrictName + "&" + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
//                            if (!jsonArray.equals("[]")) {
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString("finYear"));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));
                                        getDemandEntity.setCessAmount(jsonObject.getString(Params.cessAmount));
                                        getDemandEntity.setWaterCharges(jsonObject.getString(Params.waterCharges));
                                        getDemandEntity.setMaintenanceCharge(jsonObject.getString(Params.maintenanceCharge));
                                        getDemandEntity.setPenaltyAmount(jsonObject.getString(Params.penaltyAmount));
                                        demandEntityList.add(getDemandEntity);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                loadDataInTable();
                            } else {
                                alertBox();
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //This method is for getting property tax demand details
    public void getpropertyDemand() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_PPBAL_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(CheckDemandActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS + "?"
                + "district=" + sDistrictName + "&" + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber + Common.PARAMETER_SEPERATOR_AMP
                + "taxType=" + sTaxType,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString(Params.financialYear));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));
                                        getDemandEntity.setSwaAmount(jsonObject.getString(Params.cSWAAmount));
                                        getDemandEntity.setPropertyTax(jsonObject.getString(Params.cProprtyTax));
                                        getDemandEntity.setSwmMonth(jsonObject.getString(Params.cSWMMonth));
                                        getDemandEntity.setSwmSno(jsonObject.getString(Params.cSWMSno));

                                        demandEntityList.add(getDemandEntity);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                tableTitlePrperty();

                            } else {
                                alertBox();
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //This method is for showing alert if no balance amount to be paid
    public void alertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckDemandActivity.this);
        builder.setMessage("No balance amount to be paid");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //This method is for retrieving prefession tax demand details
    public void getprofessionalDemand() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_PPBAL_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(CheckDemandActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = "jsonArrayrequ =" + URL_ASSESS_DETAILS
                + "district=" + sDistrictName + "&" + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber + Common.PARAMETER_SEPERATOR_AMP
                + "taxType=" + sTaxType;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS + "?"
                + "district=" + sDistrictName + "&" + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber + Common.PARAMETER_SEPERATOR_AMP
                + "taxType=" + sTaxType,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString(Params.financialYear));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));

                                        demandEntityList.add(getDemandEntity);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                loadDataInTable();

                            } else {
                                alertBox();
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void loadDataInTable() {
        tableTitle();
    }

    //This method is for loading data in the table
    public void tableTitle() {
        TableRow tableRowTitle = new TableRow(this);
        tableRowTitle.setLayoutParams(layoutParams);

        checkBoxAll = new CheckBox(this);
        checkBoxAll.setLayoutParams(layoutParams);
        checkBoxAll.setText("All");

        TextView tvFinancialYear = new TextView(this);
        tvFinancialYear.setLayoutParams(layoutParams);
        tvFinancialYear.setText("FinancialYear");
        tvFinancialYear.setTextColor(getResources().getColor(R.color.black));

        TextView tvTerm = new TextView(this);
        tvTerm.setLayoutParams(layoutParams);
        tvTerm.setText("Term");
        tvTerm.setTextColor(getResources().getColor(R.color.black));


        TextView tvBalance = new TextView(this);
        tvBalance.setLayoutParams(layoutParams);
        tvBalance.setText("Balance(INR)");
        tvBalance.setTextColor(getResources().getColor(R.color.black));

        tableRowTitle.addView(checkBoxAll);
        tableRowTitle.addView(tvFinancialYear);
        tableRowTitle.addView(tvTerm);
        tableRowTitle.addView(tvBalance);
        tableLayoutTerm.addView(tableRowTitle, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        call table values method
        tableValues();
    }

    //This method is for loading property tax data in table
    public void tableTitlePrperty() {
        TableRow tableRowTitle = new TableRow(this);
        tableRowTitle.setLayoutParams(layoutParams);

        checkBoxAll = new CheckBox(this);
        checkBoxAll.setLayoutParams(layoutParams);
        checkBoxAll.setText("");

        TextView tvFinancialYear = new TextView(this);
        tvFinancialYear.setLayoutParams(layoutParams);
        tvFinancialYear.setText("Fin\nYear");
        tvFinancialYear.setTextColor(getResources().getColor(R.color.black));

        TextView tvTerm = new TextView(this);
        tvTerm.setLayoutParams(layoutParams);
        tvTerm.setText("Term");
        tvTerm.setTextColor(getResources().getColor(R.color.black));

        TextView tvPropertyTax = new TextView(this);
        tvPropertyTax.setLayoutParams(layoutParams);
        tvPropertyTax.setText("Tax\namt");
        tvPropertyTax.setTextColor(getResources().getColor(R.color.black));

        TextView tvSwmAmount = new TextView(this);
        tvSwmAmount.setLayoutParams(layoutParams);
        tvSwmAmount.setText("SW\namt");
        tvSwmAmount.setTextColor(getResources().getColor(R.color.black));

        TextView tvBalance = new TextView(this);
        tvBalance.setLayoutParams(layoutParams);
        tvBalance.setText("Total\n(INR)");
        tvBalance.setTextColor(getResources().getColor(R.color.black));

        tableRowTitle.addView(checkBoxAll);
        tableRowTitle.addView(tvFinancialYear);
        tableRowTitle.addView(tvTerm);
        tableRowTitle.addView(tvPropertyTax);
        tableRowTitle.addView(tvSwmAmount);
        tableRowTitle.addView(tvBalance);
        tableLayoutTerm.addView(tableRowTitle, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        call table values method
        tableValuesProperty();
    }

    //This method is for loading data in table
    public void tableValuesProperty() {
        checkBoxes = new CheckBox[demandEntityList.size()];
        try {
            for (int i = 0; i < demandEntityList.size(); i++) {
                final TableRow tableRowValue = new TableRow(this);

                tableRowValue.setId(i);
                tableRowValue.setLayoutParams(layoutParams);

                checkBoxes[i] = new CheckBox(this);
                checkBoxes[i].setLayoutParams(layoutParams);
//                checkBoxes[i].setButtonDrawable(id);

                String[] stringArray = demandEntityList.get(i).getFinancialYear().split("-");

                String name = stringArray[1].substring(2, 4);

                final TextView fin = new TextView(this);
                fin.setLayoutParams(layoutParams);
                fin.setText(stringArray[0] + "-" + name);
//                fin.setTextColor(Color.WHITE);

                final TextView term = new TextView(this);
                term.setLayoutParams(layoutParams);
                term.setText(demandEntityList.get(i).getTerm());
//                term.setTextColor(Color.WHITE);

                final TextView taxamoun = new TextView(this);
                taxamoun.setLayoutParams(layoutParams);
                taxamoun.setText(demandEntityList.get(i).getBalance());
//                taxamoun.setTextColor(Color.WHITE);

                final TextView PrpertytaxAmt = new TextView(this);
                PrpertytaxAmt.setLayoutParams(layoutParams);
                PrpertytaxAmt.setText(demandEntityList.get(i).getPropertyTax());

                final TextView swmamount = new TextView(this);
                swmamount.setLayoutParams(layoutParams);
                swmamount.setText(demandEntityList.get(i).getSwaAmount());

                totalBalance = totalBalance + Float.parseFloat(demandEntityList.get(i).getBalance());

                tableRowValue.addView(checkBoxes[i]);
                tableRowValue.addView(fin);
                tableRowValue.addView(term);
                tableRowValue.addView(PrpertytaxAmt);
                tableRowValue.addView(swmamount);
                tableRowValue.addView(taxamoun);

                tableLayoutTerm.addView(tableRowValue, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkBoxValidation();
        tableTotal();
    }

    //This method is for loading data in table
    public void tableValues() {
        checkBoxes = new CheckBox[demandEntityList.size()];
        try {
            for (int i = 0; i < demandEntityList.size(); i++) {
                final TableRow tableRowValue = new TableRow(this);

                tableRowValue.setId(i);
                tableRowValue.setLayoutParams(layoutParams);

                checkBoxes[i] = new CheckBox(this);
                checkBoxes[i].setLayoutParams(layoutParams);
//                checkBoxes[i].setButtonDrawable(id);


                final TextView fin = new TextView(this);
                fin.setLayoutParams(layoutParams);
                fin.setText(demandEntityList.get(i).getFinancialYear());
//                fin.setTextColor(Color.WHITE);

                final TextView term = new TextView(this);
                term.setLayoutParams(layoutParams);
                term.setText(demandEntityList.get(i).getTerm());
//                term.setTextColor(Color.WHITE);

                final TextView taxamoun = new TextView(this);
                taxamoun.setLayoutParams(layoutParams);
                taxamoun.setText(demandEntityList.get(i).getBalance());

                totalBalance = totalBalance + Float.parseFloat(demandEntityList.get(i).getBalance());

                tableRowValue.addView(checkBoxes[i]);
                tableRowValue.addView(fin);
                tableRowValue.addView(term);
                tableRowValue.addView(taxamoun);

                tableLayoutTerm.addView(tableRowValue, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkBoxValidation();
        tableTotal();
    }

    //This method is for loading data in table
    public void tableTotal() {
        TableRow tableRowTotal = new TableRow(this);
        tableRowTotal.setLayoutParams(layoutParams);

        TextView tvEmpty = new TextView(this);
        tvEmpty.setLayoutParams(layoutParams);
        tvEmpty.setText("");

        TextView tvEmpty1 = new TextView(this);
        tvEmpty1.setLayoutParams(layoutParams);
        tvEmpty1.setText("");

        TextView tvTotalKey = new TextView(this);
        tvTotalKey.setLayoutParams(layoutParams);
        tvTotalKey.setText("Total");
        tvTotalKey.setTextColor(getResources().getColor(R.color.black));

        TextView tvTotal = new TextView(this);
        tvTotal.setLayoutParams(layoutParams);
        tvTotal.setText(String.valueOf(String.format("%.2f", totalBalance)));
        tvTotal.setTextColor(getResources().getColor(R.color.black));

        tableRowTotal.addView(tvEmpty);
        tableRowTotal.addView(tvEmpty1);
        tableRowTotal.addView(tvTotalKey);
        tableRowTotal.addView(tvTotal);
        tableLayoutTerm.addView(tableRowTotal, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    //This method is for validating check boxes
    public void checkBoxValidation() {
        checkBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAll.isChecked()) {
                    for (int i = 0; i < demandEntityList.size(); i++) {
                        checkBoxes[i].setEnabled(true);
                        checkBoxes[i].setChecked(true);
                    }
                } else {
                    for (int i = 0; i < demandEntityList.size(); i++) {
                        checkBoxes[i].setChecked(false);
                        checkBoxes[i].setId(i);
                        if (checkBoxes[i].getId() == 0) {
                            checkBoxes[i].setEnabled(true);
                        } else {
                            checkBoxes[i].setEnabled(false);
                        }
                    }
                }
            }
        });

        for (iBox = 0; iBox < demandEntityList.size(); iBox++) {

            checkBoxes[iBox].setId(iBox);
            if (checkBoxes[iBox].getId() == 0) {
                checkBoxes[iBox].setEnabled(true);
            } else {
                checkBoxes[iBox].setEnabled(false);
            }

//            Check Change Listener
            checkBoxes[iBox].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (int j = 0; j < demandEntityList.size() - 1; j++) {
                            if (checkBoxes[j].isChecked()) {
                                checkBoxes[j + 1].setEnabled(true);
                            }
                            if (checkBoxes[demandEntityList.size() - 1].isChecked()) {
                                checkBoxAll.setChecked(true);
                            }
                        }

                        sFinancialYear = sFinancialYear + "," + demandEntityList.get(buttonView.getId()).getFinancialYear();
                        sSno = sSno + "," + demandEntityList.get(buttonView.getId()).getsNo();
                        sTerm = sTerm + "," + demandEntityList.get(buttonView.getId()).getTerm();

                        if (sTaxType.equalsIgnoreCase("P")) {
                            if (Double.parseDouble(demandEntityList.get(buttonView.getId()).getSwaAmount()) > 0) {
                                sSWMSno = sSWMSno + "." + demandEntityList.get(buttonView.getId()).getSwmSno();
                                sSWMmonth = sSWMmonth + "." + demandEntityList.get(buttonView.getId()).getSwmMonth();

                                sSWMFinalMonth = sSWMmonth.replace(".", ",");
                                sSWMFinalSno = sSWMSno.replace(".", ",");

                            } else {
                                sSWMSno = "";
                                sSWMmonth = "";
                            }
                        } else {
                            sSWMSno = "";
                            sSWMmonth = "";
                        }

                        sCessAmount = sCessAmount + "," + demandEntityList.get(buttonView.getId()).getCessAmount();
                        sMaintenanceAmount = sMaintenanceAmount + "," + demandEntityList.get(buttonView.getId()).getMaintenanceCharge();
                        sPenaltyAmount = sPenaltyAmount + "," + demandEntityList.get(buttonView.getId()).getPenaltyAmount();
                        sWaterCharges = sWaterCharges + "," + demandEntityList.get(buttonView.getId()).getWaterCharges();

                        if (sTaxType.equalsIgnoreCase("P")) {
                            sSWMAmounttemp = sSWMAmounttemp + Double.parseDouble(demandEntityList.get(buttonView.getId()).getSwaAmount());
                            prpertyamt = prpertyamt + Double.parseDouble(demandEntityList.get(buttonView.getId()).getPropertyTax());

                        } else {
                            sSWMAmounttemp = 0.00;
                            prpertyamt = 0.00;
                        }

                        totalDemand = totalDemand + Double.parseDouble(demandEntityList.get(buttonView.getId()).getBalance());

                        System.out.println("22222  " + totalDemand);

                        tvTotalDemand.setText(String.valueOf(String.format("%.2f", totalDemand)));
                        if (sTaxType.equals("W")) {
                            penaltyAmount = penaltyAmount + Double.parseDouble(demandEntityList.get(buttonView.getId()).getPenaltyAmount());
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        } else {
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        }
                        plusCharges(totalDemand);
//                        Un check chekBox
                    } else {
                        for (int k = 0; k < demandEntityList.size() - 1; k++) {
                            if (!checkBoxes[k].isChecked()) {
                                checkBoxes[k + 1].setEnabled(false);
                                checkBoxes[k + 1].setChecked(false);
                            }
                            if (!checkBoxes[demandEntityList.size() - 1].isChecked()) {
                                checkBoxAll.setChecked(false);
                            }
                        }
                        //                    Slno , financialYear, term

                        sFinancialYear = sFinancialYear.substring(0, sFinancialYear.lastIndexOf(","));
                        sSno = sSno.substring(0, sSno.lastIndexOf(","));
                        sTerm = sTerm.substring(0, sTerm.lastIndexOf(","));

                        if (sTaxType.equalsIgnoreCase("P")) {
//                            if (Double.parseDouble(demandEntityList.get(buttonView.getId()).getSwaAmount()) > 0) {

                            if (sSWMSno.length() > 0) {
                                sSWMSno = sSWMSno.substring(0, sSWMSno.lastIndexOf("."));
                                sSWMmonth = sSWMmonth.substring(0, sSWMmonth.lastIndexOf("."));

                                sSWMFinalMonth = sSWMmonth.replace(".", ",");
                                sSWMFinalSno = sSWMSno.replace(".", ",");
                            }

                        } else {
                            sSWMSno = "";
                            sSWMmonth = "";
                        }
//
                        sCessAmount = sCessAmount.substring(0, sCessAmount.lastIndexOf(","));
                        sMaintenanceAmount = sMaintenanceAmount.substring(0, sMaintenanceAmount.lastIndexOf(","));
                        sPenaltyAmount = sPenaltyAmount.substring(0, sPenaltyAmount.lastIndexOf(","));
                        sWaterCharges = sWaterCharges.substring(0, sWaterCharges.lastIndexOf(","));

                        if (sTaxType.equalsIgnoreCase("P")) {
                            sSWMAmounttemp = sSWMAmounttemp - Double.parseDouble(demandEntityList.get(buttonView.getId()).getSwaAmount());
                            prpertyamt = prpertyamt - Double.parseDouble(demandEntityList.get(buttonView.getId()).getPropertyTax());


                            System.out.println("2222222" + sSWMAmounttemp);
                            System.out.println("22222" + prpertyamt);
                        } else {
                            sSWMAmounttemp = 0.00;
                            prpertyamt = 0.00;
                        }

                        // Calculation of amount

                        totalDemand = totalDemand - Double.parseDouble(demandEntityList.get(buttonView.getId()).getBalance());
                        tvTotalDemand.setText(String.valueOf(totalDemand));

                        if (sTaxType.equals("W")) {
                            penaltyAmount = penaltyAmount - Double.parseDouble(demandEntityList.get(buttonView.getId()).getPenaltyAmount());
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        } else {
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        }
                        plusCharges(totalDemand);
                    }
                }
            });
        }
    }

    //This method is for adding additional charges with total demand
    public void plusCharges(double sTotalDemand) {

        totalDemandAmount = sTotalDemand + penaltyAmount;
        tvTotalDemandAmount.setText(String.format("%.2f", totalDemandAmount));

        switch (sPaymentMode) {
            case "---select Payment mode---":
                transactionCost = 0;
                break;
            case "CreditCard":
                if (sBankName.equals("Amex") || sBankName.equals("Amex ezeClick")) {
                    Log.e("demaaaa", "" + Amex_Creditcard);
                    Log.e("demaaaa", "" + totalDemandAmount);

                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_amex_Creditcard)) / 100;

                } else {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_master_Creditcard)) / 100;
                }
                break;
            case "DebitCard":
                if (totalDemandAmount <= Double.parseDouble(CC_debitAmount)) {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_master_DebitCard_2KBelow)) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_master_DebitCard_2KAbove)) / 100;
                }
                break;
            case "NetBanking":
                if (sBankName.equals("HDFC Bank")) {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_hdfc_NetBank)) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Double.parseDouble(CC_others_NetBank)) / 100;
                }
                break;
            default:
                transactionCost = (totalDemandAmount * Double.parseDouble(CC_cashCard)) / 100;
                break;
        }

        tvTransactionCost.setText(String.format("%.2f", transactionCost));

        serviceCharge = (transactionCost * Double.parseDouble(CC_serviceTax)) / 100;
        tvServiceCharge.setText(String.format("%.2f", serviceCharge));

        if (totalDemandAmount == 0) {
            bankCharges = 0;
        } else {
            bankCharges = Double.parseDouble(CC_bankCharges);
        }
        tvBankCharge.setText(String.format("%.2f", bankCharges));

//        amountToBePaid = totalDemandAmount + transactionCost + serviceCharge + bankCharges;

        String totamt = String.format("%.2f", totalDemandAmount);
        String transactioncost = String.format("%.2f", transactionCost);
        String servicecharge = String.format("%.2f", serviceCharge);
        String bankcharge = String.format("%.2f", bankCharges);

        amountToBePaid = Double.parseDouble(totamt) +
                Double.parseDouble(transactioncost) +
                Double.parseDouble(servicecharge) +
                Double.parseDouble(bankcharge);

        tvTotalAmtToBePaid.setText(String.format("%.2f", amountToBePaid));
    }

    //This method is for getting order id for online payment
    public void getOrderId() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_ORDERID;

        final ProgressDialog dialog = new ProgressDialog(CheckDemandActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(URL_ASSESS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                orderId = s;
                System.out.println("mobileNo" + sContactNum);

                try {
                    getCCAvenue_getDataAcceptNew();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //This method is for getting CCAvenue data
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

                if (!mOnline_Bank_Status.isEmpty() && !orderId.isEmpty())

                {
                    if (sharedPreference.contains("firstName")) {

                        saveOnlineBeforeTransDetails(muserid, orderId, sTaxType, sAssessmentNumber, sDistrictName, sPanchayatName, String.valueOf(amountToBePaid), sPaymentMode, sContactNum, sEmail, "SRUser", "", "", "", "", sAssessmentName, sWardNo, "", sSno, sFinancialYear, sTerm, "", sCessAmount, sMaintenanceAmount, sPenaltyAmount, String.valueOf(totalDemandAmount), "", "", "", String.valueOf(prpertyamt), String.valueOf(sSWMAmounttemp), sSWMSno, sSWMFinalMonth, "A");

                    }else {
                        saveOnlineBeforeTransDetails("", orderId, sTaxType, sAssessmentNumber, sDistrictName, sPanchayatName, String.valueOf(amountToBePaid), sPaymentMode, sContactNum, sEmail, "SRUser", "", "", "", "", sAssessmentName, sWardNo, "", sSno, sFinancialYear, sTerm, "", sCessAmount, sMaintenanceAmount, sPenaltyAmount, String.valueOf(totalDemandAmount), "", "", "", String.valueOf(prpertyamt), String.valueOf(sSWMAmounttemp), sSWMSno, sSWMFinalMonth, "A");

                    }

                } else
                    showToast("Bank and Token not Found");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());

                waitingDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    showToast("Time out");

                } else if (error instanceof AuthFailureError) {

                    showToast("Connection Time out");

                } else if (error instanceof ServerError) {
                    showToast("Could not connect server");

                } else if (error instanceof NetworkError) {

                    showToast("Please check the internet connection");

                } else if (error instanceof ParseError) {
                    showToast("Parse Error");

                } else {

                    showToast(error.getMessage());
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

    //This method is for saving online before transaction
    public void saveOnlineBeforeTransDetails(String UserId, String OrderId, String TaxType, String TaxNo,
                                             String District, String Panchayat, String Amount, String PayMode, String MobileNo, String MailId,
                                             String LoginType, String DistrictTamil, String PanchayatTamil, String DistrictId, String PanchayatId,
                                             String TaxName, String TaxWardNo, String TaxStreetName, String TaxDemandSno, String TaxFinYear,
                                             String TaxHalfYear, String TaxQuarterAmount, String TaxCessAmount, String TaxMtnCharge,
                                             String TaxPenaltyAmount, String TaxTotalAmount, String TaxOrgName, String TaxDesignation,
                                             String TaxLeaseTypeCode, String TaxPropertyAmount, String TaxSwmAmount, String TaxSWMSno, String TaxSWMMonth, String TaxEntryType) {

        System.out.println("taaaaaaaaaaa" + TaxSWMSno);
        System.out.println("taaaaaaaaaaa" + TaxSWMMonth);

        String tempDistrict, tempPayMode, tempCardName, tempMailId,
                tempPanchayat, tempTaxType, tempLoginType, tempDistrictTamil, tempPanchayatTamil, tempTaxName, tempTaxStreetName;
        String URL_SAVE_ONLINE_TRANS = "";

        String taxsno = "", finyear = "", term = "", swmSno = "", swmMonth = "";
        try {
            tempTaxType = URLEncoder.encode(TaxType, "UTF-8");
            tempDistrict = URLEncoder.encode(District, "UTF-8");
            tempPanchayat = URLEncoder.encode(Panchayat, "UTF-8");
            tempPayMode = URLEncoder.encode(PayMode, "UTF-8");
//            tempMailId = URLEncoder.encode(MailId, "UTF-8");
            tempLoginType = URLEncoder.encode(LoginType, "UTF-8");
            tempDistrictTamil = URLEncoder.encode(DistrictTamil, "UTF-8");
            tempPanchayatTamil = URLEncoder.encode(PanchayatTamil, "UTF-8");
            tempTaxName = URLEncoder.encode(TaxName, "UTF-8");
//            tempTaxStreetName = URLEncoder.encode(TaxStreetName, "UTF-8");

            if (TaxDemandSno.length() > 0) {
                taxsno = TaxDemandSno.replaceFirst(",", "");
                finyear = TaxFinYear.replaceFirst(",", "");
                term = TaxHalfYear.replaceFirst(",", "");
                if (TaxSWMSno.length() > 0) {
                    swmSno = TaxSWMSno.replaceFirst(".", "");
                    swmMonth = TaxSWMMonth.replaceFirst(",", "");
                }
            }

            URL_SAVE_ONLINE_TRANS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_SAVE_ONLINE_BEFORE_TRANS + "?" +
                    "UserId=" + UserId + "&OrderId=" + OrderId + "&TaxType=" + tempTaxType +
                    "&TaxNo=" + TaxNo + "&District=" + tempDistrict + "&Panchayat=" + tempPanchayat +
                    "&Amount=" + Amount + "&PayMode=" + tempPayMode + "&MobileNo=" + MobileNo +
                    "&MailId=" + MailId + "&LoginType=" + tempLoginType + "&DistrictTamil=" + tempDistrictTamil +
                    "&PanchayatTamil=" + tempPanchayatTamil + "&DistrictId=" + DistrictId + "&PanchayatId=" + PanchayatId +
                    "&TaxName=" + tempTaxName + "&TaxWardNo=" + TaxWardNo + "&TaxStreetName=" + TaxStreetName +
                    "&TaxDemandSno=" + taxsno + "&TaxFinYear=" + finyear +
                    "&TaxHalfYear=" + term + "&TaxQuarterAmount=" + TaxQuarterAmount + "&TaxCessAmount=" + TaxCessAmount +
                    "&TaxMtnCharge=" + TaxMtnCharge + "&TaxPenaltyAmount=" + TaxPenaltyAmount + "&TaxTotalAmount=" + TaxTotalAmount +
                    "&TaxOrgName=" + TaxOrgName + "&TaxDesignation=" + TaxDesignation + "&TaxLeaseTypeCode=" + TaxLeaseTypeCode + "&TaxPropertyAmount=" + TaxPropertyAmount + "&TaxSwmAmount=" + TaxSwmAmount + "&TaxSWMSno=" + swmSno + "&TaxSWMMonth=" + swmMonth + "&TaxEntryType=" + TaxEntryType + "";

            System.out.println("URL_SAVE = " + URL_SAVE_ONLINE_TRANS);

            final ProgressDialog dialog = new ProgressDialog(CheckDemandActivity.this);

            dialog.setMessage("Loading...");
            dialog.show();
            dialog.setCancelable(false);

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final JsonObjectRequest request = new JsonObjectRequest(URL_SAVE_ONLINE_TRANS, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            try {

                                System.out.println(jsonObject.getString("code"));
                                if (jsonObject.getString("message").equalsIgnoreCase("updated sucessfully")) {
                                    postToAvenues();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some thing wrong check details", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    volleyError.printStackTrace();
                }
            });
            requestQueue.add(request);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method is for posting to CCAvenue
    public void postToAvenues() {

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AvenuesParams.ORDER_ID, orderId);
        intent.putExtra(AvenuesParams.ACCESS_CODE, Common.AccessCode_CC_Avuenue);
        intent.putExtra(AvenuesParams.MERCHANT_ID, Common.MERCHANTID_CC_Avuenue);

        if (sharedPreference.contains(sharedPreferenceHelpher.pref_login_firstname)) {

        intent.putExtra(AvenuesParams.BILLING_NAME, mfirstname);
        intent.putExtra(AvenuesParams.BILLING_ADDRESS, maddress);
        intent.putExtra(AvenuesParams.BILLING_COUNTRY, mcountry);
        intent.putExtra(AvenuesParams.BILLING_STATE, mstate);
        intent.putExtra(AvenuesParams.BILLING_CITY, mcityname);
        intent.putExtra(AvenuesParams.BILLING_ZIP, mpincode);
        //user mail id
        intent.putExtra(AvenuesParams.DELIVERY_NAME, mfirstname);
        intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, maddress);
        intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, mcountry);
        intent.putExtra(AvenuesParams.DELIVERY_STATE, mstate);
        intent.putExtra(AvenuesParams.DELIVERY_CITY, mcityname);
        intent.putExtra(AvenuesParams.DELIVERY_ZIP, mpincode);
        intent.putExtra(AvenuesParams.DELIVERY_TEL, mcontactno);

    } else {
        intent.putExtra(AvenuesParams.BILLING_NAME, "UnRegistered");
        intent.putExtra(AvenuesParams.BILLING_ADDRESS, "UnRegistered");
        intent.putExtra(AvenuesParams.BILLING_COUNTRY, "India");
        intent.putExtra(AvenuesParams.BILLING_STATE, "UnRegistered");
        intent.putExtra(AvenuesParams.BILLING_CITY, "UnRegistered");
        intent.putExtra(AvenuesParams.BILLING_ZIP, "UnRegistered");
        intent.putExtra(AvenuesParams.BILLING_TEL, sContactNum);
        intent.putExtra(AvenuesParams.BILLING_EMAIL, sEmail);
        intent.putExtra(AvenuesParams.DELIVERY_NAME, "UnRegistered");
        intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, "UnRegistered");
        intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, "UnRegistered");
        intent.putExtra(AvenuesParams.DELIVERY_STATE, "UnRegistered");
        intent.putExtra(AvenuesParams.DELIVERY_CITY, "UnRegistered");
        intent.putExtra(AvenuesParams.DELIVERY_ZIP, "UnRegistered");
        intent.putExtra(AvenuesParams.DELIVERY_TEL, sContactNum);
    }

        intent.putExtra(AvenuesParams.CARD_NUMBER, sCardNumber);
        intent.putExtra(AvenuesParams.EXPIRY_YEAR, sExpYear);
        intent.putExtra(AvenuesParams.EXPIRY_MONTH, sExpMonth);
        intent.putExtra(AvenuesParams.CVV, sCVV);
        intent.putExtra(AvenuesParams.REDIRECT_URL, Common.URL_REDIRECT);
        intent.putExtra(AvenuesParams.CANCEL_URL, Common.URL_CANCEL);
        intent.putExtra(AvenuesParams.RSA_KEY_URL, Common.URL_RSA_KEY);
        intent.putExtra(AvenuesParams.PAYMENT_OPTION, PAYMENT_OPTION.trim());
        intent.putExtra(AvenuesParams.ISSUING_BANK, sBankName);
        intent.putExtra(AvenuesParams.CARD_TYPE, CARD_TYPE);
        intent.putExtra(AvenuesParams.CARD_NAME, sBankName);
        intent.putExtra(AvenuesParams.DATA_ACCEPTED_AT, mOnline_Bank_Status);
        intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "Nil");
        intent.putExtra(AvenuesParams.CURRENCY, CURRENCY_CC_Avuenue);
        intent.putExtra(AvenuesParams.AMOUNT, String.valueOf(amountToBePaid));
        intent.putExtra(AvenuesParams.SAVE_CARD, "N");
        intent.putExtra(Params.assessmentNumber, sAssessmentNumber);
        intent.putExtra(Params.assessmentName, sAssessmentName);
        intent.putExtra(Params.districtName, sDistrictName);
        intent.putExtra(Params.panchayatName, sPanchayatName);
        intent.putExtra(Params.taxType, sTaxType);
        intent.putExtra(Params.doorNo, sDoorNo);
        intent.putExtra(Params.wardNo, sWardNo);
        intent.putExtra(Params.streetName, sStreetName);
        intent.putExtra(Params.sNo, sSno);
        intent.putExtra(Params.financialYear, sFinancialYear);
        intent.putExtra(Params.term, sTerm);
        intent.putExtra(Params.userId, sUserId);
        intent.putExtra(Params.cSWAAmount, String.valueOf(sSWMAmounttemp));
        intent.putExtra(Params.cProprtyTax, String.valueOf(prpertyamt));
        intent.putExtra(Params.cSWMMonth, sSWMFinalMonth);
        intent.putExtra(Params.cSWMSno, sSWMFinalSno);
        intent.putExtra(Params.cessAmount, sCessAmount);
        intent.putExtra(Params.maintenanceCharge, sMaintenanceAmount);
        intent.putExtra(Params.penaltyAmount, String.valueOf(penaltyAmount));
        intent.putExtra(Params.waterCharges, sWaterCharges);
        intent.putExtra(BILLING_TEL, sContactNum);
        intent.putExtra(BILLING_EMAIL, sEmail);
        intent.putExtra("totalDemandAmount", String.valueOf(totalDemandAmount));
        intent.putExtra("demandAmount", String.valueOf(totalDemand));
        startActivity(intent);
    }

}
