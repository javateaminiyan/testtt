package com.examp.three.activity.NonTax.HelpherClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.examp.three.NewNavigation;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import com.examp.three.model.Birth_Death.Districts;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.listener.RecyclerClickListener;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 8/10/2018.
 */

public class NewAssessmentHelpher {
    public static NewAssessmentHelpher newAssessmentHelpher = null;
    public static final String TAG = NewAssessmentHelpher.class.getSimpleName();
    private Activity activity;
    Typeface avvaiyarfont;
    ArrayList<Districts> mDistrictsList = new ArrayList<>();
    ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> mStreetBeansList = new ArrayList<>();
    ArrayList<String> mDistrictList = new ArrayList<>();
    ArrayList<Districts> mPanchayatList = new ArrayList<>();
    ArrayList<String> mPanchayatsList = new ArrayList<>();
    ArrayList<String> mWardStringList = new ArrayList<>();
    ArrayList<String> mStreetStringList = new ArrayList<>();
    public int mdistrictCode;
    public int mpanchayatCode;
    public String mselectedWard;
    String mselectedStreetCode;
    String mselectedStreet;
    public String mselectedDistrict;
    public String mselectedPanchayat;
    SpinnerDialog spinnerDialog;
    AlertDialog newAssesmentdialog;

    private NewAssessmentHelpher(Activity activity) {
        this.activity = activity;
        this.avvaiyarfont = Typeface.createFromAsset(activity.getAssets(), "fonts/avvaiyar.ttf");
    }

    public static NewAssessmentHelpher getInstance(Activity activity) {
        if (newAssessmentHelpher == null) {
            newAssessmentHelpher = new NewAssessmentHelpher(activity);
        }
        newAssessmentHelpher.activity = activity;
        return newAssessmentHelpher;
    }

    //This method is for retriving the list of districts
    public void getDistricts(final EditText editText) {
        mDistrictsList.clear();
        mDistrictList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
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
                            Log.e(TAG, jsonObject.getString("DistrictName"));
                            mDistrictList.add(districtname);
                            mDistrictsList.add(new Districts(districtid, districtname));
                        }
                        pd.dismiss();
                        setSpinnerComman(mDistrictList, editText, "District");

                    } else {
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, t.toString());
            }
        });

    }

    //This method is for retrieving the list of panchayats
    public void getPanchayat(final EditText editText) {
        mPanchayatsList.clear();
        mPanchayatList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getPanchayat(Common.ACCESS_TOKEN, mdistrictCode);
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
                            int panchayatid = jsonObject.getInt("PanchayatId");
                            String panchayatName = jsonObject.getString("PanchayatName");
                            Log.e(TAG, jsonObject.getString("PanchayatName"));
                            mPanchayatList.add(new Districts(panchayatid, panchayatName));
                            mPanchayatsList.add(panchayatName);
                        }
                        pd.dismiss();
                        setSpinnerComman(mPanchayatsList, editText, "Panchayat");
                    } else {
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, t.toString());
            }
        });
    }

    //This method is for retrieving the list of ward no's
    public void getWardNo(final EditText editText) {
        mWardStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Log.e("Panchayat", mselectedPanchayat + "dghbdf");
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN, "Ward", "", mselectedDistrict, mselectedPanchayat);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    JSONArray jsonArray = records.getJSONArray("recordsets");
                    if (jsonArray.length() > 0) {
                        JSONArray wardArray = jsonArray.getJSONArray(0);
                        if (wardArray.length() > 0) {
                            for (int i = 0; i < wardArray.length(); i++) {
                                JSONObject wardObjects = wardArray.getJSONObject(i);
                                mWardStringList.add(wardObjects.getString("WardNo"));
                            }
                            pd.dismiss();
                            setSpinnerComman(mWardStringList, editText, "WardNo");
                        }
                    } else {
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
                Log.e(TAG, t.toString());
            }
        });
    }

    //This method is for retrieving the list of street names
    public void getStreetName(final EditText editText) {
        mStreetBeansList.clear();
        mStreetStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN, "Street", mselectedWard, mselectedDistrict, mselectedPanchayat);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    JSONArray jsonArray = records.getJSONArray("recordsets");
                    if (jsonArray.length() > 0) {
                        JSONArray streetArray = jsonArray.getJSONArray(0);
                        if (streetArray.length() > 0) {
                            for (int i = 0; i < streetArray.length(); i++) {
                                JSONObject streetObjects = streetArray.getJSONObject(i);
                                mStreetBeansList.add(new com.examp.three.model.Birth_Death.Street_Pojo( streetObjects.getString("StreetName"),streetObjects.getString("StreetNo")));
                                mStreetStringList.add(streetObjects.getString("StreetName"));
                            }
                            pd.dismiss();
                            showStreetValuesInAlert(mStreetBeansList, editText);
                        }
                    } else {
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
                Log.e(TAG, t.toString());
            }
        });
    }

    //This method is for showing the registration response
    public void showRegistrationResponse_Alert(String sTitle, String sDesc, String img) {

        int image = activity.getResources().getIdentifier(img, "drawable", activity.getPackageName());
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View v2 = activity.getLayoutInflater().inflate(R.layout.alert_response, null);
        mBuilder.setView(v2);
        mBuilder.setCancelable(false);

        TextView tv_title = v2.findViewById(R.id.tv_title);
        TextView tv_desc = v2.findViewById(R.id.tv_desc);
        Button btn_ok = v2.findViewById(R.id.btn_ok);
        ImageView iv_res = v2.findViewById(R.id.iv_res);

        iv_res.setImageResource(image);
        tv_title.setText(sTitle);
        tv_desc.setText(sDesc);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newAssesmentdialog.dismiss();
                Intent i = new Intent(activity, NewNavigation.class);
                i.putExtra("Type","NonTax");
                activity.startActivity(i);
                activity.finish();

            }
        });

        newAssesmentdialog = mBuilder.create();
        if (!activity.isFinishing()) {
            newAssesmentdialog.show();
        }

    }

    //This method is for showing street names in alert
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
                mselectedStreet = streetAdapter.getList().get(position).getStreetName();
                editText.setTypeface(avvaiyarfont);

                dialog.dismiss();
            }
        }));
    }

    //This method is for saving the Non tax new assessment no
    public void saveNewAssessmentNonTax(String name, String mobileno, String emailid, String leasename, String doorno) {
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.saveNonTaxDetails(Common.ACCESS_TOKEN, mselectedDistrict, mselectedPanchayat, name, mobileno, emailid, leasename, doorno, mselectedWard, mselectedStreetCode, mselectedStreet, "Android");
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    String resp = records.getString("message");
                    Log.e("response=>", resp);
                    pd.dismiss();
                    if (resp.startsWith("Success")) {
                        showRegistrationResponse_Alert("Success","Registered successfully!!","ic_success");
                    }else{
                        showRegistrationResponse_Alert("Failed", "Sorry, Submission Failed", "ic_error");
                    }
                    Log.e("resulttt", records.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, t.toString());
            }
        });
    }

    //This method is for setting the common spinner
    public void setSpinnerComman(final ArrayList<String> arrayList, final EditText editText, final String title) {

        spinnerDialog = new SpinnerDialog(activity, arrayList, "Select " + title, R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                editText.setText(arrayList.get(i));
                switch (title) {
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
                }

            }
        });
    }

}
