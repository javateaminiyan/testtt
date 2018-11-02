package com.examp.three.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import com.examp.three.Etown_Login;
import com.examp.three.R;
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

import static com.examp.three.common.SharedPreferenceHelper.pref_login_password;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_username;

public class RootNavigation extends AppCompatActivity {
    Toolbar mtoolbar;
    String mIntent_Type = null;
    private LinearLayout mlinear_online_payment, mlinear_NewAssessment, mlinear_TrackNewAssessment, mlinear_AssessmentSearch, mlinear_PaymentHistory,
            mlinear_ViewDCB, mlinear_MakePayment, mlinear_OnlineFilling, mlinear_TrackOnlineFilling,
            mlinear_Calculator, mlinear_NameTransfer, mlinear_birth_deathRegistration,
            mlinear_CertificateSearch, mlinear_deathTrack, mlinear_birth_deathAbstract, mlinear_TL_ContactUs, mlinear_TL_Viewquery, mlinear_TL_ViewHistory, mlinear_TL_applicationStatus,
            mlinear_Building_Completerequest, mlinear_Building_knowmyrequeststatus, mlinear_TL_renewalTrade, mlinear_TL_newApplicationTrade, mLinear_Building_newApplication;
    TextView mtextView_TrackAssessment, mtextView_TrackNewAssessment, mtextView_AssessmentSearch,
            mtextView_birth_deathAbstract, mtextView_CertificateSearch, mtextView_deathTrack, mtextView_birth_deathRegistration;
    LinearLayout rootlayout;
    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    String login_username, login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_navigation);

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
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_online_payment.setVisibility(View.GONE);

                    mlinear_birth_deathAbstract.setVisibility(View.GONE);
                    break;

                case "Profession":
                    getSupportActionBar().setTitle(R.string.toolbar_professiontax);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);

                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);
                    mlinear_online_payment.setVisibility(View.GONE);

                    break;
                case "NonTax":
                    getSupportActionBar().setTitle(R.string.toolbar_nontax);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);
                    mlinear_online_payment.setVisibility(View.GONE);

                    break;

                case "Water":

                    getSupportActionBar().setTitle(R.string.toolbar_watertax);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);
                    mtextView_TrackAssessment.setText(R.string.water_title_trackassessment);
                    mtextView_TrackNewAssessment.setText(R.string.water_title_newconnection);
                    mtextView_AssessmentSearch.setText(R.string.water_title_searchconnection);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);
                    mlinear_online_payment.setVisibility(View.GONE);

                    break;

                case "Birth":

                    getSupportActionBar().setTitle(R.string.toolbar_birth_details);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);
                    mlinear_ViewDCB.setVisibility(View.GONE);
                    mlinear_MakePayment.setVisibility(View.GONE);
                    mlinear_NewAssessment.setVisibility(View.GONE);
                    mlinear_TrackNewAssessment.setVisibility(View.GONE);
                    mlinear_PaymentHistory.setVisibility(View.GONE);
                    mlinear_AssessmentSearch.setVisibility(View.GONE);
                    mlinear_birth_deathRegistration.setVisibility(View.VISIBLE);
                    mlinear_CertificateSearch.setVisibility(View.VISIBLE);
                    mlinear_deathTrack.setVisibility(View.VISIBLE);
                    mlinear_birth_deathAbstract.setVisibility(View.VISIBLE);

                    mtextView_birth_deathRegistration.setText(R.string.Birth_apply_registration);
                    mtextView_birth_deathAbstract.setText(R.string.Birth_abstract);
                    mtextView_deathTrack.setText(R.string.Birth_track_registration);
                    mtextView_CertificateSearch.setText(R.string.Birth_certificate_search);
                    mlinear_online_payment.setVisibility(View.GONE);

                    break;

                case "Death":

                    getSupportActionBar().setTitle(R.string.toolbar_death_details);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);
                    mlinear_ViewDCB.setVisibility(View.GONE);
                    mlinear_MakePayment.setVisibility(View.GONE);
                    mlinear_NewAssessment.setVisibility(View.GONE);
                    mlinear_TrackNewAssessment.setVisibility(View.GONE);
                    mlinear_PaymentHistory.setVisibility(View.GONE);
                    mlinear_AssessmentSearch.setVisibility(View.GONE);
                    mlinear_birth_deathRegistration.setVisibility(View.VISIBLE);
                    mlinear_CertificateSearch.setVisibility(View.VISIBLE);
                    mlinear_deathTrack.setVisibility(View.VISIBLE);
                    mlinear_birth_deathAbstract.setVisibility(View.VISIBLE);
                    mlinear_online_payment.setVisibility(View.GONE);

                    mtextView_birth_deathRegistration.setText(R.string.Death_apply_death_registration);
                    mtextView_birth_deathAbstract.setText(R.string.Death_track_abstract);
                    mtextView_deathTrack.setText(R.string.Death_track_death_registration);
                    mtextView_CertificateSearch.setText(R.string.Death_death_certificate_search);
                    mlinear_online_payment.setVisibility(View.GONE);

                    break;

                case "Grievances_Track":

                    getSupportActionBar().setTitle(R.string.toolbar_grievances);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);
                    mlinear_ViewDCB.setVisibility(View.GONE);
                    mlinear_MakePayment.setVisibility(View.GONE);
                    mlinear_NewAssessment.setVisibility(View.GONE);
                    mlinear_TrackNewAssessment.setVisibility(View.GONE);
                    mlinear_PaymentHistory.setVisibility(View.GONE);
                    mlinear_AssessmentSearch.setVisibility(View.GONE);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.VISIBLE);
                    mlinear_deathTrack.setVisibility(View.VISIBLE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);
                    mtextView_deathTrack.setText(R.string.track_grievances);
                    mtextView_CertificateSearch.setText(R.string.grievances_register);
                    mlinear_online_payment.setVisibility(View.GONE);
                    break;

                case "Trade":

                    getSupportActionBar().setTitle(R.string.toolbar_trade_licence);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);

                    mlinear_PaymentHistory.setVisibility(View.GONE);
                    mlinear_ViewDCB.setVisibility(View.GONE);
                    mlinear_TrackNewAssessment.setVisibility(View.GONE);
                    mlinear_MakePayment.setVisibility(View.GONE);
                    mlinear_NewAssessment.setVisibility(View.GONE);
                    mlinear_AssessmentSearch.setVisibility(View.GONE);
                    mlinear_online_payment.setVisibility(View.VISIBLE);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);

                    mlinear_TL_ContactUs.setVisibility(View.VISIBLE);
                    mlinear_TL_Viewquery.setVisibility(View.VISIBLE);
                    mlinear_TL_ViewHistory.setVisibility(View.VISIBLE);
                    mlinear_TL_applicationStatus.setVisibility(View.VISIBLE);
                    mlinear_TL_renewalTrade.setVisibility(View.VISIBLE);
                    mlinear_TL_newApplicationTrade.setVisibility(View.VISIBLE);

                    break;

                case "Building":

                    getSupportActionBar().setTitle(R.string.toolbar_building_licence);
                    mlinear_TrackOnlineFilling.setVisibility(View.GONE);
                    mlinear_OnlineFilling.setVisibility(View.GONE);
                    mlinear_Calculator.setVisibility(View.GONE);
                    mlinear_NameTransfer.setVisibility(View.GONE);

                    mlinear_PaymentHistory.setVisibility(View.GONE);
                    mlinear_ViewDCB.setVisibility(View.GONE);
                    mlinear_TrackNewAssessment.setVisibility(View.GONE);
                    mlinear_MakePayment.setVisibility(View.GONE);
                    mlinear_NewAssessment.setVisibility(View.GONE);
                    mlinear_AssessmentSearch.setVisibility(View.GONE);
                    mlinear_online_payment.setVisibility(View.VISIBLE);
                    mlinear_birth_deathRegistration.setVisibility(View.GONE);
                    mlinear_CertificateSearch.setVisibility(View.GONE);
                    mlinear_deathTrack.setVisibility(View.GONE);
                    mlinear_birth_deathAbstract.setVisibility(View.GONE);

                    mLinear_Building_newApplication.setVisibility(View.VISIBLE);
                    mlinear_Building_Completerequest.setVisibility(View.VISIBLE);
                    mlinear_Building_knowmyrequeststatus.setVisibility(View.VISIBLE);

                    break;
            }
        }

        mLinear_Building_newApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), BP_InComplete_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });
        mlinear_TL_newApplicationTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), TL_InComplete_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
            }
        });

        mlinear_Building_knowmyrequeststatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), BP_AppRequestStatus_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_Building_Completerequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), Building_Completed_Request.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_TL_Viewquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {


                    Intent i = new Intent(getApplicationContext(), TL_QueryDetails_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_TL_renewalTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), LisenceRenewal.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_TL_ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });
        mlinear_TL_ViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), TL_ViewReceipt_History_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_TL_applicationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), TL_ApplicationStatus_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_online_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    if (mIntent_Type.equalsIgnoreCase("Building"))
                    {
                        Intent i = new Intent(getApplicationContext(), BuildingOnlinePayment.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Trade")) {
                        Intent i = new Intent(getApplicationContext(), OnlinePayment.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    }

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_deathTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    if (mIntent_Type.equalsIgnoreCase("Birth"))
                    {
                        Intent i = new Intent(getApplicationContext(), TrackBirthRegistration_Activity.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Death")) {
                        Intent i = new Intent(getApplicationContext(), TrackDeathRegistration_Activity.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Grievances_Track")) {
                        Intent i = new Intent(getApplicationContext(), Grievances_Track.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);
                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_TrackOnlineFilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), TrackOnlineFiling.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_OnlineFilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent i = new Intent(getApplicationContext(), OnlineFiling.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_birth_deathRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    if (mIntent_Type.equalsIgnoreCase("Birth"))
                    {
                        Intent i = new Intent(getApplicationContext(), BirthRegistration_Activity.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Death")) {
                        Intent i = new Intent(getApplicationContext(), DeathRegistration_Activity.class);
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_MakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    if (mIntent_Type.equalsIgnoreCase("Property"))
                    {
                        if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                            Intent in = new Intent(getApplicationContext(), Etown_Login.class);
                            in.putExtra("Type", "P");

                            startActivity(in);
                            finish();
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                        } else {
                            Intent i = new Intent(getApplicationContext(), MakePayment.class);

                            i.putExtra(Common.Type, "P");
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                        }
                    } else if (mIntent_Type.equalsIgnoreCase("Profession"))
                    {
                        if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                            Intent in = new Intent(getApplicationContext(), Etown_Login.class);
                            in.putExtra("Type", "PR");

                            startActivity(in);
                            finish();
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                        } else {
                            Intent i = new Intent(getApplicationContext(), MakePayment.class);

                            i.putExtra(Common.Type, "PR");
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                        }

                    } else if (mIntent_Type.equalsIgnoreCase("Water"))
                    {
                        if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                            Intent in = new Intent(getApplicationContext(), Etown_Login.class);
                            in.putExtra("Type", "W");

                            startActivity(in);
                            finish();
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                        } else {
                            Intent i = new Intent(getApplicationContext(), MakePayment.class);

                            i.putExtra(Common.Type, "W");
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                        }

                    } else if (mIntent_Type.equalsIgnoreCase("NonTax"))
                    {
                        Snackbar.make(rootlayout, "Non Tax Payment is Under Construction !", Snackbar.LENGTH_SHORT).show();
                    }

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_NewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), NewAssessment_Activity.class);

                    if (mIntent_Type.equalsIgnoreCase("Property"))
                    {
                        i.putExtra(Common.Type, "P");
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Profession"))
                    {
                        i.putExtra(Common.Type, "PR");
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Water"))
                    {
                        i.putExtra(Common.Type, "W");
                        startActivity(i);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("NonTax"))
                    {
                        Intent i_nontax = new Intent(getApplicationContext(), NewAssessment.class);
                        startActivity(i_nontax);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);
                    }

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_Calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), PropertyTaxCalculator_Activity.class);
                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_AssessmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isNetworkAvailable(getApplicationContext())) {

                    switch (mIntent_Type) {
                        case "Property": {

                            Intent i = new Intent(getApplicationContext(), AssessmentSearch.class);
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                            break;
                        }
                        case "Profession": {
                            Intent i = new Intent(getApplicationContext(), Shared_AssessmentSearch.class);
                            i.putExtra("Tax_Type", "Profession");

                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                            break;
                        }
                        case "Water":
                            Intent i = new Intent(getApplicationContext(), Water_AssessmentSearch_Activity.class);
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;

                        case "NonTax":
                            Intent intent = new Intent(getApplicationContext(), Shared_AssessmentSearch.class);
                            intent.putExtra("Tax_Type", "NonTax");
                            startActivity(intent);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                    }

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
            }
        });

        mlinear_CertificateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent intent = new Intent(getApplicationContext(), BirthCertificateSearch_Activity.class);

                    if (mIntent_Type.equalsIgnoreCase("Birth"))
                    {
                        intent.putExtra("Tax_Type", "Birth");

                        startActivity(intent);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Death")) {
                        intent.putExtra("Tax_Type", "Death");

                        startActivity(intent);

//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Grievances_Track")) {
                        Intent i = new Intent(getApplicationContext(), Grievances_Registration.class);
                        startActivity(i);
                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_birth_deathAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent intent = new Intent(getApplicationContext(), BirthAbstract_Activity.class);

                    if (mIntent_Type.equalsIgnoreCase("Birth"))
                    {
                        intent.putExtra("Tax_Type", "Birth");

                        startActivity(intent);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);

                    } else if (mIntent_Type.equalsIgnoreCase("Death")) {
                        intent.putExtra("Tax_Type", "Death");

                        startActivity(intent);
//                        overridePendingTransition(R.anim.anim_slide_out_left,
//                                R.anim.leftanim);
                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_NameTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(getApplicationContext())) {
                    Intent nametransfer_intent = new Intent(RootNavigation.this, NameTransfer.class);
                    startActivity(nametransfer_intent);
//                    overridePendingTransition(R.anim.anim_slide_out_left,
//                            R.anim.leftanim);
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_ViewDCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), ViewDcb.class);

                    switch (mIntent_Type) {
                        case "Property": {

                            i.putExtra("Tax_Type", "Property");
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                        }
                        case "Profession": {
                            i.putExtra("Tax_Type", "Profession");
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                        }
                        case "Water":
                            i.putExtra("Tax_Type", "Water");
                            startActivity(i);

                            break;

                        case "NonTax":
                            i.putExtra("Tax_Type", "NonTax");
                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;

                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_PaymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(getApplicationContext())) {
                    switch (mIntent_Type) {
                        case "Property": {

                            Intent i = new Intent(getApplicationContext(), PaidHistory.class);
                            i.putExtra("Tax_Type", "Property");

                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                        }
                        case "Profession": {
                            Intent i = new Intent(getApplicationContext(), Shared_PaymentHistory.class);
                            i.putExtra("Tax_Type", "Profession");

                            startActivity(i);

                            break;
                        }

                        case "NonTax":
                            Intent i = new Intent(getApplicationContext(), Shared_PaymentHistory.class);
                            i.putExtra("Tax_Type", "NonTax");

                            startActivity(i);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);
                            break;

                        case "Water":
                            Intent intent_water = new Intent(getApplicationContext(), PaidHistory.class);
                            intent_water.putExtra("Tax_Type", "Water");

                            startActivity(intent_water);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });

        mlinear_TrackNewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    Intent tracknewassessment = new Intent(getApplicationContext(), TrackNewAssessmentNo.class);

                    switch (mIntent_Type) {

                        case "Property": {

                            tracknewassessment.putExtra("Tax_Type", "Property");
                            startActivity(tracknewassessment);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                        }
                        case "Profession": {
                            tracknewassessment.putExtra("Tax_Type", "Profession");

                            startActivity(tracknewassessment);

//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                        }
                        case "Water":
                            tracknewassessment.putExtra("Tax_Type", "Water");
                            startActivity(tracknewassessment);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;

                        case "NonTax":
                            tracknewassessment.putExtra("Tax_Type", "NonTax");
                            startActivity(tracknewassessment);
//                            overridePendingTransition(R.anim.anim_slide_out_left,
//                                    R.anim.leftanim);

                            break;
                    }

                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
            }

        });
    }

    //This method is for initializing all the views in the layout activity
    private void initViews() {
        mlinear_NewAssessment = findViewById(R.id.linear_new_assessment);
        mlinear_online_payment = findViewById(R.id.linear_online_payment);

        mlinear_TrackNewAssessment = findViewById(R.id.linear_tracknew_assessment);
        mlinear_AssessmentSearch = findViewById(R.id.linear_assessmentsearch);
        mlinear_PaymentHistory = findViewById(R.id.linear_payment_history);
        mlinear_ViewDCB = findViewById(R.id.linear_view_dcb);
        mlinear_birth_deathRegistration = findViewById(R.id.linear_birth_death_regsitration);
        mlinear_birth_deathAbstract = findViewById(R.id.linear_birth_death_abstract);
        mlinear_deathTrack = findViewById(R.id.linear_birth_death_track);
        mlinear_CertificateSearch = findViewById(R.id.linear_birth_certificate_search);
        rootlayout = findViewById(R.id.rootlayout);
        mlinear_MakePayment = findViewById(R.id.linear_makepayment);
        mlinear_OnlineFilling = findViewById(R.id.linear_online_filling);
        mlinear_TrackOnlineFilling = findViewById(R.id.linear_track_online_filling);
        mlinear_Calculator = findViewById(R.id.linear_tax_calculator);
        mlinear_NameTransfer = findViewById(R.id.linear_name_transfer);
        mLinear_Building_newApplication = findViewById(R.id.linear_newapplication_building);

        mlinear_TL_ContactUs = findViewById(R.id.linear_contactUs);
        mlinear_TL_Viewquery = findViewById(R.id.linear_viewquery);
        mlinear_TL_ViewHistory = findViewById(R.id.linear_application_viewreceipt_history);
        mlinear_TL_applicationStatus = findViewById(R.id.linear_application_status);
        mlinear_TL_renewalTrade = findViewById(R.id.linear_renewal_trade);
        mlinear_TL_newApplicationTrade = findViewById(R.id.linear_newapplication_trade);

        mlinear_Building_Completerequest = findViewById(R.id.linear_application_completerequest);
        mlinear_Building_knowmyrequeststatus = findViewById(R.id.linear_knowmyrequeststatus);

        mlinear_Building_Completerequest.setVisibility(View.GONE);
        mlinear_Building_knowmyrequeststatus.setVisibility(View.GONE);
        mLinear_Building_newApplication.setVisibility(View.GONE);

        mlinear_TL_ContactUs.setVisibility(View.GONE);
        mlinear_TL_Viewquery.setVisibility(View.GONE);
        mlinear_TL_ViewHistory.setVisibility(View.GONE);
        mlinear_TL_applicationStatus.setVisibility(View.GONE);
        mlinear_TL_renewalTrade.setVisibility(View.GONE);
        mlinear_TL_newApplicationTrade.setVisibility(View.GONE);

        mtextView_TrackAssessment = findViewById(R.id.txtview_trackassessment);
        mtextView_TrackNewAssessment = findViewById(R.id.txtview_newassessment);
        mtextView_AssessmentSearch = findViewById(R.id.txtview_assessmentsearch);
        mtextView_birth_deathRegistration = findViewById(R.id.txt_birth_death_regsitration);
        mtextView_birth_deathAbstract = findViewById(R.id.txt_birth_death_abstract);
        mtextView_deathTrack = findViewById(R.id.txt_birth_death_track);
        mtextView_CertificateSearch = findViewById(R.id.txt_birth_certificate_search);

    }
}
