package com.examp.three.activity.Trade.Helpher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examp.three.R;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.adapter.TradeLicence.TradeNameAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.listener.RecyclerClickListener;
import com.examp.three.model.Birth_Death.Districts;
import com.examp.three.model.TradeLicence.LicenseTypeBean;
import com.examp.three.model.TradeLicence.TradeBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 8/10/2018.
 */

public class LicenseHelpher {
    public static LicenseHelpher mLicenseHelpher = null;
    public static final String TAG = LicenseHelpher.class.getSimpleName();
    private Activity activity;
    Typeface avvaiyarfont;
    ArrayList<Districts> mDistrictsList = new ArrayList<>();
    ArrayList<com.examp.three.model.Birth_Death.StreetBean> mStreetBeansList = new ArrayList<>();
    ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> mStreetBeansPojoList = new ArrayList<>();
    ArrayList<String> mDistrictList = new ArrayList<>();
    ArrayList<Districts> mPanchayatList = new ArrayList<>();
    ArrayList<String> mPanchayatsList = new ArrayList<>();
    ArrayList<String> mWardStringList = new ArrayList<>();
    ArrayList<String> mStreetStringList = new ArrayList<>();
    ArrayList<String> mLicenseYear = new ArrayList<>();
    ArrayList<String> mLicenseTypeStringList = new ArrayList<>();
    ArrayList<String> mTradeNameStringList = new ArrayList<>();
    ArrayList<LicenseTypeBean>mLicenseTypeBeanList = new ArrayList<>();
    ArrayList<TradeBean>mTradeNameBeanList = new ArrayList<>();

    public int mdistrictCode;
    public int mpanchayatCode;
    public String mselectedWard;
    public String mselectedStreetCode;
    public String mselectedStreet;
    public String mselectedDistrict;
    public String validFromDate,validToDate;
    public String mselectedPanchayat;
    public int mLicenceTypeId;
    public String mLicenceTypeNameEnglish;
    public String mTradeName;
    public String mFinancialYear;
    public String mTradeCode;
    public String mTradeRate;

    SpinnerDialog spinnerDialog;

    private LicenseHelpher(Activity activity){
        this.activity = activity;
        this.avvaiyarfont = Typeface.createFromAsset(activity.getAssets(), "fonts/avvaiyar.ttf");
    }

    public static LicenseHelpher getInstance(Activity activity){
        if(mLicenseHelpher == null){
            mLicenseHelpher = new LicenseHelpher(activity);
        }
        if(mLicenseHelpher.activity != activity){
            mLicenseHelpher.activity = activity;
        }
        return mLicenseHelpher;
    }

