package com.examp.three.activity.Trade;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.adapter.TradeLicence.TradeAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.listener.RecyclerClickListener;
import com.examp.three.model.Result;
import com.examp.three.model.TradeLicence.TradeNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;

import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_GETLICENCERENEWALFULLDATA;
import static com.examp.three.common.Common.API_GETSTREETNAMES;
import static com.examp.three.common.Common.API_GETTRADENAMES;
import static com.examp.three.common.Common.API_GETWARDNO;
import static com.examp.three.common.Common.API_SAVELICENCERENEWAL;

public class SaveLicenceRenewal extends AppCompatActivity {

    //Views
    EditText etMobileNo, etEmailId, etApplicantSurname, etFirstName, etRelationSurname, etRelationName, etDoorNo, etStreetName,
            etCityName, etDistrict, etPincode, etAadharNo, etGst, enetMobileNo, enetEmailId, enetApplicantSurname,
            enetFirstName, enetRelationSurname, enetRelationName, enetDoorNo, enetStreetName, enetCityName, enetDistrict,
            enetPincode, enetAadharNo, enetGst, etTradeDistrict, etTradePanchayat, etTradeStreetName, etTradeWardNo,
            etTradeDoorNo, etTradeLicenceFor, etTradeName, etTradeAmount, etTradeESTName, etNumberOfMachine, etHorsePower,
            etRentPaid, enetTradeStreetName, enetTradeWardNo, enetTradeDoorNo, enetTradeLicenceFor, enetTradeName,
            enetTradeAmount, enetTradeESTName, enetNumberOfMachine, enetHorsePower, enetRentPaid;

    CardView cardApplicant, cardTrade, cardTradeDetails, cardAttachment;

    LinearLayout llAppFirst, enllAppFirst, llAppSecond, enllAppSecond, llWardStreet, enllWardStreet, llDoorLi, enllDoorLi,
            llTradeEst, enllTradeEst, enllMotorTrade, llAttachPropTaxOwn, llAttachRentalAgreement, llAttachNOC,
            llRentalAgreeCopy, llMotorTrade;

    TextView tvChoosenAppPhoto, tvUploadAppPhoto, tvChoosenAppAddress, tvUploadAppAddress, tvChoosenRentAgreement,
            tvUploadRentalAgreement, tvChoosenNOCDoc, tvUploadNOCDoc, tvChoosenPropTax, tvUploadPropTax, tvRenewalRequest;

    Button buttonSave;
    TextInputLayout tilRent, entilRent;

    RelativeLayout rootLayout;

    //Toolbar
    Toolbar mtoolbar;

    //RadioButton
    RadioButton radioMale, radioFemale, radioTransgender, radioRental, radioOwn, radioWithoutChange;

    //Checkbox
    CheckBox checkBoxRental, checkBoxMotor, checkBoxNoc;

    //ProgressDialog
    SpotsDialog progressDialog;

    //Strings
    String TAG = "TradeLicenceRenewal--> ", uploadSelectType = "", uploadType = "", intentDistrict = "", intentPanchayat = "",
            intentLicenceNo = "", intentLicenceValidity = "", Check = "WithoutChange", Gender = "", queryType = "",
            buildingType = "", rentalAgreementStatus = "", NOCStatus = "", FinancialYear = "", Penalty = "", Pfa = "",
            Advertisement = "", ServiceCharge = "", tradeCode = "", LicenceTypeId = "", applicationNo = "", motorInstalled = "",
            finalLicenceValidity = "", validFrom = "", validTo = "";

    //ArrayList
    ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> streetLists = new ArrayList<>();
    ArrayList<String> wardNo_items = new ArrayList<>();
    ArrayList<TradeNames> tradenamelistBean = new ArrayList<>();

    //Spinner
    SpinnerDialog spinnerDialogWardNo;

    //Adapter
    StreetAdapter streetAdapter;
    TradeAdapter tradeAdapter;

    File file;

