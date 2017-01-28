package scholarnet.haikaroselab.com.scholarnet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Random;

import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.beaPackage.ModulePostActivityFun;
import scholarnet.haikaroselab.com.scholarnet.constants.Colors;
import scholarnet.haikaroselab.com.scholarnet.pojosandModels.SubjectItem;

/**
 * Created by root on 3/8/16.
 */
public class CorrectSubjectsItemAdapter extends RecyclerView.Adapter<CorrectSubjectsItemAdapter.SubjectItemHolder> {

    public static int currentId;
    List<SubjectItem> list;
    Context context;


    public CorrectSubjectsItemAdapter(Context context, List<SubjectItem> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public SubjectItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        SubjectItemHolder holder = new SubjectItemHolder(view);
        holder.setOnclickListener(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SubjectItemHolder holder, int position) {

        SubjectItem item = list.get(position);
        holder.setMenuDetail(item, position);
    }

    public class SubjectItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView subject;
        TextView explore;
        LinearLayout beginButton;
        TextView title_short;
        LinearLayout circleView;

        public SubjectItemHolder(View view) {
            super(view);

            beginButton=(LinearLayout)view.findViewById(R.id.beginButton);
            subject = (TextView) view.findViewById(R.id.subject);
            explore = (TextView) view.findViewById(R.id.explore);
            title_short=(TextView)view.findViewById(R.id.title_short);
            circleView=(LinearLayout)view.findViewById(R.id.circle_view_background);
            //explore = (LinearLayout) view.findViewById(R.id.explore);
            context = view.getContext();
        }

        public void setMenuDetail(SubjectItem subject, int position) {

            this.explore.setText(subject.getCode().toUpperCase());
            this.title_short.setText(subject.getName().substring(0, 1).toUpperCase());
            GradientDrawable gradientDrawable=(GradientDrawable)circleView.getBackground();


            int randomInt =new Random().nextInt(18);
            gradientDrawable.setColor(Color.parseColor(Colors.colorsMaterial[randomInt]));

            if(subject.getName().length()>35){
                this.subject.setText(subject.getName().substring(0,1).toUpperCase() + subject.getName().substring(1,32)+"...");
            } else {
                this.subject.setText(subject.getName().substring(0,1).toUpperCase()+subject.getName().substring(1));
            }


        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.beginButton) {
                Intent intent = new Intent(this.context, ModulePostActivityFun.class);
                ///this is interesting
                long i = SubjectItemHolder.this.getItemId();

                int trythis = getIdSuper(SubjectItemHolder.this.getAdapterPosition());
                intent.putExtra("id", trythis);
                intent.putExtra("title",getCodeSuper(SubjectItemHolder.this.getAdapterPosition()));
                context.startActivity(intent);
            }

        }
        public void setOnclickListener(View view) {
            beginButton.setOnClickListener(SubjectItemHolder.this);

        }
        /////////////////////////////////////////////////////////////
        public int getIdSuper(long value) {
            SubjectItem item = list.get((int) (value));
            return item.getId();
        }
        /////////////////////////////////////////////////////////////
        public String  getCodeSuper(long value) {
            SubjectItem item = list.get((int) (value));
            return item.getCode();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
