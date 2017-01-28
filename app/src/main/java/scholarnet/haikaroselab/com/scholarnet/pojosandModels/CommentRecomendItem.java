package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

/**
 * Created by root on 3/15/16.
 */
public class CommentRecomendItem {


    public CommentRecomendItem(){

    }
    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    private int recommend;
    private int comment;

}
