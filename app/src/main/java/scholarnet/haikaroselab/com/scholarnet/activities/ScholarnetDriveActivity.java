package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.beaPackage.Document;
import scholarnet.haikaroselab.com.scholarnet.beaPackage.DocumentAdapter;
import scholarnet.haikaroselab.com.scholarnet.beaPackage.DocumentJSONListParser;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.R;

public class ScholarnetDriveActivity extends AppCompatActivity {

    private RecyclerView documentsRecycler;
    private ProgressBar progressBar;
    private List<Document> documentList=new ArrayList<>();
    private int subjectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarnet_drive);

        String subjectCode=getIntent().getStringExtra("code");
        subjectId=getIntent().getIntExtra("subjectId",0);

        getSupportActionBar().setTitle("SDrive ("+subjectCode+")");
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////////////////doing some assaignments//////////////

        Log.e("we are good emmanuel","haha");
        documentsRecycler = (RecyclerView) findViewById(R.id.documents_recycler);
        LinearLayoutManager layout = new LinearLayoutManager(getBaseContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        documentsRecycler.setLayoutManager(layout);
        documentsRecycler.setItemAnimator(new DefaultItemAnimator());
        progressBar = (ProgressBar) findViewById(R.id.documents_progress);

        progressBar.setVisibility(View.INVISIBLE);

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("subject",Integer.toString(subjectId));

        client.get(CommonInformation.SDRIVE_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(),"process failed , please try again later",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                String responseString2="[{id:1,date:17may2006,caption:caption,url:somecrazyurl.pdf,extension:xls}," +
                        "{id:1,date:may,caption:caption,url:mcamocci.txt,extension:doc}," +
                        "{id:1,date:may,caption:caption,url:\"https:\\//pdos.csail.mit.edu\\/6.828\\/2010\\/readings\\/pointers.pdf\",extension:pdf}," +
                        "{id:1,date:may,caption:caption,url:\"http:\\//www.cse.ust.hk\\/~liao\\/comp102\\/PPT\\/array.ppt\",extension:ppt}]";

                if(responseString.length()>3){

                    Log.e("file json response",responseString);
                    documentList= DocumentJSONListParser.getAllDocuments(responseString);
                    Document document=documentList.get(0);
                    Log.e("doc",document.getCaption());
                    Log.e("doc",document.getUrl());
                    Log.e("doc",document.getDate());

                    DocumentAdapter adapter=new DocumentAdapter(getBaseContext(),documentList);
                    documentsRecycler.setAdapter(adapter);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }else if(id==R.id.upload){
            Intent intent=new Intent(getBaseContext(),DocumentsUploaderActivity.class);
            intent.putExtra("subjectId",subjectId);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sdrive_menu,menu);
        return true;
    }
}
