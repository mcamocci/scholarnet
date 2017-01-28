package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.UserYearCourseId;

/**
 * Created by root on 3/12/16.
 */
public class UserYearCourseIdItemParser {

    public static UserYearCourseId getUserYear(String string){

        List<UserYearCourseId> list=new ArrayList<>();
        try {
            JSONArray array=new JSONArray(string);

            for (int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);
                UserYearCourseId item=new UserYearCourseId();
                ////////the set up of objects/////////////////////
                item.setName(object.getString("display"));
                item.setYear((object.getInt("year")));
                item.setCourseId(object.getInt("courseId"));
                list.add(item);
            }
        } catch (JSONException e) {
            return null;
        }
        return list.get(0);
    }
}
