package com.android.womensafety;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SmsSender {

    public void sendSms(String phone, String msg) {

        try {
            SmsManager sms = SmsManager.getDefault(); // using android SmsManager
            sms.sendTextMessage(phone, null, msg, null, null); // adding number and text
        } catch (Exception e) {
            Log.e("SMS", e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMultiPartSms(final Context context, final String msg, String phone) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);


            //---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "SMS sent" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(context, "Generic failure" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(context, "No service" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(context, "Null PDU" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(context, "Radio off" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "SMS delivered" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(context, "SMS not delivered" + msg,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));


            SmsManager sms = SmsManager.getDefault();

//            ArrayList<String> parts = sms.divideMessage(msg);
//
//            ArrayList<PendingIntent> sendList = new ArrayList<>();
//            sendList.add(sentPI);
//
//            ArrayList<PendingIntent> deliverList = new ArrayList<>();
//            deliverList.add(deliveredPI);

//            sms.sendMultipartTextMessage(phone, null, parts, sendList, deliverList);
            sms.sendTextMessage(phone, null, msg, sentPI, deliveredPI);

        } catch (Exception e) {
            Log.e("SMS", e.getMessage());
            e.printStackTrace();
        }
    }


}
