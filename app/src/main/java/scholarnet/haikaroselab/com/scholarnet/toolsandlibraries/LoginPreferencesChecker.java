package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.UserData;

/**
 * Created by root on 3/7/16.
 */
public class LoginPreferencesChecker {


    //////THIS CODE return stored user information when the user is logged in

    public static UserData getUserData(Context context){

        UserData userData=new UserData();
        SharedPreferences login=context.getSharedPreferences("USER",0);
        String phone=login.getString("phone", "null");
        String password=login.getString("password","null");
        userData.setPassword(password);
        userData.setPhone(phone);
        return userData;
    }

    //////THIS CODE IS FOR CHECKING IF THE USER IS LOGGED IN

    public static boolean isLogedIn(Context context){
        SharedPreferences login=context.getSharedPreferences("USER",0);
        if((login.getString("phone","null").equalsIgnoreCase("null"))
                || (login.getString("password","null").equalsIgnoreCase("null"))){
            return false;
        }
        return true;
    }

    //////THIS CODE IS FOR SAVING LOGIN STATUS OF THE USER

    public static void logUser(String phone,String password,Context context){
        SharedPreferences login=context.getSharedPreferences("USER",0);
        SharedPreferences.Editor editor=login.edit();
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.commit();
    }

    //////THIS CODE IS FOR LOGING OUT THE USER AND CLEARING LOGIN INFORMATION

    public static void clearLoggInfo(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("USER", 0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    ////////////////////////////////////HELLO THE CODE BELLOW IS FOR KEEPING USER NUMBER /////////////////////////////////

    ///this is for clearing and signing in the user while staying with phone!!
    public static void logEvent(Context context,String phone){
        /////clearing everything
        SharedPreferences sharedPreferences=context.getSharedPreferences("LOG_EVENT",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        ////storing phone number
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        SharedPreferences.Editor edit=login.edit();
        edit.putString("phone", phone);
        edit.commit();

    }


    ///this is for getting the phone number of the commented post later emmanuel!!
    public static String logEventGetPhone(Context context){
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        String number=login.getString("phone", "scholarnet user");
        return number;
    }
    ///this is for getting the phone number of the commented post later emmanuel!!
    public static String logEventGetUser(Context context){
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        String number=login.getString("user","haha");
        return number;
    }

    ///this is for getting the courseId
    public static String logEventGetCourseId(Context context){
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        String number=login.getString("courseId","id");
        return number;
    }

    public static void logEventUserStoreYearAndStoreUserAndStoreCourseId(Context context,String phone,String user,String year,int courseId){
        /////clearing everything
        SharedPreferences sharedPreferences=context.getSharedPreferences("LOG_EVENT",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        ///////////storing phone//////////////////
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        SharedPreferences.Editor edit=login.edit();
        edit.putString("phone", phone);
        edit.commit();
        ////storing username
        edit.putString("user", user);
        edit.commit();
        ////storing year
        edit.putString("year", year);
        edit.commit();
        ////storing courseId
        edit.putString("courseId",Integer.toString(courseId));
        edit.commit();
        Log.e("store first course id", Integer.toString(courseId));
        Log.e("user", user);
        Log.e("year", year);
    }

    public static void logEventStoreUser(Context context,String phone,String user){
        /////clearing everything
        SharedPreferences sharedPreferences=context.getSharedPreferences("LOG_EVENT",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        ///////////storing phone//////////////////
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        SharedPreferences.Editor edit=login.edit();
        edit.putString("phone", phone);
        edit.commit();
        ////storing username
        edit.putString("user", user);

        edit.commit();

    }

    public static String logEventUserGetStoredYear(Context context){
        SharedPreferences login=context.getSharedPreferences("LOG_EVENT",0);
        String year=login.getString("year","year");
        return year;
    }

}
