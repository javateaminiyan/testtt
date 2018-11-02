package com.examp.three.activity.paymentgateway.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.examp.three.R;
import com.examp.three.activity.paymentgateway.utility.AvenuesParams;
import com.examp.three.activity.paymentgateway.utility.Constants;
import com.examp.three.activity.paymentgateway.utility.Params;
import com.examp.three.activity.paymentgateway.utility.RSAUtility;
import com.examp.three.activity.paymentgateway.utility.ServiceHandler;
import com.examp.three.activity.paymentgateway.utility.ServiceUtility;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_EMAIL;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_TEL;

public class WebViewActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    Intent mainIntent;
    String  encVal;
    Toolbar toolbar;
    private ProgressBar progressBar;
    // Shared Preferences
    SharedPreferences preferences;
    WebView webview;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "EtownPreference";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_payment__web_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.transaction_page);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        mainIntent = getIntent();

        preferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();

        // Calling async task to get display content
        new RenderView().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

            int i = 0;
            String vResponse = "";
            do {
                vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
                System.out.println("vResponse = " + vResponse);
                System.out.println("vResponse.indexOf(\"[\")" + vResponse.indexOf("<"));
                i++;
            } while (vResponse.indexOf("<") == 4 && i <= 10);
            try {
                vResponse = vResponse.substring(0, vResponse.indexOf("<!")).trim();

                if (!ServiceUtility.chkNull(vResponse).equals("")
                        && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                    StringBuffer vEncVal = new StringBuffer("");
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CVV, mainIntent.getStringExtra(AvenuesParams.CVV)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NUMBER, mainIntent.getStringExtra(AvenuesParams.CARD_NUMBER)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CUSTOMER_IDENTIFIER, mainIntent.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_YEAR, mainIntent.getStringExtra(AvenuesParams.EXPIRY_YEAR)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_MONTH, mainIntent.getStringExtra(AvenuesParams.EXPIRY_MONTH)));

                    encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
                }
            } catch (NullPointerException e) {
                showToast("Please go back and try again later");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html as needed by the app
                    String status = null;

                    System.out.println("html = " + html);

                    if (html.contains("Failure")) {
                        status = "Transaction Declined!";
                    } else if (html.contains("Success")) {
                        status = "Transaction Successful!";
                    } else if (html.contains("Aborted")) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra(Params.transactionStatus, status);

                    intent.putExtra(Params.orderId, mainIntent.getStringExtra(Params.orderId));
                    intent.putExtra(Params.amountPaid, mainIntent.getStringExtra(AvenuesParams.AMOUNT));
                    intent.putExtra(Params.assessmentNumber, mainIntent.getStringExtra(Params.assessmentNumber));
                    intent.putExtra(Params.assessmentName, mainIntent.getStringExtra(Params.assessmentName));
                    intent.putExtra(Params.districtName, mainIntent.getStringExtra(Params.districtName));
                    intent.putExtra(Params.panchayatName, mainIntent.getStringExtra(Params.panchayatName));
                    intent.putExtra(Params.taxType, mainIntent.getStringExtra(Params.taxType));
                    intent.putExtra(Params.doorNo, mainIntent.getStringExtra(Params.doorNo));
                    intent.putExtra(Params.wardNo, mainIntent.getStringExtra(Params.wardNo));
                    intent.putExtra(Params.streetName, mainIntent.getStringExtra(Params.streetName));
                    intent.putExtra(Params.sNo, mainIntent.getStringExtra(Params.sNo));
                    intent.putExtra(Params.financialYear, mainIntent.getStringExtra(Params.financialYear));
                    intent.putExtra(Params.term, mainIntent.getStringExtra(Params.term));
                    intent.putExtra(Params.userId, mainIntent.getStringExtra(Params.userId));

                    intent.putExtra(Params.cSWAAmount,mainIntent.getStringExtra(Params.cSWAAmount));
                    intent.putExtra(Params.cProprtyTax,mainIntent.getStringExtra(Params.cProprtyTax));
                    intent.putExtra(Params.cSWMMonth,mainIntent.getStringExtra(Params.cSWMMonth));
                    intent.putExtra(Params.cSWMSno,mainIntent.getStringExtra(Params.cSWMSno));

                    intent.putExtra(Params.cessAmount, mainIntent.getStringExtra(Params.cessAmount));
                    intent.putExtra(Params.maintenanceCharge, mainIntent.getStringExtra(Params.maintenanceCharge));
                    intent.putExtra(Params.penaltyAmount, mainIntent.getStringExtra(Params.penaltyAmount));
                    intent.putExtra(Params.waterCharges, mainIntent.getStringExtra(Params.waterCharges));
                    intent.putExtra("totalDemandAmount", mainIntent.getStringExtra("totalDemandAmount"));
                    intent.putExtra("demandAmount", mainIntent.getStringExtra("demandAmount"));

                    intent.putExtra("ApplicationRefNo", mainIntent.getStringExtra("ApplicationRefNo"));
                    intent.putExtra(BILLING_TEL, mainIntent.getStringExtra(BILLING_TEL)); //user Contactno id
                    intent.putExtra(BILLING_EMAIL, mainIntent.getStringExtra(BILLING_TEL));

                    Log.e("TAXTYPE", "" + mainIntent.getStringExtra(Params.taxType));


                    intent.putExtra("HTMLRESPONSE", html);

                    startActivity(intent);
                }
            }

            webview = (WebView) findViewById(R.id.webview);
            webview.setWebViewClient(new WebViewClientDemo());
            webview.setWebChromeClient(new WebChromeClientDemo());
            webview.getSettings().setJavaScriptEnabled(true);

            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if (url.contains("/ccavResponseHandler.aspx")) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                    if (url.contains("oops")) {
                        alertBox();
                    }
                    if (dialog.isShowing())
                        dialog.dismiss();
                    System.out.println(webview.getUrl());
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    alertBox();
                }
            });

            /* An instance of this class will be registered as a JavaScript interface */
            StringBuffer params = new StringBuffer();
            try {
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.LANGUAGE, "EN"));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, mainIntent.getStringExtra(AvenuesParams.BILLING_NAME)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.BILLING_CITY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.BILLING_STATE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY)));
                params.append(ServiceUtility.addToPostParams(BILLING_TEL, mainIntent.getStringExtra(BILLING_TEL)));
                params.append(ServiceUtility.addToPostParams(BILLING_EMAIL, mainIntent.getStringExtra(BILLING_EMAIL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME, mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE, mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1, "additional Info."));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2, "additional Info."));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, "additional Info."));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, "additional Info."));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.PAYMENT_OPTION, mainIntent.getStringExtra(AvenuesParams.PAYMENT_OPTION)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_TYPE, mainIntent.getStringExtra(AvenuesParams.CARD_TYPE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NAME, mainIntent.getStringExtra(AvenuesParams.CARD_NAME)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DATA_ACCEPTED_AT, mainIntent.getStringExtra(AvenuesParams.DATA_ACCEPTED_AT)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ISSUING_BANK, mainIntent.getStringExtra(AvenuesParams.ISSUING_BANK)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));

            } catch (Exception e) {
                showToast(e.getMessage());
            }
            String vPostParams = params.substring(0, params.length() - 1);
            try {
                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void alertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
        builder.setMessage("Error at server page please try later");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }
    }

    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
