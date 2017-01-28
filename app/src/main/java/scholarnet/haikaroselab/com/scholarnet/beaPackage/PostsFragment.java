package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.adapters.ArticleAdapter;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ArticleItem;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.ArticleListJSONParser;
import scholarnet.haikaroselab.com.scholarnet.constants.Colors;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
public class PostsFragment extends Fragment {

    private ProgressBar progressBar;
    private ArticleAdapter adapter;
    private int page=1;


    View wholeLayerRetry;
    private int subjectId;
    private RecyclerView recyclerView;
    private TextView noContent;
    private Context context;
    private View fragmentView;

    private List<ArticleItem> allArticles=new ArrayList<>();
    private List<ArticleItem> moreItems=new ArrayList<>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public PostsFragment() {
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
    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("he he","am called oncreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("he he","am called oncreateview");
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_posts, container, false);

        //setting the fragment view
        fragmentView=view;
        //adapter initialization is done here
        recyclerView=(RecyclerView)view.findViewById(R.id.article_list);

        if(recyclerView==null){
            Log.e("recycler","null");
        }



        adapter=new ArticleAdapter(context,allArticles);
        recyclerView.setAdapter(adapter);


        customLoadMoreDataFromApi(page);

        wholeLayerRetry=(View)view.findViewById(R.id.textView17);
        wholeLayerRetry.setVisibility(View.INVISIBLE);
        
        wholeLayerRetry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                customLoadMoreDataFromApi(page);
            }
        });

        progressBar=(ProgressBar)view.findViewById(R.id.progressBar4);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(Colors.primary),android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        noContent=(TextView)view.findViewById(R.id.textView12);
        noContent.setVisibility(View.INVISIBLE);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layout) {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                PostsFragment.this.page = page;
                customLoadMoreDataFromApi(page);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    //function for calling all objects

    public List<ArticleItem> requestsArticles(int offset){

        List<ArticleItem> articleItems=new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20 * 1000);
        client.setConnectTimeout(20 * 1000);
        client.setResponseTimeout(20 * 1000);
        RequestParams params = new RequestParams();
        params.put("phone", LoginPreferencesChecker.logEventGetPhone(context));
        params.put("offset", offset);


        client.get(CommonInformation.ARTICLE_ALL_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    noContent.setVisibility(View.INVISIBLE);
                    wholeLayerRetry.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
               // emptyMessage.setText("Could not connect to the internet , try again later");
                Log.e("error message", throwable.toString());
               // emptyMessage.setVisibility(View.VISIBLE);
                noContent.setVisibility(View.INVISIBLE);
                wholeLayerRetry.setVisibility(View.VISIBLE);
                ///setting listeners for the retry
                LinearLayout fakeButton = (LinearLayout) wholeLayerRetry.findViewById(R.id.amfake);
                //listeners for retry button
                fakeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("dont ", " i will work forever worry");
                        requestsArticles(page);
                    }
                });

                if(allArticles.size()<1){
                    noContent.setText("could not fetch data online, please try again later");
                    noContent.setVisibility(View.VISIBLE);
                }else{
                    noContent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                if (responseString.length() > 5) {

                    List<ArticleItem> downloadedArticle = ArticleListJSONParser.getAllArticle(responseString);

                    if (downloadedArticle.size() > 0) {

                        allArticles.addAll(downloadedArticle);

                        //allArticles.addAll(downloadedArticle);
                        final int curSize = adapter.getItemCount();
                        adapter.notifyItemRangeInserted(curSize, allArticles.size() - 1);


                    } else {
                        Log.e("hehe", "we are not good");
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                    wholeLayerRetry.setVisibility(View.INVISIBLE);
                    noContent.setVisibility(View.INVISIBLE);

                } else {
                    if (responseString.contains("[]")) {

                        progressBar.setVisibility(View.INVISIBLE);
                        noContent.setVisibility(View.INVISIBLE);
                        if(allArticles.size()<1){
                            wholeLayerRetry.setVisibility(View.VISIBLE);
                            noContent.setText("Content not availlable for this course, to publish content please go to module page");
                            noContent.setVisibility(View.VISIBLE);

                        }else{
                            wholeLayerRetry.setVisibility(View.INVISIBLE);
                            noContent.setVisibility(View.INVISIBLE);
                            Snackbar snack= Snackbar.make(getActivity().findViewById(android.R.id.content), "No more articles to load", Snackbar.LENGTH_LONG);
                            View view=snack.getView();
                            view.setBackgroundColor(Color.parseColor(Colors.primary));
                            TextView snackText=(TextView)view.findViewById(android.support.design.R.id.snackbar_text);
                            snackText.setTextColor(Color.parseColor("#ffffff"));
                            snackText.setGravity(Gravity.CENTER_HORIZONTAL);
                            snack.show();

                        }


                    } else {

                        progressBar.setVisibility(View.INVISIBLE);
                        noContent.setVisibility(View.VISIBLE);
                        wholeLayerRetry.setVisibility(View.VISIBLE);
                        LinearLayout fakeButton = (LinearLayout) wholeLayerRetry.findViewById(R.id.amfake);

                        //listeners for retry button
                        fakeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("dont ", " i will work forever worry");
                                requestsArticles(page);
                            }
                        });


                    }

                }
            }
        });

        return articleItems;
    }

    ////without forgetting this method fellas.....................

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter

        //moreItems=requestsArticles(offset);
        requestsArticles(offset);
    }

}
