package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;

public class ProfileActivity extends AppCompatActivity {

    private TextView username;
    private TextView yearvalue;
    private EditText year;

    private LayoutInflater inflater;
    private PopupWindow popupWindow;
    private View popUpWindowView;

    ImageView fakeChangeButton;
    ImageView fakeChangeButtonYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView close=(ImageView)toolbar.findViewById(R.id.back);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ///initializing some views
        username=(TextView)findViewById(R.id.change_name);
        username.setText(LoginPreferencesChecker.logEventGetUser(getBaseContext()));
        fakeChangeButton=(ImageView)findViewById(R.id.name);
        fakeChangeButtonYear=(ImageView)findViewById(R.id.year);
        yearvalue=(TextView)findViewById(R.id.change_year);

        yearvalue.setText(LoginPreferencesChecker.logEventUserGetStoredYear(getBaseContext()));

        fakeChangeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //am ready for popup menu emmanuel

                inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popUpWindowView = inflater.inflate(R.layout.activity_change_username, null);
                popupWindow = new PopupWindow(popUpWindowView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                final TextView save= (TextView) popUpWindowView.findViewById(R.id.save);
                final EditText enteredName=(EditText)popUpWindowView.findViewById(R.id.name);
                TextView discard = (TextView) popUpWindowView.findViewById(R.id.discard);

                //showing the popupwindow at the center
                popupWindow.showAtLocation(v,Gravity.CENTER,0,0);

                ///setting the listeners for edit and deleting//////////////////////////////////////

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name=enteredName.getText().toString().trim();
                        if(name.length()>=1){
                            ChangeDisplayName(LoginPreferencesChecker.logEventGetPhone(getBaseContext()),name);
                            username.setText(name);
                            popupWindow.dismiss();
                        }else{
                            Toast.makeText(getBaseContext(),"empty value",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        fakeChangeButtonYear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //am ready for popup menu emmanuel

                inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popUpWindowView = inflater.inflate(R.layout.activity_change_year, null);
                popupWindow = new PopupWindow(popUpWindowView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                final TextView save= (TextView) popUpWindowView.findViewById(R.id.save);
                final EditText enteredyear=(EditText)popUpWindowView.findViewById(R.id.year);
                TextView discard = (TextView) popUpWindowView.findViewById(R.id.discard);

                //showing the popupwindow at the center
                popupWindow.showAtLocation(v,Gravity.CENTER,0,0);

                ///setting the listeners for edit and deleting//////////////////////////////////////

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String year=enteredyear.getText().toString().trim();
                        if(year.length()==1){

                            try{

                                int yearValue=Integer.parseInt(year);
                                if(yearValue>0 && yearValue<5){
                                    changeYear(LoginPreferencesChecker.logEventGetPhone(getBaseContext()), year);
                                    Toast.makeText(getBaseContext(),
                                            LoginPreferencesChecker.logEventGetPhone(getBaseContext())+
                                            year,Toast.LENGTH_LONG).show();
                                    LoginPreferencesChecker.logEventUserStoreYearAndStoreUserAndStoreCourseId(
                                            getBaseContext(),
                                            LoginPreferencesChecker.logEventGetPhone(getBaseContext()),
                                            LoginPreferencesChecker.logEventGetUser(getBaseContext()),
                                            Integer.toString(yearValue),
                                            Integer.parseInt(LoginPreferencesChecker.logEventGetCourseId(getBaseContext())
                                            ));
                                    Log.e("year value is ",Integer.toString(yearValue));
                                    Log.e("the store year is ", year);
                                    yearvalue.setText(year);
                                    popupWindow.dismiss();
                                }else{
                                    Toast.makeText(getBaseContext(),"invalid value",Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception ex){
                                Toast.makeText(getBaseContext(),"invalid value",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(getBaseContext(),"invalid",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
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

    public void changeYear(final String phone, final String year){


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("year",year);

        client.get(getBaseContext(), CommonInformation.SET_YEAR_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onStart(){  }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String code=responseString;
                if(responseString.equals("1")){
                    //LoginPreferencesChecker.logEventUserStore(getBaseContext(),phone,year);
                }
            }
        });

    }

    public void ChangeDisplayName(final String phone, final String name){


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("display",name);

        client.get(getBaseContext(),CommonInformation.CHANGE_DISPLAY_NAME, params, new TextHttpResponseHandler() {
            @Override
            public void onStart(){  }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String code=responseString;
                if(responseString.equals("1")){
                    LoginPreferencesChecker.logEventStoreUser(getBaseContext(), phone, name);
                }
            }
        });

    }
}
