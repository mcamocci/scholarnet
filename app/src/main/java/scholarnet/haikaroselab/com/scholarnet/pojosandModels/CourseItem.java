package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

/**
 * Created by root on 3/11/16.
 */
public class CourseItem {

    private String name;
    private String code;
    private int id;

    public CourseItem(){}

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setCode(String code){
        this.code=code;
    }
    public String getCode(){
        return this.code;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return  this.id;
    }

}
