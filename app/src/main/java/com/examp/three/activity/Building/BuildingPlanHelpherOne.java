package com.examp.three.activity.Building;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import com.examp.three.model.Birth_Death.Districts;
import com.examp.three.model.Building.SurveyorBean;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.listener.RecyclerClickListener;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuildingPlanHelpherOne {
    private static BuildingPlanHelpherOne ourInstance ;
    public static final String TAG = BuildingPlanHelpherOne.class.getSimpleName();

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
    ArrayList<String> mBuildingStringList = new ArrayList<>();
    ArrayList<String> mSurveyarStringList = new ArrayList<>();
    ArrayList<SurveyorBean> mSurveyarBeanList = new ArrayList<>();
    ArrayList<String> mServiceCatagory = new ArrayList<>();
    ArrayList<String> mIndustryTypeStrings = new ArrayList<>();
    ArrayList<String> mService = new ArrayList<>();
    public int mdistrictCode;
    public int mpanchayatCode;
    public String mselectedWard;
    public String msurveyorId;
    public String mselectedStreetCode;
    public String mselectedStreet;
    public String mselectedDistrict;
    public String mselectedPanchayat;
    public String mBuildingHeight;
    public String mMinimumLandRequired;
    public String mFloorType;
    public String mFloorArea;

    public int building_plan_copy=0;
    public int propertyDocuments=0;
    public int pattaCertificate=0;
    public int registeredDocument=0;
    public int propertyHolderPhoto=0;
    public int photoOfAssessment=0;
    public int encumbranceCertificate=0;
    public int layoutCopy=0;
    public int demolitionPlan=0;
    public int probatedOrderCopy=0;
    public int gPADocumentCopy=0;
    public int reconstitutionDeed=0;
    public int legalHeirShipCertificate=0;
    public int landTaxReceipt=0;
    public int existPlanCopy=0;
    public int nOC=0;

    SpinnerDialog spinnerDialog;

    private BuildingPlanHelpherOne(Activity activity) {
        this.activity = activity;
        this.avvaiyarfont = Typeface.createFromAsset(activity.getAssets(), "fonts/avvaiyar.ttf");
    }

    public static BuildingPlanHelpherOne getInstance(Activity activity) {
            if(ourInstance == null){
                ourInstance = new BuildingPlanHelpherOne(activity);
            }
            if(ourInstance.activity != activity){
                ourInstance.activity = activity;
            }
        return ourInstance;
    }

    //This method is for getting list of districts
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

    //This method is for setting the selected district id
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

    //This method is for getting list of panchayats
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

    //This method is for getting list of Ward no's
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

    //This method is for getting list of streetnames
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
                                mStreetBeansPojoList.add(new
                                        com.examp.three.model.Birth_Death.Street_Pojo(streetObjects.optString("StreetName"),streetObjects.optString("StreetNo")));
                                mStreetStringList.add(streetObjects.optString("StreetName"));
                            }
                            pd.dismiss();
//                            setSpinnerComman(mStreetStringList,editText,"StreetName");
                            showStreetValuesInAlert(mStreetBeansPojoList, editText);
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

    public void getServiceCatagory(final EditText editText){
        mServiceCatagory.clear();
        mServiceCatagory.add("Building Plan Approval");
        setSpinnerComman(mServiceCatagory, editText, "Service Catagory");
    }

    //This method is for getting building types with selected district and panchayat
    public void getBuildingType(final EditText editText,final LinearLayout linearLayout,final TextView tv_first_info,final TextView tv_second_info,final  TextView tv_third_info,final TextView tv_fourth_info){
        mBuildingStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Log.e("Panchayat", mselectedPanchayat + "dghbdf");
        Call districtresult = retrofitInterface.getBuildingType(Common.ACCESS_TOKEN, mselectedPanchayat, mselectedDistrict);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        mBuildingStringList.add(jsonObject.optString("BuildingType"));
                    }
                    pd.dismiss();
