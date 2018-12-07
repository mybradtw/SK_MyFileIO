package tw.brad.myfileio;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private File sdroot, approot, dcimPath;

    private MyDBHelper dbHelper;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    123);
        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        sdroot = Environment.getExternalStorageDirectory();

        dcimPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Log.v("brad", "dcim:" + dcimPath.getAbsolutePath());

        File downloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        Log.v("brad", downloadPath.getAbsolutePath());
        Log.v("brad", sdroot.getAbsolutePath());

        File[] files = downloadPath.listFiles();
        for (File file : files){
            Log.v("brad", file.getName() + ":" + (file.isFile()?"File":"Dir"));
        }


        approot = new File(sdroot, "Android/data/" +
                            getPackageName() + "/");
        if (!approot.exists()){
            Log.v("brad", "mkdir...");
            approot.mkdirs();
        }

        dbHelper = new MyDBHelper(this, "brad", null,1);
        database = dbHelper.getReadableDatabase();

    }


    public void test1(View view) {
        File test1 = new File(sdroot, "sakura.txt");
        try {
            FileWriter writer = new FileWriter(test1);
            writer.write("Hello, Sakura");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Save OK1", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("brad", e.toString());
        }
    }
    public void test2(View view) {
        File test1 = new File(approot, "sakura.txt");
        try {
            FileWriter writer = new FileWriter(test1);
            writer.write("Hello, Sakura");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Save OK2", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("brad", e.toString());
        }

    }

    public void test3(View view) {
        // INSERT INTO sakura (cname,tel,birthday) VALUES ("brad", "0912-123456","1999-01-02");
        ContentValues values = new ContentValues();
        values.put("cname", "brad");
        values.put("tel", "0912-123456");
        values.put("birthday", "1999-01-02");
        database.insert("sakura", null, values);
    }

    public void test4(View view) {
        // SELECT * FROM sakura
        Cursor cursor = database.query(
                "sakura",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String cname = cursor.getString(cursor.getColumnIndex("cname"));
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            Log.v("brad", id + ":"+ cname + ":" + tel + ":" + birthday);
        }

    }

//    File file = new File(dcimPath, "Camera/IMG_20180808_171602.jpg");
//    Uri contentUri = FileProvider.getUriForFile(this,
//            "tw.brad.myfileio",
//            file);
//        Log.v("brad", contentUri.getPath());
//        img.setImageURI(contentUri);


    public void test5(View view) {
        // DELECT FROM sakura WHERE id = 3 AND cname = 'brad'
        database.delete("sakura", "id = ? AND cname = ?", new String[]{"3","brad"});
        test4(null);
    }

    public void test6(View view) {
        // UPDATE sakua SET cname = 'peter', tel = '321' WHERE id = 5;
        ContentValues values = new ContentValues();
        values.put("cname", "peter");
        values.put("tel", "321");
        database.update("sakura", values, "id = ?", new String[]{"5"});
        test4(null);
    }
}
