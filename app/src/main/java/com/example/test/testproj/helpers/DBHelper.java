package com.example.test.testproj.helpers;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class which extends {@link SQLiteOpenHelper}'s pages and
 * uses local database {@link assets/showstore.db}
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static final String DATABASE_NAME = "costore.db"; // name of db
    private static final int SCHEMA = 1; // version
    public static final String TABLE = "offers"; // name of table
    // column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_CURRENCYID = "currencyId";
    public static final String COLUMN_STOC_QUANTITY = "stock_quantity";
    public static final String COLUMN_CATEGORY_ID = "categoryId";
    public static final String COLUMN_CATEGORY_PARENT_ID = "category_parentId";
    public static final String COLUMN_VENDOR = "vendor";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PARAMS_XML = "params_xml";
    private Context myContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DATABASE_NAME;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       /* db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IMAGE + "TEXT,"
                + COLUMN_URL + "TEXT,"
                + COLUMN_RATE + "REAL,"
                + COLUMN_FAVORITE + "INTEGER);")
        ;
        // insert test data
        db.execSQL("INSERT INTO " + TABLE + " ("
                + COLUMN_NAME
                + "," + COLUMN_DESCRIPTION + ","
                + COLUMN_IMAGE + ","
                + COLUMN_URL + ","
                + COLUMN_RATE + ","
                + COLUMN_FAVORITE + ") VALUES ('FirstShow', 'The first show in database','http://static.tvmaze.com/uploads/images/original_untouched/31/78286.jpg','http://static.tvmaze.com/uploads/images/original_untouched/31/78286.jpg',6.7, 1);");
    */
    }

    public void create_db() {
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                //get local database like a thread
                myInput = myContext.getAssets().open(DATABASE_NAME);
                // path to new database
                String outFileName = DB_PATH;

                // opening an empty database
                myOutput = new FileOutputStream(outFileName);

                // copying data byte to byte
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        } catch (IOException ex) {
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    public SQLiteDatabase open() throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}