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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.examp.three.BuildConfig;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import javax.inject.Inject;
import com.examp.three.model.BuildingPlan.BP_RequestStatus_Pojo;
import com.examp.three.adapter.Building.BP_RequestStatus_Adapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class BP_AppRequestStatus_Activity extends AppCompatActivity {

    private static final String TAG = "App Request Status";
    //views
    RecyclerView bars_rv;

    Toolbar bars_toolbar;

    //non views

    BP_RequestStatus_Adapter adapter;

    ArrayList<BP_RequestStatus_Pojo> beanlist;

    SpotsDialog spotsDialog;

    String errorType,login_userId,login_username;

    LinearLayout ars_root_layout;

    TextView tv_no_history_found;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bp_app_request_status);

        //linearlayout
        ars_root_layout = (LinearLayout) findViewById(R.id.ars_root_layout);

        //spotsdialog
        spotsDialog = new SpotsDialog(BP_AppRequestStatus_Activity.this);

        //recycler view
        bars_rv = (RecyclerView) findViewById(R.id.bars_rv);

        //toolbar
        bars_toolbar = (Toolbar) findViewById(R.id.bars_toolbar);

        setSupportActionBar(bars_toolbar);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_requestStatus);
        //textview
        tv_no_history_found = (TextView) findViewById(R.id.tv_no_history_found);

        //array list
        beanlist = new ArrayList<>();

        //linear layout manager
        if (bars_rv != null) {
            bars_rv.setHasFixedSize(true);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        bars_rv.setLayoutManager(linearLayoutManager);

        //----
        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");
        login_username = sharedPreference.getString(pref_login_firstname,"");

        if (Common.isNetworkAvailable(BP_AppRequestStatus_Activity.this)) {
            getAppRequestDetails(login_userId,login_username);
        } else {
            Snackbar.make(ars_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }
    }

    //This method is for retrieving Request status by passing userId and userName
    public void getAppRequestDetails(String userid,String username) {

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.Request_Status(Common.ACCESS_TOKEN, userid, username);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {
                    if (BuildConfig.DEBUG) {

                        Log.e(TAG, "" + response.raw().toString());
                    }

                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONArray jsonArray = new JSONArray(sResponse);
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String ApplicationNo = jsonObject.optString("ApplicationNo");
                                String AppDate = jsonObject.optString("AppDate");
                                String ApplicationName = jsonObject.optString("ApplicationName");
                                String FatherName = jsonObject.optString("FatherName");
                                String MobileNo = jsonObject.optString("MobileNo");
                                String EmailId = jsonObject.optString("EmailId");
                                String PlotAreaSqFt = jsonObject.optString("PlotAreaSqFt");
                                String BuildingType = jsonObject.optString("BuildingType");
                                String AppProcessingStage = jsonObject.optString("AppProcessingStage");

                                beanlist.add(new BP_RequestStatus_Pojo(ApplicationNo, AppDate,
                                        ApplicationName, FatherName, MobileNo,
                                        EmailId, PlotAreaSqFt, BuildingType,
                                        AppProcessingStage));

                                if (BuildConfig.DEBUG) {

                                    Log.e(TAG, "" + ApplicationNo + " -- ");
                                }
                            }

                            setAdapter();
                            tv_no_history_found.setVisibility(View.GONE);

                        } else {
                            Snackbar.make(ars_root_layout, "No Record found !", Snackbar.LENGTH_SHORT).show();
                            tv_no_history_found.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Snackbar.make(ars_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();
                if (t instanceof IOException) {
                    errorType = "Timeout";

                    Snackbar.make(ars_root_layout, "Please Check network Connection", Snackbar.LENGTH_SHORT).show();

                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(ars_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                } else {
                    errorType = "Error";
                    Snackbar.make(ars_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    //This method is for setting adapter for retrieved values
    public void setAdapter() {
        adapter = new BP_RequestStatus_Adapter(beanlist, BP_AppRequestStatus_Activity.this);
        bars_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bars_rv.setNestedScrollingEnabled(false);
    }
}

