package com.examp.three.activity.QuickPay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.CheckDemandActivity;
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

public class PaymentInstantActivity extends AppCompatActivity {
    TextView tvTaxType, tvAssName, tvAssDoorNum, tvAssWardNum, tvAssStreetName;
    EditText etAssessmentNum, etContactNum, etEmail;
    String taxType;
    String  sDistrictName, sPanchayatName, sTaxType, sAssessmentNumber,
            assName, assDoorNum, assWardNum, assStreetName;
    Button buttonSubmit, buttonConfirm ,buttonClear;
    RelativeLayout relativeLayout;
    boolean isBullSector = false, isDataPresent = false, isPanActive = false;
   String Message;

   SpotsDialog waitingDialog,waitDialog;

    String userName,userId;
    private String mDistrictId, mDistrictName;
    private String mPanchayatId, mPanchayatName;
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;
    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();
    private HashMap<Integer, String> mDistrictHashmapitems = new LinkedHashMap<>();
    private HashMap<Integer, String> mPanchayatHashmapitems = new LinkedHashMap<>();

    private static String TAG = PaymentInstantActivity.class.getSimpleName();

    // Shared Preferences
    SharedPreferences preferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    String MyPREFERENCES = "User";

    EditText etDistrict,etPanchayat;
    Toolbar mtoolbar;
    Typeface tfavv;

    SharedPreferences sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_instant);

