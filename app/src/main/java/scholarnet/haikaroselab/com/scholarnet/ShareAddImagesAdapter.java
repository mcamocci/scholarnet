package scholarnet.haikaroselab.com.scholarnet;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ImagePathPojo;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.ImageEfficient;

/**
 * Created by root on 3/8/16.
 */
public class ShareAddImagesAdapter extends RecyclerView.Adapter<ShareAddImagesAdapter.ImageHolder> {


    List<ImagePathPojo> list;
    Context context;

    public ShareAddImagesAdapter(Context context, List<ImagePathPojo> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_add_item, parent, false);
        ImageHolder holder = new ImageHolder(view);
        holder.setListener();
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.initializeSomething(list.get(position).getImageUrl());

    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String url;
        ImageView image;
        ProgressBar progressBar;

        public ImageHolder(View view) {
            super(view);

             image = (ImageView) view.findViewById(R.id.image);
            //progressBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.loading_image) {


            }

        }

        public void initializeSomething(String url) {

            this.url = url;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(url,options);
            // Calculate inSampleSize
            options.inSampleSize = ImageEfficient.calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap bitmap=BitmapFactory.decodeFile(url,options);
            image.setImageBitmap(bitmap);

        }

        public void setListener() {
            //image.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
