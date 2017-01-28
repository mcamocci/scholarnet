package scholarnet.haikaroselab.com.scholarnet.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.WhatsapItem;

/**
 * Created by root on 9/28/15.
 */
public class ImprovedWhatsAdapter extends ArrayAdapter<WhatsapItem> {

    private Context context;
    private List<WhatsapItem> list;
    ImageView image_view;
    ProgressBar progressBar;

    public ImprovedWhatsAdapter(Context context, int resources, List<WhatsapItem> objects){

        super(context,resources,objects);
        this.context=context;
        this.list=objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        WhatsapItem item=list.get(position);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view=(View)inflater.inflate(R.layout.whatsap_item, null);
        TextView detail_view = (TextView) view.findViewById(R.id.hidden_text);
        image_view = (ImageView) view.findViewById(R.id.hidden_image);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        image_view.setTag(position);
        image_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(v.getId()==R.id.hidden_image){

                    int pos=(Integer)v.getTag();
                    loadImage(list.get(pos).getUrl(),pos,image_view, progressBar);

                }

            }
        });

        if(!(list.get(position).getText().contains("no text"))){
            //  recycler_replaced_list
            removeUnnecessaryImage(list.get(position).getText(),image_view,detail_view);
        }else{
            removeUnnecessaryDetails("haha",detail_view,image_view);
        }

        return view;
    }


    public void removeUnnecessaryImage(String details,ImageView view,TextView details_view) {
        ((ViewManager)view.getParent()).removeView(view);
        details_view.setText(details);
    }

    public void removeUnnecessaryDetails(String details,View view,ImageView image) {
        ((ViewManager)view.getParent()).removeView(view);
    }


    public void loadImage(String url,int pos,final ImageView image,final ProgressBar progressbar) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.get(CommonInformation.COMMON+"/"+url, params, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "please try again later", Toast.LENGTH_LONG).show();
                Toast.makeText(context,Integer.toString(statusCode), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progressbar.setVisibility(View.INVISIBLE);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (bitmap != null) {

                    //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,680,610, true);

                    image.setImageBitmap(bitmap);
                    image.getLayoutParams().height= ActionBar.LayoutParams.WRAP_CONTENT;
                    image.getLayoutParams().width=ActionBar.LayoutParams.WRAP_CONTENT;

                }
            }

            @Override
            public void onStart() {

                progressbar.setVisibility(View.VISIBLE);
            }

        });

    }


}
