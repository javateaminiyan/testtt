package com.examp.three.fragment.Death_Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.examp.three.R;
import com.examp.three.activity.Death_Modules.DeathRegistration_Activity;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.common.helper.CommonMethods;
import com.examp.three.listener.RecyclerClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by priyadharshini on 31/07/2018.
 */

public class DeathRegistration_Second_Fragment extends Fragment  {
    View v;
    Typeface mAvvaiyarfont;
    SpinnerDialog spinnerDialog;

    public final String TAG = DeathRegistration_Second_Fragment.class.getSimpleName();

    //Hospital List
    List<com.examp.three.model.Birth_Death.HospitalBean> mHospitalBeanslist = new ArrayList<>();
    ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> mStreetBeansList = new ArrayList<>();

    //String Hospital list
    ArrayList<String> mHospitalStringList = new ArrayList<>();
    ArrayList<String> mWardStringList = new ArrayList<>();
    ArrayList<String> mStreetStringList = new ArrayList<>();

    ArrayList<String> mPlace_birth_list = new ArrayList<>();
    @Nullable
    @BindView(R.id.et_placeofbirth)EditText et_placeofbirth;
    @Nullable
    @BindView(R.id.et_hospname)EditText et_hospname;
    @Nullable
    @BindView(R.id.et_doorno)EditText et_doorno;
    @Nullable
    @BindView(R.id.et_wardno)EditText et_wardno;
    @Nullable
    @BindView(R.id.et_streetno)EditText et_streetno;
    @Nullable
    @BindView(R.id.btn_next_second)Button btn_next_second;
    @Nullable
    @BindView(R.id.et_death_address)EditText et_death_address;
    @Nullable
    @BindView(R.id.et_pincode)EditText et_pincode;
    @Nullable
    @BindView(R.id.li_parent_lay)LinearLayout li_parent_lay;

    //String declarations
    public String selected_district;
    public String selected_panchayat;
    public String selected_ward;
    public String selected_street_code;

    //integer declarations
    public int selected_hospital_code;
    public String street_code;

    //Object creation
    SharedPreferenceHelper sharedPreferenceHelpher;
    StreetAdapter streetAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_deathregistration_second,container,false);
        mAvvaiyarfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avvaiyar.ttf");
        ButterKnife.bind(this,v);
        onClicks();

        //Object initialization
        sharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());
        //lis items for place of birth
        mPlace_birth_list.add("House");
        mPlace_birth_list.add("Hospital");
        mPlace_birth_list.add("OtherPlaces");