    //This method is for getting districts
    public void getDistricts(final EditText editText){
        mDistrictsList.clear();
        mDistrictList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        if(!pd.isShowing()){
            pd.show();
        }
        Call districtresult = retrofitInterface.getDistricts(Common.ACCESS_TOKEN);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int districtid = jsonObject.getInt("DistrictId");
                            String districtname = jsonObject.getString("DistrictName");
                            Log.e(TAG,jsonObject.getString("DistrictName"));
                            mDistrictList.add(districtname);
                            mDistrictsList.add(new Districts(districtid,districtname));
                        }
                        pd.dismiss();
                        setSpinnerComman(mDistrictList,editText,"District");

                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });

    }

    //This method is for getting licence for value
    public void getLicenseFor(final EditText editText){
        mLicenseYear.clear();
        mLicenseYear.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        if(!pd.isShowing()){
            pd.show();
        }
        Call districtresult = retrofitInterface.getLicenseFor(Common.ACCESS_TOKEN);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String year = jsonObject.optString("LicenceFor");
                            mLicenseYear.add(year);
                        }
                        pd.dismiss();
                        setSpinnerComman(mLicenseYear,editText,"Select Year");

                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });

    }

    //This method is for getting valid from and to
    public void getValidFromTo(String years){
        String finyr[] = years.split("-");
        validFromDate = finyr[0]+"-04"+"-01";
        validToDate = finyr[1]+"-03"+"-31";
    }

    //This method is for getting panchayat
    public void getPanchayat(final EditText editText){
        mPanchayatsList.clear();
        mPanchayatList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getPanchayat(Common.ACCESS_TOKEN,mdistrictCode);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int panchayatid = jsonObject.getInt("PanchayatId");
                            String panchayatName = jsonObject.getString("PanchayatName");
                            Log.e(TAG,jsonObject.getString("PanchayatName"));
                            mPanchayatList.add(new Districts(panchayatid,panchayatName));
                            mPanchayatsList.add(panchayatName);
                        }
                        pd.dismiss();
                        setSpinnerComman(mPanchayatsList,editText,"Panchayat");
                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });
    }

    //This method is for getting ward no
    public void getWardNo(final EditText editText){
        mWardStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Log.e("Panchayat",mselectedPanchayat+"dghbdf");
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"Ward","",mselectedDistrict,mselectedPanchayat);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    JSONArray jsonArray = records.getJSONArray("recordsets");
                    if(jsonArray.length()>0){
                        JSONArray wardArray = jsonArray.getJSONArray(0);
                        if(wardArray.length()>0){
                            for(int i=0;i<wardArray.length();i++){
                                JSONObject wardObjects = wardArray.getJSONObject(i);
                                mWardStringList.add(wardObjects.getString("WardNo"));
                            }
                            pd.dismiss();
                            setSpinnerComman(mWardStringList,editText,"WardNo");

                        }
                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });
    }

    //This method is for getting street name
    public void getStreetName(final EditText editText){
        mStreetBeansList.clear();
        mStreetStringList.clear();
        mStreetBeansPojoList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"Street",mselectedWard,mselectedDistrict,mselectedPanchayat);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    JSONArray jsonArray = records.getJSONArray("recordsets");
                    if(jsonArray.length()>0){
                        JSONArray streetArray = jsonArray.getJSONArray(0);
                        if(streetArray.length()>0){
                            for(int i=0;i<streetArray.length();i++){
                                JSONObject streetObjects = streetArray.getJSONObject(i);
                                mStreetBeansList.add(new com.examp.three.model.Birth_Death.StreetBean(streetObjects.optString("StreetNo"),streetObjects.optString("StreetName")));
                                mStreetBeansPojoList.add(new com.examp.three.model.Birth_Death.Street_Pojo(streetObjects.optString("StreetName"),streetObjects.optString("StreetNo")));
                                mStreetStringList.add(streetObjects.optString("StreetName"));
                            }
                            pd.dismiss();
//                            setSpinnerComman(mStreetStringList,editText,"StreetName");
                            showStreetValuesInAlert(mStreetBeansPojoList,editText);
                        }
                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });
    }

    //This method is for getting licence type value
    public void getLicenceType(final EditText editText){
        mLicenseTypeStringList.clear();
        mLicenseTypeBeanList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getLicenseTypeMaster(Common.ACCESS_TOKEN);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);

                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        int mTempLicenceTypeId = jsonObject.optInt("LicenceTypeId");
                        String mTempLicenceTypeNameEnglish = jsonObject.getString("LicenceTypeNameEnglish");
                        mLicenseTypeStringList.add(mTempLicenceTypeNameEnglish);
                        mLicenseTypeBeanList.add(new LicenseTypeBean(mTempLicenceTypeId,mTempLicenceTypeNameEnglish));
                    }
                  setSpinnerComman(mLicenseTypeStringList,editText,"License Type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });

    }

    //This method is for getting licence name value
    public void getLicenseName(final String id, final EditText editText){
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getLicenseTypeMaster(Common.ACCESS_TOKEN);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);

                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        int mTempLicenceTypeId = jsonObject.optInt("LicenceTypeId");
