package com.examp.three.activity.Trade;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_DISTRICT_DETAILS;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;

public class ContactUsActivity extends AppCompatActivity {

    //components
    LinearLayout lin_contact;
    TextInputLayout inputLayout;
    Button btn_submit;
    Toolbar mtoolbar;
    EditText et_selectPanchayat, et_selectDistrict;
    RelativeLayout drawer;
    TextView tv_eoname_tamil,tv_eoname_english,tv_eofficeno,tv_mobileno,tv_email;

    SpotsDialog waitingDialog,waitDialog;
    private String mDistrictId, mDistrictName;
    private String mPanchayatId, mPanchayatName;
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    public static String TAG = ContactUsActivity.class.getSimpleName();

    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    private HashMap<Integer, String> mDistrictHashmapitems = new LinkedHashMap<>();
    private HashMap<Integer, String> mPanchayatHashmapitems = new LinkedHashMap<>();
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    String eo_name_tamil, eo_name_english, eomobile_no, office_no, emailId, district, panchayat;

    int districtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_contact_us);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_contactus);

        init();

    }

    //This method is for initializing all the view in this layout activity
    public void init() {

        lin_contact = (LinearLayout) findViewById(R.id.linear_contact);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        et_selectDistrict = (EditText) findViewById(R.id.et_district);
        et_selectPanchayat = (EditText) findViewById(R.id.et_panchayat);


        tv_eoname_tamil = (TextView)findViewById(R.id.tv_eoname_tamil);
        tv_eoname_english = (TextView)findViewById(R.id.tv_eoname_english);
        tv_eofficeno = (TextView)findViewById(R.id.tv_officeno);
        tv_mobileno = (TextView)findViewById(R.id.tv_phone_no);
        tv_email = (TextView)findViewById(R.id.tv_email);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty())
            et_selectDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));

        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            et_selectPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));

        districtId = sharedPreference.getInt(PREF_SELECTDISTRICTID,0);

        townPanchayat(districtId);

        drawer = (RelativeLayout) findViewById(R.id.root_layout);

        districtApiCall();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_selectDistrict.getText().toString().isEmpty()) {
                    Snackbar.make(drawer, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (et_selectPanchayat.getText().toString().isEmpty()) {
                    Snackbar.make(drawer, R.string.snack_panchayat_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                getContactDetails(et_selectDistrict.getText().toString(),et_selectPanchayat.getText().toString());

            }
        });


        spinnerDialogPanchayat = new SpinnerDialog(ContactUsActivity.this, mPanchayatList, "Select or Search Panchayat", "Close");// With No Animation


        spinnerDialogDistrict = new SpinnerDialog(ContactUsActivity.this, mDistrictList, "Select or Search District", "Close");// With No Animation


        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                et_selectDistrict.setText(item);

                 cleartext();

                for (Map.Entry<Integer, String> entry : mDistrictHashmapitems.entrySet()) {

                    if (entry.getValue().equals(item)) {
                        Log.e(TAG, "District Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());

                        townPanchayat(entry.getKey());

                    }


                }


            }
        });
        et_selectDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanchayatList.clear();

                et_selectPanchayat.setText("");


                et_selectPanchayat.setText("");
                et_selectDistrict.setText("");

                spinnerDialogDistrict.showSpinerDialog();

            }
        });


        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                cleartext();
                et_selectPanchayat.setText(item);

                for (Map.Entry<Integer, String> entry : mPanchayatHashmapitems.entrySet()) {

                    if (entry.getValue().equals(item)) {
                        Log.e(TAG, "Panchayat Id ===> : : " + entry.getKey() + " Count : " + entry.getValue());
                    }


                }

            }
        });

        et_selectPanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_selectDistrict.getText().toString().equals("")) {
                    Snackbar.make(drawer, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }


                spinnerDialogPanchayat.showSpinerDialog();
            }
        });

    }

    //This method is for retrieving list of districts
    private void districtApiCall() {

        waitingDialog = new SpotsDialog(ContactUsActivity.this);
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


                    } else Snackbar.make(drawer, "Error", Snackbar.LENGTH_SHORT).show();


                    waitingDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(drawer, e.getMessage(), Snackbar.LENGTH_SHORT).show();

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


                    SnackShowTop("Time out", drawer);


                } else if (error instanceof AuthFailureError) {


                    SnackShowTop("Connection Time out", drawer);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", drawer);


                } else if (error instanceof NetworkError) {


                    SnackShowTop("Please check the internet connection", drawer);


                } else if (error instanceof ParseError) {


                    SnackShowTop("Parse Error", drawer);

                } else {


                    SnackShowTop(error.getMessage(), drawer);

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

        waitDialog = new SpotsDialog(ContactUsActivity.this);
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


                    } else Snackbar.make(drawer, "Error", Snackbar.LENGTH_SHORT).show();

                    waitDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(drawer, e.getMessage(), Snackbar.LENGTH_SHORT).show();

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

                    SnackShowTop("Time out", drawer);

                } else if (error instanceof AuthFailureError) {

                    SnackShowTop("Connection Time out", drawer);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", drawer);

                } else if (error instanceof NetworkError) {

                    SnackShowTop("Please check the internet connection", drawer);

                } else if (error instanceof ParseError) {

                    SnackShowTop("Parse Error", drawer);

                } else {

                    SnackShowTop(error.getMessage(), drawer);

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

    //This method is for getting contact details
    public void getContactDetails(String sdistrict, String spanchayat) {

        String url = "http://www.predemos.com/Etown/Trade_GetPanchayatContactDetails?District=" + sdistrict + "&Panchayat=" + spanchayat + "";
        String REQUEST_TAG = "apiContactDetails";

        JsonArrayRequest api_contact_details = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() > 0) {
                    lin_contact.setVisibility(View.VISIBLE);


                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                            eo_name_tamil = jsonObject.getString("EO_Name");
                            eo_name_english = jsonObject.getString("EONameEnglish");
                            eomobile_no = jsonObject.getString("EOMobileNo");
                            office_no = jsonObject.getString("OfficeNo");
                            emailId = jsonObject.getString("EmailId");
                            district = jsonObject.getString("District");
                            panchayat = jsonObject.getString("Panchayat");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        tv_eoname_tamil.setText(eo_name_tamil);
                        tv_eoname_english.setText(eo_name_english);
                        tv_eofficeno.setText(office_no);
                        tv_mobileno.setText(eomobile_no);
                        tv_email.setText(emailId);

                    }





                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);


                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(api_contact_details, REQUEST_TAG);

    }

    //This method is for clearing all text views
    public void cleartext(){
        tv_eoname_tamil.setText("");
        tv_eoname_english.setText("");
        tv_eofficeno.setText("");
        tv_mobileno.setText("");
        tv_email.setText("");
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
