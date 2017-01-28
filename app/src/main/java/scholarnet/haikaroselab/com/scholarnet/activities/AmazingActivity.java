package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.*;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CommentRecomendItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.CommentRecommendJSONItemParser;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.FileOperations;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ImageItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.ImagesListJSONparser;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;


/**
 * Created by root on 3/18/16.
 */
public class AmazingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewOne;
    private ImageView imageViewTwo;
    private ImageView imageViewThree;
    private ImageView imageViewFour;
    private String imageOne;
    private int imageCount;
    private String imageTwo;
    private String imageThree;
    private String imageFour;
    private PopupWindow popUpWindow;
    private LayoutInflater inflater;
    private View popUpWindowView;
    //private TextView download_text;

    //private CardView download_card;
    private int passedId;
    private String details;
    private TextView detail_tex;
    private List<ImageItem> imagesList = new ArrayList<>();
    CommentRecomendItem commRecCount = new CommentRecomendItem();

    private Toolbar toolbar;

    //initializing card_view holders

    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;



    ////declaring donut progress bar/////////////////////////////////////////////////////////////////////////////////////////
    private DonutProgress donutProgress1;
    private DonutProgress donutProgress2;
    private DonutProgress donutProgress3;
    private DonutProgress donutProgress4;

    ////////////////////////////////images url and view progress////////////////////////////////////////////////////////

    private LinearLayout loadingProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_amazing);

        /////////instantiating cardview holders///////////////////////////////////////////////////////////////////////////////

        cardView1=(CardView)findViewById(R.id.card_view1);
        cardView2=(CardView)findViewById(R.id.card_view2);
        cardView3=(CardView)findViewById(R.id.card_view3);
        cardView4=(CardView)findViewById(R.id.card_view4);

        ///////////////////////////////////////////////initilizing what is neccessary to load//////////////////////////
        loadingProgress=(LinearLayout)findViewById(R.id.image_progress_load);
        loadingProgress.setVisibility(View.GONE);

        ////////////////////////////hidding them all//////////////////////////////////////////////////////////////////////////

        //////////showing all the necessary card_viwe holder/////////////////////////////////////////////////////////////////

        cardView1.setVisibility(View.INVISIBLE);
        cardView2.setVisibility(View.INVISIBLE);
        cardView3.setVisibility(View.INVISIBLE);
        cardView4.setVisibility(View.INVISIBLE);

        ////////////////////initializing donut progress bar by default///////////////////////////////////////////////////////

        donutProgress1 = (DonutProgress) findViewById(R.id.donut_progress1);
        donutProgress2 = (DonutProgress) findViewById(R.id.donut_progress2);
        donutProgress3 = (DonutProgress) findViewById(R.id.donut_progress3);
        donutProgress4 = (DonutProgress) findViewById(R.id.donut_progress4);

        ///hidding them all by default////////////////////////////////////////////////////////////////////////////////////////

        donutProgress1.setVisibility(View.INVISIBLE);
        donutProgress2.setVisibility(View.INVISIBLE);
        donutProgress3.setVisibility(View.INVISIBLE);
        donutProgress4.setVisibility(View.INVISIBLE);

        ////////hey the initialization process end here fortunately////////////////////////////////////////////////////////////

        //////getting all the passed data
        passedId = getIntent().getIntExtra("articleId", 0);
        details = getIntent().getStringExtra("detail");
        String poster = getIntent().getStringExtra("poster");
        imageCount=getIntent().getIntExtra("imageCount",0);

        //request all the images online
        if(imageCount>0){
            requestImagesUrlOnline(passedId);
        }


        //////////////////////////////dealing with toolbar issues/////////////////////////////////////////////

        toolbar = (Toolbar) findViewById(R.id.toolbar_amazing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + poster.substring(0,1).toUpperCase()+poster.substring(1,poster.length())+ "</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Initializing all image views//////////////////////////////////////////////////////////////////////
        imageViewOne = (ImageView) findViewById(R.id.image_view_one);
        imageViewTwo = (ImageView) findViewById(R.id.image_view_two);
        imageViewThree = (ImageView) findViewById(R.id.image_view_three);
        imageViewFour = (ImageView) findViewById(R.id.image_view_four);
        detail_tex = (TextView) findViewById(R.id.detail_textview);


        //getting imagesList online
        //requestImagesUrlOnline(passedId);
        requestCommentRecommendCounts();

        /////////////////////////setting the detail text/////////////////////////////////////////////////////
        detail_tex.setText(details);

        ////////////////////////////initialize other views///////////////////////////////////////////////////

      //  download_card = (CardView) findViewById(R.id.download_card);
       // download_card.setVisibility(View.INVISIBLE);
       /// download_text = (TextView) findViewById(R.id.download_text);

        ///////////////////////hidding all view by default//////////////////////////////////////////////////
        imageViewOne.setVisibility(View.GONE);
        imageViewTwo.setVisibility(View.GONE);
        imageViewThree.setVisibility(View.GONE);
        imageViewFour.setVisibility(View.GONE);
        ///////////////////////showing necessary view ////////////////////////////////////////////////////////
        loadWithoutDownloadWhenAvaillable();

        ///////////////////////setting listeners ////////////////////////////////////////////////////////
        imageViewOne.setOnClickListener(this);
        imageViewTwo.setOnClickListener(this);
        imageViewThree.setOnClickListener(this);
        imageViewFour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.image_view_one) {

            ///checking for availlabiliity

            String imageName = new File(imageOne).getName();
            if (isFileAvaillable(new File(FileOperations.getAppFolder() + File.separator + imageName))) {

                /////opening image with image view application////
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + new File(FileOperations.getAppFolder() + File.separator + imageName).getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);

            } else {
                     retrieveFileOnline(CommonInformation.COMMON + imageOne, getBaseContext(), imageViewOne,imageName,donutProgress1);
            }

        } else if (v.getId() == R.id.image_view_two) {

            ///////////checking for availlabiliity////////////////////////////////
            String imageName = new File(imageTwo).getName();
            if (isFileAvaillable(new File(FileOperations.getAppFolder() + File.separator + imageName))) {

                /////opening image with image view application////
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + new File(FileOperations.getAppFolder() + File.separator + imageName).getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);

            } else {
                retrieveFileOnline(CommonInformation.COMMON + imageTwo, getBaseContext(), imageViewTwo, imageName,donutProgress2);
            }

        } else if (v.getId() == R.id.image_view_three) {

            ///checking for availlabiliity

            String imageName = new File(imageThree).getName();
            if (isFileAvaillable(new File(FileOperations.getAppFolder() + File.separator + imageName))) {

                /////opening image with image view application////
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + new File(FileOperations.getAppFolder() + File.separator + imageName).getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);


            } else {
                retrieveFileOnline(CommonInformation.COMMON + imageThree, getBaseContext(), imageViewThree, imageName,donutProgress3);
            }

        } else if (v.getId() == R.id.image_view_four) {


            ///checking for availlabiliity
            ///////////checking for availlabiliity////////////////////////////////
            String imageName = new File(imageFour).getName();

            if (isFileAvaillable(new File(FileOperations.getAppFolder() + File.separator + imageName))) {
                /////opening image with image view application////
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + new File(FileOperations.getAppFolder() + File.separator + imageName).getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);

            } else {
                retrieveFileOnline(CommonInformation.COMMON + imageFour, getBaseContext(), imageViewFour, imageName,donutProgress4);
            }

        }
    }


    public boolean isFileAvaillable(File file) {

        if (file.exists()) {
            return true;
        }
        return false;
    }

    public void retrieveFileOnline(String url, final Context context, final ImageView imageView, final String originalName, final DonutProgress donutProgress) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(url, params, new FileAsyncHttpResponseHandler(context) {


            long currentValue=0;
            long totalSize=0;
            @Override
            public void onProgress(long bytesWritten, long totalSize) {

                currentValue+=bytesWritten;
                long totalprogress;
                totalprogress = (bytesWritten*100)/totalSize;
                donutProgress.setProgress((int) (totalprogress));
                super.onProgress(bytesWritten,totalSize);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                // progressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "please try again later", Toast.LENGTH_LONG).show();
                donutProgress.setVisibility(View.INVISIBLE);

                Toast.makeText(getBaseContext(),throwable.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                //progressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(getBaseContext(), "download completed", Toast.LENGTH_LONG).show();
                donutProgress.setVisibility(View.INVISIBLE);
                FileOperations.writeFileOperation(getBaseContext(), file, originalName);
                FileOperations.displayWhenAvaillable(getBaseContext(), new File(FileOperations.getAppFolder() + File.separator + originalName), imageView);
            }

            @Override
            public void onStart() {
                // progressbar.setVisibility(View.VISIBLE);
                donutProgress.setVisibility(View.VISIBLE);
                Toast.makeText(getBaseContext(), "downloading,please wait!!", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.amazing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }else if (id == R.id.comment) {
            Intent intent=new Intent(AmazingActivity.this, CommentsActivity.class);
            intent.putExtra("id",passedId);
            startActivity(intent);
        } else if (id == R.id.recommend) {
            recommendDefinition();
        }

        return true;
    }

    public void requestCommentRecommendCounts() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post", passedId);

        client.get(getBaseContext(), CommonInformation.POST_COUNT_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "process failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<CommentRecomendItem> items = CommentRecommendJSONItemParser.getCommentRecommedItem(responseString);

                for (CommentRecomendItem item : items) {
                    commRecCount.setRecommend(item.getRecommend());
                    commRecCount.setComment(item.getComment());
                }
            }
        });
    }

    public void setRecommendRequest() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post", passedId);
        params.put("user", LoginPreferencesChecker.logEventGetPhone(AmazingActivity.this));

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

    public void requestImagesUrlOnline(int id) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post", id);

        client.get(getBaseContext(), CommonInformation.IMAGES_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                loadingProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "process failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                imagesList = ImagesListJSONparser.getAllImages(responseString);


                if (imagesList != null) {

                    ListIterator<ImageItem> imageIterator = imagesList.listIterator();

                    if (imagesList.size() > 4) {

                        imageOne = imageIterator.next().getUrl();
                        imageTwo = imageIterator.next().getUrl();
                        imageThree = imageIterator.next().getUrl();
                        imageFour = imageIterator.next().getUrl();

                        /////////////show them///////////////////////////////////////////
                        imageViewOne.setVisibility(View.VISIBLE);
                        imageViewTwo.setVisibility(View.VISIBLE);
                        imageViewThree.setVisibility(View.VISIBLE);
                        imageViewFour.setVisibility(View.VISIBLE);

                        //////////showing all the necessary card_viwe holder//////////////////////

                        cardView1.setVisibility(View.VISIBLE);
                        cardView2.setVisibility(View.VISIBLE);
                        cardView3.setVisibility(View.VISIBLE);
                        cardView4.setVisibility(View.VISIBLE);




                    }if (imagesList.size() == 4) {
                        imageOne = imageIterator.next().getUrl();

                        imageTwo = imageIterator.next().getUrl();
                        imageThree = imageIterator.next().getUrl();
                        imageFour = imageIterator.next().getUrl();

                        /////////////show them///////////////////////////////////////////
                        imageViewOne.setVisibility(View.VISIBLE);
                        imageViewTwo.setVisibility(View.VISIBLE);
                        imageViewThree.setVisibility(View.VISIBLE);
                        imageViewFour.setVisibility(View.VISIBLE);

                        //////////showing all the necessary card_viwe holder//////////////////////

                        cardView1.setVisibility(View.VISIBLE);
                        cardView2.setVisibility(View.VISIBLE);
                        cardView3.setVisibility(View.VISIBLE);
                        cardView4.setVisibility(View.VISIBLE);


                    } else if (imagesList.size() == 3) {
                        imageOne = imageIterator.next().getUrl();
                        imageTwo = imageIterator.next().getUrl();
                        imageThree = imageIterator.next().getUrl();

                        /////////////////////show them////////////////////////
                        imageViewOne.setVisibility(View.VISIBLE);
                        imageViewTwo.setVisibility(View.VISIBLE);
                        imageViewThree.setVisibility(View.VISIBLE);

                        //////////showing all the necessary card_viwe holder//////////////////////

                        cardView1.setVisibility(View.VISIBLE);
                        cardView2.setVisibility(View.VISIBLE);
                        cardView3.setVisibility(View.VISIBLE);


                    } else if (imagesList.size() == 2) {
                        imageOne = imageIterator.next().getUrl();
                        imageTwo = imageIterator.next().getUrl();
                        /////////////////////show them////////////////////////
                        imageViewOne.setVisibility(View.VISIBLE);
                        imageViewTwo.setVisibility(View.VISIBLE);
                        //////////showing all the necessary card_viwe holder//////////////////////

                        cardView1.setVisibility(View.VISIBLE);
                        cardView2.setVisibility(View.VISIBLE);


                    } else if (imagesList.size() == 1) {
                        imageOne = imagesList.get(0).getUrl();
                        /////////////////////show them////////////////////////
                        imageViewOne.setVisibility(View.VISIBLE);

                        //////////showing all the necessary card_viwe holder//////////////////////

                        cardView1.setVisibility(View.VISIBLE);

                    }

                    loadWithoutDownloadWhenAvaillable();
                }
                loadingProgress.setVisibility(View.GONE);
            }
        });
    }

    public void loadWithoutDownloadWhenAvaillable() {

        ///////////////////////showing necessary view ////////////////////////////////////////////////////////

        if (imageOne != null) {
            imageViewOne.setVisibility(View.VISIBLE);

            ///////display the file if already exists//////////////////////
            FileOperations.displayWhenAvaillable(getBaseContext(), new File(FileOperations.getAppFolder()
                    + File.separator + new File(imageOne).getName()), imageViewOne);

        }
        if (imageTwo != null) {
            imageViewTwo.setVisibility(View.VISIBLE);
            ///////display the file if already exists//////////////////////
            FileOperations.displayWhenAvaillable(getBaseContext(), new File(FileOperations.getAppFolder()
                    + File.separator + new File(imageTwo).getName()), imageViewTwo);
        }
        if (imageThree != null) {
            imageViewThree.setVisibility(View.VISIBLE);
            ///////display the file if already exists//////////////////////
            FileOperations.displayWhenAvaillable(getBaseContext(), new File(FileOperations.getAppFolder()
                    + File.separator + new File(imageThree).getName()), imageViewThree);
        }
        if (imageFour != null) {
            imageViewFour.setVisibility(View.VISIBLE);
            ///////display the file if already exists//////////////////////
            FileOperations.displayWhenAvaillable(getBaseContext(), new File(FileOperations.getAppFolder()
                    + File.separator + new File(imageFour).getName()), imageViewFour);
        }
    }

    public void recommendDefinition(){

        //am ready for popup menu emmanuel
        inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpWindowView = inflater.inflate(R.layout.activity_recommend_confirm, null);
        // popUpWindowView.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadein));
        popUpWindow = new PopupWindow(popUpWindowView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popUpWindow.setAnimationStyle(R.style.Animation);
        popUpWindow.setBackgroundDrawable(new BitmapDrawable());
        popUpWindow.setOutsideTouchable(true);
        TextView accept = (TextView) popUpWindowView.findViewById(R.id.accept);
        TextView discard = (TextView) popUpWindowView.findViewById(R.id.discard);

        //showing the popupwindow at the center
        popUpWindow.showAtLocation(toolbar, Gravity.CENTER,0,0);

        ///setting the listeners for edit and deleting//////////////////////////////////////

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecommendRequest();
                requestCommentRecommendCounts();
                popUpWindow.dismiss();

            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpWindow.dismiss();

            }
        });
    }
}
