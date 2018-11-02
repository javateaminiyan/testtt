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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.activity.Trade.Helpher.LicenseHelpher;
import com.examp.three.activity.Trade.NewRegister_Activity;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;
/**
 * Created by priyadharshini on 31/07/2018.
 */

public class LicenseRegistration_Third_Fragment extends Fragment implements Validator.ValidationListener {
    public static final String TAG = LicenseRegistration_Third_Fragment.class.getSimpleName();
    @NotEmpty(message = "Licence Type  Required")
    @Nullable
    @BindView(R.id.et_licensetype)
    EditText et_licensetype;

    @NotEmpty(message = "Trade Name  Required")
    @Nullable
    @BindView(R.id.et_tradename)
    EditText et_tradename;

    @Nullable
    @BindView(R.id.tv_amount)
    TextView tv_amount;

    @NotEmpty(message = "Establishment Name  Required")
    @Nullable
    @BindView(R.id.et_establishmentname)
    EditText et_establishmentname;

    @Nullable
    @BindView(R.id.et_rental_paid)
    EditText et_rental_paid;

    @Nullable
    @BindView(R.id.et_no_machines)
    EditText et_no_machines;

    @Nullable
    @BindView(R.id.et_horsepower)
    EditText et_horsepower;

    @Nullable
    @BindView(R.id.tv_rental)
    TextView tv_rental;

    @Nullable
    @BindView(R.id.li_rental_layout)
    LinearLayout li_rental_layout;

    @Nullable
    @BindView(R.id.li_motors_details)
    LinearLayout li_motors_details;

    @Nullable
    @BindView(R.id.tv_own)
    TextView tv_own;

    @Nullable
    @BindView(R.id.check_motors_used)
    CheckBox check_motors_used;

    @Nullable
    @BindView(R.id.check_doc_noc)
    CheckBox check_doc_noc;

    @Nullable
    @BindView(R.id.check_rental_copy)
    CheckBox check_rental_copy;

    @Nullable
    @BindView(R.id.li_parent_lay)
    LinearLayout li_parent_lay;

    @Nullable
    @BindView(R.id.btn_next)
    Button btn_next;

    @Nullable
    @BindView(R.id.btn_back)
    Button btn_back;

    SharedPreferenceHelper sharedPreferenceHelpher;
    Validator validator;

    String login_userId;
    String mnumberOfMotorsInstalled = "0";
    String mRentalAgrStatus = "No";
    String mNOCStatus = "No";
    String mTotalHorsePower = "0";
    String mRentPaid = "0";

    LicenseHelpher mLicenseHelpher;

    View v;
    String mApiSno, prefRental, prefNoc, prefMotor;

    Typeface mAvvaiyarfont;

    @Inject
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_licenseregistration_third, container, false);

        mAvvaiyarfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avvaiyar.ttf");

        ButterKnife.bind(this, v);
        validator = new Validator(this);
        validator.setValidationListener(this);

        mLicenseHelpher = LicenseHelpher.getInstance(getActivity());

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_userId= sharedPreference.getString(pref_login_userid,"");

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isNetworkAvailable(getActivity())){
                    validator.validate();
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = NewRegister_Activity.license_registration_pager.getCurrentItem();
                NewRegister_Activity.license_registration_pager.setCurrentItem(current - 1);
            }
        });
        check_motors_used.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.isMotorInstalled = true;
                    li_motors_details.setVisibility(View.VISIBLE);
//                    sharedPreferenceHelpher.putMotorInstalled("Yes",getActivity());
                } else {
                    Common.isMotorInstalled = false;
                    li_motors_details.setVisibility(View.GONE);
//                    sharedPreferenceHelpher.putMotorInstalled("No",getActivity());
                }
            }
        });
        check_doc_noc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.mHaveNocDoc = true;
//                    sharedPreferenceHelpher.putNoc("Yes",getActivity());
                } else {
                    Common.mHaveNocDoc = false;
//                    sharedPreferenceHelpher.putNoc("No",getActivity());
                }
            }
        });
        check_rental_copy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.mHaveRentalCopy = true;
                } else {
                    Common.mHaveRentalCopy = false;
                }
            }
        });
        tv_rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_rental.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box_selected));
                tv_rental.setTextColor(getActivity().getResources().getColor(R.color.white));
                tv_own.setTextColor(getActivity().getResources().getColor(R.color.ownrentalcolor));
                tv_own.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box));
                Common.isRental = true;
                li_rental_layout.setVisibility(View.VISIBLE);

            }
        });
        tv_own.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.isRental = false;
                tv_own.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box_selected));
                tv_own.setTextColor(getActivity().getResources().getColor(R.color.white));
                tv_rental.setTextColor(getActivity().getResources().getColor(R.color.ownrentalcolor));
                tv_rental.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box));
                li_rental_layout.setVisibility(View.GONE);
                Common.mHaveRentalCopy = false;
                check_rental_copy.setChecked(false);
