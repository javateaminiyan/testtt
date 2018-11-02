package com.examp.three.activity.Trade;

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
import android.widget.TextView;

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
import com.examp.three.adapter.TradeLicence.ViewReceiptHistory_Adapter;
import com.examp.three.common.Common;
import com.examp.three.model.TradeLicence.ViewReceiptHistory_Pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class TL_ViewReceipt_History_Activity extends AppCompatActivity {

    //views
    RecyclerView vrh_rv;

    Toolbar vrh_toolbar;
    LinearLayout rootView;
    TextView tv_nodata;

    //non views
    ViewReceiptHistory_Adapter adapter;

    ArrayList<ViewReceiptHistory_Pojo> beanlist;

    SpotsDialog spotsDialog;

    String login_userId;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt_history);

        //recycler view
        vrh_rv = (RecyclerView) findViewById(R.id.vrh_rv);

        tv_nodata = (TextView) findViewById(R.id.tv_nodata);

        //LinearLayout
        rootView = (LinearLayout) findViewById(R.id.rootView);

        //toolbar
        vrh_toolbar = (Toolbar) findViewById(R.id.vrh_toolbar);
        vrh_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
        setSupportActionBar(vrh_toolbar);

        //array list
        beanlist = new ArrayList<>();

        //linear layout manager
        if (vrh_rv != null) {
            vrh_rv.setHasFixedSize(true);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        vrh_rv.setLayoutManager(linearLayoutManager);

        //----

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        getHistoryDetails(login_userId);

    }

    //This method is for retrieving history details
    public void getHistoryDetails(final String userId) {

        String url = Common.baseUrl + "Grid_Load?UserId=" + userId;

        beanlist.clear();

        spotsDialog = new SpotsDialog(TL_ViewReceipt_History_Activity.this);
        spotsDialog.show();
        JsonArrayRequest api_history_details = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject json_history = (JSONObject) response.getJSONObject(i);
                            int sno = json_history.getInt("Sno");
                            String onlineAppplicationNo = json_history.getString("OnlineApplicationNo");
                            String tradeCode = json_history.getString("TradeCode");
                            String tradeDescrption = json_history.getString("TradeDescription");
                            String tradersCode = json_history.getString("TradersCode");
                            String ApplicantName = json_history.getString("ApplicantName");
                            String establishmentName = json_history.getString("EstablishmentName");
                            int tradeRate = json_history.getInt("TradeRate");
                            int penality = json_history.getInt("Penalty");
                            int pfa = json_history.getInt("PFA");
                            int advertisement = json_history.getInt("Advertisement");
                            int serviceCharge = json_history.getInt("ServiceCharge");
                            int toalAmount = json_history.getInt("TotalAmount");
                            String wardno = json_history.getString("WardNo");
                            String streetName = json_history.getString("StreetName");
                            String doorno = json_history.getString("DoorNo");
                            String application_date = json_history.getString("ApplicationDate");
                            String licenceYear = json_history.getString("LicenceYear");
                            String validFrom = json_history.getString("ValidFrom");
                            String validTo = json_history.getString("ValidTo");
                            String mobileNo = json_history.getString("MobileNo");
                            String email = json_history.getString("Email");
                            String district = json_history.getString("District");
                            String panchayat = json_history.getString("Panchayat");
                            String appDtatus = json_history.getString("AppStatus");
                            String RFlag = json_history.getString("RFlage");
                            String reason = json_history.getString("Reason");
                            String entryUser = json_history.getString("EntryUser");
                            String entryDate = json_history.getString("EntryDate");
                            String financialYear = json_history.getString("FinancialYear");
                            String licenceNo = json_history.getString("LicenceNo");


                            String[] date = entryDate.split("T");
                            String tdate = date[0];


                            beanlist.add(new ViewReceiptHistory_Pojo(appDtatus, onlineAppplicationNo, changeDateFormat(tdate),
                                    financialYear, licenceYear, licenceNo, ApplicantName, establishmentName,
                                    district, panchayat, userId,tradersCode));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    tv_nodata.setVisibility(View.GONE);

                    setAdapter();

                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }
                spotsDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {


                    SnackShowTop("Time out", rootView);


                } else if (error instanceof AuthFailureError) {


                    SnackShowTop("Connection Time out", rootView);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", rootView);


                } else if (error instanceof NetworkError) {


                    SnackShowTop("Please check the internet connection", rootView);


                } else if (error instanceof ParseError) {


                    SnackShowTop("Parse Error", rootView);

                } else {


                    SnackShowTop(error.getMessage(), rootView);

                }
                spotsDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", Common.ACCESS_TOKEN);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(api_history_details, "HistoyRequest");

    }

    //This method is for changing the date format
    public String changeDateFormat(String sdate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(sdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
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

    //This method is for setting adapter for retrieved values
    public void setAdapter() {
        adapter = new ViewReceiptHistory_Adapter(TL_ViewReceipt_History_Activity.this, beanlist);
        vrh_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vrh_rv.setNestedScrollingEnabled(false);
    }
}
