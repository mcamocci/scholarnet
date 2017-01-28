package scholarnet.haikaroselab.com.scholarnet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CommentItem;
import scholarnet.haikaroselab.com.scholarnet.R;

/**
 * Created by root on 3/8/16.
 */
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.CommentItemHolder> {

    List<CommentItem> list;
    Context context;

    public CommentItemAdapter(Context context, List<CommentItem> list) {
        this.list = list;
        this.context = context;
    }


    @Override
    public CommentItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentItemHolder holder = new CommentItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentItemHolder holder, int position) {

        CommentItem itemHolder = list.get(position);
        holder.setCommentDetail(itemHolder.getDate(), itemHolder.getPoster(), itemHolder.getInfo());

    }

    public class CommentItemHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView date;
        TextView user;
        TextView comments;


        public CommentItemHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            user = (TextView) view.findViewById(R.id.commenter);
            comments = (TextView) view.findViewById(R.id.comment_detail);
            context = view.getContext();
        }

        public void setCommentDetail(String date, String uploader, String comments) {
            this.date.setText(date);
            this.user.setText(uploader);
            this.comments.setText(comments);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
