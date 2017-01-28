package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.constants.Colors;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.CorrectSubjectsItemAdapter;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.SubjectItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.SubjectListJSONParser;

public class ModulesListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    private String courseId;
    private RecyclerView subjectsRecycler;
    private Context context;
    private TextView emptyMessage;
    private View retry_view;
    private List<SubjectItem> subjectsItems = new ArrayList<>();

    public ModulesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModulesListFragment newInstance(String param1, String param2) {
        ModulesListFragment fragment = new ModulesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.e("event","oncreate");
        courseId=mParam1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_module_fragment, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar5);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(Colors.primary),android.graphics.PorterDuff.Mode.SRC_IN);

        subjectsRecycler = (RecyclerView)view.findViewById(R.id.subject_recyler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        subjectsRecycler.setItemAnimator(new DefaultItemAnimator());
        subjectsRecycler.setLayoutManager(layoutManager);
        emptyMessage = (TextView)view.findViewById(R.id.textView8);

        ///preparing the retry_view and empty message

        retry_view=view.findViewById(R.id.retry_view);
        retry_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getAllSubjects();
            }
        });

        emptyMessage.setVisibility(View.INVISIBLE);
        retry_view.setVisibility(View.INVISIBLE);

        ///////////////////////////////////////////////////

        if(!(subjectsItems.size()>0)){
            getAllSubjects();
            Log.e("request","you have to log us emmanuel");
        }else{
            CorrectSubjectsItemAdapter adapter = new CorrectSubjectsItemAdapter(context, subjectsItems);
            subjectsRecycler.setAdapter(adapter);
            emptyMessage.setVisibility(View.INVISIBLE);
        }

        Log.e("event", "oncreate view");
        return view;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        Log.e("event","onattach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getAllSubjects() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20 * 1000);
        client.setConnectTimeout(20 * 1000);
        client.setResponseTimeout(20 * 1000);
        RequestParams params = new RequestParams();
        params.put("course", courseId);
        params.put("user", LoginPreferencesChecker.logEventGetPhone(context));

        client.get(CommonInformation.SUBJECT_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

                progressBar.setVisibility(View.VISIBLE);
                emptyMessage.setVisibility(View.INVISIBLE);
                retry_view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
                Log.e("prob", (throwable).toString());
                for(int trial=0;trial<4;trial++){
                    if(throwable.toString().contains("java.net.SocketTimeoutException")){
                        Log.e("am in emmanuel", "its socket and i can handle it ");
                        getAllSubjects();
                        break;
                    }else{
                        break;
                    }
                }
                if(subjectsItems.size()<1){
                    emptyMessage.setText("could not load module list, please try again later");
                    emptyMessage.setVisibility(View.VISIBLE);
                    retry_view.setVisibility(View.VISIBLE);
                }else{
                    emptyMessage.setVisibility(View.INVISIBLE);
                    retry_view.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                subjectsItems = SubjectListJSONParser.getAllArticle(responseString);

                if (responseString.length() > 5) {
                    if (subjectsItems != null) {
                        CorrectSubjectsItemAdapter adapter = new CorrectSubjectsItemAdapter(context, subjectsItems);
                        subjectsRecycler.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyMessage.setVisibility(View.INVISIBLE);
                        retry_view.setVisibility(View.INVISIBLE);
                        // ScholarNetDatabase database = new ScholarNetDatabase(getBaseContext());
                        // database.insertSubjects(subjectsItems);
                    }
                } else {
                    emptyMessage.setText("No subject have been registered for your course");
                    progressBar.setVisibility(View.INVISIBLE);
                    retry_view.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.VISIBLE);
                }

            }
        });
    }

}
