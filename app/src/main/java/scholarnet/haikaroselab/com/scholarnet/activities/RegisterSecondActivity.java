package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CourseItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.CourseListJSONParser;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;

public class RegisterSecondActivity extends AppCompatActivity {

    private AutoCompleteTextView password_text_view_one;
    private AutoCompleteTextView password_text_view_tw0;
    private AutoCompleteTextView phone_text_view_one;
    private AutoCompleteTextView phone_text_view_two;
    private LinearLayout registerButton;

    private String user_name;
    private int college_id;
    private int course_id;
    private int year;
    private String role;


    ////the new approach to the course list/////////////////////////////////////////////////////////
    private Spinner courseSpiner;
    ArrayAdapter courseAdapter;
    private List<String> courseContainer = new ArrayList<>();
    private List<CourseItem> superCourseContainer=new ArrayList<>();

    private LinearLayout containerOne;
    private LinearLayout containerTwo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);

        ///////////////////////////////////receiving values sent by the intent//////////////////////////

        Bundle bundle=getIntent().getExtras();
        user_name=bundle.getString("username");
        college_id=bundle.getInt("college");
        Log.e("college id is ",Integer.toString(college_id));
        year=bundle.getInt("year");
        role=bundle.getString("role");

        ///////////////////////////////initialization of some components new approach///////////////
        containerOne=(LinearLayout)findViewById(R.id.container_one);
        containerTwo=(LinearLayout)findViewById(R.id.container_two);
        containerTwo.setVisibility(View.INVISIBLE);

        ///////////////////////getting all the course //////////////////////////////////////////////
        getImportantCourseInfo();
        /////////////////////////////new approach course////////////////////////////////////////////
        courseSpiner=(Spinner)findViewById(R.id.course_spinner);

        ////////////////////////////////////////////////////////////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("REGISTER"));

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////////////////////////////////seting input views/////////////////////////////////////////
        phone_text_view_one = (AutoCompleteTextView) findViewById(R.id.phone_view);
        phone_text_view_two = (AutoCompleteTextView) findViewById(R.id.phone_view_1);
        password_text_view_one = (AutoCompleteTextView) findViewById(R.id.password_view);
        password_text_view_tw0 = (AutoCompleteTextView) findViewById(R.id.password_view_1);

        ////////////////////////validating the phone/////////////////////////////////////////////////////

        phone_text_view_one.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (phone_text_view_one.getText().length() > 13 || phone_text_view_one.getText().length() < 10) {
                        phone_text_view_one.setText("");
                        phone_text_view_one.setGravity(Gravity.CENTER);
                    } else {
                        phone_text_view_one.setGravity(Gravity.CENTER);
                    }

                }
            }
        });

        phone_text_view_two.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!(phone_text_view_one.getText().toString().equals(phone_text_view_two.getText().toString()))) {
                        phone_text_view_two.setText("");
                        phone_text_view_two.setGravity(Gravity.CENTER);
                    }

                }
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////


        registerButton = (LinearLayout) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(CourseItem course:RegisterSecondActivity.this.superCourseContainer){

                    if(course.getName().contains(courseSpiner.getSelectedItem().toString())){
                        RegisterSecondActivity.this.course_id=course.getId();

                    }
                }
                if(passedPasswordAndPhone()){

                    dataIsValidGoOnNet(user_name,Integer.toString(college_id),Integer.toString(RegisterSecondActivity.this.course_id),Integer.toString(year),role,phone_text_view_one.getText().toString().trim(),
                            password_text_view_one.getText().toString().trim());
                    Snackbar.make(view, "register process started", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(view, "Invalid information", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }


    ///method for doing registration of the user


    public void dataIsValidGoOnNet(String user_name, String college, String course,
                                   String year, String role, String phone, String password
    ) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", user_name);
        params.put("college", college_id);
        params.put("course",course_id);
        params.put("year", year);
        params.put("role", role.toLowerCase());
        params.put("phone", phone);
        params.put("password", password);

        client.get(getBaseContext(), CommonInformation.REGISTRATION_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               Log.e("register2", "Failed in the register2");
                Toast.makeText(getBaseContext(),"Registration Failed, try later",Toast.LENGTH_LONG).show();
                finish();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if(responseString.equalsIgnoreCase("1")){
                    Log.e("Passed", "regitration");
                    Toast.makeText(getBaseContext(),"Welcome to Scholarnet",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getBaseContext(),ScholarLoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

        });

    }
    public boolean passedPasswordAndPhone(){

        if(phone_text_view_one.getText().toString().trim().equals(phone_text_view_two.getText().toString()
                .trim())&& password_text_view_one.getText().toString().trim().equals(password_text_view_tw0.getText().toString().trim())
                ){

            if(password_text_view_one.getText().toString().trim().length()>=8){
                String phone=phone_text_view_one.getText().toString().trim();
                String password=password_text_view_one.getText().toString().trim();


                return true;
            }

        }

        return false;
    }

    public void AdapterAuto(){

        courseAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1,RegisterSecondActivity.this.courseContainer);
        courseSpiner.setAdapter(courseAdapter);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void getImportantCourseInfo() {


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("college",college_id);
        Log.e("course id is",Integer.toString(college_id));


        client.get(getBaseContext(), CommonInformation.COURSE_URL,params,new TextHttpResponseHandler() {

            @Override
            public void onStart() {

                Log.e("register","process started in the register");
                containerTwo.setVisibility(View.GONE);
                containerOne.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(getBaseContext(),"try again later please",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                List<CourseItem> courseItems = CourseListJSONParser.getCourseList(responseString);

                Log.e("course id is",Integer.toString(college_id));


                for (CourseItem course : courseItems) {

                    String value = course.getName();
                    RegisterSecondActivity.this.courseContainer.add(value);

                }
                RegisterSecondActivity.this.superCourseContainer=courseItems;

                try{
                    courseItems.get(0).getId();
                }catch (Exception ex){

                }
                AdapterAuto();
                containerTwo.setVisibility(View.VISIBLE);
                containerOne.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }else if(id==R.id.retry_id){
            getImportantCourseInfo();
        }
        return true;
    }

}