//        getStreetNameHo();

        et_placeofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSpinnerPlaceBirth(mPlace_birth_list);
            }
        });
        et_hospname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_placeofbirth.getText().toString().equalsIgnoreCase("House") && !et_placeofbirth.getText().toString().equalsIgnoreCase("OtherPlaces")){
                    if(mHospitalStringList.size()>0){
                        setSpinnerHospital(mHospitalStringList);
                    }else{
                        if(Common.isNetworkAvailable(getActivity())){
                            getHospitalNames();
                        }else{
                            Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }else{
                    Snackbar.make(li_parent_lay,"Death place is Not a hospital",Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "Death place is Not a hospital", Toast.LENGTH_SHORT).show();
                }
            }
        });
        et_wardno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWardStringList.size()>0){
                    setSpinnerWard(mWardStringList);
                }else{
                    if(Common.isNetworkAvailable(getActivity())){
                        getWardNo();
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
        });
        et_streetno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(selected_ward!=null){
                   if(Common.isNetworkAvailable(getActivity())){
                       getStreetName();
                   }else{
                       Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                   }

               }else{
                   Snackbar.make(li_parent_lay,"Please select the ward!!",Snackbar.LENGTH_SHORT).show();
//                   Toast.makeText(getActivity(), "Please select the ward!!", Toast.LENGTH_SHORT).show();
               }
            }
        });

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            selected_district = sharedPreferenceHelpher.getSpecificValues("PREF_death_reg_District");
            selected_panchayat = sharedPreferenceHelpher.getSpecificValues("PREF_death_reg_Panchayat");

            Log.e("d",selected_district+" xf");
            Log.e("d",selected_panchayat+" xf");

        }
    }

    public void onClicks()
    {

        btn_next_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_placeofbirth.getText().toString().equalsIgnoreCase("House")){

                    if(!et_doorno.getText().toString().isEmpty()){
                        if(!et_streetno.getText().toString().isEmpty()){
                            if(!et_wardno.getText().toString().isEmpty()){
                                if(Common.isNetworkAvailable(getActivity())){
                                    nextFragement();
                                }else{
                                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                                }

                            }else{
                                Snackbar.make(li_parent_lay,"Ward no is required field!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else{
                            Snackbar.make(li_parent_lay,"Street Name is requiered field",Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        Snackbar.make(li_parent_lay,"Door no is required field",Snackbar.LENGTH_SHORT).show();
                    }
                }
                if(et_placeofbirth.getText().toString().equalsIgnoreCase("Hospital")){

                    if(!et_hospname.getText().toString().isEmpty()){
                        if(!et_doorno.getText().toString().isEmpty()){
                            if(!et_streetno.getText().toString().isEmpty()){
                                if(!et_wardno.getText().toString().isEmpty()){
                                    if(Common.isNetworkAvailable(getActivity())){
                                        nextFragement();
                                    }else{
                                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Snackbar.make(li_parent_lay,"Ward no is required field!",Snackbar.LENGTH_SHORT).show();
                                }
                            }else{
                                Snackbar.make(li_parent_lay,"Street Name is requiered field!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else{
                            Snackbar.make(li_parent_lay,"Door no is required field!",Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        Snackbar.make(li_parent_lay,"Hospital Name is required field!",Snackbar.LENGTH_SHORT).show();
                    }

                }
                if(et_placeofbirth.getText().toString().equalsIgnoreCase("OtherPlaces")){

                    if(!et_death_address.getText().toString().isEmpty()){
                        if(!et_pincode.getText().toString().isEmpty()){
                            if(Common.isNetworkAvailable(getActivity())){
                                nextFragement();
                            }else{
                                Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else{
                            Snackbar.make(li_parent_lay,"Door no is required field!",Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        Snackbar.make(li_parent_lay,"Death Address is required field!",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void nextFragement(){
        sharedPreferenceHelpher.putPlaceofDeath(et_placeofbirth.getText().toString(),
                String.valueOf((int)Math.round(selected_hospital_code)),

                et_hospname.getText().toString(),
                et_doorno.getText().toString(),
                et_wardno.getText().toString(),
                street_code,
                et_streetno.getText().toString(),
                et_death_address.getText().toString(),
                et_pincode.getText().toString()

        );
        int current = DeathRegistration_Activity.death_registration_pager.getCurrentItem();
        DeathRegistration_Activity.death_registration_pager.setCurrentItem(current+1);
    }
    public void setSpinnerPlaceBirth(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select Place of Birth", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                et_placeofbirth.setText(arrayList.get(i));
                if(arrayList.get(i).equalsIgnoreCase("House")){
                    et_hospname.getText().clear();
                    et_doorno.getText().clear();
                    et_doorno.setFocusable(true);
                    et_wardno.getText().clear();
                    et_wardno.setFocusable(true);
                    et_streetno.getText().clear();
                    et_streetno.setFocusable(true);
                    et_death_address.getText().clear();
                    et_death_address.setFocusable(false);
                    et_pincode.getText().clear();
                    et_pincode.setFocusable(false);
                }
                if(arrayList.get(i).equalsIgnoreCase("Hospital")){
                    et_hospname.getText().clear();
                    et_doorno.getText().clear();
                    et_wardno.getText().clear();
                    et_doorno.setFocusable(false);
                    et_wardno.setFocusable(false);
                    et_streetno.setFocusable(false);
                    et_streetno.getText().clear();
                    et_death_address.getText().clear();
                    et_death_address.setFocusable(false);
                    et_pincode.getText().clear();
                    et_pincode.setFocusable(false);
                }
                if(arrayList.get(i).equalsIgnoreCase("OtherPlaces")){
                    et_hospname.getText().clear();
                    et_hospname.setFocusable(false);
                    et_doorno.getText().clear();
                    et_doorno.setFocusable(false);
                    et_wardno.getText().clear();
                    et_wardno.setFocusable(false);
                    et_streetno.getText().clear();
                    et_streetno.setFocusable(false);
                    et_death_address.getText().clear();
                    et_death_address.setFocusable(true);
                    et_pincode.getText().clear();
                    et_pincode.setFocusable(true);
                }
            }
        });
    }
    public void setSpinnerHospital(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select Hospital", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                et_hospname.setText(arrayList.get(i));
                selected_hospital_code = mHospitalBeanslist.get(i).getmHospitalCode();
                if(Common.isNetworkAvailable(getActivity())){
                    getSpecificHospitalDetails();
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void setSpinnerStreet(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "தேர்ந்தெடு", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                et_streetno.setText(arrayList.get(i));
                et_streetno.setTypeface(mAvvaiyarfont);
                street_code = mStreetBeansList.get(i).getStreetNo();

            }
        });
    }
    public void setSpinnerWard(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select WardNo", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                et_wardno.setText(arrayList.get(i));
                selected_ward=arrayList.get(i);
            }
        });
    }

    public void getHospitalNames(){
        mHospitalStringList.clear();
        mHospitalBeanslist.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"BirthDeath","",selected_district,selected_panchayat);
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
                        JSONArray hospitalArray = jsonArray.getJSONArray(7);
                        if(hospitalArray.length()>0){
                            for(int i=0;i<hospitalArray.length();i++){
                                JSONObject hospitalObjects = hospitalArray.getJSONObject(i);
                                mHospitalBeanslist.add(new com.examp.three.model.Birth_Death.HospitalBean(hospitalObjects.getInt("HospitalCode"),hospitalObjects.getString("HospitalName")));
                                mHospitalStringList.add(hospitalObjects.getString("HospitalName"));
                            }
                            pd.dismiss();
                            setSpinnerHospital(mHospitalStringList);

                        }else{
                            pd.dismiss();
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
    public void getWardNo(){
        mWardStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"Ward","",selected_district,selected_panchayat);
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
                            setSpinnerWard(mWardStringList);

                        }else{
                            pd.dismiss();
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
    public void getStreetNameList(){
        mStreetBeansList.clear();
        mStreetStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"Street",selected_ward,selected_district,selected_panchayat);
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
                                mStreetBeansList.add(new com.examp.three.model.Birth_Death.Street_Pojo(streetObjects.getString("StreetName"),streetObjects.getString("StreetNo")));
                                mStreetStringList.add(streetObjects.getString("StreetName"));
                            }
                            for(int k=0;k<mStreetBeansList.size();k++){
                                Log.e("street code",mStreetBeansList.get(k).getStreetNo());
                                Log.e("street code==>",street_code);
                                Log.e("street name==>",mStreetBeansList.get(k).getStreetName());
                                if(street_code.equalsIgnoreCase(mStreetBeansList.get(k).getStreetNo())){
                                    et_streetno.setText(mStreetBeansList.get(k).getStreetName());
                                    et_streetno.setTypeface(mAvvaiyarfont);
                                }
                            }
                            pd.dismiss();

                        }
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
    public void getStreetName(){
        mStreetBeansList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"Street",selected_ward,selected_district,selected_panchayat);
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
                                mStreetBeansList.add(new com.examp.three.model.Birth_Death.Street_Pojo(streetObjects.getString("StreetName"),streetObjects.getString("StreetNo")));
                                mStreetStringList.add(streetObjects.getString("StreetName"));
                            }
                            pd.dismiss();
                            showStreetValuesInAlert(mStreetBeansList);

                        }else{
                            pd.dismiss();
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

    public void getSpecificHospitalDetails(){
        mStreetStringList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getMasterDetails(Common.ACCESS_TOKEN,"Hospital",String.valueOf((int)Math.round(selected_hospital_code)),selected_district,selected_panchayat);
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
                        JSONArray detailsArray = jsonArray.getJSONArray(0);
                        if(detailsArray.length()>0){
                            for(int i=0;i<detailsArray.length();i++){
                                JSONObject detailsObjects = detailsArray.getJSONObject(i);
                                et_doorno.setText(detailsObjects.getString("DoorNo"));
                                et_wardno.setText(detailsObjects.getString("WardCode"));
                                selected_ward = detailsObjects.getString("WardCode");
                                street_code =detailsObjects.getString("StreetCode");
                            }
                            if(Common.isNetworkAvailable(getActivity())){
                                getStreetNameList();
                            }else{
                                Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                            }
                            pd.dismiss();

                        }else{
                            pd.dismiss();
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
    public void showStreetValuesInAlert(final ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> data_list) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View v2 = getLayoutInflater().inflate(R.layout.recycler_alert, null);

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

        streetAdapter = new StreetAdapter(data_list);
        recyclerView.setAdapter(streetAdapter);
        streetAdapter.notifyDataSetChanged();

        mBuilder.setView(v2);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
                et_streetno.setText(streetAdapter.getList().get(position).getStreetName());
                street_code = streetAdapter.getList().get(position).getStreetNo();
                et_streetno.setTypeface(mAvvaiyarfont);
                dialog.dismiss();
            }
        }));
    }

}
