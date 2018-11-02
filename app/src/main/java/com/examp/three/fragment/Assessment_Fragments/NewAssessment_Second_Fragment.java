package com.examp.three.fragment.Assessment_Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.NewAssessment_Activity;
import com.examp.three.adapter.SharedAdapter.StreetAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.listener.RecyclerClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * import com.e
 * Created by priyadharshini on 31/07/2018.
 */

public class NewAssessment_Second_Fragment extends Fragment {

  String TAG= NewAssessment_Second_Fragment.class.getSimpleName();
    View v;
    Button btn_next;
    TextInputLayout inputLayoutBuildingNo, inputLayoutBuilingDate, inputLayoutBlockNo, inputLayoutWardNo, inputLayoutStreetName, inputLayoutBuildingZone,
            inputLayoutBuildingUsage, inputLayoutBuildingType, inputLayoutTotalArea;
    LinearLayout rootLayout;

    EditText etBlNo, etBlDate, etBlockNo, etWardNo, etStreetName, etBuildingZone, etBuildingUsage, etBuildingType, etTotalArea;

    String buildingZoneVal, buildingUsageVal, buildingTypeVal;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String prefername = "StepperPreference";

    HashMap<String, String> buildingzoneHash = new HashMap<>();
    HashMap<String, String> buildingusageHash = new HashMap<>();
    HashMap<String, String> buildingtypeHash = new HashMap<>();
    HashMap<String, String> wardHash = new HashMap<>();
    HashMap<String, String> streetHash = new HashMap<>();

    SpinnerDialog spinnerDialogBuildinglocation;
    SpinnerDialog spinnerDialogBuildingusage;
    SpinnerDialog spinnerDialogBuildingType;
    SpinnerDialog spinnerDialogWardNo;
    SpinnerDialog spinnerDialogStreetName;

    ArrayList<String> buildinglocation_items = new ArrayList<>();
    ArrayList<String> buildingusage_items = new ArrayList<>();
    ArrayList<String> buildingtype_items = new ArrayList<>();
    ArrayList<String> wardno_items = new ArrayList<>();
    ArrayList<String> streetName_items = new ArrayList<>();

    Calendar myCalendar = Calendar.getInstance();

    String districtName, panchayatName, mobileNo, emailId, streetCode;

    SharedPreferenceHelper stepperPreference;
    SpotsDialog spotsDialog;

    public String REQUEST_TAG = "Etown";
    String errorType,selected_StreetName;
    StreetAdapter streetAdapter;
    ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> arrayListStreet;
    ArrayList<String> sArrayList_StreetNames;
    Typeface typeTamil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_new_ass_second, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        stepperPreference = new SharedPreferenceHelper(getActivity());

        btn_next = (Button) v.findViewById(R.id.btn_next);
        arrayListStreet = new ArrayList<>();
        sArrayList_StreetNames = new ArrayList<>();

        //typeface
        typeTamil = ResourcesCompat.getFont(getActivity(), R.font.avvaiyar);

//        String value = getArguments().getString("panchayat");
//        Log.e(TAG,value);
        init(v);
        onClicks();
        onClickViews();

