package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;


public class DocumentUploaderService extends IntentService {

    private Context context;

    public DocumentUploaderService(Context context){
        super("DocumentUploaderService");
        this.context=context;
    }
    public DocumentUploaderService(){
        super("DocumentUploaderService");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getBaseContext(),"finished",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("down","started");
        String filePath=intent.getStringExtra("path");
        String title=intent.getStringExtra("title");
        int  id=intent.getIntExtra("subjectId",0);

        Log.e("service",Integer.toString(id));
        Log.e("file path",filePath);
        Log.e("title",title);
        uploadTheFile(id,title,new File(filePath));
    }

    public void uploadTheFile(int id, String title,File file) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(20 * 1000);
        RequestParams params = new RequestParams();
        client.setConnectTimeout(20 * 1000);
        client.setResponseTimeout(20 * 1000);

        params.put("subject", id);
        params.put("title", LoginPreferencesChecker.logEventGetPhone(getBaseContext()));
        try {
            params.put("file", file);
        } catch (Exception ex) {
        }
        client.post(getBaseContext(), "http://www.scholarnetapp.com/mob/setPost/", params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onStart() {

            }

        });
    }
}
