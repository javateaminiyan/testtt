package com.examp.three.activity.paymentgateway.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.examp.three.HomeActivity;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.QuickPay.QuickPayActivity;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.MakePayment;
import com.examp.three.activity.paymentgateway.utility.Params;
import com.examp.three.common.Common;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import dmax.dialog.SpotsDialog;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_EMAIL;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_TEL;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_ONLINE_Trade_OnlineTaxTransactionDetails;

public class StatusActivity extends AppCompatActivity {

    String status, amountPaid, assessmentNumber, assessmentName, districtName, panchayatName, taxType,
            doorNo, wardNo, streetName, sNo, financialYear, term, userId, htmlResponse, sMethodName,
            tempSNo, tempFinancialYear, tempTerm, cessAmount, waterCharges, maintenanceCharge, penaltyAmount,
            tempCessAmount, tempWaterCharges, tempMaintenanceCharge, emailId, contactNum, applicationRefNo,
            totalDemandAmount, demandAmount;
    String TAG = StatusActivity.class.getSimpleName();
    String tempAmountPaid;

    String tempcSWAAmount, SWAAmount, tempcProprtyTax, ProprtyTax;


    String orderId, trackingId, bankRefNum, paymentMode, cardName,
            statusMessage, currency, orderStatus, bankName, ccaAmount;

    String mSubject = "Online Tax Payment - Payment Details", message, message1, mailMessage, mailMessage1,
            failureMessage, failureMailMessage, sTaxType, assConn, taxCharge, member;

    String stringArray[];

