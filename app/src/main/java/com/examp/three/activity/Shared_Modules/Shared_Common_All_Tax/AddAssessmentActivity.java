package com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
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
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.paymentgateway.utility.Params;
import com.examp.three.common.Common;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_DISTRICT_DETAILS;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_username;

public class AddAssessmentActivity extends AppCompatActivity {

    String districtId, sDistrictName, sPanchayatName, taxType, taxNo, taxName,  name, wardNo, doorNo, streetName;
    Button buttonAdd, buttonCheck, buttonClear;
    EditText editTextAssessmentNo,etDistrict,etPanchayat;
    TextView tvName, tvWard, tvDoor, tvStreet, tvNameKey, tvWardKey, tvDoorKey, tvStreetKey,tvTitle;
    int check;
    ProgressDialog dialog;
    SpotsDialog waitingDialog,waitDialog;

    // Shared Preferences
    SharedPreferences preferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    String MyPREFERENCES = "User";

    int districtPosition = 0, panchayatPosition = 0, taxTypePosition = 0;

    TextView tv1;
    TextView tv2;
    Typeface  typeface;

    Toolbar mtoolbar;
     private static String TAG = AddAssessmentActivity.class.getSimpleName();
    String userName,userId;
    private String mDistrictId, mDistrictName;
    private String mPanchayatId, mPanchayatName;
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;
    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();
    private HashMap<Integer, String> mDistrictHashmapitems = new LinkedHashMap<>();
    private HashMap<Integer, String> mPanchayatHashmapitems = new LinkedHashMap<>();

    SharedPreferences sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/avvaiyar.ttf");

        etDistrict = (EditText) findViewById(R.id.et_District);
        etPanchayat = (EditText) findViewById(R.id.et_Panchayat);
        editTextAssessmentNo = (EditText) findViewById(R.id.editText_AssessmentNo);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonCheck = (Button) findViewById(R.id.buttonCheck);
        tvName = (TextView) findViewById(R.id.textViewNameVal);
        tvName.setTypeface(typeface);
        tvWard = (TextView) findViewById(R.id.textViewWardNoVal);
        tvDoor = (TextView) findViewById(R.id.textViewDoorNoVal);
        tvStreet = (TextView) findViewById(R.id.textViewStreetVal);
        tvStreet.setTypeface(typeface);
        tvNameKey = (TextView) findViewById(R.id.textViewName);
        tvWardKey = (TextView) findViewById(R.id.textViewWardNo);
        tvDoorKey = (TextView) findViewById(R.id.textViewDoorNo);
        tvStreetKey = (TextView) findViewById(R.id.textViewStreetName);
        dialog = new ProgressDialog(AddAssessmentActivity.this);

        tvTitle = (TextView)findViewById(R.id.title) ;

        mtoolbar = (Toolbar)findViewById(R.id.toolbar);

        initToolbar();

        preferences = getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = preferences.edit();

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));

        int districtId = sharedPreference.getInt(PREF_SELECTDISTRICTID, 0);

        townPanchayat(districtId);

        userName = preferences.getString(pref_login_username, "");
        userId = preferences.getString(pref_login_userid, "");

        Intent i = getIntent();
        taxType = i.getStringExtra(Common.Type);

        int position = 0;
        if (taxType != null) {
            switch (taxType) {
                case "P":
                    position = 1;
                    tvTitle.setText("Property Tax");
                    tvTitle.setBackgroundColor(getResources().getColor(R.color.property));
                    tvTitle.setTextColor(getResources().getColor(R.color.white));

                    break;
                case "PR":
                    position = 2;
                    tvTitle.setText("Profession Tax");
                    tvTitle.setBackgroundColor(getResources().getColor(R.color.profession));

                    tvTitle.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "W":
                    position = 3;
                    tvTitle.setText("Waster Charges");
                    tvTitle.setBackgroundColor(getResources().getColor(R.color.water));

                    tvTitle.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "N":
                    position = 4;
                    break;
            }

        }

        districtApiCall();

        spinnerDialogPanchayat = new SpinnerDialog(AddAssessmentActivity.this, mPanchayatList, "Select or Search Panchayat", "Close");// With No Animation

        spinnerDialogDistrict = new SpinnerDialog(AddAssessmentActivity.this, mDistrictList, "Select or Search District", "Close");// With No Animation

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                etDistrict.setText(item);
                sDistrictName=item;

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

                etDistrict.setText("");

                etPanchayat.setText("");
                etDistrict.setText("");

                spinnerDialogDistrict.showSpinerDialog();

            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

