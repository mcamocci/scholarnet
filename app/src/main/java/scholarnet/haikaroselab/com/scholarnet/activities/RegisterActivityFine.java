package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CollegeItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.CollegeListJsonParser;

public class RegisterActivityFine extends AppCompatActivity {

    private View viewAnyWhere;
    private boolean notSupported=true;
    ///some adapters
    ArrayAdapter collegeAdapter;

    private  int superCollegeId;
    int yearValue;
    private List<CollegeItem> superCollegeContainer=new ArrayList<>();

    private AutoCompleteTextView year_text_view;
    private AutoCompleteTextView role_text_view;
    private LinearLayout nextButton;
    private AutoCompleteTextView college_text_view;
    private AutoCompleteTextView course_text_view;
    private AutoCompleteTextView username_text_view;
    private ProgressBar progressBar;
    private List<String> collegeContainer = new ArrayList<>();

    //////////////////////this is new programming approach to follow //////////////////////////////////
    private LinearLayout firstLoad;
    private LinearLayout collegeLoadProgress;
    private LinearLayout firstContainer;
    private CardView secondContainer;
    private TextView waitingTxt;
    private ProgressBar waitingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_register);

        ///////////////////////////Loading new approach////////////////////////////////////////////
        firstContainer=(LinearLayout)findViewById(R.id.firstcontent); firstContainer.setVisibility(View.INVISIBLE);
        secondContainer=(CardView)findViewById(R.id.second_content); secondContainer.setVisibility(View.INVISIBLE);

        waitingTxt=(TextView)findViewById(R.id.waiting_text);
        waitingProgress=(ProgressBar)findViewById(R.id.waiting_progress);

        ////////////getting all the registered college information/////////////////////////////////
        getImportantCollegeInfo();
        ///////////////////////////////////////////////////////////////////////////////////////////


        ///initializing the progressbars
        firstLoad=(LinearLayout)findViewById(R.id.firstLoad);
        collegeLoadProgress=(LinearLayout)findViewById(R.id.collegeLoad);

        if(collegeContainer.size()<1){
            getImportantCollegeInfo();
        }else{
            collegeLoadProgress.setVisibility(View.INVISIBLE);
        }

        String[] year = getResources().getStringArray(R.array.year);
        String[] role = getResources().getStringArray(R.array.role);

        getSupportActionBar().setTitle(Html.fromHtml("REGISTER"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter yearAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, year);
        ArrayAdapter roleAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, role);
        AdapterAuto();//this does as above

        ///////////////////////////////////seting input views/////////////////////////////////////////

        progressBar = (ProgressBar) findViewById(R.id.progressBar_register);
        year_text_view = (AutoCompleteTextView) findViewById(R.id.year_view);
        role_text_view = (AutoCompleteTextView) findViewById(R.id.role_view);
        nextButton=(LinearLayout)findViewById(R.id.next);

        college_text_view = (AutoCompleteTextView) findViewById(R.id.college_view);
        course_text_view = (AutoCompleteTextView) findViewById(R.id.course_view);
        username_text_view = (AutoCompleteTextView) findViewById(R.id.username_view);

        /////////////////////////////////////setting adapter when necessary////////////////////////////////
        year_text_view.setAdapter(yearAdapter);
        role_text_view.setAdapter(roleAdapter);
        college_text_view.setAdapter(collegeAdapter);


        ////////////////////////validating the year/////////////////////////////////////////////////////

        year_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    try {
                        if (Integer.parseInt(year_text_view.getText().toString()) > 5) {
                            year_text_view.setText("");

                        } else {
                            year_text_view.setTextColor(Color.BLACK);

                        }
                    } catch (Exception ex) {
                        year_text_view.setText("");

                    }

                }
            }
        });


        //////////////////////looking for course information/////////////////////////

        college_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }
        });

        ///moving to the next registration screen////////////////////////////////////////////

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(notSupported){
                    if(everythigIsValid(v)){

                        for(CollegeItem item:RegisterActivityFine.this.superCollegeContainer) {

                            if (item.getName().contains(college_text_view.getText().toString().trim())) {

                                RegisterActivityFine.this.superCollegeId = item.getId();
                            }
                        }
                        Intent intent=new Intent(RegisterActivityFine.this,RegisterSecondActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("username",username_text_view.getText().toString().trim());
                        bundle.putInt("college", superCollegeId);
                        bundle.putInt("year",Integer.parseInt(year_text_view.getText().toString().trim()));
                        bundle.putString("role", role_text_view.getText().toString().trim());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }else if(id==R.id.retry_id){
            secondContainer.setVisibility(View.GONE);
            getImportantCollegeInfo();
        }
        return true;
    }

    ///method for getting some important data

    public void getImportantCollegeInfo() {

        //getting some college information

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getBaseContext(), CommonInformation.COLLEGE_URL, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                firstLoad.setVisibility(View.VISIBLE);
                firstContainer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               Log.e("register","connection failed in the register");
               Toast.makeText(getBaseContext(),"try again later",Toast.LENGTH_LONG).show();
                waitingProgress.setVisibility(View.GONE);
                waitingTxt.setText("please try again later , there is poor connection between you and our servers");
                firstContainer.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                firstLoad.setVisibility(View.GONE);
                String response = new String(responseString);

                List<CollegeItem> collegeItems = CollegeListJsonParser.parseJson(response);

                RegisterActivityFine.this.superCollegeContainer=collegeItems;
                for (CollegeItem item : collegeItems) {
                    String name = item.getName();
                    RegisterActivityFine.this.collegeContainer.add(name);
                }

                firstContainer.setVisibility(View.VISIBLE);
                secondContainer.setVisibility(View.VISIBLE);


            }
        });
    }


    public void AdapterAuto(){
        collegeAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, RegisterActivityFine.this.collegeContainer);
    }

    public boolean everythigIsValid(View view){

        String user_name=username_text_view.getText().toString().trim();
        String college=college_text_view.getText().toString().trim();
        try{
            yearValue=Integer.parseInt(year_text_view.getText().toString().trim());

        }catch (Exception ex){

            if(year_text_view.getText().toString().isEmpty() || year_text_view.getText().toString()
                    ==null)
            Snackbar.make(view, "year value is invalid", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;

        }

        String role=role_text_view.getText().toString().trim();

        if(user_name.length()<4 || college.isEmpty()||college==null|| role.isEmpty()
                ||yearValue<1 || yearValue>5){


            Log.e("register",user_name);
            Log.e("register",college);

            if(user_name.isEmpty()||user_name==null){
                Snackbar.make(view, "user name cant be null", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
            return  false;
        }else{

         return true;
        }

    }



}
