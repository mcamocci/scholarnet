package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CommentRecomendItem;

/**
 * Created by root on 3/15/16.
 */
public class CommentRecommendJSONItemParser {

    public static List<CommentRecomendItem> getCommentRecommedItem(String content){

        List<CommentRecomendItem> list=new ArrayList<>();
        try {
            JSONArray array=new JSONArray(content);
            for(int i=0;i<array.length();i++){
                CommentRecomendItem item=new CommentRecomendItem();
                JSONObject object=array.getJSONObject(i);
                item.setComment(object.getInt("comments"));
                item.setRecommend(object.getInt("recommendations"));
                list.add(item);
            }
        } catch (JSONException e) {
           return null;
        }
        return list;
    }

}
