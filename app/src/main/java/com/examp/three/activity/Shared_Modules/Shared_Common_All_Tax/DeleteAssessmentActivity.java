package com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.examp.three.R;
import com.examp.three.adapter.expandable_adapter.ExpandableListAdapter;
import com.examp.three.common.Common;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class DeleteAssessmentActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView, nExpandableListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Button buttonDelete;
    int groupPosition;
    String district, panchayat, taxNo, taxtype, userId;

    TextView tvNotFound;
    ImageView imgNoData;

    // Shared Preferences
    SharedPreferences preferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    String MyPREFERENCES = "User";
    Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_assessment);
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);

        initToolbar();

        preferences = getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = preferences.edit();

        userId = preferences.getString(pref_login_userid, "");

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        tvNotFound = (TextView) findViewById(R.id.noAssessmentFound);

        imgNoData = (ImageView) findViewById(R.id.imageView_nodata);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(expListView.getVisibility() == View.VISIBLE) {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAssessmentActivity.this);
                            builder.setTitle("Delete");
                            builder.setMessage("Are you sure you want to delete?");
                            builder.setIcon(R.drawable.ic_delete);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (Common.isNetworkAvailable(DeleteAssessmentActivity.this)) {
                                                deleteParticularTaxType(district, panchayat, taxtype, taxNo, userId);
                                            } else {
                                                Toast.makeText(getApplicationContext(),
                                                        "Please check your Internet connection",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            builder.setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Assessment",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "No data found",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please Select Assessment",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent i = getIntent();
        taxtype = i.getStringExtra(Common.Type);
        if (taxtype != null) {
            switch (taxtype) {
                case "P":

                    if (Common.isNetworkAvailable(DeleteAssessmentActivity.this)) {
                        getParticularTaxType(taxtype, userId);
                    } else {
                        alertBox("Please check your Internet connection");
                    }
                    break;
                case "PR":

                    if (Common.isNetworkAvailable(DeleteAssessmentActivity.this)) {
                        getParticularTaxType(taxtype, userId);
                    } else {
                        alertBox("Please check your Internet connection");
                    }
                    break;
                case "W":

                    if (Common.isNetworkAvailable(DeleteAssessmentActivity.this)) {
                        getParticularTaxType(taxtype, userId);
                    } else {
                        alertBox("Please check your Internet connection");
                    }
                    break;
                case "N":
//                    position = 4;
                    break;
            }
        }
        expandDetails();

    }

    //This method is for getting particular tax type
    public void getParticularTaxType(String taxType, String userId) {
        String URL_GETTAX = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_GET_PARTICULAR_TAX + "?" +
                "taxType=" + taxType + Common.PARAMETER_SEPERATOR_AMP + "userId=" + userId;

        final ProgressDialog dialog = new ProgressDialog(DeleteAssessmentActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_GETTAX, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() == 0) {
                        expListView.setVisibility(View.GONE);
                        tvNotFound.setVisibility(View.VISIBLE);
                        imgNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNotFound.setVisibility(View.INVISIBLE);
                        imgNoData.setVisibility(View.INVISIBLE);
                        expListView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String district = jsonArray.getJSONObject(i).getString("district");
                            String panchayat = jsonArray.getJSONObject(i).getString("panchayat");
                            String assName = jsonArray.getJSONObject(i).getString("assName");
                            String assNo = jsonArray.getJSONObject(i).getString("assNo");

                            List<String> restData = new ArrayList<String>();
                            restData.add("AssessmentNo :" + assNo);
                            restData.add("Panchayat :" + panchayat);
                            restData.add("District :" + district);
                            System.out.println("AssName : " + assName);
                            listDataHeader.add(assNo + " , " + assName);
                            listDataChild.put(assNo + " , " + assName, restData);
                        }
                        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
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
        requestQueue.add(jsonArrayRequest);
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 25000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 25000;
            }

            @Override
            public void retry(VolleyError volleyError) throws VolleyError {
            }
        });
    }

    //This method is for initializing toolbar
    private void initToolbar() {
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Delete Assessment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //This method is for expanding list view
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

    //This method is for deleting the particular tax type
    public void deleteParticularTaxType(String district, String panchayat, String taxType, String taxNo, String userId) {

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(panchayat, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String URL_DELETETAX = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_DELETE_TAX_DETAILS + "?" +
                "district=" + district + "&" + "panchayat=" + tempPanchayat + "&" + "taxType=" + taxType + "&" +
                "taxNo=" + taxNo + "&" + "userId=" + userId;
        System.out.println("dddddddd"+URL_DELETETAX);

        final ProgressDialog dialog = new ProgressDialog(DeleteAssessmentActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_DELETETAX, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("jsonObject = " + jsonObject);
                try {
                    int responseCode = jsonObject.getInt("code");
                    if (responseCode > 0) {
                        listAdapter.removeView(groupPosition);
                        System.out.println("groupPosition = " + groupPosition);
                        listAdapter.notifyDataSetChanged();
                        try {
                            expListView.getItemAtPosition(1);
                        } catch (Exception e) {
                            expListView.setVisibility(View.GONE);
                            tvNotFound.setVisibility(View.VISIBLE);
                            imgNoData.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
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
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 25000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 25000;
            }

            @Override
            public void retry(VolleyError volleyError) throws VolleyError {
            }
        });
    }

    //This method is for showing alert
    public void alertBox(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeleteAssessmentActivity.this);
//        builder.setTitle("Registration successful");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Common.isNetworkAvailable(DeleteAssessmentActivity.this)) {
                            getParticularTaxType(taxtype, userId);
                        } else {
                            alertBox("Please check your Internet connection");
                        }
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
