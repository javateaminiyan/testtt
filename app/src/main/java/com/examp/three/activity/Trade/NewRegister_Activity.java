package com.examp.three.activity.Trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.badoualy.stepperindicator.StepperIndicator;
import com.examp.three.R;
import com.examp.three.adapter.TradeLicence.LicenseRegistration_PagerAdapter;
import com.examp.three.model.TradeLicence.TL_ViewToComp_Pojo;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewRegister_Activity extends AppCompatActivity {
    StepperIndicator license_registration_indicator;
    public static ViewPager license_registration_pager;
    LicenseRegistration_PagerAdapter license_pagerAdapter;
    @BindView(R.id.license_registration_toolbar)
    Toolbar license_registration_toolbar;

    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register_);
        ButterKnife.bind(this);
        setSupportActionBar(license_registration_toolbar);
        license_registration_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //id's
        license_registration_pager = (ViewPager) findViewById(R.id.license_registration_pager);
        license_registration_indicator = (StepperIndicator) findViewById(R.id.license_registration_indicator);

        assert license_registration_pager != null;

        //setAdapter
        license_pagerAdapter = new LicenseRegistration_PagerAdapter(getSupportFragmentManager(), 4);
        license_registration_pager.setAdapter(license_pagerAdapter);
        license_registration_indicator.setViewPager(license_registration_pager);
        license_registration_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Intent i = getIntent();
        String sQueryType = i.getStringExtra("QueryType");
        int Sno = i.getIntExtra("Sno",0);
        String sMobileNo = i.getStringExtra("MobileNo");
        String sEmail = i.getStringExtra("Email");
        String sApplicantSurName = i.getStringExtra("ApplicantSurName");
        String sApplicantFirstName = i.getStringExtra("ApplicantFirstName");
        String sApGender = i.getStringExtra("ApGender");
        String sApFGSurName = i.getStringExtra("ApFGSurName");
        String sApFGFirstName = i.getStringExtra("ApFGFirstName");


        String sApDoorNo = i.getStringExtra("ApDoorNo");
        String sApStreet = i.getStringExtra("ApStreet");
        String sApCity = i.getStringExtra("ApCity");
        String sApDistrict = i.getStringExtra("ApDistrict");
        String sApPIN = i.getStringExtra("ApPIN");
        String sAadhar = i.getStringExtra("Aadhar");
        String GST = i.getStringExtra("GST");

        String sDistrict = i.getStringExtra("sDistrict");
        String sPanchayat = i.getStringExtra("sPanchayat");
        String sWardNo = i.getStringExtra("sWardNo");
        String sStreetName = i.getStringExtra("sStreetName");
        String sDoorNo = i.getStringExtra("sDoorNo");
        String LicenceYear = i.getStringExtra("LicenceYear");

        String LicenceTypeId = i.getStringExtra("LicenceTypeId");
        String TradeDescription = i.getStringExtra("TradeDescription");
        String EstablishmentName = i.getStringExtra("EstablishmentName");
        String motorInstalled = i.getStringExtra("motorInstalled");
        int motorTotalQty = i.getIntExtra("motorTotalQty",0);
        int HorsePowerTotal = i.getIntExtra("HorsePowerTotal",0);
        String RentalAgrStatus = i.getStringExtra("RentalAgrStatus");
        String RentPaid = i.getStringExtra("RentPaid");
        String NOCStatus = i.getStringExtra("NOCStatus");
        int TradeRate = i.getIntExtra("TradeRate",0);

        String ApplicantPhoto = i.getStringExtra("ApplicantPhoto");
        String AddressProofCopy = i.getStringExtra("AddressProofCopy");
        String AgreementCopy = i.getStringExtra("AgreementCopy");
        String NOCCopy = i.getStringExtra("NOCCopy");
        String MachineSpecifications = i.getStringExtra("MachineSpecifications");
        String MachineInstallationDig = i.getStringExtra("MachineInstallationDig");

        EventBus.getDefault().postSticky(new TL_ViewToComp_Pojo(sQueryType,Sno,sMobileNo, sEmail, sApplicantSurName,
                sApplicantFirstName,
                sApGender, sApFGSurName, sApFGFirstName, sApDoorNo, sApStreet,
                sApCity, sApDistrict, sApPIN, sAadhar,GST,sDistrict,sPanchayat,sWardNo,
                sStreetName,sDoorNo,LicenceYear,LicenceTypeId,TradeDescription,TradeRate,EstablishmentName,motorInstalled,
                motorTotalQty,HorsePowerTotal,RentalAgrStatus,RentPaid,NOCStatus,ApplicantPhoto,AddressProofCopy,
                AgreementCopy,NOCCopy,MachineSpecifications,MachineInstallationDig));

    }


}
