package scholarnet.haikaroselab.com.scholarnet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.R;

/**
 * Created by root on 3/8/16.
 */
public class DetailImageDiplayAdapter extends RecyclerView.Adapter<DetailImageDiplayAdapter.ImageHolder> {

    List<String> list;
    Context context;
    public DetailImageDiplayAdapter(Context context, List<String> list){

        this.list=list;
        this.context=context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.image_load_item,parent,false);
        ImageHolder holder=new ImageHolder(view);
        holder.setOnclickListener(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

        String  item=list.get(position);
        holder.loadImageDetail(item);
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Context context;
        ProgressBar progressBar;
        private String url;
        ImageView explore;

     public ImageHolder(View view){
        super(view);

         progressBar =(ProgressBar)view.findViewById(R.id.loadingBar);
         explore=(ImageView)view.findViewById(R.id.loading_image);
         context=view.getContext();
        }

        public void loadImageDetail(String url){
            this.url=url;
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.loading_image){
                ///perfom network operation of loading getting image
                ///opening image in the gallery application
               // Intent intent = new Intent(this.context, ModuleActivity.class);
               // Toast.makeText(context,"am working ",Toast.LENGTH_LONG).show();
                //context.startActivity(intent);
            }

        }

        public void setOnclickListener(View view){
             explore.setOnClickListener(ImageHolder.this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
