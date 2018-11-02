package com.examp.three.fragment.Assessment_Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.NewAssessment_Activity;
import com.examp.three.common.Common;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.stepper_bean.NewAssessmentFirstFragment_bean;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;

/**
 * Created by priyadharshini on 31/07/2018.
 */

public class NewAssessment_First_Fragment extends Fragment {
    View v;
    Button btn_next;
    TextInputLayout inputLayoutDistrict, inputLayoutPanchayat, inputLayoutName, inputLayoutMobileNo, inputLayoutEmailId;
    EditText etDistrict, etPanchayat, etName, etMobileNo, etEmailId;
    LinearLayout rootLayout;

    ArrayList<String> distict_items = new ArrayList<>();
    ArrayList<String> panchayat_items = new ArrayList<>();
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    //    sharedpreference
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String prefername = "pager";

    HashMap<Integer, String> districtHash = new HashMap<>();

    SharedPreferenceHelper stepperPreference;

    SpotsDialog spotsDialog;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_new_ass_first, container, false);

        btn_next = (Button) v.findViewById(R.id.btn_next);

        stepperPreference = new SharedPreferenceHelper(getActivity());

        init(v);
        onClicks();

        onClickViews();

        return v;
    }

    //This method is for initializing the view in the fragment activity
    public void init(View v) {

        inputLayoutDistrict = (TextInputLayout) v.findViewById(R.id.input_layout_district);
        inputLayoutPanchayat = (TextInputLayout) v.findViewById(R.id.input_layout_panchayat);
        inputLayoutName = (TextInputLayout) v.findViewById(R.id.input_layout_name);
        inputLayoutMobileNo = (TextInputLayout) v.findViewById(R.id.input_layout_mobileno);
        inputLayoutEmailId = (TextInputLayout) v.findViewById(R.id.input_layout_email);

        rootLayout = (LinearLayout) v.findViewById(R.id.rootLinear);

        etDistrict = (EditText) v.findViewById(R.id.input_district);
        etPanchayat = (EditText) v.findViewById(R.id.input_panchayat);
        etName = (EditText) v.findViewById(R.id.input_name);
        etMobileNo = (EditText) v.findViewById(R.id.input_mobileno);
        etEmailId = (EditText) v.findViewById(R.id.input_emailid);

        etDistrict.addTextChangedListener(new MyTextWatcher(etDistrict));
        etPanchayat.addTextChangedListener(new MyTextWatcher(etPanchayat));
        etName.addTextChangedListener(new MyTextWatcher(etName));
        etMobileNo.addTextChangedListener(new MyTextWatcher(etMobileNo));
        etEmailId.addTextChangedListener(new MyTextWatcher(etEmailId));

        preferences = getActivity().getSharedPreferences(prefername, Context.MODE_PRIVATE);
        editor = preferences.edit();

        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
        } else {
            etDistrict.setText("");
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty()) {
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));
        }else{
            etPanchayat.setText("");
        }
    }

    public void onClicks() {

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submit();

            }
        });

    }

    private void submit() {

        if (!validateDistrict()) {
            return;
        }
        if (!validatePanchayat()) {
            return;
        }
        if (!validateName()) {
            return;
        }
        if (!validateMobileNo()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        editor.putString("assFrag1", "1");
        editor.putString(Common.param_distrct, etDistrict.getText().toString());
        editor.putString(Common.param_panchayat, etPanchayat.getText().toString());
        editor.putString(Common.param_mobileNo, etMobileNo.getText().toString());
        editor.putString(Common.param_emailId, etEmailId.getText().toString());
        editor.commit();

        stepperPreference.stepperOne(etDistrict.getText().toString(),
                etPanchayat.getText().toString(),
                etName.getText().toString(),
                etMobileNo.getText().toString(),
                etEmailId.getText().toString());

        EventBus.getDefault().postSticky(new NewAssessmentFirstFragment_bean(etDistrict.getText().toString(),etPanchayat.getText().toString(),
                etName.getText().toString(),etMobileNo.getText().toString(),etEmailId.getText().toString()));

        if (!etDistrict.getText().toString().isEmpty() &&
                !etPanchayat.getText().toString().isEmpty() &&
                !etName.getText().toString().isEmpty() &&
                !etMobileNo.getText().toString().isEmpty() && !etEmailId.getText().toString().isEmpty()) {
            int current = NewAssessment_Activity.pager.getCurrentItem();
            NewAssessment_Activity.pager.setCurrentItem(current + 1);

        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_district:
                    validateDistrict();
                    break;
                case R.id.input_panchayat:
                    validatePanchayat();
                    break;
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_mobileno:
                    validateMobileNo();
                    break;
                case R.id.input_emailid:
                    validateEmail();
                    break;
            }
        }
    }

    //This method is for validating district
    private boolean validateDistrict() {
        if (etDistrict.getText().toString().trim().isEmpty()) {
            inputLayoutDistrict.setError(getString(R.string.err_msg_district));
            requestFocus(etDistrict);
            return false;
        } else {
            inputLayoutDistrict.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating mobile number
    private boolean validateMobileNo() {
        if (etMobileNo.getText().toString().trim().isEmpty()) {
            inputLayoutMobileNo.setError(getString(R.string.err_msg_mobileNo));
            requestFocus(etMobileNo);
            return false;
        } else if (etMobileNo.getText().toString().length() != 10) {
            inputLayoutMobileNo.setError(getString(R.string.err_msg_mobileNoLength));
            requestFocus(etMobileNo);
            return false;
        } else {
            inputLayoutMobileNo.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating panchayat
    private boolean validatePanchayat() {
        if (etPanchayat.getText().toString().trim().isEmpty()) {
            inputLayoutPanchayat.setError(getString(R.string.err_msg_panchayat));
            requestFocus(etPanchayat);
            return false;
        } else {
            inputLayoutPanchayat.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating name
    private boolean validateName() {
        if (etName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(etName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating email id
    private boolean validateEmail() {
        String email = etEmailId.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmailId.setError(getString(R.string.err_msg_email));
            requestFocus(etEmailId);
            return false;
        } else {
            inputLayoutEmailId.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //This method is for validating edittext
    private boolean validateEdittext(EditText et, TextInputLayout layout, String errorMessage) {
        if (et.getText().toString().trim().isEmpty()) {
            layout.setError(errorMessage);
            layout.setHintEnabled(false);
            requestFocus(et);
            return false;
        } else {
            layout.setErrorEnabled(false);
        }
        return true;
    }

    //This method is for retrieving the list of districts
    private void getDistrict() {

        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.show();
        spotsDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";


        String url = Common.API_DISTRICT_DETAILS;
        distict_items.clear();

        Log.e("rrrrrrrrr","rrr---->"+ url);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                spotsDialog.dismiss();
                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObject = (JSONObject) response.getJSONObject(i);

                        int districtId = jsonObject.getInt("DistrictId");
                        String districtName = jsonObject.getString("DistrictName");

                        districtHash.put(districtId, districtName);
                        distict_items.add(districtName);

                    }

                    setSpinner(distict_items);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spotsDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
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
                params.put("accesstoken", Common.ACCESS_TOKEN);
                return params;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest, REQUEST_TAG);

    }

    //This method is for retrieving the list of panchayats
    private void getPanchayat(int districtId) {
        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.show();
        String REQUEST_TAG = "apiPanchayat_Request";

        String url = Common.API_TOWNPANCHAYAT + "" + districtId;

        panchayat_items.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                spotsDialog.dismiss();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject panchaytOBJ = (JSONObject) response.getJSONObject(i);
                        int panchayatId = panchaytOBJ.getInt("PanchayatId");
                        String panchayatName = panchaytOBJ.getString("PanchayatName");

                        panchayat_items.add(panchayatName);
                    }

                    setSpinnerForPanchayat(panchayat_items);


                } catch (JSONException e) {
                    e.toString();
                }
                spotsDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
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
                params.put("accesstoken", Common.ACCESS_TOKEN);
                return params;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest, REQUEST_TAG);

    }

    public void onClickViews() {
        etDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getActivity())) {

//                    spinnerDialogDistrict.showSpinerDialog();
                    getDistrict();

                    etPanchayat.setText("");
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        etPanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getActivity())) {
                    if (validateEdittext(etDistrict, inputLayoutDistrict, "Please Select District")) {
                        for (Map.Entry<Integer, String> entry : districtHash.entrySet()) {
                            if (entry.getValue().equalsIgnoreCase(etDistrict.getText().toString()))
                                getPanchayat(entry.getKey());

                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Select District !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snack.show();
    }

    //This method is for setting the spinner for district
    public void setSpinner(ArrayList<String> arrayList) {

        spinnerDialogDistrict = new SpinnerDialog(getActivity(), arrayList, "Select or Search District", "Close");// With No Animation

        spinnerDialogDistrict.showSpinerDialog();

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                etDistrict.setText(item);
            }
        });
    }

    //This method is for setting the spinner for panchayat list
    public void setSpinnerForPanchayat(ArrayList<String> arrayList) {

        spinnerDialogPanchayat = new SpinnerDialog(getActivity(), arrayList, "Select or Search Panchayat", "Close");// With No Animation

        spinnerDialogPanchayat.showSpinerDialog();

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                etPanchayat.setText(s);

            }
        });

    }

}
