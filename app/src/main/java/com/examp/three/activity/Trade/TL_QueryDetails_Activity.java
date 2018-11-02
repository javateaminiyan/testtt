package com.examp.three.activity.Trade;

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
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.TradeLicence.ViewQuery_Adapter;
import com.examp.three.model.TradeLicence.TL_querydetails_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;

import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_QUERY_DETAILS;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class TL_QueryDetails_Activity extends AppCompatActivity {

    //views
    RecyclerView vrh_rv;

    Toolbar vrh_toolbar;
    TextView tv_nodata;

    String Sno, OnlineApplicationNo, Query, ApplicationUserId, MobileNo, Email, District, Panchayat, CreatedBy, QueryDate,
            Answer, AnswerDate, QAStatus,login_userId;

    //non views

    ViewQuery_Adapter adapter;

    ArrayList<TL_querydetails_pojo> beanlist;
    String TAG = TL_QueryDetails_Activity.class.getSimpleName();
    LinearLayout drawer;
    SpotsDialog waitingDialog;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_details);

        //recycler view
        vrh_rv = (RecyclerView) findViewById(R.id.vrh_rv);
        drawer = (LinearLayout) findViewById(R.id.rootlayout);

        //textview
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);

        //toolbar
        vrh_toolbar = (Toolbar) findViewById(R.id.vrh_toolbar);
        setSupportActionBar(vrh_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toobar_query);

        //array list
        beanlist = new ArrayList<>();

        //linear layout manager
        if (vrh_rv != null) {
            vrh_rv.setHasFixedSize(true);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        vrh_rv.setLayoutManager(linearLayoutManager);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        //----
        queryDetailsApiCall(login_userId);
    }

    //This method is for api calling for query details
    private void queryDetailsApiCall(String userid) {

        waitingDialog = new SpotsDialog(TL_QueryDetails_Activity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiQueryDetails_Request";


        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_QUERY_DETAILS + "PublicUserId="+userid, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
//                    Log.e(TAG, response.toString());
                    waitingDialog.dismiss();

                    if (response.length() > 0) {
                        if (!response.isNull(0)) {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject query_jsonObject = response.getJSONObject(i);

                                Sno = query_jsonObject.getString("Sno");
                                OnlineApplicationNo = query_jsonObject.getString("OnlineApplicationNo");
                                Query = query_jsonObject.getString("Query");
                                ApplicationUserId = query_jsonObject.getString("ApplicationUserId");
                                MobileNo = query_jsonObject.getString("MobileNo");
                                Email = query_jsonObject.getString("Email");
                                District = query_jsonObject.getString("District");
                                Panchayat = query_jsonObject.getString("Panchayat");
                                CreatedBy = query_jsonObject.getString("CreatedBy");
                                QueryDate = query_jsonObject.getString("QueryDate");
                                Answer = query_jsonObject.getString("Answer");
                                AnswerDate = query_jsonObject.getString("AnswerDate");
                                QAStatus = query_jsonObject.getString("QAStatus");

                                beanlist.add(new TL_querydetails_pojo(Sno, OnlineApplicationNo, Query, District,
                                        Panchayat, QueryDate, Answer, AnswerDate, QAStatus));

                            }


                        } else Snackbar.make(drawer, "Error", Snackbar.LENGTH_SHORT).show();

                    } else {
                        tv_nodata.setVisibility(View.VISIBLE);
                    }
                    waitingDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(drawer, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    waitingDialog.dismiss();
                }
                setAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    //This method is for showing snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for setting adapter for retrieved values
    public void setAdapter() {
        adapter = new ViewQuery_Adapter(beanlist, TL_QueryDetails_Activity.this);
        vrh_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vrh_rv.setNestedScrollingEnabled(false);
    }
}
