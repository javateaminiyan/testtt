package com.examp.three.fragment.Building;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.activity.Building.BuildingPlanHelpherOne;
import com.examp.three.activity.Building.ChecklistDetails;
import com.examp.three.activity.Building.NewApplicationActivity;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.Building.BP_Incomplete_Pojo;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;


public class BuildingLicenseThirdFragment extends Fragment implements Validator.ValidationListener{
View mThirdFragmentView;

@NotEmpty(message = "Building Type is a required field")
@Nullable
@BindView(R.id.et_building_type) EditText et_building_type;

@Nullable
@BindView(R.id.et_plot_sq_m) EditText et_plot_sq_m;

@Nullable
@BindView(R.id.et_industryType) EditText et_industryType;

@NotEmpty(message = "Plot Area is a required field")
@Nullable
@BindView(R.id.et_plot_area) EditText et_plot_area;

@NotEmpty(message = "Surveyar Name is a required field")
@Nullable
@BindView(R.id.et_surveyar_name) EditText et_surveyar_name;

@Nullable
@BindView(R.id.li_parent_lay) LinearLayout li_parent_lay;

@Nullable
@BindView(R.id.li_industryType) LinearLayout li_industryType;

@Nullable
@BindView(R.id.tv_frst_info) TextView tv_frst_info;

@Nullable
@BindView(R.id.tv_second_info) TextView tv_second_info;

@Nullable
@BindView(R.id.tv_third_info) TextView tv_third_info;

@Nullable
@BindView(R.id.tv_fourth_info) TextView tv_fourth_info;

@Nullable
@BindView(R.id.btnSubmit)
Button btnSubmit;

    @Nullable
    @BindView(R.id.btn_back)
    Button btn_back;

    String mQueryType,mServiceCategory,mServiceName, mApplicationDate;
    String mApplicationName,mFatherName, mMobileNo, mCommunicationAddress;
    String mEmailID,mSurveyNo,mDocumentNo,mDoorNo,mWardNo,mStreetNo="",mTown;
    String mCity,mPincode,mPlatNo,mBlockNo,mBuildingType,mPlotAreaSqFt;
    String mPlotAreaSqMt,mSurveyorName,mAppStatus="", mAppDescription="Testing";
    String mUserId="",mUserName="",mPanchayat,mDistrict,mCreatedBy="";
    String getmApplicationId;

Validator validator;
BuildingPlanHelpherOne mBuildingPlanHelpherOne;
SharedPreferenceHelper mSharedPreferenceHelpher;


    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mThirdFragmentView =  inflater.inflate(R.layout.fragment_building_license_third, container, false);

        ButterKnife.bind(this,mThirdFragmentView);

