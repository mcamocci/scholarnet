package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CommentItem;

/**
 * Created by root on 3/14/16.
 */
public class CommentsListJSONParser {

    public static List<CommentItem> getCommentsList(String content) {
        List<CommentItem> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                CommentItem item = new CommentItem();
                item.setId(object.getInt("id"));
                item.setPoster(object.getString("poster"));
                item.setInfo(object.getString("info"));
                item.setDate(object.getString("date"));
                list.add(item);
            }

        } catch (JSONException ex) {
            return null;
        }

        return list;
    }
}
