package com.examp.three;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.ContactUs;
import com.examp.three.activity.Profile.Profile;
import com.examp.three.activity.Property.PropertyTaxCalculator_Activity;
import com.examp.three.activity.QuickPay.QuickPayActivity;
import com.examp.three.adapter.Home.CommanListAdapter;
import com.examp.three.adapter.HomeSliderAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.DI.components.DaggerIconInfoComponents;
import com.examp.three.common.DI.modules.AppModule;
import com.examp.three.common.DI.modules.HomePresenterModule;
import com.examp.three.common.DI.modules.SharedPrefModule;
import com.examp.three.common.SharedPrefManager;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.common.helper.CommonInterface;
import com.examp.three.common.helper.CommonMethods;
import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Home.HomeBean;
import com.examp.three.model.Home.MoveToCommanList;
import com.examp.three.model.Panchayats;
import com.examp.three.presenter.HomePresenter;
import com.examp.three.presenter.HomeactivtyContract;
import com.examp.three.recyclerclasses.ClickListener;
import com.examp.three.recyclerclasses.RecyclerTouchListener;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import io.paperdb.Paper;

import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTDISTRICTID;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYAT;
import static com.examp.three.common.SharedPreferenceHelper.PREF_SELECTPANCHAYATID;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_email;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_firstname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_lastname;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_password;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_type;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_username;
import static com.examp.three.common.SharedPreferenceHelper.sharedPreferenceHelpher;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeactivtyContract.view, View.OnClickListener, CommonInterface {
    ViewPager viewPager;
    android.app.AlertDialog waitingDialog;
    HomeSliderAdapter sliderAdapter;
    public static String TAG = HomeActivity.class.getSimpleName();
    DrawerLayout drawer;
    private String mDistrictId, mDistrictName;
    private String mPanchayatId, mPanchayatName;
    SpinnerDialog spinnerDialogDistrict;
    SpinnerDialog spinnerDialogPanchayat;

    String districtList = null;
    Menu mMenu;

    //    @Inject
    SharedPreferences sharedPreference;

    String MyPREFERENCES = "User";
//    private DIComponent myComponent;

    SharedPreferences.Editor editor;

    private HashMap<Integer, String> mDistrictHashmapitems = new LinkedHashMap<>();
    String login_username, login_password, login_emailId, login_firstName, login_lastName, login_surName,
            login_district, login_panchayat;
    private HashMap<Integer, String> mPanchayatHashmapitems = new LinkedHashMap<>();
    ExtensiblePageIndicator indicator;
    TextView tv_selectPanchayat, tv_selectDistrict;
    LinearLayout linear_building, lin_trade_licence, linPropertyTax, liBirthDetails, mlinProfessional_tax,
            linear_watercharges, linear_nontax, linear_grievances, linear_deathdetails;
    CoordinatorLayout rootlayout;

    CommonMethods commonMethods;

    int versionNumber;
    String versionName;
    String latestVersion;

    TextView tvuserName, tvemail;

    SpotsDialog spotsDialog;

//new

    RecyclerView recyclerView, rv_more_icons;
    List<HomeBean> commanList;
    //HomePresenter homePresenter;
    @BindView(R.id.lay_birth)
    LinearLayout lay_birth;
    @BindView(R.id.lay_death)
    LinearLayout lay_death;

    @BindView(R.id.lay_trade)
    LinearLayout lay_trade;

    @BindView(R.id.lay_greivances)
    LinearLayout lay_greivances;

    @BindView(R.id.lay_building)
    LinearLayout lay_building;

    @BindView(R.id.fab_tax_calculator)
    FloatingActionButton fabTaxCalculator;

    @Inject
    HomePresenter homePresenter;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SharedPrefManager sharedPrefManager;

    CommanListAdapter commanListAdapter;

    private ArrayList<String> mDistrictList = new ArrayList<String>();
    private ArrayList<District_Pojo> mDistrictPojoList = new ArrayList<District_Pojo>();
    private ArrayList<Panchayats> mPanchayatPojoList = new ArrayList<>();
    private ArrayList<String> mPanchayatList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        commonMethods = new CommonMethods(HomeActivity.this, HomeActivity.this);

        if (Common.isNetworkAvailable(HomeActivity.this)) {
            commonMethods.getDistricts(drawer);
        } else {
            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        Paper.init(getApplicationContext());
        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login_username = sharedPreference.getString(pref_login_username, "");
        login_password = sharedPreference.getString(pref_login_password, "");
        login_emailId = sharedPreference.getString(pref_login_email, "");
        login_firstName = sharedPreference.getString(pref_login_firstname, "");
        login_lastName = sharedPreference.getString(pref_login_lastname, "");
        login_surName = login_lastName + " " + login_firstName;

        Log.e("sharedddd", "ss" + login_username);

//new

        recyclerView = (RecyclerView) findViewById(R.id.common_layout);
        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));

        DaggerIconInfoComponents.builder().appModule(new AppModule(getApplication())).sharedPrefModule(new SharedPrefModule()).homePresenterModule(new HomePresenterModule(this)).build().inject(HomeActivity.this);
        homePresenter.checkCommanListWithMovelist(sharedPreferences, sharedPrefManager);
        lay_birth.setOnClickListener(this);
        lay_death.setOnClickListener(this);
        lay_trade.setOnClickListener(this);
        lay_greivances.setOnClickListener(this);
        lay_building.setOnClickListener(this);
        recyclerEvents();

        if (Common.isNetworkAvailable(HomeActivity.this)) {
            getLatestVersion();
        } else {
            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        drawer = findViewById(R.id.drawer_layout);

        fabTaxCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PropertyTaxCalculator_Activity.class));
            }
        });

       /* lay_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                        Intent i = new Intent(getApplicationContext(), Etown_Login.class);
                        i.putExtra("Type", "Trade");

                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_out_left,
                                R.anim.leftanim);

                    } else {
                        moveRootIntent("Trade");

                    }
                } else
                    Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

            }
        });*/

        tv_selectPanchayat = findViewById(R.id.tv_select_panchayat);
        tv_selectDistrict = findViewById(R.id.tv_select_district);

        sharedPreferenceHelpher = new SharedPreferenceHelper(HomeActivity.this);

        initializeDistrictPanchayat_SetText();
        initializeDPSpinnerOnClicks();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tvuserName = (TextView) header.findViewById(R.id.header_username);
        tvemail = (TextView) header.findViewById(R.id.header_email);

        tvuserName.setText(login_surName);
        tvemail.setText(login_emailId);

        Menu nav_Menu = navigationView.getMenu();

        if (Build.VERSION.SDK_INT > 11) {
            if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                nav_Menu.findItem(R.id.logout).setVisible(false);
                nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                nav_Menu.findItem(R.id.nav_quickpay).setVisible(true);

            } else {

                nav_Menu.findItem(R.id.logout).setVisible(true);
                nav_Menu.findItem(R.id.nav_profile).setVisible(true);
                nav_Menu.findItem(R.id.nav_quickpay).setVisible(false);

            }
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void moveRootIntent(String intent_type) {
        Intent i = new Intent(getApplicationContext(), NewNavigation.class);
        i.putExtra("Type", intent_type);
        startActivity(i);
        overridePendingTransition(R.anim.anim_slide_out_left,
                R.anim.leftanim);
    }

    public void getLatestVersion() {
        String URL_ADD = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_GET_LATEST_VERSION;

//        spotsDialog = new SpotsDialog(this);
//        spotsDialog.setCancelable(false);
//        spotsDialog.show();

        try {

            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumber = pinfo.versionCode;
            versionName = pinfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("rrrrrrrrr" + s.toString());
                latestVersion = s;
                if (Integer.valueOf(latestVersion) > versionNumber) {
                    if (!(HomeActivity.this).isFinishing()) {
                        //show dialog
                        alertBoxVersion();

                    }


                } else if (Integer.valueOf(latestVersion) < versionNumber) {
                    if (!(HomeActivity.this).isFinishing()) {
                        //show dialog
                        alertboxmaintenance();
                    }
                }
                // spotsDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // spotsDialog.dismiss();
            }
        });

        AppSingleton.getInstance(HomeActivity.this).addToRequestQueue(request, "latestversion");

    }

    public void alertboxmaintenance() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        pDialog.setTitleText("Etowns");
        pDialog.setContentText("Etowns App is under Maintenance");
