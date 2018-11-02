package com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.Property.PaidHistory;
import com.examp.three.activity.paymentgateway.utility.Params;
import com.examp.three.adapter.expandable_adapter.ExpandableListAdapter;
import com.examp.three.common.Common;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_contact;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_email;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_username;

public class MakePayment extends AppCompatActivity {

    private String district, panchayat;
    String assName;
    boolean  isDataPresent = false, isPanActive = false;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int groupPosition;
    String taxType, userId, taxNo, Message,userName;
    boolean isBullSector = false;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView, nExpandableListView;

    Button buttonPay;
    TextView tvHeading, tvNotFound;
    ImageView imgNoData;
    Toolbar mtoolbar;
    public static Typeface TamilFont;

    // Shared Preferences
    SharedPreferences preferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    String MyPREFERENCES = "User";
    String emailId,contactNo;

    private static String TAG=MakePayment.class.getSimpleName();

    PowerMenu powerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        initToolbar();

        tvHeading = (TextView) findViewById(R.id.heading);
        tvNotFound = (TextView) findViewById(R.id.noAssessmentFound);
        imgNoData = (ImageView) findViewById(R.id.imageView_nodata);
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        buttonPay = (Button) findViewById(R.id.buttonPay);

        TamilFont = Typeface.createFromAsset(getAssets(), "fonts/avvaiyar.ttf");

        preferences = getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = preferences.edit();

        userName = preferences.getString(pref_login_username, "");
        userId = preferences.getString(pref_login_userid, "");
        emailId = preferences.getString(pref_login_email, "");
        contactNo = preferences.getString(pref_login_contact, "");

        Intent i = getIntent();
        taxType = i.getStringExtra(Common.Type);

        powerMenu = new PowerMenu.Builder(getApplicationContext())
                .addItem(new PowerMenuItem("Add Assessment", false))
                .addItem(new PowerMenuItem("Delete Assessment", false))
                .addItem(new PowerMenuItem("Payment History", false))
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | RIGHT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(Color.WHITE)
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                .setSelectedMenuColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();

        if (Common.isNetworkAvailable(getApplicationContext())) {

            getParticularTaxType(taxType, userId);

            Log.e("tttt","---> "+taxType);
            Log.e("tttt","---> "+userId);

        } else {
            alertBox1("Please check your Internet connection");
        }
        expandDetails();

        tvNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (nExpandableListView.isGroupExpanded(groupPosition)) {
                        for (int j = 0; j < 3; j++) {
                            taxNo = nExpandableListView.getExpandableListAdapter().getChild(groupPosition, 0).toString();
                            panchayat = nExpandableListView.getExpandableListAdapter().getChild(groupPosition, 1).toString();
                            district = nExpandableListView.getExpandableListAdapter().getChild(groupPosition, 2).toString();

                            System.out.println("Tax Details = " + taxNo);
                            System.out.println("Panchayat Details = " + panchayat);
                            System.out.println("District Details = " + district);
                        }
                        taxNo = taxNo.substring(taxNo.indexOf(":") + 1, taxNo.length());
                        panchayat = panchayat.substring(panchayat.indexOf(":") + 1, panchayat.length());
                        district = district.substring(district.indexOf(":") + 1, district.length());
                        System.out.println("taxNo panchayat district = " + taxNo + panchayat + district);

                        if (Common.isNetworkAvailable(getApplicationContext())) {
                            if (taxType.equals("N")) {
                                alertBox("Non tax is under construction");
                            } else {
                                getBalanceDetails();
                            }
                        } else {
                            alertBox("Please check your internet connection");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Assessment",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please Select Assessment",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //This method is for on menu item click listener
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            powerMenu.setSelectedPosition(position); // change selected item
            if (item.getTitle().equalsIgnoreCase("Add Assessment")){
                Intent i = new Intent(MakePayment.this,AddAssessmentActivity.class);
                i.putExtra(Common.Type,taxType);
                startActivity(i);
                finish();
            }else if (item.getTitle().equalsIgnoreCase("Delete Assessment")){
                Intent i1 = new Intent(MakePayment.this,DeleteAssessmentActivity.class);
                i1.putExtra(Common.Type,taxType);
                startActivity(i1);
                finish();
            }else if (item.getTitle().equalsIgnoreCase("Payment History")){

                if (taxType.equalsIgnoreCase("P")){
                    Intent i1 = new Intent(MakePayment.this,PaidHistory.class);
                    i1.putExtra("Tax_Type", "Property");
                    startActivity(i1);
                    finish();
                }else {
                    Intent i1 = new Intent(MakePayment.this,Shared_PaymentHistory.class);
                    i1.putExtra("Tax_Type", taxType);
                    startActivity(i1);
                    finish();
                }
            }
            powerMenu.dismiss();
        }
    };