//                    setSpinnerComman(mBuildingStringList, editText, "Select Building Type");
                    setSpinnerIndustryType(mBuildingStringList, editText, "Building Type",linearLayout,tv_first_info,tv_second_info,tv_third_info,tv_fourth_info);
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

    //This method is for getting surveyar names with selected district and panchayat
    public void getSurveyarName(final EditText editText){
        mSurveyarStringList.clear();
        mSurveyarBeanList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Log.e("Panchayat", mselectedPanchayat + "dghbdf");
        Call districtresult = retrofitInterface.getSurveyar(Common.ACCESS_TOKEN, mselectedPanchayat, mselectedDistrict);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        mSurveyarStringList.add(jsonObject.optString("SurveyorName"));
                        mSurveyarBeanList.add(new SurveyorBean(jsonObject.optString("SurveyorId"),jsonObject.optString("mSurveyorName")));
                    }
                    pd.dismiss();
                    setSpinnerComman(mSurveyarStringList, editText, "Select Surveyor Name");
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

    public void getService(final EditText editText){
        mService.clear();
        mService.add("Additional Construction");
        mService.add("Application for Demolition and Reconstruction");
        mService.add("New Building permit Approval in vaccant plot");
        mService.add("Regularisation of Individual Plot");
        setSpinnerComman(mService, editText, "Service");
    }

    public String convertSquareftToSquareMtrs(Float val){
        return String.valueOf(val/10.764);
    }

    public void getIndustryTypes(final EditText editText){
        mIndustryTypeStrings.clear();
        mIndustryTypeStrings.add("Cottage Industries");
        mIndustryTypeStrings.add("Green, Orange Catagory Industries");
        mIndustryTypeStrings.add("Heavy Industries");
        setSpinnerComman(mIndustryTypeStrings,editText,"Industries Type");
    }

    //This method is for showing street names in alert
    public void showStreetValuesInAlert(final ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> data_list, final  EditText editText) {

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

    //This method is for setting spinner for all retrieved districts, panchayats, ward no's, street names, building
    // types, surveyar names and industry types.
    public void setSpinnerComman(final ArrayList<String> arrayList, final EditText editText, final String title) {

        spinnerDialog = new SpinnerDialog(activity, arrayList, "Select " + title, R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                editText.setText(arrayList.get(i));
//                Log.e("distt== ", "" + mDistrictList.get(i).getMdistrictName() + " -- " + mDistrictList.get(i).getMdistrictId());
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
                    case "StreetName":
                        mselectedStreetCode = mStreetBeansList.get(i).getmStreetNum();
                        editText.setText(arrayList.get(i));
                        mselectedStreet = arrayList.get(i);
                        editText.setTypeface(avvaiyarfont);
                        break;

                    case "Building Type":
                        editText.setText(arrayList.get(i));
                        break;

                    case "Select Surveyor Name":
                        editText.setText(arrayList.get(i));
                        msurveyorId = mSurveyarBeanList.get(i).getmSurveyorId();
                        break;

                    case "Industries Type":
                        editText.setText(arrayList.get(i));
                        break;
                }

            }
        });
    }

    //This method is for setting industry types in spinner
    public void setSpinnerIndustryType(final ArrayList<String> arrayList, final EditText editText, final String title, final LinearLayout linearLayout,final TextView textView,final TextView textView1,final TextView textView2,final TextView textView3) {

        spinnerDialog = new SpinnerDialog(activity, arrayList, "Select " + title, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                editText.setText(arrayList.get(i));
//                Log.e("distt== ", "" + mDistrictList.get(i).getMdistrictName() + " -- " + mDistrictList.get(i).getMdistrictId());
                switch (title) {

                    case "Building Type":
                        editText.setText(arrayList.get(i));
                        if(arrayList.get(i).equalsIgnoreCase("Industries")){
                            linearLayout.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            textView1.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);
                            textView3.setVisibility(View.GONE);
                        }else{
                            getMeasurements(arrayList.get(i),textView,textView1,textView2,textView3);
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView1.setVisibility(View.VISIBLE);
                            textView2.setVisibility(View.VISIBLE);
                            textView3.setVisibility(View.VISIBLE);
                        }
                        break;
                }

            }
        });
    }

    //This method is for getting measurements
    public void getMeasurements(final String bType,final TextView textView,final TextView textView1,final TextView textView2,final TextView textView3){

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        Log.e("Panchayat", mselectedPanchayat + "dghbdf");
        Call districtresult = retrofitInterface.getMeasurementDetails(Common.ACCESS_TOKEN,mselectedPanchayat, mselectedDistrict,bType);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mBuildingHeight = jsonObject.optString("BuildingHeight");
                            mMinimumLandRequired = jsonObject.optString("MinimumLandRequired");
                            mFloorArea = jsonObject.optString("FloorArea");
                            mFloorType = jsonObject.optString("FloorType");
                        }
                    }
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("*Maximum allowable height of the building: up to "+mBuildingHeight+"meters height.");
                    textView1.setText("*Floor Type"+mFloorType);
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setText("*Maximum built up floor area:"+mFloorArea+"square meters.");
                    textView2.setVisibility(View.VISIBLE);
                    textView3.setText("* Minimum land required for applying for permission: "+mMinimumLandRequired+"Sqm");
                    textView3.setVisibility(View.VISIBLE);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    //This method is for types of files to choose
    public void showFileChooser(int PICK_IMAGE_REQUEST) {
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "image/*",
                        "text/html",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        activity.startActivityForResult(Intent.createChooser(intent, "ChooseFile"), PICK_IMAGE_REQUEST);
    }

    //This method is for uploading image or anytype of file which is choosen
    public void uploadImage(final File file, final String utype,final String applicationId,final String district,final String panchayat,final LinearLayout li_parent_lay) {

        Log.e("upload","function");
        RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("uploading");
        dialog.show();

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call<ResponseBody> req = retrofitInterface.UploadBuildingPlanDoc(body,applicationId,panchayat,district,"Y",utype);
        req.request();
        req.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("respomnse", "" + response.message());
                SnackShowTop(" Upload Success", li_parent_lay);
                switch ((utype)){
                    case "BuildingPlanCopy":
                        building_plan_copy = 1;
                        break;
                    case "PropertyDocuments":
                        propertyDocuments = 1;
                        break;
                    case "PattaCertificate":
                        pattaCertificate = 1;
                        break;
                    case "RegisteredDocument":
                        registeredDocument = 1;
                        break;
                    case "PropertyHolderPhoto":
                        propertyHolderPhoto = 1;
                        break;
                    case "DemolitionPlan":
                        demolitionPlan = 1;
                        break;
                    case "PhotoOfAssessment":
                        photoOfAssessment = 1;
                        break;
                    case "EncumbranceCertificate":
                        encumbranceCertificate = 1;
                        break;
                    case "LayoutCopy":
                        layoutCopy = 1;
                        break;
                    case "ProbatedOrderCopy":
                        probatedOrderCopy = 1;
                        break;
                    case "GPADocumentCopy":
                        gPADocumentCopy = 1;
                        break;
                    case "ReconstitutionDeed":
                        reconstitutionDeed = 1;
                        break;
                    case "LegalHeirShipCertificate":
                        legalHeirShipCertificate = 1;
                        break;
                    case "LandTaxReceipt":
                        landTaxReceipt = 1;
                        break;
                    case "ExistPlanCopy":
                        existPlanCopy = 1;
                        break;
                    case "NOC":
                        nOC = 1;
                        break;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("res==>", "" + t.getMessage());
                dialog.dismiss();

            }
        });
    }

    //This method is for showing snackbar in this activity
    public void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snack.show();
    }

    //Permission method
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("permission =>","Permission is granted1");
                return true;
            } else {

                Log.e("permission =>","Permission is revoked1");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e("permission =>","Permission is granted1");
            return true;
        }
    }

    //Permission method
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("permission =>","Permission is granted2");
                return true;
            } else {

                Log.e("permission =>","Permission is revoked2");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e("permission =>","Permission is granted2");
            return true;
        }
    }

}
