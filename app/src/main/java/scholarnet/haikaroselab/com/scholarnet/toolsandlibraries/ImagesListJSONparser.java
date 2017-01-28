package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ImageItem;

/**
 * Created by root on 3/14/16.
 */
public class ImagesListJSONparser {

    public static List<ImageItem> getAllImages(String content){

        List<ImageItem> list=new ArrayList<>();

        try {
            JSONArray array=new JSONArray(content);
            for (int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);

                ImageItem item=new ImageItem();
                item.setId(object.getInt("id"));
                item.setName(object.getString("image"));
                item.setUrl(object.getString("image"));
                list.add(item);
            }
        } catch (JSONException e) {

           return null;
        }
        return list;
    }
}
