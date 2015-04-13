package com.example.alex.encryption;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.telephony.SmsManager;
import java.security.Key;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
public class receive extends ActionBarActivity {

    EditText secretKey;
    TextView senderNum;
    String encryptedMsg;
    TextView decryptedMsg;
    Button submit;

    String originNum = "";
    String msgContent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("RVS", "onCreate of receive");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        senderNum= (TextView) findViewById(R.id.senderNumber);
        decryptedMsg =(TextView) findViewById(R.id.decryptedMsg);
        Log.d("RVS", "getting extras");

        Bundle extras = getIntent().getExtras();
        Log.d("RVS","got extras");
        originNum= extras.getString("recNum");
        Log.d("RVS","got originNum: " + originNum);
        encryptedMsg =  extras.getString("msgContent");
        Log.d("RVS","got msg:" + encryptedMsg);


        if(originNum != null && !originNum.equals("")){
            Log.d("RVS", originNum);
            senderNum.setText(originNum);
            Button sub = (Button) findViewById(R.id.submit);
            sub.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    secretKey=(EditText) findViewById(R.id.secretKey);
                    Log.d("RVS", "trying to decrypt");
                    try {
                        EncryptSMS dec = new EncryptSMS("AES", "CBC", "PKCS5Padding");
                        CipheredMessage mes = new CipheredMessage(encryptedMsg);
                        String s = secretKey.getText().toString();
                        String msg = dec.DecryptMessage(s,mes);
                        decryptedMsg.setText(msg);

                    }
                    catch (Exception e) {
                        Log.d("RVS", "decryption failed");
                        Log.d("RVS", e.toString());
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            Log.d("RVS","Sender number not valid");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
