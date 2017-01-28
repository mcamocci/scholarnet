package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.ImagesListJSONparser;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.adapters.ImprovedWhatsAdapter;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CommentRecomendItem;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ImageItem;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.WhatsapItem;

public class MoreDetailActivity extends AppCompatActivity {

    private int passedId;
    private ListView recycler_replaced_list;
    private List<ImageItem> imagesList=new ArrayList<>();
    CommentRecomendItem commRecCount=new CommentRecomendItem();
    private String details;


    List<WhatsapItem> whatsapItemsList=new ArrayList<>();

    TextView comments_c;
    TextView recommends_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_post_view);

        ///Loading layout components
        recycler_replaced_list =(ListView)findViewById(R.id.recycler_replaced_list);


        //////getting all the passed data
        passedId=getIntent().getIntExtra("articleId",0);
        details=getIntent().getStringExtra("detail");
        String poster=getIntent().getStringExtra("poster");


        //getting imagesList online
        requestImagesUrlOnline(passedId);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(poster.toUpperCase());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_viewer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }else if(id==R.id.comment_ic){
            Intent intent=new Intent(MoreDetailActivity.this,CommentsActivity.class);
            intent.putExtra("id",passedId);
            startActivity(intent);
        }else if(id==R.id.recommend_ic){
            setRecommendRequest();

        }
        return  true;
    }

    public void requestImagesUrlOnline(int id){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post",id);

        client.get(getBaseContext(), CommonInformation.IMAGES_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "process failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                imagesList = ImagesListJSONparser.getAllImages(responseString);

                if (imagesList != null) {

                    WhatsapItem whatsapDetail = new WhatsapItem();
                    whatsapDetail.setText(details);
                    whatsapDetail.setUrl("no url");
                    whatsapItemsList.add(whatsapDetail);

                    for (ImageItem item : imagesList) {

                        if (item.getUrl() != null) {
                            WhatsapItem whatsapItem = new WhatsapItem();
                            whatsapItem.setText("no text");
                            whatsapItem.setUrl(item.getUrl());
                            whatsapItemsList.add(whatsapItem);
                        }
                    }
                    setSuperAdapters(whatsapItemsList);
                }
            }
        });
    }

    public void setSuperAdapters(List<WhatsapItem> items){

        recycler_replaced_list.setAdapter(new ImprovedWhatsAdapter(getBaseContext(), R.layout.whatsap_item, items));
    }


    public void setRecommendRequest(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post",passedId);
        params.put("user", LoginPreferencesChecker.logEventGetPhone(MoreDetailActivity.this));

        client.get(getBaseContext(), CommonInformation.RECOMMEND_ARTICLE_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(getBaseContext(), "process failed", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
