package com.examp.three.activity.Building;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.examp.three.R;
import com.examp.three.model.Building.BLCompletedRequestBean;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.Building.BuildingCompletedRequestAdapter;
import com.examp.three.common.Common;
import dmax.dialog.SpotsDialog;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class Building_Completed_Request extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_building_licence;
    LinearLayout rootLayout;
    TextView tvNoData ;

    String TAG = Building_Completed_Request.class.getSimpleName();

    String applicationNo, applicationDate,applicantName,obpaDate,licenceFee,delopmentCharge,
            vacantLandTax,totalbl;

    SpotsDialog waitingDialog;
    BuildingCompletedRequestAdapter mAdapter;
    List<BLCompletedRequestBean> buildingbeanList;

    String login_userId,login_username;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building__completed__request);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildingbeanList = new ArrayList<>();

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");
        login_username = sharedPreference.getString(pref_login_firstname,"");

        rootLayout = (LinearLayout)findViewById(R.id.rootlayout);

        init();
    }

    public void init() {
        rv_building_licence = (RecyclerView) findViewById(R.id.recycler_building_lice);
        tvNoData = (TextView)findViewById(R.id.tv_nodata);

        completeRequest(login_userId);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_building_licence.setLayoutManager(layoutManager);
    }

    //This method is for retrieving completed requests
    private void completeRequest(String userId) {

        waitingDialog = new SpotsDialog(Building_Completed_Request.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiComplete_Request";

        String API_COMPLETED_REQUEST = Common.baseUrl+"Completed_Request?UserId="+userId;

        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_COMPLETED_REQUEST, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {

                    try {
                        Log.e(TAG, response.toString());

                        if (response.length()>0) {

                            for(int i=0;i<response.length();i++) {
                                JSONObject jsonRes = (JSONObject) response.getJSONObject(i);
                             applicationNo = jsonRes.getString("ApplicationNo");
                             applicationDate = jsonRes.getString("ApplicationDate");
                             applicantName =jsonRes.getString("ApplicantName");
                             obpaDate = jsonRes.getString("OBPADate");
                             licenceFee = jsonRes.getString("LicenceFees");
                             delopmentCharge = jsonRes.getString("DevelopmentCharge");
                             vacantLandTax = jsonRes.getString("VacantLandTax");
                             totalbl = jsonRes.getString("TotalBL");

                                buildingbeanList.add(new BLCompletedRequestBean(applicationNo,applicationDate,applicantName,obpaDate,licenceFee,delopmentCharge,
                                        vacantLandTax,totalbl));
                            }
                            mAdapter = new BuildingCompletedRequestAdapter(getApplicationContext(), buildingbeanList);
                            rv_building_licence.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                            tvNoData.setVisibility(View.GONE);
                            rv_building_licence.setVisibility(View.VISIBLE);

                        } else {
                            Snackbar.make(rootLayout, "No data found", Snackbar.LENGTH_SHORT).show();
                            tvNoData.setVisibility(View.VISIBLE);
                            rv_building_licence.setVisibility(View.GONE);
                        }

                        waitingDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Snackbar.make(rootLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

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

    //This method is for showing snack bar messages in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }
}