//                sharedPreferenceHelpher.putRental("No",getActivity());
            }
        });
        et_licensetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isNetworkAvailable(getActivity())){
                    mLicenseHelpher.getLicenceType(et_licensetype);
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        et_tradename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isNetworkAvailable(getActivity())){
                    mLicenseHelpher.getTradeName(et_tradename, tv_amount);
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
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

        if (!TextUtils.isEmpty(pojo.getLicenceTypeId())) {
            mLicenseHelpher.getLicenseName(pojo.getLicenceTypeId(), et_licensetype);
            mLicenseHelpher.mLicenceTypeId = Integer.parseInt(pojo.getLicenceTypeId());
        }

        if (!TextUtils.isEmpty(pojo.getRentalAgrStatus())) {

            if (pojo.getRentalAgrStatus().equalsIgnoreCase("Yes")) {
                check_rental_copy.setChecked(true);
                et_rental_paid.setText(pojo.getRentPaid());
                li_rental_layout.setVisibility(View.VISIBLE);

                tv_rental.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box_selected));
                tv_rental.setTextColor(getActivity().getResources().getColor(R.color.white));
                tv_own.setTextColor(getActivity().getResources().getColor(R.color.ownrentalcolor));
                tv_own.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box));
                Common.isRental = true;
                Common.mHaveRentalCopy = true;
            } else {
                check_rental_copy.setChecked(false);
                li_rental_layout.setVisibility(View.GONE);

                tv_own.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box_selected));
                tv_own.setTextColor(getActivity().getResources().getColor(R.color.white));
                tv_rental.setTextColor(getActivity().getResources().getColor(R.color.ownrentalcolor));
                tv_rental.setBackground(getActivity().getResources().getDrawable(R.drawable.ed_border_box));
                li_rental_layout.setVisibility(View.GONE);
                Common.mHaveRentalCopy = false;
            }
        }

        if (!TextUtils.isEmpty(pojo.getMotorInstalled())) {

            if (pojo.getMotorInstalled().equalsIgnoreCase("true")) {
                Common.isMotorInstalled = true;
                check_motors_used.setChecked(true);
                li_motors_details.setVisibility(View.VISIBLE);
                et_no_machines.setText(String.valueOf(pojo.getMotorTotalQty()));
                et_horsepower.setText(String.valueOf(pojo.getHorsePowerTotal()));
            } else {
                Common.isMotorInstalled = false;
                check_motors_used.setChecked(false);
                li_motors_details.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(pojo.getNOCStatus())) {

            if (pojo.getNOCStatus().equalsIgnoreCase("Yes")) {
                Common.mHaveNocDoc = true;
                check_doc_noc.setChecked(true);
            } else {
                Common.mHaveNocDoc = false;
                check_doc_noc.setChecked(false);
            }
        }

        if (!TextUtils.isEmpty(pojo.getTradeDescription())) {

            et_tradename.setText(pojo.getTradeDescription());
            et_tradename.setTypeface(mAvvaiyarfont);
        }

        if (!TextUtils.isEmpty(pojo.getEstablishmentName())) {
            et_establishmentname.setText(pojo.getEstablishmentName());
        }
        if (pojo.getTradeRate() != 0) {
            tv_amount.setText(String.valueOf(pojo.getTradeRate()) + ".00");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            //Object initialization
            sharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());
            mApiSno = sharedPreferenceHelpher.getSpecificValues("PREF_sno");
            Log.e("d", mApiSno + " xf");
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (Common.isMotorInstalled) {
            if (!et_horsepower.getText().toString().isEmpty() && !et_horsepower.getText().toString().equalsIgnoreCase(" ")) {
                if (!et_no_machines.getText().toString().isEmpty() && !et_no_machines.getText().toString().equalsIgnoreCase(" ")) {
                    mnumberOfMotorsInstalled = et_no_machines.getText().toString();
                    mTotalHorsePower = et_horsepower.getText().toString();
                } else {
                    Snackbar.make(li_parent_lay, "Please fill the Number of Machines involved", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(li_parent_lay, "Please fill the Total horse power", Snackbar.LENGTH_SHORT).show();
            }
            sharedPreferenceHelpher.putMotorInstalled("Yes");
        } else {
            sharedPreferenceHelpher.putMotorInstalled("No");
        }


        if (Common.isRental) {
            if (Common.mHaveRentalCopy) {
                mRentalAgrStatus = "Yes";
                mRentPaid = et_rental_paid.getText().toString();
            }
            sharedPreferenceHelpher.putRental("Yes");
        } else {
            sharedPreferenceHelpher.putRental("No");
            mRentalAgrStatus = "No";
        }


        if (Common.mHaveNocDoc) {
            mNOCStatus = "Yes";

            sharedPreferenceHelpher.putNoc("Yes");
        } else {
            sharedPreferenceHelpher.putNoc("No");
            mNOCStatus = "No";
        }

        sharedPreferenceHelpher.putUpdateTradeDetail(String.valueOf(mLicenseHelpher.mLicenceTypeId),
                mLicenseHelpher.mTradeName, mLicenseHelpher.mTradeCode, mLicenseHelpher.mTradeRate,
                et_establishmentname.getText().toString(),
                String.valueOf(Common.isMotorInstalled), mnumberOfMotorsInstalled, mTotalHorsePower,
                et_rental_paid.getText().toString(), mRentalAgrStatus, mNOCStatus,
                mLicenseHelpher.mFinancialYear,
                login_userId, login_userId);

        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.saveTradeLicenseDetails(Common.ACCESS_TOKEN, "UpdateTradeDetail",
                mApiSno, "t3", "New", "",
                "", "", "", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "",
                "", mLicenseHelpher.mFinancialYear, String.valueOf(mLicenseHelpher.mLicenceTypeId),
                mLicenseHelpher.mTradeName, mLicenseHelpher.mTradeCode, mLicenseHelpher.mTradeRate,
                et_establishmentname.getText().toString(),
                String.valueOf(Common.isMotorInstalled), mnumberOfMotorsInstalled, mTotalHorsePower,
                "", et_rental_paid.getText().toString(), mRentalAgrStatus, mNOCStatus,
                "2018-2019",
                login_userId, login_userId, mLicenseHelpher.validFromDate, mLicenseHelpher.validToDate, "0",
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
                        String sno[] = res.split("~");
                        String mSno = sno[1];
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
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {

            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());

            //display the error message
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                view.requestFocus();

            } else {
                Snackbar.make(li_parent_lay, message, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
