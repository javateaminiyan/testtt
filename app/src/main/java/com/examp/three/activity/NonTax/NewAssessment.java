package com.examp.three.activity.NonTax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.examp.three.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.examp.three.activity.NonTax.HelpherClasses.NewAssessmentHelpher;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYATID;
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class NewAssessment extends AppCompatActivity implements Validator.ValidationListener {

    //OBJECT DECLARATIONS
    NewAssessmentHelpher newAssessmentHelpher;
    Validator validator;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_district)
    EditText et_district;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_panchayat)
    EditText et_panchayat;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_name)
    EditText et_name;
    @Length(min = 10, max = 10, message = "Mobile length shoul be 10")
    @NotEmpty
    @Nullable
    @BindView(R.id.et_mobileno)
    EditText et_mobileno;
    @Email
    @NotEmpty
    @Nullable
    @BindView(R.id.et_emailid)
    EditText et_emailid;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_leasename)
    EditText et_leasename;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_doorno)
    EditText et_doorno;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_wardno)
    EditText et_wardno;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_streetname)
    EditText et_streetname;
    @Nullable
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @Nullable
    @BindView(R.id.nontax_newassessment_toolbar)
    Toolbar nontax_newassessment_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_new_assessment);
        ButterKnife.bind(this);
        newAssessmentHelpher = NewAssessmentHelpher.getInstance(NewAssessment.this);
        setSupportActionBar(nontax_newassessment_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.New_Assessment);

        if (!sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT).isEmpty() && sharedPreferenceHelpher.getInt(PREF_SELECTDISTRICTID)!=0) {
            et_district.setText(sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT));
            newAssessmentHelpher.mdistrictCode = sharedPreferenceHelpher.getInt(PREF_SELECTDISTRICTID);
            newAssessmentHelpher.mselectedDistrict = sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT);
        } else {
            et_district.setText("");
        }
        if (!sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT).isEmpty() && sharedPreferenceHelpher.getInt(PREF_SELECTPANCHAYATID)!=0) {
            et_panchayat.setText(sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT));
            newAssessmentHelpher.mpanchayatCode  = sharedPreferenceHelpher.getInt(PREF_SELECTPANCHAYATID);
            newAssessmentHelpher.mselectedPanchayat  = sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT);
        }else{
            et_panchayat.setText("");
        }

        et_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAssessmentHelpher.getDistricts(et_district);
            }
        });
        et_panchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAssessmentHelpher.getPanchayat(et_panchayat);
            }
        });
        et_wardno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAssessmentHelpher.getWardNo(et_wardno);
            }
        });
        et_streetname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAssessmentHelpher.getStreetName(et_streetname);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        newAssessmentHelpher.saveNewAssessmentNonTax(et_name.getText().toString(),
                et_mobileno.getText().toString(), et_emailid.getText().toString(), et_leasename.getText().toString(), et_doorno.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {

            View view = error.getView();

            String message = error.getCollatedErrorMessage(NewAssessment.this);

            //display the error message
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                view.requestFocus();

            } else {
                Toast.makeText(NewAssessment.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
