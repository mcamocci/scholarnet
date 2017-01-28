package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UsefullyFunctions;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText phoneEditText;
    private String phone;
    private LinearLayout fakeButton;
    private ProgressBar progressBar;
    private ReceiveAndDirect receiveAndDirect;
    private ForgotPasswordActivity forgotPasswordActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle(Html.fromHtml("FORGOT PASSWORD"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///registering receiver dynamically
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addCategory("android.intent.category.HOME");
        try {
            registerReceiver(new ReceiveAndDirect(), filter);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error message",e.toString());
        }

        ////////////////////////////////////////////////////////////////

        ////initialize the forgotActivity instance
        forgotPasswordActivity=this;

        //initialize neccessary views
        phoneEditText=(EditText)findViewById(R.id.editText3);
        fakeButton=(LinearLayout)findViewById(R.id.button4);
        phone=phoneEditText.getText().toString().trim();
        progressBar=(ProgressBar)findViewById(R.id.progressBar6);
        progressBar.setVisibility(View.INVISIBLE);


        ///when need the code

        fakeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phoneEditContent;
                try{
                    phoneEditContent=phoneEditText.getText().toString().trim();
                    phone= UsefullyFunctions.getPhone(phoneEditContent);
                    if(phone!=null){
                        if(phone.length()>=10 && phone.length()<16){
                            //sending sms to yourself
                            if(progressBar!=null){
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            Log.e("inputed phone",phone);
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(phone,null,"i want to change password",null,null);
                        }

                    }
                }catch (Exception ex){

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return  true;
    }

    public void redirectTheClient(final String phone){

        ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getBaseContext(),ChangePasswordActivity.class);
                intent.putExtra("phone",UsefullyFunctions.getPhone(phone));
                startActivity(intent);
            }
        });
    }

    public ForgotPasswordActivity getContext(){
        return forgotPasswordActivity;
    }

    public class ReceiveAndDirect extends BroadcastReceiver{

        private String senderNumber;

        public ReceiveAndDirect getInstance(){
            receiveAndDirect=this;
            return receiveAndDirect;
        }
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the data (SMS data) bound to intent
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;

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
                }

                Log.e("RECEIVER","AM WORKING AND THE PHONE IS"+senderNumber);
                if(senderNumber!=null){
                    if (phone.equals(senderNumber)){
                        if(progressBar!=null){
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        try{
                            forgotPasswordActivity.getContext().redirectTheClient(senderNumber);
                        }catch (Exception ex){

                        }

                    }
                }
            }

            ForgotPasswordActivity.this.unregisterReceiver(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
