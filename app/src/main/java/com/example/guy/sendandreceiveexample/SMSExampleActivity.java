package com.example.guy.sendandreceiveexample;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.*;
import android.content.Intent;


public class SMSExampleActivity extends Activity
{
    Button sendSMS;
    EditText msgTxt;
    EditText numText;
    IntentFilter intentFilter;
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            TextView inText = (TextView)findViewById(R.id.textMsg);
            inText.setText(intent.getExtras().getString("sms"));
        }
    };
    // @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsexample);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        sendSMS = (Button) findViewById(R.id.sendBtn);
        msgTxt = (EditText) findViewById(R.id.message);
        numText = (EditText) findViewById(R.id.numberTxt);
        sendSMS.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String myMessage = msgTxt.getText().toString();
                String theNumber = numText.getText().toString();
                sendMSG(theNumber,myMessage);
            }
        });
    }
    protected void sendMSG(String theNumber, String myMsg)
    {
        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sendPI = PendingIntent.getBroadcast(this,0,new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent)
            {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(SMSExampleActivity.this,"SMS sent",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),"Generic Failure", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(),"No Service", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        },new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Delivered", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "SMS Not Delivered", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        for(int x = 0; x<10; x++)
        {
            System.out.println("MESSAGE SENT!!!!!!!!!!!!!");
        }
        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage(theNumber, null, myMsg, sendPI, deliveredPI);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }
    protected void onResume()
    {
        registerReceiver(intentReceiver,intentFilter);
        super.onResume();
    }
    protected void onPause()
    {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}