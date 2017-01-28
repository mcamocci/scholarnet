package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.app.IntentService;

import android.content.Intent;
import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class UploadImageService extends IntentService {

    private  String url;
    private  Context context;
    RequestParams params = new RequestParams();
    File real_file1;
    File real_file2;
    File real_file3;
    File real_file4;
    private List<Cookie> cookies;

    public UploadImageService(String url,Context context) {

        super("UploadImageService");
        this.context=context;
        this.url=url;
    }

    public UploadImageService(){
        super("UploadingImageService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {


        Toast.makeText(getBaseContext(),"upload process started",Toast.LENGTH_LONG).show();


        Log.e("down","started");
        //String filePath=intent.getStringExtra("path");
       // String title=intent.getStringExtra("title");
       // int  id=intent.getIntExtra("subjectId",0);

       // Log.e("service",Integer.toString(id));
      ///  Log.e("file path",filePath);
       // Log.e("title",title);

        String article=intent.getStringExtra("article");
        int subjectId=intent.getIntExtra("subjectId", 0);
        String fileOne=intent.getStringExtra("fileOne");
        String fileTwo=intent.getStringExtra("fileTwo");
        String fileThree=intent.getStringExtra("fileThree");
        String fileFour=intent.getStringExtra("fileFour");

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(20 * 1000);

        client.setConnectTimeout(20 * 1000);
        client.setResponseTimeout(20 * 1000);

        ////this is where the cookie belongs!!
        /////////working with cookie from the server/////////////////////////////////

        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);

        ////////////////////////////////////////////////////////////////////////////

        cookies=myCookieStore.getCookies();


        if(fileOne!=null){
            real_file1=new File(fileOne);

        }if(fileTwo!=null){

            real_file2=new File(fileTwo);

        }if(fileThree!=null){

            real_file3=new File(fileThree);

        }if(fileFour!=null){

            real_file4=new File(fileFour);
        }

        try{

            if(fileOne!=null){
                params.put("image1",real_file1);
            }if(fileTwo!=null){
                params.put("image2",real_file2);
            }if(fileThree!=null){
                params.put("image3",real_file3);
            }if(fileFour!=null){
                params.put("image4",real_file4);
            }

            params.put("article",article);
            params.put("user",LoginPreferencesChecker.logEventGetPhone(getBaseContext()));
            params.put("subject", subjectId);

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            params.setForceMultipartEntityContentType(true);

            if(!cookies.isEmpty()){

                ///retrieving the cookies //from the server

                Cookie cookie = cookies.get(0);
                Log.e("cookies name:", cookie.getName());
                Log.e("cookies values:", cookie.getValue());

                Log.e("cookies domain:", cookie.getDomain());
                Log.e("cookies path:", cookie.getPath());

                for(Cookie cookie1:cookies){
                    Log.e("cookie name jkk:",cookie1.getName()+":"+cookie1.getValue());
                    if(cookie1.getName().equalsIgnoreCase("csrftoken")){
                     //   client.addHeader("X-CSRFToken", cookie.getValue());
                       // client.addHeader("Content-type","multipart/form-data");
                        Log.e("fine", "ok");
                    }else if(cookie1.getName().equalsIgnoreCase("sessionid")){
                       // client.addHeader("Cookie", cookie.getValue());
                        Log.e("fine", "ok1");
                    }
                }

                /////////////////////////////////////////////////////////

                client.getHttpContext().setAttribute(cookie.getName(), cookie.getValue());

                Log.e("hello", cookie.getValue());
             //   client.addHeader("Connection", "keep-alive");
              //  client.addHeader("Referer",CommonInformation.ARTICLE_POST_URL);
            }


        }catch (Exception ex){

        }
        URL url=null;
        try {
            url=new URL("http://www.scholarnetapp.com/mob/setPost/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.e("the url is;",url.toString());

        client.post(context,"http://www.scholarnetapp.com/mob/setPost/",params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.e("code", Integer.toString(statusCode));
                for (Header head : headers) {
                    Log.e("hey", head.getValue() + ":" + head.getName());
                }
                Log.e("response from server",new String(responseString));
                Toast.makeText(UploadImageService.this, "article upload process is " + responseString + "!!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(UploadImageService.this, "upload failed", Toast.LENGTH_LONG).show();
                Toast.makeText(UploadImageService.this, new String(throwable.toString()), Toast.LENGTH_LONG).show();

                Log.e("cod", Integer.toString(statusCode));

                for (Header head : headers) {
                    Log.e("hey", head.getName() + ":" + head.getValue());
                }
            }



            @Override
            public void onStart() {

                Log.e("process","started");
            }

        });
    }

    @Override
    public void onDestroy() {


    }
}
