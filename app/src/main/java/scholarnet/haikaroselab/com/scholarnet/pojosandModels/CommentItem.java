package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

import android.graphics.ComposePathEffect;

/**
 * Created by root on 3/14/16.
 */
public class CommentItem {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    private int id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private String poster;
    private String info;

    public CommentItem(){

    }
}
