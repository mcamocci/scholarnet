package scholarnet.haikaroselab.com.scholarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.UploadImageService;

public class DocumentsUploaderActivity extends AppCompatActivity {

    private View commonView;
    private int idToPass;
    private String filepath="0";
    EditText writtenArticle;
    LinearLayout emptyView;
    int RESULT_LOAD_FILE = 1994;
    private ImageView imageOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        commonView = findViewById(R.id.toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("Upload document"));
        idToPass = getIntent().getIntExtra("subjectId", 0);


        writtenArticle = (EditText) findViewById(R.id.written_article);
        emptyView = (LinearLayout) findViewById(R.id.empty_view);
        imageOne=(ImageView)findViewById(R.id.image_select_one);

        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent,RESULT_LOAD_FILE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.upload) {

            //some validation goes here
            String title = writtenArticle.getText().toString().trim();
            if (title.length()>3 && filepath.length()>2) {

                Log.e("passed","hongera");
                Intent intent = new Intent(DocumentsUploaderActivity.this,UploadImageService.class);
                intent.putExtra("title", title);
                intent.putExtra("subject", idToPass);
                intent.putExtra("path",filepath);
                Log.e("path",filepath);
                Log.e("title",title);
                Log.e("id to pass",Integer.toString(idToPass));
                startService(intent);
               // finish();

            } else {
               Toast.makeText(getBaseContext(),"can not post",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK && null != data) {
            String selectedFile= data.getDataString();
            filepath=selectedFile;
        }
    }


}
