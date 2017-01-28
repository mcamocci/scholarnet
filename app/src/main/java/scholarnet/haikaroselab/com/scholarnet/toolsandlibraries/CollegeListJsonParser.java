package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.CollegeItem;

/**
 * Created by root on 3/10/16.
 */
public class CollegeListJsonParser {

    public static List<CollegeItem> parseJson(String string){

        List<CollegeItem> collegeList=new ArrayList<>();


        try{

            JSONArray array=new JSONArray(string);

            for(int i=0;i<array.length();i++){

                JSONObject object=array.getJSONObject(i);
                CollegeItem collegeItem=new CollegeItem();
                collegeItem.setName(object.getString("name"));
                collegeItem.setId(object.getInt("id"));
                collegeItem.setShortName(object.getString("code"));
                collegeList.add(collegeItem);
            }

        }catch(JSONException ex){

        }
        return collegeList;
    }
}
