package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

/**
 * Created by root on 3/11/16.
 */
public class CollegeItem {


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private String shortName;

    public CollegeItem(){}

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
