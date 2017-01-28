package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Context;

import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;

public class EditArticleActivity extends AppCompatActivity {

    private EditText articleToEdit;

    private int articleId;
    private  String copiedText;
    private LayoutInflater inflater;
    private View popUpWindowView;
    private PopupWindow popupWindow;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){

            if(articleToEdit.getText().toString().trim().length()>copiedText.length()){

                //am ready for popup menu emmanuel

                inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popUpWindowView = inflater.inflate(R.layout.activity_popup_confirm, null);

                TextView keep = (TextView) popUpWindowView.findViewById(R.id.keep);
                TextView discard = (TextView) popUpWindowView.findViewById(R.id.discard);

                popupWindow = new PopupWindow(popUpWindowView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);

                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);

                popupWindow.showAtLocation(articleToEdit,Gravity.CENTER,0,0);
                //popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);

                ///setting the listeners for edit and deleting//////////////////////////////////////

                keep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        editArticle(articleId, articleToEdit.getText().toString().trim());
                        finish();
                    }
                });

                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        finish();
                    }
                });

            }else{
                finish();
            }

        }else if(id==R.id.correct){
            if(!(copiedText.equalsIgnoreCase(articleToEdit.getText().toString().trim()))){
                editArticle(articleId, articleToEdit.getText().toString().trim());
                finish();
            }
        }
        return  true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);
        articleId=Integer.parseInt(getIntent().getStringExtra("articleId"));
        articleToEdit=(EditText)findViewById(R.id.article_to_edit);
        getSupportActionBar().setTitle(Html.fromHtml("EDIT ARTICLE"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        copiedText=getIntent().getStringExtra("passedArticle");
        if(copiedText!=null){
            articleToEdit.setText(copiedText);
            articleToEdit.setSelection(copiedText.length());
        }

    }


    public void editArticle(final int articleId,final String articleContent){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("article",articleContent);
        params.put("articleId",articleId);

        client.get(getBaseContext(), CommonInformation.EDIT_ARTICLE_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.e("article edi", "failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {



            }
        });


    }
}