    private int MAX_ATTACHMENT_COUNT = 10;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_licence_renewal);

        //Toolbar
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_licence_renewal);

        //EditTexts
        etMobileNo = (EditText) findViewById(R.id.et_mobile_no);
        etEmailId = (EditText) findViewById(R.id.et_email_id);
        etApplicantSurname = (EditText) findViewById(R.id.et_applicant_surname);
        etFirstName = (EditText) findViewById(R.id.et_first_name);
        etRelationSurname = (EditText) findViewById(R.id.et_relation_surname);
        etRelationName = (EditText) findViewById(R.id.et_relation_name);
        etDoorNo = (EditText) findViewById(R.id.et_doorno);
        etStreetName = (EditText) findViewById(R.id.et_streetname);
        etCityName = (EditText) findViewById(R.id.et_city_name);
        etDistrict = (EditText) findViewById(R.id.et_district);
        etPincode = (EditText) findViewById(R.id.et_pincode);
        etAadharNo = (EditText) findViewById(R.id.et_aadhar_no);
        etGst = (EditText) findViewById(R.id.et_gst);
        enetMobileNo = (EditText) findViewById(R.id.en_et_mobile_no);
        enetEmailId = (EditText) findViewById(R.id.en_et_email_id);
        enetApplicantSurname = (EditText) findViewById(R.id.en_et_applicant_surname);
        enetFirstName = (EditText) findViewById(R.id.en_et_first_name);
        enetRelationSurname = (EditText) findViewById(R.id.en_et_relation_surname);
        enetRelationName = (EditText) findViewById(R.id.en_et_relation_name);
        enetDoorNo = (EditText) findViewById(R.id.en_et_doorno);
        enetStreetName = (EditText) findViewById(R.id.en_et_streetname);
        enetCityName = (EditText) findViewById(R.id.en_et_city_name);
        enetDistrict = (EditText) findViewById(R.id.en_et_district);
        enetPincode = (EditText) findViewById(R.id.en_et_pincode);
        enetAadharNo = (EditText) findViewById(R.id.en_et_aadhar_no);
        enetGst = (EditText) findViewById(R.id.en_et_gst);
        etTradeDistrict = (EditText) findViewById(R.id.et_trade_district);
        etTradePanchayat = (EditText) findViewById(R.id.et_trade_panchayat);
        etTradeStreetName = (EditText) findViewById(R.id.et_trade_streetname);
        etTradeWardNo = (EditText) findViewById(R.id.et_trade_wardno);
        enetTradeStreetName = (EditText) findViewById(R.id.enet_trade_streetname);
        enetTradeWardNo = (EditText) findViewById(R.id.enet_trade_wardno);
        etTradeDoorNo = (EditText) findViewById(R.id.et_trade_doorno);
        etTradeLicenceFor = (EditText) findViewById(R.id.et_trade_licencefor);
        enetTradeDoorNo = (EditText) findViewById(R.id.enet_trade_doorno);
        enetTradeLicenceFor = (EditText) findViewById(R.id.enet_trade_licencefor);
        etTradeName = (EditText) findViewById(R.id.et_trade_name);
        etTradeAmount = (EditText) findViewById(R.id.et_trade_amount);
        etTradeESTName = (EditText) findViewById(R.id.et_trade_estname);
        enetTradeName = (EditText) findViewById(R.id.enet_trade_name);
        enetTradeAmount = (EditText) findViewById(R.id.enet_trade_amount);
        enetTradeESTName = (EditText) findViewById(R.id.enet_trade_estname);
        etNumberOfMachine = (EditText) findViewById(R.id.et_numberofmachine);
        etHorsePower = (EditText) findViewById(R.id.et_horsepower);
        enetNumberOfMachine = (EditText) findViewById(R.id.enet_numberofmachine);
        enetHorsePower = (EditText) findViewById(R.id.enet_horsepower);
        etRentPaid = (EditText) findViewById(R.id.et_rentpaid);
        enetRentPaid = (EditText) findViewById(R.id.enet_rentpaid);

        //TextView
        tvChoosenAppPhoto = (TextView) findViewById(R.id.tv_choosen_app_photo);
        tvUploadAppPhoto = (TextView) findViewById(R.id.tv_upload_app_photo);
        tvChoosenAppAddress = (TextView) findViewById(R.id.tv_choosen_app_address);
        tvUploadAppAddress = (TextView) findViewById(R.id.tv_upload_app_address);
        tvChoosenRentAgreement = (TextView) findViewById(R.id.tv_choosen_rent_agreement);
        tvUploadRentalAgreement = (TextView) findViewById(R.id.tv_upload_rental_agree);
        tvChoosenNOCDoc = (TextView) findViewById(R.id.tv_choosen_noc_doc);
        tvUploadNOCDoc = (TextView) findViewById(R.id.tv_upload_noc_doc);
        tvChoosenPropTax = (TextView) findViewById(R.id.tv_choosen_prop_tax);
        tvUploadPropTax = (TextView) findViewById(R.id.tv_upload_prop_tax);
        tvRenewalRequest = (TextView) findViewById(R.id.tv_renewal_request);

        //Button
        buttonSave = (Button) findViewById(R.id.button_save);

        //RadioButton
        radioMale = (RadioButton) findViewById(R.id.radio_male);
        radioFemale = (RadioButton) findViewById(R.id.radio_female);
        radioTransgender = (RadioButton) findViewById(R.id.radio_tg);
        radioRental = (RadioButton) findViewById(R.id.radio_rental);
        radioOwn = (RadioButton) findViewById(R.id.radio_own);
        radioWithoutChange = (RadioButton) findViewById(R.id.radio_without_change);

        radioWithoutChange.setChecked(true);
        setInitially();

        //LinearLayout
        llAttachPropTaxOwn = (LinearLayout) findViewById(R.id.ll_attach_prop_tax_own);
        llAttachRentalAgreement = (LinearLayout) findViewById(R.id.ll_attach_rental_agreement);
        llAttachNOC = (LinearLayout) findViewById(R.id.ll_attach_noc);
        llRentalAgreeCopy = (LinearLayout) findViewById(R.id.ll_rental_agree_copy);
        llMotorTrade = (LinearLayout) findViewById(R.id.ll_motor_trade);
        llAppFirst = (LinearLayout) findViewById(R.id.ll_app_first);
        enllAppFirst = (LinearLayout) findViewById(R.id.en_ll_app_first);
        llAppSecond = (LinearLayout) findViewById(R.id.ll_app_second);
        enllAppSecond = (LinearLayout) findViewById(R.id.en_ll_app_second);
        llWardStreet = (LinearLayout) findViewById(R.id.ll_ward_street);
        enllWardStreet = (LinearLayout) findViewById(R.id.en_ll_ward_street);
        llDoorLi = (LinearLayout) findViewById(R.id.ll_door_li);
        enllDoorLi = (LinearLayout) findViewById(R.id.enll_door_li);
        llTradeEst = (LinearLayout) findViewById(R.id.ll_trade_est);
        enllTradeEst = (LinearLayout) findViewById(R.id.enll_trade_est);
        enllMotorTrade = (LinearLayout) findViewById(R.id.enll_motor_trade);

        //Checkbox
        checkBoxRental = (CheckBox) findViewById(R.id.checkbox_rental);
        checkBoxMotor = (CheckBox) findViewById(R.id.checkbox_motor);
        checkBoxNoc = (CheckBox) findViewById(R.id.checkbox_noc);

        //RelativeLayout
        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        //CardView
        cardApplicant = (CardView) findViewById(R.id.card_applicant);
        cardTrade = (CardView) findViewById(R.id.card_trade);
        cardTradeDetails = (CardView) findViewById(R.id.card_trade_details);
        cardAttachment = (CardView) findViewById(R.id.card_attachment);

        //TextInputLayout
        tilRent = (TextInputLayout) findViewById(R.id.til_rent);
        entilRent = (TextInputLayout) findViewById(R.id.entil_rent);

        cardApplicant.setCardBackgroundColor(getResources().getColor(R.color.grey));
        cardTrade.setCardBackgroundColor(getResources().getColor(R.color.grey));
        cardTradeDetails.setCardBackgroundColor(getResources().getColor(R.color.grey));
        cardAttachment.setCardBackgroundColor(getResources().getColor(R.color.grey));

        Intent i = getIntent();

        intentDistrict = i.getStringExtra("district");
        intentPanchayat = i.getStringExtra("panchayat");
        intentLicenceNo = i.getStringExtra("licenceNo");
        intentLicenceValidity = i.getStringExtra("licenceValidity");

        String[] licVal = intentLicenceValidity.split("-");
        int licPre = Integer.valueOf(licVal[0]);
        int licPost = Integer.valueOf(licVal[1]);
        int flicPre = licPre + 1;
        int flicPost = licPost + 1;

        finalLicenceValidity = String.valueOf(flicPre) + "-" + String.valueOf(flicPost);

        tvRenewalRequest.setText(finalLicenceValidity);

        validFrom = String.valueOf(flicPre) + "-04-01";
        validTo = String.valueOf(flicPost) + "-03-31";

        Log.e(TAG, "----" + validFrom);
        Log.e(TAG, "----" + validTo);

        if (Common.isNetworkAvailable(getApplicationContext())) {
            getFullDate(intentDistrict, intentPanchayat, intentLicenceNo, intentLicenceValidity);
        } else {

            Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        spinnerDialogWardNo = new SpinnerDialog(SaveLicenceRenewal.this, wardNo_items, "Select or Search Ward No", "Close");// With No Animation
        spinnerDialogWardNo = new SpinnerDialog(SaveLicenceRenewal.this, wardNo_items, "Select or Search Ward No", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation

        spinnerDialogWardNo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int position) {
                enetTradeWardNo.setText(s);

                if (Common.isNetworkAvailable(getApplicationContext())) {
                    streetList(s, intentDistrict, intentPanchayat);
                } else {

                    Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        enetTradeWardNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerDialogWardNo.showSpinerDialog();

            }
        });

        enetTradeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTradeNames();

            }
        });

        enetTradeStreetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (streetLists.size() > 0) {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        setStreetNames();
                    } else {

                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                } else {

                    Snackbar.make(rootLayout, "No Streets Found", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        tvChoosenAppPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadSelectType = "ApplicantPhoto";
                showFileChooser();
            }
        });

        tvChoosenAppAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadSelectType = "AddressProof";
                showFileChooser();
            }
        });

        tvChoosenRentAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadSelectType = "RentalAgreement";
                showFileChooser();
            }
        });

        tvChoosenNOCDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadSelectType = "NOCDocument";
                showFileChooser();
            }
        });

        tvChoosenPropTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadSelectType = "PropertyTax";
                showFileChooser();
            }
        });

        tvUploadAppPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadType = "";

                queryType = "InsertPhoto";

                if (tvChoosenAppPhoto.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rootLayout, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });

        tvUploadAppAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadType = "";

                queryType = "InsertApfDoc";

                if (tvChoosenAppAddress.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rootLayout, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });

        tvUploadRentalAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadType = "";

                queryType = "InsertRentalDoc";

                if (tvChoosenRentAgreement.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rootLayout, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvUploadNOCDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadType = "";

                queryType = "InsertNOCDoc";

                if (tvChoosenNOCDoc.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rootLayout, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvUploadPropTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadType = "";

                queryType = "InsertPtrDoc";

                if (tvChoosenPropTax.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rootLayout, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveLicenceRenewal(enetApplicantSurname.getText().toString(), enetFirstName.getText().toString(), Gender,
                        enetRelationSurname.getText().toString(), enetRelationName.getText().toString(),
                        enetMobileNo.getText().toString(), enetEmailId.getText().toString(), enetDoorNo.getText().toString(),
                        enetStreetName.getText().toString(), enetCityName.getText().toString(), enetDistrict.getText().toString(),
                        enetPincode.getText().toString(), enetAadharNo.getText().toString(), enetGst.getText().toString(),
                        etTradeDistrict.getText().toString(), etTradePanchayat.getText().toString(),
                        enetTradeWardNo.getText().toString(), enetTradeStreetName.getText().toString(),
                        enetTradeDoorNo.getText().toString(), etTradeLicenceFor.getText().toString(),
                        etTradeName.getText().toString(), enetTradeAmount.getText().toString(),
                        enetTradeESTName.getText().toString(), enetNumberOfMachine.getText().toString(),
                        enetHorsePower.getText().toString(), enetRentPaid.getText().toString());
            }
        });
    }

    //This method is for setting trade name in an alert
    public void setTradeNames() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dview = li.inflate(R.layout.alert_org_names, null);

        builder.setView(dview);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);

        RecyclerView recyclerView = (RecyclerView) dview.findViewById(R.id.org_recycler);

        tradeAdapter = new TradeAdapter(SaveLicenceRenewal.this, tradenamelistBean);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tradeAdapter);
        tradeAdapter.notifyDataSetChanged();

        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, tradeAdapter.getItemCount());

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(SaveLicenceRenewal.this, new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
                enetTradeName.setText(tradenamelistBean.get(position).getDescription());

                tradeCode = tradenamelistBean.get(position).getTradeCode();

                enetTradeAmount.setText(tradenamelistBean.get(position).getTradeRate());

                dialog.dismiss();

            }
        }));
    }

    //This method is for saving licence renewal values
    public void saveLicenceRenewal(String applicantSurname, String appFirstName, String gender, String relationSurName,
                                   String relationName, String mobileNo, String emailId, String doorNo, String streetName,
                                   String cityName, String districtName, String pinCode, String aadharNo, String gstNo,
                                   String tradeDistrict, String tradePanchayat, String tradeWardno, String tradeStreet,
                                   String tradeDoorno, String licenceValidity, String tradeName, String tradeAmount,
                                   String tradeEstName, String nmberOfMachine, String horsePower, String rentPaid) {


        if (checkBoxMotor.isChecked()) {

            motorInstalled = "Yes";
        } else {

            motorInstalled = "No";
        }

        progressDialog = new SpotsDialog(SaveLicenceRenewal.this);
        progressDialog.show();

        String appSurname = "", appFirstname = "", relationSurname = "", relationname = "", mobileno = "", emailid = "", doorno = "",
                streetname = "", cityname = "", districtname = "", pincode = "", aadharno = "", gstno = "", tradedistrict = "",
                tradepanchayat = "", tradewardno = "", tradestreet = "", tradedoorno = "", licencevalidity = "", tradename = "",
                tradeamount = "", tradeestname = "", numberofmachine = "", horsepower = "", rentpaid = "";

        try {
            appSurname = URLEncoder.encode(applicantSurname, "UTF-8");
            appFirstname = URLEncoder.encode(appFirstName, "UTF-8");
            relationSurname = URLEncoder.encode(relationSurName, "UTF-8");
            relationname = URLEncoder.encode(relationName, "UTF-8");
            mobileno = URLEncoder.encode(mobileNo, "UTF-8");
            emailid = URLEncoder.encode(emailId, "UTF-8");
            doorno = URLEncoder.encode(doorNo, "UTF-8");
            streetname = URLEncoder.encode(streetName, "UTF-8");
            cityname = URLEncoder.encode(cityName, "UTF-8");
            districtname = URLEncoder.encode(districtName, "UTF-8");
            pincode = URLEncoder.encode(pinCode, "UTF-8");
            aadharno = URLEncoder.encode(aadharNo, "UTF-8");
            gstno = URLEncoder.encode(gstNo, "UTF-8");
            tradedistrict = URLEncoder.encode(tradeDistrict, "UTF-8");
            tradepanchayat = URLEncoder.encode(tradePanchayat, "UTF-8");
            tradewardno = URLEncoder.encode(tradeWardno, "UTF-8");
            tradestreet = URLEncoder.encode(tradeStreet, "UTF-8");
            tradedoorno = URLEncoder.encode(tradeDoorno, "UTF-8");
            licencevalidity = URLEncoder.encode(licenceValidity, "UTF-8");
            tradename = URLEncoder.encode(tradeName, "UTF-8");
            tradeamount = URLEncoder.encode(tradeAmount, "UTF-8");
            tradeestname = URLEncoder.encode(tradeEstName, "UTF-8");
            numberofmachine = URLEncoder.encode(nmberOfMachine, "UTF-8");
            horsepower = URLEncoder.encode(horsePower, "UTF-8");
            rentpaid = URLEncoder.encode(rentPaid, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String REQUEST_TAG = "townPanchayat";

        String url = API_SAVELICENCERENEWAL + applicationNo + "&ApplicationType=Renewal&ApplicationRefId=0&" +
                "ApplicantInitial=" + appSurname + "&ApplicantFirstName=" + appFirstname + "&ApGender=" + gender + "&" +
                "ApFatherInitial=" + relationSurname + "&ApFatherFirstName=" + relationname + "&ApMobileNo=" + mobileno + "&" +
                "ApEmail=" + emailid + "&ApDoorNo=" + doorno + "&ApStreetName=" + streetname + "&ApCityName=" + cityname + "&" +
                "ApDistrict=" + districtname + "&ApPINCode=" + pincode + "&ApAadharNo=" + aadharno + "&ApGSTNo=" + gstno + "&" +
                "District=" + tradedistrict + "&Panchayat=" + tradepanchayat + "&WardNo=" + tradewardno + "&StreetName=" + tradestreet + "&" +
                "DoorNo=" + tradedoorno + "&LicenceValidity=" + licencevalidity + "&LicenceTypeId=" + LicenceTypeId + "&" +
                "TradeName=" + tradename + "&TradeCode=" + tradeCode + "&TradeRate=" + tradeamount + "&EstablishmentName=" + tradeestname + "&" +
                "motorInstalled=" + motorInstalled + "&motorTotalQty=" + numberofmachine + "&HorsePowerTotal=" + horsepower + "&" +
                "BuldingType=" + buildingType + "&RentPaid=" + rentpaid + "&RentalAgrStatus=" + rentalAgreementStatus + "&" +
                "NOCStatus=" + NOCStatus + "&FinancialYear=" + FinancialYear + "&EntryUserId=500015&EntryUser=Vedi&" +
                "ValidFrom=" + validFrom + "&ValidTo=" + validTo + "&Penalty=" + Penalty + "&PFA=" + Pfa + "&Advertisement=" + Advertisement + "&" +
                "ServiceCharge=" + ServiceCharge;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            String code = response.getString("code");
                            String message = response.getString("message");

                            Log.e(TAG, "----" + message);
                            String[] mess = message.split("~");

                            if (mess[0].equalsIgnoreCase("Success")) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    SnackShowTop("Time out", rootLayout);
                } else if (error instanceof AuthFailureError) {
                    SnackShowTop("Connection Time out", rootLayout);
                } else if (error instanceof ServerError) {
                    SnackShowTop("Could not connect server", rootLayout);
                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rootLayout);
                } else if (error instanceof ParseError) {
                    SnackShowTop("Parse Error", rootLayout);
                } else {
                    SnackShowTop(error.getMessage(), rootLayout);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;

            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);

    }

    //This method is for getting full data for licence renewal
    public void getFullDate(String districtName, String panchayatName, String licenceNumber, String licenceValidity) {

        progressDialog = new SpotsDialog(SaveLicenceRenewal.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        String url = API_GETLICENCERENEWALFULLDATA + districtName + "&Panchayat=" + panchayatName + "&OldLicenceNo=" + licenceNumber + "&" +
                "OldLicenceValidity=" + licenceValidity;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    progressDialog.dismiss();

                                    int sno = jsonObject.getInt("Sno");
                                    String onlineApplicationNo = jsonObject.getString("OnlineApplicationNo");
                                    String licenceTypeId = jsonObject.getString("LicenceTypeId");
                                    int TradeCode = jsonObject.getInt("TradeCode");
                                    String tradeDescription = jsonObject.getString("TradeDescription");
                                    int tradersCode = jsonObject.getInt("TradersCode");
                                    String applicantSurName = jsonObject.getString("ApplicantSurName");
                                    String applicantFirstName = jsonObject.getString("ApplicantFirstName");
                                    String applicantName = jsonObject.getString("ApplicantName");
                                    String apFGSurName = jsonObject.getString("ApFGSurName");
                                    String apFGFirstName = jsonObject.getString("ApFGFirstName");
                                    String apGender = jsonObject.getString("ApGender");
                                    String apDoorNo = jsonObject.getString("ApDoorNo");
                                    String apStreet = jsonObject.getString("ApStreet");
                                    String apCity = jsonObject.getString("ApCity");
                                    String apDistrict = jsonObject.getString("ApDistrict");
                                    String apPIN = jsonObject.getString("ApPIN");
                                    String aadhar = jsonObject.getString("Aadhar");
                                    String tin = jsonObject.getString("Tin");
                                    String establishmentName = jsonObject.getString("EstablishmentName");
                                    int tradeRate = jsonObject.getInt("TradeRate");
                                    int penalty = jsonObject.getInt("Penalty");
                                    int pfa = jsonObject.getInt("PFA");
                                    int advertisement = jsonObject.getInt("Advertisement");
                                    int serviceCharge = jsonObject.getInt("ServiceCharge");
                                    int totalAmount = jsonObject.getInt("TotalAmount");
                                    String wardNo = jsonObject.getString("WardNo");
                                    String streetName = jsonObject.getString("StreetName");
                                    String doorNo = jsonObject.getString("DoorNo");
                                    String applicationDate = jsonObject.getString("ApplicationDate");
                                    String financialYear = jsonObject.getString("FinancialYear");
                                    String licenceYear = jsonObject.getString("LicenceYear");
                                    String validFrom = jsonObject.getString("ValidFrom");
                                    String validTo = jsonObject.getString("ValidTo");
                                    String mobileNo = jsonObject.getString("MobileNo");
                                    String email = jsonObject.getString("Email");
                                    String district = jsonObject.getString("District");
                                    String panchayat = jsonObject.getString("Panchayat");
                                    String appStatus = jsonObject.getString("AppStatus");
                                    String rFlage = jsonObject.getString("RFlage");
                                    String reason = jsonObject.getString("Reason");
                                    String entryUser = jsonObject.getString("EntryUser");
                                    String entryDate = jsonObject.getString("EntryDate");
                                    String motorInstalled = jsonObject.getString("motorInstalled");
                                    int motorTotalQty = jsonObject.getInt("motorTotalQty");
                                    int horsePowerTotal = jsonObject.getInt("HorsePowerTotal");
                                    String BuildingType = jsonObject.getString("BuldingType");
                                    int rentPaid = jsonObject.getInt("RentPaid");
                                    String rentalAgrStatus = jsonObject.getString("RentalAgrStatus");
                                    String nocStatus = jsonObject.getString("NOCStatus");
                                    String licenceNo = jsonObject.getString("LicenceNo");


                                    if (Common.isNetworkAvailable(getApplicationContext())) {
                                        wardNoList(district, panchayat);
                                    } else {

                                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                                    }

                                    if (Common.isNetworkAvailable(getApplicationContext())) {
                                        tradeNameList(district, panchayat, licenceTypeId);
                                    } else {

                                        Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                                    }

                                    applicationNo = onlineApplicationNo;
                                    LicenceTypeId = licenceTypeId;
                                    tradeCode = String.valueOf(TradeCode);
                                    Penalty = String.valueOf(penalty);
                                    Pfa = String.valueOf(pfa);
                                    Advertisement = String.valueOf(advertisement);
                                    ServiceCharge = String.valueOf(serviceCharge);

                                    buildingType = BuildingType;

                                    rentalAgreementStatus = rentalAgrStatus;

                                    NOCStatus = nocStatus;

                                    FinancialYear = financialYear;

                                    //WithoutChange
                                    etMobileNo.setText(mobileNo);
                                    etEmailId.setText(email);
                                    etApplicantSurname.setText(applicantSurName);
                                    etFirstName.setText(applicantFirstName);
                                    etRelationSurname.setText(apFGSurName);
                                    etRelationName.setText(apFGFirstName);

                                    //WithChange
                                    enetMobileNo.setText(mobileNo);
                                    enetEmailId.setText(email);
                                    enetApplicantSurname.setText(applicantSurName);
                                    enetFirstName.setText(applicantFirstName);
                                    enetRelationSurname.setText(apFGSurName);
                                    enetRelationName.setText(apFGFirstName);

                                    Gender = apGender;

                                    //WithoutChange
                                    etDoorNo.setText(apDoorNo);
                                    etStreetName.setText(apStreet);
                                    etCityName.setText(apCity);
                                    etDistrict.setText(apDistrict);
                                    etPincode.setText(apPIN);
                                    etAadharNo.setText(aadhar);

                                    //WithChange
                                    enetDoorNo.setText(apDoorNo);
                                    enetStreetName.setText(apStreet);
                                    enetCityName.setText(apCity);
                                    enetDistrict.setText(apDistrict);
                                    enetPincode.setText(apPIN);
                                    enetAadharNo.setText(aadhar);

                                    etTradeDistrict.setText(district);
                                    etTradePanchayat.setText(panchayat);
                                    etTradeStreetName.setText(streetName);
                                    etTradeWardNo.setText(wardNo);
                                    enetTradeStreetName.setText(streetName);
                                    enetTradeWardNo.setText(wardNo);

                                    etTradeDoorNo.setText(doorNo);
                                    etTradeLicenceFor.setText(licenceYear);

                                    enetTradeDoorNo.setText(doorNo);
                                    enetTradeLicenceFor.setText(finalLicenceValidity);

                                    etTradeAmount.setText("" + tradeRate);
                                    etTradeESTName.setText(establishmentName);
                                    enetTradeAmount.setText("" + tradeRate);
                                    enetTradeESTName.setText(establishmentName);

                                    etNumberOfMachine.setText("" + motorTotalQty);
                                    etHorsePower.setText("" + horsePowerTotal);
                                    enetNumberOfMachine.setText("" + motorTotalQty);
                                    enetHorsePower.setText("" + horsePowerTotal);

                                    etRentPaid.setText("" + rentPaid);
                                    enetRentPaid.setText("" + rentPaid);

                                    etTradeName.setText(tradeDescription);
                                    enetTradeName.setText(tradeDescription);

                                    setConditions(apGender, motorInstalled, buildingType, rentalAgrStatus, nocStatus);

                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                }

                                progressDialog.dismiss();

                            }
                        } else {
                            progressDialog.dismiss();

//                            Snackbar.make(rootLayout, "No Data Found", Snackbar.LENGTH_SHORT).show();

                            renewedAlert();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            SnackShowTop("Time out", rootLayout);
                        } else if (error instanceof AuthFailureError) {
                            SnackShowTop("Connection Time out", rootLayout);
                        } else if (error instanceof ServerError) {
                            SnackShowTop("Could not connect server", rootLayout);
                        } else if (error instanceof NetworkError) {
                            SnackShowTop("Please check the internet connection", rootLayout);
                        } else if (error instanceof ParseError) {
                            SnackShowTop("Parse Error", rootLayout);
                        } else {
                            SnackShowTop(error.getMessage(), rootLayout);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;

            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }

    //This method is for showing the renewed alert
    public void renewedAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dview = li.inflate(R.layout.alert_renewed, null);

        builder.setView(dview);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        Button buttonOk = (Button) dview.findViewById(R.id.button_ok);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    //This method is for retrieving the trade names
    public void tradeNameList(String district, String panchayat, String licenceTypeId) {

        String url = API_GETTRADENAMES + district + "&Panchayat=" + panchayat + "&LicenceTypeId=" + licenceTypeId;

        progressDialog = new SpotsDialog(SaveLicenceRenewal.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if (jsonArray.length() > 0) {


                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String code = jsonObject.getString("Code");
                                    String descriptionT = jsonObject.getString("DescriptionT");
                                    String tradeRate = jsonObject.getString("TradeRate");

                                    progressDialog.dismiss();

                                    tradenamelistBean.add(new TradeNames(code, descriptionT, tradeRate));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }
                            }
                        } else {

                            progressDialog.dismiss();

                            Snackbar.make(rootLayout, "No Trade Names Found", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            SnackShowTop("Time out", rootLayout);
                        } else if (error instanceof AuthFailureError) {
                            SnackShowTop("Connection Time out", rootLayout);
                        } else if (error instanceof ServerError) {
                            SnackShowTop("Could not connect server", rootLayout);
                        } else if (error instanceof NetworkError) {
                            SnackShowTop("Please check the internet connection", rootLayout);
                        } else if (error instanceof ParseError) {
                            SnackShowTop("Parse Error", rootLayout);
                        } else {
                            SnackShowTop(error.getMessage(), rootLayout);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };

        progressDialog.dismiss();

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }

    //This method is for setting conditions
    public void setConditions(String gender, String motorInstalled, String buildingType, String rentalStatus,
                              String nocStatus) {

        if (gender.equalsIgnoreCase("M")) {

            radioMale.setChecked(true);
            radioFemale.setChecked(false);
            radioTransgender.setChecked(false);


        } else if (gender.equalsIgnoreCase("F")) {

            radioMale.setChecked(false);
            radioFemale.setChecked(true);
            radioTransgender.setChecked(false);
        } else {

            radioMale.setChecked(false);
            radioFemale.setChecked(false);
            radioTransgender.setChecked(true);
        }

        if (motorInstalled.equalsIgnoreCase("No")) {

            checkBoxMotor.setChecked(false);
        } else {
            checkBoxMotor.setChecked(true);
        }

        if (buildingType.equalsIgnoreCase("Own")) {

            radioOwn.setChecked(true);

            llAttachPropTaxOwn.setVisibility(View.VISIBLE);
            llRentalAgreeCopy.setVisibility(View.GONE);

        } else {
            radioRental.setChecked(true);
            llAttachPropTaxOwn.setVisibility(View.GONE);
            llRentalAgreeCopy.setVisibility(View.VISIBLE);
        }

        if (rentalStatus.equalsIgnoreCase("No")) {

            checkBoxRental.setChecked(false);
        } else {
            checkBoxRental.setChecked(true);
        }

        if (nocStatus.equalsIgnoreCase("No")) {
            checkBoxNoc.setChecked(false);
        } else {
            checkBoxNoc.setChecked(true);
        }

        if (Check.equalsIgnoreCase("WithoutChange")) {
            checkBoxNoc.setClickable(false);
            checkBoxMotor.setClickable(false);
            checkBoxRental.setClickable(false);
        }
    }

    //This method is for setting activity to be initially
    public void setInitially() {

        tvChoosenAppPhoto.setEnabled(false);
        tvUploadAppPhoto.setEnabled(false);
        tvChoosenAppAddress.setEnabled(false);
        tvUploadAppAddress.setEnabled(false);
        tvChoosenRentAgreement.setEnabled(false);
        tvUploadRentalAgreement.setEnabled(false);
        tvChoosenNOCDoc.setEnabled(false);
        tvUploadNOCDoc.setEnabled(false);
        tvChoosenPropTax.setEnabled(false);
        tvUploadPropTax.setEnabled(false);
        tvRenewalRequest.setEnabled(false);

        etTradeDoorNo.setClickable(false);
        radioMale.setClickable(false);
        radioFemale.setClickable(false);
        radioTransgender.setClickable(false);
        radioRental.setClickable(false);
        radioOwn.setClickable(false);

    }

    //This method is for radio button validations
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_without_change:
                if (checked)

                    Check = "WithoutChange";

                llAppFirst.setVisibility(View.VISIBLE);
                llAppSecond.setVisibility(View.VISIBLE);

                enllAppFirst.setVisibility(View.GONE);
                enllAppSecond.setVisibility(View.GONE);

                cardApplicant.setCardBackgroundColor(getResources().getColor(R.color.grey));

                llWardStreet.setVisibility(View.VISIBLE);
                enllWardStreet.setVisibility(View.GONE);

                cardTrade.setCardBackgroundColor(getResources().getColor(R.color.grey));

                llDoorLi.setVisibility(View.VISIBLE);
                enllDoorLi.setVisibility(View.GONE);

                llTradeEst.setVisibility(View.VISIBLE);
                enllTradeEst.setVisibility(View.GONE);

                cardTradeDetails.setCardBackgroundColor(getResources().getColor(R.color.grey));

                cardAttachment.setCardBackgroundColor(getResources().getColor(R.color.grey));

                checkBoxMotor.setClickable(false);

                checkBoxRental.setClickable(false);

                if (radioRental.isChecked()) {

                    etRentPaid.setVisibility(View.VISIBLE);
                    etRentPaid.setHint("RentPaid");
                    tilRent.setVisibility(View.VISIBLE);
                    enetRentPaid.setVisibility(View.GONE);
                    enetRentPaid.setHint("");
                    entilRent.setVisibility(View.GONE);
                }

                checkBoxNoc.setClickable(true);

                etTradeDistrict.setFocusable(false);
                etTradeDistrict.setClickable(false);

                etTradePanchayat.setFocusable(false);
                etTradePanchayat.setClickable(false);

                etTradeStreetName.setFocusable(false);
                etTradeStreetName.setClickable(false);

                etTradeWardNo.setFocusable(false);
                etTradeWardNo.setClickable(false);

                etTradeName.setFocusable(false);
                etTradeName.setClickable(false);

                etTradeAmount.setFocusable(false);
                etTradeAmount.setClickable(false);

                etTradeESTName.setFocusable(false);
                etTradeESTName.setClickable(false);

                etNumberOfMachine.setFocusable(false);
                etNumberOfMachine.setClickable(false);

                etHorsePower.setFocusable(false);
                etHorsePower.setClickable(false);

                etRentPaid.setFocusable(false);
                etRentPaid.setClickable(false);

                radioMale.setClickable(false);
                radioFemale.setClickable(false);
                radioTransgender.setClickable(false);
                radioRental.setClickable(false);
                radioOwn.setClickable(false);

                tvChoosenAppPhoto.setEnabled(false);
                tvUploadAppPhoto.setEnabled(false);
                tvChoosenAppAddress.setEnabled(false);
                tvUploadAppAddress.setEnabled(false);
                tvChoosenRentAgreement.setEnabled(false);
                tvUploadRentalAgreement.setEnabled(false);
                tvChoosenNOCDoc.setEnabled(false);
                tvUploadNOCDoc.setEnabled(false);
                tvChoosenPropTax.setEnabled(false);
                tvUploadPropTax.setEnabled(false);
                tvRenewalRequest.setEnabled(false);

                break;

            case R.id.radio_with_change:
                if (checked)

                    Check = "WithChange";

                llAppFirst.setVisibility(View.GONE);
                llAppSecond.setVisibility(View.GONE);

                enllAppFirst.setVisibility(View.VISIBLE);
                enllAppSecond.setVisibility(View.VISIBLE);

                cardApplicant.setCardBackgroundColor(getResources().getColor(R.color.white));

                llWardStreet.setVisibility(View.GONE);
                enllWardStreet.setVisibility(View.VISIBLE);

                cardTrade.setCardBackgroundColor(getResources().getColor(R.color.white));

                llDoorLi.setVisibility(View.GONE);
                enllDoorLi.setVisibility(View.VISIBLE);

                llTradeEst.setVisibility(View.GONE);
                enllTradeEst.setVisibility(View.VISIBLE);

                cardTradeDetails.setCardBackgroundColor(getResources().getColor(R.color.white));

                cardAttachment.setCardBackgroundColor(getResources().getColor(R.color.white));

                checkBoxMotor.setClickable(true);
                //                checkBoxMotor.setEnabled(true);

                //                checkBoxRental.setEnabled(true);
                checkBoxRental.setClickable(true);

                if (radioRental.isChecked()) {

                    etRentPaid.setVisibility(View.GONE);
                    etRentPaid.setHint("");
                    tilRent.setVisibility(View.GONE);
                    enetRentPaid.setVisibility(View.VISIBLE);
                    enetRentPaid.setHint("RentPaid");
                    entilRent.setVisibility(View.VISIBLE);
                }

                //                checkBoxNoc.setEnabled(true);
                checkBoxNoc.setClickable(true);

