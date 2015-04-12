package com.example.alex.encrypt;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.Send);
        final Button change = (Button) findViewById(R.id.receive);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
                // Perform action on click
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_receive);
                // Perform action on click
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void sendMessage(View view) {
        EditText number = (EditText) findViewById(R.id.recNum);
        EditText message = (EditText) findViewById(R.id.msgContent);
        EditText key = (EditText) findViewById(R.id.secretKey);

        try {
            EncryptSMS eSMS = new EncryptSMS("AES", "CBC", "PKCS5Padding");
            CipheredMessage cM = eSMS.EncryptMessage(key.getText().toString(),
                  message.getText().toString());
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number.getText().toString(), null, cM.toTransmitionString(), null, null);


        /*    TextView textView = (TextView) findViewById(R.id.msgContent);
            textView.setText(cM.toTransmitionString()
                    + "\n"
                    + plaintext);*/

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
