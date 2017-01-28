package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CourseItem;

/**
 * Created by root on 3/10/16.
 */
public class CourseListJSONParser {


    public static List<CourseItem> getCourseList(String resource){

        List<CourseItem> courseList=new ArrayList<>();

        try {
            JSONArray array=new JSONArray(resource);

            for(int i=0;i<array.length();i++){

                JSONObject object=array.getJSONObject(i);
                CourseItem item=new CourseItem();
                item.setId(object.getInt("id"));
                item.setName(object.getString("name"));
                courseList.add(item);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return courseList;
    }

}
