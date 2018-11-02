package com.examp.three.common.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.examp.three.BuildConfig;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Panchayats;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;

public class CommonMethods {
    private String TAG = CommonMethods.class.getSimpleName();

    private ArrayList<District_Pojo> arrayListDistrict = new ArrayList<>();
    private ArrayList<Panchayats> arrayListPanchayatPojo = new ArrayList<>();
    private ArrayList<String> sArrayList_DistrictNames = new ArrayList<>();
    private ArrayList<String> mPanchayatList = new ArrayList<>();

    //progress
    private ProgressDialog pd;

    private String errorType;
    private Context context;

    private CommonInterface commonInterface;

    public CommonMethods(CommonInterface commonInterface, Context context) {
        this.commonInterface = commonInterface;
        this.context = context;
    }

    //This method is for getting districts list
    public void getDistricts(final View view) {

        arrayListDistrict.clear();
        sArrayList_DistrictNames.clear();

        pd = new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getDistrict(Common.ACCESS_TOKEN);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                pd.dismiss();

                if (response.isSuccessful()) {
                    if (BuildConfig.DEBUG) {

                        Log.e(TAG, "url" + response.raw().toString());
                    }
                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONArray jsonArray = new JSONArray(sResponse);

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.optJSONObject(i);

                                String sDistrictName = jsonObject.optString("DistrictName");

                                int sDistrictId = jsonObject.optInt("DistrictId");

                                arrayListDistrict.add(new District_Pojo(sDistrictName, sDistrictId));

                                sArrayList_DistrictNames.add(arrayListDistrict.get(i).getdName());

                                if (BuildConfig.DEBUG) {

                                    Log.e(TAG, "" + sDistrictName + " -- " + sDistrictId);
                                }
                            }
                            commonInterface.getDistrict(arrayListDistrict, sArrayList_DistrictNames);


                        } else {
                            Toast.makeText(context, "No data found !", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                pd.dismiss();

                if (t instanceof IOException) {
                    errorType = "Timeout";
                    Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();

                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Toast.makeText(context, errorType, Toast.LENGTH_SHORT).show();

                } else {
                    errorType = "Error";
                    Toast.makeText(context, errorType, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //This method is for getting list of panchayats
    public void getTownPanchayat(Integer districtId, final View view) {

        mPanchayatList.clear();

        pd = new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.show();

        String REQUEST_TAG = "townPanchayat";


        JsonArrayRequest api_townPanchayat_Request = new JsonArrayRequest(Request.Method.GET, API_TOWNPANCHAYAT + districtId, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pd.dismiss();

                try {

                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject district_jsonObject = response.getJSONObject(i);

                            int mPanchayatId = district_jsonObject.optInt("PanchayatId");
                            String mPanchayatName = district_jsonObject.optString("PanchayatName");

                            mPanchayatList.add(mPanchayatName);
                            arrayListPanchayatPojo.add(new Panchayats(mPanchayatName,mPanchayatId));

                            Log.d("ldkflkdlflklfk", "------->   " + mPanchayatList.size());
                        }
                        if(commonInterface==null){
                            Log.e("error==>mPanchayatList",commonInterface+"");
                        }
                        if(mPanchayatList==null){
                            Log.e("error==>mPanchayatList",mPanchayatList+"");
                        }
                        commonInterface.getPanchayat(arrayListPanchayatPojo,mPanchayatList);


                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                }

            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pd.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Connection Time out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Could not Connect to Server", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

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

        Log.e(TAG, "URL Panchayat > " + api_townPanchayat_Request.getUrl());
        AppSingleton.getInstance(context).addToRequestQueue(api_townPanchayat_Request, REQUEST_TAG);
    }

}
