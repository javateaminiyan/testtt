package com.examp.three.activity.Property;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.common.Common;
import com.examp.three.common.helper.CommonInterface;
import com.examp.three.common.helper.CommonMethods;
import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Panchayats;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_DISTRICT_DETAILS;
import static com.examp.three.common.Common.API_PROPERTYTAXCALCULATION;
import static com.examp.three.common.Common.API_PROPERTYTAXMASTERDETAILS;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYATID;
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class PropertyTaxCalculator_Activity extends AppCompatActivity implements CommonInterface{

    android.app.AlertDialog waitingDialog;
    public static String TAG = PropertyTaxCalculator_Activity.class.getName();
    LinearLayout ptc_ll_input, ptc_ll_btn_controls, ptc_ll_output;
    HashMap<Integer, String> districtHash = new HashMap<>();
    HashMap<String, String> buildingzoneHash = new HashMap<>();
    HashMap<String, String> buildingusageHash = new HashMap<>();
    HashMap<String, String> buildingtypeHash = new HashMap<>();

    ArrayList<String> distict_items = new ArrayList<>();
    ArrayList<String> panchayat_items = new ArrayList<>();

    ArrayList<String> buildinglocation_items = new ArrayList<>();
    ArrayList<String> buildingusage_items = new ArrayList<>();
    ArrayList<String> buildingtype_items = new ArrayList<>();

    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;
    SpinnerDialog spinnerDialogBuildinglocation;
    SpinnerDialog spinnerDialogBuildingusage;
    SpinnerDialog spinnerDialogBuildingType;
    LinearLayout rootlayout;
    TextInputLayout inputLayoutBuildingLocation, inputLayoutDistrict, inputLayoutPanchayat,
            inputLayoutBuildingUsage, inputLayoutBuildingType, inputLayoutTotalArea;

    EditText etBuildinglocation, etDistrict, etPanchayat, etBuildingUsage, etBuildingType, etTotalArea;

    TextView tvZone, tvBuildingUsage, tvTaxRate, tvMonthlyRental, tvAnnulRate, tvPlotValue, tvBuildingValue, tvMaintenaceOfBuilding,
            tvBasicValue, tvDiscountAge, tvDiscountBuildingType, tvAnnualVal, tvGeneralTax, tvLibraryCess, tvEducationTax,
            tvPropertyTax, tvTotalTax;
    String buildingZoneVal, buildingUsageVal, buildingTypeVal;
    Toolbar mtoolbar;

    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<District_Pojo> mDistrictPojoList = new ArrayList<District_Pojo>();
    private ArrayList<Panchayats> mPanchayatPojoList = new ArrayList<Panchayats>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    CommonMethods commonMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propertytax_calculator);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_calculator);

        ptc_ll_input = findViewById(R.id.ptc_ll_input);
        ptc_ll_btn_controls = findViewById(R.id.ptc_ll_btn_controls);
        ptc_ll_output = findViewById(R.id.ptc_ll_output);

        commonMethods = new CommonMethods(PropertyTaxCalculator_Activity.this, PropertyTaxCalculator_Activity.this);

        init();

        if (Common.isNetworkAvailable(PropertyTaxCalculator_Activity.this)) {
            commonMethods.getDistricts(rootlayout);
        } else {
            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }
        initializeDistrictPanchayat_SetText();
        initializeDPSpinnerOnClicks();

        if(!etDistrict.getText().toString().equalsIgnoreCase("")){
            getBuildingMaster(etDistrict.getText().toString(), etPanchayat.getText().toString());
        }
    }

    //This method is for generating the calculation
    public void generateCalculation(View v) {
        submit();
    }

    //This method is for initilizing the view in the layout activity
    private void init() {
        rootlayout = findViewById(R.id.rootlayout);
        inputLayoutDistrict = findViewById(R.id.input_layout_district);
        inputLayoutPanchayat = findViewById(R.id.input_layout_panchayat);
        inputLayoutBuildingLocation = findViewById(R.id.input_layout_building_location);
        inputLayoutBuildingUsage = findViewById(R.id.input_layout_buildingusage);
        inputLayoutBuildingType = findViewById(R.id.input_layout_building_type);
        inputLayoutTotalArea = findViewById(R.id.input_layout_totalarea);

        etDistrict = findViewById(R.id.input_district);
        etPanchayat = findViewById(R.id.input_panchayat);
        etBuildinglocation = findViewById(R.id.input_location);
        etBuildingUsage = findViewById(R.id.input_building_usage);
        etBuildingType = findViewById(R.id.input_buildingtype);
        etTotalArea = findViewById(R.id.input_totalarea);

        tvZone = findViewById(R.id.tvZone);
        tvBuildingUsage = findViewById(R.id.tvBuildingUsage);
        tvTaxRate = findViewById(R.id.tvTaxRate);
        tvMonthlyRental = findViewById(R.id.tvMonthlyRental);
        tvAnnulRate = findViewById(R.id.tvAnnualRental);
        tvPlotValue = findViewById(R.id.tvPlotValue);
        tvBuildingValue = findViewById(R.id.tvBuildingValue);
        tvMaintenaceOfBuilding = findViewById(R.id.tvMaintenanceBuilding);
        tvBasicValue = findViewById(R.id.tv_BasicRental);
        tvDiscountAge = findViewById(R.id.tvDiscountAge);
        tvDiscountBuildingType = findViewById(R.id.tvDiscountType);
        tvAnnualVal = findViewById(R.id.tvAnnualValue);
        tvGeneralTax = findViewById(R.id.tvGeneralTax);
        tvLibraryCess = findViewById(R.id.tvLibCess);
        tvEducationTax = findViewById(R.id.tvEducationTax);
        tvPropertyTax = findViewById(R.id.tvPropertytax);
        tvTotalTax = findViewById(R.id.tvTotalTax);

        etDistrict.addTextChangedListener(new MyTextWatcher(etDistrict));
        etPanchayat.addTextChangedListener(new MyTextWatcher(etPanchayat));
        etBuildinglocation.addTextChangedListener(new MyTextWatcher(etBuildinglocation));
        etBuildingUsage.addTextChangedListener(new MyTextWatcher(etBuildingUsage));
        etBuildingType.addTextChangedListener(new MyTextWatcher(etBuildingType));
        etTotalArea.addTextChangedListener(new MyTextWatcher(etTotalArea));

        spinnerDialogBuildinglocation = new SpinnerDialog(PropertyTaxCalculator_Activity.this, buildinglocation_items, "Select or Search Zone", "Close");// With No Animation


        spinnerDialogBuildingusage = new SpinnerDialog(PropertyTaxCalculator_Activity.this, buildingusage_items, "Select or Search Building Usage", "Close");// With No Animation


        spinnerDialogBuildingType = new SpinnerDialog(PropertyTaxCalculator_Activity.this, buildingtype_items, "Select or Search Building Type", "Close");// With No Animation

        spinnerDialogBuildinglocation.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {


                for (Map.Entry<String, String> entry : buildingzoneHash.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(s)) {
                        Log.e(TAG, "building locatiom " + entry.getKey());
                        buildingZoneVal = entry.getKey();
                        etBuildinglocation.setText(entry.getValue());
                    }
                }

            }
        });

        etBuildinglocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerDialogBuildinglocation.showSpinerDialog();
            }
        });

        spinnerDialogBuildingusage.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                for (Map.Entry<String, String> entry : buildingusageHash.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(s)) {
                        Log.e(TAG, "building locatiom " + entry.getKey());
                        buildingUsageVal = entry.getKey();
                        etBuildingUsage.setText(entry.getValue());
                    }
                }
            }
        });

        etBuildingUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerDialogBuildingusage.showSpinerDialog();
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

        etBuildingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerDialogBuildingType.showSpinerDialog();
            }
        });

    }

    //This method is for redirecting to validate method the district, panchayat, building location, usage,
    // type and total area
    private void submit() {
        if (!validateDistrict()) {
            return;
        }
        if (!validatePanchayat()) {
            return;
        }
        if (!validateBuildingLocation()) {
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

        getBuildingCalculate(buildingZoneVal, buildingUsageVal, buildingTypeVal, etTotalArea.getText().toString(), etDistrict.getText().toString(), etPanchayat.getText().toString());
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
                case R.id.input_location:
                    validateBuildingLocation();
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

    private boolean validateBuildingLocation() {
        if (etBuildinglocation.getText().toString().trim().isEmpty()) {
            inputLayoutBuildingLocation.setError(getString(R.string.err_msg_blocation));
            requestFocus(etBuildinglocation);
            return false;
        } else {
            inputLayoutBuildingLocation.setErrorEnabled(false);
        }

        return true;
    }

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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //This method is for showing the snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for getting building master
    private void getBuildingMaster(String district, String panchayat) {
        waitingDialog = new SpotsDialog(PropertyTaxCalculator_Activity.this);

        waitingDialog.show();
        waitingDialog.setCancelable(false);

        String REQUEST_TAG = "getPanchayat";

        String url = null;
        try {
            url = API_PROPERTYTAXMASTERDETAILS + "Type=Property&Input=&District=" + URLEncoder.encode(district, "utf-8") + "&Panchayat=" + URLEncoder.encode(panchayat, "utf-8");

            Log.e("apidddd",""+url);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        buildinglocation_items.clear();
        buildingusage_items.clear();
        buildingtype_items.clear();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
                                    Log.e(TAG, "hhhhhh" + entry.getKey() + "" + entry.getValue());
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

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    waitingDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());

                waitingDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    SnackShowTop("Time out", rootlayout);

                } else if (error instanceof AuthFailureError) {

                    SnackShowTop("Connection Time out", rootlayout);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", rootlayout);

                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rootlayout);

                } else if (error instanceof ParseError) {

                    SnackShowTop("Parse Error", rootlayout);

                } else {

                    SnackShowTop(error.getMessage(), rootlayout);

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest, REQUEST_TAG);
    }

    //This method is for retrieving the calculated building values
    private void getBuildingCalculate(String buildingLocation, final String buildingUsage, String buildingType, String TotalArea, String district, String panchayat) {
        waitingDialog = new SpotsDialog(PropertyTaxCalculator_Activity.this);

        waitingDialog.show();
        waitingDialog.setCancelable(false);

        String REQUEST_TAG = "getBuildingCalculate";

        String url = null;
        try {
            url = API_PROPERTYTAXCALCULATION + "BLocation=" + buildingLocation + "&BUsage=" + buildingUsage + "&BType=" + buildingType + "&TotalArea=" + TotalArea + "&District=" + URLEncoder.encode(district, "utf-8") + "&Panchayat=" + URLEncoder.encode(panchayat, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("iuiuiii" + url.toString());

        panchayat_items.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.length() > 0) {

                        JSONArray jsonArray = new JSONArray(response.getString("recordsets"));

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);

                                if (jsonArray1.length() > 0) {

                                    ptc_ll_input.setVisibility(View.GONE);
                                    ptc_ll_btn_controls.setVisibility(View.GONE);
                                    ptc_ll_output.setVisibility(View.VISIBLE);

                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray1.get(j);

                                        Double taxRate = jsonObject.getDouble("TaxRate");
                                        Double monthValue = jsonObject.getDouble("MonthValue");
                                        Double annualValue = jsonObject.getDouble("BuildingValue");
                                        Double plotValue = jsonObject.getDouble("PlotValue");
                                        Double buildingValue = jsonObject.getDouble("BuildingValue");
                                        Double buildMaintenance = jsonObject.getDouble("BuildMaintenance");
                                        Double basicValue = jsonObject.getDouble("BasicValue");
                                        Double ageDiscount = jsonObject.getDouble("AgeDiscount");
                                        Double usageDiscount = jsonObject.getDouble("UsageDiscount");
                                        Double annualTax = jsonObject.getDouble("AnnualTax");
                                        Double basicTax = jsonObject.getDouble("BasicTax");
                                        Double libraryTax = jsonObject.getDouble("LibraryTax");
                                        Double eductionTax = jsonObject.getDouble("EductionTax");
                                        Double propertyTax = jsonObject.getDouble("PropertyTax");

                                        tvZone.setText(buildingZoneVal);
                                        tvBuildingUsage.setText(etBuildingUsage.getText().toString());
                                        tvTaxRate.setText(String.valueOf(taxRate));
                                        tvMonthlyRental.setText(String.valueOf(monthValue));
                                        tvAnnulRate.setText(String.valueOf(annualTax));
                                        tvPlotValue.setText(String.valueOf(plotValue));
                                        tvBuildingValue.setText(String.valueOf(buildingValue));
                                        tvMaintenaceOfBuilding.setText(String.valueOf(buildMaintenance));
                                        tvBasicValue.setText(String.valueOf(basicValue));
                                        tvDiscountAge.setText(String.valueOf(ageDiscount));
                                        tvDiscountBuildingType.setText(String.valueOf(usageDiscount));
                                        tvAnnualVal.setText(String.valueOf(annualValue));
                                        tvGeneralTax.setText(String.valueOf(basicTax));
                                        tvLibraryCess.setText(String.valueOf(libraryTax));
                                        tvEducationTax.setText(String.valueOf(eductionTax));
                                        tvPropertyTax.setText(String.valueOf(propertyTax));
                                        tvTotalTax.setText(String.valueOf(propertyTax));

                                        System.out.println("gffffff" + eductionTax);

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "No data found ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        waitingDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    waitingDialog.dismiss();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());

                waitingDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    SnackShowTop("Time out", rootlayout);

                } else if (error instanceof AuthFailureError) {

                    SnackShowTop("Connection Time out", rootlayout);

                } else if (error instanceof ServerError) {

                    SnackShowTop("Could not connect server", rootlayout);

                } else if (error instanceof NetworkError) {

                    SnackShowTop("Please check the internet connection", rootlayout);

                } else if (error instanceof ParseError) {

                    SnackShowTop("Parse Error", rootlayout);

                } else {

                    SnackShowTop(error.getMessage(), rootlayout);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
    }

    //This method is for initializing district and panchayat from shared preferences
    public void  initializeDistrictPanchayat_SetText(){

        if (sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT) != null) {
            etDistrict.setText(sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT));
        } else {
            etDistrict.setText("Select District");
        }

        if (sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT) != null) {
            etPanchayat.setText(sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT));
        } else {
            etPanchayat.setText("Select Panchayat");
        }
    }

    //This method is for initiazing the district and panchayat spinners
    public void initializeDPSpinnerOnClicks()
    {
        spinnerDialogDistrict = new SpinnerDialog(PropertyTaxCalculator_Activity.this, mDistrictList, "Select District", "Close");// With No Animation

        spinnerDialogPanchayat = new SpinnerDialog(PropertyTaxCalculator_Activity.this, mPanchayatList, "Select Panchayat", "Close");// With No Animation

        etDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etPanchayat.setText(R.string.select_panchayat);
                spinnerDialogDistrict.showSpinerDialog();

            }
        });

        etPanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etDistrict.getText().toString().equalsIgnoreCase("Select District")) {
                    Snackbar.make(rootlayout, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                int districtId = sharedPreferenceHelpher.getInt(PREF_SELECTDISTRICTID);
                int panchayatId = sharedPreferenceHelpher.getInt(PREF_SELECTPANCHAYATID);
                commonMethods.getTownPanchayat(districtId, rootlayout);

                getBuildingMaster(etDistrict.getText().toString(), etPanchayat.getText().toString());

            }
        });

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                etDistrict.setText(item);

                int pDistrictId = mDistrictPojoList.get(position).getdID();

                sharedPreferenceHelpher.putString(PREF_SELECTDISTRICT, item);
                sharedPreferenceHelpher.putInt(PREF_SELECTDISTRICTID, pDistrictId);
                sharedPreferenceHelpher.putString(PREF_SELECTPANCHAYAT, null);
                sharedPreferenceHelpher.putString(PREF_SELECTPANCHAYATID, null);
            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {
                int panchayatId = mPanchayatPojoList.get(i).getPanchayatId();

                etPanchayat.setText(item);

                sharedPreferenceHelpher.putString(PREF_SELECTPANCHAYAT, item);
                sharedPreferenceHelpher.putInt(PREF_SELECTPANCHAYATID, panchayatId);

            }
        });
    }

    @Override
    public void getDistrict(ArrayList<District_Pojo> districtPojo, ArrayList<String> arrayList) {

        mDistrictPojoList.clear();
        mDistrictList.clear();

        mDistrictList.addAll(arrayList);
        mDistrictPojoList.addAll(districtPojo);
    }

    @Override
    public void getPanchayat(ArrayList<Panchayats> panchayatPojo, ArrayList<String> arrayList) {

        mPanchayatList.clear();
        mPanchayatPojoList.clear();

        mPanchayatList.addAll(arrayList);
        mPanchayatPojoList.addAll(panchayatPojo);

        if (mPanchayatList.size() > 0) {
            spinnerDialogPanchayat.showSpinerDialog();
        } else {
            showSnackbar(rootlayout, "No Panchayat Found !");
        }
    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
