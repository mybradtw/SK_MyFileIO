package tw.brad.myfileio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    private String createTable =
            "CREATE TABLE sakura (id INTEGER PRIMARY KEY AUTOINCREMENT,cname TEXT,tel TEXT,birthday DATE)";
    private String createTable2 =
            "CREATE TABLE myorder (id INTEGER PRIMARY KEY AUTOINCREMENT,sid INTEGER,pname TEXT,qty INTEGER)";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
        Log.v("brad", "OK1");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(createTable2);
        Log.v("brad", "OK2");
    }
}
