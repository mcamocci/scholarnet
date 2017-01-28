package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import scholarnet.haikaroselab.com.scholarnet.beaPackage.SimpleFragmentPagerAdapter;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;

/**
 * Created by root on 4/24/16.
 */
public class FirstActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private PopupWindow popupWindow;
    private View popUpWindowView;
    private Toolbar toolbar;
    private String courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //getting all necessary intents
        ///getting the course id intent
        courseId = Integer.toString(getIntent().getIntExtra("id", 0));


        toolbar=(Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Scholarnetapp</font>"));
        setSupportActionBar(toolbar);

        //end of setting color
        toolbar.setTitleTextColor(Color.WHITE);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                getBaseContext(),courseId));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out) {

            //am ready for popup menu emmanuel
            inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popUpWindowView = inflater.inflate(R.layout.activity_logout_confirm, null);
           // popUpWindowView.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadein));
            popupWindow = new PopupWindow(popUpWindowView,
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.Animation);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            TextView accept = (TextView) popUpWindowView.findViewById(R.id.accept);
            TextView discard = (TextView) popUpWindowView.findViewById(R.id.discard);

            //showing the popupwindow at the center
            popupWindow.showAtLocation(toolbar, Gravity.CENTER,0,0);

            ///setting the listeners for edit and deleting//////////////////////////////////////

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    LoginPreferencesChecker.clearLoggInfo(getBaseContext());
                    Intent intent=new Intent(getBaseContext(),ScholarLoginActivity.class);
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

        }else if (id == R.id.about){
            appStarted(toolbar);
        }
        else if (id == R.id.profile){
            Intent intent=new Intent(getBaseContext(),ProfileActivity.class);
            startActivity(intent);

        }else if (id == R.id.report){
            Intent intent=new Intent(getBaseContext(),ReportProblemActivity.class);
            startActivity(intent);

        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;
    }

    //about
    public void appStarted(View view){

        inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpWindowView = inflater.inflate(R.layout.welcome, null);

        popupWindow = new PopupWindow(popUpWindowView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

    }


}