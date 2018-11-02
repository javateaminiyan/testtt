package com.examp.three.activity.Property;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.Property_Tax.PaidHostoryAdapter;
import com.examp.three.adapter.Property_Tax.PaymentHistoryAdapter;
import com.examp.three.common.Common;
import com.examp.three.model.SharedBean.TaxBalancePayment;
import com.examp.three.model.propertytax.PaymenyHistoryBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_DISTRICT_DETAILS;
import static com.examp.three.common.Common.API_PAYMENTHISTORY_WEBVIEW;
import static com.examp.three.common.Common.API_TAXBALANCEPAYMENTDETAILS;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class PaidHistory extends AppCompatActivity implements PaymentHistoryAdapter.Ipaidhistory {

    public static String TAG = PaidHistory.class.getSimpleName();

    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    TextInputEditText etDistrict, etPanchayat,etAssessmentName,etOrderNo,etEmailId,etMobileNo,etAmount,etCollectionDate,
            etReceiptNo,etTaxNo,tetPanchayat,tetDistrict,etUserId;

    RecyclerView recyclerView,recyclerHistory;

    RelativeLayout rootlayout;
    android.app.AlertDialog waitingDialog,waitDialog;

    private String volley_ReceiptNo, volley_TaxPaid, volley_TaxNO;
    private String volley_ReceiptDate, volley_PaymentMode, volley_financialyear;


    LinearLayout mLinear_Userdata;
    TextView mtextView_UserStreetNo, mtextView_UserWardNo, mtextView_UserDoorNo, mtextView_User_Name;
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    private String mDistrictId, mDistrictName;
    private String mPanchayatId, mPanchayatName;
    private EditText medittext_Taxno;
    private Button mbutton_Submit;
    private HashMap<Integer, String> mDistrictHashmapitems = new LinkedHashMap<>();
    private HashMap<Integer, String> mPanchayatHashmapitems = new LinkedHashMap<>();
    Intent intent;
    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();
    private ArrayList<TaxBalancePayment> mTaxBalancePayment = new ArrayList<TaxBalancePayment>();
    private String mVolley_Url;
    private TextView muserTitle;
    String mIntent_Type;

    Toolbar mtoolbar;

    private ArrayList<PaymenyHistoryBean> paymenyHistoryBean = new ArrayList<PaymenyHistoryBean>();

    PaymentHistoryAdapter paymentHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_history);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_property_payment_history);
        districtApiCall();
        intent = getIntent();

        rootlayout = findViewById(R.id.rootlayout);

        recyclerView = findViewById(R.id.recyclerPH);
        recyclerHistory = findViewById(R.id.recycler_history);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManagerH = new LinearLayoutManager(getApplicationContext());
        recyclerHistory.setLayoutManager(layoutManagerH);

        muserTitle = findViewById(R.id.user_title);
        medittext_Taxno = findViewById(R.id.edittext_taxno);

        etDistrict = findViewById(R.id.et_district);
        etPanchayat = findViewById(R.id.et_panchayat);

        etAssessmentName = findViewById(R.id.assessment_name);
        etOrderNo = findViewById(R.id.order_no);
        etEmailId = findViewById(R.id.email_id);
        etMobileNo = findViewById(R.id.mobile_no);
        etAmount = findViewById(R.id.amount);
        etCollectionDate = findViewById(R.id.collection_date);
        etReceiptNo = findViewById(R.id.receipt_no);
        etTaxNo = findViewById(R.id.tax_no);
        tetPanchayat = findViewById(R.id.panchayat);
        tetDistrict = findViewById(R.id.district);
        etUserId = findViewById(R.id.user_id);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString(pref_login_userid, "");
        Log.e("uuuu","----> "+userId);

        if (intent != null) {
            mIntent_Type = intent.getStringExtra("Tax_Type");
        }

        getPaymentHistory(userId);

        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
            medittext_Taxno.setEnabled(true);
        } else {
            medittext_Taxno.setEnabled(false);
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));

        mtextView_User_Name = findViewById(R.id.textview_user_name);

        mtextView_UserStreetNo = findViewById(R.id.textview_user_streetno);

        mtextView_UserDoorNo = findViewById(R.id.textview_user_doorno);

        mtextView_UserWardNo = findViewById(R.id.textview_user_wardno);
        mLinear_Userdata = findViewById(R.id.linear_userdata);

        mbutton_Submit = findViewById(R.id.btn_submit);

        mbutton_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDistrict.getText().toString().isEmpty()) {
                    Snackbar.make(rootlayout, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (etPanchayat.getText().toString().isEmpty()) {
                    Snackbar.make(rootlayout, R.string.snack_panchayat_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (medittext_Taxno.getText().toString().isEmpty()) {
                    Snackbar.make(rootlayout, R.string.snack_taxno_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (Common.isNetworkAvailable(getApplicationContext())) {

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        spinnerDialogPanchayat = new SpinnerDialog(PaidHistory.this, mPanchayatList, "Select or Search Panchayat", "Close");// With No Animation

        spinnerDialogDistrict = new SpinnerDialog(PaidHistory.this, mDistrictList, "Select or Search District", "Close");// With No Animation

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                etDistrict.setText(item);


                for (Map.Entry<Integer, String> entry : mDistrictHashmapitems.entrySet()) {

                    if (entry.getValue().equals(item)) {
                        Log.e(TAG, "District Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());

                        townPanchayat(entry.getKey());

                    }
                }
            }
        });

        etDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanchayatList.clear();
                medittext_Taxno.setText("");

                etPanchayat.setText("");

                medittext_Taxno.setEnabled(false);

                etPanchayat.setText("");
                etDistrict.setText("");

                spinnerDialogDistrict.showSpinerDialog();
            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                etPanchayat.setText(item);
                medittext_Taxno.setEnabled(true);

                for (Map.Entry<Integer, String> entry : mPanchayatHashmapitems.entrySet()) {

                    if (entry.getValue().equals(item)) {
                        Log.e(TAG, "Panchayat Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());
                    }
                }
            }
        });

        etPanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDistrict.getText().toString().equals("")) {
                    Snackbar.make(rootlayout, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                spinnerDialogPanchayat.showSpinerDialog();
            }
        });

    }

    //This method is for retrieving the payment history based on the user id
    public void getPaymentHistory(String userID){
        waitDialog = new SpotsDialog(PaidHistory.this);
        waitDialog.show();
        waitDialog.setCancelable(false);
        String REQUEST_TAG = "apiPaymentDetails_Request";

        String url = "http://etownmobile.in/EtownServicedemo/getPaymentHistory?userId="+userID;

        JsonArrayRequest apiPaymentDetails_Request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {

                try {
                    Log.e(TAG, response.toString());

                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);

                            String userId = district_jsonObject.getString("userId");
                            String district = district_jsonObject.getString("district");
                            String panchayat = district_jsonObject.getString("panchayat");
                            String taxNo = district_jsonObject.getString("taxNo");
                            String receiptNo = district_jsonObject.getString("receiptNo");
                            String collectionDate = district_jsonObject.getString("collectionDate");
                            String amount = district_jsonObject.getString("amount");
                            String taxType = district_jsonObject.getString("taxType");
                            String loginType = district_jsonObject.getString("loginType");
                            String mobileNo = district_jsonObject.getString("mobileNo");
                            String emailId = district_jsonObject.getString("emailId");
                            String orderNo = district_jsonObject.getString("orderNo");
                            String assessmentName = district_jsonObject.getString("assessmentName");

                            String[] colDate = collectionDate.split(" ");
                            etUserId.setText(Html.fromHtml("<b>User Id : </b> "+userId ));
                            tetDistrict.setText(Html.fromHtml("<b>District : </b> "+district ));
                            tetPanchayat.setText(Html.fromHtml("<b>Panchayat : </b> "+panchayat ));
                            etTaxNo.setText(Html.fromHtml("<b>Tax No : </b> "+taxNo ));
                            etReceiptNo.setText(Html.fromHtml("<b>Receipt No : </b> "+receiptNo ));
                            etCollectionDate.setText(Html.fromHtml("<b>Collection Date : </b> "+colDate[0] ));
                            etAmount.setText(Html.fromHtml("<b>Amount : </b> "+amount ));
                            etMobileNo.setText(Html.fromHtml("<b>Mobile No : </b> "+mobileNo ));
                            etEmailId.setText(Html.fromHtml("<b>Email Id : </b> "+emailId ));
                            etOrderNo.setText(Html.fromHtml("<b>Order No : </b> "+orderNo ));
                            etAssessmentName.setText(assessmentName);

                            String oldFormat= "yyyy-MM-dd";
                            String newFormat= "dd/MM/yyyy";

                            String formatedDate = "";
                            SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
                            Date myDate = null;
                            try {
                                myDate = dateFormat.parse(colDate[0]);
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }

                            SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
                            formatedDate = timeFormat.format(myDate);

                            paymenyHistoryBean.add(new PaymenyHistoryBean(userId,district,panchayat,taxNo,receiptNo,
                                    formatedDate,amount,mobileNo,emailId,orderNo,assessmentName,taxType));
                        }

                        paymentHistoryAdapter = new PaymentHistoryAdapter(PaidHistory.this, paymenyHistoryBean,
                                PaidHistory.this);
                        recyclerHistory.setAdapter(paymentHistoryAdapter);

                        waitDialog.dismiss();

                    } else Snackbar.make(rootlayout, "No Payment History", Snackbar.LENGTH_SHORT).show();
                    waitDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    waitDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());
                waitDialog.dismiss();
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiPaymentDetails_Request, REQUEST_TAG);
    }

    //This method is for retrieving the list of districts
    private void districtApiCall() {

        waitingDialog = new SpotsDialog(PaidHistory.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_DISTRICT_DETAILS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, response.toString());

                            if (!response.isNull(0)) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject district_jsonObject = response.getJSONObject(i);

                                    mDistrictId = district_jsonObject.getString("DistrictId");
                                    mDistrictName = district_jsonObject.getString("DistrictName");

                                    mDistrictHashmapitems.put(Integer.parseInt(mDistrictId), mDistrictName);

                                    mDistrictList.add(mDistrictName);
                                }

                            } else Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();

                            waitingDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                            waitingDialog.dismiss();
                        }
                    }
                }).start();

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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);

    }

    //This method is for retrieving the list of panchayats
    private void townPanchayat(Integer districtId) {

        waitingDialog = new SpotsDialog(PaidHistory.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "townPanchayat";

        JsonArrayRequest api_townPanchayat_Request = new JsonArrayRequest(Request.Method.GET, API_TOWNPANCHAYAT + districtId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.e(TAG, response.toString());

                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);

                            mPanchayatId = district_jsonObject.getString("PanchayatId");
                            mPanchayatName = district_jsonObject.getString("PanchayatName");

                            mPanchayatHashmapitems.put(Integer.parseInt(mPanchayatId), mPanchayatName);

                            mPanchayatList.add(mPanchayatName);
                        }

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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);


                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(api_townPanchayat_Request, REQUEST_TAG);
    }

    //This method is for retrieving the balance payment details
    public void taxBalancePayment(Integer taxNo, final String district, final String panchayat, final String taxType) throws UnsupportedEncodingException {
        mTaxBalancePayment.clear();
        waitingDialog = new SpotsDialog(PaidHistory.this);
        waitingDialog.show();
        String REQUEST_TAG = "taxBalancePayment";

        if (intent != null) {
            mIntent_Type = intent.getStringExtra("Tax_Type");

            switch (mIntent_Type) {
                case "Property":
                    mVolley_Url = API_TAXBALANCEPAYMENTDETAILS + "Type=PHistory&" + "TaxNo=" + taxNo +
                            "&FinYear=&District=" + district + "&Panchayat=" + panchayat + "";

                    break;
                case "Water":
                    mVolley_Url = API_TAXBALANCEPAYMENTDETAILS + "Type=WHistory&" + "TaxNo=" + taxNo +
                            "&FinYear=&District=" + district + "&Panchayat=" + panchayat + "";
                    muserTitle.setText("Connection");
                    break;
                case "Birth":

                    break;
            }
        }

        Log.e(TAG, "----> "+mVolley_Url);

        JsonObjectRequest api_taxBalancePayment_Request = new JsonObjectRequest(Request.Method.GET, mVolley_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, response.toString());

                    String recordsetsarray = response.getString("recordsets");

                    JSONArray jsonArray = new JSONArray(recordsetsarray);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONArray insideJsonArray = jsonArray.getJSONArray(i);

                        if (insideJsonArray.length() > 0 && i == 0) {
                            JSONObject json_userdetails = insideJsonArray.getJSONObject(0);
                            String Name = json_userdetails.getString("Name");
                            String DoorNo = json_userdetails.getString("DoorNo");
                            String WardNo = json_userdetails.getString("WardNo");
                            String StreetName = json_userdetails.getString("StreetName");

                            mtextView_User_Name.setText(Name);

                            mtextView_UserStreetNo.setText(StreetName);

                            mtextView_UserDoorNo.setText(DoorNo);

                            mtextView_UserWardNo.setText(WardNo);

                            Log.e(TAG, "Name===>" + Name);

                        } else if (insideJsonArray.length() > 0) {

                            for (int j = 0; j < insideJsonArray.length(); j++) {
                                JSONObject json_taxdetails = insideJsonArray.getJSONObject(j);

                                volley_TaxNO = json_taxdetails.getString("TaxNo");
                                volley_financialyear = json_taxdetails.getString("FinancialYear");
                                volley_ReceiptDate = json_taxdetails.getString("ReceiptDate");
                                volley_PaymentMode = json_taxdetails.getString("PaymentMode");
                                volley_ReceiptNo = json_taxdetails.getString("ReceiptNo");
                                volley_TaxPaid = json_taxdetails.getString("TaxPaid");

                                viewReceipt(taxType,volley_TaxNO,volley_ReceiptNo,volley_ReceiptDate,district,panchayat);
                                Log.e(TAG, "Name TAX===>" + volley_TaxPaid);

                            }

                        } else {
//                            mLinear_Userdata.setVisibility(View.GONE);
                            Snackbar.make(rootlayout, "No Receipt found", Snackbar.LENGTH_SHORT).show();
                            mTaxBalancePayment.clear();

                        }
                    }

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, "error" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(api_taxBalancePayment_Request, REQUEST_TAG);
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

    //This method is for viewing the receipt in the web view
    @Override
    public void viewReceipt(String TaxType,String taxNo,String receiptNo,String receiptDate,String district,
                            String panchayat){

        String url = "";

        Log.e("ppppp","-----> "+mIntent_Type);

        switch (mIntent_Type) {
            case "Property":

                url = API_PAYMENTHISTORY_WEBVIEW + "RType=Receipt&qTaxType=Property&qTaxNo=" + taxNo +
                        "&qReceiptNo=" + receiptNo + "&qColDate=" + receiptDate + "&qDistrict="
                        +district+ "&qPanchayat=" + panchayat + "";

                break;

            case "Water":

                url = API_PAYMENTHISTORY_WEBVIEW + "RType=Receipt&qTaxType=Water&qTaxNo=" + taxNo +
                        "&qReceiptNo=" + receiptNo + "&qColDate=" + receiptDate + "&qDistrict="
                        + district + "&qPanchayat=" + panchayat + "";
                break;
        }

        Log.e("ppppp","-----> "+url);

        openDownloadBrowser(PaidHistory.this, url);
    }

    public void openDownloadBrowser(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        if (i.resolveActivity(activity.getPackageManager()) == null) {
            i.setData(Uri.parse(url));
        }
        activity.startActivity(i);
    }
}
