package com.examp.three;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.examp.three.Network_Connection_BroadCast.InternetConnector_Receiver;
import com.examp.three.activity.Grievance_admin.GrievanceAdminActivity;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_password;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_type;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_username;

public class SplashScreen extends AppCompatActivity {

    public String TAG = SplashScreen.class.getName();
    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 7000;
    boolean value;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = InternetConnector_Receiver.getConnectivityStatusString(context);
            setStatus(status);

        }
    };

    LinearLayout rootlayout;
    String login_username, login_password, login_type, login_emailId;
    String MyPREFERENCES = "User";
    SharedPreferences sharedPreference;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        sharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        rootlayout = findViewById(R.id.rootlayout);
        checkRunTimePermission();
        login_username = sharedPreference.getString(pref_login_username, "");
        login_password = sharedPreference.getString(pref_login_password, "");
        login_type = sharedPreference.getString(pref_login_type, "");


        registerInternetCheckReceiver();

    }


    public void splash_Call() {

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if (login_type.equalsIgnoreCase("user") && !login_username.isEmpty() && !login_password.isEmpty()) {

                    Intent dashboardintent = new Intent(getApplicationContext(), HomeActivity.class);
                    dashboardintent.putExtra("Type", "login");
                    startActivity(dashboardintent);


                } else if (login_type.equalsIgnoreCase("department") && !login_username.isEmpty() && !login_password.isEmpty()) {
                    Intent dashboardintent = new Intent(getApplicationContext(), GrievanceAdminActivity.class);
                    dashboardintent.putExtra("Type", "login");
                    startActivity(dashboardintent);


                } else if (login_username.isEmpty() && login_password.isEmpty()) {
                    Intent dashboardintent = new Intent(getApplicationContext(), HomeActivity.class);
                    dashboardintent.putExtra("Type", "login");
                    startActivity(dashboardintent);

                }


                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                // isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            checkRunTimePermission();
                        }
                    }
                }
            }
        }
    }


    public void setStatus(final String status) {

        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {


            splash_Call();

        } else {

            Snackbar.make(rootlayout, "Internet Not Connected", Snackbar.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setTitle("Oops");
            builder.setMessage("Internet Not Connected");
            builder.setCancelable(true);
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
                                splash_Call();
                                dialog.dismiss();
                            }


                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    public void registerInternetCheckReceiver() {
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    public void dialog(boolean value) {
        // if(value)   {
        Intent dashboardintent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(dashboardintent);
        overridePendingTransition(R.anim.anim_slide_out_left,
                R.anim.leftanim);

    }

}


