package com.examp.three.activity.Property;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
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
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.common.helper.CommonInterface;
import com.examp.three.common.helper.CommonMethods;
import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Birth_Death.Districts;
import com.examp.three.model.Panchayats;
import com.examp.three.model.Result;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import dmax.dialog.SpotsDialog;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_DISTRICT_DETAILS;
import static com.examp.three.common.Common.API_SAVENAMETRANSFER;
import static com.examp.three.common.Common.API_TOWNPANCHAYAT;
import static com.examp.three.common.Common.GET_ASSESSMENT_PROP;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYATID;
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class NameTransfer extends AppCompatActivity implements CommonInterface {

    //Views
    TextInputEditText etDistrict, etPanchayat;
    TextView etChoosenFileOne, etChoosenFileTwo, etChoosenFileThree;
    EditText edTaxNo, etMobileNo, etEmailId, etTransferName;
    Button submit, buttonSubmitNameTransfer;
    TextView tvName, tvWardNo, tvDoorNo, tvStreetName, tvUploadOne, tvUploadTwo, tvUploadThree;
    LinearLayout llAssessmentDetails, llNameTransfer;
    RelativeLayout rlRoot;

    //ArrayList
    ArrayList<String> distict_items = new ArrayList<>();
    ArrayList<String> panchayat_items = new ArrayList<>();

    ArrayList<Districts> districts = new ArrayList<>();
    ArrayList<Panchayats> panchayats = new ArrayList<>();

    //Strings
    String name = "", doorNo = "", wardNo = "", streetName = "", TAG = "NameTransfer--> ",
            fileString = "", requestNo = "", uploadType = "";

    //Integers
    int districtId, panchayatId, MAX_ATTACHMENT_COUNT = 10;

    //Spinners
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    //ProgressDialog
    SpotsDialog progressDialog;

    //Toolbar
    Toolbar mtoolbar;

    File file;
    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<District_Pojo> mDistrictPojoList = new ArrayList<District_Pojo>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_transfer);

        //Toolbar
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.property_name_transfer);

        //TextInputEditTexr
        etDistrict = (TextInputEditText) findViewById(R.id.et_district);
        etPanchayat = (TextInputEditText) findViewById(R.id.et_panchayat);
        etChoosenFileOne = (TextView) findViewById(R.id.ed_choosen_file_one);
        etChoosenFileTwo = (TextView) findViewById(R.id.ed_choosen_file_two);
        etChoosenFileThree = (TextView) findViewById(R.id.tv_choosen_file);

        //EditText
        edTaxNo = (EditText) findViewById(R.id.ed_taxno);
        etMobileNo = (EditText) findViewById(R.id.et_mobile_no);
        etEmailId = (EditText) findViewById(R.id.et_email_id);
        etTransferName = (EditText) findViewById(R.id.et_transfer_name);

        //Button
        submit = (Button) findViewById(R.id.button_submit_name_transfer);
        buttonSubmitNameTransfer = (Button) findViewById(R.id.submit_nametransfer);

        //TextView
        tvName = (TextView) findViewById(R.id.tv_name);
        tvWardNo = (TextView) findViewById(R.id.tv_wardno);
        tvDoorNo = (TextView) findViewById(R.id.tv_doorno);
        tvStreetName = (TextView) findViewById(R.id.tv_streetname);
        tvUploadOne = (TextView) findViewById(R.id.tv_upload_one);
        tvUploadTwo = (TextView) findViewById(R.id.tv_upload_two);
        tvUploadThree = (TextView) findViewById(R.id.tv_upload_three);

        //LinearLayout
        llAssessmentDetails = (LinearLayout) findViewById(R.id.ll_assesmentdetails);
        llNameTransfer = (LinearLayout) findViewById(R.id.ll_nametransfer);

        //RelativeLayout
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        requestNo = generateRandom();

        commonMethods = new CommonMethods(NameTransfer.this, NameTransfer.this);

        if (Common.isNetworkAvailable(NameTransfer.this)) {
            commonMethods.getDistricts(rlRoot);
        } else {
            Snackbar.make(rlRoot, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }
        initializeDistrictPanchayat_SetText();
        initializeDPSpinnerOnClicks();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        edTaxNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDistrict.getText().toString().isEmpty() && etPanchayat.getText().toString().isEmpty()) {
                    Snackbar.make(rlRoot, "Select District and Panchayat", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideSoftKeyboard();

                if (!etDistrict.getText().toString().isEmpty()) {

                    if(!etPanchayat.getText().toString().isEmpty()){

                        if (!edTaxNo.getText().toString().isEmpty()){
                            getAssessmentDetails(edTaxNo.getText().toString(), etDistrict.getText().toString(), etPanchayat.getText().toString());
                        }else {
                            Snackbar.make(rlRoot, "Enter Tax No", Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(rlRoot, "Please Select Panchayat", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(rlRoot, "Please Select District", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        etChoosenFileOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileString = "registrationDocument";
                showFileChooser();

            }
        });

        etChoosenFileTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileString = "legalDocument";
                showFileChooser();

            }
        });

        etChoosenFileThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileString = "deathCertificate";
                showFileChooser();
            }
        });

        tvUploadOne.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                uploadType = "RegDocument";

                if (etChoosenFileOne.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rlRoot, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rlRoot, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });

        tvUploadTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadType = "NameDocument";

                if (etChoosenFileTwo.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rlRoot, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rlRoot, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvUploadThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadType = "DeathDocument";

                if (etChoosenFileThree.getText().toString().equalsIgnoreCase("Choose File")) {

                    Snackbar.make(rlRoot, "Choose File First  !", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        uploadImage(file);
                    } else {

                        Snackbar.make(rlRoot, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonSubmitNameTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!etMobileNo.getText().toString().isEmpty() && !etEmailId.getText().toString().isEmpty() &&
                        !etTransferName.getText().toString().isEmpty() && !etChoosenFileOne.getText().toString().isEmpty()) {

                    if (etChoosenFileOne.getText().toString().isEmpty()) {

                        Snackbar.make(rlRoot, "Upload Mandatory Document", Snackbar.LENGTH_SHORT).show();

                    } else {

                        saveNameTransferDetails(requestNo, etDistrict.getText().toString(), etPanchayat.getText().toString(),
                                etMobileNo.getText().toString(), etEmailId.getText().toString(), edTaxNo.getText().toString(),
                                tvName.getText().toString(), tvDoorNo.getText().toString(), tvWardNo.getText().toString(), streetName,
                                name);
                    }

                } else {
                    Snackbar.make(rlRoot, "Enter Mobile Number, Email Id and Transfer Name", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //This Method for saving name transfer details
    public void saveNameTransferDetails(String requestNo, String districtName, String panchayatName, String mobNo, String emailId,
                                        String taxNo, String name, String doorNo, String wardNo, String streetName,
                                        String transferName) {

        progressDialog = new SpotsDialog(NameTransfer.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        try {

            requestNo = URLEncoder.encode(requestNo, "UTF-8");
            districtName = URLEncoder.encode(districtName, "UTF-8");
            panchayatName = URLEncoder.encode(panchayatName, "UTF-8");
            mobNo = URLEncoder.encode(mobNo, "UTF-8");
            emailId = URLEncoder.encode(emailId, "UTF-8");
            taxNo = URLEncoder.encode(taxNo, "UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
            doorNo = URLEncoder.encode(doorNo, "UTF-8");
            wardNo = URLEncoder.encode(wardNo, "UTF-8");
            streetName = URLEncoder.encode(streetName, "UTF-8");
            transferName = URLEncoder.encode(transferName, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = API_SAVENAMETRANSFER + requestNo + "&District=" + districtName + "&Panchayat=" + panchayatName + "&" +
                "MobileNo=" + mobNo + "&EmailId=" + emailId + "&TaxNo=" + taxNo + "&Name=" + name + "&DoorNo=" + doorNo + "&WardNo=" + wardNo + "&" +
                "StreetName=" + streetName + "&TransferName=" + transferName + "&EntryType=Android";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject recordset = new JSONObject(response.toString());

                            String message = recordset.getString("message");

                            String[] messageResponse = message.split("~");

                            if (messageResponse[0].equalsIgnoreCase("Success")) {

                                Snackbar.make(rlRoot, "Saved Successfully ", Snackbar.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    SnackShowTop("Time out", rlRoot);
                } else if (error instanceof AuthFailureError) {
                    SnackShowTop("Connection Time out", rlRoot);
                } else if (error instanceof ServerError) {
                    SnackShowTop("Could not connect server", rlRoot);
                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rlRoot);
                } else if (error instanceof ParseError) {
                    SnackShowTop("Parse Error", rlRoot);
                } else {
                    SnackShowTop(error.getMessage(), rlRoot);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, REQUEST_TAG);

    }

    //This Method is for getting assessment details for particular tax number
    public void getAssessmentDetails(String taxNo, String districtName, String panchayatName) {

        progressDialog = new SpotsDialog(NameTransfer.this);
        progressDialog.show();

        String REQUEST_TAG = "townPanchayat";

        String url = GET_ASSESSMENT_PROP + taxNo + "&FinYear=&District=" + districtName + "&Panchayat=" + panchayatName;

        Log.e("errrrr","eeee"+url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject recordset = new JSONObject(response.toString());

                            JSONArray firstarray = new JSONArray(recordset.getString("recordsets"));
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

                                        llAssessmentDetails.setVisibility(View.VISIBLE);
                                        llNameTransfer.setVisibility(View.VISIBLE);

                                        progressDialog.dismiss();
                                        Log.e("ooosecond", "---" + jsonObject.toString());

                                    }
                                } else {

                                    Snackbar.make(rlRoot, "No Data Found ", Snackbar.LENGTH_SHORT).show();

                                    llNameTransfer.setVisibility(View.GONE);
                                    progressDialog.dismiss();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    SnackShowTop("Time out", rlRoot);
                } else if (error instanceof AuthFailureError) {
                    SnackShowTop("Connection Time out", rlRoot);
                } else if (error instanceof ServerError) {
                    SnackShowTop("Could not connect server", rlRoot);
                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rlRoot);
                } else if (error instanceof ParseError) {
                    SnackShowTop("Parse Error", rlRoot);
                } else {
                    SnackShowTop(error.getMessage(), rlRoot);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, REQUEST_TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                }
                break;
        }
        addFile(docPaths);

    }

    //This method is for getting file name and showing the name of the file
    public void addFile(ArrayList<String> doc_paths) {

        if (doc_paths != null) {

            ArrayList<String> path = doc_paths;

            if (fileString.equalsIgnoreCase("registrationDocument")) {
                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    etChoosenFileOne.setText(String.valueOf(file.getName()));
                }
            } else if (fileString.equalsIgnoreCase("legalDocument")) {
                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    etChoosenFileTwo.setText(String.valueOf(file.getName()));
                }
            } else if (fileString.equalsIgnoreCase("deathCertificate")) {
                for (String s : path) {
                    String pat = s.toString();
                    file = new File(pat);
                    etChoosenFileThree.setText(String.valueOf(file.getName()));
                }
            }

        }
    }

    //This method is for uploading selected image or file
    public void uploadImage(File file) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);

        final ProgressDialog dialog = new ProgressDialog(NameTransfer.this);
        dialog.setMessage("uploading");
        dialog.show();

        Call<Result> req = retrofitInterface.UploadNameTransfer(body, requestNo,
                etDistrict.getText().toString(), etPanchayat.getText().toString(), edTaxNo.getText().toString(), uploadType,
                "Android");

        req.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
                Log.e("respomnse", "" + response.message());

                String message = response.body().getMesssage();

                Snackbar.make(rlRoot, "" + message, Snackbar.LENGTH_SHORT).show();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
                Log.e("res", "" + t.getMessage());

                Snackbar.make(rlRoot, "" + t.getMessage(), Snackbar.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });
    }

    //This method is for adding the filetypes which is necessary for file picking
    private void showFileChooser() {

        String[] zips = {".zip", ".rar"};
        String[] pdfs = {".pdf"};
        String[] images = {".png", ".jpg"};
        int maxCount = MAX_ATTACHMENT_COUNT - photoPaths.size();
        if ((docPaths.size() + photoPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(getApplicationContext(), "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items",
                    Toast.LENGTH_SHORT).show();
        } else {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .setSelectedFiles(docPaths)
                    .setActivityTheme(R.style.LibAppTheme)
                    .setActivityTitle("Please select doc")
                    .addFileSupport("ZIP", zips)
                    .addFileSupport("PDF", pdfs, R.drawable.arrow_back)
                    .addFileSupport("IMAGES", images)
                    .enableDocSupport(false)
                    .enableSelectAll(true)
                    .sortDocumentsBy(SortingTypes.name)
                    .withOrientation(Orientation.UNSPECIFIED)
                    .pickFile(this);
        }
    }

    //This method is for showing the snackbar in an activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }

    //This method is for generating random number
    public static String generateRandom() {
        String chars = "1234567890";

        final int PW_LENGTH = 5;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < PW_LENGTH; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    }

    //This method is for getting district and panchayat from shared preference and setting in their fields
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

    //This method is for initializing spinners with retrieved district and panchayat
    public void initializeDPSpinnerOnClicks(){
        spinnerDialogDistrict = new SpinnerDialog(NameTransfer.this, mDistrictList, "Select District", "Close");// With No Animation

        spinnerDialogPanchayat = new SpinnerDialog(NameTransfer.this, mPanchayatList, "Select Panchayat", "Close");// With No Animation

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
                    Snackbar.make(rlRoot, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                 panchayatId = sharedPreferenceHelpher.getInt(PREF_SELECTPANCHAYATID);

                int districtId = sharedPreferenceHelpher.getInt(PREF_SELECTDISTRICTID);
                commonMethods.getTownPanchayat(districtId, rlRoot);

            }
        });

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                etDistrict.setText(item);

                 districtId = mDistrictPojoList.get(position).getdID();

                sharedPreferenceHelpher.putString(PREF_SELECTDISTRICT, item);
                sharedPreferenceHelpher.putInt(PREF_SELECTDISTRICTID, districtId);
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
            showSnackbar(rlRoot, "No Panchayat Found !");
        }
    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
