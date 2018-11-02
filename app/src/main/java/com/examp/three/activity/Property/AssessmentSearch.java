package com.examp.three.activity.Property;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.adapter.Property_Tax.Assessment_Search.Assessment_Search_Adapter;
import com.examp.three.common.Common;
import com.examp.three.common.helper.CommonInterface;
import com.examp.three.common.helper.CommonMethods;
import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Panchayats;
import com.examp.three.model.propertytax.AssessmentSearch_Pojo;
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
import static com.examp.three.common.Common.API_TAXBALANCEPAYMENTDETAILS;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class AssessmentSearch extends AppCompatActivity  implements CommonInterface {

    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    RelativeLayout rootlayout;
    TextInputEditText etDistrict, etPanchayat;

    public static String TAG = AssessmentSearch.class.getSimpleName();
    String name = "", doorNo = "", wardNo = "", streetName = "";

    Button submit;

    android.app.AlertDialog waitingDialog;
    EditText edTaxNo;
    TextView tvName, tvDoorNo, tvWardNo, tvStreetName;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";
    LinearLayout llBalance;

    ArrayList<AssessmentSearch_Pojo> assessment_search_bean = new ArrayList<>();

    private RecyclerView recyclerView;
    private Assessment_Search_Adapter assessmentAdapter;

    Toolbar mtoolbar;
    CommonMethods commonMethods;

    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<District_Pojo> mDistrictPojoList = new ArrayList<District_Pojo>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_assessment_search);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.property_searchassessment);

        initViews();

        commonMethods = new CommonMethods(AssessmentSearch.this, AssessmentSearch.this);

        if (Common.isNetworkAvailable(AssessmentSearch.this)) {
            commonMethods.getDistricts(rootlayout);
        } else {
            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        initializeDistrictPanchayat_SetText();
        initializeDPSpinnerOnClicks();

        if(!etPanchayat.getText().toString().equalsIgnoreCase("")) {
            edTaxNo.setEnabled(true);
        }

            sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty()) {
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));
            edTaxNo.setEnabled(true);
        } else {
            edTaxNo.setEnabled(false);
        }
        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));

        edTaxNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDistrict.length() == 0 && etPanchayat.length() == 0) {
                    Snackbar.make(rootlayout, "Select District and Panchayat", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideSoftKeyboard();

                if (Common.isNetworkAvailable(getApplicationContext())) {
                if (etDistrict.length() > 0) {

                    if (etPanchayat.length() > 0){

                        if (edTaxNo.length() > 0){
                                try {
                                    assessment_search_bean.clear();
                                    getAssessmentDetails();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                        }else {
                            Snackbar.make(rootlayout, "Please Enter Tax No", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Snackbar.make(rootlayout, "Please Select District", Snackbar.LENGTH_SHORT).show();
                }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //This method is for initializing the view in the layout activity
    private void initViews() {

        etDistrict = findViewById(R.id.et_district);
        etPanchayat = findViewById(R.id.et_panchayat);

        submit = findViewById(R.id.submit);

        edTaxNo = findViewById(R.id.tax_no);
        edTaxNo.setEnabled(false);
        tvName = findViewById(R.id.textview_user_name);
        tvDoorNo = findViewById(R.id.textview_user_doorno);
        tvWardNo = findViewById(R.id.textview_user_wardno);
        tvStreetName = findViewById(R.id.textview_user_streetno);
        rootlayout = findViewById(R.id.rootlayout);

        llBalance = findViewById(R.id.ll_balance);

        recyclerView = findViewById(R.id.recyclerview);

    }

    //This method is for getting the assessment details for the tax no
    public void getAssessmentDetails() throws UnsupportedEncodingException {

        waitingDialog = new SpotsDialog(AssessmentSearch.this);
        waitingDialog.show();
        String REQUEST_TAG = "getAssessmentDetails";

        String taxno = URLEncoder.encode(edTaxNo.getText().toString(), "UTF-8");
        String district = URLEncoder.encode(etDistrict.getText().toString(), "UTF-8");
        String panchayat = URLEncoder.encode(etPanchayat.getText().toString(), "UTF-8");

        String url = API_TAXBALANCEPAYMENTDETAILS + "Type=PSearch&TaxNo=" + taxno + "&FinYear=&" +
                "District=" + district + "&Panchayat=" + panchayat;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject recordset = new JSONObject(response.toString());

                            JSONArray firstarray = new JSONArray(recordset.getString("recordsets"));
                            if (!firstarray.equals("[[],[]]")) {

                                for (int i = 0; i < firstarray.length(); i++) {

                                    Log.e("ooofirstarray", "---" + firstarray.toString());

                                    JSONArray secondArray = (JSONArray) firstarray.get(0);

                                    if (secondArray.length() > 0) {
                                        for (int j = 0; j < secondArray.length(); j++) {

                                            JSONObject jsonObject = (JSONObject) secondArray.get(i);

                                            name = jsonObject.getString("Name");
                                            doorNo = jsonObject.getString("DoorNo");
                                            wardNo = jsonObject.getString("WardNo");
                                            streetName = jsonObject.getString("StreetName");

                                            tvName.setText(name);
                                            tvDoorNo.setText(doorNo);
                                            tvWardNo.setText(wardNo);
                                            tvStreetName.setText(streetName);

                                            llBalance.setVisibility(View.VISIBLE);

                                        }
                                    } else {

                                        Snackbar.make(rootlayout, "No Data Found ", Snackbar.LENGTH_SHORT).show();

                                    }

                                    JSONArray thirdArray = (JSONArray) firstarray.get(1);

                                    if (thirdArray.length() > 0) {

                                        for (int j = 0; j < thirdArray.length(); j++) {

                                            JSONObject jsonObjectthird = (JSONObject) thirdArray.get(j);

                                            String taxNo = jsonObjectthird.getString("TaxNo");
                                            String finYear = jsonObjectthird.getString("FinancialYear");
                                            int halfYear = jsonObjectthird.getInt("HalfYear");
                                            int balance = jsonObjectthird.getInt("Balance");

                                            assessment_search_bean.add(new AssessmentSearch_Pojo(taxNo, finYear, halfYear, balance));

                                        }
                                        balanceHistory();
                                    } else {

                                        Snackbar.make(rootlayout, "No Data Found ", Snackbar.LENGTH_SHORT).show();

                                        llBalance.setVisibility(View.GONE);
                                    }
                                }
                            }
                              else {
                                    Snackbar.make(rootlayout, "No Data Found ", Snackbar.LENGTH_SHORT).show();
                                    assessment_search_bean.clear();
                                    assessmentAdapter.notifyDataSetChanged();

                                    llBalance.setVisibility(View.GONE);
                                }
                            waitingDialog.dismiss();

                        } catch (JSONException e) {
                            waitingDialog.dismiss();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    waitingDialog.dismiss();

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

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, REQUEST_TAG);
    }

    //This method is for setting the balance history adapter
    public void balanceHistory() {

        assessmentAdapter = new Assessment_Search_Adapter(assessment_search_bean);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assessmentAdapter);
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

    //This method is for getting district and panchayat from shared preference and setting in their edittexts
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

    //This method is for initializing the district and panchayat spinners
    public void initializeDPSpinnerOnClicks()
    {
        spinnerDialogDistrict = new SpinnerDialog(AssessmentSearch.this, mDistrictList, "Select District", "Close");// With No Animation

        spinnerDialogPanchayat = new SpinnerDialog(AssessmentSearch.this, mPanchayatList, "Select Panchayat", "Close");// With No Animation

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
                commonMethods.getTownPanchayat(districtId, rootlayout);

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
                edTaxNo.setEnabled(false);
            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                etPanchayat.setText(item);

                sharedPreferenceHelpher.putString(PREF_SELECTPANCHAYAT, item);
                edTaxNo.setEnabled(true);
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
        mPanchayatList.addAll(arrayList);

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

