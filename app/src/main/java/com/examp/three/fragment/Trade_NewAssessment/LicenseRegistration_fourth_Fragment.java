package com.examp.three.fragment.Trade_NewAssessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.examp.three.activity.Trade.Helpher.LicenseHelpher;
import com.examp.three.activity.Trade.NewRegister_Activity;
import com.examp.three.activity.Trade.TL_InComplete_Activity;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.TradeLicence.TL_ViewToComp_Pojo;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;
/**
 * Created by priyadharshini on 31/07/2018.
 */

public class LicenseRegistration_fourth_Fragment extends Fragment implements Validator.ValidationListener {
    View v;

    @Nullable
    @BindView(R.id.li_parent_lay)
    LinearLayout li_parent_lay;
    @Nullable
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @Nullable
    @BindView(R.id.btn_back)
    Button btn_back;

    @Nullable
    @BindView(R.id.li_rental)
    LinearLayout li_isRental;

    @Nullable
    @BindView(R.id.li_property_receipt)
    LinearLayout li_peoperty;

    @Nullable
    @BindView(R.id.li_noc)
    LinearLayout li_isNoc;

    @Nullable
    @BindView(R.id.li_machinespe)
    LinearLayout li_machinespec;

    @Nullable
    @BindView(R.id.li_machineInstallation_dgm)
    LinearLayout li_machineinstallation;

    @Nullable
    @BindView(R.id.li_applicationphoto_upload)
    LinearLayout li_applicationphoto_upload;

    @Nullable
    @BindView(R.id.li_addressproof_upload)
    LinearLayout li_addressproof_upload;

    @Nullable
    @BindView(R.id.li_rental_upload)
    LinearLayout li_rental_upload;

    @Nullable
    @BindView(R.id.li_noc_upload)
    LinearLayout li_noc_upload;

    @Nullable
    @BindView(R.id.li_property_receipt_upload)
    LinearLayout li_property_upload;

    @Nullable
    @BindView(R.id.li_machinespec_upload)
    LinearLayout li_machinespec_upload;

    @Nullable
    @BindView(R.id.li_machine_installation_upload)
    LinearLayout li_machineinstallation_upload;

    @Nullable
    @BindView(R.id.et_ApplicationPhoto)
    EditText et_applicationPhoto;

    @Nullable
    @BindView(R.id.et_address_proof)
    EditText et_address_proof;

    @Nullable
    @BindView(R.id.et_rental)
    EditText et_rental;

    @Nullable
    @BindView(R.id.et_noc_doc)
    EditText et_noc_doc;

    @Nullable
    @BindView(R.id.et_property_receipt)
    EditText et_property_doc;

    @Nullable
    @BindView(R.id.et_machinespec_doc)
    EditText et_machinespec_doc;

    @Nullable
    @BindView(R.id.et_machineinstalltion_doc)
    EditText et_machineinstallation_doc;
    LicenseHelpher mLicenseHelpher;

    SharedPreferenceHelper sharedPreferenceHelpher;

    String mqType, mSno, mExApplicationNo, mApplicationType, mApplicantInitial, mApplicantFirstName,
            ApGender, mApFatherInitial, mApFatherFirstName, mApMobileNo, mApEmail, mApDoorNo, mApStreetName,
            ApCityName, mApDistrict, mApPINCode, mApAadharNo, mApGSTNo, District, Panchayat, mWardNo, StreetName, mDoorNo,
            LicenceValidity, mLicenceTypeId, TradeName,
            TradeCode, mTradeRate, mEstablishmentName, mmotorInstalled,
            motorTotalQty, mHorsePowerTotal, mBuldingType, mRentPaid,
            mRentalAgrStatus, mNOCStatus, mFinancialYear, mEntryUserId,
            mEntryUser, mValidFrom, mValidTo, mPenalty,
            mPFA, mmAdvertisement, mServiceCharge, mEntryType, validFrom, validTo;

    boolean isMotorInstalled;
    boolean isRental;
    boolean mHaveRentalCopy;
    boolean mHaveNocDoc;