//                    etTradeDistrict.setFocusable(true);
                etTradeDistrict.setClickable(false);

//                    etTradePanchayat.setFocusable(true);
                etTradePanchayat.setClickable(false);

//                    etTradeStreetName.setFocusable(true);
                etTradeStreetName.setClickable(true);

//                    etTradeWardNo.setFocusable(true);
                etTradeWardNo.setClickable(true);

//                    etTradeLicenceFor.setFocusable(true);
                etTradeLicenceFor.setClickable(false);

                etTradeName.setFocusable(true);
                etTradeName.setClickable(true);

                etTradeAmount.setFocusable(true);
                etTradeAmount.setClickable(true);

                etTradeESTName.setFocusable(true);
                etTradeESTName.setClickable(true);

                etNumberOfMachine.setFocusable(true);
                etNumberOfMachine.setClickable(true);

                etHorsePower.setFocusable(true);
                etHorsePower.setClickable(true);

                etRentPaid.setFocusable(true);
                etRentPaid.setClickable(true);

                radioMale.setClickable(true);
                radioFemale.setClickable(true);
                radioTransgender.setClickable(true);
                radioRental.setClickable(true);
                radioOwn.setClickable(true);

                tvChoosenAppPhoto.setEnabled(true);
                tvUploadAppPhoto.setEnabled(true);
                tvChoosenAppAddress.setEnabled(true);
                tvUploadAppAddress.setEnabled(true);
                tvChoosenRentAgreement.setEnabled(true);
                tvUploadRentalAgreement.setEnabled(true);
                tvChoosenNOCDoc.setEnabled(true);
                tvUploadNOCDoc.setEnabled(true);
                tvChoosenPropTax.setEnabled(true);
                tvUploadPropTax.setEnabled(true);
                tvRenewalRequest.setEnabled(true);

                break;

            case R.id.radio_male:
                if (checked)

                    Gender = "M";
                break;

            case R.id.radio_female:
                if (checked)

                    Gender = "F";
                break;

            case R.id.radio_tg:
                if (checked)

                    Gender = "Others";
                break;

            case R.id.radio_rental:
                if (checked)

                    buildingType = "Rental";

                llAttachPropTaxOwn.setVisibility(View.GONE);

                llRentalAgreeCopy.setVisibility(View.VISIBLE);

                if (Check.equalsIgnoreCase("WithoutChange")) {
                    etRentPaid.setVisibility(View.VISIBLE);
                    etRentPaid.setEnabled(false);
//                        etRentPaid.setHint("RentPaid");
                    enetRentPaid.setVisibility(View.GONE);
//                        enetRentPaid.setHint("");

                } else {

                    etRentPaid.setVisibility(View.GONE);
//                        etRentPaid.setHint("");
                    enetRentPaid.setVisibility(View.VISIBLE);
//                        enetRentPaid.setHint("RentPaid");
                }

                break;

            case R.id.radio_own:
                if (checked)

                    buildingType = "Rental";

                llAttachPropTaxOwn.setVisibility(View.VISIBLE);

                llRentalAgreeCopy.setVisibility(View.GONE);

                llAttachRentalAgreement.setVisibility(View.GONE);

                checkBoxRental.setChecked(false);


                break;
        }
    }

    //This method is for check box validations
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkbox_motor:
                if (checked) {

                    motorInstalled = "Yes";

                    if (Check.equalsIgnoreCase("WithoutChange")) {

                        llMotorTrade.setVisibility(View.VISIBLE);
                        enllMotorTrade.setVisibility(View.GONE);

                    } else {
                        llMotorTrade.setVisibility(View.GONE);
                        enllMotorTrade.setVisibility(View.VISIBLE);
                    }

                } else {

                    motorInstalled = "No";

                    llMotorTrade.setVisibility(View.GONE);
                    enllMotorTrade.setVisibility(View.GONE);

                }
                break;

            case R.id.checkbox_rental:
                if (checked) {

                    rentalAgreementStatus = "Yes";

                    llAttachRentalAgreement.setVisibility(View.VISIBLE);

                } else {
                    rentalAgreementStatus = "No";

                    llAttachRentalAgreement.setVisibility(View.GONE);
                }

                break;

            case R.id.checkbox_noc:
                if (checked) {

                    NOCStatus = "Yes";
                    llAttachNOC.setVisibility(View.VISIBLE);
                } else {

                    NOCStatus = "No";
                    llAttachNOC.setVisibility(View.GONE);
                }

                break;
        }
    }

    //This method is for retrieving list of ward no's
    public void wardNoList(String district, String panchayat) {

        progressDialog = new SpotsDialog(SaveLicenceRenewal.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        String url = API_GETWARDNO + district + "&Panchayat=" + panchayat;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.e(TAG, "----" + response.toString());
                        try {
                            JSONObject recoredSet = new JSONObject(response.toString());

                            JSONArray firstarray = new JSONArray(recoredSet.getString("recordsets"));

                            if (firstarray.length() > 0) {

                                for (int i = 0; i < firstarray.length(); i++) {

                                    JSONArray secondArray = firstarray.getJSONArray(0);

//                                    Log.e(TAG, "----" + secondArray.toString());

                                    if (secondArray.length() > 0) {

                                        for (int j = 0; j < secondArray.length(); j++) {

                                            JSONObject jsonObject = (JSONObject) secondArray.get(j);

                                            String wardNo = jsonObject.getString("WardNo");

                                            wardNo_items.add(wardNo);
                                            progressDialog.dismiss();

                                        }

                                        progressDialog.dismiss();
                                    } else {

                                        progressDialog.dismiss();
                                        Snackbar.make(rootLayout, "No Wards Found", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(rootLayout, "No Wards Found", Snackbar.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    SnackShowTop("Time out", rootLayout);
                } else if (error instanceof AuthFailureError) {
                    SnackShowTop("Connection Time out", rootLayout);
                } else if (error instanceof ServerError) {
                    SnackShowTop("Could not connect server", rootLayout);
                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rootLayout);
                } else if (error instanceof ParseError) {
                    SnackShowTop("Parse Error", rootLayout);
                } else {
                    SnackShowTop(error.getMessage(), rootLayout);
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;

            }
        };

        progressDialog.dismiss();

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, REQUEST_TAG);
    }

    //This method is for retrieving street names list
    public void streetList(String wardNo, String district, String panchayat) {

        progressDialog = new SpotsDialog(SaveLicenceRenewal.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        String url = API_GETSTREETNAMES + wardNo + "&District=" + district + "&Panchayat=" + panchayat;

//        Log.e(TAG, "---" + url);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.e(TAG, "----" + response.toString());
                        try {
                            JSONObject recoredSet = new JSONObject(response.toString());

                            JSONArray firstarray = new JSONArray(recoredSet.getString("recordsets"));

                            if (firstarray.length() > 0) {

                                for (int i = 0; i < firstarray.length(); i++) {

                                    JSONArray secondArray = firstarray.getJSONArray(0);

//                                    Log.e(TAG, "----" + secondArray.toString());

                                    if (secondArray.length() > 0) {

                                        for (int j = 0; j < secondArray.length(); j++) {

                                            JSONObject jsonObject = (JSONObject) secondArray.get(j);

                                            String streetNo = jsonObject.getString("StreetNo");
                                            String streetName = jsonObject.getString("StreetName");

                                            streetLists.add(new com.examp.three.model.Birth_Death.Street_Pojo(streetName, streetNo));
                                            progressDialog.dismiss();
                                        }

                                        progressDialog.dismiss();
                                    } else {

                                        progressDialog.dismiss();
                                        Snackbar.make(rootLayout, "No Streets Found", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(rootLayout, "No Streets Found", Snackbar.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    SnackShowTop("Time out", rootLayout);
                } else if (error instanceof AuthFailureError) {
                    SnackShowTop("Connection Time out", rootLayout);
                } else if (error instanceof ServerError) {
                    SnackShowTop("Could not connect server", rootLayout);
                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rootLayout);
                } else if (error instanceof ParseError) {
                    SnackShowTop("Parse Error", rootLayout);
                } else {
                    SnackShowTop(error.getMessage(), rootLayout);
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;

            }
        };

        progressDialog.dismiss();

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, REQUEST_TAG);
    }

    //This method is for setting street names in an adapter
    public void setStreetNames() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dview = li.inflate(R.layout.alert_org_names, null);

        builder.setView(dview);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);

        RecyclerView recyclerView = (RecyclerView) dview.findViewById(R.id.org_recycler);

        streetAdapter = new StreetAdapter(streetLists);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(streetAdapter);
        streetAdapter.notifyDataSetChanged();

        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, streetAdapter.getItemCount());

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(SaveLicenceRenewal.this, new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
                enetTradeStreetName.setText(streetLists.get(position).getStreetName());
                String streetName = streetLists.get(position).getStreetName();

                String stringIdCode = streetLists.get(position).getStreetNo();

                dialog.dismiss();

            }
        }));
    }

    //This method is for showing snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for showing the file types to choose
    private void showFileChooser() {

        String[] zips = {".zip", ".rar"};
        String[] pdfs = {".pdf"};
        String[] images = {".png", ".jpg"};
        int maxCount = MAX_ATTACHMENT_COUNT - photoPaths.size();
        if ((docPaths.size() + photoPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(getApplicationContext(), "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items",
                    Toast.LENGTH_SHORT).show();
        } else {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .setSelectedFiles(docPaths)
                    .setActivityTheme(R.style.LibAppTheme)
                    .setActivityTitle("Please select doc")
                    .addFileSupport("ZIP", zips)
                    .addFileSupport("PDF", pdfs, R.drawable.arrow_back)
                    .addFileSupport("IMAGES", images)
                    .enableDocSupport(false)
                    .enableSelectAll(true)
                    .sortDocumentsBy(SortingTypes.name)
                    .withOrientation(Orientation.UNSPECIFIED)
                    .pickFile(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                }
                break;

        }
        addFile(docPaths);
    }

    //This method is for getting file name for a selected file
    public void addFile(ArrayList<String> doc_paths) {

        if (doc_paths != null) {

            ArrayList<String> path = doc_paths;

            if (uploadSelectType.equalsIgnoreCase("ApplicantPhoto")) {
                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    tvChoosenAppPhoto.setText(String.valueOf(file.getName()));
                }
            } else if (uploadSelectType.equalsIgnoreCase("AddressProof")) {

                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    tvChoosenAppAddress.setText(String.valueOf(file.getName()));
                }
            } else if (uploadSelectType.equalsIgnoreCase("RentalAgreement")) {

                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    tvChoosenRentAgreement.setText(String.valueOf(file.getName()));
                }
            } else if (uploadSelectType.equalsIgnoreCase("NOCDocument")) {

                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    tvChoosenNOCDoc.setText(String.valueOf(file.getName()));
                }
            } else if (uploadSelectType.equalsIgnoreCase("PropertyTax")) {

                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    tvChoosenPropTax.setText(String.valueOf(file.getName()));
                }
            }
        }
    }

    //This method is for uploading image or file
    public void uploadImage(File file) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        final ProgressDialog dialog = new ProgressDialog(SaveLicenceRenewal.this);
        dialog.setMessage("uploading");
        dialog.show();

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call<Result> req = retrofitInterface.UploadLicenceRenewal(body, queryType,
                applicationNo, enetMobileNo.getText().toString(), "50015", enetFirstName.getText().toString(),
                "Android");

        req.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
//                Log.e("respomnse", "" + response.message());

                String message = response.body().getMesssage();
                Snackbar.make(rootLayout, "" + message, Snackbar.LENGTH_SHORT).show();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
//                Log.e("res", "" + t.getMessage());

                Snackbar.make(rootLayout, "" + t.getMessage(), Snackbar.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });
    }

}
