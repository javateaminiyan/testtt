package com.examp.three.activity.WaterTax;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.Water_Tax.Water_Assessment_Search_Adapter;
import com.examp.three.common.Common;
import com.examp.three.model.Water_Tax.Water_Assessment_Search_Pojo;

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

import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_DISTRICT_DETAILS;
import static com.examp.three.common.Common.API_TAXBALANCEPAYMENTDETAILS;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;

public class Water_AssessmentSearch_Activity extends AppCompatActivity {

    ArrayList<String> panchayat_items = new ArrayList<>();
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    RelativeLayout rootlayout;
    TextInputEditText etDistrict, etPanchayat;
    ImageView imBack;

    public static String TAG = Water_AssessmentSearch_Activity.class.getSimpleName();
    String name = "", doorNo = "", wardNo = "", streetName = "", mIntent_Type;

    Button submit;

    android.app.AlertDialog waitingDialog,waitDialog;
    EditText edTaxNo;
    TextView tvName, tvDoorNo, tvWardNo, tvStreetName;

    private HashMap<Integer, String> mDistrictHashmapitems = new LinkedHashMap<>();

    private HashMap<Integer, String> mPanchayatHashmapitems = new LinkedHashMap<>();

    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    LinearLayout  llBalance;

    ArrayList<Water_Assessment_Search_Pojo> assessment_search_bean = new ArrayList<>();

