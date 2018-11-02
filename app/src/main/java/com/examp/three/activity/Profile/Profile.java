package com.examp.three.activity.Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examp.three.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.examp.three.HomeActivity;
import com.examp.three.common.SharedPreferenceHelper;

import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_address;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_city;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_contact;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_country;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_email;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_lastname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_pincode;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_state;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_title;

public class Profile extends AppCompatActivity implements Validator.ValidationListener{

    @Nullable
    @BindView(R.id.profile_toolbar) Toolbar profile_toolbar;
    @NotEmpty(message = "Title field Should not be Empty")
    @Nullable
    @BindView(R.id.et_title) EditText et_title;
    @NotEmpty(message = "First Name Should not be Empty")
    @Nullable
    @BindView(R.id.et_firstname) EditText et_firstname;
    @NotEmpty(message = "Last Name Should not be Empty")
    @Nullable
    @BindView(R.id.et_lastname) EditText et_lastname;
    @NotEmpty(message = "Email Id Should not be Empty")
    @Nullable
    @BindView(R.id.et_emailid) EditText et_emailid;
    @NotEmpty(message = "Contact No Should not be Empty")
    @Length(min = 10,max = 10,message = "Mobile number should be 10 digit")
    @Nullable
    @BindView(R.id.et_contactno) EditText et_contactno;
    @NotEmpty(message = "Address Should not be Empty")
    @Nullable
    @BindView(R.id.et_address_one) EditText et_address_one;
    @Nullable
    @BindView(R.id.et_address_two) EditText et_address_two;
    @Nullable
    @BindView(R.id.et_address_three) EditText et_address_three;
    @NotEmpty(message = "City Name Should not be Empty")
    @Nullable
    @BindView(R.id.et_cityname) EditText et_cityname;
    @NotEmpty(message = "State Name Should not be Empty")
    @Nullable
    @BindView(R.id.et_statename) EditText et_statename;
    @NotEmpty(message = "Country Should not be Empty")
    @Nullable
    @BindView(R.id.et_country) EditText et_country;
    @NotEmpty(message = "Country Should not be Empty")
    @Length(min = 6,max = 6,message = "Pincode should be Six Digit")
    @Nullable
    @BindView(R.id.et_pincode) EditText et_pincode;
    @Nullable
    @BindView(R.id.li_parent_lay) LinearLayout li_parent_lay;

    @Nullable
    @BindView(R.id.tv_update_emailid)TextView tv_update_emailid;

    @Nullable
    @BindView(R.id.et_otp_contactno) EditText et_otp_contactno;

    @Nullable
    @BindView(R.id.et_otp_emailid) EditText et_otp_emailid;

    @Nullable
    @BindView(R.id.ti_otp_contactno) TextInputLayout ti_otp_contactno;

    @Nullable
    @BindView(R.id.ti_otp_emailid) TextInputLayout ti_otp_emailid;

    @Nullable
    @BindView(R.id.tv_update_contactno)TextView tv_update_contactno;


