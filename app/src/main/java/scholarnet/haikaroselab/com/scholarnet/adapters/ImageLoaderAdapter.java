package scholarnet.haikaroselab.com.scholarnet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ImageItem;

/**
 * Created by root on 3/8/16.
 */
public class ImageLoaderAdapter extends RecyclerView.Adapter<ImageLoaderAdapter.ImageSingleHolder>{

    List<ImageItem> list;
    Context context;

    public ImageLoaderAdapter(Context context,List<ImageItem> list){
        this.list=list;
        this.context=context;
    }

    @Override
    public ImageSingleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.image_add_item,parent,false);
        ImageSingleHolder holder=new ImageSingleHolder(view);
        holder.setOnClickListener(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageSingleHolder holder, int position) {


    }

    public class ImageSingleHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;

        public ImageSingleHolder(View view){
        super(view);

         image=(ImageView)view.findViewById(R.id.image);
        }

        public void setImage(Bitmap bitmap){
           //this.image.setText(details);
            File file=new File("useless address");
            // Glide.with(context).load(file.getAbsolutePath()).into(image);
            //image.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.loading_image){

                int myPosition=ImageSingleHolder.this.getAdapterPosition();
                ImageItem item=list.get(myPosition);

                ///hey when user click image the pulling of image action occurs Emma!!!
                startNetworkRequestForPullingImage(
                        CommonInformation.COMMON+item.getUrl(),v);
            }

        }

        public void setOnClickListener(View view){

            image.setOnClickListener(ImageSingleHolder.this);
        }

        public void startNetworkRequestForPullingImage(String url, final View view){

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url,new FileAsyncHttpResponseHandler(view.getContext()) {

                @Override
                public void onStart() {
                    Toast.makeText(view.getContext(),"downloading the image", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Toast.makeText(view.getContext(),"failed", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
