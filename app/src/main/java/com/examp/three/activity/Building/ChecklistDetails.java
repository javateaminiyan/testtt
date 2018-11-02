package com.examp.three.activity.Building;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.examp.three.R;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChecklistDetails extends AppCompatActivity {

    @Nullable
    @BindView(R.id.buildingplan_documentUpload_toolbar) Toolbar tb_buildingplan_documentUpload_toolbar;

    @Nullable
    @BindView(R.id.et_building_copy) EditText et_building_copy;
    @Nullable
    @BindView(R.id.et_PropertyDocuments) EditText et_PropertyDocuments;
    @Nullable
    @BindView(R.id.et_PattaCertificate) EditText et_PattaCertificate;
    @Nullable
    @BindView(R.id.et_RegisteredDocument) EditText et_RegisteredDocument;
    @Nullable
    @BindView(R.id.et_PropertyHolderPhoto) EditText et_PropertyHolderPhoto;
    @Nullable
    @BindView(R.id.et_PhotoOfAssessment) EditText et_PhotoOfAssessment;
    @Nullable
    @BindView(R.id.et_EncumbranceCertificate) EditText et_EncumbranceCertificate;
    @Nullable
    @BindView(R.id.et_LayoutCopy) EditText et_LayoutCopy;
    @Nullable
    @BindView(R.id.et_DemolitionPlan) EditText et_DemolitionPlan;
    @Nullable
    @BindView(R.id.et_ProbatedOrderCopy) EditText et_ProbatedOrderCopy;
    @Nullable
    @BindView(R.id.et_GPADocumentCopy) EditText et_GPADocumentCopy;
    @Nullable
    @BindView(R.id.et_ReconstitutionDeed) EditText et_ReconstitutionDeed;
    @Nullable
    @BindView(R.id.et_LegalHeirShipCertificate) EditText et_LegalHeirShipCertificate;
    @Nullable
    @BindView(R.id.et_LandTaxReceipt) EditText et_LandTaxReceipt;
    @Nullable
    @BindView(R.id.et_ExistPlanCopy) EditText et_ExistPlanCopy;
    @Nullable
    @BindView(R.id.et_NOC) EditText et_NOC;

    @Nullable
    @BindView(R.id.img_building_copy) ImageView img_building_copy;
    @Nullable
    @BindView(R.id.img_PropertyDocuments) ImageView img_PropertyDocuments;
    @Nullable
    @BindView(R.id.img_PattaCertificate) ImageView img_PattaCertificate;
    @Nullable
    @BindView(R.id.img_RegisteredDocument) ImageView img_RegisteredDocument;
    @Nullable
    @BindView(R.id.img_PropertyHolderPhoto) ImageView img_PropertyHolderPhoto;
    @Nullable
    @BindView(R.id.img_PhotoOfAssessment) ImageView img_PhotoOfAssessment;
    @Nullable
    @BindView(R.id.img_EncumbranceCertificate) ImageView img_EncumbranceCertificate;
    @Nullable
    @BindView(R.id.img_LayoutCopy) ImageView img_LayoutCopy;
    @Nullable
    @BindView(R.id.img_DemolitionPlan) ImageView img_DemolitionPlan;
    @Nullable
    @BindView(R.id.img_ProbatedOrderCopy) ImageView img_ProbatedOrderCopy;
    @Nullable
    @BindView(R.id.img_GPADocumentCopy) ImageView img_GPADocumentCopy;
    @Nullable
    @BindView(R.id.img_ReconstitutionDeed) ImageView img_ReconstitutionDeed;
    @Nullable
    @BindView(R.id.img_LegalHeirShipCertificate) ImageView img_LegalHeirShipCertificate;
    @Nullable
    @BindView(R.id.img_LandTaxReceipt) ImageView img_LandTaxReceipt;
    @Nullable
    @BindView(R.id.img_ExistPlanCopy) ImageView img_ExistPlanCopy;
    @Nullable
    @BindView(R.id.img_NOC) ImageView img_NOC;

    @Nullable
    @BindView(R.id.li_parent_lay) LinearLayout li_parent_lay;

    @Nullable
    @BindView(R.id.btn_submit) Button btn_submit;

    @Nullable
    @BindView(R.id.btn_reset) Button btn_reset;

    static final int BUILDING_PLAN_COPY = 101;
    static final int PROPERTYDOCUMENTS = 102;
    static final int PATTACERTIFICATE = 103;
    static final int REGISTEREDDOCUMENT = 104;
    static final int PROPERTYHOLDERPHOTO = 105;
    static final int PHOTOOFASSESSMENT = 106;
    static final int ENCUMBERANCECERTIFICATE = 107;
    static final int LAYOUTCOPY = 108;
    static final int DEMOLITIONPLAN = 109;
    static final int PROBATEDORDERCOPY = 110;
    static final int GPADOCUMENTCOPY = 111;
    static final int RECONSTITUNEDDEED = 112;
    static final int LEGALHEIRSHIPCERTIFICATE= 113;
    static final int LANDTAXRECEIPT= 114;
    static final int EXISTPLANCOPY= 115;
    static final int NOC= 116;

    File mFileBuildingPlanCopy;
    File mPropertyDocuments;
    File mPattaCertificate;
    File mRegisteredDocument;
    File mPropertyHolderPhoto;
    File mPhotoOfAssessment;
    File mEncumbranceCertificate;
    File mLayoutCopy;
    File mDemolitionPlan;
    File mProbatedOrderCopy;
    File mGPADocumentCopy;
    File mReconstitutionDeed;
    File mLegalHeirShipCertificate;
    File mLandTaxReceipt;
    File mExistPlanCopy;
    File mNOC;

    String mApplicationId;
    String mPanchayat;
    String mDistrict;

    BuildingPlanHelpherOne mBuildingPlanHelpherOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_details);
        ButterKnife.bind(this);

        mBuildingPlanHelpherOne = BuildingPlanHelpherOne.getInstance(ChecklistDetails.this);
        mApplicationId = getIntent().getStringExtra("ApplicationId");
        mPanchayat = getIntent().getStringExtra("Panchayat");
        mDistrict = getIntent().getStringExtra("District");

        tb_buildingplan_documentUpload_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
        setSupportActionBar(tb_buildingplan_documentUpload_toolbar);

        tb_buildingplan_documentUpload_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistDetails.this,NewApplicationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Building plan
        img_building_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mFileBuildingPlanCopy!=null)
                mBuildingPlanHelpherOne.uploadImage(mFileBuildingPlanCopy, "BuildingPlanCopy", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                      mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_building_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                mBuildingPlanHelpherOne.showFileChooser(BUILDING_PLAN_COPY);
            }
        });
        //PropertyDocuments
        img_PropertyDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mPropertyDocuments!=null)
                    mBuildingPlanHelpherOne.uploadImage(mPropertyDocuments, "PropertyDocuments", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_PropertyDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(PROPERTYDOCUMENTS);
            }
        });
        //PattaCertificate
        img_PattaCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                   if(mPattaCertificate!=null)
                    mBuildingPlanHelpherOne.uploadImage(mPattaCertificate, "PattaCertificate", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                else
                    mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_PattaCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(PATTACERTIFICATE);
            }
        });
        //RegisteredDocument
        img_RegisteredDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mRegisteredDocument!=null)
                    mBuildingPlanHelpherOne.uploadImage(mRegisteredDocument, "RegisteredDocument", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                else
                    mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_RegisteredDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(REGISTEREDDOCUMENT);
            }
        });
        //PropertyHolderPhoto
        img_PropertyHolderPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                   if(mPropertyHolderPhoto!=null)
                    mBuildingPlanHelpherOne.uploadImage(mPropertyHolderPhoto, "PropertyHolderPhoto", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                else
                    mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_PropertyHolderPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())

                    mBuildingPlanHelpherOne.showFileChooser(PROPERTYHOLDERPHOTO);

            }
        });
        //PhotoOfAssessment
        img_PhotoOfAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                if(mPhotoOfAssessment!=null)
                    mBuildingPlanHelpherOne.uploadImage(mPhotoOfAssessment, "PhotoOfAssessment", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                else
                    mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_PhotoOfAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(PHOTOOFASSESSMENT);
            }
        });
        //EncumbranceCertificate
        img_EncumbranceCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mEncumbranceCertificate!=null)
                    mBuildingPlanHelpherOne.uploadImage(mEncumbranceCertificate, "EncumbranceCertificate", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_EncumbranceCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(ENCUMBERANCECERTIFICATE);

            }
        });
        //LayoutCopy
        img_LayoutCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mLayoutCopy!=null)
                    mBuildingPlanHelpherOne.uploadImage(mLayoutCopy, "LayoutCopy", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_LayoutCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(LAYOUTCOPY);
            }
        });
        //DemolitionPlan
        img_DemolitionPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mDemolitionPlan!=null)
                    mBuildingPlanHelpherOne.uploadImage(mDemolitionPlan, "DemolitionPlan", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
             }
        });
        et_DemolitionPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(DEMOLITIONPLAN);
            }
        });
        //ProbatedOrderCopy
        img_ProbatedOrderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mProbatedOrderCopy!=null)
                    mBuildingPlanHelpherOne.uploadImage(mProbatedOrderCopy, "ProbatedOrderCopy", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_ProbatedOrderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(PROBATEDORDERCOPY);
            }
        });

        //GPADocumentCopy
        img_GPADocumentCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mGPADocumentCopy!=null)
                    mBuildingPlanHelpherOne.uploadImage(mGPADocumentCopy, "GPADocumentCopy", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_GPADocumentCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(GPADOCUMENTCOPY);
            }
        });
        //ReconstitutionDeed
        img_ReconstitutionDeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mReconstitutionDeed!=null)
                    mBuildingPlanHelpherOne.uploadImage(mReconstitutionDeed, "ReconstitutionDeed", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_ReconstitutionDeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(RECONSTITUNEDDEED);
            }
        });
        //LegalHeirShipCertificate
        img_LegalHeirShipCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mLegalHeirShipCertificate!=null)
                    mBuildingPlanHelpherOne.uploadImage(mLegalHeirShipCertificate, "LegalHeirShipCertificate", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_LegalHeirShipCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(LEGALHEIRSHIPCERTIFICATE);
            }
        });
        //LandTaxReceipt
        img_LandTaxReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mLandTaxReceipt!=null)
                    mBuildingPlanHelpherOne.uploadImage(mLandTaxReceipt, "LandTaxReceipt", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_LandTaxReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(LANDTAXRECEIPT);
            }
        });
        // ExistPlanCopy
        img_ExistPlanCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mExistPlanCopy!=null)
                    mBuildingPlanHelpherOne.uploadImage(mExistPlanCopy, "ExistPlanCopy", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_ExistPlanCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(EXISTPLANCOPY);
            }
        });
        //NOC
        img_NOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    if(mNOC!=null)
                    mBuildingPlanHelpherOne.uploadImage(mNOC, "NOC", mApplicationId, mDistrict, mPanchayat,li_parent_lay);
                    else
                        mBuildingPlanHelpherOne.SnackShowTop("Please select the File",li_parent_lay);
            }
        });
        et_NOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.isReadStoragePermissionGranted() && mBuildingPlanHelpherOne.isWriteStoragePermissionGranted())
                    mBuildingPlanHelpherOne.showFileChooser(NOC);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuildingPlanHelpherOne.photoOfAssessment == 1){
                    Intent HomeRedirect = new Intent(ChecklistDetails.this, BP_InComplete_Activity.class);
                    startActivity(HomeRedirect);
                }else{
                    mBuildingPlanHelpherOne.SnackShowTop("Photo Of Assessment Document is Required",li_parent_lay);
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadActivity();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //1
        if (requestCode == BUILDING_PLAN_COPY && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

           if(! TextUtils.isEmpty(filePath)) {
               mFileBuildingPlanCopy = new File(filePath);

               et_building_copy.setText(String.valueOf(mFileBuildingPlanCopy.getName()));
           }

        }//2
        if (requestCode == PROPERTYDOCUMENTS && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)) {
                mPropertyDocuments = new File(filePath);

                et_PropertyDocuments.setText(String.valueOf(mPropertyDocuments.getName()));
            }


        }//3
        if (requestCode == PATTACERTIFICATE && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mPattaCertificate = new File(filePath);

                et_PattaCertificate.setText(String.valueOf(mPattaCertificate.getName()));
            }


        }//4
        if (requestCode == REGISTEREDDOCUMENT && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mRegisteredDocument = new File(filePath);

                et_RegisteredDocument.setText(String.valueOf(mRegisteredDocument.getName()));
            }


        }//5
        if (requestCode == PROPERTYHOLDERPHOTO && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mPropertyHolderPhoto = new File(filePath);

                et_PropertyHolderPhoto.setText(String.valueOf(mPropertyHolderPhoto.getName()));
            }


        }//6
        if (requestCode == PHOTOOFASSESSMENT && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mPhotoOfAssessment = new File(filePath);

                et_PhotoOfAssessment.setText(String.valueOf(mPhotoOfAssessment.getName()));
            }

        }//7
        if (requestCode == ENCUMBERANCECERTIFICATE && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mEncumbranceCertificate = new File(filePath);

                et_EncumbranceCertificate.setText(String.valueOf(mEncumbranceCertificate.getName()));
            }


        }//8
        if (requestCode == LAYOUTCOPY && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mLayoutCopy = new File(filePath);

                et_LayoutCopy.setText(String.valueOf(mLayoutCopy.getName()));
            }


        }//9
        if (requestCode == DEMOLITIONPLAN && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mDemolitionPlan = new File(filePath);

                et_DemolitionPlan.setText(String.valueOf(mDemolitionPlan.getName()));
            }


        }//10
        if (requestCode == PROBATEDORDERCOPY && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mProbatedOrderCopy = new File(filePath);

                et_ProbatedOrderCopy.setText(String.valueOf(mProbatedOrderCopy.getName()));

            }

        }//11
        if (requestCode == GPADOCUMENTCOPY && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mGPADocumentCopy = new File(filePath);

                et_GPADocumentCopy.setText(String.valueOf(mGPADocumentCopy.getName()));
            }


        }//12
        if (requestCode == RECONSTITUNEDDEED && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mReconstitutionDeed = new File(filePath);

                et_ReconstitutionDeed.setText(String.valueOf(mReconstitutionDeed.getName()));
            }


        }//13
        if (requestCode == LEGALHEIRSHIPCERTIFICATE && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mLegalHeirShipCertificate = new File(filePath);

                et_LegalHeirShipCertificate.setText(String.valueOf(mLegalHeirShipCertificate.getName()));
            }


        }//14
        if (requestCode == LANDTAXRECEIPT && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mLandTaxReceipt = new File(filePath);

                et_LandTaxReceipt.setText(String.valueOf(mLandTaxReceipt.getName()));
            }


        }//15
        if (requestCode == EXISTPLANCOPY && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mExistPlanCopy = new File(filePath);

                et_ExistPlanCopy.setText(String.valueOf(mExistPlanCopy.getName()));
            }


        }//16
        if (requestCode == NOC && resultCode == RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            if(! TextUtils.isEmpty(filePath)){
                mNOC = new File(filePath);

                et_NOC.setText(String.valueOf(mNOC.getName()));
            }

        }
    }

    //This method is for reloading the same activity
    public void reloadActivity(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}
