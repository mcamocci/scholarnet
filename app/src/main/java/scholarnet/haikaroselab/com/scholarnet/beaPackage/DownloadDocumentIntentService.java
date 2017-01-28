package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import scholarnet.haikaroselab.com.scholarnet.toolsandlibraries.FileOperations;

public class DownloadDocumentIntentService extends IntentService {

    private String url;
    private Context context;
    private String originalName;

    public DownloadDocumentIntentService() {
        super("downloadImage");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        url=intent.getStringExtra("url");
        FileOperations.writeDocumentOperation(getBaseContext(),url);
    }


}