//        mtoolbar = (Toolbar)findViewById(R.id.toolbar);

        initToolbar();

        preferences = getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = preferences.edit();

        userName = preferences.getString(pref_login_username, "");
        userId = preferences.getString(pref_login_userid, "");

        tfavv = Typeface.createFromAsset(getAssets(), "fonts/avvaiyar.ttf");

        Bundle bundle = getIntent().getExtras();
        taxType = bundle.getString(Params.taxType);

        buttonSubmit = (Button) findViewById(R.id.button_qp_submit);
        buttonConfirm = (Button) findViewById(R.id.button_qp_confirm);
         buttonClear =(Button) findViewById(R.id.button_qp_clear);

        relativeLayout = (RelativeLayout) findViewById(R.id.layout_assessmentDetails);

        etDistrict = (EditText) findViewById(R.id.et_district);
        etPanchayat = (EditText) findViewById(R.id.et_panchayat);

        tvAssName = (TextView) findViewById(R.id.textView_qp_name);
        tvAssName.setTypeface(tfavv);
        tvAssDoorNum = (TextView) findViewById(R.id.textView_qp_doorNum);
        tvAssWardNum = (TextView) findViewById(R.id.textView_qp_wardNum);
        tvAssStreetName = (TextView) findViewById(R.id.textView_qp_street);
        tvAssStreetName.setTypeface(tfavv);

        etAssessmentNum = (EditText) findViewById(R.id.editText_assessmentNum);
        etContactNum = (EditText) findViewById(R.id.editText_qp_phone_num);
        etEmail = (EditText) findViewById(R.id.editText_qp_emailId);

        tvTaxType = (TextView) findViewById(R.id.textView_taxType);
        tvTaxType.setText(taxType);
        theme();

        districtApiCall();

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));

        int districtId = sharedPreference.getInt(PREF_SELECTDISTRICTID, 0);

        Log.e("qqqq","--> "+districtId);

        if (districtId!=0){
            townPanchayat(districtId);
        }

        spinnerDialogPanchayat = new SpinnerDialog(PaymentInstantActivity.this, mPanchayatList, "Select or Search Panchayat", "Close");// With No Animation

        spinnerDialogDistrict = new SpinnerDialog(PaymentInstantActivity.this, mDistrictList, "Select or Search District", "Close");// With No Animation

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
                    Toast.makeText(PaymentInstantActivity.this, R.string.snack_district_search, Toast.LENGTH_SHORT).show();
                    return;
                }
                spinnerDialogPanchayat.showSpinerDialog();
            }
        });

        switch (taxType) {
            case Params.property_tax:
                sTaxType = "P";
                break;
            case Params.profession_tax:
                sTaxType = "PR";
                break;
            case Params.water_tax:
                sTaxType = "W";
                break;
            case Params.miscellaneous_tax:
                sTaxType = "N";
                break;
        }

        etAssessmentNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                relativeLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (event != null){// && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)) {
                validateAndCallGetAss();
//                }
                return false;
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndCallGetAss();
//                disableText();
                InputMethodManager imm =
                        (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etAssessmentNum.getWindowToken(), 0);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(PaymentInstantActivity.this)) {
                    if (sTaxType.equals("N")) {
                        alertBox("Non tax is under construction");
                    } else {
                        getBalanceDetails();
                    }
                } else {
                    alertBox("Please check your internet connection");
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

    //This method is for initializing the toolbar
    private void initToolbar() {
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Instant Pay");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    //This method is for retrieving the list of districts
    private void districtApiCall() {

        waitingDialog = new SpotsDialog(PaymentInstantActivity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";
        Log.e("es",API_DISTRICT_DETAILS+"") ;

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
                        Toast.makeText(PaymentInstantActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PaymentInstantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(PaymentInstantActivity.this, "Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(PaymentInstantActivity.this, "Connection Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(PaymentInstantActivity.this, "Could not connect to server ", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(PaymentInstantActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {

                    Toast.makeText(PaymentInstantActivity.this, "Parse Error", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(PaymentInstantActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        waitDialog = new SpotsDialog(PaymentInstantActivity.this);
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
//                        Toast.makeText(PaymentInstantActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    waitDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(PaymentInstantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(PaymentInstantActivity.this, "Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(PaymentInstantActivity.this, "Connection Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(PaymentInstantActivity.this, "Could not connect to server ", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(PaymentInstantActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {

                    Toast.makeText(PaymentInstantActivity.this, "Parse Error", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(PaymentInstantActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    //This method is for clearing the texts
    public void clearText(){
       districtApiCall();
        etContactNum.setText("");
        etContactNum.setHint("Contact Number");
        etEmail.setText("");
        etEmail.setHint("Email");
        etAssessmentNum.setText("");
        etAssessmentNum.setHint("Assessment Number");
    }

    //This method is for enabling the texts
    public void enableText(){

        etAssessmentNum.setFocusableInTouchMode(true);
        etContactNum.setFocusableInTouchMode(true);
        etEmail.setFocusableInTouchMode(true);
        etEmail.setFocusable(true);
    }

    //This method is for disabling the texts
    public void disableText(){

        etAssessmentNum.setFocusableInTouchMode(false);
        etContactNum.setFocusableInTouchMode(false);
        etEmail.setFocusableInTouchMode(false);
        etEmail.setFocusable(false);
    }

    //This method is for validating and getting assessment details
    private void validateAndCallGetAss() {
        buttonConfirm.setEnabled(true);
        sAssessmentNumber = etAssessmentNum.getText().toString();
        String contactNum = etContactNum.getText().toString();
        String email = etEmail.getText().toString();

        if (sAssessmentNumber.equals("")) {
            etAssessmentNum.setError("Assessment number required");
        } else if (contactNum.equals("")) {
            etContactNum.setError("Contact number required");
        } else if (email.equals("")) {
            etEmail.setError("EmailId required");
        } else if (contactNum.length() != 10 || contactNum.startsWith("0")) {
            showToast("Enter a valid phone number");
        } else if (!isValidEmail(email)) {
            showToast("Enter a valid email");
        }
        else {
            if (Common.isNetworkAvailable(PaymentInstantActivity.this)) {
                if (etDistrict.getText().toString().equalsIgnoreCase("")) {
                    showToast("Please select district");
                } else if (etPanchayat.getText().toString().equalsIgnoreCase("")) {
                    showToast("Please select panchayat");
                } else {
                    getAssessmentDetails();
                    disableText();
                }
            } else {
                showToast("Please check your Internet connection");
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void theme() {
        Resources resources = getResources();

        if (taxType.equalsIgnoreCase(Params.property_tax)) {
            tvTaxType.setBackgroundColor(resources.getColor(R.color.property));
        } else if (taxType.equalsIgnoreCase(Params.profession_tax)) {
            tvTaxType.setBackgroundColor(resources.getColor(R.color.profession));
        } else if (taxType.equalsIgnoreCase(Params.water_tax)) {
            tvTaxType.setBackgroundColor(resources.getColor(R.color.water));
        } else if (taxType.equalsIgnoreCase(Params.miscellaneous_tax)) {
            tvTaxType.setBackgroundColor(resources.getColor(R.color.nonTax));
        }
    }

    //This method is for getting assessment details
    public void getAssessmentDetails() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_TAX_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(PaymentInstantActivity.this);
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
                + "district=" + sDistrictName + Common.PARAMETER_SEPERATOR_AMP + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber + Common.PARAMETER_SEPERATOR_AMP
                + "taxType=" + sTaxType,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            System.out.println("jsonArray = " + jsonArray);
                            if (!jsonArray.isNull(0)) {
//                            if (jsonArray != null) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                                assName = jsonObject.getString(Params.assessmentName);
                                assDoorNum = jsonObject.getString(Params.doorNo);
                                assWardNum = jsonObject.getString(Params.wardNo);
                                assStreetName = jsonObject.getString(Params.streetName);
                                tvAssName.setText(assName);
                                tvAssDoorNum.setText(assDoorNum);
                                tvAssWardNum.setText(assWardNum);
                                tvAssStreetName.setText(assStreetName);
                                relativeLayout.setVisibility(View.VISIBLE);
                            } else {
                                showToast("No data found");
                            }
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

                System.out.println("volleyError = " + volleyError.toString());
                showToast("Server taking too long to response please try later");
            }
        });
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //This method is for getting balance details
    public void getBalanceDetails() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_BAL_BLOCK_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(PaymentInstantActivity.this);
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

        final JsonObjectRequest request = new JsonObjectRequest(URL_ASSESS_DETAILS
                + "?" + "district=" + sDistrictName + Common.PARAMETER_SEPERATOR_AMP
                + "panchayat=" + tempPanchayat + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber
                + Common.PARAMETER_SEPERATOR_AMP + "taxType=" + sTaxType, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            System.out.println("jsonString = " + jsonObject);

                            isBullSector = jsonObject.getBoolean("bulsector");
                            isDataPresent = jsonObject.getBoolean("dataPresent");
                            Message =jsonObject.getString("message");

                            JSONArray blockList = new JSONArray();
                            blockList = jsonObject.getJSONArray("panchayatBlockList");
                            System.out.println("blockList = " + blockList);
                            for (int i = 0; i < blockList.length(); i++) {
                                if (sPanchayatName.equals(blockList.get(i))) {
                                    isPanActive = false;
                                    break;
                                } else {
                                    isPanActive = true;
                                }
                            }
                            if (isBullSector) {
                                alertBox("Assessment is in Court case or Government building");
                                isBullSector = false;
                            } else if (!isPanActive) {
                                alertBox("Mobile payment is not active for this panchayat");
                            } else if (isDataPresent) {

                                if(Message.equalsIgnoreCase("")) {
                                    Intent intent = new Intent(PaymentInstantActivity.this, CheckDemandActivity.class);
                                    intent.putExtra(Params.districtName, sDistrictName);
                                    intent.putExtra(Params.panchayatName, sPanchayatName);
                                    intent.putExtra(Params.taxType, sTaxType);
                                    intent.putExtra(Params.assessmentNumber, sAssessmentNumber);
                                    intent.putExtra(Params.assessmentName, assName);
                                    intent.putExtra(Params.doorNo, assDoorNum);
                                    intent.putExtra(Params.streetName, assStreetName);
                                    intent.putExtra(Params.wardNo, assWardNum);
                                    intent.putExtra(Params.userId, "unregistered");
                                    intent.putExtra(Params.contactNo, etContactNum.getText().toString());
                                    intent.putExtra(Params.emailId, etEmail.getText().toString());

                                    startActivity(intent);
                                }else{
                                    alertBox(Message);

                                }
                            } else if (!isDataPresent) {
                                alertBox("No balance amount to be paid");
                            }

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
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void alertBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentInstantActivity.this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}