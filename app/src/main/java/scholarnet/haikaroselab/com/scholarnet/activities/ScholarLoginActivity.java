package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.entity.StringEntity;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.ConnectionAvaillable;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UsefullyFunctions;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.UserYearCourseId;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UserYearCourseIdItemParser;
import scholarnet.haikaroselab.com.scholarnet.constants.Colors;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;


public class ScholarLoginActivity extends AppCompatActivity {


    private AutoCompleteTextView phone_number_view;
    private EditText password_view;
    private ProgressBar progressBar;
    private TextView signup;
    private int courseId;
    private View anyWhereView;
    private List<Cookie> cookies;

    private boolean valid_data = false;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ///using custom fonts android
        Typeface scholarnet=Typeface.createFromAsset(getAssets(),"google_font_2.ttf");
        TextView title=(TextView)findViewById(R.id.textView9);
        title.setTypeface(scholarnet);

        //////////////////////////////////////////////////////////////////////////////////

        // Set up the login form items//////////////////////////////////////////////////////
        phone_number_view = (AutoCompleteTextView) findViewById(R.id.number);
        password_view = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.sign_up_text);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        progressBar.setVisibility(View.INVISIBLE);
        forgotPassword=(TextView)findViewById(R.id.forgot_password_text);

        /////////////////////////user forgot the password//////////////////////////////////
        forgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (ConnectionAvaillable.isInternetConnected(getBaseContext())) {
                    Intent intent=new Intent(getBaseContext(),ForgotPasswordActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(v, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

//////////////////////////////////////validation is done here/////////////////////////////////////////

        phone_number_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {


                } else {
                    if (phone_number_view.getText().length() > 13 || phone_number_view.getText().length() < 10) {


                        if (!phone_number_view.getText().toString().trim().isEmpty()
                                || phone_number_view.getText().toString().trim() == null
                                || phone_number_view.getText().toString().trim().length() > 0) {

                            if (phone_number_view.getText().toString().trim().length() > 10 && phone_number_view
                                    .getText().toString().trim().indexOf("+") == -1) {
                                phone_number_view.setText("wrong input");
                                phone_number_view.setTextColor(Color.parseColor(Colors.danger));
                                ScholarLoginActivity.this.valid_data = false;
                            }
                        }

                        ScholarLoginActivity.this.valid_data = false;
                    } else {
                        phone_number_view.setTextColor(Color.BLACK);
                        ScholarLoginActivity.this.valid_data = true;
                    }
                }
            }
        });

////////////////////sign up button//////////////////////////////////////////////////////////////////

        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
   ;
                if (ConnectionAvaillable.isInternetConnected(getBaseContext())) {
                    Intent intent = new Intent(ScholarLoginActivity.this, RegisterActivityFine.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(v, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        });

/////////////////////////////////////////////////Login Button/////////////////////////////////////////////

        final LinearLayout mEmailSignInButton = (LinearLayout) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                ///close the idiotic keyboard
                View funcyView=ScholarLoginActivity.this.getCurrentFocus();
                InputMethodManager iMmanager=(InputMethodManager)getSystemService((getBaseContext().INPUT_METHOD_SERVICE));
                iMmanager.hideSoftInputFromWindow(funcyView.getWindowToken(),0);

                if (phone_number_view.getText().toString().trim().contains("wrong input") || password_view.
                        getText().toString().trim().isEmpty() || password_view.getText().toString().trim()
                        .length() < 8 ||
                        phone_number_view.getText().toString().length() < 10
                        ||
                        phone_number_view.getText().toString().length() > 13
                        ||
                        phone_number_view.getText().toString().length() > 10 && phone_number_view.getText()
                                .toString().indexOf("+") == -1
                        ||
                        phone_number_view.getText().toString().length() == 13 && phone_number_view.getText()
                                .toString().indexOf("+") == -1
                        || !isPhone(phone_number_view.getText().toString().trim()) && phone_number_view.getText()
                        .toString().trim().length() == 10
                        ) {

                    phone_number_view.setText("");
                    phone_number_view.setTextColor(Color.RED);
                    password_view.setText("");

                    Snackbar.make(view, "Wrong input combination", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    if (ConnectionAvaillable.isInternetConnected(getBaseContext())) {

                        startRequest(view);

                    } else {
                        Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void startRequest(View view) {
        anyWhereView = view;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", phone_number_view.getText().toString().trim());
        params.put("password", password_view.getText().toString().trim());

        ////this is where the cookie belongs!!
        /////////working with cookie from the server/////////////////////////////////

        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        client.setConnectTimeout(15000);
        client.setTimeout(15000);

        ////////////////////////////////////////////////////////////////////////////

        cookies=myCookieStore.getCookies();

        //////now is the time to work with the json objects////////////////////////////////////////

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("username", phone_number_view.getText().toString().trim());
            jsonObject.put("password", password_view.getText().toString().trim());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ///entity comes here
        StringEntity entity=null;
        try {
            entity=new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////


        client.get(getBaseContext(), CommonInformation.LOGIN_URL, params, new TextHttpResponseHandler(){

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                   courseId = Integer.parseInt(responseString);
                } catch (Exception ex) {
                    courseId = 0;
                }
                if (courseId != 0) {

                    //retrieving the user
                    getUserName(phone_number_view.getText().toString().trim());
                    Intent intent = new Intent(ScholarLoginActivity.this, FirstActivity.class);
                    intent.putExtra("id", courseId);
                    ///hey this for storing login information (phone) of the user////////////////////////////////////////////
                    LoginPreferencesChecker.logUser(UsefullyFunctions.getPhone(phone_number_view.getText().toString().trim()),
                            password_view.getText().toString().trim(), getBaseContext());
                    LoginPreferencesChecker.logEvent(ScholarLoginActivity.this, UsefullyFunctions.getPhone(phone_number_view.getText().toString().trim()));

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    startActivity(intent);
                    finish();
                } else {

                    password_view.setTextColor(Color.RED);
                    phone_number_view.setTextColor(Color.RED);
                    progressBar.setVisibility(View.INVISIBLE);

                    Snackbar.make(anyWhereView, "wrong information", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getBaseContext(),"Please try again later",Toast.LENGTH_LONG).show();
                //Log.e("status code, net fail scholar login", Integer.toString(statusCode));
                Log.e("ney",throwable.toString());
              }

        });

    }

    public boolean isPhone(String phone) {
        try {
            int number = Integer.parseInt(phone);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean isPhoneAgain(String phone) {

        String string = phone.substring(1, 12);
        try {
            int integer = Integer.parseInt(string);
        } catch (Exception ex) {
            return false;
        }
        return true;
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
                Toast.makeText(getBaseContext(), "process failed", Toast.LENGTH_LONG).show();
                Log.e("retrieved", LoginPreferencesChecker.logEventGetCourseId(getBaseContext()));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
               // Log.e("response of the user name",responseString);
                UserYearCourseId userYearCourseId = UserYearCourseIdItemParser.getUserYear(responseString);
                LoginPreferencesChecker.logEventUserStoreYearAndStoreUserAndStoreCourseId(ScholarLoginActivity.this, LoginPreferencesChecker.logEventGetPhone(getBaseContext()), userYearCourseId.getName(),
                        Integer.toString(userYearCourseId.getYear()),userYearCourseId.getCourseId());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