//                .setCancelText("No")

        pDialog.setCustomImage(R.drawable.settings);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                HomeActivity.this.finish();

            }
        });
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void alertBoxVersion() {

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        pDialog.setTitleText("Etowns");
        pDialog.setContentText("There is new version of this  ETowns application Available , click  UPDATE to upgrade now?");
//                .setCancelText("No")
        pDialog.setCustomImage(R.mipmap.logo);
        pDialog.setConfirmText("UPDATE");
        pDialog.showCancelButton(true);

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    sweetAlertDialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

                    finish();
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void alertBoxVersion_logout() {

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        pDialog.setTitleText("Etowns");
        pDialog.setContentText("Are you sure want to Logout?");
        pDialog.setCancelText("No");
        pDialog.setCustomImage(R.mipmap.logo);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(true);
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        });
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                editor = sharedPreference.edit();

                editor.putString(pref_login_type, "");
                editor.putString(pref_login_password, "");
                editor.putString(pref_login_username, "");

                editor.clear();
                editor.apply();

                Intent i = new Intent(HomeActivity.this, Etown_Login.class);
                i.putExtra("Type", "Home");
                startActivity(i);
                finish();
                sweetAlertDialog.dismiss();

            }
        });
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void recyclerEvents() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (commanList.get(position).getImageId()) {
                    case R.drawable.nbirth:
//                        Toast.makeText(HomeActivity.this, "birth", Toast.LENGTH_SHORT).show();
                        moveRootIntent("Birth");

                        break;
                    case R.drawable.ndeath:
                        moveRootIntent("Death");

                        break;
                    case R.drawable.ntradelicence:
                        if (Common.isNetworkAvailable(getApplicationContext())) {

                            if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                                Intent i = new Intent(getApplicationContext(), Etown_Login.class);
                                i.putExtra("Type", "Trade");

                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.anim_slide_out_left,
                                        R.anim.leftanim);

                            } else {
                                moveRootIntent("Trade");
                            }

                        } else
                            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                        break;
                    case R.drawable.ngrievances:
                        moveRootIntent("Grievances_Track");
                        break;
                    case R.drawable.nbuildingplan:
                        if (Common.isNetworkAvailable(getApplicationContext())) {

                            if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                                Intent i = new Intent(getApplicationContext(), Etown_Login.class);
                                i.putExtra("Type", "Building");

                                startActivity(i);
                                finish();

                                overridePendingTransition(R.anim.anim_slide_out_left,
                                        R.anim.leftanim);

                            } else {
                                moveRootIntent("Building");
                            }

                        } else
                            Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                        break;
                    case R.drawable.nproperty:

                        moveRootIntent("Property");
                        break;
                    case R.drawable.nprofessional:
                        moveRootIntent("Profession");
                        break;

                    case R.drawable.nwater:

                        moveRootIntent("Water");
                        break;
                    case R.drawable.nnontax:
                        moveRootIntent("NonTax");
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (commanList.get(position).getImageId() != R.drawable.nproperty && commanList.get(position).getImageId() != R.drawable.nprofessional
                        && commanList.get(position).getImageId() != R.drawable.nwater && commanList.get(position).getImageId() != R.drawable.nnontax) {
                    switch (commanList.get(position).getImageId()) {
                        case R.drawable.nbirth:
                            deleteiconFromRecent(position, "1");
                            break;
                        case R.drawable.ndeath:
                            deleteiconFromRecent(position, "2");
                            break;
                        case R.drawable.ntradelicence:
                            deleteiconFromRecent(position, "3");
                            break;
                        case R.drawable.ngrievances:
                            deleteiconFromRecent(position, "4");
                            break;
                        case R.drawable.nbuildingplan:
                            deleteiconFromRecent(position, "5");
                            break;
                    }

                } else {
                    Toast.makeText(HomeActivity.this, "You cannot Delete this Options!", Toast.LENGTH_SHORT).show();
                }

            }
        }));
    }

    @Override
    public void returningCommonList(List<HomeBean> commanList) {
        this.commanList = commanList;
        commanListAdapter = new CommanListAdapter(HomeActivity.this, commanList);
        recyclerView.setAdapter(commanListAdapter);
    }

    public void deleteiconFromRecent(final int position, final String objectname) {
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to remove from here?");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commanList.remove(position);
                commanListAdapter.notifyDataSetChanged();
                homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", objectname, 1), "UserIcon~" + objectname);
                alertDialog.show().dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.show().dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MoveToCommanList m;
        switch (id) {
            case R.id.lay_birth:
                m = homePresenter.getObject(sharedPreferences, sharedPrefManager, "UserIcon~1");
                if (m != null) {
                    if (m.getCount() < 3) {
                        homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "1", m.getCount() + 1), "UserIcon~1");
                    }
                    if (m.getCount() >= 3) {
                        homePresenter.checkCommanListWithMovelist(sharedPreferences, sharedPrefManager);
                    }
                    moveRootIntent("Birth");
                } else {
                    homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "1", 1), "UserIcon~1");
                }
                break;
            case R.id.lay_death:
                m = homePresenter.getObject(sharedPreferences, sharedPrefManager, "UserIcon~2");
                if (m != null) {
                    if (m.getCount() < 3) {
                        homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", m.getCount() + 1), "UserIcon~2");
                    }
                    if (m.getCount() >= 3) {
                        homePresenter.checkCommanListWithMovelist(sharedPreferences, sharedPrefManager);
                    }
                    moveRootIntent("Death");
                } else {
                    homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", 1), "UserIcon~2");
                }
                break;
            case R.id.lay_trade:

                m = homePresenter.getObject(sharedPreferences, sharedPrefManager, "UserIcon~3");
                if (m != null) {
                    if (m.getCount() < 3) {
                        homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", m.getCount() + 1), "UserIcon~3");
                    }
                    if (m.getCount() >= 3) {
                        homePresenter.checkCommanListWithMovelist(sharedPreferences, sharedPrefManager);
                    }
                    if (Common.isNetworkAvailable(getApplicationContext())) {

                        if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                            Intent i = new Intent(getApplicationContext(), Etown_Login.class);
                            i.putExtra("Type", "Trade");

                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_out_left,
                                    R.anim.leftanim);

                        } else {
                            moveRootIntent("Trade");

                        }

                    } else
                        Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                } else {
                    homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", 1), "UserIcon~3");
                }
                break;
            case R.id.lay_greivances:
                m = homePresenter.getObject(sharedPreferences, sharedPrefManager, "UserIcon~4");
                if (m != null) {
                    if (m.getCount() < 3) {
                        homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", m.getCount() + 1), "UserIcon~4");
                    }
                    if (m.getCount() >= 3) {
                        homePresenter.checkCommanListWithMovelist(sharedPreferences, sharedPrefManager);
                    }
                    moveRootIntent("Grievances_Track");

                } else {
                    homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", 1), "UserIcon~4");
                }
                break;
            case R.id.lay_building:
                m = homePresenter.getObject(sharedPreferences, sharedPrefManager, "UserIcon~5");
                if (m != null) {
                    if (m.getCount() < 3) {
                        homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", m.getCount() + 1), "UserIcon~5");
                    }
                    if (m.getCount() >= 3) {
                        homePresenter.checkCommanListWithMovelist(sharedPreferences, sharedPrefManager);
                    }
                    if (Common.isNetworkAvailable(getApplicationContext())) {

                        if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

                            Intent i = new Intent(getApplicationContext(), Etown_Login.class);
                            i.putExtra("Type", "Building");

                            startActivity(i);
                            finish();

                            overridePendingTransition(R.anim.anim_slide_out_left,
                                    R.anim.leftanim);

                        } else {
                            moveRootIntent("Building");

                        }

                    } else
                        Snackbar.make(rootlayout, "No Internet Connection !", Snackbar.LENGTH_SHORT).show();

                } else {
                    homePresenter.storeObject(sharedPreferences, sharedPrefManager, new MoveToCommanList("103", "2", 1), "UserIcon~5");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Etown App")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//
    }

    //    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        mMenu = menu;

        if (TextUtils.isEmpty(login_username) && TextUtils.isEmpty(login_password)) {

            mMenu.getItem(1).setVisible(false);
            mMenu.getItem(0).setVisible(true);
        } else {

            mMenu.getItem(1).setVisible(true);
            mMenu.getItem(0).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.login) {

            Intent i = new Intent(HomeActivity.this, Etown_Login.class);
            i.putExtra("Type", "Home");
            startActivity(i);
            return true;
        }

        if (id == R.id.logout) {
            alertBoxVersion_logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawers();

        } else if (id == R.id.nav_quickpay) {

            Intent i = new Intent(getApplicationContext(), QuickPayActivity.class);
            startActivity(i);

        } else if (id == R.id.logout) {

            if (Common.isNetworkAvailable(getApplicationContext())) {
                alertBoxVersion_logout();

            } else {
                Snackbar.make(drawer, "Not connected to Internet", Snackbar.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_share) {

            if (Common.isNetworkAvailable(getApplicationContext())) {

                try {
                    Intent actionSend = new Intent(Intent.ACTION_SEND);
                    actionSend.setType("text/plain");
                    actionSend.putExtra(Intent.EXTRA_SUBJECT, "ETown App");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.examp.three&hl=en_IN\n\n";
                    actionSend.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(actionSend, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                    e.printStackTrace();
                }

            } else {
                Snackbar.make(drawer, "Not connected to Internet", Snackbar.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_about) {

            Intent contact_intent = new Intent(HomeActivity.this, ContactUs.class);
            startActivity(contact_intent);

        } else if (id == R.id.nav_profile) {

            Intent contact_intent = new Intent(HomeActivity.this, Profile.class);
            startActivity(contact_intent);

        } else if (id == R.id.nav_send) {

            if (Common.isNetworkAvailable(getApplicationContext())) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Try Google play
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.examp.three&hl=en_IN"));
                if (!MyStartActivity(intent)) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.examp.three&hl=en_IN"));
                    if (!MyStartActivity(intent)) {
                        //Well if this also fails, we have run out of options, inform the user.
                        Toast.makeText(HomeActivity.this, "Could not open Android market, please " +
                                "install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {

                Snackbar.make(drawer, "Internet Not Working", Snackbar.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public void initializeDistrictPanchayat_SetText() {

        Log.e("ddddd", "----> " + sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT));

        if (sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT) != null) {

            Log.e("ddddd", "----> " + sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT));
            tv_selectDistrict.setText(sharedPreferenceHelpher.getString(PREF_SELECTDISTRICT));
        } else {
            tv_selectDistrict.setText("Select District");
        }

        if (sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT) != null) {

            Log.e("ddddd", "----> " + sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT));

            tv_selectPanchayat.setText(sharedPreferenceHelpher.getString(PREF_SELECTPANCHAYAT));
        } else {
            tv_selectPanchayat.setText("Select Panchayat");
        }
    }

    public void initializeDPSpinnerOnClicks() {
        spinnerDialogDistrict = new SpinnerDialog(HomeActivity.this, mDistrictList, "Select District", "Close");// With No Animation

        spinnerDialogPanchayat = new SpinnerDialog(HomeActivity.this, mPanchayatList, "Select Panchayat", "Close");// With No Animation

        tv_selectDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_selectPanchayat.setText(R.string.select_panchayat);
                spinnerDialogDistrict.showSpinerDialog();

            }
        });

        tv_selectPanchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_selectDistrict.getText().toString().equalsIgnoreCase("Select District")) {
                    Snackbar.make(drawer, R.string.snack_district_search, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                int districtId = sharedPreferenceHelpher.getInt(PREF_SELECTDISTRICTID);
                commonMethods.getTownPanchayat(districtId, drawer);

            }
        });

        spinnerDialogDistrict.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tv_selectDistrict.setText(item);

                int pDistrictId = mDistrictPojoList.get(position).getdID();

                sharedPreferenceHelpher.putString(PREF_SELECTDISTRICT, item);
                sharedPreferenceHelpher.putInt(PREF_SELECTDISTRICTID, pDistrictId);
                sharedPreferenceHelpher.putString(PREF_SELECTPANCHAYAT, null);

            }
        });

        spinnerDialogPanchayat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                tv_selectPanchayat.setText(item);
                int panchayatId = mPanchayatPojoList.get(i).getPanchayatId();
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
            showSnackbar(drawer, "No Panchayat Found !");
        }

    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
