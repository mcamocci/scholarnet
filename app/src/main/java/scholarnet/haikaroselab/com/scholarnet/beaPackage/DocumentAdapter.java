package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scholarnet.haikaroselab.com.scholarnet.constants.Colors;
import scholarnet.haikaroselab.com.scholarnet.R;

/**
 * Created by root on 3/8/16.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    List<Document> list;
    Context context;


    public DocumentAdapter(Context context, List<Document> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        DocumentViewHolder holder = new DocumentViewHolder(view);
        holder.setOnClickListener(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        Document item = list.get(position);
        holder.setArticleDetails(item.getId(),item.getDate(), item.getCaption(), item.getUrl(),item.getExtension(), position);
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView date;
        LinearLayout boxOfColor;
        View wholeView;
        String id;
        TextView caption;
        View circle;
        TextView ext;
        int currentPos;

        public DocumentViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.textView14);
            caption = (TextView) view.findViewById(R.id.textView13);
            wholeView=view;
            circle=wholeView.findViewById(R.id.imageView4);
            ext= (TextView) view.findViewById(R.id.extension);
            context = view.getContext();

            Log.e("log log","we were");
            boxOfColor=(LinearLayout)view.findViewById(R.id.linearLayout6);

        }

        public void setArticleDetails(int id,String date, String caption, String url,String extension,int position) {

            String filename =new File(url).getName();
            String filenameArray[] = filename.split("\\.");
            String extensions = filenameArray[filenameArray.length-1];
            this.id=Integer.toString(id);
            this.currentPos = position;
            this.date.setText("uploaded :"+date);
            this.ext.setText(extensions.toUpperCase());
            this.caption.setText(filename.substring(0,1).toUpperCase()+filename.substring(1,filename.length()).toLowerCase());

            Map<String,String> colors=new HashMap<>();

            colors.put( "xls","#4CAF50");
            colors.put( "pdf","#f44336");
            colors.put( "docx","#0D47A1");
            colors.put( "doc","#0D47A1");
            colors.put( "ppt","#ff7053");
            colors.put( "pptx","#ff7053");

            if(extension.equals("xls")){

                boxOfColor.setBackgroundColor(Color.parseColor(colors.get("xls")));

            }else if(extension.equals("pdf")){

                boxOfColor.setBackgroundColor(Color.parseColor(colors.get("pdf")));

            }else if(extension.equals("doc")){

                boxOfColor.setBackgroundColor(Color.parseColor(colors.get("doc")));

            }else if(extension.equals("ppt")){

                boxOfColor.setBackgroundColor(Color.parseColor(colors.get("ppt")));
                Toast.makeText(context,extension,Toast.LENGTH_SHORT).show();

            }else if(extension.equals("docx")){

                boxOfColor.setBackgroundColor(Color.parseColor(colors.get("docx")));

            }

            //giving the circle green color when the file exists.

            File document = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "" +
                    "scholarnet" + File.separator + "Scholarnet documents" + File.
                    separator + new File(list.get(currentPos).getUrl()).getName());
            if (document.exists()) {
                   ((GradientDrawable) circle.getBackground()).setColor((Color.parseColor(Colors.success)));
            }else{
                   ((GradientDrawable) circle.getBackground()).setColor(Color.parseColor(colors.get("pdf")));
            }

        }

        public void setOnClickListener(View view) {
            wholeView.setOnClickListener(DocumentViewHolder.this);
        }

        @Override
        public void onClick(View v) {


            File document = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "" +
                    "scholarnet" + File.separator + "Scholarnet documents" + File.
                    separator + new File(list.get(currentPos).getUrl()).getName());
            if (document.exists()) {
                MimeTypeMap map = MimeTypeMap.getSingleton();
                String ext = MimeTypeMap.getFileExtensionFromUrl(document.getName());
                String type = map.getMimeTypeFromExtension(ext);
                if (type == null)
                    type = "*/*";
                Intent openIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.fromFile(document);
                openIntent.setDataAndType(data, type);
                context.startActivity(openIntent);

            } else {
                Toast.makeText(context,"File not exists , download started",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DownloadDocumentIntentService.class);
                intent.putExtra("url", list.get(currentPos).getUrl());
                context.startService(intent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
