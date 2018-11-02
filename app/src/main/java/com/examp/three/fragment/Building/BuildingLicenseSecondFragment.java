package com.examp.three.fragment.Building;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.examp.three.R;
import com.examp.three.activity.Building.BuildingPlanHelpherOne;
import com.examp.three.activity.Building.NewApplicationActivity;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.Building.BP_Incomplete_Pojo;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingLicenseSecondFragment extends Fragment implements Validator.ValidationListener{
    @NotEmpty(message = "Survey Number Should Not be empty")
    @Nullable
    @BindView(R.id.et_surveyno) EditText et_surveyno;
    @NotEmpty(message = "Document Number Should Not be empty")
    @Nullable
    @BindView(R.id.et_documentno) EditText et_documentno;
    @NotEmpty(message = "Door Number Should Not be empty")
    @Nullable
    @BindView(R.id.et_doorno) EditText et_doorno;
    @NotEmpty(message = "Ward Number Should Not be empty")
    @Nullable
    @BindView(R.id.et_wardno) EditText et_wardno;
    @NotEmpty(message = "Street Name Should Not be empty")
    @Nullable
    @BindView(R.id.et_street) EditText et_street;
    @NotEmpty(message = "Town Panchayat Should Not be empty")
    @Nullable
    @BindView(R.id.et_town) EditText et_town;
    @NotEmpty(message = "District Name Should Not be empty")
    @Nullable
    @BindView(R.id.et_district) EditText et_district;
    @NotEmpty(message = "Pin code Should Not be empty")
    @Nullable
    @BindView(R.id.et_pincode) EditText et_pincode;
    @NotEmpty(message = "Plot Number Should Not be empty")
    @Nullable
    @BindView(R.id.et_plotno) EditText et_plotno;
    @NotEmpty(message = "Block Number Should Not be empty")
    @Nullable
    @BindView(R.id.et_blockno) EditText et_blockno;

    @Nullable
    @BindView(R.id.li_parent_lay) LinearLayout li_parent_lay;

    @Nullable
    @BindView(R.id.btn_next) Button btnNext;

    @Nullable
    @BindView(R.id.btn_back) Button btnBack;

    View mSecondFragmentView;
    BuildingPlanHelpherOne mBuildingPlanHelpherOne;
    SharedPreferenceHelper mSharedPreferenceHelpher;

    Typeface avvaiyarfont;

    String mSelectedDistrict;
    String mSelectedTown;

    Validator validator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSecondFragmentView = inflater.inflate(R.layout.fragment_building_license_second, container, false);

        ButterKnife.bind(this,mSecondFragmentView);

        avvaiyarfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avvaiyar.ttf");

        validator = new Validator(this);
        mBuildingPlanHelpherOne = BuildingPlanHelpherOne.getInstance(getActivity());
        validator.setValidationListener(this);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferenceHelpher.putApplicationBuildingInfoFromSecondFragment(et_surveyno.getText().toString(),
                        mBuildingPlanHelpherOne.mselectedStreetCode,et_documentno.getText().toString(),
                        et_doorno.getText().toString(),et_wardno.getText().toString(),et_street.getText().toString(),
                        et_town.getText().toString(),et_district.getText().toString(),et_pincode.getText().toString(),
                        et_plotno.getText().toString(),et_blockno.getText().toString());

                validator.validate();
            }
        });
        et_wardno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getWardNo(et_wardno);
            }
        });
        et_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getStreetName(et_street);
            }
        });


        et_town.setText(mSelectedTown);
        et_district.setText(mSelectedDistrict);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = NewApplicationActivity.mLicense_registration_pager.getCurrentItem();
                NewApplicationActivity.mLicense_registration_pager.setCurrentItem(current-1);
            }
        });
        return mSecondFragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mSharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());
            mSelectedDistrict = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_district");
            mSelectedTown = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_townpanchayat");
            et_district.setText(mSelectedDistrict);
            et_town.setText(mSelectedTown);
            Log.e("dvbgg",mSelectedDistrict);
        }
        else {
            Log.e("not","visible");
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
    public void onEventMainThread(BP_Incomplete_Pojo event){
        Log.e("emaill=>",event.getmSurveyNo()+"<=chf");
        Log.e("emaill=>",event.getmDocumentNo()+"<=chf");
        Log.e("emaill=>",event.getmDoorNo()+"<=chf");
        Log.e("emaill=>",event.getmWardNo()+"<=chf");
        Log.e("emaill=>",event.getmStreetNo()+"<=chf");
        Log.e("emaill=>",event.getmPincode()+"<=chf");
        Log.e("emaill=>",event.getmPlatNo()+"<=chf");
        Log.e("emaill=>",event.getmBlockNo()+"<=chf");

        if(!TextUtils.isEmpty(event.getmSurveyNo()))
        et_surveyno.setText(event.getmSurveyNo());

        if(!TextUtils.isEmpty(event.getmDocumentNo()))
        et_documentno.setText(event.getmDocumentNo());

        if(!TextUtils.isEmpty(event.getmDoorNo()))
        et_doorno.setText(event.getmDoorNo());

        if(!TextUtils.isEmpty(event.getmWardNo()))
        {
            et_wardno.setText(event.getmWardNo());
            mBuildingPlanHelpherOne.mselectedWard = event.getmWardNo();
        }

        if(!TextUtils.isEmpty(event.getmStreetNo()))
        {
            mBuildingPlanHelpherOne.mselectedStreet = event.getmStreetNo();
            et_street.setText(event.getmStreetNo());
            et_street.setTypeface(avvaiyarfont);
        }

        if(!TextUtils.isEmpty(event.getmTown()))
        et_town.setText(event.getmTown());

        if(!TextUtils.isEmpty(event.getmDistrict()))
        et_district.setText(event.getmDistrict());

        if(!TextUtils.isEmpty(event.getmPlatNo()))
        et_plotno.setText(event.getmPlatNo());

        if(!TextUtils.isEmpty(event.getmBlockNo()))
        et_blockno.setText(event.getmBlockNo());

        if(event.getmPincode()!=0)
         et_pincode.setText(event.getmPincode()+"");
    }

    @Override
    public void onValidationSucceeded() {
        Log.e("fvghcsd",et_street.getText().toString()+"xfbhg");


        String m = mSharedPreferenceHelpher.getSpecificValues("PREF_buildingplan_Street");
        Log.e("fvghcsd",m+"xfbhg");

        int current = NewApplicationActivity.mLicense_registration_pager.getCurrentItem();
        NewApplicationActivity.mLicense_registration_pager.setCurrentItem(current+1);
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
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Snackbar.make(li_parent_lay,message,Snackbar.LENGTH_SHORT).show();

            }
        }
    }
}
