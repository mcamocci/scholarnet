package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UsefullyFunctions;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.UserYearCourseId;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UserYearCourseIdItemParser;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;

public class NoConnectionActivity extends AppCompatActivity {

    private TextView retrybutton;
    private ProgressBar progressBar;
    private String username;
    private String password;
    private int courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        getSupportActionBar().hide();

        username=getIntent().getStringExtra("username");
        password=getIntent().getStringExtra("password");
        progressBar=(ProgressBar)findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);

        retrybutton=(TextView)findViewById(R.id.retry);
        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                retryTheProcess(username, password);
            }
        });
    }


    public void retryTheProcess(final String username, final String password){

            Log.e("some process","am starting emmanuel");
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(20 * 1000);
            client.setConnectTimeout(20*1000);
            client.setResponseTimeout(20*1000);
            RequestParams params = new RequestParams();
            params.put("username", username);
            params.put("password",password);

            Log.e("phone",username);
            Log.e("pass", Integer.toString(password.length()));

            client.get(getBaseContext(), CommonInformation.LOGIN_URL, params, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("sina", "sina taarifa na nimeshindwa");
                    Log.e("error code", throwable.toString());
                    Log.e("sgag", Integer.toString(statusCode));

                    if(throwable.toString().contains("java.net.SocketTimeoutException")){
                        Log.e("trial","we are trying emmanuel");
                        retryTheProcess(username, password);
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        //when failed due to connection show not internet activity
                        Intent intent = new Intent(getBaseContext(), NoConnectionActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    Log.e("response",responseString);
                    Log.e("najaribu","mafanikio");
                    Log.e("size",Integer.toString(password.length()));

                    try {
                        courseId = Integer.parseInt(responseString);
                    } catch (Exception ex) {
                        courseId = 0;
                    }
                    if (courseId != 0) {
                        Log.e("no comment","we won emmanuel");
                        //retrieving the user
                        getUserName(username);
                        Intent intent = new Intent(getBaseContext(), FirstActivity.class);
                        intent.putExtra("id", courseId);
                        ///hey this for storing login information (phone) of the user////////////////////////////////////////////
                        LoginPreferencesChecker.logUser(UsefullyFunctions.getPhone(username),
                                password, getBaseContext());
                        LoginPreferencesChecker.logEvent(getBaseContext(), UsefullyFunctions.getPhone(username));
                        Log.e("logged","i have logged in");
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        startActivity(intent);
                        finish();
                    }else{
                        Log.e("no comment",Integer.toString(courseId));
                    }
                }
            });
    }

    public void getUserName(final String phone){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user",phone);

        client.get(getBaseContext(), CommonInformation.DISPLAY_NAME_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(getBaseContext(), "connection failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Log.e("response of the user name",responseString);
                UserYearCourseId userYearCourseId = UserYearCourseIdItemParser.getUserYear(responseString);
                LoginPreferencesChecker.logEventUserStoreYearAndStoreUserAndStoreCourseId(NoConnectionActivity.this, LoginPreferencesChecker.logEventGetPhone(getBaseContext()), userYearCourseId.getName(),
                        Integer.toString(userYearCourseId.getYear()),userYearCourseId.getCourseId());

            }
        });
    }

}