    // Shared Preferences
    SharedPreferences preferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    String MyPREFERENCES = "User";
    String taxTypeFull;
    String fname;
    String tempSWMMonth, tempSwmSno, SWMSno, SWMMonth;
    String TaxType = null;
    android.app.AlertDialog waitingDialog;
    RelativeLayout rootlayout;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_paymentstatus);
        rootlayout = findViewById(R.id.rootlayout);
        Intent mainIntent = getIntent();
        status = mainIntent.getStringExtra(Params.transactionStatus);

        totalDemandAmount = mainIntent.getStringExtra("totalDemandAmount");
        demandAmount = mainIntent.getStringExtra("demandAmount");
        amountPaid = mainIntent.getStringExtra(Params.amountPaid);
        tempAmountPaid = String.format("%.2f", Double.parseDouble(amountPaid));
        assessmentNumber = mainIntent.getStringExtra(Params.assessmentNumber);
        assessmentName = mainIntent.getStringExtra(Params.assessmentName);
        districtName = mainIntent.getStringExtra(Params.districtName);
        panchayatName = mainIntent.getStringExtra(Params.panchayatName);
        taxType = mainIntent.getStringExtra(Params.taxType);
        doorNo = mainIntent.getStringExtra(Params.doorNo);
        wardNo = mainIntent.getStringExtra(Params.wardNo);
        streetName = mainIntent.getStringExtra(Params.streetName);

        emailId = mainIntent.getStringExtra(BILLING_EMAIL);
        contactNum = mainIntent.getStringExtra(BILLING_TEL);

        tempcSWAAmount = mainIntent.getStringExtra(Params.cSWAAmount);

        SWAAmount = tempcSWAAmount;

        tempcProprtyTax = mainIntent.getStringExtra(Params.cProprtyTax);
        ProprtyTax = tempcProprtyTax;

        tempSWMMonth = mainIntent.getStringExtra(Params.cSWMMonth);
        tempSwmSno = mainIntent.getStringExtra(Params.cSWMSno);
        if (Double.parseDouble(SWAAmount) > 0) {
            SWMSno = tempSwmSno.substring(1);
            SWMMonth = tempSWMMonth.substring(1);
        } else {
            SWMSno = "";
            SWMMonth = "";
        }

        tempCessAmount = mainIntent.getStringExtra(Params.cessAmount);
        cessAmount = tempCessAmount.substring(1);

        tempWaterCharges = mainIntent.getStringExtra(Params.waterCharges);
        waterCharges = tempWaterCharges.substring(1);

        tempMaintenanceCharge = mainIntent.getStringExtra(Params.maintenanceCharge);
        maintenanceCharge = tempMaintenanceCharge.substring(1);

        penaltyAmount = mainIntent.getStringExtra(Params.penaltyAmount);

        preferences = getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = preferences.edit();

        tempSNo = mainIntent.getStringExtra(Params.sNo);
        sNo = tempSNo.substring(1);

        tempFinancialYear = mainIntent.getStringExtra(Params.financialYear);
        financialYear = tempFinancialYear.substring(1);

        tempTerm = mainIntent.getStringExtra(Params.term);
        term = tempTerm.substring(1);

        emailId = mainIntent.getStringExtra(BILLING_EMAIL);
        contactNum = mainIntent.getStringExtra(BILLING_TEL);
        applicationRefNo = mainIntent.getStringExtra("ApplicationRefNo");

        TaxType = mainIntent.getStringExtra(Params.taxType);

        userId = mainIntent.getStringExtra(Params.userId);
        htmlResponse = mainIntent.getStringExtra("HTMLRESPONSE");

        stringArray = htmlResponse.split("<br>");

        orderId = stringArray[0].substring(stringArray[0].indexOf("=") + 1).trim();
        trackingId = stringArray[1].substring(stringArray[1].indexOf("=") + 1).trim();
        bankRefNum = stringArray[2].substring(stringArray[2].indexOf("=") + 1).trim();
        orderStatus = stringArray[3].substring(stringArray[3].indexOf("=") + 1).trim();
        paymentMode = stringArray[5].substring(stringArray[5].indexOf("=") + 1).trim();
        cardName = stringArray[6].substring(stringArray[6].indexOf("=") + 1).trim();
        statusMessage = stringArray[8].substring(stringArray[8].indexOf("=") + 1).trim();
        currency = stringArray[9].substring(stringArray[9].indexOf("=") + 1).trim();
        ccaAmount = stringArray[10].substring(stringArray[10].indexOf("=") + 1).trim();

        TextView tvOrderId = (TextView) findViewById(R.id.textView_orderId);
        TextView tvTrackingId = (TextView) findViewById(R.id.textView_trackingId);
        TextView tvBankRefNum = (TextView) findViewById(R.id.textView_bankRefNum);
        TextView tvOrderStatus = (TextView) findViewById(R.id.textView_OrderStatus);
        TextView tvPaymentMode = (TextView) findViewById(R.id.textView_paymentMode);
        TextView tvCardName = (TextView) findViewById(R.id.textView_cardName);
        TextView tvStatusMessage = (TextView) findViewById(R.id.textView_statusMessage);
        TextView tvAmount = (TextView) findViewById(R.id.textView_amount);
        TextView tvStatus = (TextView) findViewById(R.id.textView_orderStatus);

        tvOrderId.setText(orderId);
        tvTrackingId.setText(trackingId);
        tvBankRefNum.setText(bankRefNum);
        tvOrderStatus.setText(status);
        tvPaymentMode.setText(paymentMode);
        tvCardName.setText(cardName);
        tvStatusMessage.setText(statusMessage);
        tvAmount.setText(String.valueOf(tempAmountPaid));

        tvStatus.setText(status);

        if (userId.equals("unregistered") || userId.equals("")) {
            member = "Member";
        } else {
            member = preferences.getString("firstName", null);
        }

         switch (taxType) {
        case "P":
        sTaxType = "Property";
        assConn = "Ass.";
        taxCharge = "tax";
        taxTypeFull = "Property Tax";
        break;
        case "PR":
        sTaxType = "Profession";
        assConn = "Ass.";
        taxCharge = "tax";
        taxTypeFull = "Profession Tax";
        break;
        case "W":
        sTaxType = "Water";
        assConn = "Con.";
        taxCharge = "charges";
        taxTypeFull = "Water Charges";
        break;
        case "N":
        sTaxType = "Property Tax";
        assConn = "Ass.";
        taxCharge = "tax";
        taxTypeFull = "Non Tax";
        break;
    }


    message = "Hi,%20Ur%20" + sTaxType + "%20" + taxCharge + "%20" + assConn + "%20No.%20" + assessmentNumber.trim() +
            "%20of%20Rs." + totalDemandAmount + "%20has%20been%20" +
            "paid%20successfully.%20" + "Know%20more%20" + "http://bit.ly/1VvJfZn";

    message1 = "Hi,%20Ur%20" + sTaxType + "%20" + taxCharge + "%20" + assConn + "%20No.%20" + assessmentNumber.trim() +
            "%20of%20Rs." + demandAmount + "%20has%20been%20" +
            "paid%20successfully.%20" + "Know%20more%20" + "http://bit.ly/1VvJfZn";

        StringBuilder sbText = new StringBuilder();
        sbText.append("<font size=2 face=verdana />").append("<br />").append("<br />");
        sbText.append("<table   align=center style='border:1px solid #04B4AE '  width=100%>");
        sbText.append("<img src='http://www.prematix.com/aimages/Payment.png'>");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("<b>Dear " + member + ",</b>");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("Thank you for using our eTown App service. We acknowledge that receipt of your payment through our payment "
                + " gateway. The details of your transaction are stated below");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("<table border=1 align=center style='border-collapse:collapse' cellspacing=5 cellpadding=5 width=80%>");
        sbText.append("<tr  bgcolor=#04B4AE>");
        sbText.append("<td Colspan=2 align=center><b><font size='4' Color='white' face=verdana>eTown E-Receipt</font></b></td>");
        sbText.append("</tr>");
        sbText.append("<tr align=left>");
        sbText.append("<td><b> <font size=2 face=verdana /> Tax Type : <font color=deeppink>" + sTaxType + "</font></b></td>");
        sbText.append("<td><b><font size=2 face=verdana /> " + assConn + "No: <font color=deeppink>" + assessmentNumber.trim() + "</font></b></b> </td>");
        sbText.append("</tr>");
        sbText.append("<tr align=left>");
        sbText.append("<td><b> <font size=2 face=verdana /> Order No : <font color=deeppink> " + orderId + "</font></b> </td>");
        sbText.append("<td><b><font size=2 face=verdana /> Amount : <font color=deeppink>" + totalDemandAmount + "</font></b> </td>");
        sbText.append("</tr>");
        sbText.append("<tr align=left>");
        sbText.append("</tr>");
        sbText.append("</table>");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("<font Color=Blue>If you want to download Payment Receipt kindly Logon with https://www.etownpanchayat.com.</font>");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("<font Color=Blue>This is a system generated mail, Please do not reply to this mail.</font>");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("<br />");
        sbText.append("<font color=blue />");
        sbText.append("<b> Regards,</b>");
        sbText.append("<font color=Brown />");
        sbText.append("<br />");
        sbText.append("<b> Executive Officer</b>");
        sbText.append("<br />");
        sbText.append("<b> " + panchayatName + " Town Panchayat</b>");
        sbText.append("<br />");
        sbText.append("<b> " + districtName + " District</b>");
        sbText.append("</table>");

        mailMessage = sbText.toString();

