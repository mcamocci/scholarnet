package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.*;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.FileOperations;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UploadImageService;

public class ShareActivity extends AppCompatActivity {

    private View commonView;
    String picturePath;
    private int idToPass;
    EditText writtenArticle;
    LinearLayout emptyView;
    int RESULT_LOAD_IMAGE = 1994;
    int count = 0;
    //declaring images view // new approach

    private ImageView imageOne;
    private ImageView imageTwo;
    private ImageView imageThree;
    private ImageView imageFour;

    // who want image

    private int whoWantImage=0;

    ///now the imageview have their corresponding String to hold urls

    private String imageOneUrl="empty";
    private String imageTwoUrl="empty";
    private String imageThreeUrl="empty";
    private String imageFourUrl="empty";

    private ArrayList<String> imagesString=new ArrayList<>();


    String[] image_url = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        commonView = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("SHARE"));
        //getting the passed id in the intent///////////////////////
        idToPass = getIntent().getIntExtra("id", 0);

        //doing some initialization

        writtenArticle = (EditText) findViewById(R.id.written_article);
        emptyView = (LinearLayout) findViewById(R.id.empty_view);

        ///new Aproach imageView initialization

        imageOne=(ImageView)findViewById(R.id.image_select_one);
        imageTwo=(ImageView)findViewById(R.id.image_select_two);
        imageThree=(ImageView)findViewById(R.id.image_select_three);
        imageFour=(ImageView)findViewById(R.id.image_select_four);

        ///adding event when each is clicked


        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(),Integer.toString(whoWantImage),Toast.LENGTH_LONG).show();
                if(imageOneUrl.contains("empty")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    whoWantImage=1;

                }else{
                    imagesString.remove(imageOneUrl);
                    imageOne.setImageResource(0);
                    imageOneUrl="empty";
                }
            }
        });
        imageTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),Integer.toString(whoWantImage),Toast.LENGTH_LONG).show();
                if(imageTwoUrl.contains("empty")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    whoWantImage=2;

                }else{
                    imagesString.remove(imageTwoUrl);
                    imageTwo.setImageResource(0);
                    imageTwoUrl="empty";
                }

            }
        });
        imageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),Integer.toString(whoWantImage),Toast.LENGTH_LONG).show();
                if(imageThreeUrl.contains("empty")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    whoWantImage=3;
                }else{
                    imagesString.remove(imageThreeUrl);
                    imageThree.setImageResource(0);
                    imageThreeUrl="empty";

                }
            }
        });
        imageFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whoWantImage=4;
                Toast.makeText(getBaseContext(),Integer.toString(whoWantImage),Toast.LENGTH_LONG).show();
                if(imageFourUrl.contains("empty")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                }else{
                    imagesString.remove(imageFourUrl);
                    imageFour.setImageResource(0);
                    imageFourUrl="empty";
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.share) {

            //some validation goes here
            String written = writtenArticle.getText().toString().trim();
            if (isValidArticle(written)) {
                //publish regardless of presence of images//
                //publishPresentedArticle(written, commonView);
                Intent intent = new Intent(ShareActivity.this, UploadImageService.class);
                intent.putExtra("article", written);
                intent.putExtra("subjectId", idToPass);

                if (imagesString.size() == 4) {

                    intent.putExtra("fileOne", imagesString.get(0));
                    intent.putExtra("fileTwo", imagesString.get(1));
                    intent.putExtra("fileThree", imagesString.get(2));
                    intent.putExtra("fileFour", imagesString.get(3));

                } else if (imagesString.size() == 3) {

                    intent.putExtra("fileOne", imagesString.get(0));
                    intent.putExtra("fileTwo", imagesString.get(1));
                    intent.putExtra("fileThree", imagesString.get(2));

                } else if (imagesString.size() == 2) {
                    intent.putExtra("fileOne", imagesString.get(0));
                    intent.putExtra("fileTwo", imagesString.get(1));


                }
                if (imagesString.size() == 1) {
                    intent.putExtra("fileOne", imagesString.get(0));

                }

                startService(intent);
                finish();

            } else {
                Snackbar.make(commonView, Html.fromHtml("<font color='#ff0000' align='center'>upload is not possible for the given information</font>"), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ///////////////////placing image where needed//////////////////////////

            if(whoWantImage==1){

                imageOneUrl=picturePath;
                imagesString.add(picturePath);
                FileOperations.displayWhenAvaillable(getBaseContext(),new File(imageOneUrl),imageOne,75,75);
                whoWantImage=0;
            }else if(whoWantImage==2){

                imageTwoUrl=picturePath;
                imagesString.add(picturePath);
                FileOperations.displayWhenAvaillable(getBaseContext(),new File(imageTwoUrl),imageTwo,75,75);
                whoWantImage=0;
            }else if(whoWantImage==3){

                imageThreeUrl=picturePath;
                imagesString.add(picturePath);
                FileOperations.displayWhenAvaillable(getBaseContext(),new File(imageThreeUrl),imageThree,75,75);
                whoWantImage=0;
            }else if(whoWantImage==4){

                imageFourUrl=picturePath;
                imagesString.add(picturePath);
                FileOperations.displayWhenAvaillable(getBaseContext(),new File(imageFourUrl),imageFour,75,75);
                whoWantImage=0;
            }
        }else{
            whoWantImage=0;
        }
    }

    public boolean isValidArticle(String written) {
        if (written.length() < 4) {
            return false;
        }
        return true;
    }

    public void publishPresentedArticle(String article, final View view) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("article", article);
        params.put("subject", ShareActivity.this.idToPass);
        params.put("user", LoginPreferencesChecker.logEventGetPhone(getBaseContext()));

        client.get(getBaseContext(), CommonInformation.ARTICLE_POST_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // Toast.makeText(getBaseContext(),"starting", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                writtenArticle.setText("");
                Snackbar.make(view, "Article has been submitted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (writtenArticle.getText().toString().trim().length() > 0) {
                Toast.makeText(getBaseContext(), "discarding all information?", Toast.LENGTH_LONG);
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }


}
