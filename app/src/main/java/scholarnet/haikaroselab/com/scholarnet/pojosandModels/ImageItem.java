package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

import android.widget.ImageView;

/**
 * Created by root on 3/3/16.
 */
public class ImageItem {

    private String url;
    private String name;
    private int id;


    public ImageItem(){}

    public ImageItem(String name, String url){
        this.name=name;
        this.url=url;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String path) {
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