    //This method is for initializing toolbar
    private void initToolbar() {

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Assessement List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //This method is for getting particular tax type
    public void getParticularTaxType(String taxType, String userId) {
        String URL_GETTAX = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_PARTICULAR_TAX + "?" +
                "taxType=" + taxType + Common.PARAMETER_SEPERATOR_AMP + "userId=" + userId;

        System.out.println("111111111111111111111" + URL_GETTAX);

        final ProgressDialog dialog = new ProgressDialog(MakePayment.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_GETTAX, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                System.out.println("jsonArray = " + jsonArray.toString());
                try {
                    if (jsonArray.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Add Assessment", Toast.LENGTH_SHORT).show();
                        tvNotFound.setVisibility(View.VISIBLE);
                        imgNoData.setVisibility(View.VISIBLE);
                        tvHeading.setVisibility(View.INVISIBLE);
                    } else {
                        tvNotFound.setVisibility(View.INVISIBLE);
                        imgNoData.setVisibility(View.INVISIBLE);
                        tvHeading.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String district = jsonArray.getJSONObject(i).getString("district");
                            String panchayat = jsonArray.getJSONObject(i).getString("panchayat");
                            assName = jsonArray.getJSONObject(i).getString("assName");
                            String assNo = jsonArray.getJSONObject(i).getString("assNo");


                            List<String> restData = new ArrayList<>();
                            restData.add("AssessmentNo :" + assNo);
                            restData.add("Panchayat :" + panchayat);
                            restData.add("District :" + district);


                            listDataHeader.add(assNo + " , " + assName);
                            listDataChild.put(assNo + " , " + assName, restData);
                        }
                        listAdapter = new ExpandableListAdapter(MakePayment.this, listDataHeader, listDataChild);
                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            int pos = -1;

                            @Override
                            public void onGroupExpand(int i) {
                                if (i != pos) {
                                    expListView.collapseGroup(pos);
                                    pos = i;
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest, "Assesement list");
    }

    //This method is for expanding data from list view
    public void expandDetails() {
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                groupPosition = i;
                nExpandableListView = expandableListView;
                return false;
            }
        });
    }

    //This method is for getting balance details
    public void getBalanceDetails() {
        String URL_ASSESS_DETAILS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_BAL_BLOCK_DETAILS;

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(panchayat, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog dialog = new ProgressDialog(MakePayment.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);


        final JsonObjectRequest request = new JsonObjectRequest(URL_ASSESS_DETAILS + "?"
                + "district=" + district + "&" + "panchayat=" + tempPanchayat
                + Common.PARAMETER_SEPERATOR_AMP + "taxNo=" + taxNo + Common.PARAMETER_SEPERATOR_AMP
                + "taxType=" + taxType,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    isBullSector = jsonObject.getBoolean("bulsector");
                    isDataPresent = jsonObject.getBoolean("dataPresent");
                    Message = jsonObject.getString("message");

                    JSONArray blockList;// = new JSONArray();
                    blockList = jsonObject.getJSONArray("panchayatBlockList");
                    System.out.println("blockList = " + blockList);
                    for (int i = 0; i < blockList.length(); i++) {
                        if (panchayat.equals(blockList.get(i))) {
                            isPanActive = false;
                            break;
                        } else {
                            isPanActive = true;
                        }
                    }
                    if (isBullSector) {
                        alertBox("Assessment is in Court case or Government building");
                        isBullSector = false;
                    } else if (!isPanActive) {
                        alertBox("Mobile payment is not active for this panchayat");
                    } else if (isDataPresent) {

                        if (Message.equalsIgnoreCase("")) {
                            Intent intent = new Intent(getApplicationContext(), CheckDemandActivity.class);

                            intent.putExtra(Params.districtName, district);
                            intent.putExtra(Params.panchayatName, panchayat);
                            intent.putExtra(Params.taxType, taxType);
                            intent.putExtra(Params.assessmentNumber, taxNo);

                            intent.putExtra(Params.assessmentName, assName);
                            intent.putExtra(Params.userId, userId);
                            intent.putExtra(Params.emailId, emailId);
                            intent.putExtra(Params.contactNo, contactNo);

                            startActivity(intent);
                        } else {
                            alertBox(Message);

                        }
                    } else if (!isDataPresent) {
                        alertBox("No balance amount to be paid");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        AppSingleton.getInstance(MakePayment.this).addToRequestQueue(request,TAG);
    }

    //This method is for showing alert for response
    public void alertBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MakePayment.this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //This method is for showing alert for response
    public void alertBox1(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MakePayment.this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Common.isNetworkAvailable(getApplicationContext())) {
                            getParticularTaxType(taxType, userId);
                        } else {
                            alertBox1("Please check your Internet connection");
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onMenu(View view) {
        powerMenu.showAsDropDown(view,-170,0);
    }
}
