package com.examp.three.activity.Building;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.examp.three.BuildConfig;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Inject;
import com.examp.three.model.Building.BP_Incomplete_Pojo;
import com.examp.three.adapter.Building.BP_Incomplete_Adapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.listener.RecyclerClickListener;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class BP_InComplete_Activity extends AppCompatActivity implements Serializable {
    private static final String TAG = BP_InComplete_Activity.class.getName();
    RelativeLayout bp_incmp_root_layout;
    RecyclerView incomp_rv;
    BP_Incomplete_Adapter adapter;
    ArrayList<BP_Incomplete_Pojo> beanlist;
    Toolbar incomp_toolbar;
    FloatingActionButton bp_fab_add_new;
    SpotsDialog spotsDialog;
    TextView bp_incmp_no_data_found;
    String errorType,login_userId,login_username;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bp_incomplete);

        //new declaration
        beanlist = new ArrayList<>();

        //spotsdialog
        spotsDialog = new SpotsDialog(BP_InComplete_Activity.this);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");
        login_username = sharedPreference.getString(pref_login_firstname,"");

        init();
        onClick();

        if (Common.isNetworkAvailable(getApplicationContext())) {
            getIncompleteDetails(login_userId,login_username);
        } else {
            Snackbar.make(bp_incmp_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        incomp_rv.addOnItemTouchListener(new RecyclerClickListener(BP_InComplete_Activity.this, new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
//                EventBus.getDefault().postSticky(beanlist.get(position));
                Log.e("pos  ", "--> " + position);
                Intent intent = new Intent(BP_InComplete_Activity.this, NewApplicationActivity.class);
// using putExtra(String key, Parcelable value) method
                intent.putExtra("mUniqueId", beanlist.get(position).getmUniqueId());
                intent.putExtra("mPincode", beanlist.get(position).getmPincode());
                intent.putExtra("mUserId", beanlist.get(position).getmUserId());
                intent.putExtra("mServiceCategory", beanlist.get(position).getmServiceCategory());
                intent.putExtra("mServiceName", beanlist.get(position).getmServiceName());
                intent.putExtra("mApplicationNo", beanlist.get(position).getmApplicationNo());
                intent.putExtra("mApplicationDate", beanlist.get(position).getmApplicationDate());
                intent.putExtra("mApplicationName", beanlist.get(position).getmApplicationName());
                intent.putExtra("mFatherName", beanlist.get(position).getmFatherName());
                intent.putExtra("mMobileNo", beanlist.get(position).getmMobileNo());
                intent.putExtra("mDoorNo", beanlist.get(position).getmDoorNo());

                intent.putExtra("mCommunicationAddress", beanlist.get(position).getmCommunicationAddress());
                intent.putExtra("mEmailId", beanlist.get(position).getmEmailId());
                intent.putExtra("mSurveyNo", beanlist.get(position).getmSurveyNo());
                intent.putExtra("mDocumentNo", beanlist.get(position).getmDocumentNo());
                intent.putExtra("mWardNo", beanlist.get(position).getmWardNo());
                intent.putExtra("mStreetNo", beanlist.get(position).getmStreetNo());
                intent.putExtra("mTown", beanlist.get(position).getmTown());

                intent.putExtra("mCity", beanlist.get(position).getmCity());
                intent.putExtra("mPlatNo", beanlist.get(position).getmPlatNo());
                intent.putExtra("mBlockNo", beanlist.get(position).getmBlockNo());
                intent.putExtra("mBuildingType", beanlist.get(position).getmBuildingType());
                intent.putExtra("mPlotAreaSqFt", beanlist.get(position).getmPlotAreaSqFt());
                intent.putExtra("mPlotAreaSqMt", beanlist.get(position).getmPlotAreaSqMt());
                intent.putExtra("mSurveyorName", beanlist.get(position).getmSurveyorName());

                intent.putExtra("mAppStatus", beanlist.get(position).getmAppStatus());
                intent.putExtra("mAppDescription", beanlist.get(position).getmAppDescription());
                intent.putExtra("mAppProcessingStage", beanlist.get(position).getmAppProcessingStage());
                intent.putExtra("mUserName", beanlist.get(position).getmUserName());
                intent.putExtra("mDistrict", beanlist.get(position).getmDistrict());
                intent.putExtra("mPanchayat", beanlist.get(position).getmPanchayat());
                intent.putExtra("mCreatedBy", beanlist.get(position).getmCreatedBy());
                intent.putExtra("mCreatedDate", beanlist.get(position).getmCreatedDate());
                intent.putExtra("mUpdatedBy", beanlist.get(position).getmUpdatedBy());
                intent.putExtra("mUpdatedDate", beanlist.get(position).getmUpdatedDate());
                intent.putExtra("mAppDate", beanlist.get(position).getmAppDate());
                intent.putExtra("isNeedToUpdate",true);

                startActivity(intent);
            }
        }));
    }

    public void init() {

        //toolbar
        incomp_toolbar = (Toolbar) findViewById(R.id.bp_incomp_toolbar);

        //recyclcerview
        incomp_rv = (RecyclerView) findViewById(R.id.bp_incomp_rv);

        //fab
        bp_fab_add_new = (FloatingActionButton) findViewById(R.id.bp_fab_add_new);

        //textview
        bp_incmp_no_data_found = (TextView) findViewById(R.id.bp_incmp_no_data_found);

        //relative lay
        bp_incmp_root_layout = (RelativeLayout) findViewById(R.id.bp_incmp_root_layout);

        setSupportActionBar(incomp_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //-----
        if (incomp_rv != null) {
            incomp_rv.setHasFixedSize(true);

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        incomp_rv.setLayoutManager(linearLayoutManager);

        //-----
    }

    //This method for setting adapter for retrieved values
    public void setAdapter() {

        adapter = new BP_Incomplete_Adapter(beanlist);
        incomp_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        incomp_rv.setNestedScrollingEnabled(false);
    }

    public void onClick() {

        bp_fab_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent im = new Intent(BP_InComplete_Activity.this,NewApplicationActivity.class);
                im.putExtra("isNeedToUpdate",false);
                startActivity(im);
            }
        });
    }

    //This method is for retrieving incompleted details from API
    public void getIncompleteDetails(String userid,String username) {

        beanlist.clear();

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getBuildingLicence_IncompleteDetails(Common.ACCESS_TOKEN, userid, username);

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

                                String mCommunicationAddress,mEmailId,mSurveyNo,mDocumentNo,mDoorNo,mWardNo,mStreetNo,
                                        mTown;
                                String mCity,mPincode,mPlatNo,mBlockNo,mBuildingType,mPlotAreaSqFt,mPlotAreaSqMt,
                                        mSurveyorName;
                                String mAppStatus,mAppDescription,mAppProcessingStage,mUserId,mUserName,mDistrict,
                                        mPanchayat,mCreatedBy;
                                String mCreatedDate,mUpdatedBy,mUpdatedDate,mAppDate;

                                int UniqueId = jsonObject.optInt("UniqueId");
                                String ServiceCategory = jsonObject.optString("ServiceCategory");
                                String ServiceName = jsonObject.optString("ServiceName");
                                String ApplicationNo = jsonObject.optString("ApplicationNo");
                                String ApplicationDate = jsonObject.optString("ApplicationDate");
                                String ApplicationName = jsonObject.optString("ApplicationName");
                                String FatherName = jsonObject.optString("FatherName");
                                String MobileNo = jsonObject.optString("MobileNo");
                                String CommunicationAddress = jsonObject.optString("CommunicationAddress");
                                String EmailId = jsonObject.optString("EmailId");
                                String SurveyNo = jsonObject.optString("SurveyNo");
                                String DocumentNo = jsonObject.optString("DocumentNo");
                                String DoorNo = jsonObject.optString("DoorNo");
                                String WardNo = jsonObject.optString("WardNo");
                                String StreetNo = jsonObject.optString("StreetNo");
                                String Town = jsonObject.optString("Town");
                                String City = jsonObject.optString("City");
                                int Pincode = jsonObject.optInt("Pincode");
                                String PlatNo = jsonObject.optString("PlatNo");
                                String BlockNo = jsonObject.optString("BlockNo");
                                String BuildingType = jsonObject.optString("BuildingType");
                                String PlotAreaSqFt = jsonObject.optString("PlotAreaSqFt");
                                String PlotAreaSqMt = jsonObject.optString("PlotAreaSqMt");
                                String SurveyorName = jsonObject.optString("SurveyorName");
                                String AppStatus = jsonObject.optString("AppStatus");
                                String AppDescription = jsonObject.optString("AppDescription");
                                String AppProcessingStage = jsonObject.optString("AppProcessingStage");
                                int UserId = jsonObject.optInt("UserId");
                                String UserName = jsonObject.optString("UserName");
                                String District = jsonObject.optString("District");
                                String Panchayat = jsonObject.optString("Panchayat");
                                String CreatedBy = jsonObject.optString("CreatedBy");
                                String CreatedDate = jsonObject.optString("CreatedDate");
                                String UpdatedBy = jsonObject.optString("UpdatedBy");
                                String UpdatedDate = jsonObject.optString("UpdatedDate");
                                String AppDate = jsonObject.optString("AppDate");

                                beanlist.add(new BP_Incomplete_Pojo(UniqueId,ServiceCategory,ServiceName,
                                       ApplicationNo,ApplicationDate,ApplicationName,FatherName,
                                       MobileNo,CommunicationAddress,EmailId,SurveyNo,DocumentNo,
                                       DoorNo,WardNo,StreetNo,Town,City,Pincode,PlatNo,
                                       BlockNo,BuildingType,PlotAreaSqFt,PlotAreaSqMt,SurveyorName,
                                       AppStatus,AppDescription,AppProcessingStage,UserId,UserName,
                                       District,Panchayat,CreatedBy
                                        ,CreatedDate,UpdatedBy,UpdatedDate,AppDate));

                                if (BuildConfig.DEBUG) {

                                    Log.e(TAG, "" + ApplicationNo + " -- ");
                                }
                            }

                            setAdapter();
                            bp_incmp_no_data_found.setVisibility(View.GONE);

                        } else {

                            Snackbar.make(bp_incmp_root_layout, "No data found !", Snackbar.LENGTH_SHORT).show();
                            bp_incmp_no_data_found.setVisibility(View.VISIBLE);
                            bp_incmp_no_data_found.setText("No data found !");

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {

                    Snackbar.make(bp_incmp_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                    bp_incmp_no_data_found.setText("Something went wrong !");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();

                if (t instanceof IOException) {

                    errorType = "Timeout";
                    Snackbar.make(bp_incmp_root_layout, "Please Check network Connection", Snackbar.LENGTH_SHORT).show();

                } else if (t instanceof IllegalStateException) {

                    errorType = "ConversionError";
                    Snackbar.make(bp_incmp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                } else {

                    errorType = "Error";
                    Snackbar.make(bp_incmp_root_layout, errorType, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spotsDialog != null) {
            spotsDialog.dismiss();
            spotsDialog = null;
        }
    }

}
