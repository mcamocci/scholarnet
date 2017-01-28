package scholarnet.haikaroselab.com.scholarnet.toolsandlibraries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import scholarnet.haikaroselab.com.scholarnet.pojosandModels.SubjectItem;

/**
 * Created by root on 3/10/16.
 */
public class ScholarNetDatabase extends SQLiteOpenHelper {


    public ScholarNetDatabase(Context context) {
        super(context, "ScholarNetDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_subject_table = "CREATE TABLE IF NOT EXISTS subject " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,code TEXT)";
        db.execSQL(create_subject_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String drop_subject_table = "DROP TABLE IF EXISTS subject";
        db.execSQL(drop_subject_table);
        onCreate(db);
    }

    public void insertSubjects(List<SubjectItem> subjects) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (SubjectItem item : subjects) {
            values.put("id", item.getId());
            values.put("title", item.getName());
            values.put("code", item.getCode());
            db.insert("subject", null, values);
        }
    }

    public ArrayList<SubjectItem> getAllSubjects() {

        String querry = "SELECT * FROM subject";
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<SubjectItem> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(querry, null);

        while (cursor.moveToNext()) {

            SubjectItem subject = new SubjectItem();
            subject.setId(cursor.getInt(0));
            subject.setName(cursor.getString(1));
            subject.setCode(cursor.getString(2));
            list.add(subject);
        }

        return list;
    }
}
