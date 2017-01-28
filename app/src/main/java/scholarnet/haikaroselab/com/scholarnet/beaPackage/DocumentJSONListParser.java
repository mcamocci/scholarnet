package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 5/16/16.
 */
public class DocumentJSONListParser {

    public static List<Document> getAllDocuments(String content){

        List<Document> myDocuments=new ArrayList<>();

        try {
            JSONArray array=new JSONArray(content);
            for(int i=0;i<array.length();i++){


                JSONObject object=array.getJSONObject(i);
                Document item=new Document();
                item.setCaption(object.getString("caption"));
                item.setUrl(object.getString("url"));
                item.setExtension(object.getString("extension"));
                item.setDate(object.getString("date"));
                myDocuments.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myDocuments;
    }
}
