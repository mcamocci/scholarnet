package scholarnet.haikaroselab.com.scholarnet.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.adapters.CommentItemAdapter;
import scholarnet.haikaroselab.com.scholarnet.beaPackage.EndlessRecyclerViewScrollListener;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CommentItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.CommentsListJSONParser;

public class CommentsActivity extends AppCompatActivity {

    private FloatingActionButton submitButton;
    private EditText writtenComment;
    private ProgressBar progressBar;
    private int articleId;
    private int page=1;
    private CommentItemAdapter adapter;
    private boolean superSuccess = false;
    List<CommentItem> listOfComments = new ArrayList<>();
    RecyclerView commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coments);
        getSupportActionBar().setTitle(Html.fromHtml("COMMENTS"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toast.makeText(getBaseContext(),LoginPreferencesChecker.logEventGetPhone(getBaseContext()),Toast.LENGTH_LONG).show();

        //////////////////initializing necessary components//////////////////////

        submitButton = (FloatingActionButton) findViewById(R.id.submit_button);
        writtenComment = (EditText) findViewById(R.id.written_comment);
        commentsList = (RecyclerView) findViewById(R.id.comments_recycler);
        progressBar=(ProgressBar)findViewById(R.id.progressBar9);
        progressBar.setVisibility(View.INVISIBLE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setItemAnimator(new DefaultItemAnimator());
        commentsList.setLayoutManager(layoutManager);

        ///the adapter goes here//////////////////

        adapter = new CommentItemAdapter(getBaseContext(), listOfComments);
        commentsList.setAdapter(adapter);

        ///receiving the bundle intent
        articleId= getIntent().getIntExtra("id", 0);

        ///getting list of all related comments
        customLoadMoreDataFromApi(page);
        ///////////////////////////////////////////////////////////////////////


        ////dealing with the recycler view/////////////////////////////////////////

        commentsList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                CommentsActivity.this.page = page;
                customLoadMoreDataFromApi(page);
            }
        });

        //////////////////////////////setting listener for the submit event

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int now=listOfComments.size();

                String comment = writtenComment.getText().toString().trim();
                Toast.makeText(getBaseContext(),comment,Toast.LENGTH_LONG).show();
                if (comment.length() >= 1) {

                    View view=CommentsActivity.this.getCurrentFocus();
                    InputMethodManager iMmanager=(InputMethodManager)getSystemService((getBaseContext().INPUT_METHOD_SERVICE));
                    iMmanager.hideSoftInputFromWindow(view.getWindowToken(),0);
                    writtenComment.setText("");
                    if (publishCommentOnline(comment, LoginPreferencesChecker.logEventGetPhone(getBaseContext()),articleId,v)) {
                        //requestCommentsList(articleId);

                        int then=listOfComments.size();
                        requestCommentsList(articleId,page);
                        if(now==then){
                          //  requestCommentsList(articleId);
                        }

                    }
                   // requestCommentsList(articleId);
                }else{
                    View view=CommentsActivity.this.getCurrentFocus();
                    InputMethodManager iMmanager=(InputMethodManager)getSystemService((getBaseContext().INPUT_METHOD_SERVICE));
                    iMmanager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return true;
    }

    public void requestCommentsList(int id,int page) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post", id);

        client.get(getBaseContext(), CommonInformation.COMMENTS_RETRIEVE_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), responseString, Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), new String(throwable.toString()), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progressBar.setVisibility(View.INVISIBLE);
                List<CommentItem> downloaded = new ArrayList<>();
                downloaded= CommentsListJSONParser.getCommentsList(responseString);
                //allArticles.addAll(downloadedArticle);
                final int curSize = adapter.getItemCount();
                listOfComments.addAll(downloaded);
                adapter.notifyItemRangeInserted(curSize, listOfComments.size() - 1);

            }
        });

    }


    public boolean publishCommentOnline(String comment,String phone,int id,final View view) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post",id);
        params.put("user",phone);
        params.put("comment",comment);

        client.get(getBaseContext(), CommonInformation.COMMENTS_POST_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), responseString, Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), new String(throwable.toString()), Toast.LENGTH_LONG).show();
                CommentsActivity.this.superSuccess = false;

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                CommentsActivity.this.superSuccess = true;
                writtenComment.setText("");
                Snackbar.make(view, "published", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        return CommentsActivity.this.superSuccess;
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter

        //moreItems=requestsArticles(offset);
        ///getting list of all related comments
        requestCommentsList(articleId,page);
    }
}
