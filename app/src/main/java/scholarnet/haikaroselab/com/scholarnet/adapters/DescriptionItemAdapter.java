package scholarnet.haikaroselab.com.scholarnet.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.DescriptionItem;

/**
 * Created by root on 9/28/15.
 */
public class DescriptionItemAdapter extends ArrayAdapter<DescriptionItem>{

    public DescriptionItemAdapter(Context context, int resources, List<DescriptionItem> objects){

        super(context,resources,objects);


    }
    /*
    private Context context;
    private int resources;
    private List<DescriptionItem> list;

    public DescriptionItemAdapter(Context context, int resources, List<DescriptionItem> objects){

        super(context,resources,objects);
        this.context=context;
        this.resources=resources;
        this.list=objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DescriptionItem item=list.get(position);

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view=(View)inflater.inflate(resources, null);

        //starting looking for images
        AsyncHttpClient client = new AsyncHttpClient();
        //RequestParams params = new RequestParams();

        client.post(CommonInformation.DESCRIPTION_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "process failed", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                //parsed_urls=DescriptionParser.parseJson(response);


                //testsEdit.setText(response);
            }

            @Override
            public void onStart() {

            }

        });

        //end looking for images
        return view;
    }

*/
}
