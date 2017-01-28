package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.DescriptionItem;

public class DescriptionParser {

	public static List<DescriptionItem> parseJson(String string){

			List<DescriptionItem> descriptionItemsList=new ArrayList<DescriptionItem>();

		try {

			JSONArray array=new JSONArray(string);

			for(int i=0;i<array.length();i++){

				JSONObject object=array.getJSONObject(i);
				DescriptionItem descriptionItem=new DescriptionItem();
				//descriptionItem.setName(object.getString("name"));
				//descriptionItem.setAddress(object.getString("address"));

				descriptionItemsList.add(descriptionItem);

	}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return descriptionItemsList;
	}
}
