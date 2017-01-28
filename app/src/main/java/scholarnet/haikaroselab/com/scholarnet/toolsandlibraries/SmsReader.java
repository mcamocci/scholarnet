package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

public class SmsReader extends BroadcastReceiver {

    private String senderNumber;
    private String receivedSms;

    public SmsReader() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";

        Log.e("me", "another one works");
        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                //fetching sender number
                senderNumber = msgs[i].getOriginatingAddress();
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }
            receivedSms = str;
            Log.e("me", "another one works"+senderNumber);

        }
    }
}