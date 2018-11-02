package com.examp.three.activity.Building;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.badoualy.stepperindicator.StepperIndicator;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.examp.three.R;
import com.examp.three.model.Building.BP_Incomplete_Pojo;
import com.examp.three.adapter.Building.BuildingPlanNewApplication_PagerAdapter;
import com.examp.three.common.Common;

public class NewApplicationActivity extends AppCompatActivity {

    public static ViewPager mLicense_registration_pager;

    BuildingPlanNewApplication_PagerAdapter mBuildingPlanNewApplicationPagerAdapter;

    int mUniqueId,mPincode,mUserId;
    String mServiceCategory,mServiceName,mApplicationNo,mApplicationDate,mApplicationName,mFatherName,mMobileNo;
    String mCommunicationAddress,mEmailId,mSurveyNo,mDocumentNo,mDoorNo,mWardNo,mStreetNo,mTown;
    String mCity,mPlatNo,mBlockNo,mBuildingType,mPlotAreaSqFt,mPlotAreaSqMt,mSurveyorName;
    String mAppStatus,mAppDescription,mAppProcessingStage,mUserName,mDistrict,mPanchayat,mCreatedBy;
    String mCreatedDate,mUpdatedBy,mUpdatedDate,mAppDate;

    @BindView(R.id.buildingplan_registration_toolbar) Toolbar mBuildingplan_registration_toolbar;
//    @BindView(R.id.buildingplan_registration_pager) ViewPager mLicense_registration_pager;
    @BindView(R.id.stepper_building_license_registration_indicator) StepperIndicator stepper_building_license_registration_indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);

        mLicense_registration_pager = (ViewPager) findViewById(R.id.buildingplan_registration_pager) ;

        ButterKnife.bind(this);
        setSupportActionBar(mBuildingplan_registration_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         mUniqueId =getIntent().getIntExtra("mUniqueId",0);
         mPincode =getIntent().getIntExtra("mPincode",0);
         mUserId =getIntent().getIntExtra("mUserId",0);
         mServiceCategory =getIntent().getStringExtra("mServiceCategory");
         mServiceName =getIntent().getStringExtra("mServiceName");
         mApplicationNo =getIntent().getStringExtra("mApplicationNo");
         mApplicationDate =getIntent().getStringExtra("mApplicationDate");
         mApplicationName =getIntent().getStringExtra("mApplicationName");
         mFatherName =getIntent().getStringExtra("mFatherName");
         mMobileNo =getIntent().getStringExtra("mMobileNo");
        mCommunicationAddress =getIntent().getStringExtra("mCommunicationAddress");
        mEmailId =getIntent().getStringExtra("mEmailId");
        mSurveyNo =getIntent().getStringExtra("mSurveyNo");
        mDocumentNo =getIntent().getStringExtra("mDocumentNo");
        mDoorNo =getIntent().getStringExtra("mDoorNo");
        mWardNo =getIntent().getStringExtra("mWardNo");
        mStreetNo =getIntent().getStringExtra("mStreetNo");
        mTown =getIntent().getStringExtra("mTown");
        mCity =getIntent().getStringExtra("mCity");
        mPlatNo =getIntent().getStringExtra("mPlatNo");
        mBlockNo =getIntent().getStringExtra("mBlockNo");
        mPlotAreaSqFt =getIntent().getStringExtra("mPlotAreaSqFt");
        mPlotAreaSqMt =getIntent().getStringExtra("mPlotAreaSqMt");
        mSurveyorName =getIntent().getStringExtra("mSurveyorName");
        mBuildingType =getIntent().getStringExtra("mBuildingType");
        mAppStatus =getIntent().getStringExtra("mAppStatus");
        mAppDescription =getIntent().getStringExtra("mAppDescription");
        mAppProcessingStage =getIntent().getStringExtra("mAppProcessingStage");
        mUserName =getIntent().getStringExtra("mUserName");
        mPanchayat =getIntent().getStringExtra("mPanchayat");
        mDistrict =getIntent().getStringExtra("mDistrict");
        mCreatedBy =getIntent().getStringExtra("mCreatedBy");
        mUserName =getIntent().getStringExtra("mUserName");
        mCreatedDate =getIntent().getStringExtra("mCreatedDate");
        mUpdatedBy =getIntent().getStringExtra("mUpdatedBy");
        mUpdatedDate =getIntent().getStringExtra("mUpdatedDate");
        mAppDate =getIntent().getStringExtra("mAppDate");

        if(getIntent().getBooleanExtra("isNeedToUpdate",false)){
            Log.e("xgfh=>","true");
            Common.isNeedToUpdateBuildingPlan = true;
        }else{
            Log.e("xgfh=>","false");
            Common.isNeedToUpdateBuildingPlan = false;
        }

        EventBus.getDefault().postSticky(new BP_Incomplete_Pojo(mUniqueId,mServiceCategory,mServiceName,
                mApplicationNo,mApplicationDate,mApplicationName,mFatherName,
                mMobileNo,mCommunicationAddress,mEmailId,mSurveyNo,mDocumentNo,
                mDoorNo,mWardNo,mStreetNo,mTown,mCity,mPincode,mPlatNo,
                mBlockNo,mBuildingType,mPlotAreaSqFt,mPlotAreaSqMt,mSurveyorName,
                mAppStatus,mAppDescription,mAppProcessingStage,mUserId,mUserName,mDistrict,mPanchayat,mCreatedBy
                ,mCreatedDate,mUpdatedBy,mUpdatedDate,mAppDate));

        mBuildingPlanNewApplicationPagerAdapter  = new BuildingPlanNewApplication_PagerAdapter(getSupportFragmentManager(),3);
        mLicense_registration_pager.setAdapter(mBuildingPlanNewApplicationPagerAdapter);
        stepper_building_license_registration_indicator.setViewPager(mLicense_registration_pager);

        mLicense_registration_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
}
