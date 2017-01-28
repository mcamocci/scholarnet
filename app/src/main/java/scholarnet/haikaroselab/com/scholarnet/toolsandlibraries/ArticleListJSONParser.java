package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.ArticleItem;

/**
 * Created by root on 3/12/16.
 */
public class ArticleListJSONParser {

    public static List<ArticleItem> getAllArticle(String string){

        List<ArticleItem> items=new ArrayList<>();

        Log.e("content", string);

        try {
            JSONArray array=new JSONArray(string);

            for (int i=0;i<array.length();i++){

                JSONObject object=array.getJSONObject(i);
                ArticleItem item=new ArticleItem();

                ////////the set up of objects/////////////////////

                item.setId(object.getInt("id"));
                item.setUploader(object.getString("display"));
                item.setDate(object.getString("updated"));
                item.setDescription(object.getString("description"));
                item.setRecomendation(object.getInt("recommendation"));
                item.setComments(object.getInt("comments"));
                item.setImages(object.getInt("images"));
                item.setPhone(object.getString("user"));

                ////////the set up of objects/////////////////////
                items.add(item);

            }
        } catch (JSONException e) {
            return null;
        }

        return items;
    }
}
