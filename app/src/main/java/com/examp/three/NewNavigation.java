package com.examp.three;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.examp.three.activity.Birth_Modules.BirthRegistration_Activity;
import com.examp.three.activity.Birth_Modules.TrackBirthRegistration_Activity;
import com.examp.three.activity.Building.BP_AppRequestStatus_Activity;
import com.examp.three.activity.Building.BP_InComplete_Activity;
import com.examp.three.activity.Building.BuildingOnlinePayment;
import com.examp.three.activity.Building.Building_Completed_Request;
import com.examp.three.activity.Death_Modules.DeathRegistration_Activity;
import com.examp.three.activity.Death_Modules.TrackDeathRegistration_Activity;
import com.examp.three.activity.Grievances.Grievances_Registration;
import com.examp.three.activity.Grievances.Grievances_Track;
import com.examp.three.activity.NonTax.NewAssessment;
import com.examp.three.activity.Profession.OnlineFiling;
import com.examp.three.activity.Profession.TrackOnlineFiling;
import com.examp.three.activity.Property.AssessmentSearch;
import com.examp.three.activity.Property.NameTransfer;
import com.examp.three.activity.Property.PaidHistory;
import com.examp.three.activity.Property.PropertyTaxCalculator_Activity;
import com.examp.three.activity.Shared_Modules.Shared_Birth_Death.BirthAbstract_Activity;
import com.examp.three.activity.Shared_Modules.Shared_Birth_Death.BirthCertificateSearch_Activity;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.MakePayment;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.NewAssessment_Activity;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.Shared_AssessmentSearch;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.Shared_PaymentHistory;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.TrackNewAssessmentNo;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.ViewDcb;
import com.examp.three.activity.Trade.ContactUsActivity;
import com.examp.three.activity.Trade.LisenceRenewal;
import com.examp.three.activity.Trade.OnlinePayment;
import com.examp.three.activity.Trade.TL_ApplicationStatus_Activity;
import com.examp.three.activity.Trade.TL_InComplete_Activity;
import com.examp.three.activity.Trade.TL_QueryDetails_Activity;
import com.examp.three.activity.Trade.TL_ViewReceipt_History_Activity;
import com.examp.three.activity.WaterTax.Water_AssessmentSearch_Activity;
import com.examp.three.common.Common;

import javax.inject.Inject;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_password;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_username;

public class NewNavigation extends AppCompatActivity {

    LinearLayout llBirthDetails,llApplyBirthRegistration,llBirthAbstract,llBirthCertificateSearch,llTrackBirthRegistration,
            llDeathDetails,llApplyDeathRegistration,llDeathAbstact,llDeathCertificateSearch,llTrackDeathRegistation,
            llPropertyTax,llPropertyNewAsssessment,llPropertyTaxCalculator,llPropertyAssessmentSearch,llPropertyNameTransfer,
            llPropertyPaymentHistory,llPropertyViewDcb,llPropertyTrackNewAssessment,llPropertyMakePayment,llProfessionTax,
            llProfessionNewAssessment,llProfessionAssessmentSearch,llProfessionPaymentHistory,llProfessionViewDcb,
            llProfessionTrackNewAssessment,llProfessionMakePayment,llProfessionOnlineFiling,llProfessionTrackOnlineFiling,
            llWaterTax,llWaterNewConnection,llWaterConnectionSearch,llWaterPaymentHistory,llWaterViewDcb,
            llWaterTrackNewConnection,llWaterMakePayment,llNonTax,llNonTaxNewAssessment,llNonTaxAssessmentSearch,
            llNonTaxPaymentHistory,llNonTaxViewDcb,llNonTaxTrackNewAssessment,llNonTaxMakePayment,llGrievances,
            llGrievancesRegister,llGrievancesTrack,llBuildingLicence,llBuildingNewApplication,llBuildingKnowMyRequestStatus,
            llBuildingCompletedRequest,llBuildingOnlinePayment,llTradeLicence,llTradeNewApplication,llTradeLicenceRenewal,
            llTradeApplicationStatus,llTradeViewReceiptHistory,llTradeViewQuery,llTradeContactUs,llTradeOnlinePayment,
            rootlayout,payment_historylinear;