    @Nullable
    @BindView(R.id.img_contact_verified)ImageView img_contact_verified;
    @Nullable
    @BindView(R.id.img_email_verified)ImageView img_email_verified;

String mTitle = "Mr.";
String mFirstName = "Vediyappan";
String mLastName="V";
String mEmailId="vedi115@gmail.com";
String mContactNo="9952102045";
String mAddress1="Pallathur Village";
String mAddress2="Uthangarai Tk,";
String mAddress3="Krishnagiri Dt,";
String mCityName="Krishnagiri Dt,";
String mPinCode="635307";
String mState="Tamil Nadu";
String mCountry="India";
Validator validator;
SharedPreferences mSharedPreferences;
    final String MyPREFERENCES = "User";
ProfileHelpher mProfileHelpher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(profile_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mSharedPreferences =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mTitle = mSharedPreferences.getString(pref_login_title, "");
        mFirstName = mSharedPreferences.getString(pref_login_firstname, "");
        mLastName = mSharedPreferences.getString(pref_login_lastname, "");
        mEmailId = mSharedPreferences.getString(pref_login_email, "");
        mContactNo = mSharedPreferences.getString(pref_login_contact, "");
        mAddress1 = mSharedPreferences.getString(pref_login_address, "");
        mCityName = mSharedPreferences.getString(pref_login_city, "");
        mPinCode = mSharedPreferences.getString(pref_login_pincode, "");
        mState = mSharedPreferences.getString(pref_login_state, "");
        mCountry = mSharedPreferences.getString(pref_login_country, "");
        mAddress2 = "";
        mAddress3 = "";

        mProfileHelpher = ProfileHelpher.getInstance(Profile.this);

        et_title.setText(mTitle);
        et_firstname.setText(mFirstName);
        et_lastname.setText(mLastName);
        et_emailid.setText(mEmailId);
        et_contactno.setText(mContactNo);
        et_address_one.setText(mAddress1);
        et_address_two.setText(mAddress2);
        et_address_three.setText(mAddress3);
        et_cityname.setText(mCityName);
        et_statename.setText(mState);
        et_country.setText(mCountry);
        et_pincode.setText(mPinCode);

        et_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileHelpher.getTitle(et_title);
            }
        });

        tv_update_emailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_emailid.getText().toString().isEmpty() && !et_emailid.getText().toString().equalsIgnoreCase("")){
                    if(mProfileHelpher.isValidEmail(et_emailid.getText().toString().trim())){
                        mProfileHelpher.sendOtp(et_emailid.getText().toString(),ti_otp_contactno);
                        ti_otp_emailid.setVisibility(View.VISIBLE);
                        ti_otp_contactno.setVisibility(View.GONE);
                    }else{
                        mProfileHelpher.showSnackbar("Invalid Email Id!",li_parent_lay);
                    }
                }
            }
        });
        tv_update_contactno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_contactno.getText().toString().isEmpty() && !et_contactno.getText().toString().equalsIgnoreCase("")){
                    if(mProfileHelpher.isValidMobileNumber(et_contactno.getText().toString().trim())){
                        mProfileHelpher.sendOtp(et_contactno.getText().toString(),ti_otp_contactno);
                        ti_otp_contactno.setVisibility(View.VISIBLE);
                        ti_otp_emailid.setVisibility(View.GONE);
                    }else{
                        mProfileHelpher.showSnackbar("Invalid Contact Number!",li_parent_lay);
                    }
                }
            }
        });

        et_otp_contactno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>=6){
                    if(s.toString().equalsIgnoreCase(mProfileHelpher.mGeneratedOtp)){
                        Log.e("success","succe==>ss");
                        tv_update_contactno.setVisibility(View.GONE);
                        ti_otp_contactno.setVisibility(View.GONE);
                        et_contactno.setClickable(false);
                        et_contactno.setFocusable(false);
                        et_contactno.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_tick_circle,0);
                    }else{
                        Log.e("actual value",mProfileHelpher.mGeneratedOtp);
                        Log.e("failed","failed==>ss");
                        mProfileHelpher.showSnackbar("Invalid Otp!",li_parent_lay);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("after text","Changed");
            }
        });
        et_statename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileHelpher.getState(et_statename);
            }
        });
        et_otp_emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>=6){
                    if(s.toString().equalsIgnoreCase(mProfileHelpher.mGeneratedOtp)){
                        Log.e("success","succe==>ss");
                        tv_update_emailid.setVisibility(View.GONE);
                        ti_otp_emailid.setVisibility(View.GONE);
                        et_emailid.setClickable(false);
                        et_emailid.setFocusable(false);
                        et_emailid.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_tick_circle,0);
                    }else{
                        Log.e("actual value",mProfileHelpher.mGeneratedOtp);
                        Log.e("failed","failed==>ss");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("after text","Changed");
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

        if(mProfileHelpher.checkAndRequestPermissions()){
            Toast.makeText(this, "gvbnm", Toast.LENGTH_SHORT).show();
        }

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                et_otp_contactno.setText(message);
               Log.e("messag= =>",message+" <====");
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profilemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.update){
            validator.validate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {

//        Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){

            View view = error.getView();

            String message = error.getCollatedErrorMessage(Profile.this);

            //display the error message
            if(view instanceof EditText){
                ((EditText) view).setError(message);
                view.requestFocus();

            }else{
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Snackbar.make(li_parent_lay,message,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
