package scholarnet.haikaroselab.com.scholarnet.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.*;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;

public class ReportProblemActivity extends AppCompatActivity {

    EditText problem;
    LinearLayout submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        getSupportActionBar().setTitle(Html.fromHtml("CONTACT US</b"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        problem=(EditText)findViewById(R.id.text_problem);
        submit_button=(LinearLayout)findViewById(R.id.button_submit);


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reported_problem=problem.getText().toString().trim();

                if(reported_problem.length()>=10){

                    startRequest(reported_problem);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+255687374009", null,"error report: "+reported_problem, null, null);

                    Snackbar.make(v, "Operation carried", Snackbar.LENGTH_LONG).show();
                    problem.setText("");
                    // .setAction("DELETE", aclist).setActionTextColor(Color.parseColor(Colors.danger))
                    // .show();
                }else{
                    Snackbar.make(v, "Operation Failed", Snackbar.LENGTH_LONG).show();
                    problem.setText("");
                }
        }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return true;
    }

    public void startRequest(String message) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user", LoginPreferencesChecker.logEventGetPhone(getBaseContext()));
        params.put("problem",message);

        client.get(getBaseContext(), CommonInformation.SEND_PROBLEM_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });

    }
}
