package com.examp.three.fragment.Trade_NewAssessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.examp.three.R;
import com.examp.three.activity.NonTax.HelpherClasses.NewAssessmentHelpher;
import com.examp.three.activity.Trade.NewRegister_Activity;
import com.examp.three.common.Common;
import com.examp.three.common.DateSelect;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.TradeLicence.TL_ViewToComp_Pojo;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;


/**
 * Created by priyadharshini on 31/07/2018.
 */

public class LicenseRegistration_First_Fragment extends Fragment implements
        Validator.ValidationListener {
    public static final String TAG = LicenseRegistration_First_Fragment.class.getSimpleName();
    View v;
    Validator validator;
    boolean isvalidated;

    @NotEmpty(message = "Mobile Number is Required")
    @Order(1)
    @Nullable
    @Length(max = 10, min = 10, message = "Mobile Number length should be 10 digit")
    @BindView(R.id.et_mobileno)
    EditText et_mobileno;

    @NotEmpty(message = "Email ID is Required")
    @Order(2)
    @Nullable
    @Email
    @BindView(R.id.et_emailid)
    EditText et_emailid;

    @NotEmpty(message = "Applicant Initial is Required")
    @Order(3)
    @Nullable
    @BindView(R.id.et_applicant_initials)
    EditText et_applicant_initials;

    @NotEmpty(message = "Applicant Name is Required")
    @Order(4)
    @Nullable
    @BindView(R.id.et_applicant_name)
    EditText et_applicant_name;

    @NotEmpty(message = "F/M/H/G Initial is Required")
    @Order(5)
    @Nullable
    @BindView(R.id.et_fmhg_initials)
    EditText et_fmhg_initials;

    @NotEmpty(message = "F/M/H/G Name is Required")
    @Order(6)
    @Nullable
    @BindView(R.id.et_fmhg_name)
    EditText et_fmhg_name;

    @NotEmpty(message = "Door Number is Required")
    @Nullable
    @BindView(R.id.et_doorno)
    EditText et_doorno;

    @NotEmpty(message = "Street Name is Required")
    @Nullable
    @BindView(R.id.et_streetname)
    EditText et_streetname;

    @NotEmpty(message = "City Name is Required")
    @Nullable
    @BindView(R.id.et_cityname)
    EditText et_cityname;

    @NotEmpty(message = "District Name is Required")
    @Nullable
    @BindView(R.id.et_district)
    EditText et_district;

    @NotEmpty(message = "Gender is Required")
    @Nullable
    @BindView(R.id.et_gender)
    EditText et_gender;

    @NotEmpty(message = "Pin Code is Required")
    @Nullable
    @BindView(R.id.et_pincode)
    EditText et_pincode;

    @NotEmpty(message = "Aadhar Number is Required")
    @Nullable
    @BindView(R.id.et_aadharno)
    EditText et_aadharno;

    @Nullable
    @BindView(R.id.et_gst)
    EditText et_gst;

    @Nullable
    @BindView(R.id.btn_next)
    Button btn_next;

    //List
    ArrayList<String> mGenderListList = new ArrayList<>();

    //Spinner variables
    SpinnerDialog spinnerDialog;

    //String integertypes
    public String selectedGender;

    //Objects used
    DateSelect dateObject;
    SharedPreferenceHelper sharedPreferenceHelpher;
    NewAssessmentHelpher mNewAssessmentHelpher;

    public int districtid;
    String queryType, qType,login_userId;
    int sSno;

    Preference pref;
    @Nullable
    @BindView(R.id.parent_lay)ScrollView parent_lay;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_licenseregistration_first, container, false);
        ButterKnife.bind(this, v);

        pref = new Preference(getActivity());

        //object creation
        dateObject = new DateSelect(getActivity());
        mNewAssessmentHelpher = NewAssessmentHelpher.getInstance(getActivity());
        sharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());

        mGenderListList.add("Male");
        mGenderListList.add("Female");


        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");


        et_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSpinnerGender(mGenderListList);
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isNetworkAvailable(getActivity())){
                    validator.validate();
                }else {
                    Snackbar.make(parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
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


        if (!TextUtils.isEmpty(pojo.getQuerytype())) {
            queryType = pojo.getQuerytype();
        }
        if (pojo.getSno() != 0) {
            sSno = pojo.getSno();
        }
        if (!TextUtils.isEmpty(pojo.getsMobileNo())) {

            et_mobileno.setText(pojo.getsMobileNo());
        }

        if (!TextUtils.isEmpty(pojo.getsEmail())) {
            et_emailid.setText(pojo.getsEmail());
        }
        if (!TextUtils.isEmpty(pojo.getsApplicantSurName())) {
            et_applicant_initials.setText(pojo.getsApplicantSurName());
        }
        if (!TextUtils.isEmpty(pojo.getsApplicantFirstName())) {
            et_applicant_name.setText(pojo.getsApplicantFirstName());
        }
        if (!TextUtils.isEmpty(pojo.getsApGender())) {
            if (pojo.getsApGender().equalsIgnoreCase("M")) {
                et_gender.setText("Male");
                selectedGender = "M";
            } else {
                et_gender.setText("Female");
                selectedGender = "F";
            }
        }
        if (!TextUtils.isEmpty(pojo.getsApFGSurName())) {
            et_fmhg_initials.setText(pojo.getsApFGSurName());
        }
        if (!TextUtils.isEmpty(pojo.getApFGFirstName())) {
            et_fmhg_name.setText(pojo.getApFGFirstName());
        }
        if (!TextUtils.isEmpty(pojo.getApDoorNo())) {
            et_doorno.setText(pojo.getApDoorNo());
        }
        if (!TextUtils.isEmpty(pojo.getApStreet())) {
            et_streetname.setText(pojo.getApStreet());
        }
        if (!TextUtils.isEmpty(pojo.getApCity())) {
            et_cityname.setText(pojo.getApCity());
        }
        if (!TextUtils.isEmpty(pojo.getApDistrict())) {
            et_district.setText(pojo.getApDistrict());
        }
        if (!TextUtils.isEmpty(pojo.getApPIN())) {
            et_pincode.setText(pojo.getApPIN());
        }
        if (!TextUtils.isEmpty(pojo.getAadhar())) {
            et_aadharno.setText(pojo.getAadhar());
        }
        if (!TextUtils.isEmpty(pojo.getGst())) {
            et_gst.setText(pojo.getGst());
        }

    }

    public void setSpinnerGender(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select Gender", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                et_gender.setText(arrayList.get(i));
                if (arrayList.get(i).equalsIgnoreCase("Male")) {
                    selectedGender = "M";
                } else {
                    selectedGender = "F";
                }
            }
        });
    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        isvalidated = false;
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

    public String parseDate(String date, String givenformat, String resultformat) {

        String result = "";
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;

        try {
            sdf = new SimpleDateFormat(givenformat);
            sdf1 = new SimpleDateFormat(resultformat);
            result = sdf1.format(sdf.parse(date));

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            sdf = null;
            sdf1 = null;
        }
        return result;
    }


    @Override
    public void onValidationSucceeded() {

        sharedPreferenceHelpher.putApplicantDetails(et_mobileno.getText().toString(), et_emailid.getText().toString(), et_applicant_initials.getText().toString(),
                et_applicant_name.getText().toString(), selectedGender, et_fmhg_initials.getText().toString(), et_fmhg_name.getText().toString(),
                et_doorno.getText().toString(), et_streetname.getText().toString(), et_cityname.getText().toString(), et_district.getText().toString(), et_pincode.getText().toString(),
                et_aadharno.getText().toString(), et_gst.getText().toString());

        //save

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        if (queryType.equalsIgnoreCase("New")) {
            qType = "InsertApplicantAddr";
            sSno = 1;
        } else if (queryType.equalsIgnoreCase("Update")) {
            qType = "UpdateApplicantAddr ";
        }

        Call districtresult = retrofitInterface.saveTradeLicenseDetails(Common.ACCESS_TOKEN,
                qType,
                String.valueOf(sSno), "t1", "New", et_applicant_initials.getText().toString(),
                et_applicant_name.getText().toString(), selectedGender,
                et_fmhg_initials.getText().toString(), et_fmhg_name.getText().toString(),
                et_mobileno.getText().toString(),
                et_emailid.getText().toString(), et_doorno.getText().toString(),
                et_streetname.getText().toString(), et_cityname.getText().toString(),
                et_district.getText().toString(), et_pincode.getText().toString(),
                et_aadharno.getText().toString(), et_gst.getText().toString(), "",
                "", "", "",
                "", "", "", "",
                "", "0", "",
                "", "", "", "",
                "", "", "", "2018-2019",
                login_userId, login_userId, "2019-05-20", "2019-05-20",
                "", "", "", "", "Android");
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("url1---", response.raw().toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result", response1);

                try {
                    JSONObject records = new JSONObject(response1);
                    String message = records.getString("message");
                    if (message.startsWith("Success")) {
                        String sno[] = records.getString("message").split("~");
                        String mSno = sno[1];
                        Log.e("fvhbjdf", mSno);
                        sharedPreferenceHelpher.putSno(mSno);
                        int current = NewRegister_Activity.license_registration_pager.getCurrentItem();
                        NewRegister_Activity.license_registration_pager.setCurrentItem(current + 1);
                    } else {
                        Snackbar.make(parent_lay,message,Snackbar.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, t.toString());
            }
        });

    }

}