//                cleartext();
                etPanchayat.setText(item);

                sPanchayatName = item;

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
                    Toast.makeText(AddAssessmentActivity.this, R.string.snack_district_search, Toast.LENGTH_SHORT).show();
                    return;
                }

                spinnerDialogPanchayat.showSpinerDialog();
            }
        });

        detailsGone();

//        userId = LoginActivity.user_id;
        addAssessmentTextListener();

        editTextAssessmentNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                taxNo = editTextAssessmentNo.getText().toString();
                if (!taxNo.equals("")) {
                    if (Common.isNetworkAvailable(AddAssessmentActivity.this)) {
                          if (etDistrict.getText().toString().equalsIgnoreCase("")) {
                            showToast("Please select district");
                        } else if (etPanchayat.getText().toString().equalsIgnoreCase("")) {
                            showToast("Please select panchayat");
                        } else {
                            checkTaxNo(sDistrictName, sPanchayatName, taxType, taxNo);
                            addAssessment();
                            disableText();
                        }
                    } else {
                        showToast("Please check your Internet Connection");
                    }
                } else {
                    showToast("Enter Assessment No");
                }
                return false;
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taxNo = editTextAssessmentNo.getText().toString();
                InputMethodManager imm =
                        (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAssessmentNo.getWindowToken(), 0);
                if (!taxNo.equals("")) {
                    if (Common.isNetworkAvailable(AddAssessmentActivity.this)) {
                        if (etDistrict.getText().toString().equalsIgnoreCase("")) {
                            showToast("Please select district");
                        } else if (etPanchayat.getText().toString().equalsIgnoreCase("")) {
                            showToast("Please select panchayat");
                        }else {
                            checkTaxNo(sDistrictName, sPanchayatName, taxType, taxNo);
                            addAssessment();
                            disableText();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Assessment No", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
                enableText();
            }
        });

    }

    //This method is for retrieving list of districts
    private void districtApiCall() {

        waitingDialog = new SpotsDialog(AddAssessmentActivity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_DISTRICT_DETAILS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

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

                    } else

                        Toast.makeText(AddAssessmentActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddAssessmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(AddAssessmentActivity.this, "Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(AddAssessmentActivity.this, "Connection Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(AddAssessmentActivity.this, "Could not connect to server ", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(AddAssessmentActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {

                    Toast.makeText(AddAssessmentActivity.this, "Parse Error", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(AddAssessmentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    //This method is for retrieving list of panchayats
    private void townPanchayat(Integer districtId) {

        waitDialog = new SpotsDialog(AddAssessmentActivity.this);
        waitDialog.show();
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
                    } else
                        Toast.makeText(AddAssessmentActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    waitDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(AddAssessmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(AddAssessmentActivity.this, "Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(AddAssessmentActivity.this, "Connection Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(AddAssessmentActivity.this, "Could not connect to server ", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(AddAssessmentActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {

                    Toast.makeText(AddAssessmentActivity.this, "Parse Error", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AddAssessmentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    //This method is for initializing toolbar
    private void initToolbar() {
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Add Assessment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //This method is for clearing texts
    public void clearText() {
        editTextAssessmentNo.setText("");
        editTextAssessmentNo.setHint("Assessment Number");
    }

    //This method is for enabling texts
    public void enableText() {
        etDistrict.setEnabled(true);
        etPanchayat.setEnabled(true);
        editTextAssessmentNo.setFocusableInTouchMode(true);
        editTextAssessmentNo.setFocusable(true);
    }

    //This method is for disabling texts
    public void disableText() {
        etDistrict.setEnabled(true);
        etPanchayat.setEnabled(true);
        editTextAssessmentNo.setFocusableInTouchMode(false);
        editTextAssessmentNo.setFocusable(false);
    }

    public void detailsGone() {
        buttonAdd.setVisibility(View.GONE);
        tvNameKey.setVisibility(View.GONE);
        tvWardKey.setVisibility(View.GONE);
        tvDoorKey.setVisibility(View.GONE);
        tvStreetKey.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
        tvWard.setVisibility(View.GONE);
        tvDoor.setVisibility(View.GONE);
        tvStreet.setVisibility(View.GONE);
    }

    public void detailsVisible() {
        buttonAdd.setVisibility(View.VISIBLE);
        tvNameKey.setVisibility(View.VISIBLE);
        tvWardKey.setVisibility(View.VISIBLE);
        tvDoorKey.setVisibility(View.VISIBLE);
        tvStreetKey.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvWard.setVisibility(View.VISIBLE);
        tvDoor.setVisibility(View.VISIBLE);
        tvStreet.setVisibility(View.VISIBLE);
    }

    //This method is for adding assessments
    public void addAssessment() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CheckData = " + check);
                if (Common.isNetworkAvailable(AddAssessmentActivity.this)) {
                    if (check > 0) {
                        try {
                            taxNo = editTextAssessmentNo.getText().toString();
                            String tempTaxName;
                            tempTaxName = tvName.getText().toString().trim();
                            taxName = URLEncoder.encode(tempTaxName, "UTF-8");

                            addAssessment(sDistrictName, sPanchayatName, taxType, taxNo, taxName, userId);
                        } catch (IOException e) {
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addAssessmentTextListener() {
        editTextAssessmentNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                detailsGone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    //This method is for adding assessments to api
    public void addAssessment(String district, String panchayat, final String taxType, String taxNo, String taxName,
                              String userId) {
        String tempName = "", tempPanchayat = "";
        try {
            tempName = URLEncoder.encode(taxName, "UTF-8");
            tempPanchayat = URLEncoder.encode(panchayat, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String URL_ADD = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_SAVE_ADD_TAX_DETAILS + "?" +
                "district=" + district + Common.PARAMETER_SEPERATOR_AMP + "panchayat=" + tempPanchayat +
                Common.PARAMETER_SEPERATOR_AMP + "taxType=" + taxType + Common.PARAMETER_SEPERATOR_AMP +
                "taxNo=" + taxNo + Common.PARAMETER_SEPERATOR_AMP + "taxName=" + tempName +
                Common.PARAMETER_SEPERATOR_AMP + "userId=" + userId;

        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        System.out.println("111111111111111111111" + URL_ADD);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_ADD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("jsonObject = " + jsonObject.toString());
                try {
                    int responseCode = jsonObject.getInt("code");
                    if (responseCode == 0) {
                        Toast.makeText(getApplicationContext(), "Already Added", Toast.LENGTH_SHORT).show();
                    } else if (responseCode == 1) {
                        Intent intent = new Intent(getApplicationContext(), MakePayment.class);
                        intent.putExtra(Common.Type,taxType);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, "Add Assessment");
    }

    //This method is for checking tax no
    public void checkTaxNo(String district, String panchayat, String taxType, String taxNo) {

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(panchayat, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL_CHECK_TAX_NO = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_CHECK_TAX_DETAILS + "?" +
                Common.PARAMETER_SEPERATOR_AMP + "district=" + district + Common.PARAMETER_SEPERATOR_AMP +
                "panchayat=" + tempPanchayat + Common.PARAMETER_SEPERATOR_AMP + "taxType=" +
                taxType + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + taxNo;

        System.out.println("111111111111111111111" + URL_CHECK_TAX_NO);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_CHECK_TAX_NO,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        check = jsonArray.length();
                        try {
                            if (jsonArray.length() == 0) {
                                detailsGone();
                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                            } else {
                                detailsVisible();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    name = jsonArray.getJSONObject(i).getString(Params.assessmentName);
                                    wardNo = jsonArray.getJSONObject(i).getString(Params.wardNo);
                                    doorNo = jsonArray.getJSONObject(i).getString(Params.doorNo);
                                    streetName = jsonArray.getJSONObject(i).getString(Params.streetName);
                                }
                                tvName.setText(name);
                                tvWard.setText(wardNo);
                                tvDoor.setText(doorNo);
                                tvStreet.setText(streetName);
                            }
                        } catch (JSONException j) {
                            j.printStackTrace();
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest, "Acheck tax no");

    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}