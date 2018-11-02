package com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.examp.three.common.Common;
import com.examp.three.common.helper.CommonInterface;
import com.examp.three.common.helper.CommonMethods;
import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Panchayats;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import static com.examp.three.common.Common.API_FinancialDates;
import static com.examp.three.common.Common.API_VIEW_DCB;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class ViewDcb extends AppCompatActivity implements CommonInterface{

    public static String TAG = ViewDcb.class.getSimpleName();
    Button btnSubmit;

    ArrayList<String> finyear_items = new ArrayList<>();

    String mIntent_Type;
    String Volley_Url;

    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;
    SpinnerDialog spinnerDialogFinYear;

    TextInputEditText etDistrict, etPanchayat, etFinYear;

    RelativeLayout rootlayout;
    android.app.AlertDialog waitingDialog;

    Toolbar mtoolbar;
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    Intent intent;

    CommonMethods commonMethods;

    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<District_Pojo> mDistrictPojoList = new ArrayList<District_Pojo>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dcb);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_viewdcb);
        getFinyear();

        intent = getIntent();
        etDistrict = findViewById(R.id.et_district);
        etPanchayat = findViewById(R.id.et_panchayat);

        rootlayout = findViewById(R.id.rootlayout);
        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreference.getString(PREF_SELECTDISTRICT, "").isEmpty())
            etDistrict.setText(sharedPreference.getString(PREF_SELECTDISTRICT, ""));

        if (!sharedPreference.getString(PREF_SELECTPANCHAYAT, "").isEmpty())
            etPanchayat.setText(sharedPreference.getString(PREF_SELECTPANCHAYAT, ""));


        etFinYear = findViewById(R.id.et_finyear);

        btnSubmit = findViewById(R.id.btn_submit);


        commonMethods = new CommonMethods(ViewDcb.this, ViewDcb.this);

        if (Common.isNetworkAvailable(ViewDcb.this)) {
            commonMethods.getDistricts(rootlayout);
        } else {
            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        initializeDistrictPanchayat_SetText();
        initializeDPSpinnerOnClicks();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isNetworkAvailable(getApplicationContext())) {


                    if (etDistrict.getText().toString().equals("")) {
                        Snackbar.make(rootlayout, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (etPanchayat.getText().toString().equals("")) {
                        Snackbar.make(rootlayout, R.string.snack_panchayat_search, Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (etFinYear.getText().toString().equals("")) {
                        Snackbar.make(rootlayout, R.string.snack_financial_search, Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (intent != null) {
                        mIntent_Type = intent.getStringExtra("Tax_Type");

                        switch (mIntent_Type) {
                            case "Property":
                                Volley_Url = API_VIEW_DCB + "qTaxType=Property&qDistrict=" + etDistrict.getText().toString().trim() + "&qPanchayat=" + etPanchayat.getText().toString().trim() + "&qFinYear=" + etFinYear.getText().toString().trim() + "";
                                openDownloadBrowser(ViewDcb.this, Volley_Url);

                                break;
                            case "Profession":

                                Volley_Url = API_VIEW_DCB + "qTaxType=Profession&qDistrict=" + etDistrict.getText().toString().trim() + "&qPanchayat=" + etPanchayat.getText().toString().trim() + "&qFinYear=" + etFinYear.getText().toString().trim() + "";
                                openDownloadBrowser(ViewDcb.this, Volley_Url);

                                break;
                            case "NonTax":
                                Volley_Url = API_VIEW_DCB + "qTaxType=NonTax&qDistrict=" + etDistrict.getText().toString().trim() + "&qPanchayat=" + etPanchayat.getText().toString().trim() + "&qFinYear=" + etFinYear.getText().toString().trim() + "";

                                openDownloadBrowser(ViewDcb.this, Volley_Url);

                                break;
                            case "Water":
                                Volley_Url = API_VIEW_DCB + "qTaxType=Water&qDistrict=" + etDistrict.getText().toString().trim() + "&qPanchayat=" + etPanchayat.getText().toString().trim() + "&qFinYear=" + etFinYear.getText().toString().trim() + "";

                                openDownloadBrowser(ViewDcb.this, Volley_Url);

                                break;
                        }

                    }
                } else {
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                }

            }
        });

        etFinYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogFinYear.showSpinerDialog();
            }
        });
    }

    //This method is for retrieving financial year
    private void getFinyear() {
        String REQUEST_TAG = "apigetFinYear";

        waitingDialog = new SpotsDialog(ViewDcb.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, API_FinancialDates, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.length() > 0) {

                        JSONArray jsonArray = new JSONArray(response.getString("recordsets"));

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);


                                if (jsonArray1.length() > 0) {
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray1.get(j);
                                        String FinYear = jsonObject.getString("FinYear");

                                        finyear_items.add(FinYear);
                                    }
                                }
                            }
                            etFinYear.setText(finyear_items.get(0));
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
                params.put("accesstoken", Common.ACCESS_TOKEN);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest, REQUEST_TAG);
    }

    //This method is for showing snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for opening receipt in browser
    public void openDownloadBrowser(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        if (i.resolveActivity(activity.getPackageManager()) == null) {
            i.setData(Uri.parse(url));
        }
        activity.startActivity(i);
    }

    //This method is for getting district and panchayat from shared preference
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

    //This method is for initializing district and panchayat spinners
    public void initializeDPSpinnerOnClicks(){
        spinnerDialogDistrict = new SpinnerDialog(ViewDcb.this, mDistrictList, "Select District", "Close");// With No Animation

        spinnerDialogPanchayat = new SpinnerDialog(ViewDcb.this, mPanchayatList, "Select Panchayat", "Close");// With No Animation

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
            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                etPanchayat.setText(item);

                sharedPreferenceHelpher.putString(PREF_SELECTPANCHAYAT, item);
            }
        });
    }

    //This method is for getting district from POJO class
    @Override
    public void getDistrict(ArrayList<District_Pojo> districtPojo, ArrayList<String> arrayList) {

        mDistrictPojoList.clear();
        mDistrictList.clear();

        mDistrictList.addAll(arrayList);
        mDistrictPojoList.addAll(districtPojo);
    }

    //This method is for getting panchayat from POJO class
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

