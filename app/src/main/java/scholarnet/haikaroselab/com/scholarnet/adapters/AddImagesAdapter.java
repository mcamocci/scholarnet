package scholarnet.haikaroselab.com.scholarnet.adapters;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.io.File;
import java.util.List;


import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ImageItem;

/**
 * Created by root on 3/8/16.
 */
public class AddImagesAdapter extends RecyclerView.Adapter<AddImagesAdapter.ImageHolder> {


    List<ImageItem> list;
    Context context;

    public AddImagesAdapter(Context context, List<ImageItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_load_item, parent, false);
        ImageHolder holder = new ImageHolder(view);
        holder.setListener();
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.initializeSomething(list.get(position).getUrl());

    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String url;
        ImageView image;
        ProgressBar progressBar;

        public ImageHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.loading_image);
            progressBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.loading_image) {

                loadImage(CommonInformation.COMMON+"/"+url,image,progressBar);

            }

        }

        public void initializeSomething(String url) {
            this.url = url;
        }

        public void setListener() {
            image.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void loadImage(String url,final ImageView image,final ProgressBar progressbar) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.get(url, params, new FileAsyncHttpResponseHandler(context) {
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

                    image.requestLayout();
                    //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,680,610, true);

                    image.setImageBitmap(bitmap);
                    if(bitmap.getWidth()>680 || bitmap.getWidth()>610){
                        image.getLayoutParams().height=610;
                        image.getLayoutParams().width=680;
                    }else{
                        image.getLayoutParams().height= ActionBar.LayoutParams.WRAP_CONTENT;
                        image.getLayoutParams().width=ActionBar.LayoutParams.WRAP_CONTENT;
                    }
                }
            }

            @Override
            public void onStart() {
                progressbar.setVisibility(View.VISIBLE);
            }

        });

    }
}
