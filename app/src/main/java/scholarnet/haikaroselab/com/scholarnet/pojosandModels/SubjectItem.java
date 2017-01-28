package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

/**
 * Created by root on 3/10/16.
 */
public class SubjectItem {


    public SubjectItem(){}
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private String code;
}
