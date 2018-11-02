package com.examp.three.activity.Building;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.examp.three.R;
import com.examp.three.model.Building.Building_OnlinePayment_Pojo;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.Building.Building_OnlinePayment_Adapter;
import dmax.dialog.SpotsDialog;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_ONLINEPAYMENT_BUILDING;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class BuildingOnlinePayment extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    LinearLayout rootlayout;
    Building_OnlinePayment_Adapter mbuilding_online_paymentAdapter;
    private ArrayList<Building_OnlinePayment_Pojo> mTaxBalancePayment = new ArrayList<Building_OnlinePayment_Pojo>();

    String muserid;
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    android.app.AlertDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_online_payment);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.Online_Payment_Building);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rootlayout = (LinearLayout) findViewById(R.id.rootlayout);
        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        muserid = sharedPreference.getString(pref_login_userid, "");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //   mTaxBalancePayment.add(new Building_OnlinePayment_Pojo("2018-02-01", "21", "Iniyan", "2018-02-01", "Completed", "50","mass"));
        building_pendinglicence();

    }

    //This method is for retrieving pending building licenses
    private void building_pendinglicence() {
        mTaxBalancePayment.clear();
        waitingDialog = new SpotsDialog(BuildingOnlinePayment.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_ONLINEPAYMENT_BUILDING + "UserId=" + muserid + "", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    //Log.e(TAG, response.toString());

                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonObject = response.getJSONObject(i);

                            String UserId = jsonObject.optString("UserId");
                            String ApplicationNo = jsonObject.optString("ApplicationNo");
                            String ApplicationDate = jsonObject.optString("ApplicationDate");
                            String ApplicantName = jsonObject.optString("ApplicantName");
                            String ApplicationName = jsonObject.optString("ApplicationName");
                            String OBPADate = jsonObject.optString("OBPADate");
                            String LicenceFees = jsonObject.optString("LicenceFees");
                            String DevelopmentCharge = jsonObject.optString("DevelopmentCharge");
                            String VacantLandTax = jsonObject.optString("VacantLandTax");
                            String TotalBL = jsonObject.optString("TotalBL");

                            mTaxBalancePayment.add(new Building_OnlinePayment_Pojo(ApplicationNo, ApplicationDate, ApplicantName, ApplicationName, OBPADate, LicenceFees, DevelopmentCharge, VacantLandTax, TotalBL, UserId));

                        }
                        mbuilding_online_paymentAdapter = new Building_OnlinePayment_Adapter(BuildingOnlinePayment.this, mTaxBalancePayment);

                        recyclerView.setAdapter(mbuilding_online_paymentAdapter);

                        mbuilding_online_paymentAdapter.notifyDataSetChanged();

                    } else Snackbar.make(rootlayout, "No data found", Snackbar.LENGTH_SHORT).show();

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

    //THis method is for showing snackbar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }
}
