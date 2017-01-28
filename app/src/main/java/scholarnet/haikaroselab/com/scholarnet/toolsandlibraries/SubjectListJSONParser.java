package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.SubjectItem;

/**
 * Created by root on 3/12/16.
 */
public class SubjectListJSONParser {

    public static List<SubjectItem> getAllArticle(String string){

        List<SubjectItem> items=new ArrayList<>();

        try {
            JSONArray array=new JSONArray(string);

            for (int i=0;i<array.length();i++){

                JSONObject object=array.getJSONObject(i);
                SubjectItem item=new SubjectItem();

                ////////the set up of objects/////////////////////

                item.setId(object.getInt("id"));
                item.setName(object.getString("name"));
                item.setCode(object.getString("code"));

                ////////the set up of objects/////////////////////
                items.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }
}
