package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import scholarnet.haikaroselab.com.scholarnet.constants.CommonInformation;

/**
 * Created by root on 3/20/16.
 */
public class FileOperations {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static  void displayWhenAvaillable(Context context,File file,ImageView image){

        if(file.exists()){

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            // Calculate inSampleSize
            options.inSampleSize = ImageEfficient.calculateInSampleSize(options, 200, 200);
            ///// Decode bitmap with inSampleSize set///////////////////
            options.inJustDecodeBounds = false;
            Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            image.requestLayout();
            image.getLayoutParams().height= ActionBar.LayoutParams.WRAP_CONTENT;
            image.getLayoutParams().width=ActionBar.LayoutParams.WRAP_CONTENT;
            image.setImageBitmap(bitmap);
        }
    }

    public static void writeFileOperation(Context context,File file,String originalName){

        ///checking for access of writting external storage of the device
        if (FileOperations.isExternalStorageWritable()) {
            ///creating app Folder//////////////////////////

            File appFolder=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"" +
                    "scholarnet"+File.separator+"Scholarnet images");

            ////creating folder when the stated folder above isnt availlable//////
            if(!appFolder.exists()){
                appFolder.mkdirs();
                //Toast.makeText(context,"folder created",Toast.LENGTH_LONG).show();
            }
            File copied=new File(appFolder+File.
                    separator+originalName);
            try {
                InputStream in=new FileInputStream(file.getAbsolutePath());
                OutputStream out=new FileOutputStream(copied);
                byte[] buff=new byte[1024];
                int len;
                while((len=in.read(buff))>0){
                    out.write(buff,0,len);
                }
                in.close();
                out.close();

               // Toast.makeText(context,"i have writtent the file)",Toast.LENGTH_LONG).show();
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment
                        .getExternalStorageDirectory())));
            } catch (IOException e) {
               // Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context,"External storage is not accessible for write",Toast.LENGTH_LONG).show();
        }
    }

    public static String getAppFolder(){
        return Environment.getExternalStorageDirectory()+File.separator+"scholarnet"+File.separator
                +"scholarnet images";
    }

    //this method seems to be redundant but its for image selection
    public static  void displayWhenAvaillable(Context context,File file,ImageView image,int width,int height){

        if(file.exists()){

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            // Calculate inSampleSize
            options.inSampleSize = ImageEfficient.calculateInSampleSize(options,width,height);
            ///// Decode bitmap with inSampleSize set///////////////////
            options.inJustDecodeBounds = false;
            Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            image.requestLayout();
            image.getLayoutParams().height=height;
            image.getLayoutParams().width=width;
            image.setImageBitmap(bitmap);
        }
    }

    ///this code is downloading Documents and saving them

    public static void writeDocumentOperation(Context context,String url){

        Log.e("url", CommonInformation.COMMON+"static/"+url);
        ///checking for access of writting external storage of the device
        if (FileOperations.isExternalStorageWritable()) {
            ///creating app Folder//////////////////////////

            File appFolder=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"" +
                    "scholarnet"+File.separator+"Scholarnet documents");

            ////creating folder when the stated folder above isnt availlable//////
            if(!appFolder.exists()){
                appFolder.mkdirs();
            }
            File copied=new File(appFolder+File.
                    separator+new File(url).getName());
            try {
                URL urls=new URL(CommonInformation.COMMON+url);
                HttpURLConnection connection=null;
                connection=(HttpURLConnection)urls.openConnection();
                connection.connect();

                Log.e("code",Integer.toString(connection.getResponseCode()));
                Log.e("size",Integer.toString(connection.getContentLength()));
                InputStream in=connection.getInputStream();

                OutputStream out=new FileOutputStream(copied);
                byte[] buff=new byte[1024];
                int len;
                while((len=in.read(buff))>0){
                    out.write(buff,0,len);
                    Log.e("bytes",Integer.toString(len));
                }
                in.close();
                out.close();
                Log.e("download process","finished like charm");

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment
                        .getExternalStorageDirectory())));
            } catch (IOException e) {
                // Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }
        }else{
            Log.e("external storage","not writable");
            Toast.makeText(context,"External storage is not accessible for write",Toast.LENGTH_LONG).show();
        }
    }
}
