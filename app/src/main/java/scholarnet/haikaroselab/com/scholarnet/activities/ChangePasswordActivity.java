package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password1;
    private EditText getPassword2;
    private ProgressBar progressBar;

    private String first;
    private String second;
    private String phone;

    private LinearLayout fakeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle(Html.fromHtml("CHANGE PASSWORD"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///getting the information from the intent;
        phone=getIntent().getStringExtra("phone");

        ///initialize all views

        password1=(EditText)findViewById(R.id.password_one);
        getPassword2=(EditText)findViewById(R.id.password_two);
        fakeButton=(LinearLayout)findViewById(R.id.button5);
        progressBar=(ProgressBar)findViewById(R.id.progressBar7);
        progressBar.setVisibility(View.INVISIBLE);

        ///getting password values

        first=password1.getText().toString().trim();
        second=getPassword2.getText().toString().trim();


        fakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ///getting password values
                first=password1.getText().toString().trim();
                second=getPassword2.getText().toString().trim();

                if(first!=null && second!=null&&phone!=null){

                    if(first.equals(second)){

                        if(first.length()>=8 && second.length()>=8){

                            ///change the password
                            goOnlineAndChangePassword(first,phone);

                        }else{
                            Snackbar.make(v,"password too short",Snackbar.LENGTH_LONG).show();
                        }
                    }else{
                        Snackbar.make(v,"password did not match",Snackbar.LENGTH_LONG).show();
                    }


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

        return true;
    }

    public void goOnlineAndChangePassword(String password,final String phone){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("password",password);
        params.put("phone",phone);

        client.get(getBaseContext(), CommonInformation.RESET_PASSWORD_URL,params,new TextHttpResponseHandler(){

            @Override
            public void onStart() {

                if(progressBar!=null){
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if(progressBar!=null){
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if(Integer.parseInt(responseString)==1){

                    Log.e("RESULTS", responseString);
                    ///return to Login Activity
                    Intent intent=new Intent(getBaseContext(),ScholarLoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Snackbar.make(progressBar,"Operation Failed, the phone you used doesnt have an existing account!!",Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if(progressBar!=null){
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(progressBar,"Operation Failed, please try again later",Snackbar.LENGTH_LONG).show();
                }


            }
        });

    }
}
