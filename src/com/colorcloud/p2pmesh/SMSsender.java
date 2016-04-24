package com.colorcloud.p2pmesh;


import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSsender  {



   static void sendMessage(String etPhoneNo, String etMsg, Context applicationContext) {
        String phoneNo = etPhoneNo;
        String msg = etMsg;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(applicationContext, "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(applicationContext,
                    ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    }