        mBuildingPlanHelpherOne = BuildingPlanHelpherOne.getInstance(getActivity());

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        et_building_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getBuildingType(et_building_type,li_industryType,tv_frst_info,tv_second_info,tv_third_info,tv_fourth_info);
            }
        });
        et_surveyar_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getSurveyarName(et_surveyar_name);
            }
        });
        et_industryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getIndustryTypes(et_industryType);
            }
        });
        et_plot_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>0){
                    et_plot_sq_m.getText().clear();
                    et_plot_sq_m.setText(mBuildingPlanHelpherOne.convertSquareftToSquareMtrs(Float.parseFloat(s.toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = NewApplicationActivity.mLicense_registration_pager.getCurrentItem();
                NewApplicationActivity.mLicense_registration_pager.setCurrentItem(current-1);
            }
        });

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mUserId= sharedPreference.getString(pref_login_userid,"");
        mUserName = sharedPreference.getString(pref_login_firstname,"");
        return mThirdFragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            mSharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());

            mQueryType = "Insert";
            mApplicationDate = "27/8/2018";
            mServiceCategory = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_servicecatagory");
            mServiceName = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_service");
            mApplicationName = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_applicationName");
            mFatherName = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_father_hub_name");
            mMobileNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_mobileno");
            mEmailID = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_emailid");
            mCommunicationAddress = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_communicationAddress");
            mSurveyNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_surveyNo");
            mDocumentNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_documentNo");
            mStreetNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_Street");
            Log.e("gbfhfgb",mStreetNo+"fhd");
            mDoorNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_doorNo");
            mWardNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_wardNo");
            mTown = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_district");
            mCity = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_town");
            mPincode = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_pincode");
            mPlatNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_plotno");
            mBlockNo = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_blockno");
            mPanchayat = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_town");
            mDistrict = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_district");
        }
    }

    @Override
    public void onValidationSucceeded() {
        mBuildingType = et_building_type.getText().toString();
        mPlotAreaSqFt = et_plot_area.getText().toString();
        mPlotAreaSqMt = et_plot_sq_m.getText().toString();
        mSurveyorName = et_surveyar_name.getText().toString();

        if(Common.isNeedToUpdateBuildingPlan){
            Log.e("gfhgb","Update=>"+mStreetNo);
            mQueryType = "Update";
            updateFunction();
        }else{
            Log.e("gfhgb","insert");
            saveFunction();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(BP_Incomplete_Pojo mBp_incomplete_pojo){
        if(!TextUtils.isEmpty(mBp_incomplete_pojo.getmBuildingType()))
            et_building_type.setText(mBp_incomplete_pojo.getmBuildingType());
        if(!mBp_incomplete_pojo.getmBuildingType().equalsIgnoreCase("Industries"))
            mBuildingPlanHelpherOne.getMeasurements(mBp_incomplete_pojo.getmBuildingType(),tv_frst_info,tv_second_info,tv_third_info,tv_fourth_info);
        if(!TextUtils.isEmpty(mBp_incomplete_pojo.getmSurveyorName()))
            et_surveyar_name.setText(mBp_incomplete_pojo.getmSurveyorName());
        if(!TextUtils.isEmpty(mBp_incomplete_pojo.getmPlotAreaSqFt()))
            et_plot_area.setText(mBp_incomplete_pojo.getmPlotAreaSqFt());
        if(!TextUtils.isEmpty(mBp_incomplete_pojo.getmPlotAreaSqMt()))
            et_plot_sq_m.setText(mBp_incomplete_pojo.getmPlotAreaSqMt());
        if(!TextUtils.isEmpty(mBp_incomplete_pojo.getmApplicationNo()))
        {
            mApplicationName = mBp_incomplete_pojo.getmApplicationNo();
            getmApplicationId =mBp_incomplete_pojo.getmApplicationNo();
        }

    }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){

            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());

            //display the error message
            if(view instanceof EditText){
                ((EditText) view).setError(message);
                view.requestFocus();

            }else{
                Snackbar.make(li_parent_lay,message,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void saveFunction(){
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.insertBuildingPlan(Common.ACCESS_TOKEN,mQueryType,mServiceCategory,mServiceName,
                mApplicationDate,mApplicationName,mFatherName,mMobileNo,mCommunicationAddress,mEmailID,mSurveyNo,mDocumentNo,mDoorNo,
                mWardNo,mStreetNo,mTown,mCity,mPincode,mPlatNo,mBlockNo,mBuildingType,mPlotAreaSqFt,mPlotAreaSqMt,mSurveyorName,mAppStatus,
                mAppDescription,mUserId,mUserName,mPanchayat,mDistrict,mUserName);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONObject jsonObject = new JSONObject(response1);
                    String result = jsonObject.getString("message");
                    if(result.startsWith("Success")){
                        String codearray[] = result.split("~");

                        Snackbar.make(li_parent_lay,"Registered successfully "+codearray[1],Snackbar.LENGTH_SHORT).show();
                        Intent ik = new Intent(getActivity(),ChecklistDetails.class);
                        ik.putExtra("ApplicationId",mApplicationName);
                        ik.putExtra("Panchayat",mPanchayat);
                        ik.putExtra("District",mDistrict);
                        startActivity(ik);
                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e("error==>", t.toString());
            }
        });
    }
    public void updateFunction(){
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.updateBuildingPlan(Common.ACCESS_TOKEN,mQueryType,mServiceCategory,mServiceName,
                mApplicationDate,mApplicationName,mFatherName,mMobileNo,mCommunicationAddress,mEmailID,mSurveyNo,mDocumentNo,mDoorNo,
                mWardNo,mStreetNo,mTown,mCity,mPincode,mPlatNo,mBlockNo,mBuildingType,mPlotAreaSqFt,mPlotAreaSqMt,mSurveyorName,mAppStatus,
                mAppDescription,mUserId,mUserName,mPanchayat,mDistrict,mCreatedBy,getmApplicationId);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd", response.toString());
                Log.e("url==>",response.raw().toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                try {
                    JSONObject jsonObject = new JSONObject(response1);
                    String result = jsonObject.getString("message");
                    if(result.startsWith("Success")){

                        Snackbar.make(li_parent_lay,"Updated successfully ",Snackbar.LENGTH_SHORT).show();
                        Intent ik = new Intent(getActivity(),ChecklistDetails.class);
                        ik.putExtra("ApplicationId",mApplicationName);
                        ik.putExtra("Panchayat",mPanchayat);
                        ik.putExtra("District",mDistrict);
                        startActivity(ik);
                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e("error==>", t.toString());
            }
        });
    }
}