    File fileApplicantPhoto;
    File fileAddressProof;
    File fileNoc;
    File fileRental;
    File fileProperty;
    File fileMecineSpec;
    File fileMachineInstallation;

    int PICK_APPLICANT_PHOTO_REQUEST = 100;
    int PICK_ADDRESS_PROOF_REQUEST = 101;
    int PICK_NOC_REQUEST = 102;
    int PICK_RENTAL_REQUEST = 103;
    int PICK_PROPERTY_REQUEST = 103;
    int PICK_MECHINESPEC_REQUEST = 103;
    int PICK_MECHINEINSTALLATION_REQUEST = 103;

    int applicantphoto_doc = 0;
    int addressproof_doc = 0;
    int noc_doc = 0;
    int rental_doc = 0;
    int machine_spec = 0;
    int property_doc = 0;
    int machine_installation_doc = 0;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    String userid = "", username = "Vediyappan", appregid = "122", mobileno = "87454541541", prefSMotor, prefsRental, prefsNOC;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_licenseregistration_four, container, false);

        ButterKnife.bind(this, v);
        onClicks();
        mLicenseHelpher = LicenseHelpher.getInstance(getActivity());

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userid= sharedPreference.getString(pref_login_userid,"");

        sharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());
        prefSMotor = sharedPreferenceHelpher.getSpecificValues("PREF_Motor");
        prefsNOC = sharedPreferenceHelpher.getSpecificValues("PREF_Noc");
        prefsRental = sharedPreferenceHelpher.getSpecificValues("PREF_Rental");

        isMotorInstalled = sharedPreferenceHelpher.getSpecificBooleanValues("PREF_motorinstalled");
        isRental = sharedPreferenceHelpher.getSpecificBooleanValues("PREF_isrental");
        mHaveRentalCopy = sharedPreferenceHelpher.getSpecificBooleanValues("PREF_rentalagreementstatus");
        mHaveNocDoc = sharedPreferenceHelpher.getSpecificBooleanValues("PREF_havenocdoc");

        mSno = sharedPreferenceHelpher.getSpecificValues("PREF_sno");
        mApplicantInitial = sharedPreferenceHelpher.getSpecificValues("PREF_tl_appInitials");
        mApplicantFirstName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_appName");
        ApGender = sharedPreferenceHelpher.getSpecificValues("PREF_tl_gender");
        mApFatherInitial = sharedPreferenceHelpher.getSpecificValues("PREF_tl_fmhgInitials");
        mApFatherFirstName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_fmhgName");
        mApMobileNo = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mobileNo");
        mApEmail = sharedPreferenceHelpher.getSpecificValues("PREF_tl_emailid");
        mApGSTNo = sharedPreferenceHelpher.getSpecificValues("PREF_tl_gst");

        mApDoorNo = sharedPreferenceHelpher.getSpecificValues("PREF_tl_apdoorNo");
        mApStreetName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_apstreetName");
        ApCityName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_apcityName");
        mApPINCode = sharedPreferenceHelpher.getSpecificValues("PREF_tl_appincode");
        mApAadharNo = sharedPreferenceHelpher.getSpecificValues("PREF_tl_aadharNo");
        District = sharedPreferenceHelpher.getSpecificValues("PREF_tl_district");
        Panchayat = sharedPreferenceHelpher.getSpecificValues("PREF_tl_panchayat");
        mWardNo = sharedPreferenceHelpher.getSpecificValues("PREF_tl_wardNo");
        StreetName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_streetName");
        mDoorNo = sharedPreferenceHelpher.getSpecificValues("PREF_tl_doorNo");
        LicenceValidity = sharedPreferenceHelpher.getSpecificValues("PREF_tl_licenseFor");

        mLicenceTypeId = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mLicenceTypeId");
        TradeName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mTradeName");
        TradeCode = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mTradeCode");
        mTradeRate = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mTradeRate");
        mEstablishmentName = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mestablishmentname");
        mmotorInstalled = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mmotorinstalled");
        motorTotalQty = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mnumberOfMotorsInstalled");
        mHorsePowerTotal = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mTotalHorsePower");
        mRentPaid = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mrental_paid");
        mRentalAgrStatus = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mRentalAgrStatus");
        mNOCStatus = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mNOCStatus");
        mFinancialYear = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mFinancialYear");
        mEntryUserId = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mUserId");
        mEntryUser = sharedPreferenceHelpher.getSpecificValues("PREF_tl_mEntryUser");

        if (Common.isMotorInstalled) {
//        if (prefSMotor != null && prefSMotor.equalsIgnoreCase("Yes")) {
            li_machinespec.setVisibility(View.VISIBLE);
            li_machineinstallation.setVisibility(View.VISIBLE);
        } else {
            li_machinespec.setVisibility(View.GONE);
            li_machineinstallation.setVisibility(View.GONE);
        }

        if (Common.mHaveNocDoc) {
//        if (prefsNOC != null && prefsNOC.equalsIgnoreCase("Yes")) {

            li_isNoc.setVisibility(View.VISIBLE);
        } else {
            li_isNoc.setVisibility(View.GONE);

        }

        if (Common.isRental) {
//        if (prefsRental != null && prefsRental.equalsIgnoreCase("Yes")) {
            if (Common.mHaveRentalCopy) {
                li_isRental.setVisibility(View.VISIBLE);
                li_peoperty.setVisibility(View.GONE);
            } else {
                li_peoperty.setVisibility(View.VISIBLE);
                li_isRental.setVisibility(View.GONE);

            }
        } else {
            li_isRental.setVisibility(View.GONE);
        }


        return v;
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(TL_ViewToComp_Pojo pojo) {

        if (!TextUtils.isEmpty(pojo.getRentalAgrStatus())) {
            if (pojo.getRentalAgrStatus().equalsIgnoreCase("Yes")) {
                li_isRental.setVisibility(View.VISIBLE);

                et_rental.setText(pojo.getAgreementCopy());
            } else {
                li_isRental.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(pojo.getMotorInstalled())) {
            if (pojo.getMotorInstalled().equalsIgnoreCase("true")) {
                li_machinespec.setVisibility(View.VISIBLE);
                li_machineinstallation.setVisibility(View.VISIBLE);


                et_machinespec_doc.setText(pojo.getMachineSpecifications());
                et_machineinstallation_doc.setText(pojo.getMachineInstallationDig());
            } else {
                li_machinespec.setVisibility(View.GONE);
                li_machineinstallation.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(pojo.getNOCStatus())) {

            if (pojo.getNOCStatus().equalsIgnoreCase("Yes")) {
                li_isNoc.setVisibility(View.VISIBLE);
                et_noc_doc.setText(pojo.getNOCCopy());
            } else {
                li_isNoc.setVisibility(View.GONE);
            }

        }

        if (!TextUtils.isEmpty(pojo.getApplicantPhoto())) {
            et_applicationPhoto.setText(pojo.getApplicantPhoto());
        }

        if (!TextUtils.isEmpty(pojo.getAddressProofCopy())) {
            et_address_proof.setText(pojo.getAddressProofCopy());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_ADDRESS_PROOF_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(!TextUtils.isEmpty(filePath)){
                fileAddressProof = new File(filePath);

                et_address_proof.setText(String.valueOf(fileAddressProof.getName()));
            }


        }

        if (requestCode == PICK_APPLICANT_PHOTO_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);

            if(!TextUtils.isEmpty(filePath)){
                fileApplicantPhoto = new File(filePath);

                et_applicationPhoto.setText(String.valueOf(fileApplicantPhoto.getName()));
            }
            cursor.close();
        }

        if (requestCode == PICK_RENTAL_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(!TextUtils.isEmpty(filePath)){
                fileRental = new File(filePath);

                et_rental.setText(String.valueOf(fileRental.getName()));
            }


        }

        if (requestCode == PICK_NOC_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(!TextUtils.isEmpty(filePath)){
                fileNoc = new File(filePath);

                et_noc_doc.setText(String.valueOf(fileNoc.getName()));
            }


        }

        if (requestCode == PICK_PROPERTY_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(!TextUtils.isEmpty(filePath)){
                fileProperty = new File(filePath);

                et_property_doc.setText(String.valueOf(fileProperty.getName()));
            }


        }

        if (requestCode == PICK_MECHINESPEC_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(!TextUtils.isEmpty(filePath)){
                fileMecineSpec = new File(filePath);

                et_machinespec_doc.setText(String.valueOf(fileMecineSpec.getName()));
            }


        }

        if (requestCode == PICK_MECHINEINSTALLATION_REQUEST && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(!TextUtils.isEmpty(filePath)){
                fileMachineInstallation = new File(filePath);

                et_machineinstallation_doc.setText(String.valueOf(fileMachineInstallation.getName()));
            }


        }
    }

    public void uploadImage(File file, final String utype, String mobileno, String applicationregid, String userid, String username, String entrytype) {


        RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("uploading");
        dialog.show();

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call<ResponseBody> req = retrofitInterface.UploadTradeDoc(body, utype, applicationregid, mobileno, userid, username, entrytype);
        req.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("respomnse", "" + response.message());
                SnackShowTop(" Upload Success", li_parent_lay);

                if (utype.equalsIgnoreCase("InsertNOCDoc")) {
                    noc_doc = 1;
                }

                if (utype.equalsIgnoreCase("InsertPtrDoc")) {
                    property_doc = 1;
                }

                if (utype.equalsIgnoreCase("InsertMachineSpec")) {
                    machine_spec = 1;
                }

                if (utype.equalsIgnoreCase("InsertMachineInsDigm")) {
                    machine_installation_doc = 1;
                }

                if (utype.equalsIgnoreCase("InsertPhoto")) {
                    applicantphoto_doc = 1;
                }

                if (utype.equalsIgnoreCase("InsertApfDoc")) {
                    addressproof_doc = 1;
                }

                if (utype.equalsIgnoreCase("InsertRentalDoc")) {
                    rental_doc = 1;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("res", "" + t.getMessage());
                dialog.dismiss();

            }
        });
    }

    private void showFileChooser(int PICK_IMAGE_REQUEST) {
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "image/*",
                        "text/html",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), PICK_IMAGE_REQUEST);

    }

    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        snack.show();
    }

    public void onClicks() {

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                validator.validate();

                if(Common.isNetworkAvailable(getActivity())){
                    nextFragement();
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }

            }


        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = NewRegister_Activity.license_registration_pager.getCurrentItem();
                NewRegister_Activity.license_registration_pager.setCurrentItem(current - 1);
            }
        });

        et_noc_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_NOC_REQUEST);
            }
        });
        et_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_ADDRESS_PROOF_REQUEST);
            }
        });

        et_applicationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_APPLICANT_PHOTO_REQUEST);
            }
        });

        et_rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_RENTAL_REQUEST);
            }
        });

        et_property_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_PROPERTY_REQUEST);

            }
        });


        et_machinespec_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_MECHINESPEC_REQUEST);
            }
        });

        et_machineinstallation_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(PICK_MECHINEINSTALLATION_REQUEST);
            }
        });


        li_noc_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileNoc!=null)
                    if(Common.isNetworkAvailable(getActivity())){
                        uploadImage(fileNoc, "InsertNOCDoc", mobileno, appregid, userid, username, "Android");
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }
                else
                Snackbar.make(li_parent_lay, "Please upload Noc Document", Snackbar.LENGTH_SHORT).show();
            }
        });

        li_addressproof_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileAddressProof!=null)
                    if(Common.isNetworkAvailable(getActivity())){
                        uploadImage(fileAddressProof, "InsertApfDoc", mobileno, appregid, userid, username, "Android");
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }
                else
                Snackbar.make(li_parent_lay, "Please upload AddressProof Document", Snackbar.LENGTH_SHORT).show();
            }
        });

        li_rental_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileRental!=null)
                    if(Common.isNetworkAvailable(getActivity())){
                        uploadImage(fileRental, "InsertRentalDoc", mobileno, appregid, userid, username, "Android");
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }
                else
                Snackbar.make(li_parent_lay, "Please upload Rental Document", Snackbar.LENGTH_SHORT).show();
            }
        });

        li_applicationphoto_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileApplicantPhoto!=null
                    if(Common.isNetworkAvailable(getActivity())){
                        uploadImage(fileApplicantPhoto, "InsertPhoto", mobileno, appregid, userid, username, "Android");
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }  else
                Snackbar.make(li_parent_lay, "Please upload Applicant Photo", Snackbar.LENGTH_SHORT).show();
            }
        });

        li_property_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileProperty!=null)
                uploadImage(fileProperty, "InsertPtrDoc", mobileno, appregid, userid, username, "Android");
                else
                Snackbar.make(li_parent_lay, "Please upload Property Document", Snackbar.LENGTH_SHORT).show();
            }
        });


        li_machinespec_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileMecineSpec!=null)
                uploadImage(fileMecineSpec, "InsertMachineSpec", mobileno, appregid, userid, username, "Android");
                else
                Snackbar.make(li_parent_lay, "Please upload Machine Specification Document", Snackbar.LENGTH_SHORT).show();
            }
        });


        li_machineinstallation_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileMachineInstallation!=null)
                uploadImage(fileMachineInstallation, "InsertMachineInsDigm", mobileno, appregid, userid, username, "Android");
                else
                Snackbar.make(li_parent_lay, "Please upload MachineInstallations Document", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void nextFragement() {
        SubmitAllDetails();
    }

    public void SubmitAllDetails() {

        if (mRentalAgrStatus.equalsIgnoreCase("Yes")) {
            mBuldingType = "Rental";
        } else {
            mBuldingType = "Own";
        }

            String validYear = LicenceValidity;
            String sYear[] = validYear.split("-");
            String year1 = sYear[0];
            String year2 = sYear[1];
            validFrom = year1+"-04-01";
            validTo = year2+"-03-31";

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.saveTradeLicenseDetails(Common.ACCESS_TOKEN, "Insert",
                mSno, "t3", "New", mApplicantInitial,
                mApplicantFirstName, ApGender, mApFatherInitial, mApFatherFirstName, mApMobileNo,
                mApEmail, mApDoorNo, mApStreetName, ApCityName, null, mApPINCode,
                mApAadharNo, mApGSTNo, District, Panchayat, mWardNo, StreetName,
                mDoorNo, mLicenseHelpher.mFinancialYear, mLicenceTypeId,
                TradeName, TradeCode, mTradeRate,
                mEstablishmentName,
                mmotorInstalled, motorTotalQty, mHorsePowerTotal,
                mBuldingType, mRentPaid, mRentalAgrStatus, mNOCStatus,
                "2018-2019",
                mEntryUserId, mEntryUser, mLicenseHelpher.validFromDate, mLicenseHelpher.validToDate, "0",
                "0", "0", "0", "Android");
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("url 3 ", "---" + response.raw().toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);
                JSONObject records = null;
                try {
                    records = new JSONObject(response1);
                    String res = records.getString("message");

                    if (res.contains("Success")) {

                        Toast.makeText(getActivity(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(li_parent_lay, "Submitted Successfully!", Snackbar.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), TL_InComplete_Activity.class);
                        startActivity(i);

                    } else {
                        Snackbar.make(li_parent_lay, res, Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Snackbar.make(li_parent_lay, t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        int current = NewRegister_Activity.license_registration_pager.getCurrentItem();
        NewRegister_Activity.license_registration_pager.setCurrentItem(current + 1);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {

            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());

            //display the error message
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                view.requestFocus();
            }
        }
    }

}