    Toolbar mtoolbar;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    String login_username, login_password;

    String mIntent_Type = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_navigation_new);

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initViews();

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_username = sharedPreference.getString(pref_login_username, "");
        login_password = sharedPreference.getString(pref_login_password, "");

        Intent intent = getIntent();
        if (intent != null) {
            mIntent_Type = intent.getStringExtra("Type");

            switch (mIntent_Type) {
                case "Property":
                    getSupportActionBar().setTitle(R.string.toolbar_property);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.VISIBLE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "Profession":
                    getSupportActionBar().setTitle(R.string.toolbar_professiontax);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.VISIBLE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "NonTax":
                    getSupportActionBar().setTitle(R.string.toolbar_nontax);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.VISIBLE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "Water":
                    getSupportActionBar().setTitle(R.string.toolbar_watertax);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.VISIBLE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "Birth":
                    getSupportActionBar().setTitle(R.string.toolbar_birth_details);
                    llBirthDetails.setVisibility(View.VISIBLE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "Death":
                    getSupportActionBar().setTitle(R.string.toolbar_death_details);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.VISIBLE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "Grievances_Track":
                    getSupportActionBar().setTitle(R.string.toolbar_grievances);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.VISIBLE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;

                case "Trade":
                    getSupportActionBar().setTitle(R.string.toolbar_trade_licence);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.GONE);
                    llTradeLicence.setVisibility(View.VISIBLE);
                    break;

                case "Building":
                    getSupportActionBar().setTitle(R.string.toolbar_building_licence);
                    llBirthDetails.setVisibility(View.GONE);
                    llDeathDetails.setVisibility(View.GONE);
                    llPropertyTax.setVisibility(View.GONE);
                    llProfessionTax.setVisibility(View.GONE);
                    llWaterTax.setVisibility(View.GONE);
                    llNonTax.setVisibility(View.GONE);
                    llGrievances.setVisibility(View.GONE);
                    llBuildingLicence.setVisibility(View.VISIBLE);
                    llTradeLicence.setVisibility(View.GONE);
                    break;
            }
        }

    }

    public void initViews(){

        rootlayout = (LinearLayout)findViewById(R.id.rootlayout);

        llBirthDetails = (LinearLayout)findViewById(R.id.llbirth_details);
        llApplyBirthRegistration = (LinearLayout)findViewById(R.id.llapply_birth_registration);
        llBirthAbstract = (LinearLayout)findViewById(R.id.llbirth_abstract);
        llBirthCertificateSearch = (LinearLayout)findViewById(R.id.llbirth_certificate_search);
        llTrackBirthRegistration = (LinearLayout)findViewById(R.id.lltrack_birth_registration);

        llDeathDetails = (LinearLayout)findViewById(R.id.lldeath_details);
        llApplyDeathRegistration = (LinearLayout)findViewById(R.id.llapply_death_registration);
        llDeathAbstact = (LinearLayout)findViewById(R.id.lldeath_abstract);
        llDeathCertificateSearch = (LinearLayout)findViewById(R.id.lldeath_certificate_search);
        llTrackDeathRegistation = (LinearLayout)findViewById(R.id.lltrack_death_registration);

        llPropertyTax = (LinearLayout)findViewById(R.id.llproperty_tax);
        llPropertyNewAsssessment = (LinearLayout)findViewById(R.id.llproperty_new_assessment);
        llPropertyTaxCalculator = (LinearLayout)findViewById(R.id.llproperty_tax_calculator);
        llPropertyAssessmentSearch = (LinearLayout)findViewById(R.id.llproperty_assessment_search);
        llPropertyNameTransfer = (LinearLayout)findViewById(R.id.llproperty_name_transfer);
        llPropertyPaymentHistory = (LinearLayout)findViewById(R.id.llproperty_payment_history);
        llPropertyViewDcb = (LinearLayout)findViewById(R.id.llproperty_view_dcb);
        llPropertyTrackNewAssessment = (LinearLayout)findViewById(R.id.llproperty_track_new_assessment);
        llPropertyMakePayment = (LinearLayout)findViewById(R.id.llproperty_make_payment);

        llProfessionTax = (LinearLayout)findViewById(R.id.llprofession_tax);
        llProfessionNewAssessment = (LinearLayout)findViewById(R.id.llprofession_new_assessment);
        llProfessionAssessmentSearch = (LinearLayout)findViewById(R.id.llprofession_assessment_search);
        llProfessionPaymentHistory = (LinearLayout)findViewById(R.id.llprofession_payment_history);
        llProfessionViewDcb = (LinearLayout)findViewById(R.id.llprofession_view_dcb);
        llProfessionTrackNewAssessment = (LinearLayout)findViewById(R.id.llprofession_track_new_assessment);
        llProfessionMakePayment = (LinearLayout)findViewById(R.id.llprofession_make_payment);
        llProfessionOnlineFiling = (LinearLayout)findViewById(R.id.llprofession_online_filing);
        llProfessionTrackOnlineFiling = (LinearLayout)findViewById(R.id.llprofession_track_online_filing);

        llWaterTax = (LinearLayout)findViewById(R.id.llwater_tax);
        llWaterNewConnection = (LinearLayout)findViewById(R.id.llwater_new_connection);
        llWaterConnectionSearch = (LinearLayout)findViewById(R.id.llwater_connection_search);
        llWaterPaymentHistory = (LinearLayout)findViewById(R.id.llwater_payment_history);
        llWaterViewDcb = (LinearLayout)findViewById(R.id.llwater_view_dcb);
        llWaterTrackNewConnection = (LinearLayout)findViewById(R.id.llwater_track_new_connection);
        llWaterMakePayment = (LinearLayout)findViewById(R.id.llwater_make_payment);

        llNonTax = (LinearLayout)findViewById(R.id.llnon_tax);
        llNonTaxNewAssessment = (LinearLayout)findViewById(R.id.llnontax_new_assessment);
        llNonTaxAssessmentSearch = (LinearLayout)findViewById(R.id.llnontax_assessment_search);
        llNonTaxPaymentHistory = (LinearLayout)findViewById(R.id.llnontax_payment_history);
        llNonTaxViewDcb = (LinearLayout)findViewById(R.id.llnontax_view_dcb);
        llNonTaxTrackNewAssessment = (LinearLayout)findViewById(R.id.llnontax_track_new_assessment);
        llNonTaxMakePayment = (LinearLayout)findViewById(R.id.llnontax_make_payment);

        llGrievances = (LinearLayout)findViewById(R.id.llgrievances);
        llGrievancesRegister = (LinearLayout)findViewById(R.id.llgrievances_register);
        llGrievancesTrack = (LinearLayout)findViewById(R.id.llgrievances_track);

        llBuildingLicence = (LinearLayout)findViewById(R.id.llbuilding_license);
        llBuildingNewApplication = (LinearLayout)findViewById(R.id.llbuilding_new_application);
        llBuildingKnowMyRequestStatus = (LinearLayout)findViewById(R.id.llbuilding_know_my_req_status);
        llBuildingCompletedRequest = (LinearLayout)findViewById(R.id.llbuilding_completed_request);
        llBuildingOnlinePayment = (LinearLayout)findViewById(R.id.llbuilding_online_payment);

        llTradeLicence = (LinearLayout)findViewById(R.id.lltrade_license);
        llTradeNewApplication = (LinearLayout)findViewById(R.id.lltrade_new_application);
        llTradeLicenceRenewal = (LinearLayout)findViewById(R.id.lltrade_license_renewal);
        llTradeApplicationStatus = (LinearLayout)findViewById(R.id.lltrade_application_status);
        llTradeViewReceiptHistory = (LinearLayout)findViewById(R.id.lltrade_view_receipt_history);
        llTradeViewQuery = (LinearLayout)findViewById(R.id.lltrade_view_query);
        llTradeContactUs = (LinearLayout)findViewById(R.id.lltrade_contact_us);
        llTradeOnlinePayment = (LinearLayout)findViewById(R.id.lltrade_online_payment);

        touchListener(llApplyBirthRegistration,"ApplyBirthRegistration");
        touchListener(llBirthAbstract,"BirthAbstract");
        touchListener(llBirthCertificateSearch,"BirthCertificateSearch");
        touchListener(llTrackBirthRegistration,"TrackBirthRegistration");
        touchListener(llApplyDeathRegistration,"ApplyDeathRegistration");
        touchListener(llDeathAbstact,"DeathAbstract");
        touchListener(llDeathCertificateSearch,"DeathCertificateSearch");
        touchListener(llTrackDeathRegistation,"TrackDeathRegistration");
        touchListener(llPropertyNewAsssessment,"PropertyNewAssessment");
        touchListener(llPropertyTaxCalculator,"PropertyTaxCalculator");
        touchListener(llPropertyAssessmentSearch,"PropertyAssessmentSearch");
        touchListener(llPropertyNameTransfer,"PropertyNameTransfer");
        touchListener(llPropertyPaymentHistory,"PropertyPaymentHistory");
        touchListener(llPropertyViewDcb,"PropertyViewDcb");
        touchListener(llPropertyTrackNewAssessment,"PropertyTrackNewAssessment");
        touchListener(llPropertyMakePayment,"PropertyMakePayment");
        touchListener(llProfessionNewAssessment,"ProfessionNewAssessment");
        touchListener(llProfessionAssessmentSearch,"ProfessionAssessmentSearch");
        touchListener(llProfessionPaymentHistory,"ProfessionPaymentHistory");
        touchListener(llProfessionViewDcb,"ProfessionViewDcb");
        touchListener(llProfessionTrackNewAssessment,"ProfessionTrackNewAssessment");
        touchListener(llProfessionMakePayment,"ProfessionMakePayment");
        touchListener(llProfessionOnlineFiling,"ProfessionOnlineFiling");
        touchListener(llProfessionTrackOnlineFiling,"ProfessionTrackOnlineFiling");
        touchListener(llWaterNewConnection,"WaterNewConnection");
        touchListener(llWaterConnectionSearch,"WaterConnectionSearch");
        touchListener(llWaterPaymentHistory,"WaterPaymentHistory");
        touchListener(llWaterViewDcb,"WaterViewDcb");
        touchListener(llWaterTrackNewConnection,"WaterTrackNewConnection");
        touchListener(llWaterMakePayment,"WaterMakePayment");
        touchListener(llNonTaxNewAssessment,"NonTaxNewAssessment");
        touchListener(llNonTaxAssessmentSearch,"NonTaxAssessmentSearch");
        touchListener(llNonTaxPaymentHistory,"NonTaxPaymentHistory");
        touchListener(llNonTaxViewDcb,"NonTaxViewDcb");
        touchListener(llNonTaxTrackNewAssessment,"NonTaxTrackNewAssessment");
        touchListener(llNonTaxMakePayment,"NonTaxMakePayment");
        touchListener(llGrievancesRegister,"GrievancesRegister");
        touchListener(llGrievancesTrack,"GrievancesTrack");
        touchListener(llBuildingNewApplication,"BuildingNewApplication");
        touchListener(llBuildingKnowMyRequestStatus,"BuildingKnowMyRequestStatus");
        touchListener(llBuildingCompletedRequest,"BuildingCompletedRequest");
        touchListener(llBuildingOnlinePayment,"BuildingOnlinePayment");
        touchListener(llTradeNewApplication,"TradeNewApplication");
        touchListener(llTradeLicenceRenewal,"TradeLicenseRenewal");
        touchListener(llTradeApplicationStatus,"TradeApplicationStatus");
        touchListener(llTradeViewReceiptHistory,"TradeViewReceiptHistory");
        touchListener(llTradeViewQuery,"TradeViewQuery");
        touchListener(llTradeContactUs,"TradeContactUs");
        touchListener(llTradeOnlinePayment,"TradeOnlinePayment");

    }

    @SuppressLint("ClickableViewAccessibility")
    public void touchListener(final LinearLayout ll, final String Value){

        Log.e("lll","---> "+ll.toString());

        ll.setOnTouchListener( new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        ll.setBackground(getResources().getDrawable(R.drawable.text_background));
                        ll.setClickable(true);

                        break;
                    case MotionEvent.ACTION_UP:

                        ll.setBackground(getResources().getDrawable(R.drawable.text_background_new));
                        ll.setClickable(true);
                        if (Common.isNetworkAvailable(getApplicationContext())) {
                            onClickEvent(Value);
                        }else
                            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                        break;
                }
                return true;
            }
        });
    }

    public void onClickEvent(String value){

        if (value.equalsIgnoreCase("ApplyBirthRegistration")){
            Intent i = new Intent(getApplicationContext(), BirthRegistration_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("BirthAbstract")){
            Intent i = new Intent(getApplicationContext(), BirthAbstract_Activity.class);
            i.putExtra("Tax_Type", "Birth");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("BirthCertificateSearch")){
            Intent i = new Intent(getApplicationContext(), BirthCertificateSearch_Activity.class);
            i.putExtra("Tax_Type", "Birth");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TrackBirthRegistration")){
            Intent i = new Intent(getApplicationContext(), TrackBirthRegistration_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ApplyDeathRegistration")){

            Intent i = new Intent(getApplicationContext(), DeathRegistration_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("DeathAbstract")){
            Intent i = new Intent(getApplicationContext(), BirthAbstract_Activity.class);
            i.putExtra("Tax_Type", "Death");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("DeathCertificateSearch")){
            Intent i = new Intent(getApplicationContext(), BirthCertificateSearch_Activity.class);
            i.putExtra("Tax_Type", "Death");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TrackDeathRegistration")){
            Intent i = new Intent(getApplicationContext(), TrackDeathRegistration_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyNewAssessment")){
            Intent i = new Intent(getApplicationContext(), NewAssessment_Activity.class);
            i.putExtra(Common.Type, "P");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyTaxCalculator")){
            Intent i = new Intent(getApplicationContext(), PropertyTaxCalculator_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyAssessmentSearch")){
            Intent i = new Intent(getApplicationContext(), AssessmentSearch.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyNameTransfer")){
            Intent nametransfer_intent = new Intent(getApplicationContext(), NameTransfer.class);
            startActivity(nametransfer_intent);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyPaymentHistory")){
            Intent i = new Intent(getApplicationContext(), PaidHistory.class);
            i.putExtra("Tax_Type", "Property");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyViewDcb")){
            Intent i = new Intent(getApplicationContext(), ViewDcb.class);
            i.putExtra("Tax_Type", "Property");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyTrackNewAssessment")){
            Intent i = new Intent(getApplicationContext(), TrackNewAssessmentNo.class);
            i.putExtra("Tax_Type", "Property");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("PropertyMakePayment")){

            if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {
                Intent in = new Intent(getApplicationContext(), Etown_Login.class);
                in.putExtra("Type", "P");
                startActivity(in);
                finish();
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            } else {
                Intent i = new Intent(getApplicationContext(), MakePayment.class);
                i.putExtra(Common.Type, "P");
                startActivity(i);
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            }
        }else if (value.equalsIgnoreCase("ProfessionNewAssessment")){
            Intent i = new Intent(getApplicationContext(), NewAssessment_Activity.class);
            i.putExtra(Common.Type, "PR");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ProfessionAssessmentSearch")){
            Intent i = new Intent(getApplicationContext(), Shared_AssessmentSearch.class);
            i.putExtra("Tax_Type", "Profession");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ProfessionPaymentHistory")){
            Intent i = new Intent(getApplicationContext(), Shared_PaymentHistory.class);
            i.putExtra("Tax_Type", "Profession");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ProfessionViewDcb")){
            Intent i = new Intent(getApplicationContext(), ViewDcb.class);
            i.putExtra("Tax_Type", "Profession");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ProfessionTrackNewAssessment")){
            Intent i = new Intent(getApplicationContext(), TrackNewAssessmentNo.class);
            i.putExtra("Tax_Type", "Profession");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ProfessionMakePayment")){
            if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {
                Intent in = new Intent(getApplicationContext(), Etown_Login.class);
                in.putExtra("Type", "PR");
                startActivity(in);
                finish();
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            } else {
                Intent i = new Intent(getApplicationContext(), MakePayment.class);
                i.putExtra(Common.Type, "PR");
                startActivity(i);
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            }
        }else if (value.equalsIgnoreCase("ProfessionOnlineFiling")){
            Intent i = new Intent(getApplicationContext(), OnlineFiling.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("ProfessionTrackOnlineFiling")){
            Intent i = new Intent(getApplicationContext(), TrackOnlineFiling.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("WaterNewConnection")){
            Intent i = new Intent(getApplicationContext(), NewAssessment_Activity.class);
            i.putExtra(Common.Type, "W");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("WaterConnectionSearch")){
            Intent i = new Intent(getApplicationContext(), Water_AssessmentSearch_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("WaterPaymentHistory")){
            Intent i = new Intent(getApplicationContext(), Shared_PaymentHistory.class);
            i.putExtra("Tax_Type", "Water");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("WaterViewDcb")){
            Intent i = new Intent(getApplicationContext(), ViewDcb.class);
            i.putExtra("Tax_Type", "Water");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("WaterTrackNewConnection")){
            Intent i = new Intent(getApplicationContext(), TrackNewAssessmentNo.class);
            i.putExtra("Tax_Type", "Water");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("WaterMakePayment")){
            if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {
                Intent in = new Intent(getApplicationContext(), Etown_Login.class);
                in.putExtra("Type", "W");
                startActivity(in);
                finish();
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            } else {
                Intent i = new Intent(getApplicationContext(), MakePayment.class);
                i.putExtra(Common.Type, "W");
                startActivity(i);
//                overridePendingTransition(R.anim.anim_slide_out_left,
//                        R.anim.leftanim);
            }
        }else if (value.equalsIgnoreCase("NonTaxNewAssessment")){
            Intent i = new Intent(getApplicationContext(), NewAssessment.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("NonTaxAssessmentSearch")){
            Intent i = new Intent(getApplicationContext(), Shared_AssessmentSearch.class);
            i.putExtra("Tax_Type", "NonTax");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("NonTaxPaymentHistory")){
            Intent i = new Intent(getApplicationContext(), Shared_PaymentHistory.class);
            i.putExtra("Tax_Type", "NonTax");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("NonTaxViewDcb")){
            Intent i = new Intent(getApplicationContext(), ViewDcb.class);
            i.putExtra("Tax_Type", "NonTax");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("NonTaxTrackNewAssessment")){
            Intent i = new Intent(getApplicationContext(), TrackNewAssessmentNo.class);
            i.putExtra("Tax_Type", "NonTax");
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("NonTaxMakePayment")){
            Snackbar.make(rootlayout, "Non Tax Payment is Under Construction !", Snackbar.LENGTH_SHORT).show();
        }else if (value.equalsIgnoreCase("GrievancesRegister")){
            Intent i = new Intent(getApplicationContext(), Grievances_Registration.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("GrievancesTrack")){
            Intent i = new Intent(getApplicationContext(), Grievances_Track.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("BuildingNewApplication")){
            Intent i = new Intent(getApplicationContext(), BP_InComplete_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("BuildingKnowMyRequestStatus")){
            Intent i = new Intent(getApplicationContext(), BP_AppRequestStatus_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("BuildingCompletedRequest")){
            Intent i = new Intent(getApplicationContext(), Building_Completed_Request.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("BuildingOnlinePayment")){
            Intent i = new Intent(getApplicationContext(), BuildingOnlinePayment.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeNewApplication")){
            Intent i = new Intent(getApplicationContext(), TL_InComplete_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeLicenseRenewal")){
            Intent i = new Intent(getApplicationContext(), LisenceRenewal.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeApplicationStatus")){
            Intent i = new Intent(getApplicationContext(), TL_ApplicationStatus_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeViewReceiptHistory")){
            Intent i = new Intent(getApplicationContext(), TL_ViewReceipt_History_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeViewQuery")){
            Intent i = new Intent(getApplicationContext(), TL_QueryDetails_Activity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeContactUs")){
            Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }else if (value.equalsIgnoreCase("TradeOnlinePayment")){
            Intent i = new Intent(getApplicationContext(), OnlinePayment.class);
            startActivity(i);
//            overridePendingTransition(R.anim.anim_slide_out_left,
//                    R.anim.leftanim);
        }
    }
}
