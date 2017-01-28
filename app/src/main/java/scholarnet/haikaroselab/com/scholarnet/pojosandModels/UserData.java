package scholarnet.haikaroselab.com.scholarnet.pojosandModels;

/**
 * Created by root on 3/24/16.
 */
public class UserData {

    public UserData(){

    }
    private String phone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String password;
    private String user_name;
    public void setUser_name(String name){
        this.user_name=user_name;
    }
    public String getUser_name(){
        return this.user_name;
    }

}
