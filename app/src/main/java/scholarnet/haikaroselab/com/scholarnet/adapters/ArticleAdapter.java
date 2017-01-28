package scholarnet.haikaroselab.com.scholarnet.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ArticleItem;
import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;
import scholarnet.haikaroselab.com.scholarnet.activities.EditArticleActivity;
import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.LoginPreferencesChecker;
import scholarnet.haikaroselab.com.scholarnet.R;
import scholarnet.haikaroselab.com.scholarnet.activities.AmazingActivity;

/**
 * Created by root on 3/8/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    List<ArticleItem> list;
    Context context;

    public ArticleAdapter(Context context, List<ArticleItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_item, parent, false);
        ArticleViewHolder holder = new ArticleViewHolder(view);
        holder.setOnClickListener(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        ArticleItem item = list.get(position);
        holder.setArticleDetails(item.getDate(), item.getUploader(), item.getDescription(),
                item.getComments(), item.getImages(), item.getRecomendation(), position, item.getPhone(),item.getId());
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView date;
        String id;
        TextView user;
        TextView detail;
        TextView comments;
        TextView images;
        TextView recommendation;ImageView article_option;

        LayoutInflater inflater;
        PopupWindow popupWindow;
        View popUpWindowView;
        int currentPos;
        boolean toggle = true;
        String phone;

        public ArticleViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.date);
            user = (TextView) view.findViewById(R.id.user);
            detail = (TextView) view.findViewById(R.id.detail);
            comments = (TextView) view.findViewById(R.id.comment_count);
            images = (TextView) view.findViewById(R.id.image_count);
            recommendation = (TextView) view.findViewById(R.id.recommendation_count);
            article_option = (ImageView) view.findViewById(R.id.article_option);
            context = view.getContext();

        }

        public void setArticleDetails(String date, String uploader, String details, int comments,
                                      int images, int recommendation, int position, String phone,int id) {

            this.id=Integer.toString(id);
            this.currentPos = position;
            this.date.setText("shared :"+date);

            this.user.setText(Html.fromHtml("<b>" + uploader + "</b>"));

            if(details.length()>250){

                this.detail.setText(Html.fromHtml("<font>"+details.substring(0,250)+"</font>"+
                        "<font color='#ff0000'> ... continue</font"));
            }else{
                this.detail.setText(details);
            }

            this.comments.setText(Integer.toString(comments));
            this.images.setText(Integer.toString(images));
            this.recommendation.setText(Integer.toString(recommendation));
            this.phone = phone;
            if (phone.equalsIgnoreCase(LoginPreferencesChecker.logEventGetPhone(context))) {
            } else {
               // article_option.setVisibility(View.GONE);
            }
        }

        public void setOnClickListener(View view) {
            detail.setOnClickListener(ArticleViewHolder.this);
            article_option.setOnClickListener(ArticleViewHolder.this);
        }

        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.detail) {

                if (toggle) {

                    int point = ArticleViewHolder.this.getAdapterPosition();
                    ArticleItem item = list.get(point);
                    Intent intent = new Intent(ArticleViewHolder.this.context, AmazingActivity.class);
                    intent.putExtra("articleId", item.getId());
                    intent.putExtra("detail", item.getDescription());
                    intent.putExtra("poster", item.getUploader());
                    intent.putExtra("imageCount",item.getImages());
                    ArticleViewHolder.this.context.startActivity(intent);
                    //old was more detail activity

                } else {
                    popupWindow.dismiss();
                    toggle = true;
                }

            } else if (v.getId() == R.id.article_option) {

                //am ready for popup menu emmanuel
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popUpWindowView = inflater.inflate(R.layout.activity_popup, null);

                TextView edit = (TextView) popUpWindowView.findViewById(R.id.edit_text_view);
                TextView delete = (TextView) popUpWindowView.findViewById(R.id.delete_text_view);

                ///setting the listeners for edit and deleting//////////////////////////////////////

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindow.dismiss();
                        Intent intent=new Intent(v.getContext(),EditArticleActivity.class);
                        intent.putExtra("passedArticle", detail.getText().toString().trim());
                        intent.putExtra("articleId", id);
                        context.startActivity(intent);
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(currentPos, list);
                        Log.e("delete", "helo delete");
                        popupWindow.dismiss();

                    }
                });


                popupWindow = new PopupWindow(popUpWindowView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
                popupWindow.setAnimationStyle(R.style.Animation);
                //popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);


                if (toggle) {

                    if (LoginPreferencesChecker.logEventGetPhone(context).equalsIgnoreCase(phone)) {
                        // Show popup here
                       // popupWindow.showAsDropDown(v);
                        popupWindow.showAtLocation(v,Gravity.CENTER,0,0);
                        toggle = false;
                    }
                } else {
                    if (popupWindow != null && popupWindow.isShowing())
                        popupWindow.dismiss();
                    toggle = true;
                }

            }
        }

        //removing item
        public void removeItem(int position, List<ArticleItem> list) {

            //deleting the item in real life/////////////////////

            ArticleItem item = list.get(position);
            deleteArticle(item.getId());
            Log.e("item",Integer.toString(item.getId()));

            list.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position, list.size());
            notifyItemRangeRemoved(0, getItemCount());


        }
    }

    public void deleteArticle(final int id) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post", id);

        client.get(context, CommonInformation.DELETE_POST_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.e("delete process started","start");
                Log.e("id",Integer.toString(id));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.e("article","article deletion failed");
                Log.e("article",throwable.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.e("article","article delete passed successfully");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