//water
        StringBuilder sbText1 = new StringBuilder();
        sbText1.append("<font size=2 face=verdana />").append("<br />").append("<br />");
        sbText1.append("<table   align=center style='border:1px solid #04B4AE '  width=100%>");
        sbText1.append("<img src='http://www.prematix.com/aimages/Payment.png'>");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("<b>Dear " + member + ",</b>");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("Thank you for using our eTown App service. We acknowledge that receipt of your payment through our payment "
                + " gateway. The details of your transaction are stated below");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("<table border=1 align=center style='border-collapse:collapse' cellspacing=5 cellpadding=5 width=80%>");
        sbText1.append("<tr  bgcolor=#04B4AE>");
        sbText1.append("<td Colspan=2 align=center><b><font size='4' Color='white' face=verdana>eTown E-Receipt</font></b></td>");
        sbText1.append("</tr>");
        sbText1.append("<tr align=left>");
        sbText1.append("<td><b> <font size=2 face=verdana /> Tax Type : <font color=deeppink>" + sTaxType + "</font></b></td>");
        sbText1.append("<td><b><font size=2 face=verdana /> " + assConn + "No: <font color=deeppink>" + assessmentNumber.trim() + "</font></b></b> </td>");
        sbText1.append("</tr>");
        sbText1.append("<tr align=left>");
        sbText1.append("<td><b> <font size=2 face=verdana /> Order No : <font color=deeppink> " + orderId + "</font></b> </td>");
        sbText1.append("<td><b><font size=2 face=verdana /> Amount : <font color=deeppink>" + demandAmount + "</font></b> </td>");
        sbText1.append("</tr>");
        sbText1.append("<tr align=left>");
        sbText1.append("</tr>");
        sbText1.append("</table>");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("<font Color=Blue>If you want to download Payment Receipt kindly Logon with https://www.etownpanchayat.com.</font>");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("<font Color=Blue>This is a system generated mail, Please do not reply to this mail.</font>");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("<br />");
        sbText1.append("<font color=blue />");
        sbText1.append("<b> Regards,</b>");
        sbText1.append("<font color=Brown />");
        sbText1.append("<br />");
        sbText1.append("<b> Executive Officer</b>");
        sbText1.append("<br />");
        sbText1.append("<b> " + panchayatName + " Town Panchayat</b>");
        sbText1.append("<br />");
        sbText1.append("<b> " + districtName + " District</b>");
        sbText1.append("</table>");

        mailMessage1 = sbText1.toString();

        failureMessage = "Hi,%20Ur%20" + sTaxType + "%20" + taxCharge + "%20" + assConn + "%20No.%20" + assessmentNumber.trim() +
                "%20has%20been%20" +
                "cancelled.%20" + "Know%20more%20" + "http://bit.ly/1VvJfZn";

        StringBuilder sbText2 = new StringBuilder();
        sbText2.append("<font size=2 face=verdana />").append("<br />").append("<br />");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("<b>Dear " + member + ",</b>");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("Your " + sTaxType + " " + taxCharge + " " + assConn + " No. " + assessmentNumber.trim() +
                " mobile payment Order No. " + orderId + "  was " + "unsuccessful.The details of your transaction are stated below");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("<table border=1 align=center style='border-collapse:collapse' cellspacing=5 cellpadding=5 width=80%>");
        sbText2.append("<tr  bgcolor=#04B4AE>");
        sbText2.append("<td Colspan=2 align=center><b><font size='4' Color='white' face=verdana>eTown E-Receipt</font></b></td>");
        sbText2.append("</tr>");
        sbText2.append("<tr align=left>");
        sbText2.append("<td><b> <font size=2 face=verdana /> Tax Type : <font color=deeppink>" + sTaxType + "</font></b></td>");
        sbText2.append("<td><b><font size=2 face=verdana /> " + assConn + "No: <font color=deeppink>" + assessmentNumber.trim() + "</font></b></b> </td>");
        sbText2.append("</tr>");
        sbText2.append("<tr align=left>");
        sbText2.append("<td><b> <font size=2 face=verdana /> Order No : <font color=deeppink> " + orderId + "</font></b> </td>");
        sbText2.append("<td><b><font size=2 face=verdana /> Transaction Status : <font color=deeppink>" + "Cancelled" + "</font></b> </td>");
        sbText2.append("</tr>");
        sbText2.append("<tr align=left>");
        sbText2.append("</tr>");
        sbText2.append("</table>");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("<font Color=Blue>If you want to download Payment Receipt kindly Logon with https://www.etownpanchayat.com.</font>");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("<font Color=Blue>This is a system generated mail, Please do not reply to this mail.</font>");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("<br />");
        sbText2.append("<font color=blue />");
        sbText2.append("<b> Regards,</b>");
        sbText2.append("<font color=Brown />");
        sbText2.append("<br />");
        sbText2.append("<b> Executive Officer</b>");
        sbText2.append("<br />");
        sbText2.append("<b> " + panchayatName + " Town Panchayat</b>");
        sbText2.append("<br />");
        sbText2.append("<b> " + districtName + " District</b>");

        failureMailMessage = sbText2.toString();

        if (status.equals("Transaction Successful!")) {
//        TODO:     Dont forget to comment during production

            tvStatus.setBackgroundColor(getResources().getColor(R.color.green));
            switch (taxType) {
                case "P":
                    sMethodName = Common.METHOD_NAME_SAVE_PROPERTY;
                    if (Common.isNetworkAvailable(StatusActivity.this)) {

                        savePropProfToCollection();
                        saveOnlineTransDetails();
                        sendSMS(contactNum, message);
                        sendMail(emailId, mSubject, mailMessage);

                    } else {
                        alertBox();
                    }
                    break;
                case "PR":
                    if (Common.isNetworkAvailable(StatusActivity.this)) {
                        sMethodName = Common.METHOD_NAME_SAVE_PROFESSION;
                        savePropProfToCollection();
                        saveOnlineTransDetails();
                        sendSMS(contactNum, message);
                        sendMail(emailId, mSubject, mailMessage);

                    } else {
                        alertBox();
                    }
                    break;
                case "W":
                    if (Common.isNetworkAvailable(StatusActivity.this)) {

                        Log.e("water","ttttttt");
                        saveWaterToCollection();
                        saveOnlineTransDetails();
                        sendSMS(contactNum, message1);
                        sendMail(emailId, mSubject, mailMessage1);

                    } else {
                        alertBox();
                    }
                    break;
                case "N":
                    if (Common.isNetworkAvailable(StatusActivity.this)) {
                        saveNonTaxToCollection();
                        saveOnlineTransDetails();
                        sendSMS(contactNum, message);
                        sendMail(emailId, mSubject, mailMessage);

                        break;
                    } else {
                        alertBox();
                    }


                case "Trade":
                    if (Common.isNetworkAvailable(StatusActivity.this)) {
                       TradeUpdateApi();
                        getTransactionfinalSaveDetails();
                        TradePaymentHistory();
                        break;
                    } else {
                        alertBox();
                    }
                case "Building":
                    if (Common.isNetworkAvailable(StatusActivity.this)) {
                        BuildingApi();
                        getTransactionfinalSaveDetails();

                        break;
                    } else {
                        alertBox();
                    }
                default:

                    break;
            }


        } else {
            tvStatus.setBackgroundColor(getResources().getColor(R.color.holo_red_dark));
            sendSMS(contactNum, failureMessage);
            sendMail(emailId, mSubject, failureMailMessage);
            saveOnlineTransDetails();
            if(taxType.equalsIgnoreCase("Trade")||taxType.equalsIgnoreCase("Building")){
                getTransactionfinalSaveDetails();

            }
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

    //This method is for redirecting to home activity
    public void home(View view) {

        if (userId.equals("unregistered") || userId.equals("")) {
            Intent intent = new Intent(StatusActivity.this, QuickPayActivity.class);
            intent.putExtra(Common.Type,taxType);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(StatusActivity.this, MakePayment.class);
            intent.putExtra(Common.Type, taxType);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void alertBox() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StatusActivity.this);
        builder.setMessage("Please connect to Internet and try again, do not go back.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (taxType) {
                            case "P":
                                sMethodName = Common.METHOD_NAME_SAVE_PROPERTY;
                                if (Common.isNetworkAvailable(StatusActivity.this)) {
                                    savePropProfToCollection();
                                    saveOnlineTransDetails();
                                } else {
                                    alertBox();
                                }
                                break;
                            case "PR":
                                if (Common.isNetworkAvailable(StatusActivity.this)) {
                                    sMethodName = Common.METHOD_NAME_SAVE_PROFESSION;
                                    savePropProfToCollection();
                                    saveOnlineTransDetails();
                                } else {
                                    alertBox();
                                }
                                break;
                            case "W":
                                if (Common.isNetworkAvailable(StatusActivity.this)) {
                                    saveWaterToCollection();
                                    saveOnlineTransDetails();
                                } else {
                                    alertBox();
                                }
                                break;
                            case "N":
                                if (Common.isNetworkAvailable(StatusActivity.this)) {
                                    saveNonTaxToCollection();
                                    saveOnlineTransDetails();
                                    break;
                                } else {
                                    alertBox();
                                }
                            case "Trade":
                                if (Common.isNetworkAvailable(StatusActivity.this)) {
                                    TradeUpdateApi();
                                    TradePaymentHistory();

                                    break;
                                } else {
                                    alertBox();
                                }
                            case "Building":
                                if (Common.isNetworkAvailable(StatusActivity.this)) {
                                    BuildingApi();

                                    break;
                                } else {
                                    alertBox();
                                }
                            default:

                                break;
                        }
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //This method is for saving the values based on the tax types
    public void savePropProfToCollection() {

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(panchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userId.equals("unregistered")) {
            userId = "";
        }

        String URL_SAVE = null;
        if (taxType.equalsIgnoreCase("P")) {
            URL_SAVE = Common.URL_ONLINEPAYMENT_DOMAIN + sMethodName + "?" + "district=" + districtName +
                    "&panchayat=" + tempPanchayat + "&assessmentNo=" + assessmentNumber +
                    "&assessmentName=" + URLEncoder.encode(assessmentName) +
                    "&financialYear=" + financialYear +
                    "&term=" + term + "&sNo=" + sNo + "&amountPaid=" + ProprtyTax + "&mobileNo=" + contactNum +
                    "&emailId=" + emailId + "&orderId=" + orderId + "&userId=" + userId + "&SWMAmount=" + SWAAmount + "&SWMSno=" + SWMSno + "&SWMMonth=" + SWMMonth +"&payType=A";// + "&totalDemandAmount=" +


        } else if (taxType.equalsIgnoreCase("PR")) {
            URL_SAVE =  Common.URL_ONLINEPAYMENT_DOMAIN + sMethodName + "?" + "district=" + districtName +
                    "&panchayat=" + tempPanchayat + "&assessmentNo=" + assessmentNumber +
                    "&assessmentName=" + URLEncoder.encode(assessmentName) +
                    "&financialYear=" + financialYear +
                    "&term=" + term + "&sNo=" + sNo + "&amountPaid=" + totalDemandAmount + "&mobileNo=" + contactNum +
                    "&emailId=" + emailId + "&orderId=" + orderId + "&userId=" + userId +"&payType=A";// + "&totalDemandAmount=" +
        }

        final ProgressDialog dialog = new ProgressDialog(StatusActivity.this);

        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest request = new JsonObjectRequest(URL_SAVE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {

                            System.out.println(jsonObject.getString("code"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                volleyError.printStackTrace();
            }
        }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //This method is for saving the Water collection
    public void saveWaterToCollection() {

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(panchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userId.equals("unregistered")) {
            userId = "";
        }

        String URL_SAVE = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_SAVE_WATER + "?" +
                "district=" + districtName + "&panchayat=" + tempPanchayat + "&assessmentNo=" + assessmentNumber +
                "&assessmentName=" + URLEncoder.encode(assessmentName) +
                "&financialYear=" + financialYear +
                "&term=" + term + "&sNo=" + sNo + "&cessAmount=" + cessAmount + "&quarterAmount=" + waterCharges +
                "&maintenanceCharge=" + maintenanceCharge +
                "&amountPaid=" + demandAmount + "&penaltyAmount=" + penaltyAmount + "&mobileNo=" + contactNum +
                "&emailId=" + emailId + "&orderId=" + orderId + "&userId=" + userId+"&payType=A";

        final ProgressDialog dialog = new ProgressDialog(StatusActivity.this);

        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest request = new JsonObjectRequest(URL_SAVE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {

                            System.out.println("resppppppppppp"+jsonObject.getString("code"));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                volleyError.printStackTrace();
            }
        });

        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void saveNonTaxToCollection() {

    }

    //This method is for saving the online transaction details
    public void saveOnlineTransDetails() {
        String tempOrderStatus, tempPaymentMode, tempCardName, tempStatusMessage, tempPanchayat, tempTaxType;
        String URL_SAVE_ONLINE_TRANS = "";
        if (userId.equals("unregistered")) {
            userId = "";
        }
        try {
            tempOrderStatus = URLEncoder.encode(orderStatus, "UTF-8");
            tempPaymentMode = URLEncoder.encode(paymentMode, "UTF-8");
            tempCardName = URLEncoder.encode(cardName, "UTF-8");
            tempStatusMessage = URLEncoder.encode(statusMessage, "UTF-8");
            tempPanchayat = URLEncoder.encode(panchayatName, "UTF-8");
            tempTaxType = URLEncoder.encode(taxTypeFull, "UTF-8");

            URL_SAVE_ONLINE_TRANS = Common.URL_ONLINEPAYMENT_DOMAIN + Common.METHOD_NAME_SAVE_ONLINE_TRANS + "?" +
                    "district=" + districtName + "&panchayat=" + tempPanchayat + "&taxType=" + tempTaxType +
                    "&assessmentNo=" + assessmentNumber + "&amount=" + ccaAmount + "&mobileNo=" + contactNum +
                    "&mailId=" + emailId + "&orderId=" + orderId + "&trackingId=" + trackingId +
                    "&bankRefNo=" + bankRefNum + "&orderStatus=" + tempOrderStatus + "&payMode=" + tempPaymentMode +
                    "&bankName=" + tempCardName + "&statusMessage=" + tempStatusMessage + "&userId=" + userId;

        } catch (IOException e) {
            e.printStackTrace();
        }

        final ProgressDialog dialog = new ProgressDialog(StatusActivity.this);

        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest request = new JsonObjectRequest(URL_SAVE_ONLINE_TRANS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {

                            System.out.println(jsonObject.getString("code"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                volleyError.printStackTrace();
            }
        });
        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //This method is for sending the sms to mobile number
    public void sendSMS(String phoneNumber, String msg) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

                String url = "http://smsstreet.in/websms/sendsms.aspx?userid=prematix&password=matixpre&sender=ETOWNS";
             String    URL = url + "&mobileNo=" +
                        phoneNumber + "&msg=" + msg;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error: " + error.getMessage());
                }
            });
            requestQueue.add(stringRequest);

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private boolean invalidMail = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {

                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
                invalidMail = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
            if (invalidMail) {
                Toast.makeText(getApplicationContext(), "Invalid Email".toString(), Toast.LENGTH_LONG).show();
                invalidMail = false;
            }
        }
    }

    //This method is for sending email
    private void sendMail(String toEmail, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(toEmail, subject, messageBody, session);
            new SendMailTask().execute(message);
//            System.out.println(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Common.MAIL_CREDENTIAL_MAILID, "Etowns"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
//        message.setText(messageBody);
        MimeMultipart multipart = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setContent(messageBody, "text/html");

        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtpout.secureserver.net");
        properties.put("mail.smtp.port", "3535");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Common.MAIL_CREDENTIAL_MAILID, Common.MAIL_CREDENTIAL_PASSWORD);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            if (userId.equals("unregistered") || userId.equals("")) {
                Intent intent = new Intent(StatusActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(StatusActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }

    //This method is for retrieving saved transaction details
    private void getTransactionfinalSaveDetails() {

        waitingDialog = new SpotsDialog(StatusActivity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "getTransactionBeforeSaveDetails";

        String enc_status = "", enc_paymentMode = "", enc_statusMessage = "", enc_cardName = "", enc_districtName = "", enc_panchayatName = "";
        try {
            if (status.equalsIgnoreCase("")) {
                enc_status = "";
            } else {
                enc_status = URLEncoder.encode(status, "UTF-8");
            }

            if (bankName.isEmpty()) {
                enc_cardName = "";
            } else {
                enc_cardName = URLEncoder.encode(bankName, "UTF-8");
            }

            if (paymentMode.isEmpty()) {
                enc_paymentMode = "";
            } else {
                enc_paymentMode = URLEncoder.encode(paymentMode, "UTF-8");
            }

            if (statusMessage.isEmpty()) {
                enc_statusMessage = "";
            } else {
                enc_statusMessage = URLEncoder.encode(statusMessage, "UTF-8");
            }

            if (districtName.isEmpty()) {
                enc_districtName = "";
            } else {
                enc_districtName = URLEncoder.encode(districtName, "UTF-8");
            }

            if (panchayatName.isEmpty()) {
                enc_panchayatName = "";
            } else {
                enc_panchayatName = URLEncoder.encode(panchayatName, "UTF-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

      String url = API_ONLINE_Trade_OnlineTaxTransactionDetails+"&OrderNo="+orderId+"&TrackingId="+trackingId+"&BankRefNo="+bankRefNum+"&OrderStatus="+enc_status+"&PayMode="+enc_paymentMode+"&BankName="+enc_cardName+"&StatusMessage="+enc_statusMessage+"&Amount="+tempAmountPaid+"&EntryUserId="+userId+"&District="+enc_districtName+"&Panchayat="+enc_panchayatName+"&ApplicationRefNo="+applicationRefNo+"&LoginMobileNo="+contactNum+"&LoginEmailId="+emailId+"";

        JsonObjectRequest getTransaction = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String code = response.getString("code");
                    String message = response.getString("message");
                    Log.e(TAG, "meeeee" + message);

                    waitingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    waitingDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, error.toString());

                //  waitingDialog.dismiss();
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

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getTransaction, REQUEST_TAG);
    }

    //This method is for updating the trade
    private void TradeUpdateApi() {

        waitingDialog = new SpotsDialog(StatusActivity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "getTransactionBeforeSaveDetails";

        String url = "http://www.paypre.info/Etown/Trade_InsertDORegistrationOnlineNew?iRegNo=12&iApplicationNo=" + URLEncoder.encode(applicationRefNo) + "&iTradeCode=434&sTradeDescTamil=0&sTradeDescEnglish=0&sTradersCode=54&sTradersDescTamil=0&sTradersDescEnglish=0&sTradersNameTamil=0&sTradersNameEnglish=0&sTradersTypeT=0&sTradersTypeE=d&sTradersPeriod=0&sPaymentMode="+paymentMode+"&sChequeNo=4&sBankName="+bankName+"&dChequeDate=2019-02-22&nTradersRate=0&nPenalty=0&nPFA=0&nAdvertisement=0&nTotalAmount="+demandAmount+"&sLicenceFor=0&sValidFrom=2019-02-11&sValidTo=2019-02-22&sWardNo=6&sStreetName=gff&sDoorNo=44&sFinancialYear="+financialYear+"&sFinancialFromDate=2019-02-22&sFinancialToDate=2019-03-12&sDistrict=" + URLEncoder.encode(districtName) + "&sPanchayat=" + URLEncoder.encode(panchayatName) + "&sUserName=" + fname + "&sUserId=" + userId + "";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                waitingDialog.dismiss();

                String output = response.optString("message");

                if (output.equals("Success")) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingDialog.dismiss();
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

    //This method is for retrieving payment history
    private void TradePaymentHistory(){
        waitingDialog = new SpotsDialog(StatusActivity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "tradePaymentHistory";

       String url =" http://www.predemos.com/Etown/Trade_InsertDORegistrationOnlineAPI?QType=Insert&"
        +" sOnlineApplicationNo="+applicationRefNo+"&sOrderNo="+orderId+"&sBankName="+bankName+""
        +" &sNetAmount="+demandAmount+"&sLoginMobileNo="+contactNum+"&sLoginEmailId="+emailId+"&sDistrict="+districtName+"&"
       +" sPanchayat="+panchayatName+"&sFinancialYear="+financialYear+"&sEntryType=Android&sUserId="+userId+"";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String message =response.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);
                waitingDialog.dismiss();

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
    }

    //This method is for updating building
    private void BuildingApi() {

        waitingDialog = new SpotsDialog(StatusActivity.this);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "getTransactionBeforeSaveDetails";

        String url = Common.baseUrl+"UPDATE_BL_NewApplication?ApplicationNo=" + URLEncoder.encode(applicationRefNo) + "&District=" + URLEncoder.encode(districtName) + "&Panchayat=" + URLEncoder.encode(panchayatName) + "";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                waitingDialog.dismiss();
                String output = response.optString("message");

                if (output.equals("Success")) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);
                waitingDialog.dismiss();

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
    }

}