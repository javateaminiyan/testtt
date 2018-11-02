package com.examp.three.fragment.Trade_NewAssessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.examp.three.R;
import com.examp.three.activity.Trade.Helpher.LicenseHelpher;
import com.examp.three.activity.Trade.NewRegister_Activity;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.TradeLicence.TL_ViewToComp_Pojo;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;
/**
 * Created by priyadharshini on 31/07/2018.
 */

public class LicenseRegistration_Second_Fragment extends Fragment implements Validator.ValidationListener {
    View v;
    Typeface mAvvaiyarfont;
    SpinnerDialog spinnerDialog;
    String mApiSno;

    public final String TAG = LicenseRegistration_Second_Fragment.class.getSimpleName();

    ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> mStreetBeansList = new ArrayList<>();

    //String Hospital list
    ArrayList<String> mWardStringList = new ArrayList<>();
    ArrayList<String> mStreetStringList = new ArrayList<>();


    @NotEmpty(message = "District Name is Required")
    @Nullable
    @BindView(R.id.et_district)
    EditText et_district;
    @NotEmpty(message = "Panchayat Name is Required")
    @Nullable
    @BindView(R.id.et_town_panchayat)
    EditText et_town_panchayat;
    @NotEmpty(message = "Door No is Required")
    @Nullable
    @BindView(R.id.et_doorno)
    EditText et_doorno;
    @NotEmpty(message = "Ward No is Required")
    @Nullable
    @BindView(R.id.et_ward_no)
    EditText et_ward_no;
    @NotEmpty(message = "Street Name is Required")
    @Nullable
    @BindView(R.id.et_street_name)
    EditText et_street_name;

    @Nullable
    @BindView(R.id.btn_next_second)
    Button btn_next_second;
    @Nullable
    @BindView(R.id.btn_back)
    Button btn_back;

    @NotEmpty(message = "License Year Range is Required")
    @Nullable
    @BindView(R.id.et_license_for)
    EditText et_license_for;

    @Nullable
    @BindView(R.id.li_parent_lay)
    LinearLayout li_parent_lay;

    //String declarations
    public String selected_district;
    public String selected_panchayat;
    public String selected_ward;
    public String validFromDate;
    public String validToDate;

    public String selected_street_code;

    //integer declarations
    public String street_code,login_userId;

    //Object creation
    SharedPreferenceHelper sharedPreferenceHelpher;
    LicenseHelpher mLicenseHelpher;
    StreetAdapter streetAdapter;
    Validator validator;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_licenseregistration_second, container, false);
        mAvvaiyarfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avvaiyar.ttf");
        ButterKnife.bind(this, v);
        onClicks();

        mLicenseHelpher = LicenseHelpher.getInstance(getActivity());

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        et_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isNetworkAvailable(getActivity())){
                    mLicenseHelpher.getDistricts(et_district);
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
        et_license_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isNetworkAvailable(getActivity())){
                    mLicenseHelpher.getLicenseFor(et_license_for);
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
        et_town_panchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isNetworkAvailable(getActivity())){
                    mLicenseHelpher.getPanchayat(et_town_panchayat);
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        et_ward_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_town_panchayat.getText().toString() != null && !et_town_panchayat.getText().toString().equalsIgnoreCase("")) {
                    if(Common.isNetworkAvailable(getActivity())){
                        mLicenseHelpher.getWardNo(et_ward_no);
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(li_parent_lay, "Please Select the Town Panchayat!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        et_street_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_ward_no.getText().toString() != null && !et_ward_no.getText().toString().equalsIgnoreCase("")) {
                    if(Common.isNetworkAvailable(getActivity())){
                        mLicenseHelpher.getStreetName(et_street_name);
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(li_parent_lay, "Please Select the Wardno!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            //Object initialization
            sharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());

            selected_district = sharedPreferenceHelpher.getSpecificValues("PREF_death_reg_District");
            selected_panchayat = sharedPreferenceHelpher.getSpecificValues("PREF_death_reg_Panchayat");
            mApiSno = sharedPreferenceHelpher.getSpecificValues("PREF_sno");

            Log.e("d", selected_district + " xf");
            Log.e("d", selected_panchayat + " xf");
            Log.e("d", mApiSno + " xf");
        }
    }

    public void onClicks() {

        btn_next_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isNetworkAvailable(getActivity())){
                    validator.validate();
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }

//                nextFragement();
            }


        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = NewRegister_Activity.license_registration_pager.getCurrentItem();
                NewRegister_Activity.license_registration_pager.setCurrentItem(current - 1);
            }
        });
    }

    @Override
    public void onValidationSucceeded() {

        sharedPreferenceHelpher.putTradeLocationDetails(et_district.getText().toString(), et_town_panchayat.getText().toString(),
                et_ward_no.getText().toString(), et_street_name.getText().toString(),
                et_doorno.getText().toString(), et_license_for.getText().toString());

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.saveTradeLicenseDetails(Common.ACCESS_TOKEN, "UpdateTradeLocation",
                mApiSno, "t2", "New", "",
                "", "", "", "", "",
                "", "", "", "", "", "",
                "", "", et_district.getText().toString(), et_town_panchayat.getText().toString(),
                et_ward_no.getText().toString(), et_street_name.getText().toString(),
                et_doorno.getText().toString(), mLicenseHelpher.mFinancialYear, "", "",
                "", "0", "",
                "", "", "", "", "0", "",
                "", "2018-2019",
                login_userId, login_userId, mLicenseHelpher.validFromDate, mLicenseHelpher.validToDate, "0", "0", "0",
                "0", "Android");

        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String response1 = new Gson().toJson(response.body());
                Log.e("urlll 2", "" + response.raw().toString());
                Log.e("result", response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    if (records.optString("message").contains("Success")) {
                        String sno[] = records.getString("message").split("~");
                        String mSno = sno[1];
                        Log.e("fvhbjdf", mSno);
                        sharedPreferenceHelpher.putSno(mSno);
                        int current = NewRegister_Activity.license_registration_pager.getCurrentItem();
                        NewRegister_Activity.license_registration_pager.setCurrentItem(current + 1);
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
        if (!TextUtils.isEmpty(pojo.getsDistrict())) {
            et_district.setText(pojo.getsDistrict());
            mLicenseHelpher.setDistrictId(pojo.getsDistrict());
            mLicenseHelpher.mselectedDistrict = pojo.getsDistrict();
        }
        if (!TextUtils.isEmpty(pojo.getsPanchayat())) {
            et_town_panchayat.setText(pojo.getsPanchayat());
            mLicenseHelpher.mselectedPanchayat = pojo.getsPanchayat();
        }
        if (!TextUtils.isEmpty(pojo.getsWardNo())) {
            et_ward_no.setText(pojo.getsWardNo());
            mLicenseHelpher.mselectedWard = pojo.getsWardNo();
        }
        if (!TextUtils.isEmpty(pojo.getsStreetName())) {
            et_street_name.setText(pojo.getsStreetName());
            et_street_name.setTypeface(mAvvaiyarfont);
            mLicenseHelpher.mselectedStreet = pojo.getsStreetName();
        }

        if (!TextUtils.isEmpty(pojo.getsDoorNo())) {
            et_doorno.setText(pojo.getsDoorNo());
        }

        if (!TextUtils.isEmpty(pojo.getLicenceYear())) {
            et_license_for.setText(pojo.getLicenceYear());
        }

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
