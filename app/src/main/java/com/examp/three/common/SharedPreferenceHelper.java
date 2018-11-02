package com.examp.three.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public  class SharedPreferenceHelper {

    private Context context;
    private final SharedPreferences sharedPreferences;

    String MyPREFERENCES = "User";
    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        this.context =context;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static final String pref_login_userid="pref_login_userid";
    public static final String pref_login_title="pref_login_tfgitle";
    public static final String pref_login_firstname="firstName";
    public static final String pref_login_lastname="pref_lotegin_lastname";
    public static final String pref_login_contact="pref_logttin_contact";
    public static final String pref_login_streetname="pref_lotygin_streetname";
    public static final String pref_login_city="pref_logirgn_city";
    public static final String pref_login_state="pref_loginhr_state";
    public static final String pref_login_pincode="pref_loginfgg_pincode";
    public static final String pref_login_address="ddd";
    public static final String pref_login_email="pref_logddin_email";
    public static final String pref_login_country="sCountry";
    public static final String pref_login_district_department="sDistrict";
    public static final String pref_login_panchayat_department="sPanchayat";

    public static final String pref_login_username="uname";

    public static final String pref_login_password="upassword";


    public static final String pref_login_type="deptoruser";

    public static final String PREF_ = "login";

    public static final String PREF_SELECTDISTRICT = "select district";

    public static final String PREF_SELECTDISTRICTID = "select districtss";

    public static final String PREF_SELECTPANCHAYAT = "select panchayat";
    public static final String PREF_SELECTPANCHAYATID = "select panchayatid";
   // sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    public static SharedPreferenceHelper sharedPreferenceHelpher = null;
    public static final String sharedPreferenceName = "BIRTH_REGISTRATION";

    public static SharedPreferenceHelper getInstance(Context context1) {
        if (sharedPreferenceHelpher == null) {
            sharedPreferenceHelpher = new SharedPreferenceHelper(context1);
        }
        return sharedPreferenceHelpher;
    }

    //This method is for putting parental info into shared preferences
    public void putParentalInfo(String brth_reg_District,
                                String brth_reg_Panchayat,
                                String brth_reg_Mobno,
                                String brth_reg_Emailid,
                                String brth_reg_FatherName,
                                String brth_reg_MotherName,
                                String brth_reg_FatherAadharNo,
                                String brth_reg_MotherAadharNo,
                                String brth_reg_PermenantAddress,
                                String brth_reg_Pincode,
                                String brth_reg_per_PermenantAddress,
                                String brth_reg_per_Pincode,
                                String brth_reg_Dob,
                                String brth_reg_Gender,
                                String brth_reg_ChildName
    ) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_brth_reg_District", brth_reg_District);
        editor.putString("PREF_brth_reg_Panchayat", brth_reg_Panchayat);
        editor.putString("PREF_brth_reg_Mobno", brth_reg_Mobno);
        editor.putString("PREF_brth_reg_Emailid", brth_reg_Emailid);
        editor.putString("PREF_brth_reg_FatherName", brth_reg_FatherName);
        editor.putString("PREF_brth_reg_MotherName", brth_reg_MotherName);
        editor.putString("PREF_brth_reg_FatherAadharNo", brth_reg_FatherAadharNo);
        editor.putString("PREF_brth_reg_MotherAadharNo", brth_reg_MotherAadharNo);
        editor.putString("PREF_brth_reg_PermenantAddress", brth_reg_PermenantAddress);
        editor.putString("PREF_brth_reg_Pincode", brth_reg_Pincode);
        editor.putString("PREF_brth_reg_per_PermenantAddress", brth_reg_per_PermenantAddress);
        editor.putString("PREF_brth_reg_per_Pincode", brth_reg_per_Pincode);
        editor.putString("PREF_brth_reg_Dob", brth_reg_Dob);
        editor.putString("PREF_brth_reg_Gender", brth_reg_Gender);
        editor.putString("PREF_brth_reg_ChildName", brth_reg_ChildName);
        editor.commit();
    }

    //This method is for putting personal info into shared preferences
    public void putPersonalInfo(String death_reg_District,
                                String death_reg_Panchayat,
                                String death_reg_Mobno,
                                String death_reg_Emailid,
                                String death_reg_FatherName,
                                String death_reg_MotherName,
                                String death_reg_husband_wife_name,
                                String death_reg_PermenantAddress,
                                String death_reg_Pincode,
                                String death_reg_per_PermenantAddress,
                                String death_reg_per_Pincode,
                                String death_reg_DOD,
                                String death_reg_Gender,
                                String death_reg_Name,
                                String death_reg_age,
                                String death_reg_age_type
    ) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_death_reg_District", death_reg_District);
        editor.putString("PREF_death_reg_Panchayat", death_reg_Panchayat);
        editor.putString("PREF_death_reg_Mobno", death_reg_Mobno);
        editor.putString("PREF_death_reg_Emailid", death_reg_Emailid);
        editor.putString("PREF_death_reg_FatherName", death_reg_FatherName);
        editor.putString("PREF_death_reg_MotherName", death_reg_MotherName);
        editor.putString("PREF_death_reg_husband_wife_name", death_reg_husband_wife_name);
        editor.putString("PREF_death_reg_PermenantAddress", death_reg_PermenantAddress);
        editor.putString("PREF_death_reg_Pincode", death_reg_Pincode);
        editor.putString("PREF_death_reg_per_PermenantAddress", death_reg_per_PermenantAddress);
        editor.putString("PREF_death_reg_per_Pincode", death_reg_per_Pincode);
        editor.putString("PREF_death_reg_DOD", death_reg_DOD);
        editor.putString("PREF_death_reg_Gender", death_reg_Gender);
        editor.putString("PREF_death_reg_Name", death_reg_Name);
        editor.putString("PREF_death_reg_age", death_reg_age);
        editor.putString("PREF_death_reg_age_type", death_reg_age_type);
        editor.commit();
    }

    public String getSpecificValues(String key) {
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);

        return sharedPreferences1.getString(key, null);
    }

    //This method is for putting place of birth info into shared preferences
    public void putPlaceofBirth(String birthplace,
                                String hospital_code,
                                String hospitalName,
                                String doorNo,
                                String wardNo,
                                String streetcode,
                                String streetName
    ) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_birthplace", birthplace);
        editor.putString("PREF_hospital_code", hospital_code);
        editor.putString("PREF_hospitalName", hospitalName);
        editor.putString("PREF_doorNo", doorNo);
        editor.putString("PREF_wardNo", wardNo);
        editor.putString("PREF_streetName", streetName);
        editor.putString("PREF_streetcode", streetcode);
        editor.commit();
    }

    //This method is for putting place of death info into shared preferences
    public void putPlaceofDeath(String deathplace,
                                String hospital_code,
                                String hospitalName,
                                String doorNo,
                                String wardNo,
                                String streetcode,
                                String streetName,
                                String other_address,
                                String other_pincode
    ) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_death_place", deathplace);
        editor.putString("PREF_death_hospital_code", hospital_code);
        editor.putString("PREF_death_hospitalName", hospitalName);
        editor.putString("PREF_death_doorNo", doorNo);
        editor.putString("PREF_death_wardNo", wardNo);
        editor.putString("PREF_death_streetName", streetName);
        editor.putString("PREF_death_streetcode", streetcode);
        editor.putString("PREF_death_other_address", other_address);
        editor.putString("PREF_death_other_pincode", other_pincode);
        editor.commit();
    }


    //This method is for putting stepper one info into shared preferences
    public void stepperOne(String district, String panchayat, String name, String mobileNo, String emailID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("StepperPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("pDistrict", district);
        editor.putString("pPanchayat", panchayat);
        editor.putString("pName", name);
        editor.putString("pMobileNo", mobileNo);
        editor.putString("pEmailId", emailID);

        editor.apply();
    }

    //This method is for putting property tax stepper two info into shared preferences
    public void stepperProperty_Two(String bLicenseNo, String bLicenseDate, String BlockNo, String wardNo, String StreetCode,
                                    String streetName, String bZone, String bUsage, String bType, String totalArea) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("StepperPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("bLicenseNo", bLicenseNo);
        editor.putString("bLicenseDate", bLicenseDate);
        editor.putString("BlockNo", BlockNo);
        editor.putString("wardNo", wardNo);
        editor.putString("streetCode", StreetCode);
        editor.putString("streetName", streetName);
        editor.putString("bZone", bZone);
        editor.putString("bUsage", bUsage);
        editor.putString("bType", bType);
        editor.putString("totalArea", totalArea);

        editor.apply();
    }

    //This method is for putting applicant building first fragment info into shared preferences
    public void putApplicantBuildingInfoFromFirstFragment(String servicecatagory,
                                                          String service,
                                                          String district,
                                                          String townpanchayat,
                                                          String applicationName,
                                                          String father_hub_name,
                                                          String mobileno,
                                                          String emailid,
                                                          String communicationAddress
    ){
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_buildingplan_servicecatagory",servicecatagory);
        editor.putString("PREF_buildingplan_service",service);
        editor.putString("PREF_buildingplan_district",district);
        editor.putString("PREF_buildingplan_townpanchayat",townpanchayat);
        editor.putString("PREF_buildingplan_applicationName",applicationName);
        editor.putString("PREF_buildingplan_father_hub_name",father_hub_name);
        editor.putString("PREF_buildingplan_mobileno",mobileno);
        editor.putString("PREF_buildingplan_emailid",emailid);
        editor.putString("PREF_buildingplan_communicationAddress",communicationAddress);
        editor.commit();
    }

    public boolean getSpecificBooleanValues(String key) {
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);

        return sharedPreferences1.getBoolean(key, false);
    }

    //This method is for putting applicant building second fragment info into shared preferences
    public void putApplicationBuildingInfoFromSecondFragment(String surveyNo,
                                                             String streetCode,
                                                             String documentNo,
                                                             String doorNo,
                                                             String wardNo,
                                                             String Street,
                                                             String town,
                                                             String district,
                                                             String pincode,
                                                             String plotno,
                                                             String blockno){


        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();

        editor.putString("PREF_buildingplan_surveyNo",surveyNo);
        editor.putString("PREF_buildingplan_streetCode",streetCode);
        editor.putString("PREF_buildingplan_documentNo",documentNo);
        editor.putString("PREF_buildingplan_doorNo",doorNo);
        editor.putString("PREF_buildingplan_wardNo",wardNo);
        editor.putString("PREF_buildingplan_Street",Street);
        editor.putString("PREF_buildingplan_town",town);
        editor.putString("PREF_buildingplan_district",district);
        editor.putString("PREF_buildingplan_pincode",pincode);
        editor.putString("PREF_buildingplan_plotno",plotno);
        editor.putString("PREF_buildingplan_blockno",blockno);
        editor.commit();

    }
    public void putSno(String sno){
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_sno",sno);
        editor.commit();
    }

    //This method is for putting motor installed values in shared preference
    public void putMotorInstalled(String motorinstalled) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_Motor", motorinstalled);
        editor.commit();
    }

    //This method is for putting NOC values in shared preference
    public void putNoc(String noc) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_Noc", noc);
        editor.commit();
    }

    //This method is for putting rental values in shared preference
    public void putRental(String rental) {
        SharedPreferences birth_reg_shared_pref;
        birth_reg_shared_pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = birth_reg_shared_pref.edit();
        editor.putString("PREF_Rental", rental);
        editor.commit();
    }

    public void putBuildingLicenseDetails(boolean motorinstalled, boolean haveNocDoc, boolean rentalagreementStatus, boolean isRental) {
        SharedPreferences licence_app_upload;
        licence_app_upload = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = licence_app_upload.edit();
        editor.putBoolean("PREF_motorinstalled", motorinstalled);
        editor.putBoolean("PREF_havenocdoc", haveNocDoc);
        editor.putBoolean("PREF_rentalagreementstatus", rentalagreementStatus);
        editor.putBoolean("PREF_isrental", isRental);
        editor.commit();


    }

    //This method is for putting applicant details info in shared preference
    public void putApplicantDetails(String mobileNo, String emailid, String appInitials, String appName, String gender,
                                    String fmhgInitials, String fmhgName, String doorNo, String streetName,
                                    String cityName, String district, String pincode, String aadharNo, String gst) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("PREF_tl_mobileNo", mobileNo);
        editor.putString("PREF_tl_emailid", emailid);
        editor.putString("PREF_tl_appInitials", appInitials);
        editor.putString("PREF_tl_appName", appName);
        editor.putString("PREF_tl_gender", gender);
        editor.putString("PREF_tl_fmhgInitials", fmhgInitials);
        editor.putString("PREF_tl_fmhgName", fmhgName);
        editor.putString("PREF_tl_apdoorNo", doorNo);
        editor.putString("PREF_tl_apstreetName", streetName);
        editor.putString("PREF_tl_apcityName", cityName);
        editor.putString("PREF_tl_apdistrict", district);
        editor.putString("PREF_tl_appincode", pincode);
        editor.putString("PREF_tl_aadharNo", aadharNo);
        editor.putString("PREF_tl_gst", gst);


        editor.commit();
    }

    //This method is for putting trade location details in shared preference
    public void putTradeLocationDetails(String district,String panchayat,
                                        String wardNo,String streetName,
                                        String doorNo,String licenseFor
    ) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("PREF_tl_district", district);
        editor.putString("PREF_tl_panchayat", panchayat);
        editor.putString("PREF_tl_wardNo", wardNo);
        editor.putString("PREF_tl_streetName", streetName);
        editor.putString("PREF_tl_doorNo", doorNo);
        editor.putString("PREF_tl_licenseFor", licenseFor);


        editor.commit();
    }


    //This method is for updating trade details in shared preference
    public void putUpdateTradeDetail(String mLicenceTypeId,String mTradeName,String mTradeCode,String mTradeRate,
                                     String mestablishmentname,String mmotorinstalled,String mnumberOfMotorsInstalled,
                                     String mTotalHorsePower,String mrental_paid,String mRentalAgrStatus,
                                     String mNOCStatus,String mFinancialYear,String mUserId,String mEntryUser){

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("PREF_tl_mLicenceTypeId", mLicenceTypeId);
        editor.putString("PREF_tl_mTradeName", mTradeName);
        editor.putString("PREF_tl_mTradeCode", mTradeCode);
        editor.putString("PREF_tl_mTradeRate", mTradeRate);
        editor.putString("PREF_tl_mestablishmentname", mestablishmentname);
        editor.putString("PREF_tl_mmotorinstalled", mmotorinstalled);
        editor.putString("PREF_tl_mnumberOfMotorsInstalled", mnumberOfMotorsInstalled);
        editor.putString("PREF_tl_mTotalHorsePower", mTotalHorsePower);
        editor.putString("PREF_tl_mrental_paid", mrental_paid);
        editor.putString("PREF_tl_mRentalAgrStatus", mRentalAgrStatus);
        editor.putString("PREF_tl_mNOCStatus", mNOCStatus);
        editor.putString("PREF_tl_mFinancialYear", mFinancialYear);
        editor.putString("PREF_tl_mUserId", mUserId);
        editor.putString("PREF_tl_mEntryUser", mEntryUser);

        editor.commit();

    }

}