        return v;
    }

    //This method is for initializing the view in fragment
    private void init(View v) {

        inputLayoutBuildingNo = (TextInputLayout) v.findViewById(R.id.input_layout_blNo);
        inputLayoutBuilingDate = (TextInputLayout) v.findViewById(R.id.input_layout_blDate);
        inputLayoutBlockNo = (TextInputLayout) v.findViewById(R.id.input_layout_blockNo);
        inputLayoutWardNo = (TextInputLayout) v.findViewById(R.id.input_layout_wardNo);
        inputLayoutBuildingZone = (TextInputLayout) v.findViewById(R.id.input_layout_building_zone);
        inputLayoutBuildingUsage = (TextInputLayout) v.findViewById(R.id.input_layout_buildingusage);
        inputLayoutBuildingType = (TextInputLayout) v.findViewById(R.id.input_layout_building_type);
        inputLayoutTotalArea = (TextInputLayout) v.findViewById(R.id.input_layout_totalarea);
        inputLayoutStreetName = (TextInputLayout) v.findViewById(R.id.input_layout_streetName);


        rootLayout = (LinearLayout) v.findViewById(R.id.rootLinear);

        etBlNo = (EditText) v.findViewById(R.id.input_blNo);
        etBlDate = (EditText) v.findViewById(R.id.input_bldate);
        etBlockNo = (EditText) v.findViewById(R.id.input_blockNo);
        etWardNo = (EditText) v.findViewById(R.id.input_wardNo);
        etStreetName = (EditText) v.findViewById(R.id.input_streetName);
        etBuildingZone = (EditText) v.findViewById(R.id.input_building_zone);
        etBuildingUsage = (EditText) v.findViewById(R.id.input_building_usage);
        etBuildingType = (EditText) v.findViewById(R.id.input_buildingtype);
        etTotalArea = (EditText) v.findViewById(R.id.input_totalarea);


        etBlNo.addTextChangedListener(new MyTextWatcher(etBlNo));
        etBlDate.addTextChangedListener(new MyTextWatcher(etBlDate));
        etBlockNo.addTextChangedListener(new MyTextWatcher(etBlockNo));
        etWardNo.addTextChangedListener(new MyTextWatcher(etWardNo));
        etStreetName.addTextChangedListener(new MyTextWatcher(etStreetName));
        etBuildingZone.addTextChangedListener(new MyTextWatcher(etBuildingZone));
        etBuildingUsage.addTextChangedListener(new MyTextWatcher(etBuildingUsage));
        etBuildingType.addTextChangedListener(new MyTextWatcher(etBuildingType));
        etTotalArea.addTextChangedListener(new MyTextWatcher(etTotalArea));

        preferences = getActivity().getSharedPreferences(prefername, Context.MODE_PRIVATE);
        editor = preferences.edit();

          districtName = preferences.getString("pDistrict", "");
        panchayatName = preferences.getString("pPanchayat", "");
        mobileNo = preferences.getString("pMobileNo", "");
        emailId = preferences.getString("pEmailId", "");


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        etBlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (Common.isNetworkAvailable(getActivity())) {
            getBuildingMaster(districtName, panchayatName);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        spinnerDialogBuildinglocation = new SpinnerDialog(getActivity(), buildinglocation_items, "Select or Search Zone", "Close");// With No Animation
        spinnerDialogBuildingusage = new SpinnerDialog(getActivity(), buildingusage_items, "Select or Search Building Usage", "Close");// With No Animation
        spinnerDialogBuildingType = new SpinnerDialog(getActivity(), buildingtype_items, "Select or Search Building Type", "Close");// With No Animation
        spinnerDialogWardNo = new SpinnerDialog(getActivity(), wardno_items, "Select or Search WardNo", "Close");// With No Animation
        spinnerDialogStreetName = new SpinnerDialog(getActivity(), streetName_items, "Select or Search Street", "Close");// With No Animation

        spinnerDialogBuildinglocation.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                etBuildingZone.setText(s + "" + buildingzoneHash.size());


                for (Map.Entry<String, String> entry : buildingzoneHash.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(s)) {
//                        Log.e(TAG, "building locatiom " + entry.getKey());
                        buildingZoneVal = entry.getKey();
                        etBuildingZone.setText(entry.getValue());
                    }
                }

            }
        });


        spinnerDialogBuildingusage.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                for (Map.Entry<String, String> entry : buildingusageHash.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(s)) {
//                        Log.e(TAG, "building locatiom " + entry.getKey());
                        buildingUsageVal = entry.getKey();
                        etBuildingUsage.setText(entry.getValue());
                    }
                }
            }
        });


        spinnerDialogStreetName.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                for (Map.Entry<String, String> entry : streetHash.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(s)) {
                        Log.e("steerrrcode", " " + entry.getKey());
                        streetCode = entry.getKey();
                    }
                }
                etStreetName.setText(s);
                etStreetName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/avvaiyar.ttf"));
