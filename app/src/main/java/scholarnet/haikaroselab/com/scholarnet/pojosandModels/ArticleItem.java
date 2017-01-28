package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

import java.util.List;

/**
 * Created by root on 3/3/16.
 */
public class ArticleItem {

    private int id;
    private String description;
    private String uploader;
    private String date;
    private int recomendation;
    private int comments;
    private int images;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }


    public int getRecomendation() {
        return recomendation;
    }

    public void setRecomendation(int recomendation) {
        this.recomendation = recomendation;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }



    public ArticleItem(){

    }

    public ArticleItem(int id, String description, String uploader, String latitude, String longitude, String date) {
        this.id = id;
        this.description = description;
        this.uploader = uploader;
        this.date = date;
    }



    public List<String> getImages_url() {
        return images_url;
    }

    public void setImages_url(List<String> images_url) {
        this.images_url = images_url;
    }

    private List<String> images_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
