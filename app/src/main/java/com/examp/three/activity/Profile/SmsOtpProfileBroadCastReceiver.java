package com.examp.three.activity.Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsOtpProfileBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // get the bundle object
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsMessages=null;
        String sms_Str = "";
        if(bundle!=null){
            //Get the sms message
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsMessages = new SmsMessage[pdus.length];
            for (int i=0; i<smsMessages.length; i++){
                smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                Log.e("Vb",smsMessages[i].getDisplayMessageBody().toString()+"ghhf");
                sms_Str = smsMessages[i].getDisplayMessageBody().toString().substring(13,19);
                //Check here sender is yours
                Intent smsIntent = new Intent("otp");
                smsIntent.putExtra("message",sms_Str);

                LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

            }
        }
    }
}
