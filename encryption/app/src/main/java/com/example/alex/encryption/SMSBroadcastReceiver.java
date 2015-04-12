package com.example.alex.encrypt;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Alex on 3/29/2015.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {



        Bundle bundle = intent.getExtras();
        Object[] object = (Object[]) bundle.get("pdus");
        SmsMessage sms[] = new SmsMessage[object.length];
        String msgContent = "";
        String originNum = "";
        Intent rec = new Intent(context, receive.class);

        rec.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        rec.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i<object.length; i++){
            sms[i] = SmsMessage.createFromPdu((byte[]) object[i]);
            msgContent = sms[i].getDisplayMessageBody();
            originNum = sms[i].getDisplayOriginatingAddress();
            buffer.append(msgContent);
            abortBroadcast();
        }


        rec.putExtra("msgContent", msgContent);
        rec.putExtra("recNum", originNum);
        context.startActivity(rec);
    }

}
