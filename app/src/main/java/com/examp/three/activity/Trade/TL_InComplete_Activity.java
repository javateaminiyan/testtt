package com.examp.three.activity.Trade;

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
import com.examp.three.adapter.TradeLicence.TL_Incomplete_Adapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.model.TradeLicence.TL_Incomplete_Pojo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class TL_InComplete_Activity extends AppCompatActivity implements TL_Incomplete_Adapter.clicktoComplete {
    private static final String TAG = TL_InComplete_Activity.class.getName();
    RecyclerView incomp_rv;
    TL_Incomplete_Adapter adapter;
    ArrayList<TL_Incomplete_Pojo> beanlist;
    Toolbar incomp_toolbar;
    FloatingActionButton tl_fab_add_new;
    SpotsDialog spotsDialog;
    RelativeLayout tl_root_layout;
    String errorType;
    TextView tl_incmp_no_data_found;

    String ApplicationType,login_userId;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tl_incomplete);

        //new declaration
        beanlist = new ArrayList<>();

        //spotsdialog
        spotsDialog = new SpotsDialog(TL_InComplete_Activity.this);

        init();
        onClicks();

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        if (Common.isNetworkAvailable(getApplicationContext())) {
            getIncompleteDetails(login_userId);
        } else {
            Snackbar.make(tl_root_layout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }
    }

    //This method is for initializing all the view in layout activity
    public void init() {
        //recyclerview
        incomp_rv = (RecyclerView) findViewById(R.id.incomp_rv);

        //toolbar
        incomp_toolbar = (Toolbar) findViewById(R.id.incomp_toolbar);
        incomp_toolbar.setTitle("Trade Licence");
        setSupportActionBar(incomp_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //fab
        tl_fab_add_new = (FloatingActionButton) findViewById(R.id.tl_fab_add_new);

        //relative lay
        tl_root_layout = (RelativeLayout) findViewById(R.id.tl_root_layout);

        //textview
        tl_incmp_no_data_found = (TextView) findViewById(R.id.tl_incmp_no_data_found);

        //-----
        if (incomp_rv != null) {
            incomp_rv.setHasFixedSize(true);

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        incomp_rv.setLayoutManager(linearLayoutManager);

        //-----
    }

    public void onClicks() {
        tl_fab_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), NewRegister_Activity.class);
                i.putExtra("QueryType","New");
                startActivity(i);
            }
        });
    }

    //This method is for getting incomplete trade licence values
    public void getIncompleteDetails(String userId) {

        beanlist.clear();

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getTL_IncompleteDetails(Common.ACCESS_TOKEN, userId);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {

                    if (BuildConfig.DEBUG) {

//                        Log.e(TAG, "" + response.raw().toString());
                    }

                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONArray jsonArray = new JSONArray(sResponse);
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int Sno = jsonObject.optInt("Sno");
                                String EntryDate = jsonObject.optString("EntryDate");
                                String MobileNo = jsonObject.optString("MobileNo");
                                String Email = jsonObject.optString("Email");
                                String ApplicantName = jsonObject.optString("ApplicantName");
                                String District = jsonObject.optString("District");
                                String Panchayat = jsonObject.optString("Panchayat");

                                beanlist.add(new TL_Incomplete_Pojo(ApplicantName, MobileNo, Email, EntryDate, District, Panchayat, Sno));


                                if (BuildConfig.DEBUG) {

//                                    Log.e(TAG, "" + ApplicantName + " -- ");
                                }
                            }

                            setAdapter();
                            tl_incmp_no_data_found.setVisibility(View.GONE);
                            tl_root_layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                        } else {

                            Snackbar.make(tl_root_layout, "No data found !", Snackbar.LENGTH_SHORT).show();
                            tl_incmp_no_data_found.setVisibility(View.VISIBLE);
                            tl_incmp_no_data_found.setText("No data found !");
                            tl_root_layout.setBackgroundColor(getResources().getColor(R.color.white));

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                } else {

                    Snackbar.make(tl_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                    tl_incmp_no_data_found.setText("Something went wrong !");
                    tl_root_layout.setBackgroundColor(getResources().getColor(R.color.white));

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();

                if (t instanceof IOException) {

                    errorType = "Timeout";
                    Snackbar.make(tl_root_layout, "Please Check network Connection", Snackbar.LENGTH_SHORT).show();

                } else if (t instanceof IllegalStateException) {

                    errorType = "ConversionError";
                    Snackbar.make(tl_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                } else {

                    errorType = "Error";
                    Snackbar.make(tl_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    //This method is for getting all incomplete trade licence values
    public void getAllIncompleteDetails(int sno,String userid) {

        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getTL_AllIncompleteDetails(Common.ACCESS_TOKEN, sno, userid);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {

                    if (BuildConfig.DEBUG) {

//                        Log.e(TAG, "" + response.raw().toString());
                    }

                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONArray jsonArray = new JSONArray(sResponse);
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int Sno = jsonObject.optInt("Sno");
                                ApplicationType = jsonObject.optString("ApplicationType");
                                String ApplicantSurName = jsonObject.optString("ApplicantSurName");
                                String ApplicantFirstName = jsonObject.optString("ApplicantFirstName");
                                String ApGender = jsonObject.optString("ApGender");
                                String ApFGSurName = jsonObject.optString("ApFGSurName");
                                String ApFGFirstName = jsonObject.optString("ApFGFirstName");
                                String ApDoorNo = jsonObject.optString("ApDoorNo");
                                String ApStreet = jsonObject.optString("ApStreet");
                                String ApCity = jsonObject.optString("ApCity");
                                String ApDistrict = jsonObject.optString("ApDistrict");
                                String ApPIN = jsonObject.optString("ApPIN");
                                String Aadhar = jsonObject.optString("Aadhar");
                                String Tin = jsonObject.optString("Tin");
                                String MobileNo = jsonObject.optString("MobileNo");
                                String Email = jsonObject.optString("Email");

                                String sDistrict = jsonObject.optString("District");
                                String sPanchayat = jsonObject.optString("Panchayat");
                                String sWardNo = jsonObject.optString("WardNo");
                                String sStreetName = jsonObject.optString("StreetName");
                                String sDoorNo = jsonObject.optString("DoorNo");
                                String LicenceYear = jsonObject.optString("LicenceYear");


                                String LicenceTypeId = jsonObject.optString("LicenceTypeId");
                                String TradeDescription = jsonObject.optString("TradeDescription");
                                String EstablishmentName = jsonObject.optString("EstablishmentName");
                                String motorInstalled = jsonObject.optString("motorInstalled");
                                int motorTotalQty = jsonObject.optInt("motorTotalQty");
                                int HorsePowerTotal = jsonObject.optInt("HorsePowerTotal");
                                String RentalAgrStatus = jsonObject.optString("RentalAgrStatus");
                                String RentPaid = jsonObject.optString("RentPaid");
                                String NOCStatus = jsonObject.optString("NOCStatus");
                                int TradeRate = jsonObject.optInt("TradeRate");


                                String ApplicantPhoto = jsonObject.optString("ApplicantPhoto");
                                String AddressProofCopy = jsonObject.optString("AddressProofCopy");
                                String AgreementCopy = jsonObject.optString("AgreementCopy");
                                String NOCCopy = jsonObject.optString("NOCCopy");
                                String MachineSpecifications = jsonObject.optString("MachineSpecifications");
                                String MachineInstallationDig = jsonObject.optString("MachineInstallationDig");



//                                Log.e("sno 11111",""+Sno);
                                Intent intent = new Intent(TL_InComplete_Activity.this,NewRegister_Activity.class);
                                intent.putExtra("QueryType","Update");
                                intent.putExtra("Sno",Sno);
                                intent.putExtra("MobileNo",MobileNo);
                                intent.putExtra("Email",Email);
                                Log.e("hfhfhg",ApplicantSurName+"");
                                Log.e("hfhfhg",ApplicantFirstName+"");
                                Log.e("hfhfhg",ApFGSurName+"");
                                Log.e("hfhfhg",ApFGFirstName+"");
                                intent.putExtra("ApplicantSurName",ApplicantSurName);
                                intent.putExtra("ApplicantFirstName",ApplicantFirstName);
                                intent.putExtra("ApGender",ApGender);
                                intent.putExtra("ApFGSurName",ApFGSurName);
                                intent.putExtra("ApFGFirstName",ApFGFirstName);
                                intent.putExtra("ApDoorNo",ApDoorNo);
                                intent.putExtra("ApStreet",ApStreet);
                                intent.putExtra("ApCity",ApCity);
                                intent.putExtra("ApDistrict",ApDistrict);
                                intent.putExtra("ApPIN",ApPIN);
                                intent.putExtra("Aadhar",Aadhar);
                                intent.putExtra("GST",Tin);

                                intent.putExtra("sDistrict",sDistrict);
                                intent.putExtra("sPanchayat",sPanchayat);
                                intent.putExtra("sWardNo",sWardNo);
                                intent.putExtra("sStreetName",sStreetName);
                                intent.putExtra("sDoorNo",sDoorNo);
                                intent.putExtra("LicenceYear",LicenceYear);


                                intent.putExtra("LicenceTypeId",LicenceTypeId);
                                intent.putExtra("TradeDescription",TradeDescription);
                                intent.putExtra("EstablishmentName",EstablishmentName);
                                intent.putExtra("motorInstalled",motorInstalled);
                                intent.putExtra("motorTotalQty",motorTotalQty);
                                intent.putExtra("HorsePowerTotal",HorsePowerTotal);
                                intent.putExtra("RentalAgrStatus",RentalAgrStatus);
                                intent.putExtra("RentPaid",RentPaid);
                                intent.putExtra("NOCStatus",NOCStatus);
                                intent.putExtra("TradeRate",TradeRate);


                                intent.putExtra("ApplicantPhoto",ApplicantPhoto);
                                intent.putExtra("AddressProofCopy",AddressProofCopy);
                                intent.putExtra("AgreementCopy",AgreementCopy);
                                intent.putExtra("NOCCopy",NOCCopy);
                                intent.putExtra("MachineSpecifications",MachineSpecifications);
                                intent.putExtra("MachineInstallationDig",MachineInstallationDig);


                                startActivity(intent);


                                if (BuildConfig.DEBUG) {

//                                    Log.e(TAG, "" + ApplicationType + " -- ");
                                }

                            }


                        }
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                } else {

                    Snackbar.make(tl_root_layout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();
                    tl_incmp_no_data_found.setText("Something went wrong !");

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();

                if (t instanceof IOException) {

                    errorType = "Timeout";
                    Snackbar.make(tl_root_layout, "Please Check network Connection", Snackbar.LENGTH_SHORT).show();

                } else if (t instanceof IllegalStateException) {

                    errorType = "ConversionError";
                    Snackbar.make(tl_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                } else {

                    errorType = "Error";
                    Snackbar.make(tl_root_layout, errorType, Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    //This method is for setting adapter for retrieved values
    public void setAdapter() {

        adapter = new TL_Incomplete_Adapter(beanlist, TL_InComplete_Activity.this);
        incomp_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        incomp_rv.setNestedScrollingEnabled(false);

    }

    @Override
    public void clickToComplete(int position, int sno) {
//        Log.e("sno --- > ", "" + sno);

        getAllIncompleteDetails(sno,login_userId);
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
