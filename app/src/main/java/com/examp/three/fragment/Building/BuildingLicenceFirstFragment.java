package com.examp.three.fragment.Building;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.examp.three.R;
import com.examp.three.activity.Building.BuildingPlanHelpherOne;
import com.examp.three.activity.Building.NewApplicationActivity;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.Building.BP_Incomplete_Pojo;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;


public class BuildingLicenceFirstFragment extends Fragment implements Validator.ValidationListener {

    View mFirstFragmentView;
    @Nullable
    @BindView(R.id.et_service_catagory) EditText et_service_catagory;
    @Nullable
    @BindView(R.id.et_service) EditText et_service;
    @Nullable
    @BindView(R.id.et_distrcit) EditText et_distrcit;
    @Nullable
    @BindView(R.id.et_townpanchayat) EditText et_townpanchayat;
    @NotEmpty(message = "Applicant Name is required field!")
    @Nullable
    @BindView(R.id.et_applicant_name) EditText et_applicant_name;

    @NotEmpty(message = "Father/Husband Name is required field!")
    @Nullable
    @BindView(R.id.et_father_husband_name) EditText et_father_husband_name;

    @NotEmpty(message = "Mobile No is required field!")
    @Length(min = 10,max = 10)
    @Nullable
    @BindView(R.id.et_mobileno) EditText et_mobileno;

    @NotEmpty(message = "E-Mail ID is required field!")
    @Email
    @Nullable
    @BindView(R.id.et_mailid) EditText et_mailid;

    @NotEmpty(message = "Communication Address is required field")
    @Nullable
    @BindView(R.id.et_per_permanent_address) EditText et_per_permanent_address;

    @Nullable
    @BindView(R.id.li_parent_lay)
    LinearLayout li_parent_lay;

    @Nullable
    @BindView(R.id.btn_next) Button btn_next;

    Validator validator;
    BuildingPlanHelpherOne mBuildingPlanHelpherOne;
    SharedPreferenceHelper mSharedPreferenceHelpher;

    String login_userId;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirstFragmentView = inflater.inflate(R.layout.fragment_building_licence_first, container, false);
        ButterKnife.bind(this,mFirstFragmentView);

        validator = new Validator(this);
        mBuildingPlanHelpherOne = BuildingPlanHelpherOne.getInstance(getActivity());
        mSharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());
        validator.setValidationListener(this);

        et_distrcit =(EditText)mFirstFragmentView.findViewById(R.id.et_distrcit);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        et_distrcit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getDistricts(et_distrcit);
                et_townpanchayat.getText().clear();
            }
        });
        et_townpanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_distrcit.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(getActivity(), "Select District First", Toast.LENGTH_SHORT).show();
                }else {
                    mBuildingPlanHelpherOne.getPanchayat(et_townpanchayat);
                }
            }
        });
        et_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getService(et_service);
            }
        });
        et_service_catagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildingPlanHelpherOne.getServiceCatagory(et_service_catagory);
            }
        });

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        return mFirstFragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onValidationSucceeded() {

        mSharedPreferenceHelpher.putApplicantBuildingInfoFromFirstFragment(et_service_catagory.getText().toString(),
                et_service.getText().toString(),et_distrcit.getText().toString(),et_townpanchayat.getText().toString(),et_applicant_name.getText().toString(),
                et_father_husband_name.getText().toString(),et_mobileno.getText().toString(),et_mailid.getText().toString(),et_per_permanent_address.getText().toString());

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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(BP_Incomplete_Pojo event){
        Log.e("emaill=>",event.getmApplicationNo()+"chf");
        Log.e("emaill=>",event.getmApplicationName()+"chf");
        Log.e("emaill=>",event.getmDistrict()+"chf");
        Log.e("emaill=>",event.getmPanchayat()+"chf");
        Log.e("emaill=>",event.getmServiceCategory()+"chf");
        Log.e("emaill=>",event.getmServiceName()+"chf");

        if(!TextUtils.isEmpty(event.getmServiceCategory())){
            et_service_catagory.setText(event.getmServiceCategory());
        }
        if(!TextUtils.isEmpty(event.getmServiceName())){
            et_service.setText(event.getmServiceName());
        }
        if(!TextUtils.isEmpty(event.getmDistrict())){
            et_distrcit.setText(event.getmDistrict());
            mBuildingPlanHelpherOne.setDistrictId(event.getmDistrict());
            mBuildingPlanHelpherOne.mselectedDistrict = event.getmDistrict();
        }
        if(!TextUtils.isEmpty(event.getmPanchayat())){
            et_townpanchayat.setText(event.getmPanchayat());
            mBuildingPlanHelpherOne.mselectedPanchayat = event.getmPanchayat();
        }
        if(!TextUtils.isEmpty(event.getmApplicationName())){
            et_applicant_name.setText(event.getmApplicationName());
        }
        if(!TextUtils.isEmpty(event.getmFatherName())){
            et_father_husband_name.setText(event.getmFatherName());
        }
        if(!TextUtils.isEmpty(event.getmMobileNo())){
            et_mobileno.setText(event.getmMobileNo());
        }
        if(!TextUtils.isEmpty(event.getmEmailId())){
            et_mailid.setText(event.getmEmailId());
        }
        if(!TextUtils.isEmpty(event.getmCommunicationAddress())){
            et_per_permanent_address.setText(event.getmCommunicationAddress());
        }

//        Toast.makeText(this, event.getEmailId(), Toast.LENGTH_SHORT).show();
    }
}
