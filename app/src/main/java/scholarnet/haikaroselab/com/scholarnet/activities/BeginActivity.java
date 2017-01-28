package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.*;
import android.view.MenuItem;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.CorrectSubjectsItemAdapter;
import scholarnet.haikaroselab.com.scholarnet.DrawerMenuFragment;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.SubjectItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.SubjectListJSONParser;

public class BeginActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ProgressBar progressBar;
    private TextView emptyMessage;
    RecyclerView subjectsRecycler;
    private String courseId;
    private LayoutInflater inflater;
    private PopupWindow popupWindow;
    private View popUpWindowView;


    //ArrayList<String> subjects=new ArrayList<>();
    List<SubjectItem> subjectsItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar5);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<b>HOME</b>"));


        ///getting the course id intent
        courseId = Integer.toString(getIntent().getIntExtra("id", 0));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        subjectsRecycler = (RecyclerView) findViewById(R.id.subject_recyler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        subjectsRecycler.setItemAnimator(new DefaultItemAnimator());
        subjectsRecycler.setLayoutManager(layoutManager);
        emptyMessage = (TextView) findViewById(R.id.textView8);

        //getting the subjects online directly
        getAllSubjects();

        DrawerMenuFragment fragment = (DrawerMenuFragment) getSupportFragmentManager().findFragmentById(R.id.frag_container);
        fragment.setDrawer(drawerLayout, toolbar);

    }


    public void getAllSubjects() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("course", courseId);
        params.put("user", LoginPreferencesChecker.logEventGetPhone(getBaseContext()));

        client.get(getBaseContext(), CommonInformation.SUBJECT_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                subjectsItems = SubjectListJSONParser.getAllArticle(responseString);

                if(responseString.length()>5){
                    if (subjectsItems != null) {
                        CorrectSubjectsItemAdapter adapter = new CorrectSubjectsItemAdapter(getBaseContext(), subjectsItems);
                        subjectsRecycler.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyMessage.setVisibility(View.INVISIBLE);
                        // ScholarNetDatabase database = new ScholarNetDatabase(getBaseContext());
                        // database.insertSubjects(subjectsItems);
                    }
                }
                else {
                    emptyMessage.setText("No subject have been registered for your course");
                    emptyMessage.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.begin_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out) {

            //am ready for popup menu emmanuel
            inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popUpWindowView = inflater.inflate(R.layout.activity_logout_confirm, null);
            popupWindow = new PopupWindow(popUpWindowView,
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            TextView accept = (TextView) popUpWindowView.findViewById(R.id.accept);
            TextView discard = (TextView) popUpWindowView.findViewById(R.id.discard);

            //showing the popupwindow at the center
            popupWindow.showAtLocation(emptyMessage,Gravity.CENTER,0,0);

            ///setting the listeners for edit and deleting//////////////////////////////////////

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    LoginPreferencesChecker.clearLoggInfo(getBaseContext());
                    Intent intent=new Intent(BeginActivity.this,ScholarLoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
            discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                }
            });
        }
        return true;
    }

    @Override
    protected void onResume() {
        getAllSubjects();
        super.onResume();
    }
}