    private RecyclerView recyclerView;
    private Water_Assessment_Search_Adapter assessmentAdapter;
    Toolbar mtoolbar;
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water__assessment_search);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.watertax_searchassessment);

        initViews();

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
            edTaxNo.setEnabled(true);
        } else {
            edTaxNo.setEnabled(false);
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));

        int districtId = sharedPreference.getInt(PREF_SELECTDISTRICTID,0);

        panchayatList(districtId);

        districtList();

        spinnerDialogPanchayat = new SpinnerDialog(Water_AssessmentSearch_Activity.this, mPanchayatList, "Select or Search Panchayat", "Close");// With No Animation

        spinnerDialogDistrict = new SpinnerDialog(Water_AssessmentSearch_Activity.this, mDistrictList, "Select or Search District", "Close");// With No Animation

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                etDistrict.setText(item);
                etPanchayat.setText("");

                for (Map.Entry<Integer, String> entry : mDistrictHashmapitems.entrySet()) {

                    if (entry.getValue().equals(item)) {
                        Log.e(TAG, "District Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());

                        panchayatList(entry.getKey());
                    }
                }
            }
        });
        etDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogDistrict.showSpinerDialog();
                panchayat_items.clear();
                edTaxNo.setEnabled(false);

            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int position) {

                etPanchayat.setText(s);
                for (Map.Entry<Integer, String> entry : mPanchayatHashmapitems.entrySet()) {

                    if (entry.getValue().equals(s)) {
                        Log.e(TAG, "Panchayat Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());
                    }
                }
            }
        });

        etPanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edTaxNo.setEnabled(true);

                if (etDistrict.length() > 0) {
                    spinnerDialogPanchayat.showSpinerDialog();

                } else {
                    Snackbar.make(rootlayout, "Select District ", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        edTaxNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDistrict.length() == 0 && etPanchayat.length() == 0) {
                    Snackbar.make(rootlayout, "Select District and Panchayat", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDistrict.length() > 0 && etPanchayat.length() > 0 && edTaxNo.length() > 0) {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        try {
                            assessment_search_bean.clear();
                            getAssessmentDetails();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else
                        Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(rootlayout, "Enter all Values", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    //This method is for initializing view in layout activity
    private void initViews() {

        etDistrict = findViewById(R.id.et_district);
        etPanchayat = findViewById(R.id.et_panchayat);

        submit = findViewById(R.id.submit);

        edTaxNo = findViewById(R.id.tax_no);
        edTaxNo.setEnabled(false);
        tvName = findViewById(R.id.textview_user_name);
        tvDoorNo = findViewById(R.id.textview_user_doorno);
        tvWardNo = findViewById(R.id.textview_user_wardno);
        tvStreetName = findViewById(R.id.textview_user_streetno);
        rootlayout = findViewById(R.id.rootlayout);

        llBalance = findViewById(R.id.ll_balance);

        recyclerView = findViewById(R.id.recyclerview);

    }

    //This method is for retrieving list of districts
    public void districtList() {

        waitingDialog = new SpotsDialog(Water_AssessmentSearch_Activity.this);
        waitingDialog.show();
        String REQUEST_TAG = "districtList";


        JsonArrayRequest request = new JsonArrayRequest(API_DISTRICT_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String districtName = jsonObject.getString("DistrictName");
                                int districtId = jsonObject.getInt("DistrictId");

                                mDistrictList.add(districtName);

                                mDistrictHashmapitems.put(districtId, districtName);
                                waitingDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                waitingDialog.dismiss();

                            }
                        }

                    }
                },
                new Response.ErrorListener() {
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);

    }

    //This method is for retrieving list of panchayats
    public void panchayatList(int districtId) {

        waitDialog = new SpotsDialog(Water_AssessmentSearch_Activity.this);
        waitDialog.show();
        String REQUEST_TAG = "panchayatList";


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, API_TOWNPANCHAYAT + districtId, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String panchayatName = jsonObject.getString("PanchayatName");
                                int panchayatId = jsonObject.getInt("PanchayatId");

                                mPanchayatList.add(panchayatName);

                                mDistrictHashmapitems.put(panchayatId, panchayatName);

                                waitDialog.dismiss();

                            } catch (JSONException e) {

                                waitDialog.dismiss();

                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);


    }

    //This method is for retrieving assessment details
    public void getAssessmentDetails() throws UnsupportedEncodingException {

        waitingDialog = new SpotsDialog(Water_AssessmentSearch_Activity.this);
        waitingDialog.show();
        String REQUEST_TAG = "getAssessmentDetails";

        String taxno = URLEncoder.encode(edTaxNo.getText().toString(), "UTF-8");
        String district = URLEncoder.encode(etDistrict.getText().toString(), "UTF-8");
        String panchayat = URLEncoder.encode(etPanchayat.getText().toString(), "UTF-8");

        String url = API_TAXBALANCEPAYMENTDETAILS + "Type=WSearch&TaxNo=" + taxno + "&FinYear=&" +
                "District=" + district + "&Panchayat=" + panchayat;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject recordset = new JSONObject(response.toString());

                            JSONArray firstarray = new JSONArray(recordset.getString("recordsets"));
                            if (!firstarray.equals("[[],[]]")) {


                                for (int i = 0; i < firstarray.length(); i++) {

                                    Log.e("ooofirstarray", "---" + firstarray.toString());

                                    JSONArray secondArray = (JSONArray) firstarray.get(0);

                                    if (secondArray.length() > 0) {
                                        for (int j = 0; j < secondArray.length(); j++) {

                                            JSONObject jsonObject = (JSONObject) secondArray.get(i);

                                            name = jsonObject.getString("Name");
                                            doorNo = jsonObject.getString("DoorNo");
                                            wardNo = jsonObject.getString("WardNo");
                                            streetName = jsonObject.getString("StreetName");

                                            tvName.setText(name);
                                            tvDoorNo.setText(doorNo);
                                            tvWardNo.setText(wardNo);
                                            tvStreetName.setText(streetName);

                                        }
                                    } else {
                                        Snackbar.make(rootlayout, "No Data Found !", Snackbar.LENGTH_SHORT).show();
                                        assessment_search_bean.clear();
                                        assessmentAdapter.notifyDataSetChanged();

                                        llBalance.setVisibility(View.GONE);

                                    }

                                    JSONArray thirdArray = (JSONArray) firstarray.get(1);

                                    if (thirdArray.length() > 0) {

                                        for (int j = 0; j < thirdArray.length(); j++) {

                                            JSONObject jsonObjectthird = (JSONObject) thirdArray.get(j);

                                            String finYear = jsonObjectthird.getString("FinancialYear");
                                            int QuarterYear = jsonObjectthird.getInt("QuarterYear");
                                            int balance = jsonObjectthird.getInt("Balance");

                                            assessment_search_bean.add(new Water_Assessment_Search_Pojo(edTaxNo.getText().toString(), finYear, QuarterYear, balance));

                                        }
                                        balanceHistory();
                                    } else {
                                        Snackbar.make(rootlayout, "No Data Found !", Snackbar.LENGTH_SHORT).show();
                                        assessment_search_bean.clear();
                                        assessmentAdapter.notifyDataSetChanged();

                                        llBalance.setVisibility(View.GONE);
                                    }

                            }
                            } else {
                                Snackbar.make(rootlayout, "No Data Found !", Snackbar.LENGTH_SHORT).show();
                                assessment_search_bean.clear();
                                assessmentAdapter.notifyDataSetChanged();

                                llBalance.setVisibility(View.GONE);
                            }
                            waitingDialog.dismiss();


                        } catch (JSONException e) {
                            waitingDialog.dismiss();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    waitingDialog.dismiss();
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

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, REQUEST_TAG);

    }

    //This method is for setting adapter for balance history
    public void balanceHistory() {

        llBalance.setVisibility(View.VISIBLE);
        assessmentAdapter = new Water_Assessment_Search_Adapter(assessment_search_bean);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assessmentAdapter);
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
}