//                    }
//                }
            }
        });


        spinnerDialogWardNo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                etWardNo.setText(s);

            }
        });


        spinnerDialogBuildingType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                for (Map.Entry<String, String> entry : buildingtypeHash.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(s)) {
                        buildingTypeVal = entry.getKey();
                        etBuildingType.setText(entry.getValue());

                    }
                }

            }
        });


    }


    public void onClicks() {

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();

            }
        });

    }

    //This method is for updating the label
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etBlDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void submit() {

        if (!validateBlNo()) {
            return;
        }
        if (!validateBlDate()) {
            return;
        }
        if (!validateBlockNo()) {
            return;
        }
        if (!validateWardNo()) {
            return;
        }
        if (!validateStreetName()) {
            return;
        }
        if (!validateBuildingZone()) {
            return;
        }
        if (!validateBuildingUsage()) {
            return;
        }
        if (!validateBuildingType()) {
            return;
        }
        if (!validateTotalArea()) {
            return;
        }

        editor.putString("assFrag2", "1");
        editor.commit();

        Log.e("builffff", "" + buildingZoneVal);
        Log.e("builffff", "" + buildingTypeVal);
        Log.e("builffff", "" + buildingUsageVal);

        stepperPreference.stepperProperty_Two(etBlNo.getText().toString(),
                etBlDate.getText().toString(), etBlockNo.getText().toString(), etWardNo.getText().toString(),
                streetCode, selected_StreetName, buildingZoneVal,
                buildingUsageVal, buildingTypeVal, etTotalArea.getText().toString());



        if (!etBlNo.getText().toString().isEmpty() &&
                !etBlDate.getText().toString().isEmpty() &&
                !etBlockNo.getText().toString().isEmpty() &&
                !etWardNo.getText().toString().isEmpty() &&
                !etStreetName.getText().toString().isEmpty() &&
                !etBuildingZone.getText().toString().isEmpty() &&
                !etBuildingUsage.getText().toString().isEmpty() &&
                !etBuildingType.getText().toString().isEmpty() &&
                !etTotalArea.getText().toString().isEmpty()
                ) {
            int current = NewAssessment_Activity.pager.getCurrentItem();
            NewAssessment_Activity.pager.setCurrentItem(current + 1);
        }


    }

    //This method is for the text watcher
    public class MyTextWatcher implements TextWatcher {

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
                case R.id.input_blNo:
                    validateBlNo();
                    break;
                case R.id.input_bldate:
                    validateBlDate();
                    break;
                case R.id.input_blockNo:
                    validateBlockNo();
                    break;
                case R.id.input_wardNo:
                    validateWardNo();
                    break;
                case R.id.input_streetName:
                    validateStreetName();
                    break;
                case R.id.input_building_zone:
                    validateBuildingZone();
                    break;
                case R.id.input_building_usage:
                    validateBuildingUsage();
                    break;
                case R.id.input_buildingtype:
                    validateBuildingType();
                    break;
                case R.id.input_totalarea:
                    validateTotalArea();
                    break;
            }
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

    //This method is for validating BL number
    private boolean validateBlNo() {
        if (etBlNo.getText().toString().trim().isEmpty()) {
            inputLayoutBuildingNo.setError(getString(R.string.err_msg_blno));
            requestFocus(etBlNo);
            return false;
        } else {
            inputLayoutBuildingNo.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating bl date
    private boolean validateBlDate() {
        if (etBlDate.getText().toString().trim().isEmpty()) {
            inputLayoutBuilingDate.setError(getString(R.string.err_msg_bldate));
            requestFocus(etBlDate);
            return false;
        } else {
            inputLayoutBuilingDate.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating block no
    private boolean validateBlockNo() {
        if (etBlockNo.getText().toString().trim().isEmpty()) {
            inputLayoutBlockNo.setError(getString(R.string.err_msg_blockNo));
            requestFocus(etBlockNo);
            return false;
        } else {
            inputLayoutBlockNo.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating ward no
    private boolean validateWardNo() {
        if (etWardNo.getText().toString().trim().isEmpty()) {
            inputLayoutWardNo.setError(getString(R.string.err_msg_wardno));
            requestFocus(etWardNo);
            return false;
        } else {
            inputLayoutWardNo.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating street name
    private boolean validateStreetName() {
        if (etStreetName.getText().toString().trim().isEmpty()) {
            inputLayoutStreetName.setError(getString(R.string.err_msg_streetname));
            requestFocus(etStreetName);
            return false;
        } else {
            inputLayoutStreetName.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating building zone
    private boolean validateBuildingZone() {
        if (etBuildingZone.getText().toString().trim().isEmpty()) {
            inputLayoutBuildingZone.setError(getString(R.string.err_msg_buildingzone));
            requestFocus(etBuildingZone);
            return false;
        } else {
            inputLayoutBuildingZone.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating building usage
    private boolean validateBuildingUsage() {
        if (etBuildingUsage.getText().toString().trim().isEmpty()) {
            inputLayoutBuildingUsage.setError(getString(R.string.err_msg_buildingusage));
            requestFocus(etBuildingUsage);
            return false;
        } else {
            inputLayoutBuildingUsage.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating building type
    private boolean validateBuildingType() {
        if (etBuildingType.getText().toString().trim().isEmpty()) {
            inputLayoutBuildingType.setError(getString(R.string.err_msg_buildingtype));
            requestFocus(etBuildingType);
            return false;
        } else {
            inputLayoutBuildingType.setErrorEnabled(false);
        }

        return true;
    }

    //This method is for validating total area
    private boolean validateTotalArea() {
        if (etTotalArea.getText().toString().trim().isEmpty()) {
            inputLayoutTotalArea.setError(getString(R.string.err_msg_totalarea));
            requestFocus(etTotalArea);
            return false;
        } else {
            inputLayoutTotalArea.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //This method is for retrieving ward details
    private void getWardDetails(final String district, String panchayat) {

        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.show();

        String url = null;

        url = Common.API_TAX_MASTER_DETAILS + "Type=Ward&Input=&District=" + district + "&Panchayat=" + panchayat;
      Log.e(TAG,url);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                spotsDialog.dismiss();
                System.out.println("222222222" + response.toString());

                try {
                    if (response.length() > 0) {

                        JSONArray jsonArray = new JSONArray(response.getString("recordsets"));

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);


                                if (jsonArray1.length() > 0) {
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray1.get(j);

                                        String WardNo = jsonObject.getString("WardNo");
                                        wardno_items.add(WardNo);
                                    }

                                }
                                spinnerDialogWardNo.showSpinerDialog();
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }


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

    //This method is for retrieving street details
    public void getStreetDetails(String wardNo, String district, String panchayat) {

        arrayListStreet.clear();
        sArrayList_StreetNames.clear();

        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.show();

        RetrofitInterface apiInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        Call call = apiInterface.getWard_Street(Common.ACCESS_TOKEN, "Street", wardNo, district, panchayat);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {

                spotsDialog.dismiss();

                if (response.isSuccessful()) {


                    String sResponse = new Gson().toJson(response.body());

                    try {
                        JSONObject jsonObject = new JSONObject(sResponse);


                        if (jsonObject.length() > 0) {


                            if (jsonObject.has("recordsets") && !(jsonObject.isNull("recordsets"))) {
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("recordsets"));

                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);

                                        if (jsonArray1.length() > 0) {
                                            for (int j = 0; j < jsonArray1.length(); j++) {

                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                                String StreetNo = jsonObject1.getString("StreetNo");

                                                String StreetName = jsonObject1.getString("StreetName");

                                                arrayListStreet.add(new com.examp.three.model.Birth_Death.Street_Pojo(StreetName, StreetNo));

                                                sArrayList_StreetNames.add(arrayListStreet.get(j).getStreetName());



                                            }

                                            showStreetValuesInAlert(arrayListStreet);

                                        } else {
                                            Snackbar.make(rootLayout, "No data found !", Snackbar.LENGTH_SHORT).show();


                                        }
                                    }

                                }
                            } else {
                                Snackbar.make(rootLayout, "Something went Wrong", Snackbar.LENGTH_SHORT).show();

                            }


                        } else {
                            Snackbar.make(rootLayout, "No data found !", Snackbar.LENGTH_SHORT).show();


                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                } else {
                    Snackbar.make(rootLayout, "Something went wrong !", Snackbar.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                spotsDialog.dismiss();

                if (t instanceof IOException) {
                    errorType = "Timeout";
                    Snackbar.make(rootLayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();


                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    Snackbar.make(rootLayout, errorType, Snackbar.LENGTH_SHORT).show();


                } else {
                    errorType = "Error";
                    Snackbar.make(rootLayout, errorType, Snackbar.LENGTH_SHORT).show();

                }

            }
        });
    }

    //This method is for showing street values in alert
    public void showStreetValuesInAlert(final ArrayList<com.examp.three.model.Birth_Death.Street_Pojo> data_list) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View v2 = getLayoutInflater().inflate(R.layout.recycler_alert, null);


        mBuilder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        RecyclerView recyclerView = v2.findViewById(R.id.recycler);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v2.getContext());
        recyclerView.setLayoutManager(layoutManager);


        streetAdapter = new StreetAdapter(data_list);
        recyclerView.setAdapter(streetAdapter);
        streetAdapter.notifyDataSetChanged();

        mBuilder.setView(v2);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {
                etStreetName.setText(streetAdapter.getList().get(position).getStreetName());
//                selected_streetId = streetAdapter.getList().get(position).getStreetNo();
                etStreetName.setTypeface(typeTamil);
                selected_StreetName = streetAdapter.getList().get(position).getStreetName();
                dialog.dismiss();
            }
        }));
    }

    //This method is for retrieving values for building master
    private void getBuildingMaster(String district, String panchayat) {
        String url = null;
        try {
            url = "http://www.predemos.com/Etown/EPSTaxMasterDetails?Type=Property&Input=&District=" + URLEncoder.encode(district, "utf-8") + "&Panchayat=" + URLEncoder.encode(panchayat, "utf-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        buildinglocation_items.clear();
        buildingusage_items.clear();
        buildingtype_items.clear();


        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                spotsDialog.dismiss();

                try {
                    if (response.length() > 0) {

                        JSONArray jsonArray = new JSONArray(response.getString("recordsets"));

                        if (jsonArray.length() > 0) {
                            JSONArray jsonArray1 = (JSONArray) jsonArray.get(0);
                            JSONArray jsonArray2 = (JSONArray) jsonArray.get(1);
                            JSONArray jsonArray3 = (JSONArray) jsonArray.get(2);


                            if (jsonArray1.length() > 0) {
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray1.get(j);

                                    String ZoneCode = jsonObject.getString("ZoneCode");
                                    String ZoneName = jsonObject.getString("ZoneName");
                                    buildingzoneHash.put(ZoneCode, ZoneName);
                                    buildinglocation_items.add(ZoneName);
                                }

                                for (Map.Entry<String, String> entry : buildingzoneHash.entrySet()) {
//                                    Log.e(TAG,"hhhhhh"+entry.getKey()+""+entry.getValue());
                                }
                            }

                            if (jsonArray2.length() > 0) {
                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray2.get(j);

                                    String UsageCode = jsonObject.getString("UsageCode");
                                    String UsageName = jsonObject.getString("UsageName");
                                    buildingusageHash.put(UsageCode, UsageName);
                                    buildingusage_items.add(UsageName);
                                }
                            }


                            if (jsonArray3.length() > 0) {
                                for (int j = 0; j < jsonArray3.length(); j++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray3.get(j);
                                    String TypeCode = jsonObject.getString("TypeCode");
                                    String TypeName = jsonObject.getString("TypeName");
                                    buildingtypeHash.put(TypeCode, TypeName);
                                    buildingtype_items.add(TypeName);

                                }
                            }


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        etWardNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                if (Common.isNetworkAvailable(getActivity())) {

                    getWardDetails(districtName, panchayatName);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });


        etStreetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                if (Common.isNetworkAvailable(getActivity())) {


                    if (validateEdittext(etWardNo, inputLayoutWardNo, "Please Select Ward No")) {
                        getStreetDetails(etWardNo.getText().toString(), districtName, panchayatName);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });


        etBuildingZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                spinnerDialogBuildinglocation.showSpinerDialog();
            }
        });


        etBuildingUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                spinnerDialogBuildingusage.showSpinerDialog();
            }
        });


        etBuildingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                spinnerDialogBuildingType.showSpinerDialog();
            }
        });


    }

    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snack.show();
    }
}
