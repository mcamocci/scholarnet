package scholarnet.haikaroselab.com.scholarnet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.activities.ProfileActivity;
import scholarnet.haikaroselab.com.scholarnet.activities.ReportProblemActivity;
import scholarnet.haikaroselab.com.scholarnet.adapters.MenuItemAdapter;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.MenuItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;


public class DrawerMenuFragment extends Fragment {


    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    //RecyclerView recyclerView;

    LayoutInflater inflater;
    Context context;
    private TextView username_text_view;
    public DrawerMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=super.getContext();
        View view=inflater.inflate(R.layout.fragment_drawer_menu, container, false);
        TextView textView=(TextView)view.findViewById(R.id.quote);
        textView.setText(Html.fromHtml("“Share your knowledge. It is a way to achieve immortality.” " +
                "<font color='#ff0000'><i>Dalai Lama XIV</i></font>"));

        initializeRecyclerView(view);
        initializeOtherView(view);


        return view;
    }


    public void setDrawer(DrawerLayout layout,Toolbar toolbar){
        this.drawerLayout=layout;

        toggle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(toggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                toggle.syncState();
            }
        });
    }

public void initializeRecyclerView(View view){

    ArrayList<String> list=new ArrayList<>();
    list.add("account");
    list.add("About");
    list.add("Report problem");
    list.add("Notification");


    final List<MenuItem> listMenu=new ArrayList<>();
    for(String item:list){
        MenuItem item1=new MenuItem();
        item1.setDescription(item);
        listMenu.add(item1);
    }

    ListView listView=(ListView)view.findViewById(R.id.menu_list);
    listView.setAdapter(new MenuItemAdapter(context,listMenu));

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String clicked=listMenu.get(position).getDescription();
            if(clicked.equalsIgnoreCase("about")){

                if(drawerLayout!=null){
                    drawerLayout.closeDrawers();
                }

            }else if(clicked.equalsIgnoreCase("notification")){


                if(drawerLayout!=null){
                    drawerLayout.closeDrawers();
                }

            }else if(clicked.equalsIgnoreCase("report problem")){
                Intent intent=new Intent(context,ReportProblemActivity.class);
                startActivity(intent);
                if(drawerLayout!=null){
                    drawerLayout.closeDrawers();
                }
            }else if(clicked.equalsIgnoreCase("account")){
                if(drawerLayout!=null){
                    Intent intent=new Intent(context,ProfileActivity.class);
                    context.startActivity(intent);
                    drawerLayout.closeDrawers();
                }
            }
        }
    });

}
    public void initializeOtherView(View view){

        username_text_view=(TextView)view.findViewById(R.id.user_name);
        while(true){
            Log.e("user", "hello am trying to get the user");
            if ((LoginPreferencesChecker.logEventGetUser(context)).contains("haha")){
                username_text_view.setText(LoginPreferencesChecker.logEventGetPhone(context));
                break;
            }else{
               username_text_view.setText(LoginPreferencesChecker.logEventGetUser(context));
                break;
            }

        }
    }

}
