package scholarnet.haikaroselab.com.scholarnet.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.MenuItem;


/**
 * Created by root on 9/28/15.
 */
public class MenuItemAdapter extends ArrayAdapter<MenuItem> {

    private Context context;
    private List<MenuItem> list;
    ImageView image_view;

    public MenuItemAdapter(Context context,List<MenuItem> objects){

        super(context, R.layout.drawer_item,objects);
        this.context=context;
        this.list=objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MenuItem item=list.get(position);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view=(View)inflater.inflate(R.layout.drawer_item, null);

        TextView detail_view = (TextView) view.findViewById(R.id.details);
        image_view = (ImageView) view.findViewById(R.id.icon);
        detail_view.setText(item.getDescription());

        if(list.get(position).getDescription().equalsIgnoreCase("about")){

            image_view.setImageResource(R.drawable.ic_info);

        }else if(list.get(position).getDescription().equalsIgnoreCase("notification")){

            image_view.setImageResource(R.drawable.ic_announcement);

        }else if(list.get(position).getDescription().equalsIgnoreCase("report problem")){

            image_view.setImageResource(R.drawable.ic_bug_report);

        }else if(list.get(position).getDescription().equalsIgnoreCase("account")){

            image_view.setImageResource(R.drawable.ic_account_circle);
        }


        return view;
    }

}
