package scholarnet.haikaroselab.com.scholarnet.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.UserData;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;


public class ScholarSplashActivity extends AppCompatActivity {

    private  int courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserData userData=null;
        if(LoginPreferencesChecker.isLogedIn(getBaseContext())){
            userData=LoginPreferencesChecker.getUserData(getBaseContext());
            startRequest(userData.getPhone(), userData.getPassword());
        }else{
            Thread splash=new Thread(){
                public void run(){
                    try{
                        Thread.sleep(4000);
                        Intent intent=new Intent(getBaseContext(),ScholarLoginActivity.class);
                        startActivity(intent);
                        Log.e("naenda","loging the user");
                        finish();
                    }catch(InterruptedException ex){
                        ex.getMessage();
                    }
                }
            };
            splash.start();
        }
    }

    public void startRequest(final String phone,final String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.setTimeout(15000);
        client.setConnectTimeout(15000);
        params.put("username",phone);
        params.put("password", password);
        client.get(getBaseContext(), CommonInformation.LOGIN_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //when failed due to connection show not internet activity
                Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
                Log.e("nina","nina taarifa nimeshindwa");
                Log.e("phone",phone + "namba");
                Log.e("password", password);
                intent.putExtra("username",phone);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    courseId = Integer.parseInt(responseString);
                    Log.e("id",Integer.toString(courseId));
                } catch (Exception ex) {
                    courseId = 0;
                }
                if (courseId != 0) {
                    Intent intent = new Intent(getBaseContext(), FirstActivity.class);
                    intent.putExtra("id", courseId);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(getBaseContext(),ScholarLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
