package scholarnet.haikaroselab.com.scholarnet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by root on 3/8/16.
 */
public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.MenuHolder> {

    List<String> list;
    DrawerLayout layout;
    Context context;
    public DrawerItemAdapter(Context context, List<String> list,DrawerLayout layout){
        this.list=list;
        this.context=context;
        this.layout=layout;
    }


    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item,parent,false);
        MenuHolder holder=new MenuHolder(view);
        holder.setListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {

        String  item=list.get(position);
        holder.setMenuDetail(item);
    }

    public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView detail;
        ImageView image;
     public MenuHolder(View view){
        super(view);

         detail=(TextView)view.findViewById(R.id.details);
        }

        public void setMenuDetail(String details){
           this.detail.setText(details);
        }

        @Override
        public void onClick(View v) {

        }

        public void setListeners(){
            detail.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
