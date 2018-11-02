package com.examp.three.activity.Trade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
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

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class LisenceRenewal extends AppCompatActivity{

    //Toolbar
    Toolbar mtoolbar;

    //Views
    Button renewal,buttonSearch;
    EditText etReferenceNo;
    RelativeLayout rootLayout;
    LinearLayout llRenewalDetails;
    TextView tvAppNo,tvAppDate,tvFinYear,tvLicenceValidity,tvTradersNo,tvLicenceNo,tvAppName,tvEstablishment,tvDistrict,
            tvPanchayat,tvStatus;

    //Spinner
    SpinnerDialog spinnerDialogApplicationNo;

    //ArrayList
    ArrayList<String> applicationNo_items=new ArrayList<>();

    //Strings
    String district = "",panchayat = "",licenceNo="",licenceValidity="",login_userId="";

    //ProgressDialog
    SpotsDialog progressDialog;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_renewal);

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_licence_renewal);

        renewal = (Button)findViewById(R.id.btn_renewal);

        etReferenceNo = (EditText)findViewById(R.id.et_reference_no);

        buttonSearch = (Button)findViewById(R.id.botton_search);

        rootLayout = (RelativeLayout)findViewById(R.id.rootlayout);

        llRenewalDetails = (LinearLayout)findViewById(R.id.ll_renewaldetails);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        //TextViews
        tvAppNo = (TextView)findViewById(R.id.tvAppNo);
        tvAppDate = (TextView)findViewById(R.id.tvAppDate);
        tvFinYear = (TextView)findViewById(R.id.tvFinYear);
        tvLicenceValidity = (TextView)findViewById(R.id.tvLicenceValidity);
        tvTradersNo = (TextView)findViewById(R.id.tvTradersNo);
        tvLicenceNo = (TextView)findViewById(R.id.tvLicenceNo);
        tvAppName = (TextView)findViewById(R.id.tvAppName);
        tvEstablishment = (TextView)findViewById(R.id.tvEstablishment);
        tvDistrict = (TextView)findViewById(R.id.tvDistrict);
        tvPanchayat = (TextView)findViewById(R.id.tvPanchayat);
        tvStatus = (TextView)findViewById(R.id.tvStatus);

        if (Common.isNetworkAvailable(getApplicationContext())) {
            applicationNoList();
        } else {

            Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        spinnerDialogApplicationNo=new SpinnerDialog(LisenceRenewal.this,applicationNo_items,"Select or Search Ward No","Close");// With No Animation
        spinnerDialogApplicationNo=new SpinnerDialog(LisenceRenewal.this,applicationNo_items,"Select or Search Ward No", R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation

        spinnerDialogApplicationNo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int position) {
                etReferenceNo.setText(s);
            }
        });
        etReferenceNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerDialogApplicationNo.showSpinerDialog();

            }
        });

        etReferenceNo.setEnabled(false);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etReferenceNo.getText().toString().isEmpty()){

                    Snackbar.make(rootLayout, "Enter Reference Number", Snackbar.LENGTH_SHORT).show();

                }else{

                    getTradeData(etReferenceNo.getText().toString());

                }
            }
        });

        renewal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),SaveLicenceRenewal.class);
                i.putExtra("district",district);
                i.putExtra("panchayat",panchayat);
                i.putExtra("licenceNo",licenceNo);
                i.putExtra("licenceValidity",licenceValidity);
                startActivity(i);
            }
        });

    }

    //This method is for retrieving the trade details with the reference number
    public void getTradeData(String referenceNo){

        progressDialog = new SpotsDialog(LisenceRenewal.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        String url = Common.API_GETTRADEDATA+referenceNo;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    int sno = jsonObject.getInt("Sno");
                                    String onlineApplicationNo = jsonObject.getString("OnlineApplicationNo");
                                    int tradeCode = jsonObject.getInt("TradeCode");
                                    String tradeDescription = jsonObject.getString("TradeDescription");
                                    int tradersCode = jsonObject.getInt("TradersCode");
                                    String applicantName = jsonObject.getString("ApplicantName");
                                    String establishmentName = jsonObject.getString("EstablishmentName");
                                    int tradeRate = jsonObject.getInt("TradeRate");
                                    int penalty = jsonObject.getInt("Penalty");
                                    int pfa = jsonObject.getInt("PFA");
                                    int advertisement = jsonObject.getInt("Advertisement");
                                    int serviceCharge = jsonObject.getInt("ServiceCharge");
                                    int totalAmount = jsonObject.getInt("TotalAmount");
                                    String wardNo = jsonObject.getString("WardNo");
                                    String streetName = jsonObject.getString("StreetName");
                                    String doorNo = jsonObject.getString("DoorNo");
                                    String applicationDate = jsonObject.getString("ApplicationDate");
                                    licenceValidity = jsonObject.getString("LicenceYear");
                                    String validFrom = jsonObject.getString("ValidFrom");
                                    String validTo = jsonObject.getString("ValidTo");
                                    String mobileNo = jsonObject.getString("MobileNo");
                                    String email = jsonObject.getString("Email");
                                    district = jsonObject.getString("District");
                                    panchayat = jsonObject.getString("Panchayat");
                                    String appStatus = jsonObject.getString("AppStatus");
                                    String rFlage = jsonObject.getString("RFlage");
                                    String reason = jsonObject.getString("Reason");
                                    String entryUser = jsonObject.getString("EntryUser");
                                    String entryDate = jsonObject.getString("EntryDate");
                                    String financialYear = jsonObject.getString("FinancialYear");
                                    licenceNo = jsonObject.getString("LicenceNo");

                                    tvAppNo.setText(Html.fromHtml("<b>Application No : </b> " + onlineApplicationNo));
                                    tvAppDate.setText(Html.fromHtml("Application Date : " + applicationDate));
                                    tvFinYear.setText(Html.fromHtml("Financial Year : " + financialYear));
                                    tvLicenceValidity.setText(Html.fromHtml("Licence Validity : " + licenceValidity));
                                    tvTradersNo.setText(Html.fromHtml("Traders No : " + tradersCode));
                                    tvLicenceNo.setText(Html.fromHtml("Licence No : " + licenceNo));
                                    tvAppName.setText(Html.fromHtml("Applicant Name : " + applicantName));
                                    tvEstablishment.setText(Html.fromHtml("Establishment : " + establishmentName));
                                    tvDistrict.setText(Html.fromHtml("District : " + district));
                                    tvPanchayat.setText(Html.fromHtml("Panchayat : " + panchayat));
                                    tvStatus.setText(Html.fromHtml("Status : " + appStatus));


                                    llRenewalDetails.setVisibility(View.VISIBLE);

                                } catch (JSONException e) {
                                }
                            }
                        }else{

                            Snackbar.make(rootLayout, "No data found", Snackbar.LENGTH_SHORT).show();

                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            SnackShowTop("Time out", rootLayout);
                        } else if (error instanceof AuthFailureError) {
                            SnackShowTop("Connection Time out", rootLayout);
                        } else if (error instanceof ServerError) {
                            SnackShowTop("Could not connect server", rootLayout);
                        } else if (error instanceof NetworkError) {
                            SnackShowTop("Please check the internet connection", rootLayout);
                        } else if (error instanceof ParseError) {
                            SnackShowTop("Parse Error", rootLayout);
                        } else {
                            SnackShowTop(error.getMessage(), rootLayout);
                        }                    }
                }){
            @Override
            public Map<String,String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;

            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }

    //This method is for retrieving application number list
    public void applicationNoList(){

        progressDialog = new SpotsDialog(LisenceRenewal.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        String url = Common.API_GETAPPLICATIONNOLIST+login_userId;

        Log.e("cccc","---> "+url);

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String onlineApplicationNo = jsonObject.getString("OnlineApplicationNo");

                                    applicationNo_items.add(onlineApplicationNo);
                                    etReferenceNo.setEnabled(true);


                                } catch (JSONException e) {
                                }
                            }
                        }else{
                            Snackbar.make(rootLayout, "No Reference Number found", Snackbar.LENGTH_SHORT).show();

                            etReferenceNo.setEnabled(false);
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            SnackShowTop("Time out", rootLayout);
                        } else if (error instanceof AuthFailureError) {
                            SnackShowTop("Connection Time out", rootLayout);
                        } else if (error instanceof ServerError) {
                            SnackShowTop("Could not connect server", rootLayout);
                        } else if (error instanceof NetworkError) {
                            SnackShowTop("Please check the internet connection", rootLayout);
                        } else if (error instanceof ParseError) {
                            SnackShowTop("Parse Error", rootLayout);
                        } else {
                            SnackShowTop(error.getMessage(), rootLayout);
                        }                    }
                }){
            @Override
            public Map<String,String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;

            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);

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
