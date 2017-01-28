package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.activities.ScholarnetDriveActivity;
import scholarnet.haikaroselab.com.scholarnet.adapters.ArticleAdapter;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ArticleItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.ArticleListJSONParser;
import scholarnet.haikaroselab.com.scholarnet.constants.Colors;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.activities.ShareActivity;

public class ModulePostActivityFun extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArticleAdapter adapter;
    private int page=1;

    View wholeLayerRetry;
    private int subjectId;
    private RecyclerView recyclerView;
    private TextView noContent;

    private List<ArticleItem> allArticles=new ArrayList<>();
    private List<ArticleItem> moreItems=new ArrayList<>();
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        //adapter initialization is done here
        recyclerView=(RecyclerView)findViewById(R.id.article_list);
        adapter=new ArticleAdapter(getBaseContext(),allArticles);
        recyclerView.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////////////////////////////
        subjectId=getIntent().getIntExtra("id",0);
        title=getIntent().getStringExtra("title");
        ////////////////////////////////////////////////////////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml(title.toUpperCase()));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        customLoadMoreDataFromApi(page);

        wholeLayerRetry=(View)findViewById(R.id.textView17);
        wholeLayerRetry.setVisibility(View.INVISIBLE);

        ////what happen when whole retry gets clicked?

        wholeLayerRetry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                customLoadMoreDataFromApi(page);
            }
        });

        progressBar=(ProgressBar)findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);
        //this code is special for the progressbar only
        progressBar.getIndeterminateDrawable().setColorFilter(
                Color.parseColor(Colors.primary), android.graphics.PorterDuff.Mode.SRC_IN);

        noContent=(TextView)findViewById(R.id.textView12);
        noContent.setVisibility(View.INVISIBLE);

        LinearLayoutManager layout = new LinearLayoutManager(getBaseContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                ModulePostActivityFun.this.page = page;
                customLoadMoreDataFromApi(page);
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),ShareActivity.class);
                intent.putExtra("id",subjectId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.explore_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }else if(id==R.id.task){
            Intent intent=new Intent(getBaseContext(),ScholarnetDriveActivity.class);
            intent.putExtra("code",title);
            intent.putExtra("subjectId",subjectId);
            startActivity(intent);
        }
        return true;
    }
    public List<ArticleItem> requestsArticles(int offset){

        List<ArticleItem> articleItems=new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20 * 1000);
        client.setConnectTimeout(20 * 1000);
        client.setResponseTimeout(20 * 1000);
        RequestParams params = new RequestParams();
        params.put("id",Integer.toString(subjectId));
        params.put("offset", offset);

        Log.e("subject id is :", Integer.toString(subjectId));

        client.get(getBaseContext(), CommonInformation.ARTICLE_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    noContent.setVisibility(View.INVISIBLE);
                    wholeLayerRetry.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("error message", throwable.toString());
                wholeLayerRetry.setVisibility(View.VISIBLE);

                ///setting listeners for the retry
                LinearLayout fakeButton = (LinearLayout) wholeLayerRetry.findViewById(R.id.amfake);
                //listeners for retry button
                fakeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("dont ", " i will work forever worry");
                        requestsArticles(page);
                    }
                });

                if(allArticles.size()<1){
                    noContent.setText("could not connect to the internet , please try again later");
                    noContent.setVisibility(View.VISIBLE);
                }else{
                    noContent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if (responseString.length() > 5) {

                    List<ArticleItem> downloadedArticle = ArticleListJSONParser.getAllArticle(responseString);

                    if (downloadedArticle.size() > 0) {

                        allArticles.addAll(downloadedArticle);
                        final int curSize = adapter.getItemCount();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemRangeInserted(curSize, allArticles.size() - 1);
                            }
                        });
                    } else {
                        Log.e("hehe", "we are not good");
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                    wholeLayerRetry.setVisibility(View.INVISIBLE);
                    noContent.setVisibility(View.INVISIBLE);

                } else {
                    if(responseString.contains("[]")){

                        progressBar.setVisibility(View.INVISIBLE);
                        noContent.setVisibility(View.INVISIBLE);

                        if(allArticles.size()<1){
                            wholeLayerRetry.setVisibility(View.VISIBLE);
                            noContent.setText("Content not availlable for this course, to publish content please click the edit button bellow");
                            noContent.setVisibility(View.VISIBLE);
                        }else{
                            noContent.setVisibility(View.INVISIBLE);
                            wholeLayerRetry.setVisibility(View.INVISIBLE);
                        }

                        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No content to load",Snackbar.LENGTH_LONG);
                        View view=snackbar.getView();
                        view.setBackgroundColor(Color.parseColor(Colors.primary));
                        TextView snackText=(TextView)view.findViewById(android.support.design.R.id.snackbar_text);
                        snackText.setGravity(Gravity.CENTER_HORIZONTAL);
                        snackText.setTextColor(Color.parseColor("#ffffff"));
                        snackbar.show();

                    }else{

                        progressBar.setVisibility(View.INVISIBLE);
                        noContent.setVisibility(View.VISIBLE);
                        wholeLayerRetry.setVisibility(View.VISIBLE);
                        LinearLayout fakeButton = (LinearLayout) wholeLayerRetry.findViewById(R.id.amfake);

                        //listeners for retry button
                        fakeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("dont ", " i will work forever worry");
                                requestsArticles(page);
                            }
                        });


                    }

                }
            }
        });

        return articleItems;
    }
    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter

        //moreItems=requestsArticles(offset);
        requestsArticles(offset);
    }

}
