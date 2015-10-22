package com.example.guy.sendandreceiveexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Guy on 10/21/2015.
 */
public class SMSReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        for(int x = 0; x<10; x++)
        {
            System.out.println("MESSAGE RECIEVED!!!!!!!!!!!!!");
        }
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String str = "";
        if(bundle!=null)
        {

            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for(int i= 0;i<messages.length;i++)
            {
                messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
                str+="Message from "+messages[i].getOriginatingAddress();
                str+=" :";
                str+=messages[i].getMessageBody().toString();
                str+="\n";
            }
            Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",str);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
