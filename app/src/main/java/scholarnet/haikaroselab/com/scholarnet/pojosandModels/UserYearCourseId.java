package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

/**
 * Created by root on 4/5/16.
 */
public class UserYearCourseId {

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int year;
    private String name;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private int courseId;
}
