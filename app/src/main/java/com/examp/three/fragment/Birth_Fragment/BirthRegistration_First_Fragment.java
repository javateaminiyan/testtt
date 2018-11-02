package com.examp.three.fragment.Birth_Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.examp.three.R;
import com.examp.three.activity.Birth_Modules.BirthRegistration_Activity;
import com.examp.three.common.Common;
import com.examp.three.common.DateSelect;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;


public class BirthRegistration_First_Fragment extends Fragment implements
        Validator.ValidationListener{

    @NotEmpty
    @Order(1)
    @Nullable
    @BindView(R.id.et_district) EditText et_district;
    @NotEmpty
    @Order(2)
    @Nullable
    @BindView(R.id.et_panchayat) EditText et_panchayat;
    @NotEmpty
    @Order(3)
    @Length(max = 10,min = 10, message ="Mobile Number length should be 10 digit")
    @Nullable
    @BindView(R.id.et_mobileno) EditText et_mobileno;
    @NotEmpty
    @Order(4)
    @Email
    @Nullable
    @BindView(R.id.et_emailid) EditText et_emailid;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_fathername) EditText et_fathername;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_mothername) EditText et_mothername;
    @Nullable
    @BindView(R.id.et_father_aadhar_no) EditText et_father_aadhar_no;
    @Nullable
    @BindView(R.id.et_mother_aadhar_no) EditText et_mother_aadhar_no;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_permanent_address) EditText et_permanent_address;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_pincode) EditText et_pincode;
    @Nullable
    @BindView(R.id.et_childname) EditText et_childname;
    @Nullable
    @BindView(R.id.et_per_permanent_address) EditText et_per_permanent_address;
    @Nullable
    @BindView(R.id.et_per_pincode) EditText et_per_pincode;
    @NotEmpty
    @Nullable
    @BindView(R.id.et_dob) EditText et_dob;
    @Checked(message = "Gender must be checked")
    @Nullable
    @BindView(R.id.rg_gender) RadioGroup rg_gender;
    @Nullable
    @BindView(R.id.rb_male) RadioButton rb_male;
    @Nullable
    @BindView(R.id.rb_female) RadioButton rb_female;
    @Nullable
    @BindView(R.id.btn_next) Button btn_next;
    @Nullable
    @BindView(R.id.check_above_address) CheckBox check_above_address;
    @Nullable
    @BindView(R.id.li_parent_lay) LinearLayout li_parent_lay;

    //List
    ArrayList<String> mDistrictsList = new ArrayList<>();
    ArrayList<String> mPanchayatsList = new ArrayList<>();

    List<com.examp.three.model.Birth_Death.Districts> mPanchayatList = new ArrayList<>();
    List<com.examp.three.model.Birth_Death.Districts> mDistrictList = new ArrayList();

    //Spinner variables
    SpinnerDialog spinnerDialog;

    //String integertypes
    public String district_Name;
    public String  panchayat_Name;
    public String selectedGender;

    //Objects used
    DateSelect dateObject;
    SharedPreferenceHelper sharedPreferenceHelpher;

    public int districtid;
    public int panchayatid;

    public static final  String TAG = BirthRegistration_Activity.class.getSimpleName();
    View v;
    Validator validator;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_birthregistration_first, container, false);
        ButterKnife.bind(this,v);

        //object creation
        dateObject = new DateSelect(getActivity());
        sharedPreferenceHelpher = SharedPreferenceHelper.getInstance(getActivity());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = BirthRegistration_Activity.birth_registration_pager.getCurrentItem();
                BirthRegistration_Activity.birth_registration_pager.setCurrentItem(current + 1);
            }
        });

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            et_district.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
        } else {
            et_district.setText("");
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty()) {
            et_panchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));
        }else{
            et_panchayat.setText("");
        }

        districtid = sharedPreference.getInt(PREF_SELECTDISTRICTID,0);

        et_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isNetworkAvailable(getActivity())){
                    getDistricts();
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        et_panchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(districtid!=0){
                    if(Common.isNetworkAvailable(getActivity())){
                        getPanchayat();
                        setSpinnerPanchayat(mPanchayatsList);
                    }else{
                        Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                    }


                }else{
                   Snackbar.make(li_parent_lay,"Please select the District!!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isNetworkAvailable(getActivity())){
                    validator.validate();
                }else{
                    Snackbar.make(li_parent_lay,"No Internet Connection!",Snackbar.LENGTH_SHORT).show();
                }

              /*  int current = BirthRegistration_Activity.birth_registration_pager.getCurrentItem();
                BirthRegistration_Activity.birth_registration_pager.setCurrentItem(current+1);*/
            }
        });
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateObject.getDate(et_dob);
            }
        });
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rb_male){
                    selectedGender = "M";
                }else if(i == R.id.rb_female){
                    selectedGender = "F";
                }
            }
        });
        check_above_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    et_per_permanent_address.setText(et_permanent_address.getText().toString());
                    et_per_pincode.setText(et_pincode.getText().toString());
                }else{
                    et_per_permanent_address.getText().clear();
                    et_pincode.getText().clear();
                }
            }
        });
        validator = new Validator(this);
        validator.setValidationListener(this);
        return v;
    }

    //This method is for retrieving list of districts
    public void getDistricts(){
        mDistrictsList.clear();
        mDistrictList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getDistricts(Common.ACCESS_TOKEN);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int districtid = jsonObject.getInt("DistrictId");
                            String districtname = jsonObject.getString("DistrictName");
                            Log.e(TAG,jsonObject.getString("DistrictName"));
                            mDistrictsList.add(districtname);
                            mDistrictList.add(new com.examp.three.model.Birth_Death.Districts(districtid,districtname));
                        }
                        pd.dismiss();
                        setSpinnerDistrict(mDistrictsList);
                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });

    }

    //This method is for setting spinner for districts
    public void setSpinnerDistrict(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select District", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                et_district.setText(arrayList.get(i));
                et_panchayat.getText().clear();
                Log.e("distt== ", "" + mDistrictList.get(i).getmDistrictName() + " -- " + mDistrictList.get(i).getmDistrictId());

                districtid = mDistrictList.get(i).getmDistrictId();
                district_Name = mDistrictList.get(i).getmDistrictName();

            }
        });
    }

    //This method is for setting spinner for panchayat
    public void setSpinnerPanchayat(final ArrayList<String> arrayList) {

        spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select Panchayat", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                et_panchayat.setText(arrayList.get(i));

                panchayatid = mPanchayatList.get(i).getmDistrictId();
                panchayat_Name = mPanchayatList.get(i).getmDistrictName();

                Log.e("panchayat ", "" + mPanchayatList.get(i).getmDistrictName() + " -- " + panchayatid);
            }
        });
    }

    //This method is for retrieving list of panchayats
    public void getPanchayat(){
        mPanchayatList.clear();
        mPanchayatsList.clear();
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Call districtresult = retrofitInterface.getPanchayat(Common.ACCESS_TOKEN,districtid);
        districtresult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("fgd",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("result",response1);
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int panchayatid = jsonObject.getInt("PanchayatId");
                            String panchayatName = jsonObject.getString("PanchayatName");
                            Log.e(TAG,jsonObject.getString("PanchayatName"));
                            mPanchayatsList.add(panchayatName);
                            mPanchayatList.add(new com.examp.three.model.Birth_Death.Districts(panchayatid,panchayatName));
                        }
                        pd.dismiss();
                    }else{
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.toString());
            }
        });
    }


    //This method is for validation succeed method
    @Override
    public void onValidationSucceeded() {
        sharedPreferenceHelpher.putParentalInfo(et_district.getText().toString(),
                                                et_panchayat.getText().toString(),
                                                et_mobileno.getText().toString(),
                                                et_emailid.getText().toString(),
                                                et_fathername.getText().toString(),
                                                et_mothername.getText().toString(),
                                                et_father_aadhar_no.getText().toString(),
                                                et_mother_aadhar_no.getText().toString(),
                                                et_permanent_address.getText().toString(),
                                                et_pincode.getText().toString(),
                                                et_per_permanent_address.getText().toString(),
                                                et_per_pincode.getText().toString(),
                                                parseDate(et_dob.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd"),
                                                selectedGender,
                                                et_childname.getText().toString()

                );

        int current = BirthRegistration_Activity.birth_registration_pager.getCurrentItem();
        BirthRegistration_Activity.birth_registration_pager.setCurrentItem(current+1);
    }

    //This method is for validation failed method
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){

            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());

            //display the error message
            if(view instanceof EditText){
                ((EditText) view).setError(message);
                view.requestFocus();

            }else{
                Snackbar.make(li_parent_lay,message,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    //This method is for parsing the date
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

}