//                        String mTempLicenceTypeNameEnglish = jsonObject.getString("LicenceTypeNameEnglish");
                        Log.e("sss",id+"equals "+mTempLicenceTypeId);
                        if(Integer.parseInt(id)==(mTempLicenceTypeId)){
                           String mTempLicenceTypeNameEnglish = jsonObject.getString("LicenceTypeNameEnglish");
                            mLicenceTypeNameEnglish = mTempLicenceTypeNameEnglish;
                            editText.setText(mLicenceTypeNameEnglish);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });
    }

    //This method is for getting list of trade names
    public void getTradeName(final EditText editText, final TextView textView){
        mTradeNameStringList.clear();
        mTradeNameBeanList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        if(!pd.isShowing()){
            pd.show();
        }
        Call districtresult = retrofitInterface.getTradeName(Common.ACCESS_TOKEN,mselectedDistrict,mselectedPanchayat,String.valueOf(mLicenceTypeId));
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("url==>",response.raw().toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);

                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String mCode = jsonObject.getString("Code");
                        String mDescriptionT = jsonObject.getString("DescriptionT");
                        String mDescriptionE = jsonObject.getString("DescriptionE");
                        String mTradeRate = jsonObject.getString("TradeRate");
                        mTradeNameStringList.add(mDescriptionT);
                        mTradeNameBeanList.add(new TradeBean(mDescriptionT,mDescriptionE,mTradeRate,mCode));
                    }
                    showTradeNamesInAlert(mTradeNameBeanList,editText,textView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });

    }

    //This method is for setting spinner in common
    public void setSpinnerComman(final ArrayList<String> arrayList, final EditText editText, final String title) {

        spinnerDialog = new SpinnerDialog(activity, arrayList, "Select "+title, R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                editText.setText(arrayList.get(i));
//                Log.e("distt== ", "" + mDistrictList.get(i).getMdistrictName() + " -- " + mDistrictList.get(i).getMdistrictId());
                switch (title){
                    case "District":
                        mdistrictCode = mDistrictsList.get(i).getmDistrictId();
                        mselectedDistrict = arrayList.get(i);
                        break;
                    case "Panchayat":
                        mpanchayatCode = mPanchayatList.get(i).getmDistrictId();
                        mselectedPanchayat = arrayList.get(i);
                        break;
                    case "WardNo":
                        mselectedWard = arrayList.get(i);
                        break;
                    case "StreetName":
                        mselectedStreetCode = mStreetBeansList.get(i).getmStreetNum();
                        editText.setText(arrayList.get(i));
                        mselectedStreet = arrayList.get(i);
                        editText.setTypeface(avvaiyarfont);
                        break;
                    case "Select Year":
                        editText.setText(arrayList.get(i));
                        if(arrayList.get(i).length()>0)
                        {
                            mFinancialYear = arrayList.get(i);
                            getValidFromTo(arrayList.get(i));
                        }
                        break;
                    case "License Type":
                        editText.setText(arrayList.get(i));
                        mLicenceTypeId = mLicenseTypeBeanList.get(i).getmLicenceTypeId();
                        mLicenceTypeNameEnglish = mLicenseTypeBeanList.get(i).getmLicenceTypeNameEnglish();
                        break;
                    case "Select Trade Name":
                        editText.setText(arrayList.get(i));
                        mTradeName = mTradeNameBeanList.get(i).getmDescriptionT();
                        mTradeCode= mTradeNameBeanList.get(i).getmCode();
                        mTradeRate= mTradeNameBeanList.get(i).getmTradeRate();
                        editText.setTypeface(avvaiyarfont);
                        break;
                }

//                district_Name = mDistrictList.get(i).getMdistrictName();

            }
        });
    }

    //This method is for showing street values in alert
    public void showStreetValuesInAlert(final ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> data_list, final EditText editText) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View v2 = activity.getLayoutInflater().inflate(R.layout.recycler_alert, null);



        mBuilder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        RecyclerView recyclerView = (RecyclerView) v2.findViewById(R.id.recycler);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v2.getContext());
        recyclerView.setLayoutManager(layoutManager);


        final StreetAdapter streetAdapter = new StreetAdapter(data_list);
        recyclerView.setAdapter(streetAdapter);
        streetAdapter.notifyDataSetChanged();

        mBuilder.setView(v2);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        recyclerView.addOnItemTouchListener(new RecyclerClickListener(activity, new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
                editText.setText(streetAdapter.getList().get(position).getStreetName());
                mselectedStreetCode = streetAdapter.getList().get(position).getStreetNo();
                editText.setTypeface(avvaiyarfont);
                dialog.dismiss();
            }
        }));
    }

    //This method is for showing trade names in alert
    public void showTradeNamesInAlert(final ArrayList<TradeBean> data_list, final EditText editText, final TextView textView) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View v2 = activity.getLayoutInflater().inflate(R.layout.recycler_alert, null);



        mBuilder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        RecyclerView recyclerView = (RecyclerView) v2.findViewById(R.id.recycler);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v2.getContext());
        recyclerView.setLayoutManager(layoutManager);


        final TradeNameAdapter tradeNameAdapter = new TradeNameAdapter(data_list);
        recyclerView.setAdapter(tradeNameAdapter);
        tradeNameAdapter.notifyDataSetChanged();

        mBuilder.setView(v2);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        recyclerView.addOnItemTouchListener(new RecyclerClickListener(activity, new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
                editText.setText(tradeNameAdapter.getList().get(position).getmDescriptionT());
                mTradeCode = tradeNameAdapter.getList().get(position).getmCode();
                mTradeName = tradeNameAdapter.getList().get(position).getmDescriptionE();
                mTradeRate = tradeNameAdapter.getList().get(position).getmTradeRate();
                editText.setTypeface(avvaiyarfont);
                Log.e("fv=>TradeRate",tradeNameAdapter.getList().get(position).getmTradeRate());
                Log.e("fv=>Desc E",tradeNameAdapter.getList().get(position).getmDescriptionE());
                textView.setText(tradeNameAdapter.getList().get(position).getmTradeRate());
                dialog.dismiss();
            }
        }));
    }

    //This method is for setting district id with name
    public void setDistrictId(final String name){
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        Call districtresult = retrofitInterface.getDistricts(Common.ACCESS_TOKEN);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int districtid = jsonObject.getInt("DistrictId");
                            String districtname = jsonObject.getString("DistrictName");
                            if(name.equalsIgnoreCase(districtname)){
                                mdistrictCode = districtid;
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
